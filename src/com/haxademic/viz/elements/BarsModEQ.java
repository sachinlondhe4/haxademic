package com.haxademic.viz.elements;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.data.Point3D;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;

public class BarsModEQ
extends ElementBase 
implements IVizElement {
	
	protected float _width;
	protected float _height;
	protected float _amp;
	protected int _numLines;
	protected TColor _baseColor;
	protected boolean _is3D = false;
	
	protected float _cols = 32;
	protected float _rows = 16;

	protected Point3D _rotation = new Point3D( 0, 0, 0 );
	protected Point3D _rotationTarget = new Point3D( 0, 0, 0 );

	public BarsModEQ( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}

	public void init() {
		// set some defaults
		_width = p.width;
		_height = p.height;
		_amp = 20;
	}
	
	public void setDrawProps(float width, float height) {
		_width = width;
		_height = height;
	}

	public void updateColorSet( ColorGroup colors ) {
		_baseColor = colors.getRandomColor().copy();
		float lighten = 0.5f;
		_baseColor.adjustRGB( lighten, lighten, lighten );
	}

	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		p.pushMatrix();
		
		int scaleMult = 3;
		setDrawProps(p.width*scaleMult, p.height*scaleMult);
		p.translate( 0f, p.height, -p.height * 1.8f );
		
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		
		// ease tilt of grid
		_rotation.easeToPoint( _rotationTarget, 5 );
		p.rotateX( _rotation.x );//+ (float) Math.PI );
		//		p.rotateY( 0 );	// _rotation.y
		//		p.rotateZ( _rotation.z );

		// draw grid
		float cellW = _width/_cols;
		float cellH = _height/_rows;
		float startX = -_width/2;
		float startY = -_height/2;
		int fillColor = _baseColor.toARGB();
		float row = 0;
		int col = 0;
		

		for (int i = 0; i < _cols * 4; i++) {
			float eqAmp = _audioData.getFFT().spectrum[i];
			col = (int) ( i % _cols );
			row = eqAmp * 2.0f * _rows;	// (int) ( Math.floor(
				
			if( eqAmp > 0.01f ) {
				p.fill( fillColor, 55f );
				p.pushMatrix();
	
				p.translate( startX + col*cellW, 0, 0 );
				p.rotateX( -eqAmp * (float) Math.PI / 5f );
				p.rect( 0, 0, cellW, cellH + row*cellH );	
	
				p.popMatrix();					
			}
		}
				
		p.popMatrix();
	}

	public void reset() {
		updateLineMode();
		updateCamera();
	}

	public void updateLineMode() {
		_is3D = ( p.random(0f,2f) >= 1 ) ? false : true;
	}
	
	public void updateCamera() {
		float circleSegment = (float) ( Math.PI * 2f ) / 16f;
		_rotationTarget.x = -circleSegment * 3f + p.random( -circleSegment * 2, 0 );
		_rotationTarget.y = p.random( -circleSegment, circleSegment );
		_rotationTarget.z = p.random( -circleSegment, circleSegment );
	}
	
	public void dispose() {
		_audioData = null;
	}
	
}
