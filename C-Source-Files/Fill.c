// data structure to store a coordinate

typedef struct { int x,y;} XYPixel ;

// an array of coordinates and a pointer to first

XYPixel XYStack[1000], *Next = &XYStack[0];


/*****************************************************************************************************************************
* simple stack based around an array
* Used for FILL algorithm
******************************************************************************************************************************/

int PushPixel(XYPixel p1)
{
    if(Next <= &XYStack[1000]) {
        *Next++ = p1;
        return 0 ;
    }
    else
        return -1 ;
}

int PopPixel(XYPixel *theXYPixel)
{
    if(Next >= XYStack) {
        *theXYPixel = *(--Next);
        return 0 ;
    }
    else
        return -1 ;
}

int IsStackEmpty(void)
{
    if(Next == XYStack)
        return 1 ;
    else
        return 0 ;
}

/********************************************************************************************
** Implementation of a Scan Line Seed Fill Algorithm
** Taken from Procedural Elements of Computer Graphics: David Rogers
********************************************************************************************/
void Fill(int _x, int _y, int _FillColour, int _BoundaryColour)
{
    register int     x, y ;
    register int     BoundaryColour = _BoundaryColour;
    register int 	 PixelColour, FillColour = _FillColour ;

    int     XRight, XLeft ;
    int     SaveX, SaveY ;      		// temp variable
    XYPixel aPoint, aPoint1 ;           // temp var

    Next = XYStack ;                    // initialise to start of stack
    aPoint.x = _x ;
    aPoint.y = _y ;

    PushPixel(aPoint) ;                   // push the seed

    while(!IsStackEmpty())                 // keep going until no more items on the stack
    {
        PopPixel(&aPoint) ;                 // get a point from the stack
        x = aPoint.x ;
        y = aPoint.y ;
        WriteAPixel(x, y, FillColour);     // fill the point in the fill colour

        // fill the span to the right of the seed value
        SaveX = x++ ;                  // save the x coord of the the point we just filled and move one pixel right

        while((char)(ReadAPixel(x,y)) != (char)(BoundaryColour))							// if new pixel is not the boundary colour
            WriteAPixel(x++, y, FillColour);     											// fill it and keep moving right along a horizontal line

        // must have found the boundary colour when moving right
        XRight = x - 1 ;		// save X coord of the last filled pixel on this line when moving right
        x = SaveX ;				// get the original starting x back

        // now fill the span to the left of the seed value

        --x ;

        while((char)(ReadAPixel(x,y)) != (char)(BoundaryColour))						// if new pixel is not the boundary colour
            WriteAPixel(x--, y, FillColour);    											// fill it and keep moving left along a horizontal line

        XLeft = x + 1 ;			// save X coord of the last filled pixel on this line when moving left
        x = SaveX ; 			// get original x coord for the seed back

		///////////////////////////////////////////////////////////////////////////////////////////////////
        // check that the scan line below is neither a polygon boundary nor
        // has been previously completely filled
        //////////////////////////////////////////////////////////////////////////////////////////////////

        SaveY = y ;			// save the current y coordinate of the line we have just drawn
        x = XLeft ;			// starting at the left x
        ++y ;				// move down one line

		// starting from the left keep moving right looking at the pixel
        // until we find something that is neither filled nor boundary colour as it represents something on the line that may be a pixel to fill

        do {
        	PixelColour = ReadAPixel(x++,y) ;
        } while(((char)(PixelColour) == (char)(BoundaryColour)) || ((char)(PixelColour) == (char)(FillColour)) );

         x-- ;

        // to get here we must have found something that needs filling i.e. the above loop found that the line below was not a complete boundary edge or filled
		// if we are still less than the previous right most X coord then it would be a new point that we need to seed
        while(x <= XRight)
        {
            // seed the scan line below
        	// if the pixel at x,y is not a boundary colour and less than extreme right

        	// skip over any pixels already filled
            while(((char)(ReadAPixel(x,y)) != (char)(BoundaryColour)) && (x <= XRight))
               ++x ;

            // push the  extreme right pixel onto the stack
            aPoint1.x = x - 1 ;
            aPoint1.y = y ;
            PushPixel(aPoint1) ;

            // continue checking in case the span is interrupted by another shape inside the one we are trying to fill

            ++x ;

            // skip over anything that is filled or boundary (i.e. other shape) inside the one we are trying to fill
            do {
            	PixelColour = ReadAPixel(x++,y) ;
            } while(((char)(PixelColour) == (char)(BoundaryColour)) || ((char)(PixelColour) == (char)(FillColour)) );

             x-- ;
        }
      	x = SaveX ;
       	y = SaveY ;

	 ///////////////////////////////////////////////////////////////////////////////////////////////////
    // check that the scan line above is neither a polygon boundary nor
    // has been previously completely filled

        y = SaveY;
        x = XLeft ;
        --y ;

        do {
        	PixelColour = ReadAPixel(x++,y) ;
        } while(((char)(PixelColour) == (char)(BoundaryColour)) || ((char)(PixelColour) == (char)(FillColour)) );

         x-- ;

        while(x <= XRight)		// if we have not reached the boundary
        {
            // seed the scan line below
            while(((char)(ReadAPixel(x,y)) != (char)(BoundaryColour)) && (x <= XRight))
               ++x ;		// look for right most x inside the boudan

            // push the  extreme right pixel onto the stack
            aPoint1.x = x - 1 ;
            aPoint1.y = y ;
            PushPixel(aPoint1) ;

            // continue checking in case the span is interrupted
            ++x ;

            do {
            	PixelColour = ReadAPixel(x++,y) ;
            } while(((char)(PixelColour) == (char)(BoundaryColour)) || ((char)(PixelColour) == (char)(FillColour)) );

             x-- ;
        }
       	x = SaveX ;
       	y = SaveY ;
    }
}