package com.haxademic.app.kacheout.game;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;

import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Vec3D;

public class Block {
	protected KacheOut p;
	// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
	protected AABB _box;
	public float x,y,z;
	float w,h;
	float r,g,b;
	int index;
	protected boolean _active;
	protected TColor _color;
	
	/**
 		this.w = w/2;
		this.h = h/2;
		this.x = x + w/2;
		this.y = y + h/2;
		
		_box = new AABB( w );
		_box.set( x, y, 0 );
		_box.setExtent( new Vec3D( this.w, this.h, 10 ) );
	 */
	
	public Block( AABB box, int index ) {
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
	
	public void die() {
		_active = false;
	}
	
	public void display() {
		if( _active == true ) {
			// adjust cell z per brightness
			float zAdd = 40 * p._audioInput.getFFT().spectrum[index % 512];
			
			//p.rotateZ( _audioInput.getFFT().averages[1] * .01f );
			_color.alpha = p.constrain( 0.5f + zAdd, 0, 1 );
			p.fill( _color.toARGB() );
			p.noStroke();
			p._toxi.box( _box );
			
//			WETriangleMesh mesh1 = ( p.round( p.frameCount / 30f ) % 2 == 0 ) ? _invaderMesh_01 : _invaderMesh_01_alt;
//			DrawMesh.drawMeshWithAudio( p, mesh1, p.getAudio(), 3f, false, _color, _color, 0.25f );

		}
	}
}
