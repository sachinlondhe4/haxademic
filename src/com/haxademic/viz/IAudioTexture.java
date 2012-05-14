package com.haxademic.viz;

import com.haxademic.core.audio.AudioInputWrapper;

import processing.core.PImage;

public interface IAudioTexture {
	public void updateTexture( AudioInputWrapper audioInput );
	public PImage getTexture();
	public void dispose();
}
