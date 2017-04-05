#include <altera_avalon_spi.h>

#define SPI_BASE 0x80001120
#define STICK_BUTTON (*(volatile unsigned char *)(0x80001140))

int adc_read_channel(alt_u8 channel);
void format_int_for_bluetooth(int x, char *ret, char prefix);
