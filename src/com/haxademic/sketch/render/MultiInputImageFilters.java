package com.haxademic.sketch.render;

import java.awt.image.BufferedImage;

import processing.core.PConstants;
import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.app.PAppletHax;
import com.haxademic.core.hardware.webcam.WebCamWrapper;
import com.haxademic.core.render.VideoFrameGrabber;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.ImageUtil;
import com.haxademic.viz.filters.BlobOuterMeshFilter;
import com.haxademic.viz.filters.PixelFilter;
import com.haxademic.viz.filters.PixelTriFilter;
import com.haxademic.viz.filters.ReflectionFilter;
import com.jhlabs.image.BumpFilter;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.HSBAdjustFilter;
import com.jhlabs.image.RaysFilter;

public class MultiInputImageFilters
extends PAppletHax  
{	
	protected int inputType;
	protected final int WEBCAM = 0;
	protected final int VIDEO = 1;
	protected final int IMAGE = 2;
	
	protected PImage _loadedImg;
	protected PImage _curFrame;
	protected VideoFrameGrabber _frameGrabber;
	protected BlobOuterMeshFilter _blobFilter;
	protected ReflectionFilter _reflectionFilter;
	protected PixelTriFilter _pixelTriFilter;
	protected PixelFilter _pixelFilter;
		
	public void setup() {
		super.setup();
		initRender();
	}
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "rendering", "false" );
		_appConfig.setProperty( "fps", "30" );
		_appConfig.setProperty( "width", "640" );
		_appConfig.setProperty( "height", "480" );
	}

	// INITIALIZE OBJECTS ===================================================================================
	public void initRender() {
		inputType = WEBCAM;
		int w = 320;
		int h = 240;
		
		_blobFilter = new BlobOuterMeshFilter( w, h );
		_reflectionFilter = new ReflectionFilter( w, h );
		_pixelFilter = new PixelFilter( w, h, 10 );
		_pixelTriFilter = new PixelTriFilter( w, h, 12 );
		_blobFilter = new BlobOuterMeshFilter( w, h );
		
		switch( inputType ) {
			case WEBCAM :
				WebCamWrapper.initWebCam( p, w, h );
				break;
			case VIDEO :
				_frameGrabber = new VideoFrameGrabber( p, "../data/video/CacheFlowe_at_Rhinoceropolis_June_2011.mov", 30 );
				break;
			case IMAGE :
				_loadedImg = p.loadImage("../data/images/maya-04.png");
				break;
		}
	}
		
	// FRAME LOOP RENDERING ===================================================================================
	public void drawApp() {
		p.background(0);
		p.fill( 255 );
		p.noStroke();
		p.rectMode( PConstants.CENTER );
		DrawUtil.setBasicLights( p );
		
		// draw current frame and image filter
		DrawUtil.setColorForPImage(this);
		DrawUtil.setPImageAlpha(this, 1.0f);
		
		// capture source image
		switch( inputType ) {
			case WEBCAM :
				_curFrame = WebCamWrapper.getImage();
				break;
			case VIDEO :
				_frameGrabber.seekAndUpdateFrame( p.frameCount );
				_curFrame = _frameGrabber.curFrame();
				break;
			case IMAGE :
				_curFrame = _loadedImg;
				break;
		}
		
		// draw source and processed/filtered images
		applyImageFilters();
		applyPostFilters();
		p.image( _curFrame, 0, 0, 640, 480 );
	}
	
	protected void applyImageFilters() {
		_curFrame = _pixelFilter.updateWithPImage( _curFrame );
//		_curFrame = _blobFilter.updateWithPImage( _pixelFilter.updateWithPImage( _curFrame ) );
//		_curFrame = _pixelTriFilter.updateWithPImage( _reflectionFilter.updateWithPImage( _curFrame ) );
//		_curFrame = _blobFilter.updateWithPImage( _pixelFilter.updateWithPImage( _reflectionFilter.updateWithPImage( _curFrame ) ) );
	}
	
	protected void applyPostFilters() {
		// create native java image
		BufferedImage buff = ImageUtil.pImageToBuffered( _curFrame );
		
		// contrast
		ContrastFilter filt = new ContrastFilter();
		filt.setBrightness(0.8f);
		filt.setContrast(1.5f);
		filt.filter(buff, buff);
		
		// hsb adjust
		HSBAdjustFilter hsb = new HSBAdjustFilter();
		hsb.setHFactor(P.sin(p.frameCount/100f));
		hsb.setSFactor(0.2f);
		hsb.setBFactor(0.2f);
		hsb.filter(buff, buff);
		
		// glow
		GlowFilter glow = new GlowFilter();
		glow.setRadius(20f);
		glow.filter(buff, buff);
		
		// bump
		BumpFilter bump = new BumpFilter();
		bump.filter(buff, buff);
		
		// edge
//		EdgeFilter edge = new EdgeFilter();
//		edge.filter(buff, buff);
		
		// motion blur
//		MotionBlurFilter blur = new MotionBlurFilter();
//		blur.setAngle(P.TWO_PI/16f);
//		blur.setDistance(30f);
//		blur.filter(buff, buff);
		
		// ray
		RaysFilter ray = new RaysFilter();
		ray.setAngle(P.TWO_PI/8f);
		ray.setDistance(60f);
		ray.filter(buff, buff);
		
		// kaleidoscope
//		KaleidoscopeFilter kaleida = new KaleidoscopeFilter();
//		kaleida.setSides(8);
//		kaleida.filter(buff, buff);
		
		// contrast again
		filt.filter(buff, buff);

		
		// save processed image back to _curFrame
		_curFrame = ImageUtil.bufferedToPImage( buff );
	}
	
	protected void drawSourceFrame() {
		p.pushMatrix();
		p.translate(0,0,-5);
		p.image(_curFrame,0,0,_curFrame.width,_curFrame.height);
		p.popMatrix();
	}
	
}
