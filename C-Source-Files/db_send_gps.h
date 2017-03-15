#define SET_LED "gpio.mode(3,gpio.OUTPUT)\r\n"
#define LIGHT_LED "gpio.write(3,gpio.LOW)\r\n"
#define OFF_LED "gpio.write(3,gpio.HIGH)\r\n"
#define DOFILE_CONNECT "dofile('Connect.lua')\r\n"
#define DOFILE_PATCH "dofile('firebaseComm.lua')\r\n"
#define HTTP_PATCH "http_patch(\"%s\", '%s', \"%d\")\r\n"

#define CONTENT_STR "{\"location\": {\"latitude\": %f, \"longitude\": %f}}"

// carriage return and new line
#define CR_NL ((const char *) "\r\n")

#define Wifi_Control (*(volatile unsigned char *)(0x84000240))
#define Wifi_Status (*(volatile unsigned char *)(0x84000240))
#define Wifi_TxData (*(volatile unsigned char *)(0x84000242))
#define Wifi_RxData (*(volatile unsigned char *)(0x84000242))
#define Wifi_Baud (*(volatile unsigned char *)(0x84000244))

#define MAX_STR_LEN 200
#define MAX_CONTENT_LEN 100

// This is for checking the time intervals
#define TIME_INTERVAL_SEC ((float) 60)


/* Function prototypes */
void format_content(double lat, double lng, char *content);
int get_content_len(char *content);
void http_patch(char *user_id, double lat, double lng);
int http_patch_check(char *user_id, int *started);
void init_wifi(void);
int put_string_wifi(const char* string);
int put_char_wifi(int c);
