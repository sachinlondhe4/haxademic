package com.haxademic.core.draw.text;

import processing.core.PApplet;
import processing.core.PFont;

public class DebugText {
	
	protected PApplet p;

	/**
	 * Load a font for debug help at the moment. 
	 */
	protected PFont _debugFont;	

	public DebugText( PApplet p5 ) {
		p = p5;
		createFont();
	}
	
	protected void createFont() {
		p.textMode( p.SCREEN );
		_debugFont = p.createFont("Arial",30);
	}
	
	public void draw( String message ) {
		p.textFont( _debugFont );
		p.fill(255);
		p.text( message, 10, 40 );
	}
}
