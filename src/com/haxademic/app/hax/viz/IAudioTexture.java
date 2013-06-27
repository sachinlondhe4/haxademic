package com.haxademic.app.hax.viz;

import com.haxademic.core.audio.AudioInputWrapper;

import processing.core.PImage;

public interface IAudioTexture
extends IVizElement {
	public void updateTexture( AudioInputWrapper audioInput );
	public PImage getTexture();
	public void dispose();
}
