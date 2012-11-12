package com.haxademic.app.matchgame.game;

import java.util.ArrayList;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;

public class MatchGamePlay {
	
	protected MatchGame p;
	
	protected int ROWS = 3;
	protected int COLS = 4;
	protected ArrayList<MatchGamePiece> _pieces;
	
	protected MatchGameControls _controls;
	protected int _cursorLeftPieceID = -1;
	protected int _cursorRightPieceID = -1;
	protected boolean _twoPiecesSelected = false;
	protected int _matchHeldCount = 0;
	
	public MatchGamePlay( MatchGameControls controls ) {
		p = (MatchGame) P.p;
		_controls = controls;
		init();
	}
	
	protected void init() {
		// build game pieces
		_pieces = new ArrayList<MatchGamePiece>();
		int i = 0;
		for( int x=0; x < COLS; x++ ) {
			for( int y=0; y < ROWS; y++ ) {
				_pieces.add( new MatchGamePiece( i, x, y ) );
				i++;
			}
		}
	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		// update game pieces and reset/check hand cursor collisions
		p.pushMatrix();
		int lastCursorLeftPieceID = _cursorLeftPieceID;
		int lastCursorRightPieceID = _cursorRightPieceID;
		_cursorLeftPieceID = -1;
		_cursorRightPieceID = -1;
		for( int i=0; i < _pieces.size(); i++ ) {
			if( _pieces.get( i ).isActive() == true ) {
				boolean collision = checkCollisions( _pieces.get( i ) );
				_pieces.get( i ).update( collision );
			}
		}
		p.popMatrix();
		
		// if both cursors are over two different pieces, or it's a new pair of pieces, start the match timer
		if( _cursorLeftPieceID != -1 && _cursorRightPieceID != -1 && _cursorLeftPieceID != _cursorRightPieceID ) {
			boolean isNewPair = ( lastCursorLeftPieceID != _cursorLeftPieceID || lastCursorRightPieceID != _cursorRightPieceID );
			if( !_twoPiecesSelected || isNewPair ) {
				selectedTwoPieces();
			} 
		} else {
			if( _twoPiecesSelected ) unselectedTwoPieces();
		}
		
		// draw hand cursor controls
		_controls.drawControls();
		
		// count up held match
		if( _twoPiecesSelected == true ) {
			_matchHeldCount++;
			P.println("_matchHeldCount = "+_matchHeldCount);
			if( _matchHeldCount == 20 ) {
				matchSuccess();
			}
		}
	}
	
	protected void selectedTwoPieces() {
		// if we had 2 selected, reset stuff so we can start this new pair fresh
		if( _twoPiecesSelected == true ) unselectedTwoPieces();
		P.println("2 pieces selected!");
		_twoPiecesSelected = true;
		_matchHeldCount = 0;
	}
	
	protected void unselectedTwoPieces() {
		P.println("2 UNselected");
		_twoPiecesSelected = false;
	}
	
	protected void matchSuccess() {
		// kill selected pieces
		for( int i=0; i < _pieces.size(); i++ ) {
			if( _pieces.get( i ).index() == _cursorLeftPieceID || _pieces.get( i ).index() == _cursorRightPieceID ) {
				_pieces.get( i ).done();
			}
		}
		// reset cursor hover IDs
		_cursorLeftPieceID = -1;
		_cursorRightPieceID = -1;
	}
	
	/**
	 * Check to see if two hand cursors are inside game pieces
	 * @param piece
	 * @return
	 */
	protected boolean checkCollisions( MatchGamePiece piece ) {
		
		if( piece.rect.intersects( _controls.handLeftRect ) ) _cursorLeftPieceID = piece.index();
		if( piece.rect.intersects( _controls.handRightRect ) ) _cursorRightPieceID = piece.index();
		
		// if either cursor intersected the piece, return true
		if( _cursorLeftPieceID == piece.index() || _cursorRightPieceID == piece.index() )
			return true;
		else
			return false;
	}
}
