package com.haxademic.app.matchgame.game;

import processing.core.PConstants;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.util.DrawUtil;

public class MatchGameControls {

	protected MatchGame p;
	
	public SimpleOpenNI  _kinectContext;

	
	public MatchGameControls( MatchGame p ) {
		this.p = p;
		init();
	}
	
	protected void init() {
		// Connect to Kinect
		// enable depthMap generation 
		// enable skeleton generation for all joints
//		_kinectContext = new SimpleOpenNI( p, SimpleOpenNI.RUN_MODE_MULTI_THREADED );
//		_kinectContext.enableDepth();
		_kinectContext = p.kinectWrapper.openni(); 
		_kinectContext.enableUser( SimpleOpenNI.SKEL_PROFILE_ALL, this );	// optional `this` routes OPENNI callbacks here instead of PApplet. nice.

//		size(_kinectContext.depthWidth(), _kinectContext.depthHeight()); 

	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		// update the cam
//		_kinectContext.update();

		// draw depthImageMap
		DrawUtil.setDrawCorner(p);
		p.image(_kinectContext.depthImage(),0,0);
		
		P.println("_kinectContext.getNumberOfUsers() :: "+_kinectContext.getNumberOfUsers());
//		P.println("_kinectContext.getUsers() :: "+_kinectContext.getUsers());

		// draw the skeleton if it's available
		if(_kinectContext.isTrackingSkeleton(1))
			drawSkeleton(1);
		if(_kinectContext.isTrackingSkeleton(2))
			drawSkeleton(2);
		if(_kinectContext.isTrackingSkeleton(3))
			drawSkeleton(3);

		DrawUtil.setDrawCenter(p);
	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId)
	{
		// find and project the hand positions to 2d space
		PVector jointPos = new PVector();
		PVector jointPosProjected = new PVector();
		float confidence = 0f;
		p.fill(0,255,255);

		DrawUtil.setDrawCorner(p);
		confidence = _kinectContext.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_LEFT_HAND,jointPos);
		if (confidence > 0.001f) {			
			_kinectContext.convertRealWorldToProjective(jointPos,jointPosProjected);
			p.ellipse(jointPosProjected.x, jointPosProjected.y, 35, 35);
		}
		P.println("left: "+jointPosProjected.x+","+jointPosProjected.y);
		
		confidence = _kinectContext.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_RIGHT_HAND,jointPos);
		if (confidence > 0.001f) {			
			_kinectContext.convertRealWorldToProjective(jointPos,jointPosProjected);
			p.ellipse(jointPosProjected.x, jointPosProjected.y, 35, 35);
		}
		P.println("right: "+jointPosProjected.x+","+jointPosProjected.y);

		// default limb drawing
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
		_kinectContext.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  
	}

	// -----------------------------------------------------------------
	// SimpleOpenNI events

	public void onNewUser(int userId)
	{
		P.println("onNewUser - userId: " + userId);
		P.println("  start pose detection");

		_kinectContext.startPoseDetection("Psi",userId);
		_kinectContext.requestCalibrationSkeleton(userId,true);
	}

	public void onLostUser(int userId)
	{
		P.println("onLostUser - userId: " + userId);
	}

	public void onStartCalibration(int userId)
	{
		P.println("onStartCalibration - userId: " + userId);
	}

	public void onEndCalibration(int userId, boolean successfull)
	{
		P.println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

		if (successfull) 
		{ 
			P.println("  User calibrated !!!");
			_kinectContext.startTrackingSkeleton(userId); 
		} 
		else 
		{ 
			P.println("  Failed to calibrate user !!!");
			P.println("  Start pose detection");
			_kinectContext.startPoseDetection("Psi",userId);
		}
	}

	public void onStartPose(String pose,int userId)
	{
		P.println("onStartPose - userId: " + userId + ", pose: " + pose);
		P.println(" stop pose detection");

		_kinectContext.stopPoseDetection(userId); 
		_kinectContext.requestCalibrationSkeleton(userId, true);

	}

	public void onEndPose(String pose,int userId)
	{
		P.println("onEndPose - userId: " + userId + ", pose: " + pose);
	}
}
