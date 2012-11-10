package com.haxademic.app.matchgame;

import processing.core.PApplet;

import com.haxademic.app.P;
import com.haxademic.app.PAppletHax;
import com.haxademic.app.matchgame.game.MatchGameAssets;
import com.haxademic.app.matchgame.game.MatchGameControls;
import com.haxademic.app.matchgame.game.MatchGamePlay;
import com.haxademic.core.cameras.common.ICamera;
import com.haxademic.core.hardware.kinect.KinectWrapper;
import com.haxademic.core.util.DrawUtil;

public class MatchGame
extends PAppletHax  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		// "--present",
		_hasChrome = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "com.haxademic.app.matchgame.MatchGame" });
	}

	// input
	public static float KINECT_MIN_DIST;
	public static float KINECT_MAX_DIST;
	public static float KINECT_WIDTH_PERCENT;
	public static int KINECT_TOP;
	public static int KINECT_BOTTOM;
	public static float K_PIXEL_SKIP = 6;
	protected boolean _isDebuggingKinect = false;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _gameWidth;
	protected int _numAverages = 32;

	protected ICamera _curCamera = null;
	
	// game state
	protected int _curMode;
	
	// game objects
	protected MatchGamePlay _gamePlay;
	protected MatchGameControls _controls;

	// game state
	protected int _gameState;
	protected int _gameStateQueued;	// wait until beginning on the next frame to switch modes to avoid mid-frame conflicts
	public static int GAME_ON = 3;
	public static int GAME_OVER = 4;
	public static int GAME_INTRO = 5;
	public static int GAME_INSTRUCTIONS = 6;
	
	protected float _cameraZFromHeight = 0;
	
	public void setup() {
		_customPropsFile = "../data/properties/matchgame.properties";
		super.setup();
		initGame();
	}

	public void initGame() {
		_stageWidth = width;
		_stageHeight = height;
		newCamera();
		
		initKinectOptions();
		buildGameObjects();
		
				
		// it's opposite day, since game mode triggers the next action
		if( _appConfig.getBoolean( "starts_on_game", true ) == true ) {
			setGameMode( GAME_ON );
		} else {
			setGameMode( GAME_INSTRUCTIONS );
		}
	}
	
	void initKinectOptions() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_WIDTH_PERCENT = _appConfig.getFloat( "kinect_width_percent", 0.5f );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		if(kinectWrapper != null) kinectWrapper.setMirror( true );
	}

	void buildGameObjects() {
		MatchGameAssets loader = new MatchGameAssets();
		_controls = new MatchGameControls();
		_gamePlay = new MatchGamePlay( _controls );
	}
	
	// HAXADEMIC STUFF --------------------------------------------------------------------------------------
	void newCamera() {
//		_curCamera = new CameraDefault( p, 0, 0, 0 );
//		_curCamera.setPosition( _stageWidth/2f, _stageHeight/2f, 0 );	// _cameraZFromHeight
//		_curCamera.setTarget( _stageWidth/2f, _stageHeight/2f, 0 );
//		_curCamera.reset();
	}
	
	protected void debugCameraPos() {
		P.println(-_stageWidth + p.mouseX*2);
//		_curCamera.setPosition( _stageWidth/2, _stageHeight/2, -_stageWidth + p.mouseX*2 );
	}
	
	// INPUT --------------------------------------------------------------------------------------

	protected void handleInput( boolean isMidi ) {
		super.handleInput( isMidi );
		if ( p.key == 'd' || p.key == 'D' ) {
			_isDebugging = !_isDebugging;
			if( kinectWrapper != null ) {
				kinectWrapper.enableRGB( !_isDebugging );
				kinectWrapper.enableDepth( !_isDebugging );
			}
		}
	}
	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	public int gameWidth() { return _gameWidth; }
	public int stageWidth() { return _stageWidth; }
	public int stageHeight() { return _stageHeight; }
	public float gameBaseZ() { return -_stageHeight; }
	public int gameState() { return _gameState; }
	public boolean isDebugging() { return _isDebugging; }
	
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void setGameMode( int mode ) {
//		p.println("next mode: "+mode);
		_gameStateQueued = mode;
	}
	
	public void swapGameMode() {
		_gameState = _gameStateQueued;
//		if( _gameState == GAME_INTRO ) {
//			_screenIntro.reset();
//			soundtrack.playIntro();
//		} else if( _gameState == GAME_INSTRUCTIONS ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) {
//				_gamePlays.get( i ).reset();
//			}
//			soundtrack.stop();
//			sounds.playSound( SFX_DOWN );
//			soundtrack.playInstructions();
//		} else if( _gameState == GAME_COUNTDOWN ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) {
//				_gamePlays.get( i ).startCountdown();
//			}
//			soundtrack.stop();
//		} else if( _gameState == GAME_ON ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) {
//				_gamePlays.get( i ).launchBall();
//			}
//			soundtrack.playNext();
//		} else if( _gameState == GAME_OVER ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
//			soundtrack.stop();
//			sounds.playSound( WIN_SOUND );
//		}
	}
		
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		p.shininess(1000f); 
		p.lights();
		p.background(0);	
		p.camera();
//		_curCamera.update();

		if( _gameState != _gameStateQueued ) swapGameMode();
		if( _gameState == GAME_INTRO ) {
//			_screenIntro.update();
		} else if( _gameState == GAME_ON ) {
			_controls.update();
			_gamePlay.update();
		}
		
		if( _isDebugging == true ) displayDebug();
	}
			
	protected void displayDebug() {
		if( p.frameCount % ( _fps * 60 ) == 0 ) {
			P.println( "time: "+P.minute()+":"+P.second() );
		}
	}
	
	protected void drawDebug() {
		// draw depth image
		DrawUtil.setCenter( p );
		p.translate( 0, 0, -1350 );
		p.fill(255, 255);
		p.noStroke();
		p.rect(0, 0, KinectWrapper.KWIDTH*1.1f, KinectWrapper.KHEIGHT*1.1f);
		p.translate( 0, 0, 100 );
		p.rotateY( (float)Math.PI );
//		p.image( _kinectWrapper.getDepthImage(), 0, 0, _kinectWrapper.KWIDTH, _kinectWrapper.KHEIGHT );
		p.image( kinectWrapper.getDepthImage(), 0, 0, _stageWidth, _stageHeight );

	}

	
	
}
