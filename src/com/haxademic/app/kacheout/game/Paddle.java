package com.haxademic.app.kacheout.game;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.data.easing.EasingFloat;
import com.haxademic.core.draw.color.EasingTColor;

public class Paddle {
	protected EasingFloat _x, _y, _z;
	protected float _stageVPadding;
	protected float _height;
	protected float _width = 0;
	protected float _easing = 1.5f;
	protected float _center;
	protected float _xPosPercent;
	protected EasingTColor _color;
	protected AABB _box;
	protected KacheOut p;

	public Paddle() {
		p = (KacheOut)PAppletHax.getInstance();
		_center = ( p.gameWidth() + _width) / 2;
		_stageVPadding = p.stageHeight() * 0.04f;
		_xPosPercent = 0.5f;
		_width = (float)p.gameWidth() / 5f;
		_height = (float)p.stageHeight() * 0.025f;
		_x = new EasingFloat( _center, _easing );
		_y = new EasingFloat( p.stageHeight() - _stageVPadding, _easing );
		_color = new EasingTColor(new TColor(TColor.WHITE), 0.2f);
		_box = new AABB( 1 );
		_box.setExtent( new Vec3D( _width, _height, _height ) );
	} 
	
	public float x() { return _x.value(); }
	public float y() { return _y.value(); }
	public float height() { return _height; }
	public float xPosPercent() { return _xPosPercent; }

	public AABB box() {
		return _box;
	}

	public void setTargetXByPercent( float percent ) {
		_xPosPercent = percent;
		percent = 1 - percent;
		_x.setTarget( _width + percent * (p.gameWidth() - _width*2f) );
	}

	public boolean detectSphere( Sphere sphere ) {
		if( _box.intersectsSphere( sphere ) ) return true;
		return false;
	}
	
	public void hit() {
		_color.setCurColor( new TColor( TColor.GREEN ) );
		_color.setTargetColor( new TColor( TColor.WHITE ) );
	}

	public void display() {
		_x.update();
		_y.update();
		_color.update();
		
		_box.set( _x.value(), _y.value(), 0 );
		_box.rotateX( p.frameCount / 30f );
//		_color.alpha = 0.5f + p._audioInput.getFFT().averages[1];
		
		p.fill( _color.color().toARGB() );
		p.noStroke();
		p._toxi.box( _box ); 
	}
}
