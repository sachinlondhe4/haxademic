package com.haxademic.core.draw.text;

import processing.core.PApplet;
import processing.core.PFont;

import com.haxademic.core.util.FontUtil;

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
		_debugFont = FontUtil.FontLoad( p, "helvetica_95_black-webfont.svg", 24);
		p.textMode( p.SCREEN );
//		textFont(createFont("Arial",30));
	}
	
	public void draw( String message ) {
		p.textFont( _debugFont );
		p.fill(255);
		p.text( message, 10, 40 );
	}
}
