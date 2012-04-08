package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.viz.elements.GridEQ;
import com.p5core.data.FloatRange;
import com.p5core.data.easing.EasingFloat;
import com.p5core.draw.shapes.Meshes;
import com.p5core.hardware.kinect.KinectWrapper;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class GamePlay {
	protected KacheOut p;
	// blocks
	protected int _gameLeft, _gameRight, _gameWidth;
	protected int _cols = 10;
	protected int _rows = 7;
	protected ArrayList<Block> _blocks;
	protected WETriangleMesh _invaderMesh_01, _invaderMesh_01_alt;
	
	// should be an array of balls
	protected Ball _ball;
	protected Paddle _paddle;
	protected Walls _walls;
	protected GridEQ _background;
	
	// controls
	protected float K_PIXEL_SKIP = 6;
	protected FloatRange _kinectRange;
	protected FloatRange _kinectCurrent;
	protected boolean _isKinectReversed = true;
	protected EasingFloat _gameRotation = new EasingFloat( 0, 10 );
	
	public GamePlay( int gameLeft, int gameRight, FloatRange kinectRange ) {
		p = (KacheOut)PAppletHax.getInstance();
		_gameLeft = gameLeft;
		_gameRight = gameRight;
		_gameWidth = gameRight - gameLeft;
		_kinectRange = kinectRange;
		_kinectCurrent = new FloatRange( -1, -1 );
		// create grid
		float boxW = _gameWidth / _cols;
		float boxH = p.stageHeight() / 2 / _rows;
		_blocks = new ArrayList<Block>();
		int index = 0;
		for (int i = 0; i < _cols; i++) {
			for (int j = 0; j < _rows; j++) {
				// Initialize each object
				_blocks.add( new Block( i*boxW, j*boxH, boxW, boxH, index ) );
				index++;
			}
		}
		
		_invaderMesh_01 = Meshes.invader1( 1 );
		_invaderMesh_01_alt = Meshes.invader1( 2 );
		_invaderMesh_01.scale( 70 );
		_invaderMesh_01_alt.scale( 70 );
		
		// create game objects
		_background = new GridEQ( p, p._toxi, p._audioInput );
		_background.updateColorSet( p.gameColors() );

		_ball = new Ball();
		_paddle = new Paddle();
		_walls = new Walls();
		
	}
	
	public void update( int gameIndex ) {
		positionGameCenter( gameIndex );
		drawBackground();
		updateControls();
		detectCollisions();
		drawGameObjects();
	}
	
	protected void positionGameCenter( int gameIndex ){
		DrawUtil.setTopLeft( p );
		p.translate( 0,0,-1200 );
		p.rotateX( p.PI / 16f );
		
		// rotate to kinect position
		// pivot from center
		p.translate( p.gameWidth() / 2 + gameIndex * p.gameWidth(), 0, 0 );
		// ease the rotation 
		float rotateExtent = p.PI / 10f;
		_gameRotation.setTarget( rotateExtent * _paddle.xPosPercent() - rotateExtent / 2f );
		_gameRotation.update();
		p.rotateY( _gameRotation.value() );
		// slide back half width
		p.translate( -p.gameWidth() / 2 , 0, 0 );
	}
	
	protected void drawBackground(){
		// draw bg
		p.pushMatrix();
		p.translate( 0, 0, -2000 );
		_background.update();
		p.popMatrix();
	}
	
	protected void findKinectCenterX() {
		// loop through point grid and skip over pixels on an interval, finding the horizonal extents of an object in the appropriate range
		float depthInMeters = 0;
		float minX = -1f;
		float maxX = -1f;
		
		// loop through kinect data within player's control range
		for ( int x = (int)_kinectRange.min(); x < (int)_kinectRange.max(); x += K_PIXEL_SKIP ) {
			for ( int y = p.KINECT_TOP; y < p.KINECT_BOTTOM; y += K_PIXEL_SKIP ) { // only use the vertical middle portion of the kinect data
				depthInMeters = p._kinectWrapper.getDepthMetersForKinectPixel( x, y, true );
				if( depthInMeters > p.KINECT_MIN_DIST && depthInMeters < p.KINECT_MAX_DIST ) {
					// keep track of kinect range
					if( minX == -1 || x < minX ) minX = x;
					if( maxX == -1 || x > maxX ) maxX = x;
				}
			}
//			p.println("min/max : "+minX+" "+ maxX);
			_kinectCurrent.set( minX, maxX );
		}
	}
	
	protected void updateControls() {
		// update keyboard or Kinect, and pass the value to the paddle
		float paddleX = 0;// = _stageWidth / 2;
		if( p._kinectWrapper.isActive() == true ) {
			findKinectCenterX();
			// send kinect data to games - calculate based off number of games vs. kinect width
			if( _kinectCurrent.center() != -1 ) {
				paddleX = MathUtil.getPercentWithinRange( _kinectRange.min(), _kinectRange.max(), _kinectCurrent.center() );
				_paddle.setTargetXByPercent( 1f - paddleX );
			}
		} else {
			_paddle.setTargetXByPercent( 1f - MathUtil.getPercentWithinRange( 0, p.gameWidth(), p.mouseX ) );
		}
	}
	
	protected void drawGameObjects() {
		// draw the blocks
		for (int i = 0; i < _blocks.size(); i++) {
			_blocks.get( i ).display();
		}
		// draw other objects
		_paddle.display();
		_walls.display();
		_ball.display( _paddle );
		
		if( p.isDebugging() == true ) {
			// draw point cloud
			p.pushMatrix();
			DrawUtil.setCenter( p );
			p.translate( 0, 0, -600 );
			p._kinectWrapper.drawPointCloudForRect( p, true, 8, 0.5f, p.KINECT_MIN_DIST, p.KINECT_MAX_DIST, p.KINECT_TOP, (int)_kinectRange.max(), p.KINECT_BOTTOM, (int)_kinectRange.min() );
			
			// draw debug positioning vertical lines
			p.pushMatrix();
			DrawUtil.setCenter( p );
			p.translate( -KinectWrapper.KWIDTH/2, 0, -700 );
			p.rect(_kinectRange.min(), 0, 2, p.stageHeight());
			p.rect(_kinectRange.max(), 0, 2, p.stageHeight());
			p.fill( 255, 0, 0 );
			p.rect(_kinectCurrent.min(), 0, 2, p.stageHeight());
			p.rect(_kinectCurrent.max(), 0, 2, p.stageHeight());
			p.fill( 0, 255, 0 );
			p.rect(_kinectCurrent.center(), 0, 2, p.stageHeight());
			p.popMatrix();
	
			p.popMatrix();
		}
	}
	
	public void launchBall() {
		_ball.launch( _paddle );
	}
	
	public void detectCollisions() {
		// paddle
		if( _ball.detectBox( _paddle.box() ) == true ) {
			_ball.bounceOffPaddle( _paddle );
		}
		// walls
		if( _walls.detectSphere( _ball.sphere() ) == true ) {
			_ball.detectWalls( _walls.leftHit(), _walls.topHit(), _walls.rightHit() );
			_walls.resetCollisions();
		}
		// blocks
		for (int i = 0; i < _blocks.size(); i++) {
			if( _blocks.get( i ).active() == true && _ball.detectBox( _blocks.get( i ).box() ) == true ) {
				_blocks.get( i ).die();
			}
		}
	}

}
