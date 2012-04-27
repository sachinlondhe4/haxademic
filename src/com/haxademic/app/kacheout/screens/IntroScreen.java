package com.haxademic.app.kacheout.screens;

import toxi.color.TColor;
import toxi.geom.mesh.WETriangleMesh;

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
		updateAnimationsOnFrameCount();
		updatePositions();
		drawObjects();
		_frameCount++;
	}
	
	protected void updateAnimationsOnFrameCount() {
		// animate on certain frames
		if( _frameCount == 0 ) {
			p.soundtrack.playIntro();
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
		} else if( _frameCount == 150 ) {
			_modeSetLogoLoc.setTargetY( -p.stageHeight() );
			_modeSetTextLoc.setTargetY( -p.stageHeight() );
			_cacheFloweLogoLoc.setTargetY( -60 );
			_cacheFloweTextLoc.setTargetY( 160 );
		} else if( _frameCount == 200 ) {
			_cacheFloweLogoLoc.setTargetY( -p.stageHeight() );
			_cacheFloweTextLoc.setTargetY( -p.stageHeight() );
			_designByLoc.setTargetY( -90 );
			_designJonLoc.setTargetY( 0 );
			_designRyanLoc.setTargetY( 100 );
		} else if( _frameCount == 250 ) {
			_designByLoc.setTargetY( -p.stageHeight() );
			_designJonLoc.setTargetY( -p.stageHeight() );
			_designRyanLoc.setTargetY( -p.stageHeight() );
		} else if( _frameCount == 255 ) {
			p.soundtrack.stop();
			p.sounds.playSound("INSERT_COIN");
			p.setGameMode( p.GAME_READY );
		}
	}
	
	protected void updatePositions() {
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
	}
	
	protected void drawObjects() {
		// set up for drawing objects
		p.pushMatrix();
		DrawUtil.setCenter( p );
		p.translate( 0, 0, p.gameBaseZ() );
		
		
		
		// draw create denver text & "presents"
		drawObjectAtLoc( p.meshPool.getMesh( p.CREATE_DENVER ), _cdLogoLoc.valueX(), _cdLogoLoc.valueY(), 0, WHITE.toARGB() );
		drawObjectAtLoc( p.meshPool.getMesh( p.PRESENTS_TEXT ), _presentsLoc.valueX(), _presentsLoc.valueY(), 0, WHITE.toARGB() );
		
		// draw kacheout logo
		drawObjectAtLoc( p.meshPool.getMesh( p.KACHEOUT_LOGO ), _kacheOutLoc.valueX(), _kacheOutLoc.valueY(), 0, WHITE.toARGB() );
		
		// draw mode set logo & text
		drawObjectAtLoc( p.meshPool.getMesh( p.MODE_SET_LOGO ), _modeSetLogoLoc.valueX(), _modeSetLogoLoc.valueY(), _modeSetLogoZ.val(), MODE_SET_BLUE.toARGB() );
		drawObjectAtLoc( p.meshPool.getMesh( p.MODE_SET_LOGOTYPE ), _modeSetTextLoc.valueX(), _modeSetTextLoc.valueY(), 0, MODE_SET_GREY.toARGB() );
		
		// draw cacheflowe logo & text
		drawObjectAtLoc( p.meshPool.getMesh( p.CACHEFLOWE_LOGO ), _cacheFloweLogoLoc.valueX(), _cacheFloweLogoLoc.valueY(), _cacheFloweLogoLoc.valueZ(), CACHEFLOWE_YELLOW.toARGB() );
		drawObjectAtLoc( p.meshPool.getMesh( p.CACHEFLOWE_LOGOTYPE), _cacheFloweTextLoc.valueX(), _cacheFloweTextLoc.valueY(), _cacheFloweTextLoc.valueZ(), CACHEFLOWE_YELLOW.toARGB() );
		
		// draw design credits
		drawObjectAtLoc( p.meshPool.getMesh( p.DESIGN_BY ), _designByLoc.valueX(), _designByLoc.valueY(), _designByLoc.valueZ(), WHITE.toARGB() );
		drawObjectAtLoc( p.meshPool.getMesh( p.JON_DESIGN), _designJonLoc.valueX(), _designJonLoc.valueY(), _designJonLoc.valueZ(), WHITE.toARGB() );
		drawObjectAtLoc( p.meshPool.getMesh( p.RYAN_DESIGN), _designRyanLoc.valueX(), _designRyanLoc.valueY(), _designRyanLoc.valueZ(), WHITE.toARGB() );
		
		
		
		// reset
		p.popMatrix();
	}
	
	protected void drawObjectAtLoc( WETriangleMesh mesh, float x, float y, float z, int color ) {
		p.pushMatrix();
		p.translate( x, y, z );
		p.fill( color );
		p.noStroke();
		p.toxi.mesh( mesh );
		p.popMatrix();	

	}
}
