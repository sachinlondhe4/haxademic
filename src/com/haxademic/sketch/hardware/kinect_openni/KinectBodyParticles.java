package com.haxademic.sketch.hardware.kinect_openni;

import java.util.ArrayList;

import toxi.color.TColor;
import toxi.geom.Vec3D;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.mesh.VectorFlyer;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.SkeletonsTracker;

@SuppressWarnings("serial")
public class KinectBodyParticles
extends PAppletHax {
	
	protected SkeletonsTracker _skeletonTracker;
	protected ArrayList<VectorFlyer> particles;
	
	public void setup() {
		super.setup();
		
		_skeletonTracker = new SkeletonsTracker();
		initBoxes();
	}
	
	protected void initBoxes() {
		particles = new ArrayList<VectorFlyer>();
		for( int i=0; i < 500; i++ ) {
			particles.add( new VectorFlyer( TColor.newHex("dddddd"), TColor.newHex("00ff00") ) );
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
		p.sphereDetail(10);

		p.pushMatrix();
		p.translate( 0, 0, -2000 );
		
		_skeletonTracker.update();
		
		DrawUtil.setDrawCenter(p);
		DrawUtil.setColorForPImage(p);
		p.image( p.kinectWrapper.getRgbImage(), p.width/2, -20, 640*8, 480*8 );
		p.popMatrix();
		
		// draw skeleton(s)
		_skeletonTracker.drawSkeletons();
		
		// drawn our own body parts
		int[] users = _skeletonTracker.getUserIDs();
		for(int i=0; i < users.length; i++) { 
			// draw body part
			drawJoint( _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_HEAD) );
			drawJoint( _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_LEFT_HAND) );
			drawJoint( _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND) );
			drawJoint( _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_LEFT_KNEE) );
			drawJoint( _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_RIGHT_KNEE) );
		}

		// draw particles
		int count = particles.size();
		for( int i=0; i < count; i++ ) {
			if( _skeletonTracker.hasASkeleton() ) findClosestAttractor( particles.get(i) );
			particles.get(i).update();
		}
	}
	
	protected void drawJoint( Vec3D pos ) {
		if( pos != null ) {
			p.pushMatrix();
			p.translate( pos.x, pos.y, pos.z );
			p.fill(255);
			p.noStroke();
			p.sphere(80f);
			p.popMatrix();
		}
	}
	
	protected void findClosestAttractor( VectorFlyer particle ) {
		// loop through attractors and store the closest & our distance for coloring
		float minDist = 999999;	
		int[] users = _skeletonTracker.getUserIDs();
		for(int i=0; i < users.length; i++) { 
			minDist = particleDistanceToBodyPart( minDist, particle, _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_HEAD) );
			minDist = particleDistanceToBodyPart( minDist, particle, _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_LEFT_HAND) );
			minDist = particleDistanceToBodyPart( minDist, particle, _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_RIGHT_HAND) );
			minDist = particleDistanceToBodyPart( minDist, particle, _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_LEFT_KNEE) );
			minDist = particleDistanceToBodyPart( minDist, particle, _skeletonTracker.getBodyPartVec3d(users[i], SimpleOpenNI.SKEL_RIGHT_KNEE) );
		}
	}
	
	protected float particleDistanceToBodyPart( float minDist, VectorFlyer particle, Vec3D bodyPartLoc ) {
		// check distance
		if( bodyPartLoc == null ) return minDist;
		if( bodyPartLoc.distanceTo(particle.position()) < minDist ) {
			particle.setTarget( bodyPartLoc );
			return bodyPartLoc.distanceTo(particle.position());
		} else {
			return minDist;
		}

	}

}
