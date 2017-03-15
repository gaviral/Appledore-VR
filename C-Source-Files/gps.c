#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "gps.h"
#include "uncategorized.h"

// initialize GPS Connection to the NIOS
void init_gps(void) {
	GPS_Baud = 0x05;
	GPS_Control = 0x15;
}

// write a char into the GPS
void put_char_gps(int c) {
	while (!(GPS_Status & 0x2));
    GPS_TxData = c;
}

// read a char from the GPS
char get_char_gps(void) {
	while (!(GPS_Status & 0x1));
	return GPS_RxData;
}

// this function reads one line at a time
void gps_read_line(char buffer[]) {
	int count = 0;
	char gpsChar = '\0';
	while(gpsChar != '$') {
		gpsChar = get_char_gps();
	}
	while(gpsChar != '\n') {
		gpsChar = get_char_gps();
		buffer[count] = gpsChar;
		count++;
	}
	buffer[count] = '\0';
}

// reading data from GPS
void gps_received_data(void) {
	init_gps();
	char charBuffer[100];
	gps_data myData;
	gps_read_line(charBuffer);
	while(check_line(charBuffer) == 0){
		gps_read_line(charBuffer);
	}
	raw_data(myData);
	gps_parse_line(charBuffer, &myData);
}

// function to get new and real gps data
void new_gps(gps_data *global, int current[]) {
	init_gps();
	char charBuffer[100];
	gps_read_line(charBuffer);
	while(check_line(charBuffer) == 0){
	 gps_read_line(charBuffer);
	}
	gps_parse_line(charBuffer, global);
	char h0 = global->time[0];
	int h0i = atoi(&h0);
	char h1 = global->time[1];
	int h1i = atoi(&h1);
	int hour = h0i * 10 + h1i;
	// get minute
	char m0 = global->time[2];
	int m0i = atoi(&m0);
	char m1 = global->time[3];
	int m1i = atoi(&m1);
	int minute = m0i * 10 + m1i;
	 // get second
	char s0 = global->time[4];
	int s0i = atoi(&s0);
	char s1 = global->time[5];
	int s1i = atoi(&s1);
	int second = s0i * 10 + s1i;

	current[0] = hour-8+24;
	current[1] = minute;
	current[2] = second;
}

// this function checks if the line is what we want (GPGGA)
int check_line(char buffer[]) {
	int result = 0;
	if(buffer[3]== 'G' && buffer[2]== 'G')
	result = 1;
	return result;
}

// this function parses the char line data into GPS data
void gps_parse_line(char line[], gps_data *currentData) {
	const char s[12] = ",";
	char tempBuffer[16][12];
	int num = 0;
	char *token;
	/* get the first token */
	token = strtok(line, s);
	/* walk through other tokens */
	while( token != NULL ) {
		strcpy(tempBuffer[num], token);
		token = strtok(NULL, s);
		num++;
	}

	strncpy(currentData->ID, tempBuffer[0], 6);
	strncpy(currentData->time, tempBuffer[1], 11);

	// TODO CORRECT LONG/LAT BASED ON CARDINAL DIRECTION
	double floatLat = atof(tempBuffer[2]);
	floatLat /= 100;
	int intPartLat = (int) floatLat;
	double decPartLat = (floatLat - intPartLat)*100/60;
	floatLat = intPartLat + decPartLat;

	double floatLong = atof(tempBuffer[4]);
	floatLong /= 100;
	int intPartLong = (int) floatLong;
	double decPartLong = (floatLong - intPartLong)*100/60;
	floatLong = intPartLong + decPartLong;

	currentData->latitude = floatLat;
	currentData->longitude = floatLong;
	strncpy(currentData->NS, tempBuffer[3], 2);
	strncpy(currentData->EW, tempBuffer[5], 2);
	strncpy(currentData->position,tempBuffer[6], 2);
	currentData->numSat = atoi(tempBuffer[7]);
	strncpy(currentData->HDOP, tempBuffer[8], 5);
	currentData->alt = atof(tempBuffer[9]);
	strncpy(currentData->altUnit, tempBuffer[10], 2);
	currentData->geo = atof(tempBuffer[11]);
	strncpy(currentData->geoUnit, tempBuffer[12], 2);

	if(currentData->EW[0] == 'W')
	   currentData->longitude = -1*(currentData->longitude);
	if(currentData->NS[0] == 'S')
	   currentData->latitude = -1*(currentData->latitude);
}

void raw_data(gps_data rawData) {
	rawData.ID[6] = "GPGGA";
	rawData.time[11] = '0';
	rawData.latitude = 0;
	rawData.longitude = 0;
	rawData.NS[2] = '0';
	rawData.EW[2] = '0';
	rawData.position[2] = '0';
	rawData.numSat = 0;
	rawData.HDOP[5] = '0';
	rawData.alt = 0;
	rawData.altUnit[2] = '0';
	rawData.geo = 0;
	rawData.geoUnit[2] = '0';
}

void get_time(int current[]) {
	char charBuffer[100];
	gps_data myData;
	gps_read_line(charBuffer);
	while(check_line(charBuffer) == 0) {
		gps_read_line(charBuffer);
	}
	gps_parse_line(charBuffer, &myData);
	// get hour
    char h0 = myData.time[0];
    int h0i = atoi(&h0);
    char h1 = myData.time[1];
    int h1i = atoi(&h1);
    int hour = h0i * 10 + h1i;

    // get minute
    char m0 = myData.time[2];
    int m0i = atoi(&m0);
    char m1 = myData.time[3];
    int m1i = atoi(&m1);
    int minute = m0i * 10 + m1i;

    // get second
    char s0 = myData.time[4];
    int s0i = atoi(&s0);
    char s1 = myData.time[5];
    int s1i = atoi(&s1);
    int second = s0i * 10 + s1i;

    current[0] = hour+24;
    current[1] = minute;
    current[2] = second;
}

void get_gps_data(gps_data_struct *cur_gps) {
	gps_data gpsData;
	int gpsTime[3];
	new_gps(&gpsData, gpsTime);
	cur_gps->latitude = gpsData.latitude;
	cur_gps->longitude = gpsData.longitude;
	cur_gps->time.hh = gpsTime[0];
	cur_gps->time.mm = gpsTime[1];
	cur_gps->time.ss = gpsTime[0];
}

