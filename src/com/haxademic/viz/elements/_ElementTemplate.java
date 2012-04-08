package com.haxademic.viz.elements;


import processing.core.PApplet;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.util.ColorGroup;

public class _ElementTemplate
extends ElementBase 
implements IVizElement {
	
	protected float _width;
	protected float _height;

	public _ElementTemplate( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}

	public void init() {
		// set some defaults
	}
	
	public void setDrawProps(float width, float height, int numLines) {
		_width = width;
		_height = height;
	}

	public void update() {
		
	}

	public void reset() {
		
	}

	public void dispose() {
		_audioData = null;
	}

	public void updateColorSet( ColorGroup colors ) {
	}

}

