package com.haxademic.viz.modules;

import toxi.color.ColorList;
import toxi.color.ColorRange;
import toxi.color.TColor;
import toxi.color.theory.ColorTheoryStrategy;
import toxi.color.theory.CompoundTheoryStrategy;

import com.haxademic.app.P;
import com.haxademic.core.util.ColorGroup;
import com.haxademic.viz.IVizModule;
import com.haxademic.viz.VizCollection;
import com.haxademic.viz.elements.AppFrame2d;
import com.haxademic.viz.elements.BarsEQ2d;
import com.haxademic.viz.elements.BarsModEQ;
import com.haxademic.viz.elements.BlobSheetElement;
import com.haxademic.viz.elements.GridEQ;
import com.haxademic.viz.elements.LinesEQ;
import com.haxademic.viz.elements.MeshDeform;
import com.haxademic.viz.elements.OuterSphere;
import com.haxademic.viz.elements.RotatingRings;
import com.haxademic.viz.elements.RotatorShapes;
import com.haxademic.viz.elements.SphereClouds;
import com.haxademic.viz.elements.SphereTextureLines;
import com.haxademic.viz.elements.SphericalHarmonicsOscillator;
import com.haxademic.viz.elements.StarField;
import com.haxademic.viz.elements.WaveformPlane;
import com.haxademic.viz.elements.WaveformShapes;
import com.haxademic.viz.elements.WaveformSingle;

public class MasterHax 
extends VizCollection
implements IVizModule {
	
	public MasterHax() {
		super();
	}

	public void addElements() {

//		_fgElements.add( new Invaders( p, toxi, _audioData ) );
		_fgElements.add( new WaveformSingle( p, toxi, _audioData ) );
		_fgElements.add( new WaveformPlane( p, toxi, _audioData ) );
		_fgElements.add( new WaveformShapes( p, toxi, _audioData ) );
		_fgElements.add( new RotatorShapes( p, toxi, _audioData ) );
		_fgElements.add( new MeshDeform( p, toxi, _audioData ) );
//		_fgElements.add( new ObjMesh( p, toxi, _audioData ) );
		SphereTextureLines sphereLinesSmall = new SphereTextureLines( p, toxi, _audioData );
		sphereLinesSmall.setDrawProps( 150 );
		_fgElements.add( sphereLinesSmall );
//		_fgElements.add( new CacheLogo( p, toxi, _audioData ) );
		_fgElements.add( new SphericalHarmonicsOscillator( p, toxi, _audioData ) );
//		_fgElements.add( new KinectMesh( p, toxi, _audioData, p._kinectWrapper ) );
		
		_bgElements.add( new BlobSheetElement( p, toxi, _audioData ) );
		_bgElements.add( new RotatingRings( p, toxi, _audioData ) );
//		_bgElements.add( new BarsEQ( p, toxi, _audioData ) );
		_bgElements.add( new BarsModEQ( p, toxi, _audioData ) );
		_bgElements.add( new LinesEQ( p, toxi, _audioData ) );
		_bgElements.add( new GridEQ( p, toxi, _audioData ) );
		
		_ambientElements.add( new SphereClouds( p, toxi, _audioData ) );
		_ambientElements.add( new StarField( p, toxi, _audioData ) );
		
		_2dElements.add( new BarsEQ2d( p, toxi, _audioData ) );
		_2dElements.add( new AppFrame2d( p, toxi, _audioData ) );

		_outerElements.add( new OuterSphere( p, toxi, _audioData ) );
		SphereTextureLines sphereLines = new SphereTextureLines( p, toxi, _audioData );
		sphereLines.setDrawProps( 4000 );
		_outerElements.add( sphereLines );

	}
	
	protected void pickNewColors() {

		if( _balletColors == null ) _balletColors = new ColorGroup( ColorGroup.NEON );
		_balletColors.setRandomGroup();
		_colorFG1 = _balletColors.getColorFromIndex( 0 );
		_colorFG2 = _balletColors.getColorFromIndex( 1 );
		_colorAmbient = _balletColors.getColorFromIndex( 2 );
		_colorBG1 = _balletColors.getColorFromIndex( 3 );
		_colorBG2 = _balletColors.getColorFromIndex( 4 );
		
		storeCurColors();
	}
	

	
}