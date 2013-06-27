package com.haxademic.app.haxvisual.viz.textures;

import processing.core.PGraphics;
import processing.core.PImage;
import toxi.color.TColor;

import com.haxademic.app.haxvisual.viz.IAudioTexture;
import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.TColorBlendBetween;

public class EQSquareTexture
implements IAudioTexture
{
	
	protected PGraphics _graphics;
	protected int _width, _height;
	protected TColorBlendBetween _color;
	
	public EQSquareTexture( int width, int height ) {
		_width = width;
		_height = height;
		_graphics = P.p.createGraphics( _width, _height, P.P3D );
		_color = new TColorBlendBetween( TColor.BLACK.copy(), TColor.BLACK.copy() );
	}
	
	public void updateTexture( AudioInputWrapper audioInput ) {
		float eqVal;
		int eqStep = Math.round( 512f / (float) _width );
		_graphics.background( 0 );
		for( int i=0; i < _width; i++ ) {
			eqVal = audioInput.getFFT().spectrum[ ( i * eqStep ) % 512 ];
			_graphics.beginDraw();
			_graphics.stroke( _color.argbWithPercent( eqVal ) );
			_graphics.line(i, 0, i, _height * eqVal );
			_graphics.endDraw();
		}
	}
	
	public PImage getTexture() {
		return _graphics;
	}
	
	public void dispose() {
		
	}

	public void init() {
		// TODO Auto-generated method stub
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	public void reset() {
		// TODO Auto-generated method stub
	}

	public void updateColorSet( ColorGroup colors ) {
		_color.setColors( TColor.BLACK.copy(), colors.getRandomColor() );
		_color.lightenColor( 0.3f );
	}

	public void updateLineMode() {
		// TODO Auto-generated method stub
	}

	public void updateCamera() {
		// TODO Auto-generated method stub
	}
}
