package com.haxademic.viz.elements;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.util.DrawUtil;

public class RotatorShapes 
extends ElementBase 
implements IVizElement {
	
	RotatorShape _rotator;
	RotatorShape _rotatorBG;

	public RotatorShapes( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData ) {
		super( p, toxi, audioData );
		init();
	}
	
	public void init() {
		_rotator = new RotatorShape( p, toxi, _audioData, 12 );
		_rotatorBG = new RotatorShape( p, toxi, _audioData, 12 );
		reset();
	}

	public void updateColor( TColor colorFG ) {
		_rotator.updateColor( colorFG );
		_rotatorBG.updateColor( colorFG.getComplement() );
	}

	public void update() {
		DrawUtil.setCenter( p );
		DrawUtil.resetGlobalProps( p );
		p.strokeWeight(1);

		p.pushMatrix();
		p.translate( 0, 0, -500 );
		_rotator.update();
		p.popMatrix();

		p.pushMatrix();
		p.translate( 0, 0, -2000 );
		_rotatorBG.update();
		p.popMatrix();

	}
	
	public void reset() {
		_rotator.reset();
		_rotatorBG.reset();
	}
	
	public void updateLineMode() {
		_rotator.updateLineMode();
		_rotatorBG.updateLineMode();
	}
	
	public void updateCamera() {
		_rotator.updateCamera();
		_rotatorBG.updateCamera();
	}

	
	public void dispose() {
		super.dispose();
		_rotator.dispose();
		_rotator = null;
		_rotatorBG.dispose();
		_rotatorBG = null;
	}
	
}
