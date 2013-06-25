package com.haxademic.sketch.particle;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.math.noise.PerlinNoise;

import com.haxademic.core.draw.util.DrawUtil;

/**
 * 
 * @author justin
 *
 */
public class ConfettiExplosion
	extends PApplet
{
	// global vars
	protected int _fps = 40;
	protected ConfettiParticles _confetti;
	PApplet p;

	public void setup ()
	{
		p = this;
		// set up stage and drawing properties
		size( 800, 600, OPENGL );
		frameRate( _fps );
		colorMode( PConstants.RGB, 255, 255, 255, 255 );
		background( 0 );
		
		shininess(1000); 
		lights();
		
		smooth();
		noStroke();
		
		_confetti = new ConfettiParticles( 300 );
		_confetti.explode();
	}

	public void draw() 
	{
		background(0);
		_confetti.update();
	}
		
	public void mouseClicked()
	{
		_confetti.explode();
	}
	
	class ConfettiParticles {
		protected ConfettiParticle[] _particles;
		protected int _numParticles;
		protected boolean _active;

		public ConfettiParticles( int numParticles ) {
			_numParticles = numParticles;
			_active = false;
			
			// create particles
			_particles = new ConfettiParticle[_numParticles];
			for( int i = 0; i < _numParticles; i++ ) {
				_particles[i] = new ConfettiParticle( width/2, height/2 );
			}
		}
		
		public void update() {
			if( _active == true ) {
				boolean stillActive = false;
				for (int i = 0; i < _numParticles; i++) {
					if( _particles[i].update() == true ) {
						stillActive = true;
					}
				}
				if( stillActive == false ) _active = false;
			}
		}
		
		public void explode() {
			_active = true;
			reset();
			for (int i = 0; i < _numParticles; i++) {
				_particles[i].setActive();
			}
		}
		
		public void reset() {
			for (int i = 0; i < _numParticles; i++) {
				_particles[i].reset();
			}
		}
	}
	
	// A Cell object
	class ConfettiParticle 
	{
		protected boolean _isActive;
		protected float _originX;
		protected float _originY;
		protected float _x;
		protected float _y;
		protected float _speedX;
		protected float _speedY;
		protected float _maxSpeedY;
		protected float _speedRot;
		protected float _rot;
		protected float _w;
		protected float _h;
		protected PerlinNoise _perlin;
		protected float _perlinOffset;
		protected float _perlinWind;
		
		protected TColor _color;
		protected TColor BLUE_DARK = TColor.newHex("0f568b");
		protected TColor BLUE_LIGHT = TColor.newHex("9cb3c7");
		protected TColor GREY_MEDIUM = TColor.newHex("d4d4d4");

		public ConfettiParticle( float x, float y ) 
		{
			_isActive = false;
			_originX = x;
			_originY = y;
			_perlin = new PerlinNoise();
			
			reset();
		} 
		
		public void setActive() {
			_isActive = true;
		}
		
		public void reset() {
			_isActive = false;
			
			// start position and set size
			_x = _originX;
			_y = _originY;
			_w = random( 6, 24 );
			_h = _w / 2f;
			
			// motion vars
			_speedX = random( -15, 15 );
			_speedY = random( -50, -30 );
			_maxSpeedY = random( 3f, 5f );
			_speedRot = random( -.15f, .15f );
			_rot = 0;
			_perlinOffset = random( 0f, 1000f );
			
			// random color
			int randColor = round( random( 0, 2 ) );
			if( randColor == 0 ) {
				_color = BLUE_DARK;
			} else if( randColor == 1 ) {
				_color = BLUE_LIGHT;
			} else {
				_color = GREY_MEDIUM;
			}
		}

		public boolean update() {
			if( _isActive == false ) return false;
				
			// draw setup
			pushMatrix();
			DrawUtil.setDrawCenter(p);
						
			// apply perlin wind on the way down
			if( _speedY > 0 )
				_perlinWind = -0.5f + _perlin.noise( _perlinOffset + ( frameCount / 80f ) );
			else
				_perlinWind = 0;
			
			// move and rotate
			translate( _x, _y );
			rotateX( _rot );
			rotateY( _rot );
			rotateZ( _rot );

			// color and draw confetti
			fill( _color.toARGB() );
			rect(0,0,_w,_w*0.5f);
//			box(_w, _h, _h);
			popMatrix();
			
			// increment movement values
			_x += _speedX + _perlinWind * 10f;
			_y += _speedY;
			if( _speedY < 0 ) {
				_speedX *= .98;
				_speedY += 1.8f;
			} else {
				if( _speedY < _maxSpeedY ) _speedY += .1f;
				_speedX *= .9;
			}
			_rot += _speedRot;
			
			// deactivate when off screen
			if( _y > p.height + 30 ) {
				_isActive = false;
			}
			return true;
		}
	}
	
}
