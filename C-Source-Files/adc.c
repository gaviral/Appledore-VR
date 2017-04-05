#include <stdlib.h>
#include "adc.h"

int adc_read_channel(alt_u8 channel)
{
	/*Local variables used to store readings from ADC channel*/
	alt_u8 wdata;
	alt_u8 high, low;
	alt_u8 *rdata = malloc(2*sizeof(alt_u8)); // allocate 2*8 bits for the read data

	// Set up the write data like this: 0001 1000;
	// The first 1 is the start bit, and the next 1 indicates single ended mode for the ADC
	wdata = 0x18;

	// or in the masked lower 3 bits of the channel (only 8 channels supported)
	wdata |= (channel & 0x07);

	// perform the actual SPI transmission: write the byte we set up above, then read 2 bytes
	alt_avalon_spi_command(SPI_BASE, 0, 1, &wdata, 2, rdata, 0);

	high = rdata[0];
	low = rdata[1];

	free(rdata);

	/* Merge the two bytes (high, low) read from the adc into the command we want.
	 * The lower 6 bits of the high variable contain the high 6 bits of our command,
	 * and the upper 4 bits of the low variable contain the low 4 bits of our command (10 bits total).
	 */
	return ((((int)high) << 4) & 0x3f0) | ((low >> 4) & 0x0f);
}

void format_int_for_bluetooth(int x, char *ret, char prefix) {
	// prefix the command with the given prefix character
	ret[0] = prefix;

	int i=1;

	// add leading zeros so that the value sent in the command is always 4 digits long
	if(x / 1000 == 0) {
		ret[i] = '0';
		i++;
	}
	if(x / 100 == 0) {
		ret[i] = '0';
		i++;
	}
	if(x / 10 == 0) {
		ret[i] = '0';
		i++;
	}

	// add the rest of the value
	sprintf(ret+i, "%d", x);
}
