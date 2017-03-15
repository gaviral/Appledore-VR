#define GPS_Status 		(*(volatile unsigned char *)(0x84000210))
#define GPS_Control 	(*(volatile unsigned char *)(0x84000210))
#define GPS_TxData 		(*(volatile unsigned char *)(0x84000212))
#define GPS_RxData 		(*(volatile unsigned char *)(0x84000212))
#define GPS_Baud    	(*(volatile unsigned char *)(0x84000214))

typedef struct {
	char ID[6];
	char time[11];
	double latitude;
	double longitude;
	char NS[2];
	char EW[2];
	char position[2];
	int numSat;
	char HDOP[5];
	double alt;
	char altUnit[2];
	double geo;
	char geoUnit[2];
} gps_data;

typedef struct {
	int hh;
	int mm;
	int ss;
} time_struct;

typedef struct {
	double latitude;
	double longitude;
	time_struct time;
} gps_data_struct;


typedef struct {
	double latitude;
	double longitude;
} coordinates;


/* Function prototypes */
void init_gps(void);
void put_char_gps(int c);
char get_char_gps(void);
void gps_read_line(char buffer[]);
void gps_received_data(void);
void new_gps(gps_data *global, int current[]);
int check_line(char buffer[]);
void gps_parse_line(char line[], gps_data *currentData);
void raw_data(gps_data rawData);
void get_time(int current[]);
void get_gps_data(gps_data_struct *cur_gps);


