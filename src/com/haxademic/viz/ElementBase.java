package com.haxademic.viz;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.p5core.audio.AudioInputWrapper;

public class ElementBase {

	protected PApplet p;
	protected ToxiclibsSupport toxi;
	public AudioInputWrapper _audioData;

	public ElementBase( PApplet p5, ToxiclibsSupport toxiclibs, AudioInputWrapper audioData ) {
		p = p5;
		toxi = toxiclibs;
		_audioData = audioData;
	}
	
	public void dispose() {
		p = null;
		toxi = null;
		_audioData = null;
	}

	public void updateColor( TColor colorFG ){}
	public void updateLineMode(){}
	public void updateCamera(){}
}
