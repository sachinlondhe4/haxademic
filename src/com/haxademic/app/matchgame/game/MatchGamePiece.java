package com.haxademic.app.matchgame.game;

import java.awt.Rectangle;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.util.DrawUtil;

public class MatchGamePiece {

	protected MatchGame p;
	
	public static int BOX_SIZE = 140; 
	public static int BOX_PADDING = 10; 
	public static int TOP_PADDING = 312 - 10;
	public static int LEFT_PADDING = 244 - 10;
	
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
				LEFT_PADDING + _col * BOX_SIZE + BOX_PADDING, 
				TOP_PADDING + _row * BOX_SIZE + BOX_PADDING, 
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
		DrawUtil.setDrawCorner(p);
		DrawUtil.setColorForPImage(p);
		p.pushMatrix();
		if( _isActive == true ) {
			if( cursorOver ) {
				_isOver = true;
				p.image( MatchGameAssets.PIECE_IMAGES.get( _matchID ), rect.x, rect.y );
			} else {
				_isOver = false;
				p.image( MatchGameAssets.PIECE_BACKFACE, rect.x, rect.y );
			}
		} else {
			p.image( MatchGameAssets.PIECE_COMPLETE, rect.x, rect.y );
		}
		p.popMatrix();
	}

}
