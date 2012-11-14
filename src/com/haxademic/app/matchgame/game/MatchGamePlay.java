package com.haxademic.app.matchgame.game;

import java.util.ArrayList;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.util.MathUtil;

public class MatchGamePlay {
	
	protected MatchGame p;
	
	protected int ROWS = 3;
	protected int COLS = 4;
	protected ArrayList<MatchGamePiece> _pieces;
	protected int[] _pieceMatchIDs;
	
	protected MatchGameControls _controls;
	protected int _cursorLeftPieceID = -1;
	protected int _cursorRightPieceID = -1;
	protected int _lastCursorRightPieceID = -1;
	protected int _lastCursorLeftPieceID = -1;
	protected boolean _twoPiecesSelected = false;
	
	protected float _matchHeldStartTime = 0f;
	protected float MATCH_HELD_TIME = 2000f;
	
	protected int _gameStartTime = 0;
	
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
		
		// set up array to distribute piece IDs 
		_pieceMatchIDs = new int[_pieces.size()];
		int curID = -1;
		for( i=0; i < _pieceMatchIDs.length; i++ ) {
			if( i % 2 == 0 ) curID++;
			_pieceMatchIDs[i] = curID;
		}
		
		// randomize and reset props
		reset();
	}
	
	public void reset() {
		// give game pieces new match IDs
		randomizeIntArray( _pieceMatchIDs );
		for( int i=0; i < _pieceMatchIDs.length; i++ ) {
			_pieces.get( i ).setMatchID( _pieceMatchIDs[i] );
			_pieces.get( i ).reset();
		}
		_gameStartTime = p.millis();

//		debug to make sure IDs are randomized and good
//		for( int i=0; i < _pieceMatchIDs.length; i++ ) {
//			P.println("match ID: "+_pieceMatchIDs[i]);
//		}
	}
	
	public void startGame() {
//		_curGameTime = 0;
	}

	protected void randomizeIntArray( int[] arr ) {
		for( int i=0; i < arr.length; i++ ) {
			int tmp = arr[i];
			int randomNum = MathUtil.randRange(0, arr.length - 1);
			arr[i] = arr[randomNum];
			arr[randomNum] = tmp;
		}
	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		updateGamePieces();
		checkForTwoPiecesSelected();
		checkForMatchComplete();
		drawTimer();
		checkGameIsDone();
		
		
		
//		// test text
//		p.fill( 255 );
//		p.textFont(MatchGameAssets.TEST_FONT, 48);
//		p.text("YO WORD", 10, 50);
//		
////		_fontRenderer = new CustomFontText2D( this, "../data/fonts/bitlow.ttf", 70.0f, color(0,255,0), 450, 100 );
////	}
////
////	public void draw() {
////		background(0);
////		translate(mouseX, height/2, 0); 
////		_fontRenderer.updateText( frameCount+"" );
////		image( _fontRenderer.getTextPImage(), 0, 0 );

	}
	
	protected void updateGamePieces() {
		// update game pieces and reset/check hand cursor collisions
		p.pushMatrix();
		_lastCursorLeftPieceID = _cursorLeftPieceID;
		_lastCursorRightPieceID = _cursorRightPieceID;
		_cursorLeftPieceID = -1;
		_cursorRightPieceID = -1;
		for( int i=0; i < _pieces.size(); i++ ) {
			boolean collision = false;
			if( _pieces.get( i ).isActive() == true ) collision = checkCollisions( _pieces.get( i ) );
			_pieces.get( i ).update( collision );
		}
		p.popMatrix();
	}
	
	/**
	 * If both cursors are over two different pieces, or it's a new pair of pieces, start the match timer.
	 * Keeps track of the selected and unselected states of the pieces, and sets flags for further piece-matching.
	 */
	protected void checkForTwoPiecesSelected() {
		if( _cursorLeftPieceID != -1 && _cursorRightPieceID != -1 && _cursorLeftPieceID != _cursorRightPieceID ) {
			boolean isNewPair = ( _lastCursorLeftPieceID != _cursorLeftPieceID || _lastCursorRightPieceID != _cursorRightPieceID );
			if( !_twoPiecesSelected || isNewPair ) {
				selectedTwoPieces();
			} 
		} else {
			if( _twoPiecesSelected ) unselectedTwoPieces();
		}
	}
	
	protected void selectedTwoPieces() {
		// if we had 2 selected, reset stuff so we can start this new pair fresh
		if( _twoPiecesSelected == true ) unselectedTwoPieces();
		// P.println("2 pieces selected!");
		_twoPiecesSelected = true;
		_matchHeldStartTime = p.millis();
	}
	
	protected void unselectedTwoPieces() {
		// P.println("2 UNselected");
		_twoPiecesSelected = false;
		_matchHeldStartTime = 0;
	}
	
	/**
	 * Check to see if two pieces have matched after the match timeout has been reached.
	 * Also, draw controls... not sure if this should always be here...
	 */
	protected void checkForMatchComplete() {
		// check for a match timeout
		float controlDrawPercent = 0;
		if( _twoPiecesSelected == true ) {
			if( p.millis() - _matchHeldStartTime > MATCH_HELD_TIME ) {
				if( _pieces.get( _cursorLeftPieceID ).matchID() == _pieces.get( _cursorRightPieceID ).matchID() )
					piecesMatched( true );
				else
					piecesMatched( false );
			}
			controlDrawPercent = ( (float) p.millis() - _matchHeldStartTime ) / MATCH_HELD_TIME;
		}
		// draw hand cursor controls with percentage complete
		_controls.drawControls( controlDrawPercent );
	}
	
	/**
	 * Tell the pieces that were selected and attempted to match whether they match or not
	 */
	protected void piecesMatched( boolean didMatch ) {
		// kill or keep selected pieces
		for( int i=0; i < _pieces.size(); i++ ) {
			if( _pieces.get( i ).index() == _cursorLeftPieceID || _pieces.get( i ).index() == _cursorRightPieceID ) {
				// check actual match between piece IDs)
				_pieces.get( i ).matched( didMatch );
			}
		}
		// reset cursor hover IDs
		_cursorLeftPieceID = -1;
		_cursorRightPieceID = -1;
	}
	
	/**
	 * Check to see if two hand cursors are inside game pieces
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
	
	/**
	 * Draws the current game's time elapsed
	 */
	protected void drawTimer() {
		int seconds = P.round( ( p.millis() - _gameStartTime ) * 0.001f );
		int minutes = P.floor( seconds / 60f );
		int secondsOnly = seconds % 60;
		String secondsText = ( secondsOnly < 10 ) ? "0"+secondsOnly : ""+secondsOnly;
		String minutesText = ( minutes < 10 ) ? "0"+minutes : ""+minutes;
		p.image( MatchGameAssets.UI_YOUR_TIME, 862, 680 );
		MatchGameAssets.TIME_FONT_RENDERER.updateText( minutesText+":"+secondsText );
		p.image( MatchGameAssets.TIME_FONT_RENDERER.getTextPImage(), 830, 700 );
	}
	
	/**
	 * Reset game if all pieces are cleared
	 */
	protected void checkGameIsDone() {
		int numIncompletePieces = 0;
		for( int i=0; i < _pieces.size(); i++ ) {
			if( _pieces.get( i ).isActive() == true ) numIncompletePieces++;
		}
		if( numIncompletePieces == 0 ) {
			reset();
		}
	}
}
