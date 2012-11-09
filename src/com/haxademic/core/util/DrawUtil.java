package com.haxademic.core.util;

import processing.core.PApplet;
import processing.core.PConstants;

import com.haxademic.app.P;

public class DrawUtil {
	/**
	 * Clears all drawing properties so we may always have the same starting
	 * point when drawing across classes.
	 * 
	 * @param p		Processing Applet for reference to p5 core
	 */
	public static void resetGlobalProps(PApplet p) {
		p.resetMatrix();
		p.colorMode( P.RGB, 255, 255, 255, 255 );
		p.fill( 0, 255, 0, 127 );
		p.stroke( 0, 255, 0, 127 );
		p.strokeWeight( 1 );
		p.imageMode( PConstants.CENTER );
		p.rectMode( PConstants.CENTER );
		p.camera();
	}

	public static void setCenter(PApplet p) {
		p.resetMatrix();
		p.translate( 0, 0, 0 );
	}

	public static void setTopLeft( PApplet p ) {
		p.resetMatrix();
		p.translate( -p.width/2, -p.height/2, 0 );
	}

	public static void setBasicLights( PApplet p ) {
		// setup lighting props
		p.shininess(500); 
		p.lights();
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, 6000);
		p.ambientLight(0.2f,0.2f,0.2f, 0, 0, -6000);
	}

	
	public static void setDrawCorner( PApplet p ) {
		p.imageMode( PConstants.CORNER );
		p.rectMode( PConstants.CORNER );
	}
	
	public static void setDrawCenter( PApplet p ) {
		p.imageMode( PConstants.CENTER );
		p.rectMode( PConstants.CENTER );
	}
	
}
