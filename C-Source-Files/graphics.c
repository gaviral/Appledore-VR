#include "graphics.h"
#include <string.h>


/**********************************************************************
* This function writes a single pixel to the x,y coords specified in the specified colour
* Note colour is a palette number (0-255) not a 24 bit RGB value
**********************************************************************/
void WriteAPixel (int x, int y, int Colour)
{
	WAIT_FOR_GRAPHICS;			// is graphics ready for new command

	GraphicsX1Reg = x;			// write coords to x1, y1
	GraphicsY1Reg = y;
	GraphicsColourReg = Colour;		// set pixel colour with a palette number
	GraphicsCommandReg = PutAPixel;		// give graphics a "write pixel" command
}

/*****************************************************************************************
* This function read a single pixel from x,y coords specified and returns its colour
* Note returned colour is a palette number (0-255) not a 24 bit RGB value
******************************************************************************************/
int ReadAPixel (int x, int y)
{
	WAIT_FOR_GRAPHICS;			// is graphics ready for new command

	GraphicsX1Reg = x;			// write coords to x1, y1
	GraphicsY1Reg = y;
	GraphicsCommandReg = GetAPixel;		// give graphics a "get pixel" command

	WAIT_FOR_GRAPHICS;			// is graphics done reading pixel
	return (int)(GraphicsColourReg) ;		// return the palette number (colour)
}

/****************************************************************************************************
** subroutine to program a hardware (graphics chip) palette number with an RGB value
** e.g. ProgramPalette(RED, 0x00FF0000) ;
****************************************************************************************************/

void ProgramPalette(int PaletteNumber, int RGB)
{
    WAIT_FOR_GRAPHICS;
    GraphicsColourReg = PaletteNumber;
    GraphicsX1Reg = RGB >> 16   ;          // program red value in ls.8 bit of X1 reg
    GraphicsY1Reg = RGB ;                	 // program green and blue into 16 bit of Y1 reg
    GraphicsCommandReg = ProgramPaletteColour;	// issue command
}

/*********************************************************************************************
This function draw a horizontal line, 1 pixel at a time starting at the x,y coords specified
*********************************************************************************************/
void HLine(int x1, int x2, int y, int Colour)
{
	// swap x1 and x2 if they're backwards
	if(x1 > x2) {
		int temp = x1;
		x1 = x2;
		x2 = temp;
	}
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x1;			// write coords to x1, x2, y1
	GraphicsX2Reg = x2;
	GraphicsY1Reg = y;
	GraphicsColourReg = Colour;		// set pixel colour with a palette number
	GraphicsCommandReg = DrawHLine;		// give graphics a "draw horizontal line" command
}

/*********************************************************************************************
This function draw a vertical line, 1 pixel at a time starting at the x,y coords specified
*********************************************************************************************/
void VLine(int y1, int y2, int x, int Colour)
{
	// swap y1 and y2 if they're backwards
	if(y1 > y2) {
		int temp = y1;
		y1 = y2;
		y2 = temp;
	}
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x;			// write coords to x1, x2, y1
	GraphicsY1Reg = y1;
	GraphicsY2Reg = y2;
	GraphicsColourReg = Colour;		// set pixel colour with a palette number
	GraphicsCommandReg = DrawVLine;		// give graphics a "draw horizontal line" command
}

void Line(int x1, int y1, int x2, int y2, int Colour)
{
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x1;			// write coords to x1, x2, y1, y2
	GraphicsX2Reg = x2;
	GraphicsY1Reg = y1;
	GraphicsY2Reg = y2;
	GraphicsColourReg = Colour;		// set pixel colour with a palette number
	GraphicsCommandReg = DrawLine;		// give graphics a "draw horizontal line" command
}

// Draws a rectangle starting at x,y and extends to width in the x and height in the y
void Rectangle(int x, int y, int width, int height, int Colour) {
	VLine(y, y+height, x, Colour);
	VLine(y, y+height, x+width, Colour);
	HLine(x, x+width, y, Colour);
	HLine(x, x+width, y+height, Colour);
}

void FilledRectangleBordered(int x, int y, int width, int height, int borderColour, int fillColour) {
	// Draw the border
	Rectangle(x, y, width, height, borderColour);
	// Draw horizontal lines to fill the rectangle
	int i;
	for(i=y+1; i<y+height; i++) {
		HLine(x+1, x+width-1, i, fillColour);
	}
}

void FilledRectangle(int x, int y, int width, int height, int colour) {
	int i;
	for(i=y; i<=y+height; i++) {
		HLine(x, x+width, i, colour);
	}
}

void Triangle(int x1, int y1, int x2, int y2, int x3, int y3, int Colour) {
	Line(x1, y1, x2, y2, Colour);
	Line(x1, y1, x3, y3, Colour);
	Line(x2, y2, x3, y3, Colour);
}

void DrawString(const char *str, int x, int y, int fontColour) {
	int currentX = x;
	int charIndex = 0;
	while(str[charIndex] != '\0') {
		OutGraphicsCharFont2a(currentX, y, fontColour, WHITE, str[charIndex], 0);
		currentX += 10 + FONT_SPACING; // 10 is the x size of each character in the font
		charIndex++;
	}
}

void DrawRectangleWithString(const char *str, int x, int y, int width, int height,
		int rectangleColour, int strColour, int fill) {

	// font size is 10x14
	int FONT_WIDTH = 10;
	int FONT_HEIGHT = 14;
	int strPixelWidth = (strlen(str) + FONT_SPACING) * FONT_WIDTH - FONT_SPACING;

	if(fill) {
		//FilledRectangle(x, y, width, height, rectangleColour);
		FilledRectangleBordered(x, y, width, height, strColour, rectangleColour);
	}
	else {
		Rectangle(x, y, width, height, rectangleColour);
	}

	// start the string at half the total space between the rectangle edge and the text string,
	// and center it by the height of the rectangle and the font height
	DrawString(str, x + (width - strPixelWidth) / 2, y + (height - FONT_HEIGHT) / 2, strColour);
}

void DrawCircle(int x0, int y0, int radius, int colour) {
    int x = radius;
    int y = 0;
    int err = 0;

    while (x >= y) {
    	WriteAPixel(x0 + x, y0 + y, colour);
    	WriteAPixel(x0 + y, y0 + x, colour);
    	WriteAPixel(x0 - y, y0 + x, colour);
    	WriteAPixel(x0 - x, y0 + y, colour);
    	WriteAPixel(x0 - x, y0 - y, colour);
    	WriteAPixel(x0 - y, y0 - x, colour);
    	WriteAPixel(x0 + y, y0 - x, colour);
    	WriteAPixel(x0 + x, y0 - y, colour);

        if (err <= 0) {
            y += 1;
            err += 2*y + 1;
        }
        if (err > 0) {
            x -= 1;
            err -= 2*x + 1;
        }
    }
}
