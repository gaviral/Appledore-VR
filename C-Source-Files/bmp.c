#include "map.h"
#include "graphics.h"
#include "Colours.h"

/* load some data from the bitmap (extract a bit of data, and set a pointer) */
void load_bitmap(void *bitmap, struct BITMAP *b) {
	// b->pixelData will just point to the right location in bitmap (so don't deallocate bitmap)
	dword pixel_data_offset = BITMAP_DWORD_READ(BMP_PIXELOFFSETINFO_OFFSET); // find where pixel data starts
	printf("pixel start byte offset: %d\n", pixel_data_offset); // debug
	b->pixel_data = ((byte *)bitmap) + pixel_data_offset;

	b->width = BITMAP_DWORD_READ(BMP_WIDTH_OFFSET);
	b->height = BITMAP_DWORD_READ(BMP_HEIGHT_OFFSET);
}

/* draw the bitmap, offset in x and y */
void draw_bitmap(struct BITMAP *bmp, int x_offset, int y_offset) {
	dword x, y;
	for (y = 0; y < bmp->height; y++) {
		for (x = 0; x < bmp->width; x++) {
			WriteAPixel(x_offset + x, y_offset + y, bmp->pixel_data[((bmp->height - 1 - y) * bmp->width) + x]);
			//WriteAPixel(x_offset + x, y_offset + y, (byte)(((dword*)bmp->pixel_data)[((bmp->height - 1 - y) * bmp->width) + x] >> 24));
			//printf("%x ", bmp->pixel_data[(y * bmp->width) + x]);
			//printf("%x ", ReadAPixel(x_offset + x, y_offset + y));
		}
	}
}
