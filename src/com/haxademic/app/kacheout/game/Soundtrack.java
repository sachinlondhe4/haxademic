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
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile("wav/kacheout/soundtrack/" + _soundtrackFiles.get( _index ), 512);
		_backgroundAudio.loop();
		_index++;
		if( _index == _soundtrackFiles.size() - 1 );
	}
	
	public void stop() {
		_backgroundAudio.pause();		
	}
}
