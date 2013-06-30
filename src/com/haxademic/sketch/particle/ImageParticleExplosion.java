package com.haxademic.sketch.particle;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * 
 * @author justin
 *
 */
@SuppressWarnings("serial")
public class ImageParticleExplosion
	extends PApplet
{
	// global vars
	protected int _fps = 30;
	protected ImageParticle[] _particles;
	protected int _numParticles = 60;
//	protected Renderer _render;

	public void setup ()
	{
		// set up stage and drawing properties
		size( 1024, 768, OPENGL );
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
		_particles = new ImageParticle[_numParticles];
		for( int i = 0; i < _numParticles; i++ ) {
			_particles[i] = new ImageParticle( width/2, height/4, image );
		}
		
		// set up renderer
//		_render = new Renderer( this, _fps, Renderer.OUTPUT_TYPE_IMAGE, "bin/output/" );
//		_render.startRenderer();
	}

	public void draw() 
	{
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
	class ImageParticle 
	{
		protected PImage _image;
		protected float _xOrig;
		protected float _yOrig;
		protected float _x;
		protected float _y;
		protected float _xVelocity;
		protected float _yVelocity;
		protected float _rotVelocity;
		protected float _w;
		protected float _h;
		protected float _rot;
		protected float _tintR;
		protected float _tintG;
		protected float _tintB;
		protected float _tintAlpha;

		public ImageParticle( float x, float y, PImage img ) 
		{
			_image = img;
			_xOrig = x;
			_yOrig = y;
			
			reset();
		} 
		
		public void reset()
		{
			_x = _xOrig;
			_y = _yOrig;
			float randSize = random( 20, 120 );
			_w = randSize;
			_h = randSize;
			_rot = 0;
			
			_xVelocity = random( -30, 30 );
			_yVelocity = random( -20, 20 );
			_rotVelocity = random( -.2f, .2f );
			
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
			
			//rotate( _rot );
			translate( _x, _y );
			fill(255,255);
			image( _image, 0, 0, _w, _h );
//			blend( _image, (int)_x, (int)_y, (int)_w, (int)_h, (int)_x, (int)_y, (int)_w, (int)_h, ADD);
//			rect(0,0,10,10);
			popMatrix();
			
			// increment values
			_x += _xVelocity;
			_y += _yVelocity;
			_xVelocity *= .98;
			_yVelocity += .5;
			_rot += _rotVelocity;
			_w *= .94;
			_h *= .94;
			_tintAlpha *= .95;
		}
	}
	
}
