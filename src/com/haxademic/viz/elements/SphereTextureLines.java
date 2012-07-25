package com.haxademic.viz.elements;

import processing.core.PApplet;
import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.data.easing.EasingFloat3d;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.MathUtil;
import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IAudioTexture;
import com.haxademic.viz.IVizElement;
import com.haxademic.viz.textures.ColumnAudioTexture;

public class SphereTextureLines 
extends ElementBase 
implements IVizElement {
	
	protected float _baseRadius;
	
	IAudioTexture _texture;

	Sphere _sphere, _sphereOuter;
	WETriangleMesh _sphereMesh;
	protected final float _ninteyDeg = P.PI / 2f;
	protected EasingFloat3d _rotation = new EasingFloat3d( 0, 0, 0, 5f );


	public SphereTextureLines( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}

	public void init() {
		setDrawProps( 200 );
		_texture = new ColumnAudioTexture( 32 );
//		 _texture = new EQGridTexture( 512, 512 );
//		 _texture = new EQSquareTexture( 512, 512 );
	}
	
	public void setDrawProps( float baseRadius ) {
		_baseRadius = baseRadius;
		createNewSphere();
	}
	
	protected void createNewSphere() {
		_sphere = new Sphere( _baseRadius );
		AABB box = new AABB( _baseRadius );
		_sphereMesh = new WETriangleMesh();
		_sphereMesh.addMesh( _sphere.toMesh( 30 ) );
		MeshUtil.calcTextureCoordinates( _sphereMesh );
	}
	
	public void updateCamera() {
		// random 45 degree angles
		_rotation.setTargetX( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
		_rotation.setTargetY( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
		_rotation.setTargetZ( _ninteyDeg/2f * MathUtil.randRange( 0, 8 ) );
	}
	
	public void updateColorSet( ColorGroup colors ) {
//		_baseColor = colors.getRandomColor().copy();
	}

	public void update() {
		p.pushMatrix();
		
		p.fill( 255 );
		p.noStroke();
		_texture.updateTexture( _audioData );

		
		p.translate( 0, 0, -400 );
		_rotation.update();
		p.rotateY( _rotation.valueX() );
		p.rotateX( _rotation.valueY() );
		p.rotateZ( _rotation.valueZ() );
	
		
		MeshUtil.drawToxiMesh( p, toxi, _sphereMesh, _texture.getTexture() );
		
		p.popMatrix();
	}

	public void reset() {

	}

	public void dispose() {
		super.dispose();
	}

}
