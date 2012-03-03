package com.haxademic.viz.elements;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.Haxademic;
import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.audio.WaveformData;
import com.p5core.data.easing.EasingFloat;
import com.p5core.data.easing.EasingFloat3d;
import com.p5core.util.ColorGroup;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class WaveformPlane 
extends ElementBase 
implements IVizElement {
	
	protected WaveformLine _wave;
	protected WaveformData _waveformData;
	protected ArrayList<WaveformData> _waveformDataHistory;
	protected TColor _baseColor;
	protected ColorGroup _curColors;
	protected EasingFloat _baseWaveLineSpacing = new EasingFloat( 5f, 5f );
	protected EasingFloat3d _rotation = new EasingFloat3d( 0, 0, 0, 5f );
	
	protected final int MODE_LINES = 1;
	protected float _drawMode = MODE_LINES;
	
	protected final int NUM_LINES = 50;
	protected final float _ninteyDeg = p.PI / 2f;


	public WaveformPlane( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}
	
	public void init() {
		_wave = new WaveformLine( p, toxi, _audioData );
		_waveformDataHistory = new ArrayList<WaveformData>();
		_waveformData = ( (Haxademic)p )._waveformData;
		for(int i = 0; i < NUM_LINES; i++) {
			_waveformDataHistory.add( new WaveformData( p, _waveformData._waveform.length ) );
		}
		reset();
	}

	public void updateColorSet( ColorGroup colors ) {
		_baseColor = colors.getRandomColor().copy();
		_baseColor.alpha = 0.85f;
		_curColors = colors;
	}

	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		
		// update audio buffers
		_waveformDataHistory.get( 0 ).copyFromOtherWaveformData( _waveformData, _waveformData._waveform.length );
		_waveformDataHistory.add( _waveformDataHistory.remove( 0 ) );
		
		p.pushMatrix();

		_baseWaveLineSpacing.update();
		
		float zDepth = 400;
		p.translate(0, 0, -zDepth);
		
//		float ninteyDeg = p.PI / 2f;
//		p.rotateY(ninteyDeg*p.mouseX/100);
//		p.rotateX(ninteyDeg*p.mouseY/100);
//		p.rotateZ(ninteyDeg*p.mouseY/100/4);
		
		// apply base easing rotation
		_rotation.update();
		p.rotateY( _rotation.valueX() );
		p.rotateX( _rotation.valueY() );
		p.rotateZ( _rotation.valueZ() );
		
		// set initial darw color to fade out
		p.stroke( _baseColor.toARGB() );
		p.noFill();
		p.pushMatrix();
//		p.rotateX(10);
		
		TColor lineColor = new TColor( TColor.WHITE );
		
		if( _drawMode == MODE_LINES ) {
			float curSpacing = _baseWaveLineSpacing.value();
			float _strokeWidth = 4;
			for(int i=0; i < NUM_LINES; i++) {
				// _curColors.getColorFromIndex(i % 1).toARGB()
				p.stroke( lineColor.toARGB() );
				
				// set waveform history on drawing object
				_wave.setWaveform( _waveformDataHistory.get(NUM_LINES - i - 1) );
				
				// set stroke width and color
				float strokeWidth = 5f * (float)(NUM_LINES - i)/(float)NUM_LINES;
				_strokeWidth = strokeWidth;
				
				float alpha = ((float)NUM_LINES - (float)i)/(float)NUM_LINES;
				lineColor.alpha = ( alpha >= 0 ) ? alpha : 0;

				
				p.pushMatrix();
				
				p.translate(0, -curSpacing, 0);
				p.rotateX(_ninteyDeg);
				_wave.setDrawProps(_strokeWidth, p.width/2 + p.width * (i+1)/20, 20f * (NUM_LINES - i)/NUM_LINES);
				_wave.update();
				
				p.popMatrix();
				p.pushMatrix();
				
				p.translate(0, curSpacing * 2, 0);
				p.rotateX(_ninteyDeg);
				_wave.setDrawProps(_strokeWidth, p.width/2 + p.width * (i+1)/20, 20f * (NUM_LINES - i)/NUM_LINES);
				_wave.update();
				
				p.popMatrix();
				
				// increment distance from center
				curSpacing += _baseWaveLineSpacing.value();
				
				
				// draw mesh lines
				
			}
		}
		
		// draw lines

//		p.rotateZ((float)(Math.PI*2f)/4f);
		
		p.popMatrix();
		p.popMatrix();
	}
	
	public void updateCamera() {
		// random 45 degree angles
		_rotation.setTargetX( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
		_rotation.setTargetY( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
		_rotation.setTargetZ( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
	}
	
	public void updateLineMode() {
		_baseWaveLineSpacing.setTarget( p.random( p.height/300f, p.height/40f ) );
	}
	
	public void reset() {
//		_drawMode = ( p.random( 0f, 4 ) > 3 ) ? 0 : 1;
//		_drawMode = ( MathUtil.randBoolean( p ) == true ) ? 0 : 1;
		updateLineMode();
		updateCamera();
	}
	
	public void dispose() {
		super.dispose();
	}
	
}