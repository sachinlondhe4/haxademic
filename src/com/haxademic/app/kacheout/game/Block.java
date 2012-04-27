package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Vec3D;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.color.EasingTColor;
import com.haxademic.core.util.MathUtil;

public class Block {
	protected KacheOut p;
	// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
	protected AABB _box, _boxOrigin;
	float r,g,b;
	int index;
	protected boolean _active;
	protected EasingTColor _color;
	protected TColor _colorStart;
	protected TColor _colorDead;
	protected ArrayList<Shard> _shards;
	protected ArrayList<Vec3D> _explodeVecs;
	protected float _scale, _speedX, _speedY;
	
	public Block( AABB box, int index, float scale, TColor color ) {
		p = (KacheOut)PAppletHax.getInstance();

		_box = box;
		_boxOrigin = _box.copy();
		this.index = index;
		_scale = scale;
		
		// random colors for now
		r = p.random( 0, 0 );
		g = p.random( 200, 255 );
		b = p.random( 0, 0 );
		
		// set up color fading
		_colorStart = color;//new TColor( TColor.GREEN );
		_colorDead = new TColor( TColor.WHITE );
		_color = new EasingTColor( _colorStart, 0.1f );
		
		reset();
	}
	
	public void reset() {
		_color.setTargetColor( _colorStart );
		_active = true;
	}
	
	public boolean active() {
		return _active;
	}
	
	public AABB box() {
		return _box;
	}
	
	public void die( float speedX, float speedY ) {
		//if( _active == true ) createShatteredMesh( speedX, speedY );
		if( _active == true ) {
			_speedX = speedX*p.random(1.5f,3.5f);
			_speedY = speedY*p.random(1.5f,3.5f);
			_color.setTargetColor( _colorDead );
			_active = false;
		}
	}
	
//	protected void createShatteredMesh( float speedX, float speedY ) {
//		if( _shards == null ) {
//			_shards = new ArrayList<Shard>();
//			_explodeVecs = new ArrayList<Vec3D>();
//			for( int i=0; i < p.shatteredCubeMeshes.size(); i++ ) {
//				_shards.add( new Shard( p.shatteredCubeMeshes.get( i ).copy(), _box.x, _box.y, _box.z ) );
//				_shards.get(i).setSpeed( speedX*p.random(2.f,4.f), speedY*p.random(2.f,4.f), p.random(-2.5f,2.5f) );
//			}
//		}
//	}
	
	public void display() {
		_color.update();
		if( _active == true ) {
			// ease box to origin
			_box.set( MathUtil.easeTo( _box.x, _boxOrigin.x, 8f ), MathUtil.easeTo( _box.y, _boxOrigin.y, 8f ), 0 );
			
			// adjust cell z per brightness
			float zAdd = 6 + 50f * p._audioInput.getFFT().spectrum[index % 512];
			_box.setExtent( new Vec3D( _scale/200f, _scale/200f, zAdd ) );
			
			_color.color().alpha = p.constrain( 0.5f + zAdd, 0, 1 );
			p.fill( _color.color().toARGB() );
			p.noStroke();
			p.toxi.box( _box );	
			
		} else {
			if( _box.y < p.stageHeight() ) {
				// gravity
				_speedY += 0.75f;
				// move box
				_box.set( _box.x + _speedX, _box.y + _speedY, 0 );
				
				p.fill( _color.color().toARGB() );
				p.noStroke();
				p.toxi.box( _box );
			}
		}
	}	
}
