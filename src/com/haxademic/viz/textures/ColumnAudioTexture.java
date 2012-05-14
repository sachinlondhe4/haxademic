package com.haxademic.viz.textures;

import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.viz.IAudioTexture;

public class ColumnAudioTexture
implements IAudioTexture
{
	
	protected AudioInputWrapper _audioInput;
	protected PImage _image;
	protected int _rows;
	
	public ColumnAudioTexture( int numRows ) {
		_rows = numRows;
		_image = new PImage( 1, _rows );
	}
	
	public void updateTexture( AudioInputWrapper audioInput ) {
		int eqStep = Math.round( 512f / (float) _rows );
		float color;
		float alpha;
		for( int i=0; i < _rows; i++ ) {
			color = audioInput.getFFT().spectrum[ ( i * eqStep ) % 512 ] * 255f;
			alpha = audioInput.getFFT().spectrum[ ( i * eqStep ) % 512 ];
			_image.set( 0, i, P.p.color( color, alpha ) ); 
		}
	}
	
	public PImage getTexture() {
		return _image;
	}
	
	public void dispose() {
		
	}
}
