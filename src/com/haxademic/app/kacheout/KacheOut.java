package com.haxademic.app.kacheout;

import geomerative.RFont;
import geomerative.RG;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.game.GamePlay;
import com.haxademic.app.kacheout.game.Soundtrack;
import com.haxademic.core.audio.AudioLoopPlayer;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.cameras.CameraDefault;
import com.haxademic.core.cameras.common.ICamera;
import com.haxademic.core.data.FloatRange;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.hardware.kinect.KinectWrapper;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.ImageUtil;
import com.haxademic.core.util.ScreenUtil;
import com.haxademic.core.util.SystemUtil;

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
	public AudioPool _sounds;
	public Soundtrack _soundtrack;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _gameWidth;
	protected int _numAverages = 32;

	protected ICamera _curCamera = null;
	
	// texts
	protected RFont _fontHelloDenver;
	protected RFont _fontBitLow;
	protected WETriangleMesh _textCreateDenver;
	
	// game state
	protected int _curMode;
	protected ColorGroup _gameColors;
	protected final int NUM_PLAYERS = 2;
	protected ArrayList<GamePlay> _gamePlays;
	protected GamePlay _player1;
	protected GamePlay _player2;
	
	// game state
	protected int _gameState;
	public final int GAME_READY = 2;
	public final int GAME_ON = 3;
	public final int GAME_OVER = 4;
	public final int GAME_TITLE = 5;
	public final int GAME_INSTRUCTIONS = 6;
	public final int GAME_COUNTDOWN = 7;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;
	
	public void setup() {
		super.setup();
		
		_stageWidth = width;
		_stageHeight = height;
//		_cameraZFromHeight = (float)_stageHeight * CAMERA_Z_WIDTH_MULTIPLIER;

		newCamera();
		
		_audioInput.setNumAverages( _numAverages );
		_audioInput.setDampening( .13f );
		
		_sounds = new AudioPool( p, p._minim );
		_sounds.loadAudioFile( "PADDLE_BOUNCE", 1, "wav/kacheout/ball_hit_wall_v03.wav" );
		_sounds.loadAudioFile( "WALL_BOUNCE", 1, "wav/kacheout/ball_hit_wall_v02.wav" );
		
		_soundtrack = new Soundtrack();
		_soundtrack.playNext();
		
		_kinectWrapper.enableDepth( true );
		_kinectWrapper.enableDepthImage( true );
				
		initGame();
	}

	public void initGame() {
		// set flags and props	
		pickNewColors();
		_gameState = GAME_READY;
		
		// init game objects
		createMeshPool();
		_audio = new AudioLoopPlayer( p );
		
		_gameWidth = _stageWidth / NUM_PLAYERS;
		float kinectRangeWidth = KinectWrapper.KWIDTH / 2f * KINECT_GAP_PERCENT;
		_player1 = new GamePlay( 0, _gameWidth, new FloatRange( 0, kinectRangeWidth ) );
		_player2 = new GamePlay( _gameWidth, _gameWidth * 2, new FloatRange( KinectWrapper.KWIDTH - kinectRangeWidth, KinectWrapper.KWIDTH ) );
		_gamePlays = new ArrayList<GamePlay>();
		_gamePlays.add( _player1 );
		_gamePlays.add( _player2 );
	}
	
	protected void createMeshPool() {
		// make sure geomerative is ready
		if( RG.initialized() == false ) RG.init( p );
		
		// load fonts
		_fontHelloDenver = new RFont( "../data/fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
		_fontBitLow = new RFont( "../data/fonts/bitlow.ttf", 200, RFont.CENTER);
		
		// "create denver presents"
		p.meshPool.addMesh( "CREATE DENVER", MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f ), 1 );
		p.meshPool.addMesh( "PRESENTS_TEXT", MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "PRESENTS", -1, 2, 0.4f ), 1 );
//		_textCreateDenver = MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f ), 20 );
//		_textCreateDenver = MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f );
		
		// Kacheout logo
		p.meshPool.addMesh( "KACHEOUT_LOGO", MeshUtil.meshFromImg( p, "../data/images/kacheout/kacheout.gif", 1f ), 20f );
		p.meshPool.addMesh( "UFO", MeshUtil.meshFromImg( p, "../data/images/kacheout/invader-01.gif", 1f ), 30f );
		
		// cacheflowe / mode set
		p.meshPool.addMesh( "MODE_SET_LOGO", MeshUtil.meshFromOBJ( p, "../data/models/mode-set.obj", 1f ), 150 );
		p.meshPool.addMesh( "MODE_SET_LOGOTYPE", MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/modeset-logotype.svg", -1, 6, 0.7f ), 4 ), 1 );
		p.meshPool.addMesh( "CACHEFLOWE_LOGO", MeshUtil.meshFromOBJ( p, "../data/models/cacheflowe-3d.obj", 1f ), 150 );
		p.meshPool.addMesh( "CACHEFLOWE_LOGOTYPE", MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/cacheflowe-logotype.svg", -1, 6, 0.7f ), 4 ), 1 );

		// design credits
		p.meshPool.addMesh( "DESIGN_BY", MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "DESIGN BY:", -1, 2, 0.3f ), 1 );
		p.meshPool.addMesh( "JON_DESIGN", MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "JON TRAISTER", -1, 2, 0.4f ), 1 );
		p.meshPool.addMesh( "RYAN_DESIGN", MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "RYAN POLICKY", -1, 2, 0.4f ), 1 );
		
		// countdown
		p.meshPool.addMesh( "COUNTDOWN_TEXT_1", MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "1", -1, 2, 3f ), 4 ), 1 );
		p.meshPool.addMesh( "COUNTDOWN_TEXT_2", MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "2", -1, 2, 3f ), 4 ), 1 );
		p.meshPool.addMesh( "COUNTDOWN_TEXT_3", MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "3", -1, 2, 3f ), 4 ), 1 );
		
		// win/lose
		p.meshPool.addMesh( "WIN_TEXT", MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "WIN", -1, 2, 1f ), 20 ), 1 );
		p.meshPool.addMesh( "LOSE_TEXT", MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LOSE", -1, 2, 1f ), 20 ), 1 );

		
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
		if ( p.key == ' ' ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).launchBall();
			if( _gameState == GAME_OVER ) {
				resetGame();
			} else if( _gameState == GAME_READY ) {
				_player1.launchBall();
				_player2.launchBall();
				_gameState = GAME_ON;
				_soundtrack.playNext();
			}
		}
		if ( p.key == 'd' || p.key == 'D' ) {
			_isDebugging = !_isDebugging;
			_kinectWrapper.enableRGB( !_isDebugging );
			_kinectWrapper.enableDepthImage( !_isDebugging );
		}
	}
	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	public int gameWidth() { return _gameWidth; }
	public int stageHeight() { return _stageHeight; }
	public int gameState() { return _gameState; }
	public ColorGroup gameColors() { return _gameColors; }
	public boolean isDebugging() { return _isDebugging; }
	
	
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
		
		_curCamera.update();

		updateGame();
		
		// testing!
//		DrawUtil.setCenter( p );
//		p.translate( 0, 0, -1000 );
//		p.fill( 255 );
//		p._toxi.mesh( _textCreateDenver );
	
		if( _isDebugging == true ) displayDebug();
	}
	
	protected void displayDebug() {
//		debugCameraPos();
		
		// draw depth image
		DrawUtil.setCenter( p );
		p.translate( 0, 0, -1350 );
		p.fill(255, 255);
		p.noStroke();
		p.rect(0, 0, _kinectWrapper.KWIDTH*1.1f, _kinectWrapper.KHEIGHT*1.1f);
		p.translate( 0, 0, 100 );
		p.rotateY( (float)Math.PI );
//		p.image( _kinectWrapper.getDepthImage(), 0, 0, _kinectWrapper.KWIDTH, _kinectWrapper.KHEIGHT );
		p.image( _kinectWrapper.getDepthImage(), 0, 0, _stageWidth, _stageHeight );
	}
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	protected void updateGame() {
		// debug bg
//		p.pushMatrix();
//		p.noStroke();
//		p.fill( 255, 255, 255, 50 );
//		p.rect( _stageWidth/2, _stageHeight/2, _stageWidth, _stageHeight );
//		p.popMatrix();
		
		p.pushMatrix();
		updateGames();
		p.popMatrix();
	}
	
	protected void updateGames(){
		// update all games before checking for complete
		for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).update( i );
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).hasClearedBoard() == true ) {
				gameOver();
			}
		}
	}
	
	protected void gameOver() {
		if( _gameState != GAME_OVER ) {
			_gameState = GAME_OVER;
			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
			snapGamePhoto();
		}
	}	

	protected void resetGame() {
		_gameState = GAME_READY;

		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).reset();
		}
	}	
	
	protected void snapGamePhoto() {
		// save screenshot and open it back up
		String screenshotFile = ScreenUtil.screenshotToJPG( p, "bin/output/kacheout/kacheout-" );
		PImage screenshot = loadImage( screenshotFile );
		
		// save kinect
		p._kinectWrapper.getVideoImage().save( "bin/output/kacheout/kacheout-" + SystemUtil.getTimestampFine( p ) + "-rgb.png" );
		
		if( p._kinectWrapper.isActive() ) {
			float screenToOutputWidthRatio = 640f / (float)_stageWidth;
			int screenShotHeight = Math.round( _stageHeight * screenToOutputWidthRatio );
			PImage img = createImage(640, 480 + screenShotHeight, RGB);
			
			// paste 2 images together and save
			img.copy( ImageUtil.getReversePImage( p._kinectWrapper.getVideoImage() ), 0, 0, 640, 480, 0, 0, 640, 480 );
			img.copy( screenshot, 0, 0, _stageWidth, _stageHeight, 0, 481, 640, screenShotHeight );
			img.save( "bin/output/kacheout/kacheout-" + SystemUtil.getTimestampFine( p ) + "-comp.png" );
		}
		
		// clean up screenshot
//		boolean success = ( new File( screenshotFile ) ).delete();
//		if (!success) p.println("counldn't delete screenshot");
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
