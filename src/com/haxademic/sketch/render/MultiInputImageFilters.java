package com.haxademic.sketch.render;

import processing.core.PConstants;
import processing.core.PImage;

import com.haxademic.app.PAppletHax;
import com.haxademic.core.hardware.webcam.WebCamWrapper;
import com.haxademic.core.render.VideoFrameGrabber;
import com.haxademic.core.util.DrawUtil;
import com.haxademic.core.util.ImageUtil;
import com.haxademic.viz.filters.BlobOuterMeshFilter;

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
	
		
	public void setup() {
		super.setup();
		initRender();
	}
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "rendering", "false" );
		_appConfig.setProperty( "fps", "30" );
		_appConfig.setProperty( "width", "1280" );
		_appConfig.setProperty( "height", "720" );
	}

	// INITIALIZE OBJECTS ===================================================================================
	public void initRender() {
		inputType = WEBCAM;
		
		switch( inputType ) {
			case WEBCAM :
				WebCamWrapper.initWebCam( p, 640, 480 );
				_blobFilter = new BlobOuterMeshFilter( 640, 480 );
				break;
			case VIDEO :
				_frameGrabber = new VideoFrameGrabber( p, "../data/video/CacheFlowe_at_Rhinoceropolis_June_2011.mov", 30 );
				_blobFilter = new BlobOuterMeshFilter( _frameGrabber.curFrame().width, _frameGrabber.curFrame().height );
				break;
			case IMAGE :
				_loadedImg = p.loadImage("../data/images/maya-04.png");
				_blobFilter = new BlobOuterMeshFilter( _loadedImg.width, _loadedImg.height );
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
				_curFrame = ImageUtil.getReversePImageFast( _curFrame );
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
		drawSourceFrame();
		p.image( _blobFilter.updateWithPImage( _curFrame ), 0, 0 );
	}
	
	protected void drawSourceFrame() {
		p.pushMatrix();
		p.translate(0,0,-5);
		p.image(_curFrame,0,0,_curFrame.width,_curFrame.height);
		p.popMatrix();
	}
	
}
