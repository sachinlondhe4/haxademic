package com.haxademic.core.util;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ScreenUtil {
	
	public static void screenshotToJPG( PApplet p, String outputDir ) {
		p.saveFrame( outputDir + SystemUtil.getTimestamp( p ) + p.nf(p.frameCount, 8) + ".jpg");
	}
	
	public static void screenshotHiRes( PApplet p, int scaleFactor, String p5Renderer, String outputDir ) {
		// from: http://amnonp5.wordpress.com/2012/01/28/25-life-saving-tips-for-processing/
		PGraphics hires = p.createGraphics(p.width*scaleFactor, p.height*scaleFactor, p5Renderer );
		p.beginRecord(hires);
		hires.scale(scaleFactor);
		p.smooth();
		p.draw();
		p.endRecord();
		hires.save( outputDir + "hires.png" );
		p.noSmooth();
	}

	public static PImage getScreenAsPImage( PApplet p ) {
//		PImage screenshot = null;
		
		PGraphics screenshot = p.createGraphics(p.width, p.height, p.P3D );
		p.beginRecord(screenshot);
//		p.smooth();
		p.draw();
		p.endRecord();
//		p.noSmooth();

		
//		try {
//			Robot robot = new Robot();
//			screenshot = new PImage(robot.createScreenCapture(new Rectangle(0,0,p.width,p.height)));
//		} catch (AWTException e) { }
		return screenshot.get();
	}
}
