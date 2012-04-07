package com.p5core.util;

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
}