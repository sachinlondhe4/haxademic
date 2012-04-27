package com.haxademic.app.kacheout.screens;

import toxi.color.TColor;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.data.easing.EasingFloat3d;
import com.haxademic.core.data.easing.ElasticFloat;
import com.haxademic.core.util.DrawUtil;

public class IntroScreen {
	protected KacheOut p;
	
	protected final TColor MODE_SET_GREY = new TColor( TColor.newRGB( 96, 96, 96 ) );
	protected final TColor MODE_SET_BLUE = new TColor( TColor.newRGB( 0, 200, 234 ) );
	protected final TColor CACHEFLOWE_YELLOW = new TColor( TColor.newRGB( 255, 249, 0 ) );
	protected final TColor WHITE = new TColor( TColor.WHITE );
	
	protected EasingFloat3d _cdLogoLoc;
	protected EasingFloat3d _presentsLoc;
	protected EasingFloat3d _kacheOutLoc;
	protected EasingFloat3d _modeSetLogoLoc;
	protected ElasticFloat _modeSetLogoZ;
	protected EasingFloat3d _modeSetTextLoc;
	protected EasingFloat3d _cacheFloweLogoLoc;
	protected EasingFloat3d _cacheFloweTextLoc;
	protected EasingFloat3d _designByLoc;
	protected EasingFloat3d _designJonLoc;
	protected EasingFloat3d _designRyanLoc;
	
	protected int _frameCount;

	public IntroScreen() {
		p = (KacheOut)PAppletHax.getInstance();
		
		_cdLogoLoc = new EasingFloat3d( 0, 0, 0, 5 );
		_presentsLoc = new EasingFloat3d( 0, 0, 0, 7 );
		_kacheOutLoc = new EasingFloat3d( 0, 0, 0, 5 );
		_modeSetLogoLoc = new EasingFloat3d( 0, 0, 0, 5 );
		_modeSetLogoZ = new ElasticFloat( -1400f, 0.8f, 0.4f );
		_modeSetTextLoc = new EasingFloat3d( 0, 0, 0, 7 );
		_cacheFloweLogoLoc = new EasingFloat3d( 0, 0, 0, 5 );
		_cacheFloweTextLoc = new EasingFloat3d( 0, 0, 0, 7 );
		_designByLoc = new EasingFloat3d( 0, 0, 0, 5 );
		_designJonLoc = new EasingFloat3d( 0, 0, 0, 7 );
		_designRyanLoc = new EasingFloat3d( 0, 0, 0, 9 );
	}
	
	public void reset() {
		_frameCount = 0;
		
		_cdLogoLoc.setCurrentY( p.stageHeight() );
		_cdLogoLoc.setTargetY( p.stageHeight() );
		_presentsLoc.setCurrentY( p.stageHeight() );
		_presentsLoc.setTargetY( p.stageHeight() );
		_kacheOutLoc.setCurrentY( p.stageHeight() );
		_kacheOutLoc.setTargetY( p.stageHeight() );
		_modeSetLogoLoc.setCurrentY( p.stageHeight() );
		_modeSetLogoLoc.setTargetY( p.stageHeight() * 10f );
		_modeSetTextLoc.setCurrentY( p.stageHeight() );
		_modeSetTextLoc.setTargetY( p.stageHeight() );
		_cacheFloweLogoLoc.setCurrentY( p.stageHeight() );
		_cacheFloweLogoLoc.setTargetY( p.stageHeight() );
		_cacheFloweTextLoc.setCurrentY( p.stageHeight() );
		_cacheFloweTextLoc.setTargetY( p.stageHeight() );
		_designByLoc.setCurrentY( p.stageHeight() );
		_designByLoc.setTargetY( p.stageHeight() );
		_designJonLoc.setCurrentY( p.stageHeight() );
		_designJonLoc.setTargetY( p.stageHeight() );
		_designRyanLoc.setCurrentY( p.stageHeight() );
		_designRyanLoc.setTargetY( p.stageHeight() );
		
		_modeSetLogoZ.setValue( -1400f );
		_modeSetLogoZ.setTarget( -1400f );

	}
	
	public void update() {
		// animate on certain frames
		if( _frameCount == 0 ) {
			p.soundtrack.playIntro();
//			p.sounds.playSound("INSERT_COIN");
			_cdLogoLoc.setTargetY( 0 );
			_presentsLoc.setTargetY( 100 );
		} else if( _frameCount == 30 ) {
		} else if( _frameCount == 60 ) {
			_cdLogoLoc.setTargetY( -p.stageHeight() );
			_presentsLoc.setTargetY( -p.stageHeight() );
			_kacheOutLoc.setTargetY( 0 );
		} else if( _frameCount == 120 ) {
			_kacheOutLoc.setTargetY( -p.stageHeight() );
			_modeSetLogoLoc.setTargetY( -60 );
			_modeSetTextLoc.setTargetY( 160 );
			_modeSetLogoZ.setTarget( 0 );
		}
		
		// update positions
		_cdLogoLoc.update();
		_presentsLoc.update();
		_kacheOutLoc.update();
		_modeSetLogoLoc.update();
		_modeSetLogoZ.update();
		_modeSetTextLoc.update();
		_cacheFloweLogoLoc.update();
		_cacheFloweTextLoc.update();
		_designByLoc.update();
		_designJonLoc.update();
		_designRyanLoc.update();
		
		
		// set up for drawing objects
		p.pushMatrix();
		DrawUtil.setCenter( p );
		p.translate( 0, 0, p.gameBaseZ() );
		
		
		
		// draw create denver text
		p.pushMatrix();
		p.translate( _cdLogoLoc.valueX(), _cdLogoLoc.valueY(), 0 );
		p.fill( WHITE.toARGB() );
		p.noStroke();
		p.toxi.mesh( p.meshPool.getMesh( p.CREATE_DENVER ) );
		p.popMatrix();
		
		// draw "presents"
		p.pushMatrix();
		p.translate( _presentsLoc.valueX(), _presentsLoc.valueY(), 0 );
		p.fill( WHITE.toARGB() );
		p.noStroke();
		p.toxi.mesh( p.meshPool.getMesh( p.PRESENTS_TEXT ) );
		p.popMatrix();
		
		// draw kacheout logo
		p.pushMatrix();
		p.translate( _kacheOutLoc.valueX(), _kacheOutLoc.valueY(), 0 );
		p.fill( WHITE.toARGB() );
		p.noStroke();
		p.toxi.mesh( p.meshPool.getMesh( p.KACHEOUT_LOGO ) );
		p.popMatrix();	
		
		// draw mode set logo
		p.pushMatrix();
		p.translate( _modeSetLogoLoc.valueX(), _modeSetLogoLoc.valueY(), _modeSetLogoZ.val() );
		p.fill( MODE_SET_BLUE.toARGB() );
		p.noStroke();
		p.toxi.mesh( p.meshPool.getMesh( p.MODE_SET_LOGO ) );
		p.popMatrix();	
		
		// draw mode set text
		p.pushMatrix();
		p.translate( _modeSetTextLoc.valueX(), _modeSetTextLoc.valueY(), 0 );
		p.fill( MODE_SET_GREY.toARGB() );
		p.noStroke();
		p.toxi.mesh( p.meshPool.getMesh( p.MODE_SET_LOGOTYPE ) );
		p.popMatrix();	
		
		
		
		// reset
		p.popMatrix();
		
		_frameCount++;
	}
}
