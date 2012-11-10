package com.haxademic.app.matchgame.game;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;

public class MatchGamePlay {
	
	protected MatchGame p;
	protected MatchGameControls _controls;
	
	protected int ROWS = 3;
	protected int COLS = 4;
	protected ArrayList<MatchGameSquare> _pieces;
	
	public MatchGamePlay( MatchGameControls controls ) {
		p = (MatchGame) P.p;
		_controls = controls;
		init();
	}
	
	protected void init() {
		_pieces = new ArrayList<MatchGameSquare>();
		for( int i=0; i < COLS; i++ ) {
			for( int j=0; j < ROWS; j++ ) {
				_pieces.add( new MatchGameSquare( i, j ) );
			}
		}
	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		
		p.pushMatrix();
		for( int i=0; i < _pieces.size(); i++ ) {
			boolean collision = checkCollisions( _pieces.get( i ) );
			_pieces.get( i ).update( collision );
		}
		p.popMatrix();
		
		
		_controls.drawControls();
	}
	
	protected boolean checkCollisions( MatchGameSquare piece ) {
		if( piece.rect.intersects( _controls.handLeftRect ) ) return true;
		if( piece.rect.intersects( _controls.handRightRect ) ) return true;
		return false;
	}
}
