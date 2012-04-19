package com.haxademic.app.kacheout;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.game.GamePlay;
import com.haxademic.core.audio.AudioLoopPlayer;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.cameras.CameraDefault;
import com.haxademic.core.cameras.common.ICamera;
import com.haxademic.core.data.FloatRange;
import com.haxademic.core.draw.shapes.Voronoi3D;
import com.haxademic.core.hardware.kinect.KinectWrapper;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.core.util.DebugUtil;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.FileUtil;

import ddf.minim.AudioPlayer;

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
	public AudioPlayer _backgroundAudio;
	
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
	protected ColorGroup _gameColors;
	protected final int NUM_PLAYERS = 2;
	protected ArrayList<GamePlay> _gamePlays;
	protected GamePlay _player1;
	protected GamePlay _player2;
	
	// game state
	protected int _gameState;
	public final int GAME_READY = 2;
	public final int GAME_ON = 3;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;
	
	// single game object refs
	public ArrayList<WETriangleMesh> shatteredCubeMeshes;

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
		
		ArrayList<String> soundtrackFiles = FileUtil.getFilesInDirOfType( "data/wav/kacheout/soundtrack", ".wav" );
		_backgroundAudio = _minim.loadFile("wav/kacheout/soundtrack/"+soundtrackFiles.get( 0 ), 512);
		_backgroundAudio.loop();
		
		_kinectWrapper.enableDepth( true );
		_kinectWrapper.enableDepthImage( true );
				
		initGame();
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
		if ( p.key == 'm' || p.key == 'M' ) {
//			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).launchBall();
			_player1.launchBall();
			_player2.launchBall();
			_gameState = GAME_ON;
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
		
		
//		_debugText.draw( "Debug :: FPS:" + _fps );
	}
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void initGame() {
		// set flags and props	
		pickNewColors();
		_gameState = GAME_READY;
		
		// init game objects
		_audio = new AudioLoopPlayer( p );

		
		_gameWidth = _stageWidth / NUM_PLAYERS;
		float kinectRangeWidth = KinectWrapper.KWIDTH / 2f * KINECT_GAP_PERCENT;
		_player1 = new GamePlay( 0, _gameWidth, new FloatRange( 0, kinectRangeWidth ) );
		_player2 = new GamePlay( _gameWidth, _gameWidth * 2, new FloatRange( KinectWrapper.KWIDTH - kinectRangeWidth, KinectWrapper.KWIDTH ) );
		_gamePlays = new ArrayList<GamePlay>();
		_gamePlays.add( _player1 );
		_gamePlays.add( _player2 );
	}
	
	protected void updateGame() {
		// debug bg
//		p.pushMatrix();
//		p.noStroke();
//		p.fill( 255, 255, 255, 50 );
//		p.rect( _stageWidth/2, _stageHeight/2, _stageWidth, _stageHeight );
//		p.popMatrix();
		
		p.pushMatrix();
		updateGames();
		logDebugInfo();
		p.popMatrix();
	}
	
	protected void updateGames(){
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).update( i );
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
