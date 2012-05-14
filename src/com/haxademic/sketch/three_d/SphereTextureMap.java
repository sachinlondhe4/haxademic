package com.haxademic.sketch.three_d;

import krister.Ess.AudioInput;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.app.P;
import com.haxademic.core.audio.AudioInputWrapper;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.viz.IAudioTexture;
import com.haxademic.viz.textures.ColumnAudioTexture;
import com.haxademic.viz.textures.EQSquareTexture;
import com.haxademic.viz.textures.WindowShadeTexture;

@SuppressWarnings("serial")
public class SphereTextureMap
extends PApplet 
{
	
	PApplet p;
	ToxiclibsSupport _toxi;
	IAudioTexture _texture;
	PImage _image;
//	PGraphics _graphics;
	Sphere _sphere, _sphereOuter;
	WETriangleMesh _sphereMesh, _sphereOuterMesh;
	AudioInputWrapper _audioInput;
	int _numEq = 512;
	boolean _useAudio = true;
	
	public SphereTextureMap() {
		p = this;
	}
	
	public void setup() {
		P.p = this;
		p.size( 800, 600, OPENGL );
		p.frameRate( 30 );
		p.colorMode( PConstants.RGB, 255, 255, 255, 1 );

		_toxi = new ToxiclibsSupport( p );
		_audioInput = new AudioInputWrapper( p, false );
		_audioInput.setNumAverages( _numEq );
		
		if( _useAudio == false ) {
			_image = p.loadImage("../data/images/globe-square.jpg");
		} else {
			_texture = new ColumnAudioTexture( _numEq );
			_texture = new EQSquareTexture( _numEq, _numEq );
			_texture = new WindowShadeTexture( _numEq, _numEq );
//			_image = new PImage( 1, _numEq );
//			_graphics = p.createGraphics( _numEq, _numEq, P.P3D );
		}
		
		_sphere = new Sphere( 100 );
		_sphereMesh = new WETriangleMesh();
		_sphereMesh.addMesh( _sphere.toMesh( 20 ) );
//		_sphereMesh.computeVertexNormals();

		_sphereOuter = new Sphere( 1250 );
		_sphereOuterMesh = new WETriangleMesh();
		_sphereOuterMesh.addMesh( _sphereOuter.toMesh( 30 ) );
//		_sphereOuterMesh.computeVertexNormals();

		calcTextureCoordinates( _sphereMesh );
		calcTextureCoordinates( _sphereOuterMesh );
	}
	
	public void draw() {
		p.background( 0 );
		p.noStroke();
		DrawUtil.setBasicLights( p );
		
		if( _useAudio == true ) updateWithAudio();
				
		p.translate( p.width/2f, p.height/2f );
		p.rotateY( p.mouseX/100f );
		p.rotateX( p.mouseY/100f );
		
		calcTextureCoordinates( _sphereMesh );

		drawToxiMesh( p, _toxi, _sphereMesh );
		drawToxiMesh( p, _toxi, _sphereOuterMesh );
	}
	
	public void updateWithAudio() {
		_texture.updateTexture( _audioInput );
	}
	
	public void drawToxiMesh( PApplet p, ToxiclibsSupport toxi, WETriangleMesh mesh ) {
		p.textureMode(P.NORMAL);
		_toxi.texturedMesh( mesh.toWEMesh(), _texture.getTexture(), true );
//		_toxi.texturedMesh( mesh.toWEMesh(), _graphics, true );
	}
	
	public void drawToxiFaces( PApplet p, ToxiclibsSupport toxi, WETriangleMesh mesh ) {
		p.textureMode(P.IMAGE);
		p.beginShape( P.TRIANGLES );
		p.texture( _image );

		// draw vertices, mapping PImage
		// http://en.wikipedia.org/wiki/UV_mapping
		// uv points spread out from the center of the image - use standard sphere UV mapping locations to multiply from there
		float halfW = (float)_image.width / 2f;
		float halfH = (float)_image.height / 2f;
		float mapW = (float)_image.width;
		float mapH = (float)_image.height;

		// loop through model's vertices
		for( Face f : mesh.getFaces() ) {
			float divisorA = (float) ( Math.sqrt( f.a.x * f.a.x ) + Math.sqrt( f.a.y * f.a.y ) + Math.sqrt( f.a.z * f.a.z ) );
			float uA = halfW + mapW * ( f.a.x / divisorA );
			float vA = halfH + mapH * ( f.a.y / divisorA );
			p.vertex( f.a.x, f.a.y, f.a.z, uA, vA );
			
			float divisorB = (float) ( Math.sqrt( f.b.x * f.b.x ) + Math.sqrt( f.b.y * f.b.y ) + Math.sqrt( f.b.z * f.b.z ) );
			float uB = halfW + mapW * ( f.b.x / divisorB );
			float vB = halfH + mapH * ( f.b.y / divisorB );
			p.vertex( f.b.x, f.b.y, f.b.z, uB, vB );
			
			float divisorC = (float) ( Math.sqrt( f.c.x * f.c.x ) + Math.sqrt( f.c.y * f.c.y ) + Math.sqrt( f.c.z * (float)f.c.z ) );
			float uC = halfW + mapW * ( f.c.x / divisorC );
			float vC = halfH + mapH * ( f.c.y / divisorC );
			p.vertex( f.c.x, f.c.y, f.c.z, uC, vC );
	   	}
		
		p.endShape();
	}
	
	void calcTextureCoordinates(WETriangleMesh mesh) {
		for( Face f : mesh.getFaces() ) {
			f.computeNormal();
			f.uvA = calcUV(f.a);
			f.uvB = calcUV(f.b);
			f.uvC = calcUV(f.c);
		}
	}
	
	Vec2D calcUV(Vec3D pos) {
		Vec3D s = pos.copy().toSpherical();
		Vec2D uv = new Vec2D( s.y / P.TWO_PI, ( 1.0f - ( s.z / P.PI + 0.5f ) ) );
		// make sure longitude is always within 0.0 ... 1.0 interval
		if (uv.x < 0) uv.x += 1f;
		else if (uv.x > 1) uv.x -= 1f;
		uv.x = P.abs(uv.x);
//		uv.x = P.constrain( uv.x, 0.000001f, 0.9999999f );
		return uv;
	}
	
	public float mapBoundsW( float num ) {
		return P.constrain( num, 0, _image.width );
	}

	public float mapBoundsH( float num ) {
		return P.constrain( num, 0, _image.height );
	}

	/**
	 * PApplet-level listener for AudioInput data from the ESS library
	 */
	public void audioInputData(AudioInput theInput) {
		_audioInput.getFFT().getSpectrum(theInput);
		_audioInput.detector.detect(theInput);
	}
}
