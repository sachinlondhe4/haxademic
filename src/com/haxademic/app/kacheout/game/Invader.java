package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.geom.AABB;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.shapes.Meshes;

public class Invader {
	
	protected KacheOut p;
	protected ArrayList<Block> _boxes;
	protected ArrayList<Block> _boxesAlt;
	protected ArrayList<Block> _curBoxesArray;
	
	protected int _x, _y;

	public Invader( int x, int y ) {
		p = (KacheOut)PAppletHax.getInstance();
		
		p.println("Invader: "+x+","+y);
		_x = x;
		_y = y;
		
		_boxes = new ArrayList<Block>();
		_boxesAlt = new ArrayList<Block>();
		ArrayList<AABB> boxes = Meshes.invader1Boxes( 0, 30 );
		for( int i=0; i < boxes.size(); i++ ) {
			boxes.get( i ).set( boxes.get( i ).x + _x, boxes.get( i ).y + _y, 0 );
			// p.println(boxes.get( i ).x + _x+","+boxes.get( i ).y);
			_boxes.add( new Block( boxes.get( i ), i) );
		}
		
		ArrayList<AABB> boxesAlt = Meshes.invader1Boxes( 0, 30 );
		for( int i=0; i < boxesAlt.size(); i++ ) _boxesAlt.add( new Block( boxes.get( i ), i) );
		
		_curBoxesArray = _boxes;
	}
	
	public void display() {
		for( int i=0; i < _curBoxesArray.size(); i++ ) {
			if( _curBoxesArray.get( i ).active() == true ) {
//				p.fill( 255 );
//				p.noStroke();
//				p._toxi.box( _curBoxesArray.get( i ).box() );
				_curBoxesArray.get( i ).display();
			}
		}
	}
	
	public boolean detectCollisions( Ball ball ) {
		boolean collided = false;
		for( int i=0; i < _curBoxesArray.size(); i++ ) {
			if( _curBoxesArray.get( i ).active() == true && ball.detectBox( _curBoxesArray.get( i ).box() ) == true ) {
				_curBoxesArray.get( i ).die();
				collided = true;
				
			}
		}
		return collided;
	}
	
}
