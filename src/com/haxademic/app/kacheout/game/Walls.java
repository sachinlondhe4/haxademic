package com.haxademic.app.kacheout.game;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;

public class Walls {
	protected KacheOut p;
	protected AABB _wallLeft, _wallTop, _wallRight;
	protected boolean _wallLeftHit, _wallTopHit, _wallRightHit;
	protected TColor _color;
	protected float BASE_ALPHA = 0.2f;
	protected float _wallLeftAlpha = BASE_ALPHA;
	protected float _wallTopAlpha = BASE_ALPHA;
	protected float _wallRightAlpha = BASE_ALPHA;
	public final int WALL_WIDTH = 20;

	public Walls() {
		p = (KacheOut)PAppletHax.getInstance();
		_color = new TColor( TColor.WHITE );
		
		_wallLeft = new AABB( 1 );
		_wallLeft.set( 0, p.stageHeight() / 2f, 0 );
		_wallLeft.setExtent( new Vec3D( WALL_WIDTH, p.stageHeight() / 2f, WALL_WIDTH ) );

		_wallTop = new AABB( 1 );
		_wallTop.set( p.gameWidth() / 2f, 0, 0 );
		_wallTop.setExtent( new Vec3D( p.gameWidth() / 2, WALL_WIDTH, WALL_WIDTH ) );

		_wallRight = new AABB( 1 );
		_wallRight.set( p.gameWidth(), p.stageHeight() / 2f, 0 );
		_wallRight.setExtent( new Vec3D( WALL_WIDTH, p.stageHeight() / 2f, WALL_WIDTH ) );

	} 
	
	public boolean leftHit(){ return _wallLeftHit; }
	public boolean topHit(){ return _wallTopHit; }
	public boolean rightHit(){ return _wallRightHit; }
	
	public boolean detectSphere( Sphere sphere ) {
		_wallLeftHit = ( _wallLeft.intersectsSphere( sphere ) ) ? true : false;
		_wallTopHit = ( _wallTop.intersectsSphere( sphere ) ) ? true : false;
		_wallRightHit = ( _wallRight.intersectsSphere( sphere ) ) ? true : false;
		if( _wallLeftHit == true || _wallTopHit == true || _wallRightHit == true ) {
			if( _wallLeftHit == true ) _wallLeftAlpha = 1;
			if( _wallTopHit == true ) _wallTopAlpha = 1;
			if( _wallRightHit == true ) _wallRightAlpha = 1;
			return true;
		}
		return false;
	}
	
	public void resetCollisions() {
		_wallLeftHit = false;
		_wallTopHit = false;
		_wallRightHit = false;
	}

	public void display() {
		if( _wallLeftAlpha > BASE_ALPHA ) _wallLeftAlpha -= 0.1f;
		if( _wallTopAlpha > BASE_ALPHA ) _wallTopAlpha -= 0.1f;
		if( _wallRightAlpha > BASE_ALPHA ) _wallRightAlpha -= 0.1f;
		p.noStroke();
		_color.alpha = _wallLeftAlpha;
		p.fill( _color.toARGB() );
		p._toxi.box( _wallLeft ); 
		_color.alpha = _wallTopAlpha;
		p.fill( _color.toARGB() );
		p._toxi.box( _wallTop ); 
		_color.alpha = _wallRightAlpha;
		p.fill( _color.toARGB() );
		p._toxi.box( _wallRight ); 
	}
}
