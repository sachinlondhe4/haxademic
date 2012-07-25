package com.haxademic.viz.textures;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.viz.IAudioTexture;

public class EQGridTexture
implements IAudioTexture
{
	
	protected PGraphics _graphics;
	protected int _width, _height;
	protected int _cols, _rows;
	protected int _colW, _rowH;
	
	public EQGridTexture( int width, int height ) {
		_width = width;
		_height = height;
		_cols = 8;
		_rows = 8;
		_colW = _width / _cols;
		_rowH = _height / _rows;
		_graphics = P.p.createGraphics( _width, _height, P.P3D );
	}
	
	public void updateTexture( AudioInputWrapper audioInput ) {
		float color;
		float alpha;
		float eqVal;
		int eqStep = Math.round( 512f / (float) ( _cols * _rows ) );
		_graphics.background( 0 );
		_graphics.noStroke();
		int index = 0;
		for( int i=0; i < _cols; i++ ) {
			for( int j=0; j < _rows; j++ ) {
				eqVal = audioInput.getFFT().spectrum[ ( index * eqStep ) % 512 ];
				color = eqVal * 255f;
				alpha = eqVal * 255f;
				
				_graphics.beginDraw();
				_graphics.fill( P.p.color( color, alpha ) );
				_graphics.rect( i*_colW, j*_rowH, _colW, _rowH );
				_graphics.endDraw();
				
				index++;
			}
		}
	}
	
	public PImage getTexture() {
		return _graphics;
	}
	
	public void dispose() {
		
	}
}
