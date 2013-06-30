
package com.haxademic.sketch.text;

import processing.core.PApplet;

import com.haxademic.core.draw.text.CustomFontText2D;

@SuppressWarnings("serial")
public class Text2DTTF
extends PApplet {
	CustomFontText2D _fontRenderer;
	
	public void setup()
	{
		size(800, 600, P3D);
		_fontRenderer = new CustomFontText2D( this, "../data/fonts/bitlow.ttf", 70.0f, color(0,255,0), CustomFontText2D.ALIGN_CENTER, 450, 100 );
	}

	public void draw() {
		background(0);
		translate(mouseX, height/2, 0); 
		_fontRenderer.updateText( frameCount+"" );
		image( _fontRenderer.getTextPImage(), 0, 0 );
	}


}
