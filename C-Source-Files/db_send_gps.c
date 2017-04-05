#include "gps.h"
#include "sys/alt_timestamp.h"
#include "db_send_gps.h"
#include "uncategorized.h"
#include "bluetooth.h" /* bluetooth.h is for the delay function and Rx/Tx masks */
#include <stdio.h>
#include <string.h>

// the struct that will hold the GPS data
gps_data_struct *cur_gps;

// function for formatting the JSON content to be sent as data
void format_content(double lat, double lng, char *content) {
	sprintf(content, CONTENT_STR, lat, lng);
}

// function that returns the length of a string
int get_content_len(char *content) {
	int i = 0;
	while (content[i] != '\0') {
		i++;
	}
	return i;
}

// function that sends the PATCH request to the database with the recent GPS data
void http_patch(char *user_id, double lat, double lng) {
	char str[MAX_STR_LEN];
	char content[MAX_CONTENT_LEN];

	// format the JSON content as a string
	format_content(lat, lng, content);
	int length = get_content_len(content);

	// TODO: Not necessary to do it every time probably
	init_wifi();

	put_string_wifi(DOFILE_CONNECT);
	delay(250);
	// this is apparently needed
	put_string_wifi(CR_NL);

	put_string_wifi(DOFILE_PATCH);
	delay(250);
	put_string_wifi(CR_NL);

	// prepare the string to call http_patch() in Lua script
	sprintf(str, HTTP_PATCH, user_id, content, length);
	printf(str);

	put_string_wifi(str);
	delay(250);
	put_string_wifi(CR_NL);

	// done with WiFi
	// TODO: as before, might not be required
	put_string_wifi(OFF_LED);
}

// function to check the time interval for patching to database
int http_patch_check(char *user_id, int *started) {
	if(user_id == NULL) return 0;

	int freq;
	unsigned long cycles;
	float duration = 0;

	// get the frequency of the timer
	freq = alt_timestamp_freq();

	// if we have not started, reset the timer
	if (*started == 0) {
		alt_timestamp_start();
		// we have started
		*started = 1;
		// reset cycles just in case
		cycles = 0;
	}
	else {
		cycles = (unsigned long) alt_timestamp();
	}
	// calculate how long it has been since last patch
	duration = (float) cycles / (float) freq;

	if (duration < TIME_INTERVAL_SEC) {
		return 0;
	}
	else {
		// if TIME_INTERVAL_SEC seconds have passed, patch to database
		send_gps_data(user_id);
		*started = 0;
		return 1;
	}
}

void send_gps_data(char *user_id) {
	get_gps_data(cur_gps);
	// check for a signal, if there isn't any, don't send data
	if (cur_gps->latitude != 0 && cur_gps->longitude != 0)
		http_patch(user_id, cur_gps->latitude, cur_gps->longitude);
}

// function to initialize the WiFi
void init_wifi(void) {
	Wifi_Control = 0x15;
	Wifi_Baud = 0x01;

	put_string_wifi(SET_LED);
	put_string_wifi(LIGHT_LED);
	printf("Wifi module initialized, LED should turn on\n");
}

// function to send a string to the WiFi module
int put_string_wifi(const char* string) {
	int i;
	for (i=0; string[i] != '\0'; i++) {
		put_char_wifi(string[i]);
	}
	return 0;
}

// helper function for put_string_wifi that sends a char
int put_char_wifi(int c) {
	while (!(Wifi_Status & 0x2));
	Wifi_TxData = c;
	return 0;
}


