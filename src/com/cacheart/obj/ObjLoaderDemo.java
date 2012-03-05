package com.cacheart.obj;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import saito.objloader.OBJModel;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.subdiv.DualDisplacementSubdivision;
import toxi.geom.mesh.subdiv.SubdivisionStrategy;
import toxi.processing.ToxiclibsSupport;

import com.p5core.draw.util.ThreeDeeUtil;
import com.p5core.render.Renderer;
import com.p5core.util.OpenGLUtil;

public class ObjLoaderDemo 
extends PApplet
{
	ToxiclibsSupport toxi;
	PApplet p;
		
	Renderer _render;
	
	OBJModel _model;
	WETriangleMesh _mesh;
	float _rot;
	
	boolean isSunflow = false;

	public void setup ()
	{
		p = this;
		// set up stage and drawing properties
		if( isSunflow == true ) {
			p.size( 1200, 800, "hipstersinc.P5Sunflow" );				//size(screen.width,screen.height,P3D);
		} else {
			p.size( 1200, 800, PConstants.OPENGL );				//size(screen.width,screen.height,P3D);
			OpenGLUtil.SetQuality( p, OpenGLUtil.MEDIUM );
		}
		p.frameRate( 30 );
//		p.colorMode( PConstants.RGB, 255, 255, 255, 255 );
		p.background( 0 );
		//p.shininess(1000); 
//		p.lights();
//		p.noStroke();
		//p.noLoop();
//		p.smooth();
		
		
		p.rectMode(PConstants.CENTER);
		p.noStroke();
		toxi = new ToxiclibsSupport( p );
		
//		p.colorMode( PConstants.RGB, 1, 1, 1, 1 );

		// set up renderer
//		_render = new Renderer( this, 30, Renderer.OUTPUT_TYPE_MOVIE );
//		_render.startRenderer();
		
		
		
		loadObj();
		
		_mesh = ThreeDeeUtil.ConvertObjModelToToxiMesh( p, _model );
//		ThreeDeeUtil.SmoothToxiMesh( p, _mesh, 2 );
	}
	
	void loadObj() {
//		_model = new OBJModel( p, "./models/THEDISCOVERYMULTIPLIED.obj" );
//		_model = new OBJModel( p, "./models/ducky.obj" );
//		_model = new OBJModel( p, "./models/teapot.obj" );
//		_model = new OBJModel( p, "./models/skull.obj" );
//		_model = new OBJModel( p, "./models/Lego_Man.obj" );
//		_model = new OBJModel( p, "./models/pointer_cursor.obj" );
//		_model = new OBJModel( p, "./models/submish-horiz-rotated.obj" );
		_model = new OBJModel( p, "./models/submish-rotated.obj" );
//		_model = new OBJModel( p, "./models/banana.obj" );
//		_model = new OBJModel( p, "./models/pointer_cursor_2_hollow.obj" );
		_model.scale(200);
		_model.disableMaterial();
		_model.disableTexture();
		
		p.println("_model faces: "+_model.getFaceCount() );
	}

	public void draw() 
	{
		if( isSunflow == false ) p.background(0,0,0,255);
		p.translate(p.width/2, p.height/2, 0);
		
		// rotate
		_rot += p.TWO_PI / 360f;
		p.rotateZ(p.mouseX/100f);
		p.rotateY(p.mouseY/100f);
//		p.rotateX(p.PI/8f);
		
		// set color
		p.fill(255, 80);
		p.stroke(255, 50f);
		p.strokeWeight(3);
		p.noStroke();
		
		// loop through and set vertices
		Triangle3D tri;
		
		// loop through model's vertices
		for( int i = 0; i < _model.getFaceCount(); i++ ) {
//		for( int i = 2; i < _model.getFaceCount(); i+=3 ) {
			// get vertex
//			vert = _model.getVertex( i );
			PVector[] facePoints = _model.getFaceVertices( i );
			p.fill(20 * (p.TWO_PI / 360f)*i, 255);
//			p.fill( 255, 100);
		
//			mesh.addFace( 
//					new Vec3D( facePoints[0].x, facePoints[0].y, facePoints[0].z ), 
//					new Vec3D( facePoints[1].x, facePoints[1].y, facePoints[1].z ), 
//					new Vec3D( facePoints[2].x, facePoints[2].y, facePoints[2].z )
//				);

			tri = new Triangle3D( 
					new Vec3D( facePoints[0].x, facePoints[0].y, facePoints[0].z ), 
					new Vec3D( facePoints[1].x, facePoints[1].y, facePoints[1].z ), 
					new Vec3D( facePoints[2].x, facePoints[2].y, facePoints[2].z )
				);
			toxi.triangle( tri );

				// from 3rd point on, start connecting triangles
//				if( i >= 2 ) {
//					mesh.addFace( 
//							new Vec3D( _model.getVertex( i ).x, _model.getVertex( i ).y, _model.getVertex( i ).z ), 
//							new Vec3D( _model.getVertex( i-1 ).x, _model.getVertex( i-1 ).y, _model.getVertex( i-1 ).z ), 
//							new Vec3D( _model.getVertex( i-2 ).x, _model.getVertex( i-2 ).y, _model.getVertex( i-2 ).z ) 
//							);
//				}
//			}

		}		
//	    toxi.mesh( mesh, true, 0 );


		//_model.draw();
//		p.println("frame: "+p.frameCount);
		
		
		
		
		p.translate(0,0,300);
		p.fill(255,0,255,255);
		
		
//		toxi.mesh( _mesh, true, 0 );
		
		
		
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
