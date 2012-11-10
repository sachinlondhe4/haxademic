package com.haxademic.app.matchgame.game;

import java.awt.Rectangle;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.util.DrawUtil;

public class MatchGameSquare {

	protected MatchGame p;
	
	public static int BOX_SIZE = 100; 
	public static int TOP_LEFT_PADDING = 50; 
	
	protected int _col = -1;
	protected int _row = -1;
	
	public Rectangle rect;

	public MatchGameSquare( int col, int row ) {
		p = (MatchGame) P.p;
		_col = col;
		_row = row;
		rect = new Rectangle( TOP_LEFT_PADDING + _col * BOX_SIZE, TOP_LEFT_PADDING + _row * BOX_SIZE, BOX_SIZE - 10, BOX_SIZE - 10 );
		init();
	}
	
	protected void init() {
		
	}

	/** 
	 * Main game play update loop
	 */
	public void update( boolean cursorOver ) {
		DrawUtil.setDrawCorner(p);
		p.pushMatrix();
		if( cursorOver ) {
			p.fill(0,255,0, 127);
		} else {
			p.fill(0,0,255, 127);
		}
		p.stroke(255,255,255, 127);
		p.rect( rect.x, rect.y, rect.width, rect.height );
		p.popMatrix();
		DrawUtil.setDrawCenter(p);
	}

}
