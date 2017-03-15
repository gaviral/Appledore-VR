#include <stdio.h>
#include <stdlib.h>
#include "stdbool.h"
#include <string.h>
#include <time.h>
#include "touchscreen.h"
#include "uncategorized.h"
#include "Colours.h"
#include "bmp.h"
#include "gps.h"
#include "map.h"
#include <math.h>
#include "screen.h"
#include "bluetooth.h" // for the delay function

struct Buttons screen1[3] ={
		{"Left", { 100, 240 }, 200, 100, LIME, BLUE, 1 },
		{"Right", { 500, 240 }, 200, 100, LIME, BLUE, 1 },
		{"Forward", { 300 , 100 }, 200, 100, LIME, BLUE, 1 }
		//,{"CURRENT_TIME", { 300, 190 }, 75, 50, LIME, BLUE, 0 }
};

extern int current_screen_num;

void change_pallette(int pallette_num){
	int index;

	switch(pallette_num){
	case 0:
		for(index=0;index<256;index++){
			ProgramPalette(index, ColourPalletteData[index]);
		}
		break;
	case 1:
		for(index=0;index<256;index++){
			ProgramPalette(index, mapColourPalletteData[index]);
		}
		break;
	}
}

/* **********************************************************
 * VIEW
 */

/*
 * Clears the screen
 */
void clear_screen() { // clear the screen
	int i;
	for (i = 0; i < YRES; i++) {
		HLine(0, XRES - 1, i, WHITE);
	}
}/*
 * Renders/clears button sent based on render parameter
 */
void render_button(struct Buttons button, int render) {
	if(render){
		DrawRectangleWithString(button.text, button.origin.x, button.origin.y,
				button.width, button.height, button.color, button.strColor,
				button.fill);
	}else{
		DrawRectangleWithString(button.text, button.origin.x, button.origin.y,
				button.width, button.height, WHITE, WHITE, 1);
	}
}

/*
 * Renders the new screen based on screen_num parameter
 */
void render_screen(int screen_num, int render) {
	int i;
	switch (screen_num) {
	case 1:
		current_screen_num = 1;
		for (i = 0; i < (sizeof(screen1) / sizeof(struct Buttons)); i++) {
			render_button(screen1[i], render);
		}
		break;
	}
}

/*
 * Check if the button is pressed
 */
bool button_pressed(struct Buttons button, Point touch_point) {
	int locality_offset = 0;

	//check left
	if (touch_point.x > button.origin.x - locality_offset) {

		//check right
		if (touch_point.x < button.origin.x + button.width + locality_offset) {

			//check top
			if (touch_point.y > button.origin.y - locality_offset) {

				//check bottom
				if (touch_point.y
						< button.origin.y + button.height + locality_offset) {

					return (true);
				}
			}
		}
	}
	return (false);
}

/*
 * This function is called everytime a screen-touch is detected
 *
 * This is where button-press events are called.
 */
void any_button_pressed(Point touch_point) {
	int i;
	switch(current_screen_num){
		case 1:
			if (button_pressed(screen1[1], touch_point)) { //begin button on screen1
				//change_button_color(&screen1[1], PRESSED_BUTTON_COLOR);
			}else if (button_pressed(screen1[2], touch_point)) { //Old Runs button on screen1
				//change_button_color(&screen1[2], PRESSED_BUTTON_COLOR);
			}
			break;
		default:
			break;
	}
}

void change_button_color(struct Buttons *button, int new_color) {
	struct Buttons button_tmp;
	button_tmp = *button;
	button_tmp.color = new_color;
	*button = button_tmp;
	printf("pressed %d ",PRESSED_BUTTON_COLOR);
	printf("button color: %d\n",(*(button)).color);
	render_button(*button, 1);//todo: this should not directly call a view function
}

void controller_init() {
	clear_screen();
	render_screen(1, 1);
}
