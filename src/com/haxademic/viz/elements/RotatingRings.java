package com.haxademic.viz.elements;


import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.data.Point3D;
import com.p5core.draw.cache.Shapes;
import com.p5core.util.ColorGroup;
import com.p5core.util.DrawUtil;

public class RotatingRings
extends ElementBase 
implements IVizElement {
	
	protected float _width;
	protected float _height;

	protected int NUM_RINGS = 10;
	
	// draw mode props
	protected int _mode = 0;
	protected final int MODE_ALPHA = 0;
	protected final int MODE_SOLID = 1;
	protected final int MODE_WIREFRAME = 2;
	protected final int NUM_MODES = 3;
	
	// spin modes
	protected int _spinMode = 0;
	protected final int SPINMODE_NONE = 0;
	protected final int SPINMODE_Y = 1;
	protected final int SPINMODE_XY = 2;
	protected final int NUM_SPIN_MODES = 3;
	
	protected Point3D _rotSpeed = new Point3D( 0, 0, 0 );
	protected Point3D _rotation = new Point3D( 0, 0, 0 );
	protected Point3D _rotationTarget = new Point3D( 0, 0, 0 );

	protected boolean _isWireframe = false;
	protected TColor _baseColor;
	protected TColor _strokeColor;

	public RotatingRings( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}

	public void init() {
		// set some defaults
	}
	
	public void updateColorSet( ColorGroup colors ) {
		_baseColor = colors.getRandomColor().copy();
		_strokeColor = _baseColor.copy();
		_strokeColor.lighten( 10 );
	}

	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		p.pushMatrix();
		
		// always rotate beginning draw position - rotates entire scene
		updateRotation();
		
		// Object properties	
		float scale = 2; // + p.sin( p.frameCount * 0.01f );
		int discPrecision = 40;

		int outerDiscRadius = 29;
		int outerDiscStartRadius = 89;
		float discSpacing = 7000;// + 1000 * p.sin(p.frameCount * 0.01f);

		for( int i = 0; i < NUM_RINGS; i++ )
		{
			int ringSpacingIndex = i+1;
			
			float ringEQVal = _audioData.getFFT().spectrum[i + 5];
			float alphaMultiplier = 1.3f;
			_baseColor.alpha = ( _isWireframe == true ) ? 0 : ringEQVal * alphaMultiplier;
			_strokeColor.alpha = ( _isWireframe == true ) ? ringEQVal * alphaMultiplier : 0;

			p.fill( _baseColor.toARGB() );
			p.stroke( _strokeColor.toARGB() );
			p.strokeWeight( 2 );
			
			// draw disc, with thickness based on eq 
			float eqThickness = ( ringEQVal * 6 ) * ( 2000 + 1000 * i );	// (i/NUM_RINGS)
			float innerRadius = outerDiscStartRadius + discSpacing * ringSpacingIndex;
			p.pushMatrix();			
			p.rotateY( i * (2*p.PI)/NUM_RINGS );
			Shapes.drawDisc3D( p, innerRadius * scale, ( innerRadius + outerDiscRadius ) * scale, eqThickness, discPrecision, _baseColor.toARGB(), _baseColor.toARGB() );//_ringColors[i].colorIntWithAlpha(ringAlpha, 0), _ringColors[i].colorIntWithAlpha(ringAlpha, wallOffset) );
			p.popMatrix();
			
			// draw orbiting star per ring
//			p.pushMatrix();
//			p.fill( _baseColor.toARGB() );//_ringColors[i].colorIntWithAlpha(ringAlpha, 0) );
//			float starX = innerRadius * scale * p.sin( p.frameCount * ringSpacingIndex * 0.01f );
//			float starY = innerRadius * scale * p.cos( p.frameCount * ringSpacingIndex * 0.01f );
//			p.translate( starX, starY, 0 );
//			p.rotateZ( i * (2*p.PI)/NUM_RINGS );
//			p.rotateY( i * (2*p.PI)/NUM_RINGS );
//			Shapes.drawStar( p, 5f, 50f * ringEQVal, 10f, 50 + 50 * ringEQVal, 0f);
//			p.popMatrix();
		}
		
		p.popMatrix();
	}

	protected void updateRotation() {
		_rotation.easeToPoint( _rotationTarget, 6 );
		p.rotateX( _rotation.x );
		p.rotateY( _rotation.y );
		p.rotateZ( _rotation.z );
		
		_rotationTarget.x += _rotSpeed.x;
		_rotationTarget.y += _rotSpeed.y;
		_rotationTarget.z += _rotSpeed.z;
	}
	
	public void reset() {
		updateLineMode();
		updateCamera();
	}
	
	public void updateLineMode() {
		_isWireframe = ( p.random(0f,2f) >= 1 ) ? false : true;
	}
	public void updateCamera() {
		// rotate
		float circleSegment = (float) ( Math.PI * 2f );
		_rotationTarget.x = p.random( -circleSegment, circleSegment );
		_rotationTarget.y = p.random( -circleSegment, circleSegment );
		_rotationTarget.z = p.random( -circleSegment, circleSegment );

		_rotSpeed.x = p.random( 0.001f, 0.001f );
		_rotSpeed.y = p.random( 0.001f, 0.001f );
		_rotSpeed.z = p.random( 0.001f, 0.001f );
	}

	public void dispose() {
		_audioData = null;
	}

}

