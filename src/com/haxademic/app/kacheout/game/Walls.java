package com.haxademic.app.kacheout.game;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.color.EasingTColor;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;

public class Walls {
	protected KacheOut p;
	protected AABB _wallLeft, _wallTop, _wallRight;
	protected boolean _wallLeftHit, _wallTopHit, _wallRightHit;
	protected EasingTColor _colorLeft, _colorTop, _colorRight;
	public final int WALL_WIDTH = 10;

	public Walls() {
		p = (KacheOut)PAppletHax.getInstance();
		_colorLeft = new EasingTColor( new TColor( TColor.WHITE ), 0.03f );
		_colorTop = new EasingTColor( new TColor( TColor.WHITE ), 0.03f );
		_colorRight = new EasingTColor( new TColor( TColor.WHITE ), 0.03f );
		
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
			if( _wallLeftHit == true ) {
				_colorLeft.setCurColor( new TColor( TColor.GREEN ) );
				_colorLeft.setTargetColor( new TColor( TColor.WHITE ) );
			}
			if( _wallTopHit == true ) {
				_colorTop.setCurColor( new TColor( TColor.RED ) );
				_colorTop.setTargetColor( new TColor( TColor.WHITE ) );
			}
			if( _wallRightHit == true ) {
				_colorRight.setCurColor( new TColor( TColor.YELLOW ) );
				_colorRight.setTargetColor( new TColor( TColor.WHITE ) );
			}
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
		p.noStroke();
		_colorLeft.update();
		_colorTop.update();
		_colorRight.update();
		p.fill( _colorLeft.color().toARGB() );
		p._toxi.box( _wallLeft ); 
		p.fill( _colorTop.color().toARGB() );
		p._toxi.box( _wallTop ); 
		p.fill( _colorRight.color().toARGB() );
		p._toxi.box( _wallRight ); 
	}
}
