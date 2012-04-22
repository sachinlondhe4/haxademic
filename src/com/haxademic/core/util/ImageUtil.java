package com.haxademic.core.util;

import processing.core.PImage;

public class ImageUtil {
	/**
	 * Return the color int for a specific pixel of a PImage
	 * @param image	Image to grab pixel color from
	 * @param x		x pixel of the image
	 * @param y		y pixel of the image
	 * @return		The color as an int
	 */
	public static int getPixelColor( PImage image, int x, int y ) {
		return image.pixels[(int)x + y * image.width];
	}
	
	public static PImage getReversePImage( PImage image ) {
		PImage reverse = new PImage( image.width, image.height );
		for( int i=0; i < image.width; i++ ){
			for(int j=0; j < image.height; j++){
				reverse.set( image.width - 1 - i, j, image.get(i, j) );
			}
		}
		return reverse;
	}

}