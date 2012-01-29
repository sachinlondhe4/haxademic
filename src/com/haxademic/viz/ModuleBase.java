package com.haxademic.viz;

import toxi.processing.ToxiclibsSupport;

import com.haxademic.Haxademic;
import com.haxademic.viz.modules.Toxi;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.cameras.common.ICamera;

public class ModuleBase {
	public Haxademic p;
	public ToxiclibsSupport toxi;
	public AudioInputWrapper _audioData;
	public ICamera _curCamera;
	
	public ModuleBase() {
		p = Haxademic.getInstance();
		toxi = p.getToxi();
		_audioData = p.getAudio();
	}
	
	public void dispose() {
		p = null;
		toxi = null;
		_audioData = null;
	}
}
