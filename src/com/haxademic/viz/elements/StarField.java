package com.haxademic.viz.elements;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.data.Point3D;
import com.haxademic.core.data.easing.EasingFloat3d;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.MathUtil;
import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;

public class StarField
extends ElementBase 
implements IVizElement {
	
	protected float _width;
	protected float _height;
	protected int _numStars;
	protected ArrayList<Star> _stars;
	protected TColor _baseColor;
	
		public StarField( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}

	public void init() {
		// set some defaults
		float scaleMult = 0.5f;
		setDrawProps(p.width*scaleMult, p.height*scaleMult);
		
		_numStars = _audioData.getFFT().spectrum.length / 8;
		_stars = new ArrayList<Star>();
		for( int i = 0; i < _numStars; i++ ) {
			_stars.add( new Star() );
		}
	}
	
	public void setDrawProps(float width, float height) {
		_width = width;
		_height = height;
	}

	public void updateColorSet( ColorGroup colors ) {
		_baseColor = colors.getRandomColor().copy();
		float lighten = 0.3f;
		_baseColor.adjustRGB( lighten, lighten, lighten );
	}

	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		p.pushMatrix();
		
		p.translate( 0f, -p.height/2, -p.height * 0.5f );
		
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		
		// slow-rotate the entire element
		p.rotateX( p.frameCount/1000f );
		
		for( int i = 0; i < _numStars; i++ ) {
			_stars.get( i ).update( _audioData.getFFT().spectrum[i] );
		}
		
		
		p.popMatrix();
	}

	public void reset() {
		updateLineMode();
		updateCamera();
	}

	public void updateLineMode() {
//		_is3D = ( p.random(0f,2f) >= 1 ) ? false : true;
	}
	
	public void updateCamera() {
//		float circleSegment = (float) ( Math.PI * 2f ) / 16f;
//		_rotationTarget.x = -circleSegment * 3f + p.random( -circleSegment * 2, 0 );
//		_rotationTarget.y = p.random( -circleSegment, circleSegment );
//		_rotationTarget.z = p.random( -circleSegment, circleSegment );
	}
	
	public void dispose() {
		_audioData = null;
	}
	
	class Star {
		protected float _size, _speed;
		protected EasingFloat3d _loc;
		protected ArrayList<Point3D> _trailPoints;
		protected int _trailIndex = 0;
		protected int _numTrails = 20;
		protected int _framesTillMove = 0;
		protected Boolean _isStrafing = false;
		protected int _zRange = 1000;
		
		public Star() {
			_trailPoints = new ArrayList<Point3D>();
			for( int i = 0; i < _numTrails; i++ ) {
				_trailPoints.add( new Point3D( 0, 0, 0 ) );
			}
			
			_loc = new EasingFloat3d( 0, 0, 0, 5 );
			reset();
		}
		
		public void reset() {
//			_loc.setCurrentX( randRangeDecimel( -_width, _width ) );
			_loc.setTargetX( MathUtil.randRangeDecimel( -_width, _width ) );
//			_loc.setCurrentY( randRangeDecimel( -_height, _height ) );
			_loc.setTargetY( MathUtil.randRangeDecimel( -_height, _height ) );
			_loc.setCurrentZ( _zRange );
			_loc.setTargetZ( _zRange );
			_size = 60 + (int) (Math.sin( p.frameCount / 100f ) * 50);
			_speed = -_size + MathUtil.randRangeDecimel( -10, 10 );
			_framesTillMove = MathUtil.randRange( 0, 30 );
		}
		
		public void update( float amp ) {
			_framesTillMove--;
			if( _framesTillMove <= 0 ) {
				_framesTillMove = MathUtil.randRange( 0, 30 );
				_isStrafing = !_isStrafing;
				
				if( _isStrafing == true ) {
					float strafeDist = _speed * _framesTillMove / 10f;
					int randDir = MathUtil.randRange( 0, 3 );
					if( randDir == 0 ) {
						_loc.setTargetY( _loc.valueY() + strafeDist );
					} else if( randDir == 1 ) {
						_loc.setTargetY( _loc.valueY() - strafeDist );
					} else if( randDir == 2 ) {
						_loc.setTargetX( _loc.valueX() + strafeDist );
					} else if( randDir == 3 ) {
						_loc.setTargetX( _loc.valueX() - strafeDist );
					}
				} else {
//					_loc.setTargetX( _loc.valueX() );
//					_loc.setTargetY( _loc.valueY() );
				}
			}

			if( _isStrafing == true ) {
				
			} else {
				// keep moving forward
				_loc.setTargetZ( _loc.valueZ() + _speed );
			}
			
			
			_loc.update();
			
			_trailPoints.get( _trailIndex ).x = _loc.valueX();
			_trailPoints.get( _trailIndex ).y = _loc.valueY();
			_trailPoints.get( _trailIndex ).z = _loc.valueZ();
			
			float baseSize = _size * amp;
			int indx = _trailIndex;
			int alpha = 255;
			int fillColor = _baseColor.toARGB();

			
//			p.fill( 255, 100 );
//			p.pushMatrix();
//			p.translate( _loc.valueX(), _loc.valueY(), _loc.valueZ() );
//			p.box( baseSize );
//			baseSize *= 0.9f;
//			p.popMatrix();

			// loop backwards through history of locations
			for( int i = _numTrails + _trailIndex; i > _trailIndex; i-- ) {
				indx = i % _numTrails;
				p.pushMatrix();
				p.translate( _trailPoints.get(indx).x, _trailPoints.get(indx).y, _trailPoints.get(indx).z );
				p.fill( fillColor, alpha );
				p.box( baseSize );
				baseSize *= 0.97f;
				alpha -= 12.5;
				p.popMatrix();
			}

			
			
			
			if( _loc.valueZ() < -_zRange ) {
				reset();
			}
			
			_trailIndex++;
			if( _trailIndex >= _trailPoints.size() ) _trailIndex = 0; 
			
		}
	}
}
