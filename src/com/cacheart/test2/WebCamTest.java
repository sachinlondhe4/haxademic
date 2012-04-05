package com.cacheart.test2;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.video.Capture;

import com.p5core.util.DrawUtil;
import com.p5core.util.OpenGLUtil;

public class WebCamTest extends PApplet {
	
	PApplet p;
	
	/**
	 * Processing web cam capture object
	 */
	protected Capture _webCam;
	
	protected int _camW = 160;
	protected int _camH = 120;
	
	public void setup () {
		p = this;
		// set up stage and drawing properties
		p.size( 640, 480, PConstants.OPENGL );
		OpenGLUtil.SetQuality( p, OpenGLUtil.HIGH );
		p.frameRate( 30 );
		p.smooth();

		initWebCam();
	}

	void initWebCam() {
		String[] cameras = Capture.list();
		if (cameras.length == 0) {
			println("There are no cameras available for capture.");
			exit();
		} else {
			println("Available cameras:");
			for (int i = 0; i < cameras.length; i++) {
				println(cameras[i]);
			}
			_webCam = new Capture(this, _camW, _camH);
		}      
	}
	
	public void draw() {
		p.background( 0 );
		p.rectMode(PConstants.CENTER);
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		DrawUtil.setBasicLights( p );
		
		
		p.translate( 0, 0, -100 );
		p.rotateX( 0.4f );
		if (_webCam.available() == true) {
			_webCam.read();
//			drawImage();
			drawDepthImage();
		}
	}
	
	void drawImage(){
		  p.image(_webCam, 0, 0);
	}
	
	void drawDepthImage(){
		int columns = _camW;
		int rows = _camH;
		int cellsize = 3;
		PImage img = _webCam.get();
		
		p.noStroke();
		
		for ( int i = 0; i < columns; i++) {
			for ( int j = 0; j < rows; j++) {
				float x = i;  // x position
				float y = j;  // y position
				float loc = x + y * img.width;  //  p.PIxel array location
				int c = img.pixels[(int)loc];  // Grab the color
				float z = p.brightness(c) / 10f;

				// Translate to the location, set fill and stroke, and draw the rect
				p.pushMatrix();
				p.translate(-img.width/2 + x, -img.height/2 + y, z);
				p.fill(c, 255);

				p.rect(0, 0, cellsize, cellsize);
				p.popMatrix();
			}
		}
	}

}
