
package com.haxademic.sketch.hardware.kinect_openni;

import java.awt.image.BufferedImage;

import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.mesh.VectorFlyer;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.SkeletonsTracker;
import com.haxademic.core.image.ImageUtil;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.GlowFilter;

@SuppressWarnings("serial")
public class Kinect2dConnections
extends PAppletHax {
	
	protected SkeletonsTracker _skeletonTracker;
	protected PGraphics _texture;
	
	public void setup() {
		super.setup();
		
		// do something
		_skeletonTracker = new SkeletonsTracker();
		_texture = P.p.createGraphics( p.width, p.height, P.P3D );
	}
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "rendering", "false" );
		_appConfig.setProperty( "kinect_active", "true" );
		_appConfig.setProperty( "width", "640" );
		_appConfig.setProperty( "height", "480" );
	}
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);

		_skeletonTracker.update();
		DrawUtil.setDrawCenter(p);
		
		// draw skeleton(s)
//		_skeletonTracker.drawSkeletons();
		
		// draw kaleidoscopes
		p.pushMatrix();
		float rotations = 8f;
		
		p.pushMatrix();
		p.translate(p.width/2, p.height/2);
//		drawWebCam(rotations);
		p.popMatrix();
		
		if( _skeletonTracker.hasASkeleton() ) {
			// set up draw colors
			p.fill(0, 255, 0, 40);
			p.stroke(127, 255, 127, 255);
			p.strokeWeight(2f);
			_texture.noFill();
			_texture.stroke(255);
			_texture.strokeWeight(3f);
			
			// generate kinect skeleton lines / texture, then draw to screen
			ImageUtil.clearPGraphics(_texture);
			drawTriangles();	
			
			p.translate(p.width/2, p.height/2);
			drawSkeletonLines(rotations);
		}

		p.popMatrix();
	}
	
	protected void drawWebCam( float rotations ) {
		// draw cam
		DrawUtil.setColorForPImage(p);
		DrawUtil.setPImageAlpha(p, 0.25f);
		PImage drawCamImg = p.kinectWrapper.getRgbImage();
//		PImage drawCamImg = getFilteredCam();
		for( int i=0; i < rotations; i++ ) {
			p.rotate((float)P.TWO_PI/rotations * (float)i);
			p.image( drawCamImg, 0, 0 );
		}
	}
	
	protected void drawSkeletonLines( float rotations ) {
		// draw kinect skeleton lines
		DrawUtil.setColorForPImage(p);
		DrawUtil.setPImageAlpha(p, 0.4f);
		for( int i=0; i < rotations; i++ ) {
			p.rotate((float)P.TWO_PI/rotations * (float)i);
			p.image( _texture, 0, 0 );
		}
	}
	
	protected void drawTriangles() {
		// loop through attractors and store the closest & our distance for coloring
		int[] users = _skeletonTracker.getUserIDs();
		for(int i=0; i < users.length; i++) { 
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_HEAD),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HAND)
					);

			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_ELBOW),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HAND),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_SHOULDER)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_ELBOW),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_SHOULDER)
					);
			
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_ELBOW),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_HEAD),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_NECK)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_ELBOW),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_HEAD),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_NECK)
					);
			
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_SHOULDER),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HAND)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_SHOULDER),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND)
					);
			
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_NECK),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_SHOULDER)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_NECK),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_SHOULDER)
					);

			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_KNEE),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HAND)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_KNEE),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND)
					);

			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_KNEE),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_FOOT)
					);
			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_KNEE),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_FOOT)
					);

			draw3PointsTriangle(
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_RIGHT_HIP),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_WAIST),
					_skeletonTracker.getBodyPart2d(users[i], SimpleOpenNI.SKEL_LEFT_HIP)
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
		
		// draw triangles
		Triangle3D tri = new Triangle3D(point1, point2, point3); 
		toxi.triangle( tri );

		// draw mesh
//		WETriangleMesh mesh = new WETriangleMesh();
//		mesh.addFace( point1, point2, point3 );
//		
//		SubdivisionStrategy subdiv = new TriSubdivision();
//		mesh.subdivide( subdiv );
//		mesh.subdivide( subdiv );
//		toxi.mesh( mesh );
		
		// draw lines
//		toxi.line( new Line3D( point1, point2 ) );
//		toxi.line( new Line3D( point2, point3 ) );
//		toxi.line( new Line3D( point3, point1 ) );		
		
		// draw lines into texture
//		_texture.beginDraw();
//		_texture.beginShape(P.TRIANGLES);
//		_texture.vertex( point1.x, point1.y, point1.z );
//		_texture.vertex( point2.x, point2.y, point2.z );
//		_texture.vertex( point3.x, point3.y, point3.z );
//		_texture.endShape();
//		_texture.endDraw();
	}
	
	protected PImage getFilteredCam() {
		// create native java image
		BufferedImage buff = ImageUtil.pImageToBuffered( p.kinectWrapper.getRgbImage() );
		
		// contrast
		ContrastFilter filt = new ContrastFilter();
		filt.setBrightness(1.2f);
		filt.setContrast(1.5f);
		filt.filter(buff, buff);
		
		// glow
		GlowFilter glow = new GlowFilter();
		glow.setRadius(20f);
		glow.filter(buff, buff);
		
		// contrast again
		filt.filter(buff, buff);
		
		// save processed image back to _curFrame
		return ImageUtil.bufferedToPImage( buff );
	}

	
}
