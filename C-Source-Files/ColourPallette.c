/*
 * ColourPalletteData.c
 *
 *  Created on0xApr 12, 2015
 *      Author0xPaul
 */

// see --http://www.rapidtables.com/web/color/RGB_Color.htm for example colours
//
// Constants for each pallette number using the name for the colour can be found in the header file "Colours.h"
//
// this data represents the 24 bit RGB values of 256 colours. DE2 can display all 256 simultaneously, but DE1 can only display 64 at any one time.
// It should be setup in hardware in a ROM file in Quartus
// but the software ('C' code) version of the data is also given below and can be used as a reference to the original data in graphics chip ROM
//
// You should program the colour pallette (DE2 = 256, DE1 = 64) at the start, i.e in main() by calling ProgramPallette(BLACK, ColourPalletteData[0])
// for each colour and programming the colour(s) you want, 1 RGB value per pallette. BLACK is enumerated as 0 in Colours.h header file
//
// You should include this as part of the project so it gets compiled, but if you want to reference it in multiple source file
// you should put an "extern" declaration in those source files e.g. extern const unsigned int ColourPalletteData[256]

const unsigned int ColourPalletteData[256] = {
0x00000000, // Black
0x00FFFFFF, // White
0x00FF0000, // Red
0x0000FF00, // Green/Lime
0x000000FF, // Blue
0x00FFFF00, // Yellow
0x0000FFFF, // Cyan
0x00FF00FF, // Magenta
0x00C0C0C0, // Silver
0x00808080, // Gray
0x00800000, // Maroon
0x00808000, // Olive
0x00008000, // DarkGreen
0x00800080, // Purple
0x00008080, // Teal
0x00000080, // Navy
0x008B0000, // Dark Red
0x00A52A2A, // Brown
0x00B22222, // FireBrick
0x00DC143C, // Crimson
0x00FF6347, // Tomato
0x00FF7F50, // Coral
0x00Cd5C5C, // Indian Red
0x00F08080, // Light Coral
0x00E9967A, // Dark Salmon
0x00FA8072, // Salmon
0x00FFA07A, // Light Salmon
0x00FF4500, // Orange Red
0x00FF8C00, // Dark Orange
0x00FFA500, // Orange
0x00FFD700, // Gold
0x00B8860B, // Dark Golden Rod
0x00DAA520, // Golden Rod
0x00EEE8AA, // Pale Golden Rod
0x00BDB76B, // Dark Kharki
0x00F0E68C, // Khaki
0x00808000, // Olive
0x00FFFF00, // Yellow
0x009ACD32, // Yellow Green
0x00556B2F, // Dark Olive Green
0x006B8E23, // Olive Drab
0x007CFC00, // Lawn Green
0x007FFF00, // Chart Reuse
0x00ADFF2F, // Green Yellow
0x00006400, // Dark Green
0x00008000, // Green
0x00228B22, // Forest Green
0x0000FF00, // Green/Lime
0x0032CD32, // Lime Green
0x0090EE90, // Light Green
0x0098FB98, // Pale Green
0x008FBC8F, // Dark See Green
0x0000FA9A, // Medium Spring Green
0x0000FF7F, // Spring Green
0x002E8B57, // Sea Green
0x0066CDAA, // Medium Aqua Marine
0x003CB371, // Medium Sea Green
0x0020B2AA, // Light Sea Green
0x002F4F4F, // Dark Slate Gray
0x00008080, // Teal
0x00008B8B, // Dark Cyan
0x0000FFFF, // Aqua/Cyan
0x00E0FFFF, // Light Cyan
0x0000CED1, // Dark Turquise
0x0040E0D0, // Turquoise
0x0048D1CC, // Medium Turquoise
0x00AFEEEE, // Pale Turquoise
0x007FFFD4, // Aqua Marine
0x00B0E0E6, // Powder Blue
0x005F9EA0, // Cadet Blue
0x004682B4, // Steel Blue
0x006495ED, // Corn Flower Blue
0x0000BFFF, // Deep Sky Blue
0x001E90FF, // Dodger Blue
0x00ADD8E6, // Light Blue
0x0087CEEB, // Sky Blue
0x0087CEFA, // Light Sky Blue
0x00191970, // Midnight Blue
0x00000080, // Navy
0x0000008B, // Bark Blue
0x000000CD, // Medium Blue
0x000000FF, // Blue
0x004169E1, // Royal Blue
0x008A2BE2, // Blue Violet
0x004B0082, // Indigo
0x00483D8B, // Dark Slate Blue
0x006A5ACD, // Slate Blue
0x007B68EE, // Medium Slate Blue
0x009370DB, // Medium Purple
0x008B008B, // Dark Magenta
0x009400D3, // Dark Violet
0x009932CC, // Dark Orchid"
0x00BA55D3, // Medium Orchid
0x00800080, // Purple
0x00D8BFD8, // Thistle
0x00DDA0DD, // Plum
0x00EE82EE, // Violet
0x00FF00FF, // Magenta/Fuchia
0x00DA70D6, // Orchid
0x00C71585, // Medium Violet Red
0x00DB7093, // Pale Violet Red
0x00FF1493, // Deep Pink
0x00FF69B4, // Hot Pink
0x00ffB6C1, // Light Pink
0x00FFC0CB, // Pink
0x00FAEBD7, // Antique White
0x00F5F5DC, // Beige
0x00FFE4C4, // Bisque
0x00FFEBCD, // Blanched Almond
0x00F5DEB3, // Wheat
0x00FFF8DC, // Corn Silk
0x00FFFACD, // Lemon Chiffon
0x00FAFAD2, // Light Golden Rod Yellow
0x00FFFFE0, // Light Yellow
0x008B4513, // Saddle Brown
0x00A0522D, // Sienna
0x00D2691E, // Chocolate
0x00CD853F, // Peru
0x00F4A460, // Sandy Brown
0x00DEB887, // Burley Wood
0x00D2B48C, // Tan
0x00BC8F8F, // Rosy Tan
0x00FFE4B5, // Moccasin
0x00FFDEAD, // Navajo White
0x00FFDAB9, // Peach Puff
0x00FFE4E1, // Misty Rose
0x00FFF0F5, // Lavendar Blush
0x00FAF0E6, // Linen
0x00FDF5E6, // Old Lace
0x00FFEFD5, // Papaya Whip
0x00FFF5EE, // Sea Shell
0x00F5FFFA, // Mint Cream
0x00708090, // Slate Gray
0x00778899, // Light Slate Gray
0x00B0C4DE, // Light Steel Blue
0x00E6E6FA, // Lavender
0x00FFFAF0, // Floral White
0x00F0F8FF, // Alice Blue
0x00F8F8FF, // Ghost White
0x00F0FFF0, // Honey Dew
0x00FFFFF0, // Ivory
0x00F0FFFF, // Azure
0x00FFFAFA, // Snow
0x00000000, // Black
0x00696969, // Dim Gray
0x00808080, // Gray
0x00A9A9A9, // Dark Gray
0x00D3D3D3, // Light Gray
0x00DCDCDC, // GainsBoro
0x00F5F5F5, // White Smoke
0x00FFFFFF, // White

// Repeating colour - change these if you like
0x00000000, // Black
0x00FFFFFF, // White
0x00FF0000, // Red
0x0000FF00, // Green/Lime
0x000000FF, // Blue
0x00FFFF00, // Yellow
0x0000FFFF, // Cyan
0x00FF00FF, // Magenta
0x00C0C0C0, // Silver
0x00808080, // Gray
0x00800000, // Maroon
0x00808000, // Olive
0x00008000, // DarkGreen
0x00800080, // Purple
0x00008080, // Teal
0x00000080, // Navy
0x008B0000, // Dark Red
0x00A52A2A, // Brown
0x00B22222, // FireBrick
0x00DC143C, // Crimson
0x00FF6347, // Tomato
0x00FF7F50, // Coral
0x00Cd5C5C, // Indian Red
0x00F08080, // Light Coral
0x00E9967A, // Dark Salmon
0x00FA8072, // Salmon
0x00FFA07A, // Light Salmon
0x00FF4500, // Orange Red
0x00FF8C00, // Dark Orange
0x00FFA500, // Orange
0x00FFD700, // Gold
0x00B8860B, // Dark Golden Rod
0x00DAA520, // Golden Rod
0x00EEE8AA, // Pale Golden Rod
0x00BDB76B, // Dark Kharki
0x00F0E68C, // Khaki
0x00808000, // Olive
0x00FFFF00, // Yellow
0x009ACD32, // Yellow Green
0x00556B2F, // Dark Olive Green
0x006B8E23, // Olive Drab
0x007CFC00, // Lawn Green
0x007FFF00, // Chart Reuse
0x00ADFF2F, // Green Yellow
0x00006400, // Dark Green
0x00008000, // Green
0x00228B22, // Forest Green
0x0000FF00, // Green/Lime
0x0032CD32, // Lime Green
0x0090EE90, // Light Green
0x0098FB98, // Pale Green
0x008FBC8F, // Dark See Green
0x0000FA9A, // Medium Spring Green
0x0000FF7F, // Spring Green
0x002E8B57, // Sea Green
0x0066CDAA, // Medium Aqua Marine
0x003CB371, // Medium Sea Green
0x0020B2AA, // Light Sea Green
0x002F4F4F, // Dark Slate Gray
0x00008080, // Teal
0x00008B8B, // Dark Cyan
0x0000FFFF, // Aqua/Cyan
0x00E0FFFF, // Light Cyan
0x0000CED1, // Dark Turquise
0x0040E0D0, // Turquoise
0x0048D1CC, // Medium Turquoise
0x00AFEEEE, // Pale Turquoise
0x007FFFD4, // Aqua Marine
0x00B0E0E6, // Powder Blue
0x005F9EA0, // Cadet Blue
0x004682B4, // Steel Blue
0x006495ED, // Corn Flower Blue
0x0000BFFF, // Deep Sky Blue
0x001E90FF, // Dodger Blue
0x00ADD8E6, // Light Blue
0x0087CEEB, // Sky Blue
0x0087CEFA, // Light Sky Blue
0x00191970, // Midnight Blue
0x00000080, // Navy
0x0000008B, // Bark Blue
0x000000CD, // Medium Blue
0x000000FF, // Blue
0x004169E1, // Royal Blue
0x008A2BE2, // Blue Violet
0x004B0082, // Indigo
0x00483D8B, // Dark Slate Blue
0x006A5ACD, // Slate Blue
0x007B68EE, // Medium Slate Blue
0x009370DB, // Medium Purple
0x008B008B, // Dark Magenta
0x009400D3, // Dark Violet
0x009932CC, // Dark Orchid
0x00BA55D3, // Medium Orchid
0x00800080, // Purple
0x00D8BFD8, // Thistle
0x00DDA0DD, // Plum
0x00EE82EE, // Violet
0x00FF00FF, // Magenta/Fuchia
0x00DA70D6, // Orchid
0x00C71585, // Medium Violet Red
0x00DB7093, // Pale Violet Red
0x00FF1493, // Deep Pink
0x00FF69B4, // Hot Pink
0x00ffB6C1, // Light Pink
0x00FFC0CB // Pink
};

const unsigned int mapColourPalletteData[256] = {
0x003B2230,
0x002D2858,
0x00362F00,
0x00302E5D,
0x00373666,
0x003C3C6E,
0x00570012,
0x00610617,
0x00040B1C,
0x00122601,
0x00030C1D,
0x00162629,
0x004B490B,
0x004B490D,
0x00472F3C,
0x00555712,
0x005E6216,
0x00016B19,
0x0001691F,
0x004B3848,
0x00523C48,
0x005C4753,
0x00464779,
0x00695762,
0x00065E70,
0x00786875,
0x00437305,
0x000D8104,
0x00118705,
0x00168913,
0x00198C1D,
0x00099277,
0x006A9640,
0x0000A5FF,
0x0024ADF5,
0x0030B6FF,
0x0070918E,
0x006A73AA,
0x007281BB,
0x004B73C3,
0x004579F2,
0x005182F2,
0x005686F4,
0x006A81C8,
0x006C84C8,
0x006388D3,
0x007589C8,
0x007A90D4,
0x006992F5,
0x006C97F3,
0x00769CE2,
0x00739CF5,
0x0043BDFF,
0x005BC6FF,
0x008D222F,
0x00302936,
0x001D330D,
0x00254119,
0x002D4E29,
0x00355A59,
0x00306C53,
0x00295E61,
0x002D6365,
0x00A63340,
0x00563E46,
0x00436744,
0x00405D68,
0x00C9222C,
0x00632934,
0x00102732,
0x00602E37,
0x00213033,
0x002E7015,
0x00633840,
0x00023E46,
0x00074852,
0x0009343E,
0x000D424A,
0x000D4C53,
0x00154F57,
0x000E5860,
0x00116169,
0x00D66B72,
0x00E8373D,
0x00234248,
0x0020474E,
0x002B3941,
0x002C4248,
0x002C4B50,
0x0023535C,
0x002C5259,
0x001F5A61,
0x00246369,
0x00E66970,
0x00F45B61,
0x002D6469,
0x00F66B71,
0x00839023,
0x00229E03,
0x002AA614,
0x002AAA07,
0x002CAB0A,
0x0030AD16,
0x00269B23,
0x00299A34,
0x002D9E3A,
0x0038B325,
0x0038B133,
0x003CB730,
0x00378F51,
0x001D727C,
0x009D7361,
0x00319D42,
0x003BA64F,
0x0039A751,
0x003F7C6C,
0x00A08E5E,
0x00438574,
0x00BD8968,
0x00A2B344,
0x0042AF63,
0x0040BA36,
0x0046BE45,
0x0049C14E,
0x004EC457,
0x0052C75F,
0x0033B502,
0x00137377,
0x00EE7378,
0x00F5767A,
0x00ECB173,
0x00EFC450,
0x0029C047,
0x0025C865,
0x00897C85,
0x00858197,
0x00988B94,
0x00918EA5,
0x0089AA83,
0x009999B4,
0x00A08183,
0x00AE8E7F,
0x00B2927F,
0x00BD9293,
0x00B29D87,
0x00B99D93,
0x00A89EA5,
0x00A0A2B8,
0x00B4A7AA,
0x00B7AFB6,
0x008CA3C5,
0x00969CC6,
0x00829CE4,
0x008AAEFC,
0x0094ACEA,
0x0092B2FC,
0x00ABB1CB,
0x00A2ADCF,
0x00B2B8D8,
0x0097C8BB,
0x00A7C283,
0x00BFC88B,
0x00BEC391,
0x0085D6FF,
0x0095BAFD,
0x009ABCFD,
0x009DC2FF,
0x00BDBDCA,
0x00B5BDD9,
0x00BDDAC3,
0x00A3BAE5,
0x00A3BDFC,
0x00A7C5F5,
0x00A2C5FD,
0x00A8C1FD,
0x00ABC4FC,
0x00ADCEFD,
0x00B1C7FA,
0x00BDCBE5,
0x00B6D1FB,
0x00B9E7FF,
0x00DEB18B,
0x00C7AFA1,
0x00C2B8B8,
0x00DD99A6,
0x00E1828D,
0x00E2828F,
0x00E18B98,
0x00F77C80,
0x00E0919F,
0x00E59A91,
0x00E39AA4,
0x00ECAEB2,
0x00DDB1C2,
0x00C0C78D,
0x00C3B9B9,
0x00CCD2A1,
0x00D2C2B6,
0x00CAE3A1,
0x00D0E5AA,
0x00D5E8B8,
0x00E6C683,
0x00EACE82,
0x00EDD393,
0x00F3C183,
0x00F0C293,
0x00FDD199,
0x00EBBCAD,
0x00EACFB4,
0x00F1CBA1,
0x00F6D6B8,
0x00F6E3A6,
0x00FFEFB8,
0x00C9C1C6,
0x00CACBD9,
0x00DDB9CA,
0x00DBC3D6,
0x00DDD2CA,
0x00D8D3D7,
0x00C9D6F2,
0x00DFDCCD,
0x00D9E3CF,
0x00DEEEC3,
0x00C5DCFD,
0x00DCE2E7,
0x00D0E2FD,
0x00D6E5FD,
0x00D8DEEF,
0x00D9E4FB,
0x00D9E7FD,
0x00EBBCC1,
0x00E0CFC0,
0x00E2DAD6,
0x00E5E0DB,
0x00E7F0D2,
0x00F0DCC7,
0x00FEE3C3,
0x00FFEBCB,
0x00F4E4D5,
0x00FEF3D9,
0x00E5E2E2,
0x00EDE3DF,
0x00EAE6E4,
0x00EAE7E9,
0x00E6EFFF,
0x00F0EAE5,
0x00F5EEE2,
0x00F5EEE8,
0x00F1F9E5,
0x00FAEFE4,
0x00FAF3E9,
0x00F3F1F2,
0x00FAF5F0,
0x00FCF9F5,
0x00FDFCFC,
0x00FFFFFF
};

