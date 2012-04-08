
package com.haxademic.sketch.obj;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import saito.objloader.OBJModel;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.core.draw.model.ObjPool;
import com.haxademic.core.draw.util.DrawMesh;
import com.haxademic.core.draw.util.ThreeDeeUtil;
import com.haxademic.core.render.Renderer;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.OpenGLUtil;

public class Obj360SpinRender 
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
	
	boolean _isSunflow = false;
	boolean _isRendering = true;

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
		_objPool = new ObjPool( p );
//		_objPool.loadObj( "MODE_SET", 150, "../data/models/mode-set.obj" );		
		_objPool.loadObj( "CACHEFLOWE", 100, "../data/models/cacheflowe-3d.obj" );		
		
		_modelIds = _objPool.getIds();
		_model = _objPool.getModel( _modelIds.get( 0 ) );
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
		DrawMesh.drawObjModel( p, toxi, _model );
//		toxi.mesh( _mesh, true, 0 );
		
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
