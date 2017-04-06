#include <stdio.h>
#include <stdlib.h>
#include "stdbool.h"
#include <string.h>
#include <time.h>
#include <math.h>
#include "touchscreen.h"
#include "uncategorized.h"
#include "Colours.h"
#include "bmp.h"
#include "map.h"
#include "screen.h"
#include "bluetooth.h"
#include "key_codes.h"
#include "db_send_gps.h"
#include "gps.h"
#include "adc.h"

#define BLUETOOTH_RECEIVE_SIZE 40
#define SKIP_USER_ID 1
#define TEST_ID "testid"

/*
 * To create a screen add it bellow
 * 	+ it's calling code in render_screen() below
 */
//***********************  WELCOME SCREEN  ***************************

int current_screen_num = 1;
volatile int * KEY_ptr = (int *) PUSHBUTTONS_BASE_ADDR;
volatile int * slider_switch_ptr = (int *) SWITCHES_BASE_ADDR;
int * red_leds_ptr = (int *) LEDS_BASE_ADDR;
char adc_command[6];

// global variable for holding push button info
volatile int pushbutton_pressed = NOKEY;


int main() {
	//change_pallette(DEFAULT_PALLETTE);
	gen_palette();
	init_touch();
	init_gps();
	controller_init();
	init_bluetooth();

	// user_id to be stored for http patch
	char user_id[BLUETOOTH_RECEIVE_SIZE]; // allocate 40 characters for it
	strcpy(user_id, TEST_ID);

	int press;
	char command[MAX_CMD_SIZE];

	char key_char = 'x';
	char last_char = 'x';

	int started = 0;
	int post_check = 0;

	int adc_x = 511, adc_y = 511; // 511 is the "middle" idling value
	int adc_x_prev = -1, adc_y_prev = -1; // the previous values of the adc readings
	int stick_button_prev = -1; // the previous value of the stick button; set to impossible value by default

	/* USER ID RETRIEVING CODE */
	// This will block until we receive a (hopefully valid) user id, and a null character from bluetooth
	if(SKIP_USER_ID) {
		printf("Skipping user id\n");
	}
	else {
		// sometimes we get an erroneous empty string returned, so if that's the case just keep waiting
		while(user_id[0] == '\0' || strcmp(user_id, TEST_ID) == 0) {
			bt_read_command(user_id);
			printf("\nGot user id:%s\n", user_id);
		}
		send_gps_data(user_id);
	}

	/* Main while loop */
	while(1) {
		/* TOUCHSCREEN DETECTION CODE */
		if(screen_touched()) {
			if (TouchScreen_RxData == 0x80) {
				get_release();
			} else if (TouchScreen_RxData == 0x81) {
				get_press();
			}
		}

		/* GPS DATABASE CHECK/PATCH CODE */
		// function call for checking time intervals
		post_check = http_patch_check(user_id, &started);
		if (post_check == 0) {
			started = 1;
		}
		else {
			started = 0;
		}

		/* SPI READING FROM THE ADC CODE */
		adc_x = adc_read_channel(0);
		adc_y = adc_read_channel(1);

		// only send the value if it's different from the last one
		if(adc_x > adc_x_prev + 1 || adc_x < adc_x_prev - 1) {
			adc_x_prev = adc_x;
			format_int_for_bluetooth(adc_x, adc_command, 'x');
			bt_send_command(adc_command);
		}

		// only send the value if it's different from the last one
		if(adc_y > adc_y_prev + 1 || adc_y < adc_y_prev - 1) {
			adc_y_prev = adc_y;
			format_int_for_bluetooth(adc_y, adc_command, 'y');
			bt_send_command(adc_command);
		}

		// check the stick button, and only send it once when it's been clicked
		if(STICK_BUTTON != stick_button_prev) {
			stick_button_prev = STICK_BUTTON;
			if(!STICK_BUTTON) {
				printf("\n");
				sprintf(adc_command, "b");
				printf("\n");
				bt_send_command(adc_command);
			}
		}

		/* PUSHBUTTON AND BLUETOOTH CODE */
		*(red_leds_ptr) = *(slider_switch_ptr); // <- VERY IMPORTANT CODE, DO NOT CHANGE
		press = *KEY_ptr;

		if (press == KEY1_V) {
			pushbutton_pressed = KEY1;
			key_char = '1';
		} else if (press == KEY2_V) {
			pushbutton_pressed = KEY2;
			key_char = '2';
		} else if (press == KEY3_V) {
			pushbutton_pressed = KEY3;
			key_char = '3';
		} else if (press == NOKEY_V || press == 0) {
			pushbutton_pressed = NOKEY;
			key_char = 'x';
			//last_char = 'x';
		}

		// if the key is the same key as last time (ie. key is held down), skip the print
		if(key_char == last_char) continue;

		if (pushbutton_pressed != NOKEY) {
			// translate the pushbutton into navigation command
			translate_command(pushbutton_pressed, command);
			// send this translated command to Android via BT
			bt_send_command(command);
			// clear the string after sending it
			memset(&command[0], 0, MAX_CMD_SIZE);
		}
		else if (last_char != key_char) {
			bt_send_command(STOP);
		}
		last_char = key_char;
	}

	return 0;
}
