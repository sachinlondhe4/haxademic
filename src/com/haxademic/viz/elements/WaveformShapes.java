package com.haxademic.viz.elements;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.data.EasingFloat;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class WaveformShapes 
extends ElementBase 
implements IVizElement {
	
	WaveformLine _wave;
	WaveformCircle _waveCircle;
	TColor _baseColor;
	protected EasingFloat _baseWaveCircleRadius = new EasingFloat( 50f, 50f );
	protected EasingFloat _baseWaveLineSpacing = new EasingFloat( 20f, 20f );
	protected boolean _fgMode = true;
	protected float _rotation = 0;
	protected float _rotationTarget = 0;
	
	protected final int MODE_CIRCLES = 0;
	protected final int MODE_LINES = 1;
	protected float _drawMode = MODE_CIRCLES;


	public WaveformShapes( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}
	
	public void init() {
		_wave = new WaveformLine( p, toxi, _audioData );
		_waveCircle = new WaveformCircle( p, toxi, _audioData );
		reset();
	}

	public void updateColor( TColor color ) {
		_baseColor = color.copy();
		_baseColor.alpha = 0.75f;
	}

	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		
		_baseWaveCircleRadius.update();
		_baseWaveLineSpacing.update();
		
		float zDepth = 400;
		p.translate(0, 0, -zDepth);
		
		p.stroke( _baseColor.toARGB() );
		p.noFill();
		p.pushMatrix();
//		p.rotateX(10);
		
		if( _drawMode == MODE_CIRCLES ) {
			// draw circles
			float curRadius = _baseWaveCircleRadius.value();
			float _strokeWidth = 1;
			for(int i=0; i < 10; i++) {
				_waveCircle.setDrawProps(_strokeWidth, curRadius, 25f);
				curRadius += _baseWaveCircleRadius.value();
				_strokeWidth += 0.5;
				_waveCircle.update();
			}
		} else if( _drawMode == MODE_LINES ) {
			float curSpacing = _baseWaveLineSpacing.value();
			float _strokeWidth = 1;
			for(int i=0; i < 10; i++) {
				p.pushMatrix();
				p.translate(0, -curSpacing, 0);
				_wave.setDrawProps(_strokeWidth, p.width + zDepth, 20);
				_wave.update();
				p.translate(0, curSpacing * 2, 0);
				_wave.setDrawProps(_strokeWidth, p.width + zDepth, 20);
				_wave.update();
				_strokeWidth += 0.5;
				curSpacing += _baseWaveLineSpacing.value();
				p.popMatrix();
			}
		}
		
		// draw lines

//		p.rotateZ((float)(Math.PI*2f)/4f);
		
		
		p.popMatrix();
	}
	
	public void reset() {
		_baseWaveCircleRadius.setTarget( p.random( p.width/50, p.width/10 ) );
		_baseWaveLineSpacing.setTarget( p.random( p.height/65, p.height/15 ) );
		_drawMode = ( p.random( 0f, 4 ) > 3 ) ? 0 : 1;
	}
	
	public void dispose() {
		super.dispose();
	}
	
}