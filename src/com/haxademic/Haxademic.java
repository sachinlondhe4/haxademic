//package com.haxademic;
//
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.sound.midi.InvalidMidiDataException;
//
//import krister.Ess.AudioInput;
//import oscP5.OscMessage;
//import processing.core.PApplet;
//import processing.core.PGraphics;
//import processing.core.PImage;
////import processing.video.Capture;
//import processing.video.*;
//import toxi.processing.ToxiclibsSupport;
//
//import com.haxademic.viz.IVizModule;
//import com.haxademic.viz.launchpad.LaunchpadViz;
//import com.haxademic.viz.modules.KacheOut;
//import com.p5core.audio.AudioInputWrapper;
//import com.p5core.audio.WaveformData;
//import com.p5core.data.P5Properties;
//import com.p5core.draw.model.ObjPool;
//import com.p5core.draw.text.DebugText;
//import com.p5core.hardware.kinect.KinectWrapper;
//import com.p5core.hardware.midi.MidiWrapper;
//import com.p5core.hardware.osc.OscWrapper;
//import com.p5core.render.MidiSequenceRenderer;
//import com.p5core.render.Renderer;
//import com.p5core.util.OpenGLUtil;
//import com.p5core.util.ScreenUtil;
//
//import fullscreen.FullScreen;
//
//
///**
// * Haxademic allows the developer to create any number of "modules",
// * with any number of "elements" that are controlled by each module.
// * One module can run at a time, and a programs may be switched with MIDI or keyboard stokes.
// * Haxademic instantiates any number of persistent objects that deal with hardware, 
// * user input, stage setup and callbacks that require definition in the PApplet.
// * 
// * @TODO: Address garbage collection - memory heap keeps growing like crazy
// * @TODO: optimize the kinectMesh element - shit is slow
// * @TODO: Mesh traversal drawing: more configurable. generative options - implement mesh drawing strategy pattern
// * @TODO: Finish converting old modules into new Elements: AudioTubes, Blobsheet, cacheRings outer rings, GridEQ w/lines, MaxCache outer rings, PlusRing, more spheres
// * @TODO: Create more abstract user/hardware input system that routes different inputs into certain types of commands.
// * @TODO: Allow more than just note_on messages from Haxademix base. should be able to respond to any midi data
// * @TODO: Port .js MathUtil methods in MathUtil
// * @TODO: Fix stepping through audio for WaveformData - this was hacked for BNC video rendering but shouldn't have to play & cue() the audio to capture the data
// * @TODO: Figure out why publish/output directory weirdness is happening, and why generated .class files need to be there...
// * @TODO: Don't initialize MIDI object if not defined in run.properties. Will need to prevent attempting to detect MIDI input on handleKeyboardInput() methods
// * @TODO: Figure out camera perspective stretching issues in MasterHax
// * @TODO: Improve launchpad visuals
// * @TODO: Add launchpad back in without a secondary AUdioInputWrapper
// * @TODO: Improve color selecting - use test sketch to figure out how to deal with color-traversing
// * @TODO: New elements: trails, supershapes, GEARS, particles
// * @TODO: add foreground/background modes to elements that could use them. 
// * @TODO: Create good input system for building up MasterHax module over time & manage flow of Elements.
// * @TODO: create more complex uses of new Elements
// * @TODO: Refine existing elements
// * 
// * @author cacheflowe
// *
// */
//public class Haxademic
//extends PApplet 
//{
//
//	/**
//	 * Auto-initialization of the main class.
//	 * @param args
//	 */
//	public static void main(String args[]) {
////		PApplet.main(new String[] { "--present", "--hide-stop", "--bgcolor=000000", "com.haxademic.Haxademic" });
//		PApplet.main(new String[] {              "--hide-stop", "--bgcolor=000000", "com.haxademic.Haxademic" });
//	}
//
////	/**
////	 * Global/static ref to PApplet - any audio-reactive object should be passed this reference, or grabbed from this static ref.
////	 */
////	private static Haxademic p;
////	
////	/**
////	 * Loads the project .properties file to configure several app properties externally.
////	 */
////	protected P5Properties _appConfig;
////	
////	/**
////	 * Single instance of Toxiclibs object
////	 */
////	public ToxiclibsSupport toxi;
////	
////	/**
////	 * FullScreen object to get rid of the grey toolbar on OS X.
////	 */
////	private FullScreen fs; 
////	
////	/**
////	 * Single instance and wrapper for the ESS audio object.
////	 */
////	public AudioInputWrapper _audioInput;
////	
////	/**
////	 * Single instance of the data needed to draw a realtime waveform / oscilloscpe.
////	 */
////	public WaveformData _waveformData;
////	
////	/**
////	 * Renderer object for saving frames and rendering movies.
////	 */
////	public Renderer _renderer;
////	public MidiSequenceRenderer _midiRenderer;
////	
////	/**
////	 * Wraps up MIDI functionality with theMIDIbus library.
////	 */
////	public MidiWrapper _midi = null;
////	
////	/**
////	 * Loads and stores a pool of .obj models.
////	 */
////	public ObjPool _objPool = null;
////	
////	/**
////	 * Wraps up Kinect functionality with openkinect library.
////	 */
////	public KinectWrapper _kinectWrapper = null;
////	
////	/**
////	 * A secondary system of running the visuals on the Launchpad. This should probably be integrated into Modules?
////	 */
////	public LaunchpadViz _launchpadViz = null;
////	
////	/**
////	 * Wraps up incoming OSC commands with the oscP5 library.
////	 */
////	public OscWrapper _oscWrapper = null;
//	
//	/**
//	 * Helps us draw text to the screen when needed.
//	 */
//	protected DebugText _debugText;
//	
//	
////	/**
////	 * Native Java object that simulates occasional keystrokes to prevent the system's screensaver from starting.
////	 */
////	protected Robot _robot;
////	
////	/**
////	 * Prevents crashing from possible attempts to re-initialize. 
////	 * Similar error described here: http://code.google.com/p/processing/issues/detail?id=356
////	 */
////	protected Boolean _is_setup = false;
////	
////	/**
////	 * Executable's target frames per second. 
////	 * This value is set in .properties file.
////	 */
////	public int _fps;
//	
//	/**
//	 * Disabled for now - should tell the BeatDetect library to help keep things interesting if there are no MIDI or OSC signals. 
//	 */
//	public Boolean _isAutoPilot = false;
//	
////	/**
////	 * Helps the Renderer object work with minimal reconfiguration. Maybe this should be moved at some point... 
////	 */
////	protected Boolean _isRendering = true;
////	
////	/**
////	 * Helps the Renderer object work without trying to read an audio file
////	 */
////	protected Boolean _isRenderingAudio = true;
////	
////	/**
////	 * Helps the Renderer object work without trying to read a MIDI file
////	 */
////	protected Boolean _isRenderingMidi = true;
//	
//	/**
//	 * The current IVizModule object that receives commands in the main Haxademic draw() loop.
//	 */
//	protected ArrayList<IVizModule> _modules;
//	
//	/**
//	 * The current index for _modules, which is changed to switch programs.
//	 */
//	protected int _curModule = 0;
//	
//	/**
//	 * No comment...
//	 * TODO: refactor this.
//	 */
//	protected int _numModules = 11;
//	
//	/**
//	 * Lets us use a sequence of pad presses to change programs via the Akai MPD.
//	 * @TODO: refactor this.
//	 */
//	protected boolean _readyForProgramChange = false;
//	
//	/**
//	 * Stores the next index for _modules, along with _readyForProgramChange.
//	 * @TODO: refactor this.
//	 */
//	protected int _readyForProgramChangeInt;
//
////	/**
////	 * Called by PApplet to run before the first draw() command.
////	 */
////	public void setup () {
////		p = this;
////		frame.setBackground(new java.awt.Color(0,0,0));
////		if ( !_is_setup ) { 
////			// load external properties and set flag
////			_appConfig = new P5Properties(p);
////			_is_setup = true;
////			// set screen size and renderer
////			String renderer = ( _appConfig.getBooleanProperty("sunflow", true ) == true ) ? "hipstersinc.P5Sunflow" : p.OPENGL;
////			if(_appConfig.getBooleanProperty("fills_screen", false)) {
////				size(screen.width,screen.height,renderer);
////			} else {
////				size(_appConfig.getIntProperty("width", 800),_appConfig.getIntProperty("height", 600),renderer);
////			}
////		}
////		
////		setAppletProps();
////		
////		// save toxi reference for any other classes
////		toxi = new ToxiclibsSupport(p);
////		
////		// init our IVizModules and supporting app-wide objects
////		initHaxademicObjects();
////		initVizModules();
////
////		// launch full-screen if needed
////		if(_appConfig.getBooleanProperty("fullscreen", false)) {
////			launchFullScreen();
////		}
////	}
////	
////	/**
////	 * Sets some initial Applet properties for OpenGL quality, FPS, and nocursor().
////	 */
////	protected void setAppletProps() {
////		_isRendering = _appConfig.getBooleanProperty("rendering", false);
////		_isRenderingAudio = _appConfig.getBooleanProperty("render_audio", false);
////		_isRenderingMidi = _appConfig.getBooleanProperty("render_midi", false);
////		if( _isRendering == true ) {
////			// prevents an error
//////			hint(DISABLE_OPENGL_2X_SMOOTH);
////			hint(ENABLE_OPENGL_4X_SMOOTH); 
////		} else {
////			if( _appConfig.getBooleanProperty("sunflow", true ) == false ) { 
////				OpenGLUtil.SetQuality(p, OpenGLUtil.MEDIUM);
////			}
////		}
////
////		_fps = _appConfig.getIntProperty("fps", 30);
////		frameRate(_fps);
////		noCursor();
////	}
////	
////	/**
////	 * Initializes app-wide support objects for hardware interaction and rendering purposes.
////	 */
////	protected void initHaxademicObjects() {
////		_audioInput = new AudioInputWrapper( p, _isRenderingAudio );
////		_waveformData = new WaveformData( p, _audioInput._bufferSize );
//////		_objPool = new ObjPool( p );
////		_renderer = new Renderer( p, _fps, Renderer.OUTPUT_TYPE_MOVIE, _appConfig.getStringProperty( "render_output_dir", "bin/output/" ) );
////		_kinectWrapper = new KinectWrapper( p );
//////		_launchpadViz = new LaunchpadViz( p5 );
////		_oscWrapper = new OscWrapper( p );
////		_debugText = new DebugText( p );
////		try { _robot = new Robot(); } catch( Exception error ) { println("couldn't init Robot for screensaver disabling"); }
////		
////	}
//	
//	/**
//	 * Initializes all the IVizModules that we've created.
//	 * @TODO: externalize this for different people's implementations
//	 */
//	protected void initVizModules() {
//		_modules = new ArrayList<IVizModule>();
//		_modules.add( new KacheOut() );
////		_modules.add( new MasterHax() );
////		_modules.add( new Boxen3D() );
////		_modules.add( new Toxi() );
////		_modules.add( new Spheres() );
////		_modules.add( new BlobSheet() );
////		_modules.add( new GridAndLinesEQ() );
////		_modules.add( new CacheRings() );
////		_modules.add( new PlusRing() );
////		_modules.add( new HorizLines() );
////		_modules.add( new AudioTubes() );
////		_modules.add( new MaxCache() );
//
//		_modules.trimToSize();
//		_modules.get( _curModule ).focus();
//	}
//
////	/**
////	 * Inits the full-screen object and launches it.
////	 */
////	protected void launchFullScreen() {
////		// add fullscreen - use fs.enter(); to make it happen immediately, or use cmd + F to toggle fullscreen manually 
////		fs = new FullScreen(this); 
//////		fs.setResolution( width, height );
////		fs.enter();
////	}
//	
//	/**
//	 * Called by PApplet as the main draw loop.
//	 */
//	public void draw() {
////		// analyze & init audio if stepping through a render
////		if( _isRendering == true ) {
////			if( p.frameCount == 2 ) {
////				if( _isRenderingAudio == true ) {
////					String audioFile = _appConfig.getStringProperty("render_audio_file", "");
////					_renderer.startRendererForAudio( audioFile, _audioInput );
////					_audioInput.gainDown();
////					_audioInput.gainDown();
////					_audioInput.gainDown();
////				} else {
////					_renderer.startRenderer();
////				}
////				if( _isRenderingMidi == true ) {
////					try {
////						_midiRenderer = new MidiSequenceRenderer(p);
////						String midiFile = _appConfig.getStringProperty("render_midi_file", "");
////						_midiRenderer.loadMIDIFile( midiFile, 124, 30, -8f );	// bnc: 98  jack-splash: 
////					} catch (InvalidMidiDataException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
////				}
////				_readyForProgramChangeInt = 0;
////			}
////			if( p.frameCount > 1 ) {
////				// have renderer step through audio, then special call to update the single WaveformData storage object				
////				if( _isRenderingAudio == true ) {
////					_renderer.analyzeAudio();				
////					_waveformData.updateWaveformDataForRender( _renderer, _audioInput.getAudioInput(), _audioInput._bufferSize );
////				}
////			}
////		}
////		
////		// wait until draw() happens, to avoid weird launch crash if midi signals were coming in as haxademic starts
////		if( _midi == null ) {
//////			if( _appConfig.getStringProperty("midi_device_in", "") != "" ) {
////				_midi = new MidiWrapper( p, _appConfig.getStringProperty("midi_device_in", ""), _appConfig.getStringProperty("midi_device_out", "") );
//////			}
////		}
////		
////		// handles overall keyboard commands
////		if( keyPressed ) handleKeyboardInput( false );		//  || _midi.midiPadIsOn( MidiWrapper.PAD_16 ) == 1
////		if( _midiRenderer != null ) {
////			boolean doneCheckingForMidi = false;
////			boolean triggered = false;
////			while( doneCheckingForMidi == false ) {
////				int rendererNote = _midiRenderer.checkCurrentNoteEvent();
////				if( rendererNote != -1 ) {
////					noteOn( 0, rendererNote, 100 );
////					triggered = true;
////				} else {
////					doneCheckingForMidi = true;
////				}
////			}
////			if( triggered == false ) _midi.allOff();
////		}
//		
//		// switch the program if we actually changed it
//		if( _readyForProgramChangeInt != _curModule )
//		{
//			_readyForProgramChange = false;
//			_curModule = _readyForProgramChangeInt;
//			p.camera();
//			p.background(0);
//			_modules.get( _curModule ).focus(); 
//		}
//		
////		// detect beats and pass through to current visual module
////		int[] beatDetectArr = _audioInput.getBeatDetection();
////		_modules.get( _curModule ).beatDetect( beatDetectArr[0], beatDetectArr[1], beatDetectArr[2], beatDetectArr[3] );
//		
//		// update current visual module
//		try{ _modules.get( _curModule ).update(); }
//		catch( ArrayIndexOutOfBoundsException e ){println("draw() broke: ArrayIndexOutOfBoundsException");}
//		
//		// update launchpad hardware if it's around
//		if( _launchpadViz != null ) _launchpadViz.update();
//		
////		// render frame if rendering
////		if( _isRendering == true ) _renderer.renderFrame();
////		
////		// keep screensaver off - hit shift every 1000 frames
////		if( p.frameCount % 1000 == 0 ) _robot.keyRelease(KeyEvent.VK_SHIFT);
//
//		// display some info
////		_debugText.draw( "Debug :: FPS:" + _fps );
//		
//		
//		if(frameCount == -1) {
//			p.camera();
//			PGraphics screenshot = createGraphics(width, height, P3D );
//			p.beginRecord(screenshot);
//			p.camera();
//			_modules.get( _curModule ).update();
//			p.endRecord();
//			PImage newImg = screenshot.get();
//			
//			println("screeshotting!");
////			filter(THRESHOLD);
//			
//	//		PImage newImg = createImage(1000, 1000, RGB);
////			PImage newImg = ScreenUtil.getScreenAsPImage( p5 );
//	//		println(newImg.width+"x"+newImg.height);
//			p.noStroke();
//	//		newImg.save( "output/saved_img/test.png" );
//			p.tint(255, 0, 255, 200f);
//	//		image( screenshot, 20, 20 );
//	//		image( screenshot, 40, 40 );
//	//		image( screenshot, 60, 60 );
////			translate(0,0,-100);
////			rotateX(TWO_PI/10);
////			rotateZ(TWO_PI/10);
//			
//			
////			textureMode(IMAGE);
////			imageMode(CORNERS);
////			resetMatrix();
////			beginShape(QUADS);
////			texture(newImg);
////			vertex(0, 0, 0, 0);
////			vertex(width, 0, width, 0);
////			vertex(width, height, width, height);
////			vertex(0, 0, 0, height);
////			endShape();
////			
////			translate(10,10,0);
////			beginShape(QUADS);
////			texture(newImg);
////			vertex(0, 0, 0, 0);
////			vertex(width, 0, width, 0);
////			vertex(width, height, width, height);
////			vertex(0, 0, 0, height);
////			endShape();
////
////			translate(10,10,0);
////			translate(0,0,-200);
////			beginShape(QUADS);
////			texture(newImg);
////			vertex(0, 0, 0, 0);
////			vertex(width, 0, width, 0);
////			vertex(width, height, width, height);
////			vertex(0, 0, 0, height);
////			endShape();
//
//			imageMode(CORNER);
////			translate(100,100,0);
//			image(newImg, 0, 0, width, height);
//		}
//	}
//
////	// switch between modules
////	public void handleKeyboardInput( Boolean isMidi ) {
////		int prevModule = _curModule;
////		
////		// change programs with midi pads
////		if( _readyForProgramChange ) {
////			if( _midi.midiPadIsOn( MidiWrapper.PAD_01 ) == 1 ) _readyForProgramChangeInt = 0;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_02 ) == 1 ) _readyForProgramChangeInt = 1;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_03 ) == 1 ) _readyForProgramChangeInt = 2;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_04 ) == 1 ) _readyForProgramChangeInt = 3;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_05 ) == 1 ) _readyForProgramChangeInt = 4;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_06 ) == 1 ) _readyForProgramChangeInt = 5;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_07 ) == 1 ) _readyForProgramChangeInt = 6;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_08 ) == 1 ) _readyForProgramChangeInt = 7;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_09 ) == 1 ) _readyForProgramChangeInt = 8;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_10 ) == 1 ) _readyForProgramChangeInt = 9;
////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_11 ) == 1 ) _readyForProgramChangeInt = 10;
//////			else if( _midi.midiPadIsOn( MidiWrapper.PAD_12 ) == 1 ) _readyForProgramChangeInt = 11;
////		} else if( _midi != null && _midi.midiPadIsOn( MidiWrapper.PAD_16 ) == 1 ) {
////			_readyForProgramChange = true;
////		}
////		
////		// handle midi loop program changes
////		if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_01 ) == 1 ) _readyForProgramChangeInt = 0;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_02 ) == 1 ) _readyForProgramChangeInt = 1;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_03 ) == 1 ) _readyForProgramChangeInt = 2;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_04 ) == 1 ) _readyForProgramChangeInt = 3;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_05 ) == 1 ) _readyForProgramChangeInt = 4;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_06 ) == 1 ) _readyForProgramChangeInt = 5;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_07 ) == 1 ) _readyForProgramChangeInt = 6;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_08 ) == 1 ) _readyForProgramChangeInt = 7;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_09 ) == 1 ) _readyForProgramChangeInt = 8;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_10 ) == 1 ) _readyForProgramChangeInt = 9;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_11 ) == 1 ) _readyForProgramChangeInt = 10;
////		else if( _midi.midiPadIsOn( MidiWrapper.PROGRAM_12 ) == 1 ) _readyForProgramChangeInt = 11;
////		
////		if( !isMidi ) {
////			// change programs with keyboard
////			if ( key == '!' ) _readyForProgramChangeInt = 0;
////			if ( key == '@' ) _readyForProgramChangeInt = 1;
////			if ( key == '#' ) _readyForProgramChangeInt = 2; 
////			if ( key == '$' ) _readyForProgramChangeInt = 3;
////			if ( key == '%' ) _readyForProgramChangeInt = 4;
////			if ( key == '^' ) _readyForProgramChangeInt = 5;
////			if ( key == '&' ) _readyForProgramChangeInt = 6;
////			if ( key == '*' ) _readyForProgramChangeInt = 7; 
////			if ( key == '(' ) _readyForProgramChangeInt = 8; 
////			if ( key == ')' ) _readyForProgramChangeInt = 9; 
////			if ( key == '_' ) _readyForProgramChangeInt = 10; 
////			//if ( key == '+' ) _readyForProgramChangeInt = 11; 
////			
////			// audio gain
////			if ( key == '.' || _midi.midiPadIsOn( MidiWrapper.PAD_14 ) == 1 ) _audioInput.gainUp(); 
////			if ( key == ',' || _midi.midiPadIsOn( MidiWrapper.PAD_13 ) == 1 ) _audioInput.gainDown(); 
////			
////			// big screenshot
////			if ( key == '\\' ) { 
////				ScreenUtil.screenshotHiRes( p, 3, p.P3D, "output/saved_img/" );
////			}
////			
////
////			// toggle autopilot
////	//		if( key == 'A' )
////	//		{
////	//			_isAutoPilot = !_isAutoPilot;
////	//		}
////		}
////		
////	}
////
////	/**
////	 * Called by PApplet as the keyboard input listener.
////	 * This makes sure only the active IVizModule receives keyboard input
////	 */
////	public void keyPressed() {
////		_modules.get( _curModule ).handleKeyboardInput();
////	}
////
////	/**
////	 * Called by PApplet to shut down the Applet.
////	 * We stop rendering if applicable, and clean up hardware connections that might barf if left open.
////	 */
////	public void stop() {
////		if( _kinectWrapper != null ) _kinectWrapper.stop();
////		if( _launchpadViz != null ) _launchpadViz.dispose();
////		if( _isRendering ) _renderer.stop();
////	}
////
////	/**
////	 * PApplet-level listener for MIDIBUS noteOn call
////	 */
////	public void noteOn(int channel, int  pitch, int velocity) {
////		if( _midi != null ) { 
////			_midi.noteOn( channel, pitch, velocity );
////			try{ 
////				handleKeyboardInput( true );
////				_modules.get( _curModule ).handleKeyboardInput();
////			}
////			catch( ArrayIndexOutOfBoundsException e ){println("noteOn BROKE!");}
////		}
////	}
////	
////	/**
////	 * PApplet-level listener for MIDIBUS noteOff call
////	 */
////	public void noteOff(int channel, int  pitch, int velocity) {
////		if( _midi != null ) _midi.noteOff( channel, pitch, velocity );
////	}
////	
////	/**
////	 * PApplet-level listener for MIDIBUS CC signal
////	 */
////	public void controllerChange(int channel, int number, int value) {
////		if( _midi != null ) _midi.controllerChange( channel, number, value );
////	}
////
////	/**
////	 * PApplet-level listener for AudioInput data from the ESS library
////	 */
////	public void audioInputData(AudioInput theInput) {
////		_audioInput.getFFT().getSpectrum(theInput);
////		if( _launchpadViz != null ) _launchpadViz.getAudio().getFFT().getSpectrum(theInput);
////		_audioInput.detector.detect(theInput);
////		_waveformData.updateWaveformData( theInput, _audioInput._bufferSize );
////	}
////	
////	/**
////	 * PApplet-level listener for OSC data from the oscP5 library
////	 */
////	public void oscEvent(OscMessage theOscMessage) {
////		int oscValue = theOscMessage.get(0).intValue();
////		String oscMsg = theOscMessage.addrPattern();
////		_oscWrapper.setOscMapItem(oscMsg, oscValue);
////
////		try { 
////			if( oscValue == 1 ) {
////				handleKeyboardInput( true );
////				_modules.get( _curModule ).handleKeyboardInput();
////			}
////		}
////		catch( ArrayIndexOutOfBoundsException e ){println("noteOn BROKE!");}
////	}
////
////	/**
////	 * Getters / Setters
////	 */
////	// instance of this -------------------------------------------------
////	public static Haxademic getInstance(){ return p; }
////	// instance of toxiclibs -------------------------------------------------
////	public ToxiclibsSupport getToxi(){ return toxi; }
////	// instance of audio wrapper -------------------------------------------------
////	public AudioInputWrapper getAudio() { return _audioInput; }
////	// instance of midi wrapper -------------------------------------------------
////	public MidiWrapper getMidi() { return _midi; }
////	// instance of midi wrapper -------------------------------------------------
////	public OscWrapper getOsc() { return _oscWrapper; }
////	// get fps of app -------------------------------------------------
////	public int getFps() { return _fps; }
////	// get fps factor of app -------------------------------------------------
////	public float getFpsFactor() { return 30f / _fps; }
////	// get autopilot boolean -------------------------------------------------
////	public Boolean getIsAutopilot() { return _isAutoPilot; }
//}
