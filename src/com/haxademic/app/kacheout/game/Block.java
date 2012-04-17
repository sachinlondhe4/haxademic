package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.util.ThreeDeeUtil;

public class Block {
	protected KacheOut p;
	// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
	protected AABB _box;
	float r,g,b;
	int index;
	protected boolean _active;
	protected TColor _color;
	protected ArrayList<Shard> _shards;
	protected ArrayList<Vec3D> _explodeVecs;
	protected float _scale;
	
	/**
 		this.w = w/2;
		this.h = h/2;
		this.x = x + w/2;
		this.y = y + h/2;
		
		_box = new AABB( w );
		_box.set( x, y, 0 );
		_box.setExtent( new Vec3D( this.w, this.h, 10 ) );
	 */
	
	public Block( AABB box, int index, float scale ) {
		p = (KacheOut)PAppletHax.getInstance();

		_box = box;
		this.index = index;
		
		// random colors for now
		r = p.random( 0, 80 );
		g = p.random( 200, 255 );
		b = p.random( 100, 200 );
		
		_color = p.gameColors().getRandomColor().copy();
		
		_active = true;
	}
	
	public boolean active() {
		return _active;
	}
	
	public AABB box() {
		return _box;
	}
	
	public void die( float speedX, float speedY ) {
		if( _active == true ) createShatteredMesh( speedX, speedY );
		_active = false;
	}
	
	protected void createShatteredMesh( float speedX, float speedY ) {
		if( _shards == null ) {
			_shards = new ArrayList<Shard>();
			_explodeVecs = new ArrayList<Vec3D>();
			for( int i=0; i < p.shatteredCubeMeshes.size(); i++ ) {
				_shards.add( new Shard( p.shatteredCubeMeshes.get( i ).copy(), _box.x, _box.y, _box.z ) );
				_shards.get(i).setSpeed( speedX*p.random(1.5f,3.5f), speedY*p.random(1.5f,3.5f), p.random(-2.5f,2.5f) );
			}
		}
	}
	
	public void display() {
		if( _active == true ) {
			// adjust cell z per brightness
			float zAdd = 6 + 50f * p._audioInput.getFFT().spectrum[index % 512];
//			_box.setExtent( arg0 )
			_box.setExtent( new Vec3D( 5, 5, zAdd ) );
			
			//p.rotateZ( _audioInput.getFFT().averages[1] * .01f );
			_color.alpha = p.constrain( 0.5f + zAdd, 0, 1 );
			p.fill( _color.toARGB() );
			p.noStroke();
			p._toxi.box( _box );
			
//			WETriangleMesh mesh1 = ( p.round( p.frameCount / 30f ) % 2 == 0 ) ? _invaderMesh_01 : _invaderMesh_01_alt;
//			DrawMesh.drawMeshWithAudio( p, mesh1, p.getAudio(), 3f, false, _color, _color, 0.25f );

		} else {
			if( _color.alpha > 0 ) {
				for( int j=0; j < _shards.size(); j++ ) {
					_shards.get( j ).update();
					p.fill( _color.toARGB() );
					p._toxi.mesh( _shards.get( j ).mesh() );
				}
			}
			_color.alpha = _color.alpha - 0.2f;
		}
	}
	
}
