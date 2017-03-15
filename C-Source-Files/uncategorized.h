#include "graphics.h"
//#include <stdbool.h>

//#define ROADMAP 0
//#define SATELLITE 1
//#define TERRAIN 2
//#define HYBRID 3

#define PRESSED_BUTTON_COLOR CYAN
#define UNpressed_BUTTON_COLOR WHITE

#define ROUTE_1 1
#define ROUTE_2 2

#define NUM_OF_MARKERS 2

extern const unsigned int ColourPalletteData[256];
extern const unsigned int mapColourPalletteData[256];

#define DEFAULT_PALLETTE 0
#define MAP_PALLETTE 1

// temporary definition for NULL (FFS)
// TODO: REMOVE THESE IF YOU DON'T HAVE THESE ISSUES
#define NULL ((void *) 0)

typedef struct {
	double start_x;
	double start_y;
	char file_name[10];
	int current_map_number;
	//todo: add user_contact for emergency
} Demo_Map;
