package com.haxademic.viz.elements;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class WaveformShapes 
extends ElementBase 
implements IVizElement {
	
	WaveformLine _wave;
	WaveformCircle _waveCircle;
	TColor _baseColor;
	protected float _baseWaveCircleRadius = 0;
	protected float _targetBaseWaveCircleRadius = 0;
	protected boolean _fgMode = true;

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
		
		_baseWaveCircleRadius = MathUtil.easeTo(_baseWaveCircleRadius, _targetBaseWaveCircleRadius, 10);
		
		float zDepth = 400;
		p.translate(0, 0, -zDepth);
		
		p.stroke( _baseColor.toARGB() );
		p.noFill();
		p.pushMatrix();
//		p.rotateX(10);
		_waveCircle.setDrawProps(40, _baseWaveCircleRadius + 400f, 25f);
		_waveCircle.update();
		_waveCircle.setDrawProps(30, _baseWaveCircleRadius + 300f, 25f);
		_waveCircle.update();
		_waveCircle.setDrawProps(20, _baseWaveCircleRadius + 200f, 25f);
		_waveCircle.update();
		_waveCircle.setDrawProps(10, _baseWaveCircleRadius + 100f, 25f);
		_waveCircle.update();
		
//		p.rotateZ((float)(Math.PI*2f)/4f);
		_wave.setDrawProps(10, p.width + zDepth, 20);
		_wave.update();
		p.translate(0, -40, 0);
		_wave.setDrawProps(40, p.width + zDepth, 40);
		_wave.update();
		p.translate(0, 80, 0);
		_wave.update();
		
		p.popMatrix();
	}
	
	public void reset() {
		_targetBaseWaveCircleRadius = p.random( 0, p.width/4 );
	}
	
	public void dispose() {
		super.dispose();
	}
	
}