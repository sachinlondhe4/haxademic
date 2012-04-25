package com.haxademic.sketch.obj;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.core.draw.mesh.MeshPool;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.draw.util.DrawMesh;
import com.haxademic.core.render.Renderer;
import com.haxademic.core.util.DebugUtil;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.OpenGLUtil;

public class ObjLoaderDemo 
extends PApplet
{
	ToxiclibsSupport toxi;
	PApplet p;
		
	Renderer _render;
	
//	OBJModel _model;
	MeshPool _objPool;
	WETriangleMesh _mesh;
	float _rot;
	int _meshIndex;
	ArrayList<String> _modelIds;
	
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
		
		// set up 3d objects pool
		_objPool = new MeshPool( p );
//		_objPool.loadObj( "SUBMISH_HORIZ", 		200, 	"./models/submish-rotated.obj" );
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
	}

	public void draw() {
		DrawUtil.setBasicLights( p );
		// draw background and set to center
		if( isSunflow == false ) p.background(0,0,0,255);
		p.translate(p.width/2, p.height/2, 0);
		
		// rotate with mouse
		_rot += p.TWO_PI / 360f;
		p.rotateZ(p.mouseX/100f);
		p.rotateY(p.mouseY/100f);
		
		// draw WETriangleMesh
		p.fill(255, 255);		// white
		p.fill(0,200,234, 255);	// mode set blue
		p.fill(255,249,0, 255);	// cacheflowe yellow
		p.noStroke();

		// draw to screen
		if( !isSunflow ) toxi.mesh( _mesh, true, 0 );
		
		
		
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
