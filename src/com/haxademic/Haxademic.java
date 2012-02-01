package com.haxademic;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import krister.Ess.AudioInput;
import oscP5.OscMessage;
import processing.core.PApplet;
import processing.core.PFont;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.IVizModule;
import com.haxademic.viz.launchpad.LaunchpadViz;
import com.haxademic.viz.modules.MasterHax;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.audio.WaveformData;
import com.p5core.data.P5Properties;
import com.p5core.hardware.kinect.KinectWrapper;
import com.p5core.hardware.midi.MidiWrapper;
import com.p5core.hardware.osc.OscWrapper;
import com.p5core.render.MidiSequenceRenderer;
import com.p5core.render.Renderer;
import com.p5core.util.OpenGLUtil;

import fullscreen.FullScreen;


/**
 * Haxademic allows the developer to create any number of "modules",
 * with any number of "elements" that are controlled by each module.
 * One module can run at a time, and a programs may be switched with MIDI or keyboard stokes.
 * Haxademic instantiates any number of persistent objects that deal with hardware, 
 * user input, stage setup and callbacks that require definition in the PApplet.
 * 
 * @TODO: Improve color selecting - use test sketch to figure out how to deal with color-traversing
 * @TODO: Address garbage collection - memory heap keeps growing like crazy
 * @TODO: optimize the kinectMesh element - shit is slow
 * @TODO: add 3d objects loader and mesh-returner - use them as backgrounds
 * @TODO: New elements: trails, supershapes, GEARS, particles
 * @TODO: add foreground/background modes to elements that could use them. 
 * @TODO: Finish converting old modules into new Elements: AudioTubes, Blobsheet, cacheRings outer rings, GridEQ w/lines, MaxCache outer rings, PlusRing, more spheres
 * @TODO: Create good input system for building up MasterHax module over time & manage flow of Elements.
 * @TODO: Create more abstract user/hardware input system that routes different inputs into certain types of commands.
 * @TODO: Improve launchpad visuals
 * @TODO: create more complex uses of new Elements
 * @TODO: Refine existing elements
 * @TODO: Mesh traversal drawing
 * @TODO: Add launchpad back in without a secondary AUdioInputWrapper
 * @TODO: Allow more than just note_on messages from Haxademix base. should be able to respond to any midi data
 * @TODO: Port .js MathUtil methods in MathUtil
 * 
 * @author cacheflowe
 *
 */
public class Haxademic
extends PApplet 
{

	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
//		PApplet.main(new String[] { "--present", "--hide-stop", "--bgcolor=000000", "com.haxademic.Haxademic" });
		PApplet.main(new String[] {              "--hide-stop", "--bgcolor=000000", "com.haxademic.Haxademic" });
	}

	/**
	 * Global/static ref to PApplet - any audio-reactive object should be passed this reference, or grabbed from this static ref.
	 */
	private static Haxademic p5;
	
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
	 * Disabled for now - should tell the BeatDetect library to help keep things interesting if there are no MIDI or OSC signals. 
	 */
	public Boolean _isAutoPilot = false;
	
	/**
	 * Helps the Renderer object work with minimal reconfiguration. Maybe this should be moved at some point... 
	 */
	protected Boolean _isRendering = true;
	
	/**
	 * Load a font for debug help at the moment. 
	 * @TODO: Should be refactored. 
	 */
	protected PFont _debugFont;	

	/**
	 * The current IVizModule object that receives commands in the main Haxademic draw() loop.
	 */
	protected IVizModule[] _modules;
	
	/**
	 * The current index for _modules, which is changed to switch programs.
	 */
	protected int _curModule = 0;
	
	/**
	 * No comment...
	 * TODO: refactor this.
	 */
	protected int _numModules = 11;
	
	/**
	 * Lets us use a sequence of pad presses to change programs via the Akai MPD.
	 * @TODO: refactor this.
	 */
	protected boolean _readyForProgramChange = false;
	
	/**
	 * Stores the next index for _modules, along with _readyForProgramChange.
	 * @TODO: refactor this.
	 */
	protected int _readyForProgramChangeInt;

	/**
	 * Called by PApplet to run before the first draw() command.
	 */
	public void setup () {
		p5 = this;
		if ( !_is_setup ) { 
			// load external properties and set flag
			_appConfig = new P5Properties(p5);
			_is_setup = true;
			// set screen size
			if(_appConfig.getBooleanProperty("fills_screen", false)) {
				size(screen.width,screen.height,OPENGL);
			} else {
				size(_appConfig.getIntProperty("width", 800),_appConfig.getIntProperty("height", 600),OPENGL);
			}
		}
		
		setAppletProps();
		
		// save toxi reference for any other classes
		toxi = new ToxiclibsSupport(p5);
		
		// init our IVizModules and supporting app-wide objects
		initHaxademicObjects();
		initVizModules();

		// launch full-screen if needed
		if(_appConfig.getBooleanProperty("fullscreen", false)) {
			launchFullScreen();
		}

		// testing font
//		_debugFont = FontUtil.FontLoad(this, "helvetica_95_black-webfont.svg", 24);
//		textMode(SCREEN);
	}
	
	/**
	 * Sets some initial Applet properties for OpenGL quality, FPS, and nocursor().
	 */
	protected void setAppletProps() {
		_isRendering = _appConfig.getBooleanProperty("rendering", false);
		if( _isRendering == true ) {
			// prevents an error
//			hint(DISABLE_OPENGL_2X_SMOOTH);
			hint(ENABLE_OPENGL_4X_SMOOTH); 
		} else {
			OpenGLUtil.SetQuality(p5, OpenGLUtil.MEDIUM);
		}

		_fps = _appConfig.getIntProperty("fps", 30);
		frameRate(_fps);
		noCursor();
	}
	
	/**
	 * Initializes app-wide support objects for hardware interaction and rendering purposes.
	 */
	protected void initHaxademicObjects() {
		_audioInput = new AudioInputWrapper( p5, _isRendering );
		_waveformData = new WaveformData( p5, _audioInput._bufferSize );
		_renderer = new Renderer( p5, _fps, Renderer.OUTPUT_TYPE_MOVIE );
		_kinectWrapper = new KinectWrapper( p5 );
//		_launchpadViz = new LaunchpadViz( p5 );
		_oscWrapper = new OscWrapper( p5 );
		try { _robot = new Robot(); } catch( Exception error ) { println("couldn't init Robot for screensaver disabling"); }
	}
	
	/**
	 * Initializes all the IVizModules that we've created.
	 * @TODO: externalize this for different people's implementations
	 */
	protected void initVizModules() {
		_modules = new IVizModule[ _numModules ];
		_modules[0] = new MasterHax();
//		_modules[1] = new Boxen3D();
//		_modules[2] = new Toxi();
//		_modules[3] = new Spheres();
//		_modules[4] = new BlobSheet();
//		_modules[5] = new GridAndLinesEQ();
//		_modules[6] = new CacheRings();
//		_modules[7] = new PlusRing();
//		_modules[8] = new HorizLines();
//		_modules[9] = new AudioTubes();
//		_modules[10] = new MaxCache();
		
		_modules[ _curModule ].focus();
	}

	/**
	 * Inits the full-screen object and launches it.
	 */
	protected void launchFullScreen() {
		// add fullscreen - use fs.enter(); to make it happen immediately, or use cmd + F to toggle fullscreen manually 
		fs = new FullScreen(this); 
//		fs.setResolution( width, height );
		fs.enter();
	}
	
	/**
	 * Called by PApplet as the main draw loop.
	 */
	public void draw() {
		// analyze & init audio if stepping through a render
		if( _isRendering == true ) {
			if( p5.frameCount == 2 ) {
				_renderer.startRendererForAudio( "wav/JackSplash.wav", _audioInput );	// cache-money.wav	// dumbo-gets-mad---plumy-tale.wav
				if( _appConfig.getBooleanProperty("render_midi", false) == true ) {
					try {
						_midiRenderer = new MidiSequenceRenderer(p5);
						_midiRenderer.loadMIDIFile( "data/bnc/plumy-tale-complex.mid", 98, 30, -8f );
					} catch (InvalidMidiDataException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
				}
				_readyForProgramChangeInt = 0;
//				_audioInput.gainUp();
			}
			if( p5.frameCount > 1 ) {
				// have renderer step through audio, then special call to update the single WaveformData storage object				
				_renderer.analyzeAudio();				
				_waveformData.updateWaveformDataForRender( _renderer, _audioInput.getAudioInput(), _audioInput._bufferSize );
			}
		}
		
		// wait until draw() happens, to avoid weird launch crash if midi signals were coming in as haxademic starts
		if( _midi == null ) _midi = new MidiWrapper( p5, _appConfig.getStringProperty("midi_device_in", "UA-25"), _appConfig.getStringProperty("midi_device_out", "UA-25") );
		
		// handles overall keyboard commands
		if( keyPressed ) handleKeyboardInput( false );		//  || _midi.midiPadIsOn( MidiWrapper.PAD_16 ) == 1
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
		
		// switch the program if we actually changed it
		if( _readyForProgramChangeInt != _curModule )
		{
			_readyForProgramChange = false;
			_curModule = _readyForProgramChangeInt;
			camera();
			background(0);
			_modules[ _curModule ].focus(); 
		}
		
		// detect beats and pass through to current visual module
		int[] beatDetectArr = _audioInput.getBeatDetection();
		_modules[ _curModule ].beatDetect( beatDetectArr[0], beatDetectArr[1], beatDetectArr[2], beatDetectArr[3] );
		
		// update current visual module
		try{ _modules[ _curModule ].update(); }
		catch( ArrayIndexOutOfBoundsException e ){println("draw() broke: ArrayIndexOutOfBoundsException");}
		
		// update launchpad hardware if it's around
		if( _launchpadViz != null ) _launchpadViz.update();
		
		// render frame if rendering
		if( _isRendering == true ) _renderer.renderFrame();
		
		// keep screensaver off - hit shift every 1000 frames
		if( p5.frameCount % 1000 == 0 ) _robot.keyRelease(KeyEvent.VK_SHIFT);

		// test text
//		textFont(metaBold);
//		fill(1,1);
//		text("Debug :: FPS:" + _fps, 10, 40);
		
	}

	// switch between modules
	public void handleKeyboardInput( Boolean isMidi ) {
		
		int prevModule = _curModule;
		
		// change programs with midi pads
		if( _readyForProgramChange ) {
			if( _midi.midiPadIsOn( MidiWrapper.PAD_01 ) == 1 ) _readyForProgramChangeInt = 0;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_02 ) == 1 ) _readyForProgramChangeInt = 1;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_03 ) == 1 ) _readyForProgramChangeInt = 2;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_04 ) == 1 ) _readyForProgramChangeInt = 3;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_05 ) == 1 ) _readyForProgramChangeInt = 4;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_06 ) == 1 ) _readyForProgramChangeInt = 5;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_07 ) == 1 ) _readyForProgramChangeInt = 6;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_08 ) == 1 ) _readyForProgramChangeInt = 7;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_09 ) == 1 ) _readyForProgramChangeInt = 8;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_10 ) == 1 ) _readyForProgramChangeInt = 9;
			else if( _midi.midiPadIsOn( MidiWrapper.PAD_11 ) == 1 ) _readyForProgramChangeInt = 10;
//			else if( _midi.midiPadIsOn( MidiWrapper.PAD_12 ) == 1 ) _readyForProgramChangeInt = 11;
		} else if( _midi != null && _midi.midiPadIsOn( MidiWrapper.PAD_16 ) == 1 ) {
			_readyForProgramChange = true;
		}
		
		// handle midi loop program changes
		if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_01 ) == 1 ) _readyForProgramChangeInt = 0;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_02 ) == 1 ) _readyForProgramChangeInt = 1;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_03 ) == 1 ) _readyForProgramChangeInt = 2;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_04 ) == 1 ) _readyForProgramChangeInt = 3;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_05 ) == 1 ) _readyForProgramChangeInt = 4;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_06 ) == 1 ) _readyForProgramChangeInt = 5;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_07 ) == 1 ) _readyForProgramChangeInt = 6;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_08 ) == 1 ) _readyForProgramChangeInt = 7;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_09 ) == 1 ) _readyForProgramChangeInt = 8;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_10 ) == 1 ) _readyForProgramChangeInt = 9;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_11 ) == 1 ) _readyForProgramChangeInt = 10;
		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_12 ) == 1 ) _readyForProgramChangeInt = 11;
		
		if( !isMidi ) {
			// change programs with keyboard
			if ( key == '!' ) _readyForProgramChangeInt = 0;
			if ( key == '@' ) _readyForProgramChangeInt = 1;
			if ( key == '#' ) _readyForProgramChangeInt = 2; 
			if ( key == '$' ) _readyForProgramChangeInt = 3;
			if ( key == '%' ) _readyForProgramChangeInt = 4;
			if ( key == '^' ) _readyForProgramChangeInt = 5;
			if ( key == '&' ) _readyForProgramChangeInt = 6;
			if ( key == '*' ) _readyForProgramChangeInt = 7; 
			if ( key == '(' ) _readyForProgramChangeInt = 8; 
			if ( key == ')' ) _readyForProgramChangeInt = 9; 
			if ( key == '_' ) _readyForProgramChangeInt = 10; 
			//if ( key == '+' ) _readyForProgramChangeInt = 11; 
			
			// audio gain
			if ( key == '.' || _midi.midiPadIsOn( MidiWrapper.PAD_14 ) == 1 ) _audioInput.gainUp(); 
			if ( key == ',' || _midi.midiPadIsOn( MidiWrapper.PAD_13 ) == 1 ) _audioInput.gainDown(); 
			
			// toggle autopilot
	//		if( key == 'A' )
	//		{
	//			_isAutoPilot = !_isAutoPilot;
	//		}
		}
		
	}

	/**
	 * Called by PApplet as the keyboard input listener.
	 * This makes sure only the active IVizModule receives keyboard input
	 */
	public void keyPressed() {
		_modules[ _curModule ].handleKeyboardInput();
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
				handleKeyboardInput( true );
				_modules[ _curModule ].handleKeyboardInput();
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
				handleKeyboardInput( true );
				_modules[ _curModule ].handleKeyboardInput();
			}
		}
		catch( ArrayIndexOutOfBoundsException e ){println("noteOn BROKE!");}
	}


	/**
	 * Getters / Setters
	 */
	// instance of this -------------------------------------------------
	public static Haxademic getInstance(){ return p5; }
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
	public Boolean getIsAutopilot() { return _isAutoPilot; }
}
