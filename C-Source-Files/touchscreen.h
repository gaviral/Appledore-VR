#define TouchScreen_Control (*(volatile unsigned char *)(0x84000230))
#define TouchScreen_Status (*(volatile unsigned char *)(0x84000230))
#define TouchScreen_TxData (*(volatile unsigned char *)(0x84000232))
#define TouchScreen_RxData (*(volatile unsigned char *)(0x84000232))
#define TouchScreen_Baud (*(volatile unsigned char *)(0x84000234))

#define Tx_Mask 0x02
#define Rx_Mask 0x01
#define PenDown 0x81	//todo: technically should be a mask
#define PenUp 0x80		//todo: technically should be a mask

#define PREVIOUS 0
#define NEXT 1
/* a data type to hold a point/coord */
typedef struct { int x, y; } Point ;

struct Buttons {
	char text[50];
	Point origin;
	int width;
	int height;
	int color;
	int strColor;
	int fill;
};

void init_touch(void);
int screen_touched( void );
void wait_for_touch();
Point get_press(void);
Point get_release(void);

void init_draw();

#define M_PI 3.14159265358979323846
typedef struct {double x; double y;} Displacement;

//returns data in km
Displacement gps_to_grid(double origin_lon, double origin_lat, double point_lon, double point_lat);

typedef struct {Point p; int height; int width; } Map;

Point grid_to_map(Map map, double dx, double dy);

int gps_has_data(void);

