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
#include "gps.h"
#include "map.h"
#include "screen.h"
#include "graphics.h"
#include "bluetooth.h" // for the delay function

struct Buttons screen1[13] = {
		{"SPRINT 1 DEMO TUTORIAL", { 0, 0 }, 800, 100, YELLOW, CRIMSON, 1 },
		{"Left", { 100, 300 }, 150, 100, CRIMSON, LIME, 1 },
		{"Forward", { 250, 300 }, 150, 100, CRIMSON, LIME, 1 },
		{"Right", { 400, 300 }, 150, 100, CRIMSON, LIME, 1 },
		{"Reset", { 550, 300 }, 150, 100, CRIMSON, LIME, 1 },
		{"KEY3", { 100, 240 }, 150, 60, LIME, BLACK, 1 },
		{"KEY2", { 250, 240 }, 150, 60, LIME, BLACK, 1 },
		{"KEY1", { 400, 240 }, 150, 60, LIME, BLACK, 1 },
		{"KEY0", { 550, 240 }, 150, 60, LIME, BLACK, 1 },
		{"", { 150, 175 }, 50, 50, 252, BLACK, 1 },
		{"", { 300, 175 }, 50, 50, 252, BLACK, 1 },
		{"", { 450, 175 }, 50, 50, 252, BLACK, 1 },
		{"", { 600, 175 }, 50, 50, 252, BLACK, 1 }
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
		HLine(0, XRES - 1, i, BLACK);
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

// TODO: remove these after sprint 1
void render_circle(int i) {
	switch (i) {
		case 0:
			circle_helper(175, 200);
			break;
		case 1:
			circle_helper(325, 200);
			break;
		case 2:
			circle_helper(475, 200);
			break;
		case 3:
			circle_helper(625, 200);
			break;
		default:
			break;
	}
}

void circle_helper(int x0, int y0) {
	int j;
	for (j = 18; j > 0; j--) {
		DrawCircle(x0, y0, j, BLACK);
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
		// TODO: Added the circle stuff here, remove when the time comes
		for (i = 0; i < 4; i++) {
			render_circle(i);
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
