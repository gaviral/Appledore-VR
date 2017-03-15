#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>
#include <unistd.h>
#include "gps.h"
#include "bluetooth.h"
#include "key_codes.h"
#include "db_send_gps.h"
#include "uncategorized.h" // just for the definition of NULL for now


// a simple delay function
void delay(unsigned int mseconds) {
    clock_t goal = mseconds + clock();
    while (goal > clock());
}

// function to initialize Bluetooth
void init_bluetooth(void) {
	// Program 6850 and baud rate generator to communicate with Bluetooth
	Bluetooth_Control = 0x15;
	Bluetooth_Baud = 0x01;
}

// function to send a command via Bluetooth to a connected device
void bt_send_command(char *cmd) {
	printf("%s ", cmd);
	int i = 0;
	while (cmd[i] != '\0') {
		if ((Bluetooth_Status & Tx_Mask)) {
			Bluetooth_TxData = cmd[i];
			//delay(5);
			i++;
		}
	}
}

// function to translate inputs into navigation commands
void translate_command(int input, char *command) {
	char cmd = 0;
	switch (input) {
		case KEY1:
			cmd = RIGHT;
			break;
		case KEY2:
			cmd = FORWARD;
			break;
		case KEY3:
			cmd = LEFT;
			break;
		default:
			break;
	}

	command[0] = cmd;

	// add the carriage return and new line
	// APPARENTLY, this is not required
	//strcat(command, CR_NL);
}
