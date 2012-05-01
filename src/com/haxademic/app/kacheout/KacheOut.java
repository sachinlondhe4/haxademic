package com.haxademic.app.kacheout;

import geomerative.RFont;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.game.GamePlay;
import com.haxademic.app.kacheout.game.Soundtrack;
import com.haxademic.app.kacheout.media.AssetLoader;
import com.haxademic.app.kacheout.media.PhotoBooth;
import com.haxademic.app.kacheout.screens.IntroScreen;
import com.haxademic.core.audio.AudioLoopPlayer;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.cameras.CameraDefault;
import com.haxademic.core.cameras.common.ICamera;
import com.haxademic.core.data.FloatRange;
import com.haxademic.core.hardware.kinect.KinectWrapper;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.DrawUtil;

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
	public static float KINECT_MIN_DIST = 1.5f;
	public static float KINECT_MAX_DIST = 2.0f;
	public static int KINECT_TOP = 100;
	public static int KINECT_BOTTOM = 440;
	public static float KINECT_GAP_PERCENT = 0.75f;
	protected boolean _isDebuggingKinect = false;

	// audio
	protected AudioLoopPlayer _audio;
	public AudioPool sounds;
	public Soundtrack soundtrack;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _gameWidth;
	protected int _numAverages = 32;

	protected ICamera _curCamera = null;
		
	// mesh IDs
	public static String CREATE_DENVER = "CREATE_DENVER";
	public static String PRESENTS_TEXT = "PRESENTS_TEXT";
	public static String KACHEOUT_LOGO = "KACHEOUT_LOGO";
	public static String UFO = "UFO";
	public static String MODE_SET_LOGO = "MODE_SET_LOGO";
	public static String MODE_SET_LOGOTYPE = "MODE_SET_LOGOTYPE";
	public static String CACHEFLOWE_LOGO = "CACHEFLOWE_LOGO";
	public static String CACHEFLOWE_LOGOTYPE = "CACHEFLOWE_LOGOTYPE";
	public static String DESIGN_BY = "DESIGN_BY";
	public static String JON_DESIGN = "JON_DESIGN";
	public static String RYAN_DESIGN = "RYAN_DESIGN";
	public static String COUNTDOWN_TEXT_1 = "COUNTDOWN_TEXT_1";
	public static String COUNTDOWN_TEXT_2 = "COUNTDOWN_TEXT_2";
	public static String COUNTDOWN_TEXT_3 = "COUNTDOWN_TEXT_3";
	public static String WIN_TEXT = "WIN_TEXT";
	public static String LOSE_TEXT = "LOSE_TEXT";
	
	// game state
	protected int _curMode;
	protected ColorGroup _gameColors;
	protected final int NUM_PLAYERS = 2;
	protected ArrayList<GamePlay> _gamePlays;
	protected GamePlay _player1;
	protected GamePlay _player2;
	
	// non-gameplay screens
	protected IntroScreen _screenIntro;
	
	// game state
	protected int _gameState;
	protected int _gameStateQueued;	// wait until beginning on the next frame to switch modes to avoid mid-frame conflicts
	public static int GAME_READY = 2;
	public static int GAME_ON = 3;
	public static int GAME_OVER = 4;
	public static int GAME_INTRO = 5;
	public static int GAME_INSTRUCTIONS = 6;
	public static int GAME_COUNTDOWN = 7;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;
	
	public void setup() {
		super.setup();
		
		_stageWidth = width;
		_stageHeight = height;
		_gameWidth = _stageWidth / NUM_PLAYERS;
//		_cameraZFromHeight = (float)_stageHeight * CAMERA_Z_WIDTH_MULTIPLIER;

		newCamera();
		
		_audioInput.setNumAverages( _numAverages );
		_audioInput.setDampening( .13f );
		
		sounds = new AudioPool( p, p._minim );
		sounds.loadAudioFile( "PADDLE_BOUNCE", 1, "wav/kacheout/ball_hit_wall_v03.wav" );
		sounds.loadAudioFile( "WALL_BOUNCE", 1, "wav/kacheout/ball_hit_wall_v02.wav" );
		sounds.loadAudioFile( "INSERT_COIN", 1, "wav/kacheout/sfx/insert coin.wav" );
		
		soundtrack = new Soundtrack();
//		_soundtrack.playNext();
		_audio = new AudioLoopPlayer( p );
		
		kinectWrapper.enableDepth( true );
		kinectWrapper.enableDepthImage( true );

		_screenIntro = new IntroScreen();
				
		initGame();
	}

	public void initGame() {
		// set flags and props	
		pickNewColors();
		
		// init game objects
		AssetLoader loader = new AssetLoader();
		
		float kinectRangeWidth = KinectWrapper.KWIDTH / 2f * KINECT_GAP_PERCENT;
		_player1 = new GamePlay( 0, 0, _gameWidth, new FloatRange( 0, kinectRangeWidth ) );
		_player2 = new GamePlay( 1, _gameWidth, _gameWidth * 2, new FloatRange( KinectWrapper.KWIDTH - kinectRangeWidth, KinectWrapper.KWIDTH ) );
		_gamePlays = new ArrayList<GamePlay>();
		_gamePlays.add( _player1 );
		_gamePlays.add( _player2 );
		
		//setGameMode( GAME_INTRO );
		setGameMode( GAME_ON );
	}
		
	// HAXADEMIC STUFF --------------------------------------------------------------------------------------
	void newCamera() {
		_curCamera = new CameraDefault( p, 0, 0, 0 );
		_curCamera.setPosition( _stageWidth/2f, _stageHeight/2f, 0 );	// _cameraZFromHeight
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
		if ( p.key == 'd' || p.key == 'D' ) {
			_isDebugging = !_isDebugging;
			kinectWrapper.enableRGB( !_isDebugging );
			kinectWrapper.enableDepthImage( !_isDebugging );
		}
	}
	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	public int gameWidth() { return _gameWidth; }
	public int stageWidth() { return _stageWidth; }
	public int stageHeight() { return _stageHeight; }
	public float gameBaseZ() { return -_stageHeight; }
	public int gameState() { return _gameState; }
	public ColorGroup gameColors() { return _gameColors; }
	public boolean isDebugging() { return _isDebugging; }
	
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void setGameMode( int mode ) {
		//p.println("next mode: "+mode);
		_gameStateQueued = mode;
	}
	
	public void swapGameMode() {
		_gameState = _gameStateQueued;
		if( _gameState == GAME_INTRO ) {
			_screenIntro.reset();
		} else if( _gameState == GAME_READY ) {
			// TODO: MAKE THE INSTRUCTION/COUNTDOWN SCREENS HERE
			setGameMode( GAME_ON );
		} else if( _gameState == GAME_ON ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).reset();
			}
			_player1.launchBall();
			_player2.launchBall();
			soundtrack.playNext();
		} else if( _gameState == GAME_OVER ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
		}
	}
		
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
		
		_curCamera.update();

		if( _gameState != _gameStateQueued ) swapGameMode();
		if( _gameState == GAME_INTRO ) {
			_screenIntro.update();
		} else if( _gameState == GAME_ON || _gameState == GAME_OVER ) {
			p.pushMatrix();
			updateGames();
			p.popMatrix();
		}
		
		if( _isDebugging == true ) displayDebug();
	}
	
	protected void updateGames(){
		// update all games before checking for complete. also take screenshot if the game's over and the time is right
		boolean takeScreenShot = false;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).update( i );
			if( _gamePlays.get( i ).shouldTakeScreenshot() == true ) takeScreenShot = true;
		}
		if( takeScreenShot == true ) PhotoBooth.snapGamePhoto( p, _stageWidth, _stageHeight );
		// check for complete
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).hasClearedBoard() == true && _gameState == GAME_ON ) {
				setGameMode( GAME_OVER );
			}
		}
	}
	
	protected void displayDebug() {
//		debugCameraPos();
		
		// draw depth image
		DrawUtil.setCenter( p );
		p.translate( 0, 0, -1350 );
		p.fill(255, 255);
		p.noStroke();
		p.rect(0, 0, kinectWrapper.KWIDTH*1.1f, kinectWrapper.KHEIGHT*1.1f);
		p.translate( 0, 0, 100 );
		p.rotateY( (float)Math.PI );
//		p.image( _kinectWrapper.getDepthImage(), 0, 0, _kinectWrapper.KWIDTH, _kinectWrapper.KHEIGHT );
		p.image( kinectWrapper.getDepthImage(), 0, 0, _stageWidth, _stageHeight );
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
