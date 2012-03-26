package com.cacheart.obj;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.p5core.draw.model.ObjPool;
import com.p5core.draw.util.DrawMesh;
import com.p5core.render.Renderer;
import com.p5core.util.OpenGLUtil;

public class ObjLoaderDemo 
extends PApplet
{
	ToxiclibsSupport toxi;
	PApplet p;
		
	Renderer _render;
	
	OBJModel _model;
	ObjPool _objPool;
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
		p.shininess(1000); 
		p.lights();
		p.smooth();
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		toxi = new ToxiclibsSupport( p );
		
		// set up renderer
//		_render = new Renderer( this, 30, Renderer.OUTPUT_TYPE_MOVIE );
//		_render.startRenderer();
		
		// set up 3d objects pool
		_objPool = new ObjPool( p );
//		_objPool.loadObj( "SUBMISH_HORIZ", 		200, 	"./models/submish-rotated.obj" );
		_objPool.loadObj( "POINTER", 			1.5f, 	"../data/models/pointer_cursor_2_hollow.obj" );
		_objPool.loadObj( "DIAMOND", 			1.2f, 	"../data/models/diamond.obj" );
		_objPool.loadObj( "INVADER", 			45, 	"../data/models/invader.obj" );
		_objPool.loadObj( "LEGO_MAN", 			30, 	"../data/models/lego-man.obj" );
		_objPool.loadObj( "DISCOVERY", 			900, 	"../data/models/the-discovery-multiplied-seied.obj" );
		_objPool.loadObj( "SOCCER_BALL", 		100, 	"../data/models/soccer_ball.obj" );
		_objPool.loadObj( "TOPSECRET", 			400, 	"../data/models/topsecret-seied.obj" );
//		_objPool.loadObj( "SHUTTLE", 			30, 	"./models/Space Shuttle.obj" );
//		_objPool.loadObj( "SPEAKER", 			200, 	"./models/speaker.obj" );
//		_objPool.loadObj( "HOUSE", 				150, 	"./models/monopoly-house.obj" );
//		_objPool.loadObj( "MODE_SET", 			150, 	"./models/mode-set-3.obj" );
		_objPool.loadObj( "SPIROGRAPH",			150, 	"../data/models/spirograph-seied.obj" );
		
//		_objPool.loadObj( "CAR_65", 			100, 	"./models/car65.obj" );
//		_objPool.loadObj( "BANANA", 			0.5f, 	"./models/banana.obj" );
		
		
		_modelIds = _objPool.getIds();
		_model = _objPool.getModel( _modelIds.get( 0 ) );
		_mesh = _objPool.getMesh( _modelIds.get( 0 ) );

//		ThreeDeeUtil.SmoothToxiMesh( p, _mesh, 2 );
	}
	
	public void keyPressed() {
		// cycle through images
		if( key == ' ' ) {
			_meshIndex++;
			if( _meshIndex >= _modelIds.size() ) _meshIndex = 0;
			
			_model = _objPool.getModel( _modelIds.get( _meshIndex ) );
			_mesh = _objPool.getMesh( _modelIds.get( _meshIndex ) );
		}
	}

	public void draw() {
		// draw backgournd and set to center
		if( isSunflow == false ) p.background(0,0,0,255);
		p.translate(p.width/2, p.height/2, 0);
		
		// rotate with mouse
		_rot += p.TWO_PI / 360f;
		p.rotateZ(p.mouseX/100f);
		p.rotateY(p.mouseY/100f);
		
		// draw OBJModel
		p.translate(0,0,-150);
		p.fill(0,200,234, 255);
		p.fill(255, 255);
		p.noStroke();
		DrawMesh.drawObjModel( p, toxi, _model );
		
		// draw WETriangleMesh
		p.translate(0,0,300);
		p.stroke(255, 100f);
		p.strokeWeight(2);
		p.noFill();
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
