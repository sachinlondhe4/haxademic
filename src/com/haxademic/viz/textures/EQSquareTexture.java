package com.haxademic.viz.textures;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.viz.IAudioTexture;

public class EQSquareTexture
implements IAudioTexture
{
	
	protected PGraphics _graphics;
	protected int _width, _height;
	
	public EQSquareTexture( int width, int height ) {
		_width = width;
		_height = height;
		_graphics = P.p.createGraphics( _width, _height, P.P3D );
	}
	
	public void updateTexture( AudioInputWrapper audioInput ) {
		float color;
		float alpha;
		float eqVal;
		int eqStep = Math.round( 512f / (float) _width );
		_graphics.background( 0 );
		for( int i=0; i < _width; i++ ) {
			eqVal = audioInput.getFFT().spectrum[ ( i * eqStep ) % 512 ];
			color = eqVal * 255f;
			alpha = eqVal * 255f;
			
			_graphics.beginDraw();
			_graphics.stroke( P.p.color( color, alpha ) );
			_graphics.line(i, 0, i, _height * eqVal );
			_graphics.endDraw();
		}
	}
	
	public PImage getTexture() {
		return _graphics;
	}
	
	public void dispose() {
		
	}
}
