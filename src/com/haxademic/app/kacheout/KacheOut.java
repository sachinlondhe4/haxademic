package com.haxademic.app.kacheout;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.AABB;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.game.GamePlay;
import com.p5core.audio.AudioLoopPlayer;
import com.p5core.cameras.CameraDefault;
import com.p5core.cameras.common.ICamera;
import com.p5core.data.FloatRange;
import com.p5core.hardware.kinect.KinectWrapper;
import com.p5core.util.ColorGroup;
import com.p5core.util.DebugUtil;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class KacheOut
extends PAppletHax  
{

	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		// "--present", 
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "com.haxademic.app.kacheout.KacheOut" });
	}

	// input
	protected float KINECT_MIN_DIST = 0.5f;
	protected float KINECT_MAX_DIST = 1.0f;
	protected float K_PIXEL_SKIP = 6;
	protected boolean _isKinectReversed = true;
	protected FloatRange _kinectPosition;
	protected ArrayList<FloatRange> _kinectPositions;
	protected boolean _isDebuggingKinect = false;

	// audio
	protected AudioLoopPlayer _audio;
	
	// debug 
	
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _gameWidth;
	protected int _numAverages = 32;

	protected ICamera _curCamera = null;
	
	// game state
	protected int _curMode;
	protected ColorGroup _gameColors;
	protected int _numPlayers = 2;
	protected ArrayList<GamePlay> _gamePlays;
	
	// game state
	protected int _gameState;
	public final int GAME_READY = 2;
	public final int GAME_ON = 3;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;

//	protected PAppletHax p;

	public void setup() {
		super.setup();
		
		_stageWidth = width;
		_stageHeight = height;
		_cameraZFromHeight = (float)_stageHeight * CAMERA_Z_WIDTH_MULTIPLIER;

		newCamera();
		
		_audioInput.setNumAverages( _numAverages );
		_audioInput.setDampening( .13f );
		
		initGame();
	}

	// HAXADEMIC STUFF --------------------------------------------------------------------------------------
	void newCamera() {
		_curCamera = new CameraDefault( p, 0, 0, 0 );
		_curCamera.setPosition( _stageWidth/2f, _stageHeight/2f, _cameraZFromHeight );
		_curCamera.setTarget( _stageWidth/2f, _stageHeight/2f, 0 );
		_curCamera.reset();
	}
	
	protected void debugCameraPos() {
		p.println(-_stageWidth + p.mouseX*2);
		_curCamera.setPosition( _stageWidth/2, _stageHeight/2, -_stageWidth + p.mouseX*2 );
	}

	public void beatDetect( int isKickCount, int isSnareCount, int isHatCount, int isOnsetCount ) {
	}
	
	// INPUT --------------------------------------------------------------------------------------

	protected void handleInput( boolean isMidi ) {
		super.handleInput( isMidi );
		if ( p.key == 'm' || p.key == 'M' ) {
			for( int i=0; i < _numPlayers; i++ ) _gamePlays.get( i ).launchBall();
			_gameState = GAME_ON;
		}
	}

	public void findKinectCenterX() {
		// sample several rows, finding the extents of objects within range
		int[] depthArray = _kinectWrapper.getDepthData();
		int offset = 0;
		int depthRaw = 0;
		float depthInMeters = 0;
		
		// loop through point grid and skip over pixels on an interval, finding the horizonal extents of an object in the appropriate range
		float kinectSegmentWidth = KinectWrapper.KWIDTH / _numPlayers;
		for( int i = 0; i < _numPlayers; i++ ) {
			float minX = -1f;
			float maxX = -1f;
			for ( int x = (int)( kinectSegmentWidth * i ); x <  kinectSegmentWidth + kinectSegmentWidth * i; x += K_PIXEL_SKIP ) {
				for ( int y = 120; y < 360; y += K_PIXEL_SKIP ) { // only use the vertical middle portion of the kinect data
					int xOffset = ( _isKinectReversed == true ) ? KinectWrapper.KWIDTH - 1 - x : x;
					offset = xOffset + y * KinectWrapper.KWIDTH;
					depthRaw = depthArray[offset];
					depthInMeters = _kinectWrapper.rawDepthToMeters( depthRaw );
					if( depthInMeters > KINECT_MIN_DIST && depthInMeters < KINECT_MAX_DIST ) {
						if( _isDebuggingKinect == true ) {
							p.fill( 255, 255, i*100 );
							p.noStroke();
							AABB box = new AABB( 4 );
							box.set( x, -5, 0 );
							_toxi.box( box );
						}

						// keep track of kinect range
						if( minX == -1 || x < minX ) {
							minX = x;
						}
						if( maxX == -1 || x > maxX ) {
							maxX = x;
						}
					} else {
						if( _isDebuggingKinect == true ) {
							p.fill( i*100, i*100, 255 );
							p.noStroke();
							AABB box = new AABB( 4 );
							box.set( x, -5, 0 );
							_toxi.box( box );
						}
					}
					if( depthInMeters > 0 ) {
					}
				}
			}
//			p.println("min/max "+i+": "+minX+" "+ maxX);
			_kinectPositions.get( i ).set( minX, maxX );
		}
	}
	
	protected void handleUserInput() {
		// update keyboard or Kinect, and pass the value to the paddle
		float paddleX = _stageWidth / 2;
		if( _kinectWrapper.isActive() == true ) {
			_kinectWrapper.update();
			findKinectCenterX();
			float kinectSegmentWidth = KinectWrapper.KWIDTH / _numPlayers;
			// send kinect data to games - calculate based off number of games vs. kinect width
			for( int i=0; i < _numPlayers; i++ ) {
				FloatRange playerKinectRange = _kinectPositions.get( i );	// _numPlayers - 1 - 
				if( playerKinectRange.center() != -1 ) {
					paddleX = MathUtil.getPercentWithinRange( kinectSegmentWidth * i, kinectSegmentWidth + i * kinectSegmentWidth, playerKinectRange.center() );
					_gamePlays.get( i ).updatePaddle( 1f - paddleX );
//					p.println(i+": "+playerKinectRange.min()+", "+playerKinectRange.max()+", "+playerKinectRange.center()+", "+paddleX);
				}
			}
		} else {
			for( int i=0; i < _numPlayers; i++ ) {
				FloatRange playerKinectRange = _kinectPositions.get( i );	// _numPlayers - 1 - 
				_gamePlays.get( i ).updatePaddle( 1f - MathUtil.getPercentWithinRange( 0, _stageWidth, p.mouseX ) );
//				paddleX = MathUtil.getPercentWithinRange( 0, _stageWidth, p.mouseX );
			}

		}
	}
	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	public int gameWidth() { return _gameWidth; }
	public int stageHeight() { return _stageHeight; }
	public int gameState() { return _gameState; }
	public ColorGroup gameColors() { return _gameColors; }
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
		
		_curCamera.update();
//		debugCameraPos();

		updateGame();
	}
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void initGame() {
		// set flags and props	
		pickNewColors();
		_gameState = GAME_READY;
		
		// init game objects
		_audio = new AudioLoopPlayer( p );
		_kinectPositions = new ArrayList<FloatRange>();
		_gamePlays = new ArrayList<GamePlay>();
		_gameWidth = _stageWidth / _numPlayers;
		for( int i=0; i < _numPlayers; i++ ) {
			_kinectPositions.add( new FloatRange( -1, -1 ) );
			_gamePlays.add( new GamePlay( _gameWidth * i , _gameWidth + _gameWidth * i ) );
		}
	}
	
	protected void updateGame() {
		// debug bg
//		p.pushMatrix();
//		p.noStroke();
//		p.fill( 255, 255, 255, 50 );
//		p.rect( _stageWidth/2, _stageHeight/2, _stageWidth, _stageHeight );
//		p.popMatrix();
		
		handleUserInput();
		updateGames();
		logDebugInfo();
	}
	
	protected void updateGames(){
		p.translate( 0,0,-400 );
		p.rotateX( p.PI / 16f );

		for( int i=0; i < _numPlayers; i++ ) {
			p.translate( i * ( _stageWidth / _numPlayers), 0 );
			_gamePlays.get( i ).update();
		}
	}
	
	protected void logDebugInfo(){
		if( p.frameCount % (30 * 10) == 0 ) {
			DebugUtil.showMemoryUsage();
		}
	}
	

	// Visual fun
	protected void pickNewColors() {
		// get themed colors
		if( _gameColors == null ) {
			_gameColors = new ColorGroup( ColorGroup.KACHE_OUT );
		}
		_gameColors.setRandomGroup();
	}

}
