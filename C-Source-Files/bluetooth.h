#define Bluetooth_Control (*(volatile unsigned char *)(0x84000220))
#define Bluetooth_Status (*(volatile unsigned char *)(0x84000220))
#define Bluetooth_TxData (*(volatile unsigned char *)(0x84000222))
#define Bluetooth_RxData (*(volatile unsigned char *)(0x84000222))
#define Bluetooth_Baud (*(volatile unsigned char *)(0x84000224))

#define Tx_Mask 0x02
#define Rx_Mask 0x01

#define MAX_CMD_SIZE 50

void delay(unsigned int mseconds);
void init_bluetooth(void);
void bt_send_command(char *cmd);
void translate_command(int input, char *command);

