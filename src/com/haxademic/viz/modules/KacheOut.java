package com.haxademic.viz.modules;

import processing.core.PConstants;
import toxi.color.TColor;

import com.haxademic.viz.IVizModule;
import com.haxademic.viz.ModuleBase;
import com.haxademic.viz.elements.GridEQ;
import com.p5core.cameras.CameraDefault;
import com.p5core.data.EasingFloat;
import com.p5core.util.ColorGroup;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class KacheOut
extends ModuleBase 
implements IVizModule
{
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _numAverages = 32;

	// game state
	protected int _curMode;
	protected ColorGroup _gameColors;

	// blocks
	protected int _cols = 10;
	protected int _rows = 7;
	protected Block[][] _blockGrid;
	
	// should be an array of balls
	protected Ball _ball;
	protected Paddle _paddle;
	protected GridEQ _background;
	
	// game state
	protected int _gameState;
	protected final int GAME_READY = 2;
	protected final int GAME_ON = 3;
	
	protected final float CAMERA_Z_DEFAULT = 414;
	
	public KacheOut() {
		super();
		// store and init audio engine
		initAudio();

		// init viz
		init();
	}

	public void init() {
		p.colorMode( PConstants.RGB, 255, 255, 255, 1 );
		p.noStroke();
		newCamera();

		_stageWidth = p.width;
		_stageHeight = p.height;

		initGame();
	}

	// HAXADEMIC STUFF --------------------------------------------------------------------------------------

	public void initAudio()
	{
		_audioData.setNumAverages( _numAverages );
		_audioData.setDampening( .13f );
	}
	
	public void focus() {
		newCamera();
	}
	
	public void handleKeyboardInput() {
		if ( p.key == 'm' || p.key == 'M' ) {
			 launchBall();
		}
	}

	void newCamera() {
		_curCamera = new CameraDefault( p, 0, 0, 0 );
		_curCamera.setPosition( _stageWidth/2f, _stageHeight/2f, CAMERA_Z_DEFAULT );
		_curCamera.setTarget( _stageWidth/2f, _stageHeight/2f, 0 );
		_curCamera.reset();
	}

	public void beatDetect( int isKickCount, int isSnareCount, int isHatCount, int isOnsetCount ) {
	}
	
	public void update() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
//		p.camera();
		
		_curCamera.update();
//		p.println(-_stageWidth + p.mouseX*2);
//		_curCamera.setPosition( _stageWidth/2, _stageHeight/2, -_stageWidth + p.mouseX*2 );

		updateGame();
	}
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void initGame() {
		pickNewColors();
		
		
		// create grid
		float boxW = _stageWidth / _cols;
		float boxH = _stageHeight / 2 / _rows;
		_blockGrid = new Block[_cols][_rows];
		int index = 0;
		for (int i = 0; i < _cols; i++) {
			for (int j = 0; j < _rows; j++) {
				// Initialize each object
				_blockGrid[i][j] = new Block( i*boxW, j*boxH, boxW, boxH, index );
				index++;
			}
		}
		
		// create game objects
		_background = new GridEQ( p, toxi, _audioData );
		_background.updateColorSet( _gameColors );

		_ball = new Ball();
		_paddle = new Paddle();
		
		// init game state
		_gameState = GAME_READY;
	}
	
	protected void updateGame() {
		// draw bg
		p.pushMatrix();
		p.translate( 0, 0, -1000 );
		_background.update();
		p.popMatrix();
		
		// debug bg
//		p.pushMatrix();
//		p.noStroke();
//		p.fill( 255, 255, 255, 50 );
//		p.rect( _stageWidth/2, _stageHeight/2, _stageWidth, _stageHeight );
//		p.popMatrix();
		
		// draw the blocks
		for (int i = 0; i < _cols; i++) {
			for (int j = 0; j < _rows; j++) {
				_blockGrid[i][j].display();
			}
		}
		
		// draw other objects
		_paddle.moveTowardsX( MathUtil.getPercentWithinRange( 0, _stageWidth, p.mouseX ) );
		_paddle.display();
		_ball.display();
		detectBlockCollisions();
	}
	
	public void launchBall() {
		_gameState = GAME_ON;
		_ball.launch();
	}
	
	public void detectBlockCollisions() {
		for (int i = 0; i < _cols; i++) {
			for (int j = 0; j < _rows; j++) {
				if( _blockGrid[i][j].active() == true && _blockGrid[i][j].detectBall() == true ) {
					String bounceSide = _blockGrid[i][j].bounceCloserSide();
					// @TODO: ball can hit multiple balls on one frame - need to find the closest and work from that. or, if we hit inside a corner, how to deal with that?
					if( bounceSide == Block.SIDE_BOTH ) {
						_ball.bounceX();
						_ball.bounceY();
					} else if( bounceSide == Block.SIDE_H ) {
						_ball.bounceX();
					} else {
						_ball.bounceY();
					}
					
					_blockGrid[i][j].die();
//					break;
				}
			}
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


	// BLOCK OBJECT --------------------------------------------------------------------------------------
	
	public class Block {
		// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
		public float x,y,z;
		float w,h;
		float r,g,b;
		int index;
		protected boolean _active;
		protected TColor _color;
		
		public static final String SIDE_H = "H";
		public static final String SIDE_V = "V";
		public static final String SIDE_BOTH = "B";

		// Cell Constructor
		Block(float x, float y, float w, float h, int index ){
			this.w = w;
			this.h = h;
			this.x = x + w/2;
			this.y = y + h/2;
			
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
				
		public boolean detectBall() {
			float ballX = _ball.x();
			float ballY = _ball.y();
			float ballSize = _ball.radius();
			 
			// form: http://stackoverflow.com/a/402010
		    float circleDistance_x = Math.abs(ballX - x - w/2);
		    float circleDistance_y = Math.abs(ballY - y - h/2);

		    if (circleDistance_x > (w/2 + ballSize)) { return false; }
		    if (circleDistance_y > (h/2 + ballSize)) { return false; }
		    if (circleDistance_x <= (w/2)) { return true; } 
		    if (circleDistance_y <= (h/2)) { return true; }
		    
		    float cornerDistance_sq = (float) Math.pow( circleDistance_x - w/2, 2 ) + (float) Math.pow( circleDistance_y - h/2, 2 );

		    return (cornerDistance_sq <= (float) Math.pow( ballSize, 2 ) );
		}
		
		public String bounceCloserSide() {
			float ballX = _ball.x();
			float ballY = _ball.y();
			float ballSize = _ball.radius();

			float overlapLeft = ( ballX < x ) ? x - w/2 - ballSize + ballX : 0;
			float overlapRight = ( ballX > x ) ? x + w/2 + ballSize - ballX : 0;
			float overlapTop = ( ballY < y ) ? y - h/2 - ballSize + ballY : 0;
			float overlapBottom = ( ballY > y ) ? y + h/2 + ballSize - ballY : 0;
			
			if( ( overlapLeft > 0 || overlapRight > 0 ) && ( overlapTop > 0 || overlapBottom > 0 ) ) {
				return SIDE_BOTH;
			}
			if( ( overlapTop > overlapLeft && overlapTop > overlapRight ) || ( overlapBottom > overlapLeft && overlapBottom > overlapRight ) ) {
				return SIDE_V;
			} else {
				return SIDE_H;
			}
		}
		
		public void die() {
			_active = false;
		}
		
		public void display() {
			if( _active == true ) {
				// adjust cell z per brightness
				float zAdd = 40 * _audioData.getFFT().spectrum[index % 512];
				p.pushMatrix();
				p.translate( x, y, 0 );
				
				//p.rotateZ( _audioData.getFFT().averages[1] * .01f );
				_color.alpha = p.constrain( 0.5f + zAdd, 0, 1 );
				p.fill( _color.toARGB() );
				p.noStroke();
				p.box( w, h, h + zAdd*4 ); 
//				p.rect( 0, 0, w + zAdd, h + zAdd ); 
				
				p.popMatrix();
			}
		}
	}

	// BALL OBJECT --------------------------------------------------------------------------------------

	class Ball {

		protected float BALL_SIZE = 20;
		float _x, _y, _speedX, _speedY;
		protected TColor _color;

		Ball() {
			// convert speed to use radians
			_speedX = ( MathUtil.randBoolean( p ) == true ) ? 4 : -4;
			_x = p.random( 0, _stageWidth );
			_y = p.random( _stageHeight / 2, _stageHeight );
			_color = _gameColors.getRandomColor().copy();
		}
		
		public float x() { return _x; }
		public float y() { return _y; }
		public float radius() { return BALL_SIZE; }
		public float paddleTop() { return _paddle.top() - BALL_SIZE; }
		
		public void launch() {
			_speedX = ( MathUtil.randBoolean( p ) == true ) ? 8 : -8;
			_speedY = -8;
		}
		
		public void bounceX() {
			_speedX *= -1;
			_x += _speedX;
		}
		
		public void bounceY() {
			_speedY *= -1;
			_y += _speedY;
		}
		
		public void display() {
			if( _gameState == GAME_READY ) {
				_x = _paddle.x();
				_y = paddleTop();
			} else {
				_x += _speedX;
				_y += _speedY;
			}
			
			detectWalls();
			detectPaddle();
			
			p.fill( _color.toARGB() );
			p.pushMatrix();
			p.translate( _x, _y, 0 );
			p.sphere( BALL_SIZE );
			p.popMatrix();
		}
		
		protected void detectWalls() {
			if( _x < 0 ) {
				_x -= _speedX;
				_speedX *= -1;
			}
			if( _x > _stageWidth ) {
				_x -= _speedX;
				_speedX *= -1;
			}
			if( _y < 0 ) {
				_y -= _speedY;
				_speedY *= -1;
			}
			if( _y > _stageHeight ) {
				_y -= _speedY;
				_speedY *= -1;
			}
		}

		protected void detectPaddle() {
			if( _y > paddleTop() ) {
				if( _x > _paddle.left() && _x < _paddle.right() ) {
					p.println("bounce!");
					_speedX = ( _x - _paddle.x() ) / 10;
					bounceY();
				}
			}
		}

	}
	
	// PADDLE OBJECT --------------------------------------------------------------------------------------

	class Paddle {

		EasingFloat x, y;
		protected int STAGE_H_PADDING = 40;
		protected float HEIGHT = 20;
		protected float _width = 200;
		protected float _easing = 5f;
		protected float _center;
		protected TColor _color;

		Paddle() {
			_center = ( _stageWidth + _width) / 2;
			x = new EasingFloat( _center, _easing );
			y = new EasingFloat( _stageHeight - STAGE_H_PADDING, _easing );
			_color = _gameColors.getRandomColor().copy();
		} 
		
		public float x() { return x.value(); }
		public float y() { return y.value(); }
		public float height() { return HEIGHT; }
		public float top() { return y.value() - HEIGHT / 2f ; }
		public float left() { return x.value() - _width / 2f ; }
		public float right() { return x.value() + _width / 2f ; }
		
		public void moveTowardsX( float percent ) {
			x.setTarget( percent * _stageWidth );
		}
		
		void display() {
			x.update();
			y.update();
			
			p.pushMatrix();
			p.translate( x.value(), y.value(), 0f );
			p.rotateX( p.frameCount / 30f );
			
			_color.alpha = 0.5f + _audioData.getFFT().averages[1];
			p.fill( _color.toARGB() );
			p.noStroke();
			p.box( _width, HEIGHT, HEIGHT ); 
			
			p.popMatrix();
		}

	}

}
