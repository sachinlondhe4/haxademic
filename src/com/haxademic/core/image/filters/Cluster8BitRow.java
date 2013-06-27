package com.haxademic.core.image.filters;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.image.ImageUtil;

public class Cluster8BitRow {
	
	protected PAppletHax p;
	protected int _width;
	protected int _height;
	protected int _rowSize;
	protected boolean _isVertical;
	protected PGraphics _pg;

	
	public Cluster8BitRow( int width, int height, int rowSize, boolean isVertical ) {
		p = (PAppletHax) P.p;
		_width = width;
		_height = height;
		_rowSize = rowSize;
		_isVertical = isVertical;
		_pg = p.createGraphics( _width, _height, P.P3D );
	}
	
	public PImage updateWithPImage( PImage source ) {
		drawPixels( source );
		return _pg;
	}
	
	protected void drawPixels( PImage source ) {
		ImageUtil.clearPGraphics( _pg );
		_pg.noStroke();
		_pg.fill(0,0);
		
		if( _isVertical == true ) {
			drawVertical( source );
		} else {
			drawHorizontal( source );
		}
	}
	
	protected void drawHorizontal( PImage source ) {	
		for( int y=0; y < source.height; y += _rowSize ) {
			int lastDrawnX = 0;
			int color = ImageUtil.getPixelColor( source, 0, y );
			for( int x=0; x < source.width; x += _rowSize ) {
				int checkColor = ImageUtil.getPixelColor( source, x, y );
				if( ImageUtil.brightnessDifference( p, color, checkColor ) > 0.05f ){
					_pg.fill(color);
					_pg.rect( lastDrawnX, y, x - lastDrawnX, _rowSize );
					color = checkColor;
					lastDrawnX = x;
				}
				
			}
		}
	}
		
	protected void drawVertical( PImage source ) {
		for( int x=0; x < source.width; x += _rowSize ) {
			int lastDrawnY = 0;
			int color = ImageUtil.getPixelColor( source, x, 0 );
			for( int y=0; y < source.height; y += _rowSize ) {
				int checkColor = ImageUtil.getPixelColor( source, x, y );
				if( ImageUtil.brightnessDifference( p, color, checkColor ) > 0.1f ){
					_pg.fill(color);
					_pg.rect( x, lastDrawnY, _rowSize, y - lastDrawnY );
					color = checkColor;
					lastDrawnY = y;
				}
				
			}
		}
	}
	
}

