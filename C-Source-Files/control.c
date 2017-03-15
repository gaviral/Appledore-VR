#include "gps.h"
#include "graphics.h"
#include "touchscreen.h"
#include <stdio.h>
#include <math.h>

void init_draw() {
	int y;
	// clear the screen
	for(y=0; y<YRES; y++) {
		HLine(0, XRES-1, y, WHITE);
	}

	DrawRectangleWithString("Button", 50, 50, 200, 100, BLUE, BLACK, TRUE);
}

//returns data in km
Displacement gps_to_grid(double origin_lon, double origin_lat, double point_lon, double point_lat) {
	Displacement p1;

	printf("olong: %0.4f, olat: %0.4f plong: %0.4f, plat: %0.4f\n", origin_lon, origin_lat, point_lon, point_lat);
	p1.x = (point_lon - origin_lon)*40000.0*cos((origin_lat + point_lat)*M_PI/360.0)/360.0;
	p1.y = (origin_lat - point_lat)*40000.0/360.0;

	printf("dx: %0.4f, dy: %0.4f\n", p1.x, p1.y);
	return p1;
}

Point grid_to_map(Map map, double dx, double dy) {
	Point p1;
//	int origin_x = map.width/2 + map.p.x;
//	int origin_y = map.height/2 + map.p.y;

		int origin_x = 500;
		int origin_y = 300;

	//1px = 1m
	p1.x = origin_x + 500.0*dx;
	p1.y = origin_y - 500.0*dy;
	printf("px: %d, py: %d\n", p1.x, p1.y);
	return p1;

}

int gps_has_data() {
	return (GPS_Status & Rx_Mask);
}
