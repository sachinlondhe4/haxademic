package com.haxademic.app.kacheout;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.geom.AABB;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.viz.elements.GridEQ;
import com.p5core.audio.AudioLoopPlayer;
import com.p5core.cameras.CameraDefault;
import com.p5core.cameras.common.ICamera;
import com.p5core.data.FloatRange;
import com.p5core.data.easing.EasingFloat;
import com.p5core.draw.shapes.Meshes;
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
	protected KinectWrapper _kinectInterface;
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
	protected final int GAME_READY = 2;
	protected final int GAME_ON = 3;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;


	protected void setupApp() {
		// store and init audio engine
		initAudio();
		// init viz
		init();
	}

	public void init() {
		colorMode( PConstants.RGB, 255, 255, 255, 1 );
		noStroke();
		newCamera();

		_kinectInterface = _kinectWrapper;
		
		_stageWidth = width;
		_stageHeight = height;
		_cameraZFromHeight = (float)_stageHeight * CAMERA_Z_WIDTH_MULTIPLIER;

		initGame();
	}

	// HAXADEMIC STUFF --------------------------------------------------------------------------------------

	public void initAudio()
	{
		_audioInput.setNumAverages( _numAverages );
		_audioInput.setDampening( .13f );
	}
	
	public void focus() {
		newCamera();
	}
	

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

	public void handleKeyboardInput() {
		if ( p.key == 'm' || p.key == 'M' ) {
			for( int i=0; i < _numPlayers; i++ ) _gamePlays.get( i ).launchBall();
		}
	}

	public void findKinectCenterX() {
		// sample several rows, finding the extents of objects within range
		int[] depthArray = _kinectInterface.getDepthData();
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
					depthInMeters = _kinectInterface.rawDepthToMeters( depthRaw );
					if( depthInMeters > KINECT_MIN_DIST && depthInMeters < KINECT_MAX_DIST ) {
						if( _isDebuggingKinect == true ) {
							p.fill( 255, 255, i*100 );
							p.noStroke();
							AABB box = new AABB( 4 );
							box.set( x, -5, 0 );
							toxi.box( box );
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
							toxi.box( box );
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
		if( _kinectInterface.isActive() == true ) {
			_kinectInterface.update();
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
			paddleX = MathUtil.getPercentWithinRange( 0, _stageWidth, p.mouseX );
		}
	}
	

	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void update() {
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
		pickNewColors();
		
		_kinectPositions = new ArrayList<FloatRange>();
		for( int i=0; i < _numPlayers; i++ ) {
			_kinectPositions.add( new FloatRange( -1, -1 ) );
		}
		
		_gamePlays = new ArrayList<GamePlay>();
		_gameWidth = _stageWidth / _numPlayers;
		for( int i=0; i < _numPlayers; i++ ) {
			_gamePlays.add( new GamePlay( _gameWidth * i , _gameWidth + _gameWidth * i ) );
		}
		
		// init game state
		_gameState = GAME_READY;
		
		
		
		_audio = new AudioLoopPlayer( p );
	}
	
	protected void updateGame() {
		// debug bg
//		p.pushMatrix();
//		p.noStroke();
//		p.fill( 255, 255, 255, 50 );
//		p.rect( _stageWidth/2, _stageHeight/2, _stageWidth, _stageHeight );
//		p.popMatrix();
		
		p.pushMatrix();
		handleUserInput();
		
		p.translate( 0,0,-400 );
		p.rotateX( p.PI / 16f );
		
		for( int i=0; i < _numPlayers; i++ ) {
			p.translate( i * ( _stageWidth / _numPlayers), 0 );
			_gamePlays.get( i ).update();
		}
		p.popMatrix();
		
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

	// GAMEPLAY OBJECT --------------------------------------------------------------------------------------
	public class GamePlay {
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
		
		public GamePlay( int gameLeft, int gameRight ) {
			_gameLeft = gameLeft;
			_gameRight = gameRight;
			_gameWidth = gameRight - gameLeft;
			// create grid
			float boxW = _gameWidth / _cols;
			float boxH = _stageHeight / 2 / _rows;
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
			_background = new GridEQ( p, toxi, _audioInput );
			_background.updateColorSet( _gameColors );

			_ball = new Ball();
			_paddle = new Paddle();
			_walls = new Walls();
			
		}
		
		public void update() {
			drawBackground();
			drawGameObjects();
			
//			DrawUtil.setCenter( p );
//			_audio.drawSynthOut();
		}
		
		public void updatePaddle( float paddleX ) {
			_paddle.moveTowardsX( paddleX );
		}
		
		protected void drawBackground(){
			// draw bg
			p.pushMatrix();
			p.translate( 0, 0, -1000 );
			_background.update();
			p.popMatrix();
		}
		
		protected void drawGameObjects() {
			detectCollisions();
			// draw the blocks
			for (int i = 0; i < _blocks.size(); i++) {
				_blocks.get( i ).display();
			}
			// draw other objects
			_paddle.display();
			_walls.display();
			_ball.display( _paddle );
		}
		
		public void launchBall() {
			_gameState = GAME_ON;
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

	// BLOCK OBJECT --------------------------------------------------------------------------------------
	
	public class Block {
		// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
		protected AABB _box;
		public float x,y,z;
		float w,h;
		float r,g,b;
		int index;
		protected boolean _active;
		protected TColor _color;
		
		public static final String SIDE_H = "H";
		public static final String SIDE_V = "V";
		public static final String SIDE_BOTH = "B";

		
		public Block(float x, float y, float w, float h, int index ) {
			this.w = w/2;
			this.h = h/2;
			this.x = x + w/2;
			this.y = y + h/2;
			
			_box = new AABB( w );
			_box.set( x, y, 0 );
			_box.setExtent( new Vec3D( this.w, this.h, 10 ) );

			this.index = index;
			
			// random colors for now
			r = p.random( 0, 80 );
			g = p.random( 200, 255 );
			b = p.random( 100, 200 );
			
			_color = _gameColors.getRandomColor().copy();
			
			_active = true;
		}
		
		public boolean active() {
			return _active;
		}
		
		public AABB box() {
			return _box;
		}
		
		public void die() {
			_active = false;
		}
		
		public void display() {
			if( _active == true ) {
				_box.set( x, y, 0 );

				// adjust cell z per brightness
				float zAdd = 40 * _audioInput.getFFT().spectrum[index % 512];
				
				//p.rotateZ( _audioInput.getFFT().averages[1] * .01f );
				_color.alpha = p.constrain( 0.5f + zAdd, 0, 1 );
				p.fill( _color.toARGB() );
				p.noStroke();
				toxi.box( _box );
				
//				WETriangleMesh mesh1 = ( p.round( p.frameCount / 30f ) % 2 == 0 ) ? _invaderMesh_01 : _invaderMesh_01_alt;
//				DrawMesh.drawMeshWithAudio( p, mesh1, p.getAudio(), 3f, false, _color, _color, 0.25f );

			}
		}
	}

	// BALL OBJECT --------------------------------------------------------------------------------------

	class Ball {

		protected float BALL_SIZE = 20;
		protected int BALL_RESOLUTION = 20;
		float _x, _y, _speedX, _speedY;
		protected TColor _color;
		protected Sphere _sphere;
		protected float SPEED = 15f;
		
		public Ball() {
			// convert speed to use radians
			_speedX = ( MathUtil.randBoolean( p ) == true ) ? SPEED : -SPEED;
			_x = p.random( 0, _gameWidth );
			_y = p.random( _stageHeight / 2, _stageHeight );
			_color = _gameColors.getRandomColor().copy();
			_sphere = new Sphere( BALL_SIZE );
		}
		
		public float x() { return _x; }
		public float y() { return _y; }
		public Sphere sphere() { return _sphere; }
		public float radius() { return BALL_SIZE; }
		
		public void launch( Paddle paddle ) {
			_x = paddle.x(); 
			_y = paddle.y() - paddle.height() - BALL_SIZE - 10;
			_speedX = ( MathUtil.randBoolean( p ) == true ) ? SPEED : -SPEED;
			_speedY = -SPEED;
		}
		
		public void bounceX() {
			_speedX *= -1;
			_x += _speedX;
		}
		
		public void bounceY() {
			_speedY *= -1;
			_y += _speedY;
		}
		
		public void display( Paddle paddle ) {
			if( _gameState == GAME_READY ) {
				_x = paddle.x();
				_y = paddle.y() - paddle.height() - BALL_SIZE - 10;
			} else {
				_x += _speedX;
				_y += _speedY;
			}
						
			p.fill( _color.toARGB() );
			_sphere.x = _x;
			_sphere.y = _y;
			toxi.sphere( _sphere, BALL_RESOLUTION );
		}
		
		protected void detectWalls( boolean leftHit, boolean topHit, boolean rightHit ) {
			if( leftHit == true ) {
				_x -= _speedX;
				_speedX *= -1;
			}
			if( rightHit == true ) {
				_x -= _speedX;
				_speedX *= -1;
			}
			if( topHit == true ) {
				_y -= _speedY;
				_speedY *= -1;
			}
//			if( _y > _stageHeight ) {
//				_y -= _speedY;
//				_speedY *= -1;
//			}
		}

		public boolean detectBox( AABB box ) {
			if( box.intersectsSphere( _sphere ) ) return true;
			return false;
		}
		
		protected void bounceOffPaddle( Paddle paddle ) {
			p.println("bounce!");
			_speedX = ( _x - paddle.x() ) / 10;
			bounceY();
		}

	}
	
	// PADDLE OBJECT --------------------------------------------------------------------------------------

	class Paddle {

		protected EasingFloat _x, _y, _z;
		protected int STAGE_H_PADDING = 40;
		protected float HEIGHT = 20;
		protected float _width = 0;
		protected float _easing = 1.5f;
		protected float _center;
		protected TColor _color;
		protected AABB _box;

		public Paddle() {
			_center = ( _gameWidth + _width) / 2;
			_width = (float)_gameWidth / 5f;
			_x = new EasingFloat( _center, _easing );
			_y = new EasingFloat( _stageHeight - STAGE_H_PADDING, _easing );
			_color = _gameColors.getRandomColor().copy();
			_box = new AABB( 1 );
			_box.setExtent( new Vec3D( _width, HEIGHT, HEIGHT ) );
		} 
		
		public float x() { return _x.value(); }
		public float y() { return _y.value(); }
		public float height() { return HEIGHT; }

		public AABB box() {
			return _box;
		}

		public void moveTowardsX( float percent ) {
			percent = 1 - percent;
			_x.setTarget( _width + percent * (_gameWidth - _width*2f) );
		}

		public boolean detectSphere( Sphere sphere ) {
			if( _box.intersectsSphere( sphere ) ) return true;
			return false;
		}

		void display() {
			_x.update();
			_y.update();
			
			_box.set( _x.value(), _y.value(), 0 );
			_box.rotateX( p.frameCount / 30f );
			_color.alpha = 0.5f + _audioInput.getFFT().averages[1];
			p.fill( _color.toARGB() );
			p.noStroke();
			toxi.box( _box ); 
		}

	}

	// WALLS BOUNDARY OBJECT --------------------------------------------------------------------------------------

	class Walls {
		
		protected AABB _wallLeft, _wallTop, _wallRight;
		protected boolean _wallLeftHit, _wallTopHit, _wallRightHit;
		protected TColor _color;
		protected float BASE_ALPHA = 0.2f;
		protected float _wallLeftAlpha = BASE_ALPHA;
		protected float _wallTopAlpha = BASE_ALPHA;
		protected float _wallRightAlpha = BASE_ALPHA;
		public final int WALL_WIDTH = 20;

		public Walls() {
			_color = new TColor( TColor.WHITE );
			
			_wallLeft = new AABB( 1 );
			_wallLeft.set( 0, _stageHeight / 2f, 0 );
			_wallLeft.setExtent( new Vec3D( WALL_WIDTH, _stageHeight / 2f, WALL_WIDTH ) );

			_wallTop = new AABB( 1 );
			_wallTop.set( _gameWidth / 2f, 0, 0 );
			_wallTop.setExtent( new Vec3D( _gameWidth / 2, WALL_WIDTH, WALL_WIDTH ) );

			_wallRight = new AABB( 1 );
			_wallRight.set( _gameWidth, _stageHeight / 2f, 0 );
			_wallRight.setExtent( new Vec3D( WALL_WIDTH, _stageHeight / 2f, WALL_WIDTH ) );

		} 
		
		public boolean leftHit(){ return _wallLeftHit; }
		public boolean topHit(){ return _wallTopHit; }
		public boolean rightHit(){ return _wallRightHit; }
		
		public boolean detectSphere( Sphere sphere ) {
			_wallLeftHit = ( _wallLeft.intersectsSphere( sphere ) ) ? true : false;
			_wallTopHit = ( _wallTop.intersectsSphere( sphere ) ) ? true : false;
			_wallRightHit = ( _wallRight.intersectsSphere( sphere ) ) ? true : false;
			if( _wallLeftHit == true || _wallTopHit == true || _wallRightHit == true ) {
				if( _wallLeftHit == true ) _wallLeftAlpha = 1;
				if( _wallTopHit == true ) _wallTopAlpha = 1;
				if( _wallRightHit == true ) _wallRightAlpha = 1;
				return true;
			}
			return false;
		}
		
		public void resetCollisions() {
			_wallLeftHit = false;
			_wallTopHit = false;
			_wallRightHit = false;
		}

		void display() {
			if( _wallLeftAlpha > BASE_ALPHA ) _wallLeftAlpha -= 0.1f;
			if( _wallTopAlpha > BASE_ALPHA ) _wallTopAlpha -= 0.1f;
			if( _wallRightAlpha > BASE_ALPHA ) _wallRightAlpha -= 0.1f;
			p.noStroke();
			_color.alpha = _wallLeftAlpha;
			p.fill( _color.toARGB() );
			toxi.box( _wallLeft ); 
			_color.alpha = _wallTopAlpha;
			p.fill( _color.toARGB() );
			toxi.box( _wallTop ); 
			_color.alpha = _wallRightAlpha;
			p.fill( _color.toARGB() );
			toxi.box( _wallRight ); 
		}

	}

}
