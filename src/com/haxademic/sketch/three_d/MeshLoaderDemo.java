package com.haxademic.sketch.three_d;

import geomerative.RFont;
import geomerative.RG;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.core.data.easing.EasingFloat3d;
import com.haxademic.core.debug.DebugUtil;
import com.haxademic.core.draw.mesh.MeshPool;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.render.Renderer;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.OpenGLUtil;

public class MeshLoaderDemo 
extends PApplet
{
	ToxiclibsSupport toxi;
	PApplet p;
		
	Renderer _render;
	
//	OBJModel _model;
	MeshPool _objPool;
	WETriangleMesh _mesh;
//	float _rot;
	EasingFloat3d _rot;
	int _meshIndex;
	ArrayList<String> _modelIds;
	boolean _wireFrame = false;
	
	boolean isSunflow = false;

	public void setup () {
		p = this;
		// set up stage and drawing properties
		if( isSunflow == true ) {
			p.size( 1200, 800, "hipstersinc.P5Sunflow" );				//size(screen.width,screen.height,P3D);
		} else {
			p.size( 1200, 800, PConstants.OPENGL );				//size(screen.width,screen.height,P3D);
			OpenGLUtil.SetQuality( p, OpenGLUtil.MEDIUM );
		}
		p.frameRate( 30 );
		p.colorMode( PConstants.RGB, 255, 255, 255, 255 );
		p.background( 0 );
		p.smooth();
		
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		toxi = new ToxiclibsSupport( p );
		
		// set up renderer
//		_render = new Renderer( this, 30, Renderer.OUTPUT_TYPE_MOVIE );
//		_render.startRenderer();
		
		_rot = new EasingFloat3d( 0, 0, 0, 10f );
		
		
		_objPool = new MeshPool( p );
		
		// 3d text with different options
		if( RG.initialized() == false ) RG.init( p );
		RFont font = new RFont( "../data/fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
		_objPool.addMesh( "HAI", MeshUtil.mesh2dFromTextFont( p, null, "../data/fonts/bitlow.ttf", 200, "HAI", -1, 2, 1f ), 1 );
		WETriangleMesh helloTextMesh = MeshUtil.mesh2dFromTextFont( p, font, null, -1, "HELLO", -1, 3, 1f );
		_objPool.addMesh( "HELLO", helloTextMesh, 1 );
		_objPool.addMesh( "HELLO_3D", MeshUtil.getExtrudedMesh( helloTextMesh, 20 ), 1 );
		
		// .svg vectors
		WETriangleMesh cacheSVG = MeshUtil.meshFromSVG( p, "../data/svg/cacheflowe-logo.svg", -1, 10, 1f );
		_objPool.addMesh( "CACHE", cacheSVG, 0.5f );
		_objPool.addMesh( "CACHE_EXTRUDE", MeshUtil.getExtrudedMesh( cacheSVG, 20 ), 1 );
		_objPool.addMesh( "DIAMOND_2D", MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/diamond.svg", -1, 3, 0.5f ), 20 ), 1 );
		_objPool.addMesh( "BIKE_COMMUTER", MeshUtil.meshFromSVG( p, "../data/svg/bike-commuter.svg", -1, 7, 0.5f ), 1 );
		
		// img models
		_objPool.addMesh( "KACHEOUT", MeshUtil.meshFromImg( p, "../data/images/kacheout/kacheout.gif", 1f ), 20f );
		_objPool.addMesh( "MUSIC_NOTE", MeshUtil.meshFromImg( p, "../data/images/music.gif", 1f ), 10f );
		_objPool.addMesh( "UFO", MeshUtil.meshFromImg( p, "../data/images/kacheout/invader-01.gif", 1f ), 30f );
		
		// .obj models
		_objPool.addMesh( "POINTER", MeshUtil.meshFromOBJ( p, "../data/models/pointer_cursor_2_hollow.obj", 1f ), 1.5f );
		_objPool.addMesh( "DIAMOND", MeshUtil.meshFromOBJ( p, "../data/models/diamond.obj", 1f ), 1.2f );
		_objPool.addMesh( "INVADER", MeshUtil.meshFromOBJ( p, "../data/models/invader.obj", 1f ), 45 );
		_objPool.addMesh( "LEGO_MAN", MeshUtil.meshFromOBJ( p, "../data/models/lego-man.obj", 1f ), 30 );
		_objPool.addMesh( "DISCOVERY", MeshUtil.meshFromOBJ( p, "../data/models/the-discovery-multiplied-seied.obj", 1f ), 900 );
		_objPool.addMesh( "SOCCER_BALL", MeshUtil.meshFromOBJ( p, "../data/models/soccer_ball.obj", 1f ), 100 );
		_objPool.addMesh( "TOPSECRET", MeshUtil.meshFromOBJ( p, "../data/models/topsecret-seied.obj", 1f ), 400 );
		_objPool.addMesh( "MODE_SET", MeshUtil.meshFromOBJ( p, "../data/models/mode-set.obj", 1f ), 150 );
		_objPool.addMesh( "SPIROGRAPH", MeshUtil.meshFromOBJ( p, "../data/models/spirograph-seied.obj", 1f ), 150 );
		_objPool.addMesh( "CACHEFLOWE", MeshUtil.meshFromOBJ( p, "../data/models/cacheflowe-3d.obj", 1f ), 150 );

//		_objPool.loadObj( "SUBMISH_HORIZ", 		200, 	"./models/submish-rotated.obj" );
//		_objPool.loadObj( "SHUTTLE", 			30, 	"./models/Space Shuttle.obj" );
//		_objPool.loadObj( "SPEAKER", 			200, 	"./models/speaker.obj" );
//		_objPool.loadObj( "HOUSE", 				150, 	"./models/monopoly-house.obj" );
//		_objPool.loadObj( "CAR_65", 			100, 	"./models/car65.obj" );
//		_objPool.loadObj( "BANANA", 			0.5f, 	"./models/banana.obj" );
		
		
		_modelIds = _objPool.getIds();
		_mesh = _objPool.getMesh( _modelIds.get( 0 ) );

//		ThreeDeeUtil.SmoothToxiMesh( p, _mesh, 2 );
		
		DebugUtil.showMemoryUsage();
		OpenGLUtil.SetQuality( p, OpenGLUtil.HIGH );
	}
	
	public void keyPressed() {
		// cycle through images
		if( key == ' ' ) {
			_meshIndex++;
			if( _meshIndex >= _modelIds.size() ) _meshIndex = 0;
			_mesh = _objPool.getMesh( _modelIds.get( _meshIndex ) );
		}
		if( key == 'w' ) {
			_wireFrame = !_wireFrame;
		}
	}

	public void draw() {
		DrawUtil.setBasicLights( p );
		// draw background and set to center
		if( isSunflow == false ) p.background(0,0,0,255);
		p.translate(p.width/2, p.height/2, 0);
		
		
		// rotate with mouse
		_rot.setTargetX( p.mouseX/100f );
		_rot.setTargetY( p.mouseY/100f );
		_rot.update();
		p.rotateZ( _rot.valueX() );
		p.rotateY( _rot.valueY() );
		
		// draw WETriangleMesh
		if( _wireFrame ) {
			p.stroke(255,249,0, 255);	// cacheflowe yellow
			p.noFill();
		} else {
			p.fill(255, 255);		// white
//			p.fill(0,200,234, 255);	// mode set blue
//			p.fill(255,249,0, 255);	// cacheflowe yellow
			p.noStroke();
		}

		// draw to screen
		toxi.mesh( _mesh );
		
		
		
		// render movie
		if( _render != null ) {
			_render.renderFrame();
			if( p.frameCount == 300 ) {
				p.println( "done!" );
				_render.stop();
				exit();
			} else {
				for( int i = 0; i < 100; i++ ) p.println( "rendering frame: " + p.frameCount );
			}
		}
	}
}
