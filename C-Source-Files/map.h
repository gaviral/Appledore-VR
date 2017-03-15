
#define BMP_PIXELOFFSETINFO_OFFSET 10 // offset in BYTES, MUST BE EVEN
#define BMP_WIDTH_OFFSET 18 // offset IN BYTES, MUST BE EVEN
#define BMP_HEIGHT_OFFSET 22 // offset IN BYTES, MUST BE EVEN

// grab 2 words (2 bytes each) at the byte offset (needs to be even since dividing by 2 to align by word), and turn these into a dword.
// had to do this because the dwords are not lined up, so cannot index into them with a dword* cast.
#define BITMAP_DWORD_READ(byte_offset) (dword) (	( ((word *) bitmap)[(byte_offset >> 1) - 1] << 8) + ((word *)bitmap)[byte_offset >> 1]	)

// would need to typedef something like this
typedef unsigned char byte;
typedef unsigned short word;
typedef unsigned long dword;

struct BITMAP  /* the structure for a bitmap. */
{
	// WORD is 16 bits, DWORD is 32, (byte is 8 obv.)
	dword width;
	dword height;
	byte *pixel_data;
};

/* DO NOT DEALLOCATE bitmap AFTER CALLING THIS; it sets a pointer to a place in bitmap */
void load_bitmap(void *bitmap, struct BITMAP *b);
/* draw the bitmap */
void draw_bitmap(struct BITMAP *bmp, int x_offset, int y_offset);


/***** STUFF FOR SDCARD - MAYBE MOVE IT *******/

//#define SIZE 308346
#define SIZE 181146

// TODO: look into EOF and stuff for size determination

int read_file(char *filename, void *buf);
int test_SD_card(void);

#define TIMEOUT_MAX 250000 // 250 kB

#define SEC 1000000
#define TEN_MS 10000
#define MAX_FILE_SIZE 150000


void render_map(int x, int y);
void gen_palette();
byte* convert_bits(byte *img);
byte *decode(void *buf, int size);
void draw_map(byte *bmp, int height, int width, int x_offset, int y_offset);
void *download(int *buf_offset, int *size, double lat1, double long1, double lat2, double long2, double lat3, double long3);
