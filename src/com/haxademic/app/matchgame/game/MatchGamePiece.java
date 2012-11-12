package com.haxademic.app.matchgame.game;

import java.awt.Rectangle;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.util.DrawUtil;

public class MatchGamePiece {

	protected MatchGame p;
	
	public static int BOX_SIZE = 140; 
	public static int BOX_PADDING = 10; 
	public static int TOP_LEFT_PADDING = 70; 
	
	protected int _col = -1;
	protected int _row = -1;
	protected int _index;
	protected int _matchID = -1;
	
	protected boolean _isActive = false;
	protected boolean _isOver = false;
	
	public Rectangle rect;

	public MatchGamePiece( int index, int col, int row ) {
		p = (MatchGame) P.p;
		_index = index;
		_col = col;
		_row = row;
		rect = new Rectangle( 
				TOP_LEFT_PADDING + _col * BOX_SIZE + BOX_PADDING, 
				TOP_LEFT_PADDING + _row * BOX_SIZE + BOX_PADDING, 
				BOX_SIZE, 
				BOX_SIZE 
		);
		init();
	}
	
	protected void init() {
		reset();
	}
	
	protected void reset() {
		_isActive = true;
		_isOver = false;
	}
	
	public void setMatchID( int matchID ) {
		_matchID = matchID;
	}

	public int matchID() {
		return _matchID;
	}

	public int index() {
		return _index;
	}

	public boolean isActive() {
		return _isActive;
	}

	public boolean isOver() {
		return _isOver;
	}
	
	public void matched( boolean didMatch ) {
		_isOver = false;
		if( didMatch == true ) _isActive = false;
	}

	/** 
	 * Main gameplay update loop. Draws the piece based on current state.
	 */
	public void update( boolean cursorOver ) {
		if( _isActive == true ) {
			DrawUtil.setDrawCorner(p);
			p.pushMatrix();
			if( cursorOver ) {
				_isOver = true;
//				p.fill(0,255,0, 127);
				p.image( MatchGameAssets.PIECE_IMAGES.get( _matchID ), rect.x, rect.y );
			} else {
				_isOver = false;
//				p.fill(0,0,255/6f * _matchID, 255);
				p.image( MatchGameAssets.PIECE_BACKFACE, rect.x, rect.y );
			}
//			p.stroke(255,255,255, 127);
//			p.rect( rect.x, rect.y, rect.width, rect.height );
			p.popMatrix();
			DrawUtil.setDrawCenter(p);
		}
	}

}
