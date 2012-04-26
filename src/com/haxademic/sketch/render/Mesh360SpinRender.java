
package com.haxademic.sketch.render;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.core.draw.mesh.MeshPool;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.render.Renderer;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.OpenGLUtil;

public class Mesh360SpinRender 
extends PApplet
{
	ToxiclibsSupport toxi;
	PApplet p;
		
	Renderer _render;
	
	MeshPool _objPool;
	WETriangleMesh _mesh;
	OBJModel _model;
	float _rot;
	int _meshIndex;
	ArrayList<String> _modelIds;
	
	boolean _isSunflow = false;
	boolean _isRendering = false;

	public void setup () {
		p = this;
		
		// set up stage and drawing properties
		if( _isSunflow == true ) {
			p.size( 1280, 720, "hipstersinc.P5Sunflow" );
		} else {
			p.size( 1280, 720, PConstants.OPENGL );
			OpenGLUtil.SetQuality( p, OpenGLUtil.HIGH );
		}
		
		p.frameRate( 30 );
		p.colorMode( PConstants.RGB, 255, 255, 255, 255 );
		p.background( 0 );
		p.smooth();
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		
		toxi = new ToxiclibsSupport( p );
		
		// set up renderer
		if( _isRendering == true ) {
			_render = new Renderer( this, 30, Renderer.OUTPUT_TYPE_MOVIE, "bin/output/" );
			_render.startRenderer();
		}
		
		// set up 3d objects pool
//		_objPool.loadObj( "MODE_SET", 150, "../data/models/mode-set.obj" );
		
		_objPool = new MeshPool( p );
		_objPool.addMesh( "CACHEFLOWE", MeshUtil.meshFromOBJ( p, "../data/models/cacheflowe-3d.obj", 1f ), 100 );
		
		_model = new OBJModel( p, "../data/models/cacheflowe-3d.obj", OBJModel.RELATIVE );
		_model.scale( 100 );
		
//		if( RG.initialized() == false ) RG.init( p );
//		RFont font = new RFont( "../data/fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
//		WETriangleMesh helloTextMesh = MeshUtil.mesh2dFromTextFont( p, font, null, -1, "HELLO", 1f );
//		_objPool.addMesh( "HELLO_3D", MeshUtil.getExtrudedMesh( helloTextMesh, 20 ), 1 );

		
		_modelIds = _objPool.getIds();
		_mesh = _objPool.getMesh( _modelIds.get( 0 ) );

//		ThreeDeeUtil.SmoothToxiMesh( p, _mesh, 1 );
		
	}

	public void draw() {
		DrawUtil.setCenter( p );
		DrawUtil.setBasicLights( p );
		
		// draw background and set to center
		if( _isSunflow == false ) p.background(0,0,0,255);
		
		if(_isSunflow) p.translate(0,0,-150);
		else p.translate(0,0,-500);
		
		// rotate with time, in a full circle
		_rot -= p.TWO_PI / 360f;
		p.rotateY( _rot );
		p.rotateX( p.TWO_PI / 16f );
		
		// draw OBJModel
		p.fill(0,200,234, 255);	// mode set blue
		p.fill(255,249,0, 255);	// cacheflowe yellow
		p.noStroke();
//		p.rect( 0, 0, 2500, 1500 );
//		DrawMesh.drawObjModel( p, toxi, _model );
//		DrawMesh.drawToxiFaces( p, toxi, _mesh );
		toxi.mesh( _mesh, false, 0 );
		
		// render movie
		if( _isRendering == true && _render != null ) {
			_render.renderFrame();
			if( _rot <= -p.TWO_PI ) {
				p.println( "done!" );
				_render.stop();
				exit();
			} else {
				for( int i = 0; i < 100; i++ ) p.println( "rendering frame: " + p.frameCount );
			}
		}
	}
}
