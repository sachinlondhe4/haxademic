package com.haxademic.core.hardware.kinect;

import java.awt.Rectangle;

import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Vec3D;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.app.matchgame.MatchGame;
import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat3d;

public class SkeletonsTracker {
	
	protected PAppletHax p;
	public SimpleOpenNI  _kinectContext;
	protected int _curUserId = -1;
	protected float _controlsMultiplier = 1;
	protected EasingFloat3d _handLeft;
	protected EasingFloat3d _handRight;
	public Rectangle handLeftRect;
	public Rectangle handRightRect;
	protected float CURSOR_DIAMETER = 36;
	protected float CURSOR_EASING_FACTOR = 7;
	
	protected boolean _userInGameArea = false;
		
	protected PVector _utilPVec = new PVector();
	protected PVector _utilPVec2 = new PVector();
	
	protected PImage testHead;
	protected PImage testHand;
	
	public SkeletonsTracker() {
		p = (PAppletHax) P.p;
		// Set Kinect user/skeleton tracking - most of the setup and updating happens in PAppletHax
		_kinectContext = p.kinectWrapper.openni();
		p.kinectWrapper.setMirror(true);
		enableSkeletonTracking();
		
		// set ratio of controls based on screen size vs kinect depth
		_handLeft = new EasingFloat3d( p.width/2, p.height/2, 0, CURSOR_EASING_FACTOR );
		_handRight = new EasingFloat3d( p.width/2, p.height/2, 0, CURSOR_EASING_FACTOR );
		handLeftRect = new Rectangle( 0, 0, 1, 1 );
		handRightRect = new Rectangle( 0, 0, 1, 1 );
	}

	/** 
	 * Main game play update loop
	 */
	public void update() {
		// find closest skeleton and only use that one
//		getClosestUser();
		
		// find hands & ease position
//		getHands( _curUserId );

		// draw the skeletons for debugging
//		drawSkeletons();
//		drawUserBlobs();

//		drawHead( _curUserId );
	}
		
	public EasingFloat3d getRightHandPos() {
		return _handRight;
	}
	
	public EasingFloat3d getLeftHandPos() {
		return _handLeft;
	}
	
	protected void enableSkeletonTracking() {
		_kinectContext.enableUser( SimpleOpenNI.SKEL_PROFILE_ALL, this );	// optional `this` routes OPENNI callbacks here instead of PApplet. nice.
	}
		
	public boolean hasASkeleton() {
		if (_kinectContext.getUsers().length > 0) return true;
		else return false;
//		getClosestUser();
//		if( _curUserId != -1 ) return true;
//		return false;
	}
	
	public void getClosestUser() {
		int[] users = _kinectContext.getUsers();
		if( users.length == 0 ) {
			_curUserId = -1;
		} else {
			_curUserId = -1;
			// use skeleton closest to center, who's in the min/mix distance boundaries
			// find & track closest user that is still on-screen userHasHands()
			float xDist = 999999f;
			boolean isInZBounds = false;
			for(int i=0; i < users.length; i++) { 
				_kinectContext.getCoM( users[i], _utilPVec );							// PVec comes back with real-world `z` in millimeters
				isInZBounds = ( _utilPVec.z > MatchGame.KINECT_MIN_DIST && _utilPVec.z < MatchGame.KINECT_MAX_DIST );
				// if in-z-bounds, user.x is legit, closer-to-center & has a good skeleton...
				if( isInZBounds == true && _utilPVec.x != 0.0 && _utilPVec.x < xDist && userHasHands( users[i] ) ) {
					_curUserId = users[i];
					xDist = _utilPVec.x;
					// P.println( "closest user: "+users[i]+" at "+zDist );
				}
			}
		}
		// P.println("curUser: "+_curUserId+" of "+_kinectContext.getUsers().length);
	}
	
	// returns true if OpenNI says we've got a left hand
	public boolean userHasHands( int userId ) {
		if( _kinectContext.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_LEFT_HAND, _utilPVec ) > 0.001f ) {
			return true;
		} else {
			// P.println("USER GONE!");
			return false;
		}
	}
		
 	public void drawHead( int userId )
	{
		float confidence = _kinectContext.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_HEAD, _utilPVec );
		_kinectContext.convertRealWorldToProjective( _utilPVec, _utilPVec2 );
		if (confidence > 0.001f) {
			DrawUtil.setColorForPImage( p );
			p.rect( _utilPVec2.x, _utilPVec2.y, 10, 10 );
		}
	}
	
 	public Vec3D getBodyPartVec3d( int userId, int bodyPartID )
	{
		if( _kinectContext.getJointPositionSkeleton( userId, bodyPartID, _utilPVec ) > 0.001f) {
			return new Vec3D( _utilPVec.x + p.width/2, -_utilPVec.y, -_utilPVec.z / 2f );
		} else {
			return null;
		}
	}
 	
 	public Vec3D getBodyPart2d( int userId, int bodyPartID )
	{
		if( _kinectContext.getJointPositionSkeleton( userId, bodyPartID, _utilPVec ) > 0.001f) {
			_kinectContext.convertRealWorldToProjective( _utilPVec, _utilPVec2 );
			return new Vec3D( _utilPVec2.x, _utilPVec2.y, 0 );
		} else {
			return null;
		}
	}
 	
 	public int[] getUserIDs() {
		return _kinectContext.getUsers();

 	}
	
	protected void drawUserBlobs() {
		int[] users = _kinectContext.getUsers();
		for(int i=0; i < users.length; i++) {
			if( _curUserId == users[i] ) {
				drawUserBlob( users[i], p.color(0, 255, 0, 255) );
			} else {
				drawUserBlob( users[i], p.color(0, 255, 0, 255) );
			}
		}
	}
	
	public void drawUserBlob( int user, int userColor ) {
		PImage userImg = p.createImage( 640, 480, P.ARGB );
		int[] kinectPixels = _kinectContext.getUsersPixels( user );
		for ( int i = 0; i < userImg.pixels.length; i++ ) {
			if( kinectPixels[i] == 1 ) userImg.pixels[i] = userColor;  
		}
		DrawUtil.setColorForPImage(p);
		p.image( userImg, 0, p.height - 480 );
	}
	
	public void drawSkeletons() {
		int[] users = _kinectContext.getUsers();
		for(int i=0; i < users.length; i++) {
//			if( _curUserId == users[i] ) {
//				p.stroke( 0, 255, 0 );
//			} else {
//				p.stroke( 255, 0, 0 );
//			}
			drawSkeleton( users[i] );
		}
	}


	// draw the skeleton with the selected joints
	public void drawSkeleton( int userId )
	{
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
