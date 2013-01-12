
package com.haxademic.sketch.hardware.kinect_openni;

import java.util.ArrayList;

import toxi.geom.Line3D;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh.subdiv.SubdivisionStrategy;
import toxi.geom.mesh.subdiv.TriSubdivision;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.app.PAppletHax;
import com.haxademic.core.data.VectorFlyer;
import com.haxademic.core.hardware.kinect.SkeletonsTracker;
import com.haxademic.core.util.DrawUtil;

public class Kinect2dConnections
extends PAppletHax {
	
	protected SkeletonsTracker _skeletonTracker;
	protected ArrayList<VectorFlyer> particles;
	
	public void setup() {
		super.setup();
		
		// do something
		_skeletonTracker = new SkeletonsTracker();
		initBoxes();
	}
	
	protected void initBoxes() {
//		attractors = new ArrayList<Attractor>();
//		for( int i=0; i < 5; i++ ) {
//			attractors.add( new Attractor() );
//		}

		particles = new ArrayList<VectorFlyer>();
		for( int i=0; i < 1000; i++ ) {
			particles.add( new VectorFlyer() );
		}
	}
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "kinect_active", "true" );
		_appConfig.setProperty( "width", "640" );
		_appConfig.setProperty( "height", "480" );
	}
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);

		// draw webcam
		p.pushMatrix();
		p.translate( 0, 0, -2000 );
		
		_skeletonTracker.update();
		DrawUtil.setDrawCenter(p);
		DrawUtil.setColorForPImage(p);
		p.image( p.kinectWrapper.getRgbImage(), p.width/2, 0, 640*7, 480*7 );
		p.popMatrix();
		
		// draw skeleton(s)
		_skeletonTracker.drawSkeletons();
		
		// draw particles
		p.fill(255);
		if( _skeletonTracker.hasASkeleton() ) drawTriangles();

	}
	
	protected void drawTriangles() {
		// loop through attractors and store the closest & our distance for coloring
		int[] users = _skeletonTracker.getUserIDs();
		for(int i=0; i < users.length; i++) { 
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_HEAD),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HAND),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND)
					);
		}
	}
	
	protected float particleDistanceToBodyPart( float minDist, VectorFlyer particle, Vec3D bodyPartLoc ) {
		if( bodyPartLoc == null ) return minDist;
		if( bodyPartLoc.distanceTo(particle.position()) < minDist ) {
			particle.setTarget( bodyPartLoc );
			return bodyPartLoc.distanceTo(particle.position());
		} else {
			return minDist;
		}

	}
	
	public void draw3PointsTriangle( Vec3D point1, Vec3D point2, Vec3D point3 ) {
		// draw a line - currently disabled from noStroke()
		if( point1 == null || point2 == null || point3 == null ) return; 
		
//		Triangle3D tri = new Triangle3D(point1, point2, point3); 
//		toxi.triangle( tri );

		WETriangleMesh mesh = new WETriangleMesh();
		mesh.addFace( point1, point2, point3 );
		
		SubdivisionStrategy subdiv = new TriSubdivision();
		mesh.subdivide( subdiv );
		mesh.subdivide( subdiv );
		
		toxi.line( new Line3D( point1, point2 ) );
		toxi.line( new Line3D( point2, point3 ) );
		toxi.line( new Line3D( point3, point1 ) );
		
		toxi.mesh( mesh );
	}


	
}
