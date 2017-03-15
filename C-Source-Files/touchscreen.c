#include <stdio.h>
#include <stdlib.h>
#include "stdbool.h"
#include "touchscreen.h"
#include "graphics.h"
#include "screen.h"
#include "uncategorized.h"

/*****************************************************************************
** Initialise touch screen controller
*****************************************************************************/
void init_touch(void)
{
 // Program 6850 and baud rate generator to communicate with touchscreen
 // send touchscreen controller an "enable touch" command
	TouchScreen_Control = 0x15;
	TouchScreen_Baud = 0x05;

	while (!(TouchScreen_Status & Tx_Mask));
		TouchScreen_TxData = 0x55;

	while (!(TouchScreen_Status & Tx_Mask));
		TouchScreen_TxData = 0x01;

	while (!(TouchScreen_Status & Tx_Mask));
		TouchScreen_TxData = 0x12;
}

/*****************************************************************************
** test if screen touched
*****************************************************************************/
int screen_touched( void )
{
 // return TRUE if any data received from 6850 connected to touchscreen
 // or FALSE otherwise
	return (TouchScreen_Status & Rx_Mask);
}

/*****************************************************************************
** wait for screen to be touched
*****************************************************************************/
void wait_for_touch()
{
	while(!screen_touched());
}

int call_draw_rectangle_with_string(struct Buttons button){
	DrawRectangleWithString(button.text, button.origin.x, button.origin.y, button.width, button.height, button.color, button.strColor, button.fill);
	printf("DEBUG: called DrawRect\n");
	//FilledRectangle(button.origin.x, button.origin.y, button.width, button.height, button.color);
	return 0;
}

/************************ Definitions for swiping ****************************/
#define SWIPE_LIM_X (int) 100
#define SWIPE_LIM_Y (int) 150
#define SWIPE_LIM_TIME (float) 1.5

/*************** Global variables for swiping (temporary) ********************/
//unsigned long start_time, end_time;
Point touch_start, touch_end;
bool first_touch = true;
bool weird_touch = false;

/********************* Function to check for swipes **************************/
bool user_swiped(unsigned long start_time, unsigned long end_time,
				Point touch_start, Point touch_end, bool weird_touch) {
	float time_taken;
	int x_diff, x_diff_abs, y_diff;

	//time_taken = (float)(end_time-start_time)/(float)(alt_timestamp_freq());
	time_taken = (float) end_time-start_time;

	x_diff = touch_end.x - touch_start.x;
	x_diff_abs = abs(x_diff);
	y_diff = abs(touch_end.y - touch_start.y);

	if (x_diff_abs >= SWIPE_LIM_X && y_diff <= SWIPE_LIM_Y && time_taken <= SWIPE_LIM_TIME && !weird_touch) {
		if (x_diff < 0) {
			printf("LEFT SWIPE DETECTED\n");
			//printScreen(NEXT);
		}
		else {
			printf("RIGHT SWIPE DETECTED\n");
			//printScreen(PREVIOUS);
		}
		return true;
	}

	return false;
}

/*****************************************************************************
* This function waits for a touch screen press event and returns X,Y coord
*****************************************************************************/
Point get_press(void)
{
	Point p1;
	int xOffset = 48;
	int yOffset = 138;

	// variable for weird swipe movements
	int weird_touch_diff;

	// wait for a pen down command then return the X,Y coord of the point
	//wait_for_touch();
	//while(TouchScreen_RxData != PenDown);
	int packet[4];
	int i;
	for (i = 0; i < 4; i++) {
		while (!(TouchScreen_Status & Rx_Mask));
		packet[i] = TouchScreen_RxData;
	}
	p1.x = (packet[1] << 7) | packet[0];
	p1.y = (packet[3] << 7) | packet[2];

	// calibrated correctly so that it maps to a pixel on screen
	p1.x = abs((int) ((p1.x - xOffset) * (800.0/(4096.0 - xOffset))));
	p1.y = abs((int) ((p1.y - yOffset) * (480.0/(4096.0 - yOffset))));

	//printf("Pressed x: %d, y: %d \n", p1.x, p1.y);
	//printf("p");
	/**************** stuff for checking weird swipe ****************/
	weird_touch_diff = abs(p1.y - touch_start.y);
	if (weird_touch_diff > SWIPE_LIM_Y) {
		weird_touch = true;
	}

	return p1;
}



/*****************************************************************************
* This function waits for a touch screen release event and returns X,Y coord
*****************************************************************************/
Point get_release(void)
{
	Point p1;
	int xOffset = 48;
	int yOffset = 128;

	// wait for a pen up command then return the X,Y coord of the point
	//wait_for_touch();
	//while(TouchScreen_RxData != PenUp);
	int packet[4];
	int i;
	for (i = 0; i < 4; i++) {
		while (!(TouchScreen_Status & Rx_Mask));
		packet[i] = TouchScreen_RxData;
	}
	p1.x = (packet[1] << 7) | packet[0];
	p1.y = (packet[3] << 7) | packet[2];

	//calibrated correctly so that it maps to a pixel on screen
	p1.x = abs((int) ((p1.x - xOffset) * (800.0/(4096.0 - xOffset))));
	p1.y = abs((int) ((p1.y - yOffset) * (480.0/(4096.0 - yOffset))));
	//printf("r");
	//printf("Released x: %d, y: %d \n", p1.x, p1.y);

	// ******************* stuff for swiping ********************
	if (first_touch) {
		touch_start = p1;
		any_button_pressed(p1);
		//start_time = (unsigned long) alt_timestamp();
		first_touch = false;
		weird_touch = false;
	}
	else {
		touch_end = p1;
		//end_time = (unsigned long) alt_timestamp();
		first_touch = true;
		user_swiped(0, 1, touch_start, touch_end, weird_touch);
		//user_swiped(start_time, end_time, touch_start, touch_end);
	}
	// *************** end of stuff for swiping ******************

	return p1;
}
