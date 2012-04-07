package com.haxademic.app;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import krister.Ess.AudioInput;
import oscP5.OscMessage;

//import com.haxademic.Haxademic;
import com.haxademic.viz.launchpad.LaunchpadViz;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.audio.WaveformData;
import com.p5core.data.P5Properties;
import com.p5core.draw.model.ObjPool;
import com.p5core.draw.text.DebugText;
import com.p5core.hardware.kinect.KinectWrapper;
import com.p5core.hardware.midi.MidiWrapper;
import com.p5core.hardware.osc.OscWrapper;
import com.p5core.render.MidiSequenceRenderer;
import com.p5core.render.Renderer;
import com.p5core.util.OpenGLUtil;
import com.p5core.util.ScreenUtil;

import fullscreen.FullScreen;
import processing.core.PApplet;
import toxi.processing.ToxiclibsSupport;

public class PAppletHax
extends PApplet 
{
	/**
	 * Global/static ref to PApplet - any audio-reactive object should be passed this reference, or grabbed from this static ref.
	 */
	protected static PAppletHax p;
	
	/**
	 * Loads the project .properties file to configure several app properties externally.
	 */
	protected P5Properties _appConfig;
	
	/**
	 * Single instance of Toxiclibs object
	 */
	public ToxiclibsSupport toxi;
	
	/**
	 * FullScreen object to get rid of the grey toolbar on OS X.
	 */
	private FullScreen fs; 
	
	/**
	 * Single instance and wrapper for the ESS audio object.
	 */
	public AudioInputWrapper _audioInput;
	
	/**
	 * Single instance of the data needed to draw a realtime waveform / oscilloscpe.
	 */
	public WaveformData _waveformData;
	
	/**
	 * Renderer object for saving frames and rendering movies.
	 */
	public Renderer _renderer;
	public MidiSequenceRenderer _midiRenderer;
	
	/**
	 * Wraps up MIDI functionality with theMIDIbus library.
	 */
	public MidiWrapper _midi = null;
	
	/**
	 * Loads and stores a pool of .obj models.
	 */
	public ObjPool _objPool = null;
	
	/**
	 * Wraps up Kinect functionality with openkinect library.
	 */
	public KinectWrapper _kinectWrapper = null;
	
	/**
	 * A secondary system of running the visuals on the Launchpad. This should probably be integrated into Modules?
	 */
	public LaunchpadViz _launchpadViz = null;
	
	/**
	 * Wraps up incoming OSC commands with the oscP5 library.
	 */
	public OscWrapper _oscWrapper = null;
	
	/**
	 * Native Java object that simulates occasional keystrokes to prevent the system's screensaver from starting.
	 */
	protected Robot _robot;
	
	/**
	 * Prevents crashing from possible attempts to re-initialize. 
	 * Similar error described here: http://code.google.com/p/processing/issues/detail?id=356
	 */
	protected Boolean _is_setup = false;
	
	/**
	 * Executable's target frames per second. 
	 * This value is set in .properties file.
	 */
	public int _fps;
	
	/**
	 * Helps the Renderer object work with minimal reconfiguration. Maybe this should be moved at some point... 
	 */
	protected Boolean _isRendering = true;
	
	/**
	 * Helps the Renderer object work without trying to read an audio file
	 */
	protected Boolean _isRenderingAudio = true;
	
	/**
	 * Helps the Renderer object work without trying to read a MIDI file
	 */
	protected Boolean _isRenderingMidi = true;

	/**
	 * Called by PApplet to run before the first draw() command.
	 */
	public void setup () {
		p = this;
		frame.setBackground(new java.awt.Color(0,0,0));
		if ( !_is_setup ) { 
			// load external properties and set flag
			_appConfig = new P5Properties(p);
			_is_setup = true;
			// set screen size and renderer
			String renderer = ( _appConfig.getBooleanProperty("sunflow", true ) == true ) ? "hipstersinc.P5Sunflow" : p.OPENGL;
			if(_appConfig.getBooleanProperty("fills_screen", false)) {
				size(screen.width,screen.height,renderer);
			} else {
				size(_appConfig.getIntProperty("width", 800),_appConfig.getIntProperty("height", 600),renderer);
			}
		}
		
		setAppletProps();
		
		// save toxi reference for any other classes
		toxi = new ToxiclibsSupport(p);
		
		initHaxademicObjects();

		setupApp();
	}
	
	/**
	 * Sets some initial Applet properties for OpenGL quality, FPS, and nocursor().
	 */
	protected void setAppletProps() {
		_isRendering = _appConfig.getBooleanProperty("rendering", false);
		_isRenderingAudio = _appConfig.getBooleanProperty("render_audio", false);
		_isRenderingMidi = _appConfig.getBooleanProperty("render_midi", false);
		if( _isRendering == true ) {
			// prevents an error
//			hint(DISABLE_OPENGL_2X_SMOOTH);
			hint(ENABLE_OPENGL_4X_SMOOTH); 
		} else {
			if( _appConfig.getBooleanProperty("sunflow", true ) == false ) { 
				OpenGLUtil.SetQuality(p, OpenGLUtil.MEDIUM);
			}
		}

		_fps = _appConfig.getIntProperty("fps", 30);
		frameRate(_fps);
		noCursor();
	}
	
	/**
	 * Initializes app-wide support objects for hardware interaction and rendering purposes.
	 */
	protected void initHaxademicObjects() {
		_audioInput = new AudioInputWrapper( p, _isRenderingAudio );
		_waveformData = new WaveformData( p, _audioInput._bufferSize );
//		_objPool = new ObjPool( p );
		_renderer = new Renderer( p, _fps, Renderer.OUTPUT_TYPE_MOVIE, _appConfig.getStringProperty( "render_output_dir", "bin/output/" ) );
		_kinectWrapper = new KinectWrapper( p );
//		_launchpadViz = new LaunchpadViz( p5 );
		_oscWrapper = new OscWrapper( p );
//		_debugText = new DebugText( p );
		try { _robot = new Robot(); } catch( Exception error ) { println("couldn't init Robot for screensaver disabling"); }
		
	}
	
	public void draw() {
		// analyze & init audio if stepping through a render
		if( _isRendering == true ) {
			if( p.frameCount == 2 ) {
				if( _isRenderingAudio == true ) {
					String audioFile = _appConfig.getStringProperty("render_audio_file", "");
					_renderer.startRendererForAudio( audioFile, _audioInput );
					_audioInput.gainDown();
					_audioInput.gainDown();
					_audioInput.gainDown();
				} else {
					_renderer.startRenderer();
				}
				if( _isRenderingMidi == true ) {
					try {
						_midiRenderer = new MidiSequenceRenderer(p);
						String midiFile = _appConfig.getStringProperty("render_midi_file", "");
						_midiRenderer.loadMIDIFile( midiFile, 124, 30, -8f );	// bnc: 98  jack-splash: 
					} catch (InvalidMidiDataException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
				}
			}
			if( p.frameCount > 1 ) {
				// have renderer step through audio, then special call to update the single WaveformData storage object				
				if( _isRenderingAudio == true ) {
					_renderer.analyzeAudio();				
					_waveformData.updateWaveformDataForRender( _renderer, _audioInput.getAudioInput(), _audioInput._bufferSize );
				}
			}
		}
		
		// wait until draw() happens, to avoid weird launch crash if midi signals were coming in as haxademic starts
		if( _midi == null ) {
//			if( _appConfig.getStringProperty("midi_device_in", "") != "" ) {
				_midi = new MidiWrapper( p, _appConfig.getStringProperty("midi_device_in", ""), _appConfig.getStringProperty("midi_device_out", "") );
//			}
		}

		
		
		// handles overall keyboard commands
		if( keyPressed ) handleInput( false );		//  || _midi.midiPadIsOn( MidiWrapper.PAD_16 ) == 1
		if( _midiRenderer != null ) {
			boolean doneCheckingForMidi = false;
			boolean triggered = false;
			while( doneCheckingForMidi == false ) {
				int rendererNote = _midiRenderer.checkCurrentNoteEvent();
				if( rendererNote != -1 ) {
					noteOn( 0, rendererNote, 100 );
					triggered = true;
				} else {
					doneCheckingForMidi = true;
				}
			}
			if( triggered == false ) _midi.allOff();
		}

		// detect beats and pass through to current visual module
		int[] beatDetectArr = _audioInput.getBeatDetection();

		drawApp();
		
		// render frame if rendering
		if( _isRendering == true ) _renderer.renderFrame();
		
		// keep screensaver off - hit shift every 1000 frames
		if( p.frameCount % 1000 == 0 ) _robot.keyRelease(KeyEvent.VK_SHIFT);

		// display some info
//		_debugText.draw( "Debug :: FPS:" + _fps );
	}
	
	protected void setupApp() {
		p.println("YOU MUST OVERRIDE setupApp()");
	}
	
	protected void drawApp() {
		p.println("YOU MUST OVERRIDE drawApp()");
	}
	
	// switch between modules
	protected void handleInput( Boolean isMidi ) {
		
	}

	/**
	 * Called by PApplet as the keyboard input listener.
	 * This makes sure only the active IVizModule receives keyboard input
	 */
	public void keyPressed() {
	}

	/**
	 * Called by PApplet to shut down the Applet.
	 * We stop rendering if applicable, and clean up hardware connections that might barf if left open.
	 */
	public void stop() {
		if( _kinectWrapper != null ) _kinectWrapper.stop();
		if( _launchpadViz != null ) _launchpadViz.dispose();
		if( _isRendering ) _renderer.stop();
	}

	/**
	 * PApplet-level listener for MIDIBUS noteOn call
	 */
	public void noteOn(int channel, int  pitch, int velocity) {
		if( _midi != null ) { 
			_midi.noteOn( channel, pitch, velocity );
			try{ 
				handleInput( true );
			}
			catch( ArrayIndexOutOfBoundsException e ){println("noteOn BROKE!");}
		}
	}
	
	/**
	 * PApplet-level listener for MIDIBUS noteOff call
	 */
	public void noteOff(int channel, int  pitch, int velocity) {
		if( _midi != null ) _midi.noteOff( channel, pitch, velocity );
	}
	
	/**
	 * PApplet-level listener for MIDIBUS CC signal
	 */
	public void controllerChange(int channel, int number, int value) {
		if( _midi != null ) _midi.controllerChange( channel, number, value );
	}

	/**
	 * PApplet-level listener for AudioInput data from the ESS library
	 */
	public void audioInputData(AudioInput theInput) {
		_audioInput.getFFT().getSpectrum(theInput);
		if( _launchpadViz != null ) _launchpadViz.getAudio().getFFT().getSpectrum(theInput);
		_audioInput.detector.detect(theInput);
		_waveformData.updateWaveformData( theInput, _audioInput._bufferSize );
	}
	
	/**
	 * PApplet-level listener for OSC data from the oscP5 library
	 */
	public void oscEvent(OscMessage theOscMessage) {
		int oscValue = theOscMessage.get(0).intValue();
		String oscMsg = theOscMessage.addrPattern();
		_oscWrapper.setOscMapItem(oscMsg, oscValue);

		try { 
			if( oscValue == 1 ) {
				handleInput( true );
			}
		}
		catch( ArrayIndexOutOfBoundsException e ){println("noteOn BROKE!");}
	}

	/**
	 * Getters / Setters
	 */
	// instance of this -------------------------------------------------
	public static PAppletHax getInstance(){ return p; }
	// instance of toxiclibs -------------------------------------------------
	public ToxiclibsSupport getToxi(){ return toxi; }
	// instance of audio wrapper -------------------------------------------------
	public AudioInputWrapper getAudio() { return _audioInput; }
	// instance of midi wrapper -------------------------------------------------
	public MidiWrapper getMidi() { return _midi; }
	// instance of midi wrapper -------------------------------------------------
	public OscWrapper getOsc() { return _oscWrapper; }
	// get fps of app -------------------------------------------------
	public int getFps() { return _fps; }
	// get fps factor of app -------------------------------------------------
	public float getFpsFactor() { return 30f / _fps; }
	// get autopilot boolean -------------------------------------------------
//	public Boolean getIsAutopilot() { return _isAutoPilot; }
}
