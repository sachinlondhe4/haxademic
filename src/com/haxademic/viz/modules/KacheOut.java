package com.haxademic.viz.modules;

import processing.core.PConstants;

import com.haxademic.viz.IVizModule;
import com.haxademic.viz.ModuleBase;
import com.p5core.cameras.CameraBasic;
import com.p5core.util.MathUtil;

public class KacheOut
extends ModuleBase 
implements IVizModule
{
	protected int _stageWidth;
	protected int _stageHeight;
	
	int _numAverages = 32;

	int _curMode;

	// Blocks
	int cols = 20;
	int rows = 15;
	Block[][] _blockGrid;
	
	// should be an array of balls
	Ball _ball;
	protected float BALL_SIZE = 20;
	
	// game state
	protected int _gameState;
	protected final int GAME_READY = 2;
	protected final int GAME_ON = 3;
	
	public KacheOut()
	{
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

		// create grid
		float boxW = _stageWidth / cols;
		float boxH = _stageHeight / 2 / rows;
		_blockGrid = new Block[cols][rows];
		int index = 0;
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				// Initialize each object
				_blockGrid[i][j] = new Block( i*boxW, j*boxH, boxW, boxH, index );
				index++;
			}
		}
		
		// create ball
		_ball = new Ball();
	}

	public void initAudio()
	{
		_audioData.setNumAverages( _numAverages );
		_audioData.setDampening( .13f );
	}

	public void focus() {
		p.colorMode( PConstants.RGB, 255, 255, 255, 1 );
		p.noStroke();
		newCamera();
	}

	public void update() {
		p.shininess(1000f); 
		p.lights();
		p.background(0);

//		_curCamera.update();
		p.rectMode(PConstants.CORNER);
		
		// draw the blocks
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				_blockGrid[i][j].display();
			}
		}
		
		// draw balls
		_ball.display();
		
//		_curCamera.setPosition( 0, 0, -_stageWidth / 2 + p.mouseX );
//		p.println(-_stageWidth / 2 + p.mouseX);
	}

	public void handleKeyboardInput(){
	}

	void newCamera() {
		_curCamera = new CameraBasic( p, 0, 0, 485 );
		_curCamera.reset();
	}

	public void beatDetect( int isKickCount, int isSnareCount, int isHatCount, int isOnsetCount ) {
	}

	// A Cell object
	public class Block {
		// A cell object knows about its location in the grid as well as its size with the variables x,y,w,h.
		public float x,y,z;
		float w,h;
		float r,g,b;
		int index;

		// Cell Constructor
		Block(float x, float y, float w, float h, int index ){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			
			this.index = index;
			
			// random colors for now
			r = p.random( 0, 80 );
			g = p.random( 200, 255 );
			b = p.random( 100, 200 );
		} 

		void display() {
			// adjust cell z per brightness
			float zAdd = 10 * _audioData.getFFT().spectrum[index % 512];
			p.pushMatrix();
			p.translate( x + w/2, y + h/2, 0 + zAdd );
			
			//p.rotateZ( _audioData.getFFT().averages[1] * .01f );
			p.fill( r, g, b );
			p.noStroke();
			p.box(w,h,h); 
			
			p.popMatrix();
		}
	}


	// A Cell object
	class Ball {

		float x, y, speedX, speedY;

		// Cell Constructor
		Ball() {
			// convert speed to use radians
			speedX = ( MathUtil.randBoolean( p ) == true ) ? 4 : -4;
			speedY = -4;
			x = p.random( 0, _stageWidth );
			y = p.random( _stageHeight / 2, _stageHeight );
		} 
		
		void detectWalls() {
			if( x < 0 ) {
				x -= speedX;
				speedX *= -1;
			}
			if( x > _stageWidth ) {
				x -= speedX;
				speedX *= -1;
			}
			if( y < 0 ) {
				y -= speedY;
				speedY *= -1;
			}
			if( y > _stageHeight ) {
				y -= speedY;
				speedY *= -1;
			}
		}
		
		void detectBlocks() {
			
		}

		void display() {
			x += speedX;
			y += speedY;
			
			detectWalls();
			detectBlocks();
			
			p.fill( 255 );
			p.pushMatrix();
			p.translate( x, y, 0 );
			p.sphere( BALL_SIZE );
			p.popMatrix();
		}
		
	}

}
