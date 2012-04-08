package com.haxademic.app.kacheout.game;

import java.util.ArrayList;

import toxi.geom.mesh.WETriangleMesh;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.viz.elements.GridEQ;
import com.p5core.draw.shapes.Meshes;

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
	
	public GamePlay( int gameLeft, int gameRight ) {
		p = (KacheOut)PAppletHax.getInstance();
		_gameLeft = gameLeft;
		_gameRight = gameRight;
		_gameWidth = gameRight - gameLeft;
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
	
	public void update() {
		drawBackground();
		drawGameObjects();
		
//		DrawUtil.setCenter( p );
//		_audio.drawSynthOut();
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
