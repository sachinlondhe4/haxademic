package com.haxademic.core.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.app.P;

public class ImageUtil {
	
	public static final int BLACK_INT = -16777216;
	public static final int CLEAR_INT = 48356;
	public static final int EMPTY_INT = 0;
	
	public static int getPixelIndex( PImage image, int x, int y ) {
		return (int) x + y * image.width;
	}
	
	/**
	 * Return the color int for a specific pixel of a PImage
	 * @param image	Image to grab pixel color from
	 * @param x		x pixel of the image
	 * @param y		y pixel of the image
	 * @return		The color as an int
	 */
	public static int getPixelColor( PImage image, int x, int y ) {
		if( x < 0 || y < 0 || image.pixels.length < getPixelIndex( image, x, y ) ) return 0;
		return image.pixels[ getPixelIndex( image, x, y ) ];
	}
	
	public static float getBrightnessForPixel( PApplet p, PImage image, int x, int y ) {
		return p.brightness( image.pixels[ getPixelIndex( image, x, y ) ] ) * 0.1f;
	}
	
	public static float colorDifference( PApplet p, int color1, int color2 ) {
		return (Math.abs(p.red(color1) - p.red(color2)) + Math.abs(p.green(color1) - p.green(color2)) + Math.abs(p.blue(color1) - p.blue(color2)) ) / 765f;
	}
	
	public static float brightnessDifference( PApplet p, int color1, int color2 ) {
		return Math.abs((p.red(color1) + p.green(color1) + p.blue(color1)) - (p.red(color2) + p.green(color2) + p.blue(color2))) / 765f;
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

	public static PImage getReversePImageFast( PImage image ) {
		PImage reverse = new PImage( image.width, image.height );
		reverse.loadPixels();
		for (int i = 0; i < image.width; i++) {
			for (int j = 0; j < image.height; j++) { 
				reverse.pixels[j*image.width+i] = image.pixels[(image.width - i - 1) + j*image.width]; // Reversing x to mirror the image
			}
		}
		reverse.updatePixels();
		return reverse;
	}

	public static PImage getScaledImage( PImage image, int newWidth, int newHeight ) {
		PImage scaled = new PImage( newWidth, newHeight );
		scaled.copy( image, 0, 0, image.width, image.height, 0, 0, newWidth, newHeight );
		return scaled;
	}
	
	public static void clearPGraphics( PGraphics pg ) {
		pg.background(0,0);
		pg.beginDraw();
		pg.endDraw();
	}
	
	public static PImage bufferedToPImage( BufferedImage bimg ) {
		try {
			PImage img=new PImage(bimg.getWidth(),bimg.getHeight(),PConstants.ARGB);
			bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
			img.updatePixels();
			return img;
		}
		catch(Exception e) {
			System.err.println("Can't create image from buffer");
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage pImageToBuffered( PImage pimg ) {
		BufferedImage dest = new BufferedImage( pimg.width, pimg.height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage( pimg.getImage(), 0, 0, null );
		g2.finalize();
		g2.dispose();
		return dest;
	}

	// ==================================================
	// Super Fast Blur v1.1
	// by Mario Klingemann 
	// <http://incubator.quasimondo.com>
	// ==================================================
	public static void fastblur(PImage img,int radius)
	{
		if (radius<1){
			return;
		}
		int w=img.width;
		int h=img.height;
		int wm=w-1;
		int hm=h-1;
		int wh=w*h;
		int div=radius+radius+1;
		int r[]=new int[wh];
		int g[]=new int[wh];
		int b[]=new int[wh];
		int rsum,gsum,bsum,x,y,i,p,p1,p2,yp,yi,yw;
		int vmin[] = new int[P.max(w,h)];
		int vmax[] = new int[P.max(w,h)];
		int[] pix=img.pixels;
		int dv[]=new int[256*div];
		for (i=0;i<256*div;i++){
			dv[i]=(i/div);
		}

		yw=yi=0;

		for (y=0;y<h;y++){
			rsum=gsum=bsum=0;
			for(i=-radius;i<=radius;i++){
				p=pix[yi+P.min(wm,P.max(i,0))];
				rsum+=(p & 0xff0000)>>16;
			gsum+=(p & 0x00ff00)>>8;
		bsum+= p & 0x0000ff;
			}
			for (x=0;x<w;x++){

				r[yi]=dv[rsum];
				g[yi]=dv[gsum];
				b[yi]=dv[bsum];

				if(y==0){
					vmin[x]=P.min(x+radius+1,wm);
					vmax[x]=P.max(x-radius,0);
				}
				p1=pix[yw+vmin[x]];
				p2=pix[yw+vmax[x]];

				rsum+=((p1 & 0xff0000)-(p2 & 0xff0000))>>16;
			gsum+=((p1 & 0x00ff00)-(p2 & 0x00ff00))>>8;
		bsum+= (p1 & 0x0000ff)-(p2 & 0x0000ff);
		yi++;
			}
			yw+=w;
		}

		for (x=0;x<w;x++){
			rsum=gsum=bsum=0;
			yp=-radius*w;
			for(i=-radius;i<=radius;i++){
				yi=P.max(0,yp)+x;
				rsum+=r[yi];
				gsum+=g[yi];
				bsum+=b[yi];
				yp+=w;
			}
			yi=x;
			for (y=0;y<h;y++){
				pix[yi]=0xff000000 | (dv[rsum]<<16) | (dv[gsum]<<8) | dv[bsum];
				if(x==0){
					vmin[y]=P.min(y+radius+1,hm)*w;
					vmax[y]=P.max(y-radius,0)*w;
				}
				p1=x+vmin[y];
				p2=x+vmax[y];

				rsum+=r[p1]-r[p2];
				gsum+=g[p1]-g[p2];
				bsum+=b[p1]-b[p2];

				yi+=w;
			}
		}

	}

}