package com.haxademic.viz.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.TColor;
import toxi.color.theory.ColorTheoryRegistry;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.color.theory.CompoundTheoryStrategy;

import com.haxademic.viz.IVizElement;
import com.haxademic.viz.IVizModule;
import com.haxademic.viz.ModuleBase;
import com.haxademic.viz.elements.BarsEQ;
import com.haxademic.viz.elements.CacheLogo;
import com.haxademic.viz.elements.GridEQ;
import com.haxademic.viz.elements.Invaders;
import com.haxademic.viz.elements.KinectMesh;
import com.haxademic.viz.elements.LinesEQ;
import com.haxademic.viz.elements.ObjMesh;
import com.haxademic.viz.elements.OuterSphere;
import com.haxademic.viz.elements.RotatingRings;
import com.haxademic.viz.elements.RotatorShapes;
import com.haxademic.viz.elements.SphereClouds;
import com.haxademic.viz.elements.SphericalHarmonicsOscillator;
import com.haxademic.viz.elements.WaveformPlane;
import com.haxademic.viz.elements.WaveformShapes;
import com.p5core.cameras.CameraBasic;
import com.p5core.hardware.midi.MidiWrapper;
import com.p5core.util.ColorGroup;
import com.p5core.util.DrawUtil;
import com.p5core.util.MathUtil;

public class MasterHax 
extends ModuleBase
implements IVizModule
{
	// class props
	protected int _numAverages = 32;
	
	protected ColorList _colorList;
	protected TColor _colorFG1;
	protected TColor _colorFG2;
	protected TColor _colorAmbient;
	protected TColor _colorBG1;
	protected TColor _colorBG2;
	
	protected IVizElement _outerElement = null;
	protected IVizElement _bgElement = null;
	protected IVizElement _fgElement = null;
	protected IVizElement _ambientElement = null;
	
	protected Vector<IVizElement> _bgElements;
	protected Vector<IVizElement> _fgElements;
	protected Vector<IVizElement> _ambientElements;
	protected Vector<IVizElement> _outerElements;
	
	protected ColorGroup _balletColors;
	
	protected int _numBigChanges = 0;
	
	protected float _curCameraZ = 0;
	
	public MasterHax( ) {
		super();
		initAudio();
		// init viz
		init();
	}

	public void init() {
		_curCamera = new CameraBasic( p, 0, 0, (int)_curCameraZ );
		_curCamera.reset();
		_curCamera.setTarget( 0, 0, 300 );

		_bgElements = new Vector<IVizElement>();
		_bgElements.add( new RotatingRings( p, toxi, _audioData ) );
		_bgElements.add( new BarsEQ( p, toxi, _audioData ) );
		_bgElements.add( new LinesEQ( p, toxi, _audioData ) );
		_bgElements.add( new GridEQ( p, toxi, _audioData ) );
		
		_fgElements = new Vector<IVizElement>();
		_fgElements.add( new Invaders( p, toxi, _audioData ) );
		_fgElements.add( new WaveformPlane( p, toxi, _audioData ) );
		_fgElements.add( new WaveformShapes( p, toxi, _audioData ) );
		_fgElements.add( new RotatorShapes( p, toxi, _audioData ) );
		_fgElements.add( new ObjMesh( p, toxi, _audioData ) );
//		_fgElements.add( new CacheLogo( p, toxi, _audioData ) );
		_fgElements.add( new SphericalHarmonicsOscillator( p, toxi, _audioData ) );
//		_fgElements.add( new KinectMesh( p, toxi, _audioData, p._kinectWrapper ) );
		
		_ambientElements = new Vector<IVizElement>();
		_ambientElements.add( new SphereClouds( p, toxi, _audioData ) );

		_outerElements = new Vector<IVizElement>();
		_outerElements.add( new OuterSphere( p, toxi, _audioData ) );
		
		pickElements();
		pickNewColors();
		pickMode();
	}

	public void initAudio() {
		_audioData.setNumAverages( _numAverages );
		_audioData.setDampening( .13f );
	}

	public void focus() {
		initAudio();
	}

	public void update() {
		// start drawing at center
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );
		DrawUtil.setBasicLights( p );
		
		// clear screen and set camera
		p.background(0,0,0,255f);
		
		// draw shapes
		if( _outerElement != null ) _outerElement.update();
		if( _bgElement != null ) _bgElement.update();
		if( _fgElement != null ) _fgElement.update();
		if( _ambientElement != null ) _ambientElement.update();		

		// set camera
		_curCameraZ = MathUtil.easeTo(_curCameraZ, 0, 10);
		_curCamera.setPosition(0, 0, (int)_curCameraZ);
		_curCamera.setTarget( 0, 0, -100 );
		_curCamera.update();

//		debugDrawColorList();
		// lets us use the keyboard to funk it up
//		if( p.keyPressed ) handleKeyboardInput();
	}
	
	protected void getNewColorList() {
		// get a new ColorList
		TColor col = ColorRange.LIGHT.getColor();
		// loop through strategies
		ArrayList strategies = ColorTheoryRegistry.getRegisteredStrategies();
		for (Iterator i=strategies.iterator(); i.hasNext();) {
			ColorTheoryStrategy s = (ColorTheoryStrategy) i.next();
			_colorList = ColorList.createUsingStrategy(s, col);
			_colorList = new ColorRange( _colorList ).addBrightnessRange(0.8f,1).getColors(null,100,0.05f);
			_colorList.sortByDistance(true);
		}
	}
	
	protected void debugDrawColorList() {
		DrawUtil.setTopLeft( p );
		// draw the color list for debug purposes
		int x = 0;
		for (Iterator<TColor> i = _colorList.iterator(); i.hasNext();) {
			TColor c = (TColor) i.next();
			p.fill(c.toARGB());
			x += 10;
			p.rect(x,0,x+10,10);
		}
	}
	
	protected void pickNewColors() {
		float lighten = 0.4f;

		if( 1 == 1 ) {
			// get a single strategy
			TColor color = ColorRange.LIGHT.getColor();
			ColorTheoryStrategy strategy = new CompoundTheoryStrategy ();
//		TColor color = ColorRange.BRIGHT.getColor();
//		ColorTheoryStrategy strategy = new RightSplitComplementaryStrategy();
			_colorList = ColorList.createUsingStrategy(strategy, color);
			
			// store a few random colors
//		TColor color1 = _colorList.getRandom();
//		color1.lighten(0.3f);
			_colorFG1 = _colorList.get( 0 );
			_colorFG2 = _colorList.get( 1 );//_colorFG1.getAnalog(45,1);//_colorList.get( 1 );//.getRandom();	// color1.complement().toARGB()
			_colorAmbient = _colorList.get( 2 );//_colorFG2.getAnalog(45,1);//_colorList.get( 2 );
			_colorBG1 = _colorList.get( 3 );//_colorAmbient.getAnalog(45,1);//_colorList.get( 3 );
			_colorBG2 = _colorList.get( 4 );//_colorBG1.getAnalog(45,1);//_colorList.get( 4 );
			
			_colorFG1.adjustRGB( lighten, lighten, lighten );
			_colorFG2.adjustRGB( lighten, lighten, lighten );
			_colorAmbient.adjustRGB( lighten, lighten, lighten );
			_colorBG1.adjustRGB( lighten, lighten, lighten );
			_colorBG2.adjustRGB( lighten, lighten, lighten );

//			_colorFG1.lighten( lighten );
//			_colorFG2.lighten( lighten );
//			_colorAmbient.lighten( lighten );
//			_colorBG1.lighten( lighten );
//			_colorBG2.lighten( lighten );
			
//			if( _balletColors == null ) {
				_balletColors = new ColorGroup( -1 );
				_balletColors.createGroupWithTColors( _colorFG1, _colorFG2, _colorAmbient, _colorBG1, _colorBG2 );
//			}
		} else {
			if( _balletColors == null ) {
				_balletColors = new ColorGroup( ColorGroup.BALLET );
			}
			_balletColors.setRandomGroup();
			_colorFG1 = _balletColors.getColorFromIndex( 0 );
			_colorFG2 = _balletColors.getColorFromIndex( 1 );//_colorFG1.getAnalog(45,1);//_colorList.get( 1 );//.getRandom();	// color1.complement().toARGB()
			_colorAmbient = _balletColors.getColorFromIndex( 2 );//_colorFG2.getAnalog(45,1);//_colorList.get( 2 );
			_colorBG1 = _balletColors.getColorFromIndex( 3 );//_colorAmbient.getAnalog(45,1);//_colorList.get( 3 );
			_colorBG2 = _balletColors.getColorFromIndex( 4 );//_colorBG1.getAnalog(45,1);//_colorList.get( 4 );
		}
		
		_colorFG1.setBrightness( lighten ).lighten( lighten );
		_colorFG2.setBrightness( lighten ).lighten( lighten );
		_colorBG1.setBrightness( lighten ).lighten( lighten );
		_colorBG2.setBrightness( lighten ).lighten( lighten );
		_colorAmbient.setBrightness( lighten ).lighten( lighten );
		
		storeCurColors();
	}
	
	protected void storeCurColors() {
		if( _outerElement != null ) _outerElement.updateColorSet( _balletColors );
		if( _bgElement != null ) _bgElement.updateColorSet( _balletColors );
		if( _fgElement != null ) _fgElement.updateColorSet( _balletColors );
		if( _ambientElement != null ) _ambientElement.updateColorSet( _balletColors );		
	}
	
	public void handleKeyboardInput() {
		if ( p.key == 'm' || p.key == 'M' || p.getMidi().midiPadIsOn( MidiWrapper.PAD_04 ) == 1 || p.getMidi().midiPadIsOn( MidiWrapper.NOTE_04 ) == 1 ) {
			pickMode();
			pickNewColors();
		}
		if ( p.key == 'c' || p.key == 'C' || p.getMidi().midiPadIsOn( MidiWrapper.PAD_01 ) == 1 || p.getMidi().midiPadIsOn( MidiWrapper.NOTE_01 ) == 1 ) {
			pickNewColors();
		}
		if ( p.key == 'v' || p.key == 'V' || p.getMidi().midiPadIsOn( MidiWrapper.PAD_02 ) == 1 || p.getMidi().midiPadIsOn( MidiWrapper.NOTE_02 ) == 1 ) {
			newCamera();
		}
		if ( p.key == 'l' || p.key == 'L' || p.getMidi().midiPadIsOn( MidiWrapper.PAD_08 ) == 1 || p.getMidi().midiPadIsOn( MidiWrapper.NOTE_08 ) == 1 ) {
			newLineMode();
		}
		if ( p.key == ' ' || p.getMidi().midiPadIsOn( MidiWrapper.PAD_07 ) == 1 || p.getMidi().midiPadIsOn( MidiWrapper.NOTE_07 ) == 1 ) {
			pickElements();
			pickNewColors();
		}
	}
	
	protected void pickElements() {
		// stagger swapping on FG and BG elements - find cur index and increment to next
		// pick bg element
		if( _numBigChanges % 2 == 1 ) {
			int curBGIndex = _bgElements.indexOf( _bgElement );
			curBGIndex = ( curBGIndex < _bgElements.size() - 1 ) ? curBGIndex + 1 : 0;
			_bgElement = _bgElements.get( curBGIndex );
		}
		
		// pick fg element
		if( _numBigChanges % 2 == 0 ) {
			int curFGIndex = _fgElements.indexOf( _fgElement );
			curFGIndex = ( curFGIndex < _fgElements.size() - 1 ) ? curFGIndex + 1 : 0;
			_fgElement = _fgElements.get( curFGIndex );
		}
		
		// pick outer element - randomly turn it off
		if( _numBigChanges > 3 ) {
			int curOuterIndex = _outerElements.indexOf( _outerElement );
			curOuterIndex = ( curOuterIndex < _outerElements.size() - 1 ) ? curOuterIndex + 1 : 0;
			_outerElement = _outerElements.get( curOuterIndex );
		}
		_outerElement = ( p.round( p.random( 0, 3 ) ) == 1 ) ? null : _outerElement;
		
		// pick ambient element
		if( _numBigChanges > 4 ) {
			int curAmbientIndex = _ambientElements.indexOf( _ambientElement );
			curAmbientIndex = ( curAmbientIndex < _ambientElements.size() - 1 ) ? curAmbientIndex + 1 : 0;
			_ambientElement = _ambientElements.get( curAmbientIndex );
		}
		_ambientElement = ( MathUtil.randBoolean( p ) == true ) ? null : _ambientElement;
		
		// keep track of changes
		_numBigChanges++;
	}
	
	protected void newCamera() {
		if( _outerElement != null && MathUtil.randBoolean( p ) == true ) _outerElement.updateCamera();
		if( _bgElement != null && MathUtil.randBoolean( p ) == true ) _bgElement.updateCamera();
		if( _fgElement != null && MathUtil.randBoolean( p ) == true ) _fgElement.updateCamera();
		if( _ambientElement != null && MathUtil.randBoolean( p ) == true ) _ambientElement.updateCamera();
	}
	
	protected void newLineMode() {
		if( _outerElement != null && MathUtil.randBoolean( p ) == true ) _outerElement.updateLineMode();
		if( _bgElement != null && MathUtil.randBoolean( p ) == true ) _bgElement.updateLineMode();
		if( _fgElement != null && MathUtil.randBoolean( p ) == true ) _fgElement.updateLineMode();
		if( _ambientElement != null && MathUtil.randBoolean( p ) == true ) _ambientElement.updateLineMode();
	}
	
	protected void pickMode() {
		
		if( _outerElement != null ) _outerElement.reset();
		if( _bgElement != null ) _bgElement.reset();
		if( _fgElement != null ) _fgElement.reset();
		if( _ambientElement != null ) _ambientElement.reset();
				
		_curCameraZ = -200;
		_curCamera.setPosition(0, 0, (int)_curCameraZ);
	}

	public void beatDetect( int isKickCount, int isSnareCount, int isHatCount, int isOnsetCount ) {

	}
	
}