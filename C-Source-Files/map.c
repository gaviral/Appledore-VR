#include "map.h"
#include "graphics.h"
#include "nanojpeg.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "Colours.h"
#include "uncategorized.h"
#include "db_send_gps.h"

byte *current_map;

//extern gps_data_struct cur_gps;

extern char map_type[10];

void render_map(int x, int y) {
	/*int buf_offset;
	int file_size;

	double lat1 = current_trip.start_location.latitude;
	double long1 = current_trip.start_location.longitude;
	double lat2 = current_trip.end_location.latitude;
	double long2 = current_trip.end_location.longitude;
	double lat3 = cur_GPS.latitude;
	double long3 = cur_GPS.longitude;

	void *buf = download(&buf_offset, &file_size, lat1, long1, lat2, long2, lat3, long3);

	if(buf == NULL) {
		printf("Couldn't download map\n");
		return;
	}

	// if a current map exists, free it
	if(current_map) {
		free(current_map);
	}

	// nanojpeg stuff
	current_map = decode(buf, file_size);
	printf("h: %d, w: %d, size: %d\n", njGetHeight(), njGetWidth(), njGetImageSize());

	// don't need buf anymore
    free(buf - buf_offset);


    draw_map(current_map, njGetHeight(), njGetWidth(), x, y);

    njDone();
    return;*/
}

void gen_palette() {
	int i;
	for(i=0; i<256; i++) {
		byte red = i >> 6;
		byte green = (i & 0x38) >> 3;
		byte blue = (i & 0x07);
		int palette_entry = ((red << 22) + (green << 13) + (blue << 5));
		//printf("%x ", palette_entry);
		ProgramPalette(i, palette_entry);
	}
}

byte* convert_bits(byte *img) {
	int size = njGetImageSize()/3;
	byte * converted = malloc(size);
	int i;
	for (i = 0; i < size; i++) {
		int pix = i*3;
		byte red = img[pix] >> 6;
		byte green = img[pix+1] >> 5;
		byte blue = img[pix+2] >> 5;

		byte result = (red << 6) + (green << 3) + blue;
		converted[i] = result;
	}

	return converted;
}

byte *decode(void *buf, int size) {
	njInit();
    if (njDecode(buf, size)) {
        printf("Error decoding the input file.\n");
    }

    byte *decoded = njGetImage();
    byte *real_decoded = convert_bits(decoded);
    return real_decoded;
}

/* draw the bitmap, offset in x and y */
void draw_map(byte *bmp, int height, int width, int x_offset, int y_offset) {
	dword x, y;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			WriteAPixel(x_offset + x, y_offset + y, bmp[(y * width) + x]);
		}
	}
}

void *download(int *buf_offset, int *size, double lat1, double long1, double lat2, double long2, double lat3, double long3) {
	/*void *buffer = malloc(MAX_FILE_SIZE);

	Init_Wifi();
	delayWifi(1);

	putstringWifi(DOFILE_CONNECT);
	delayWifi(1);
	putstringWifi(DOFILE_PIC);
	delayWifi(1);

	char str[150];
	sprintf(str, "http_init_url(\"%s\", \"%f\", \"%f\", \"%f\", \"%f\", \"%f\", \"%f\")\r\n", "terrain", lat1, long1, lat2, long2, lat3, long3);
	printf(str);
	putstringWifi(str);

	char end_sequence[9] = "DONEdone";
	int seq_counter = 0;
	int count = 0;
	int timeout_count = 0;

	char c;

	while (1) {
		// check for timeout
		if(timeout_count >= TIMEOUT_MAX) {
			printf("reading from wifi timed out\n");
			free(buffer);
			return NULL;
		}
		timeout_count++;

		c = getcharWifi();
		((byte *)buffer)[count] = c;
		count++;

		// stop reading when we hit the end sequence
		if (c == end_sequence[seq_counter]) {
			seq_counter++;
			if (seq_counter == 8) break;
		}
		else {
			seq_counter = 0;
		}
	}
	printf("Done reading file from wifi\n");

	byte *jpg;
	int i = 0;
	// get rid of the garbage values in the beginning
	while(((byte *)buffer)[i] != 0xFF) {
		i++;
		count--;
	}
	jpg = ((byte *)buffer) + i;

	putstringWifi(OFF_LED);
	printf("Finished wifi main, LED should turn off\n");

	*buf_offset = i;
	*size = count;
	return jpg;*/
}
