package com.haxademic.sketch.hardware.kinect_openni;

import java.util.ArrayList;

import toxi.geom.Vec3D;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.app.PAppletHax;
import com.haxademic.core.data.VectorFlyer;
import com.haxademic.core.hardware.kinect.SkeletonsTracker;
import com.haxademic.core.util.DrawUtil;

public class KinectBodyParticles
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
		int count = particles.size();
		for( int i=0; i < count; i++ ) {
			if( _skeletonTracker.hasASkeleton() ) findClosestAttractor( particles.get(i) );
			particles.get(i).update();
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
		}
		
		
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);
//
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);
//
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
//
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);
//
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
//		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  

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

	
}
