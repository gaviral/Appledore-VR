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

/*
 * To create a screen add it bellow
 * 	+ it's calling code in render_screen() below
 */
//***********************  WELCOME SCREEN  ***************************

int current_screen_num = 1;
volatile int * KEY_ptr = (int *) PUSHBUTTONS_BASE_ADDR;
volatile int * slider_switch_ptr = (int *) SWITCHES_BASE_ADDR;
int * red_leds_ptr = (int *) LEDS_BASE_ADDR;

// global var for holding push button info
volatile int pushbutton_pressed = NOKEY;


int main() {
	//change_pallette(DEFAULT_PALLETTE);
	gen_palette();
	init_touch();
	init_gps();
	controller_init();
	init_bluetooth();

	// TODO: Temporary, need to retrieve user id from Android and send it via BT
	char *user_id = "test_id";

	int press;
	char command[MAX_CMD_SIZE];

	char key_char = 'x';
	char last_char = 'x';

	int started = 0;
	int post_check = 0;

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

		/* PUSHBUTTON AND BLUETOOTH CODE */
		*(red_leds_ptr) = *(slider_switch_ptr);
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
