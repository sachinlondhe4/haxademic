package com.haxademic.sketch.brim_liski_repetitions;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.video.Movie;
import toxi.color.TColor;
import toxi.math.noise.PerlinNoise;

import com.haxademic.app.P;
import com.haxademic.app.PAppletHax;
import com.haxademic.core.data.easing.EasingFloat;
import com.haxademic.core.draw.color.EasingTColor;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.ImageUtil;
import com.haxademic.core.util.MathUtil;

public class BrimLiskiRepetitions
extends PAppletHax  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "com.haxademic.sketch.brim_liski_repetitions.BrimLiskiRepetitions" });
	}
	
	/**
	 * Image sequence
	 */
	protected ArrayList<String> _images;
	
	/**
	 * Image path
	 */
	protected String _videoFile;
	
	/**
	 * Video object
	 */
	protected Movie _myMovie = null;
	
	/**
	 * Image sequence index
	 */
	protected int _frameIndex;
	
	/**
	 * Radians of the wind
	 */
	protected EasingFloat _windRadians;
	
	/**
	 * Array of Particles
	 */
	protected ArrayList<Particle> _particles;
	
	/**
	 * Array of frame/radians
	 */
	protected ArrayList<FrameRadians> _frameRadians;

	/**
	 * Current radians array index
	 */
	protected int _frameRadiansIndex;

	/**
	 * Last Frame
	 */
	protected FrameRadians _finalFrame;

	protected int NUM_PARTICLES = 500;
	
	public void setup() {
		_customPropsFile = "../data/properties/brimliskirepetitions.properties";
		super.setup();
	}

	// INITIALIZE OBJECTS ===================================================================================
	public void initRender() {
		_videoFile = _appConfig.getString( "video_file", "" );
		_frameIndex = 0;
		_myMovie = new Movie( this, _videoFile );
		_myMovie.play();
		_myMovie.frameRate( _fps );
		_myMovie.pause();
		
		buildRadiansForFrames();
		
//		_windRadians = MathUtil.randRange( 0, 2 * (float) Math.PI );
		
		_particles = new ArrayList<Particle>();
		for( int i = 0; i < NUM_PARTICLES; i++ ) {
			_particles.add( new Particle(i) );
		}
		
		_myMovie.read();
		for( int i = 0; i < NUM_PARTICLES; i++ ) {
			_particles.get(i).reset();
		}
	}
	
	protected void buildRadiansForFrames() {
		_windRadians = new EasingFloat( 0, 10 );
		_frameRadians = new ArrayList<FrameRadians>();
		_frameRadiansIndex = 0;
		
		_frameRadians.add( new FrameRadians( 0, 0, 0.00f, 45 ) );
		_frameRadians.add( new FrameRadians( 0, 6, 0.90f, 135 ) );
		_frameRadians.add( new FrameRadians( 0, 11, 0.70f, 350 ) );
		_frameRadians.add( new FrameRadians( 0, 17, 0.23f, 250 ) );
		_frameRadians.add( new FrameRadians( 0, 22, 0.86f, 0 ) );
		_frameRadians.add( new FrameRadians( 0, 28, 0.36f, 300 ) );
		_frameRadians.add( new FrameRadians( 0, 33, 0.93f, 270 ) );
		_frameRadians.add( new FrameRadians( 0, 39, 0.36f, 202 ) );
		_frameRadians.add( new FrameRadians( 0, 44, 0.80f, 275 ) );
		_frameRadians.add( new FrameRadians( 0, 50, 0.40f, 12 ) );
		_frameRadians.add( new FrameRadians( 0, 55, 0.00f, 0 ) );
		_frameRadians.add( new FrameRadians( 1, 29, 0.93f, 90 ) );
		_frameRadians.add( new FrameRadians( 1, 41, 0.13f, 0 ) );
		_frameRadians.add( new FrameRadians( 1, 48, 0.03f, 295 ) );
		_frameRadians.add( new FrameRadians( 1, 52, 0.06f, 183 ) );
		_frameRadians.add( new FrameRadians( 1, 58, 0.40f, 315 ) );
		_frameRadians.add( new FrameRadians( 2, 03, 0.06f, 280 ) );
		_frameRadians.add( new FrameRadians( 2, 07, 0.03f, 195 ) );
		_frameRadians.add( new FrameRadians( 2, 12, 0.13f, 300 ) );
		_frameRadians.add( new FrameRadians( 2, 17, 0.96f, 240 ) );
		_frameRadians.add( new FrameRadians( 2, 22, 0.10f, 30 ) );
		_frameRadians.add( new FrameRadians( 2, 26, 0.90f, 10 ) );
		_frameRadians.add( new FrameRadians( 2, 35, 0.26f, 180 ) );
		_frameRadians.add( new FrameRadians( 2, 40, 0.80f, 290 ) );
		_frameRadians.add( new FrameRadians( 2, 51, 0.60f, 254 ) );
		_frameRadians.add( new FrameRadians( 2, 56, 0.70f, 270 ) );
		_frameRadians.add( new FrameRadians( 3, 01, 0.83f, 90 ) );
		
		
		_finalFrame = new FrameRadians( 3, 13, 0.66f, -1 );
	}
		
	// FRAME LOOP RENDERING ===================================================================================
	public void drawApp() {
		if( p.frameCount == 1 ) initRender();
		p.background(0);
		p.fill( 255 );
		p.noStroke();
		p.rectMode( PConstants.CENTER );
		
		DrawUtil.setBasicLights( p );
		
		if( _myMovie != null ) _myMovie.read();
		if( _myMovie.pixels.length > 100 ) {
			setWindRadiansPerClip();		
			seekAndDrawMovieFrame();
			
			for( int i = 0; i < NUM_PARTICLES; i++ ) {
				_particles.get(i).update();
			}

		}
		// step to next image
		_frameIndex++;
		
		// stop when done
		if( _frameIndex == _finalFrame.frame + 1 ) {
			p.exit();
		}
	}
	
	protected void setWindRadiansPerClip() {
		_windRadians.update();
		if( _frameRadiansIndex < _frameRadians.size() && _frameRadians.get( _frameRadiansIndex ).frame == _frameIndex ) {
			_windRadians.setTarget( _frameRadians.get( _frameRadiansIndex ).radians );
			_frameRadiansIndex++;
		}
	}
		
	// MOVIE PARSING METHODS ===================================================================================
	protected void seekAndDrawMovieFrame() {
		seekTo( (float) p.frameCount / 30f );
		_myMovie.read();
//		p.image( _myMovie, 0, 0 );
	}
	
	public void randomMovieTime() {
		_myMovie.jump(random(_myMovie.duration()));
	}
	
	public void seekTo( float time ) {
		_myMovie.jump( time );
	}
	
	// Called every time a new frame is available to read
	public void movieEvent(Movie m) {
	  // m.read();
	}
	
	// PARTICLE CLASS ===========================================================================================	
	public class Particle {
		protected int _index;
		protected float _x = 0;
		protected float _y = 0;
		protected float _speed = 0;
		protected float _size = 0;
		protected EasingTColor _color = new EasingTColor( new TColor( TColor.WHITE ), 0.2f );
		protected PerlinNoise _perlin = new PerlinNoise();
		protected float _windOffset = 0;
		
		public Particle( int index ) {
			_index = index;
//			reset();
		}
		
		public void update() {
			_windOffset = _perlin.noise( p.frameCount / 1000f );
			
			_x += Math.sin( _windRadians.value() + _windOffset ) * _speed;
			_y += Math.cos( _windRadians.value() + _windOffset ) * _speed;
			checkBoundaries();
			
			// ease color towards current pixel
			_color.setTargetColor( TColor.newARGB( ImageUtil.getPixelColor( _myMovie, Math.round( _x ), Math.round( _y ) ) ) );
			_color.update();
			
			float amp = 0.5f + p.getAudio().getFFT().spectrum[_index % 512] * 2;
			
			p.fill( _color.color().toARGB() );
//			p.ellipse( _x, _y, _size * amp, _size * amp );
//			p.rect( _x, _y, _size * amp, _size * amp );

			p.pushMatrix();
			p.translate( _x, _y );
			p.sphere( _size * amp );
			p.popMatrix();
		}
		
		public void checkBoundaries() {
			if( _x > p.width - 1 ) _x = 0;
			if( _x < 0 ) _x = p.width - 1;
			if( _y > p.height - 1 ) _y = 0;
			if( _y < 0 ) _y = p.height - 1;
			// set color to pixel if recycled to other side of stage
			if( _x == 0 || _x == p.width - 1 || _y == 0 || _y == p.height - 1 ) setColorFromPosition();
		}
		
		public void reset() {
			_x = MathUtil.randRange( 0, p.width - 1 );
			_y = MathUtil.randRange( 0, p.height - 1 );
			
//			_speed = MathUtil.randRangeDecimel( 4, 12 );
			_size = MathUtil.randRangeDecimel( 10, 50 );
			_speed = _size/4;
			
			setColorFromPosition();
		}
		
		protected void setColorFromPosition() {
			TColor curColor = TColor.newARGB( ImageUtil.getPixelColor( _myMovie, (int) _x, (int) _y ) );
			_color.setCurAndTargetColors( curColor, curColor );
		}
	}
	
	// PARTICLE CLASS ===========================================================================================	
	public class FrameRadians {
		public int frame;
		public float radians;
		
		public FrameRadians( int minutes, int seconds, float hundredths, float angle ) {
			
			angle += 50; // hack to make angle correct - originals were entered as a standard cartesian angle
			
			frame = ( minutes * _fps * 60 ) + ( seconds * _fps ) + Math.round( hundredths * _fps );
			radians = angleToRadians( angle );
			
		}
		
		protected float angleToRadians( float angle ) {
			return  angle * (float) Math.PI / 180f; 
		}

	}
}
