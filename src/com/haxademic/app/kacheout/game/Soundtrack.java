package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.util.FileUtil;
import com.haxademic.core.util.MathUtil;

import ddf.minim.AudioPlayer;

public class Soundtrack {
	
	protected KacheOut p;
	protected AudioPlayer _backgroundAudio = null;
	protected ArrayList<String> _soundtrackFiles;
	protected int _index = 0;
	
	public Soundtrack() {
		p = (KacheOut)PAppletHax.getInstance();
		
		_soundtrackFiles = FileUtil.getFilesInDirOfType( "data/wav/kacheout/soundtrack", ".wav" );
		_index = MathUtil.randRange( 0, _soundtrackFiles.size() - 1 );
	}
	
	public void playNext() {
		_index++;
		if( _index == _soundtrackFiles.size() - 1 ) _index = 0;
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile("wav/kacheout/soundtrack/" + _soundtrackFiles.get( _index ), 512);
		_backgroundAudio.loop();
		if( _index == _soundtrackFiles.size() - 1 ) _index = 0;
	}
	
	public void playIntro() {
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile("wav/kacheout/soundtrack/special/08 disrupt - the bass has left the building.wav", 512);
		_backgroundAudio.play(0);
	}
	
	public void playInstructions() {
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile("wav/kacheout/soundtrack/special/01 rip-off artist - bang trim.wav", 512);
		_backgroundAudio.play(0);
	}
	
	public void stop() {
		if( _backgroundAudio != null && _backgroundAudio.isPlaying() ) _backgroundAudio.pause();		
	}
}
