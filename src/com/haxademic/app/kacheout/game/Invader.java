package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.geom.AABB;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.shapes.Meshes;
import com.haxademic.core.draw.util.ThreeDeeUtil;

public class Invader {
	
	protected KacheOut p;
	protected ArrayList<Block> _boxes;
	protected ArrayList<Block> _boxesAlt;
	protected ArrayList<Block> _curBoxesArray;
	protected boolean _isAnimating = true;
	
	protected int _x, _y, _row;
	protected float _scale;
	

	public Invader( int x, int y, float scale, int row ) {
		p = (KacheOut)PAppletHax.getInstance();
		
		_x = x;
		_y = y;
		_row = row;
		_scale = scale;
		
		buildInvader();
		
		_curBoxesArray = _boxes;
	}
	
	protected void buildInvader() {
		// create 2 arrays of blocks
		_boxes = new ArrayList<Block>();
		_boxesAlt = new ArrayList<Block>();
		ArrayList<AABB> boxes = null, boxesAlt = null;
		if( _row % 3 == 0 ) {
			boxes = Meshes.invader1Boxes( 0, _scale );
			boxesAlt = Meshes.invader1Boxes( 1, _scale );
		} else if( _row % 3 == 1 ) {
			boxes = Meshes.invader2Boxes( 0, _scale );
			boxesAlt = Meshes.invader2Boxes( 1, _scale );
		} else {
			boxes = Meshes.invader3Boxes( 0, _scale );
			boxesAlt = Meshes.invader3Boxes( 1, _scale );
		}
		
		// populate arrays while positioning individual blocks to the center of this invader
		for( int i=0; i < boxes.size(); i++ ) {
			boxes.get( i ).set( boxes.get( i ).x + _x, boxes.get( i ).y + _y, 0 );
			_boxes.add( new Block( boxes.get( i ), i, _scale*100f) );
		}
		for( int i=0; i < boxesAlt.size(); i++ ) {
			boxesAlt.get( i ).set( boxesAlt.get( i ).x + _x, boxesAlt.get( i ).y + _y, 0 );
			_boxesAlt.add( new Block( boxesAlt.get( i ), i, _scale*100f) );
		}
	}
	
	public void display() {
		// animate
		if( _isAnimating == true && p.frameCount % 15 == 0 ) {
			_curBoxesArray = ( _curBoxesArray == _boxes ) ? _boxesAlt : _boxes;
		}
		// draw boxen
		for( int i=0; i < _curBoxesArray.size(); i++ ) {
			if( _curBoxesArray.get( i ).active() == true ) {
				_curBoxesArray.get( i ).display();
			} else {
				_curBoxesArray.get( i ).display();
			}
		}
	}
	
	public boolean detectCollisions( Ball ball ) {
		boolean collided = false;
		for( int i=0; i < _curBoxesArray.size(); i++ ) {
			if( _curBoxesArray.get( i ).active() == true && ball.detectBox( _curBoxesArray.get( i ).box() ) == true ) {
				_curBoxesArray.get( i ).die( ball.speedX(), ball.speedY() );
				collided = true;
				_isAnimating = false;
			}
		}
		return collided;
	}
	
}
