package com.haxademic.app.matchgame.game;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;

public class MatchGamePlay {
	
	protected MatchGame p;
	protected MatchGameControls _controls;
	
	public MatchGamePlay( MatchGame p, MatchGameControls controls ) {
		this.p = p;
		_controls = controls;
		init();
	}
	
	protected void init() {
		
	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		p.translate(0, 0, -p.mouseX);
		p.fill(0,255,0);
		p.box(100,100,100);
	}
}
