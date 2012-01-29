package com.cacheart.test2;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.video.MovieMaker;

public class MovieMakerTest
	extends PApplet
{
	// global vars
	protected int _fps = 30;
	protected Boolean _isSetup = false;
	MovieMaker mm;  // Declare MovieMaker object
	
	public void setup ()
	{
		// set up stage
//		if( !_isSetup ){
//			size( 1400, 900, P3D );				//size(screen.width,screen.height,P3D);
//			_isSetup = true;
////			hint(DISABLE_OPENGL_2X_SMOOTH); 
////			hint(ENABLE_OPENGL_4X_SMOOTH); 
//			frameRate( _fps );
//			colorMode( PConstants.RGB, 1, 1, 1 );
//			background( 0, 0, 0 );
//		}
		
		  size(320, 240, P3D);
		  // Create MovieMaker object with size, filename,
		  // compression codec and quality, framerate
		  mm = new MovieMaker(this, width, height, "output/drawing.mov", 30, MovieMaker.H263, MovieMaker.HIGH);
		  background(204);
	}

	/**
	 * Auto-initialization of the root class.
	 * @param args
	 */
//	public static void main(String args[]) {
//		PApplet.main(new String[] { "--present", "--hide-stop", "--bgcolor=000000", "com.cacheart.test2.MovieMakerTest" });
//	}

	public void draw() 
	{
		  ellipse(mouseX, mouseY, 20, 20);
		  mm.addFrame();  // Add window's pixels to movie
	}
	
	public void keyPressed() {
	  if (key == ' ') {
	    mm.finish();  // Finish the movie if space bar is pressed!
	  }
	}

	
}
