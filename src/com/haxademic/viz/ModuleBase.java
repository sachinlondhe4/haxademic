package com.haxademic.viz;

import toxi.processing.ToxiclibsSupport;

import com.haxademic.app.PAppletHax;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.cameras.common.ICamera;

public class ModuleBase {
	public PAppletHax p;
	public ToxiclibsSupport toxi;
	public AudioInputWrapper _audioData;
	public ICamera _curCamera;
	
	public ModuleBase() {
		p = PAppletHax.getInstance();
		toxi = p.getToxi();
		_audioData = p.getAudio();
	}
	
	public void dispose() {
		p = null;
		toxi = null;
		_audioData = null;
	}
}
