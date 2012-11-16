package com.haxademic.sketch.particle;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import toxi.color.TColor;
import toxi.math.noise.PerlinNoise;

import com.haxademic.core.render.Renderer;
import com.haxademic.core.util.DrawUtil;

/**
 * 
 * @author justin
 *
 */
public class ConfettiExplosion
	extends PApplet
{
	// global vars
	protected int _fps = 30;
	protected ConfettiParticle[] _particles;
	protected int _numParticles = 300;
//	protected Renderer _render;
	PApplet p;

	public void setup ()
	{
		p = this;
		// set up stage and drawing properties
		size( 800, 600, OPENGL );
		frameRate( _fps );
		colorMode( PConstants.RGB, 255, 255, 255, 255 );
		background( 0 );
//		noSmooth();
//		shininess(1000); 
//		lights();
		smooth();
		noStroke();
		
		
		// set up image
		PImage image;
		image = loadImage("../data/images/middle-finger-trans.png");
		imageMode( PConstants.CENTER );
		
		// create particles
		_particles = new ConfettiParticle[_numParticles];
		for( int i = 0; i < _numParticles; i++ ) {
			_particles[i] = new ConfettiParticle( width/2, height/2, image );
		}
		
		// set up renderer
//		_render = new Renderer( this, _fps, Renderer.OUTPUT_TYPE_IMAGE, "bin/output/" );
//		_render.startRenderer();
	}

	public void draw() 
	{
		background(0);
		// update particles
		for (int i = 0; i < _numParticles; i++) 
		{
			_particles[i].update();
		}
	}
	
	/**
	 * Key handling for rendering functions - stopping and saving an image
	 */
	public void keyPressed()
	{
//		if( key == 'p' ) _render.renderFrame();
	}  
	
	public void mouseClicked()
	{
		// update particles
		for (int i = 0; i < _numParticles; i++) 
		{
			_particles[i].reset();
		}
	}
	
	// A Cell object
	class ConfettiParticle 
	{
		protected PImage _image;
		protected float _xOrig;
		protected float _yOrig;
		protected float _x;
		protected float _y;
		protected float _xVelocity;
		protected float _yVelocity;
		protected float _maxYVelocity;
		protected float _rotVelocity;
		protected float _w;
		protected float _h;
		protected float _rot;
		protected float _tintR;
		protected float _tintG;
		protected float _tintB;
		protected float _tintAlpha;
		protected PerlinNoise _perlin;
		protected float _perlinOffset;
		protected float _perlinWind;
		
		protected TColor _color;
		protected TColor BLUE_DARK = TColor.newHex("0f568b");
		protected TColor BLUE_LIGHT = TColor.newHex("9cb3c7");
		protected TColor GREY_MEDIUM = TColor.newHex("d4d4d4");

		public ConfettiParticle( float x, float y, PImage img ) 
		{
			_image = img;
			_xOrig = x;
			_yOrig = y;
			_perlin = new PerlinNoise();
			
			reset();
		} 
		
		public void reset()
		{
			_x = _xOrig;
			_y = _yOrig;
			float randSize = random( 6, 24 );
			_w = randSize;
			_h = randSize;
			_rot = 0;
			_perlinOffset = random( 0f, 1000f );
			
			_xVelocity = random( -15, 15 );
			_yVelocity = random( -50, -30 );
			_maxYVelocity = random( 3f, 5f );
			_rotVelocity = random( -.2f, .2f );
			
			int randColor = round( random( 0, 2 ) );
			if( randColor == 0 ) {
				_color = BLUE_DARK;
			} else if( randColor == 1 ) {
				_color = BLUE_LIGHT;
			} else {
				_color = GREY_MEDIUM;
			}
			
			_tintR = random( .4f, 1 );
			_tintG = random( .3f, .6f );
			_tintB = random( .4f, .9f );
			_tintAlpha = random( .8f, .99f );
		}

		public void update() 
		{
			// draw
			pushMatrix();
			
			tint( _tintR * 255f, _tintG * 255f, _tintB * 255f, _tintAlpha * 255f );
			
			if( _yVelocity > 0 )
				_perlinWind = -0.5f + _perlin.noise( _perlinOffset + ( frameCount / 80f ) );
			else
				_perlinWind = 0;
			
			//rotate( _rot );
			DrawUtil.setDrawCenter(p);
			translate( _x, _y );
			fill( _color.toARGB() );
			rotateX(_rot);
			rotateY(_rot);
			rotateZ(_rot);
//			image( _image, 0, 0, _w, _h );
//			blend( _image, (int)_x, (int)_y, (int)_w, (int)_h, (int)_x, (int)_y, (int)_w, (int)_h, ADD);
			rect(0,0,_w,_w*0.5f);
			popMatrix();
			
			// increment values
			_x += _xVelocity + _perlinWind * 10f;
			_y += _yVelocity;
			if( _yVelocity < 0 ) {
				_xVelocity *= .98;
				_yVelocity += 1.8f;
			} else {
				if( _yVelocity < _maxYVelocity ) _yVelocity += .1f;
				_xVelocity *= .9;
			}
			_rot += _rotVelocity;
			
//			_w *= .99;
//			_h *= .99;
//			_tintAlpha *= .999;
		}
	}
	
}
