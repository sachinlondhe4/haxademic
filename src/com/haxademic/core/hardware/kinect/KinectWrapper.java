package com.haxademic.core.hardware.kinect;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.core.app.P;

public class KinectWrapper {
	
	protected PApplet p;
	protected SimpleOpenNI _kinect;
	protected boolean _kinectActive = true;

	protected int _hardwareTilt = 0;
	public static int KWIDTH = 640;
	public static int KHEIGHT = 480;

	public int[] _depthArray;
	public PVector[] _realWorldMap;
	
	// The sensor has an angular field of view of 57� horizontally and 43� vertically, while the motorized pivot is capable of tilting the sensor up to 27� either up or down
	// http://en.wikipedia.org/wiki/Field_of_view

	public KinectWrapper( PApplet p, boolean initDepth, boolean initRGB, boolean initDepthImage ) {
		this.p = p;

		_kinect = new SimpleOpenNI( p, SimpleOpenNI.RUN_MODE_MULTI_THREADED );
		_kinect.enableDepth();
		_kinect.enableRGB();
//		_kinect.enableIR();	// IR doesn't like being enabled off the bat - it kills the RGB camera?!
		_kinect.setMirror(false);
				
		// enable depthMap generation 
		if(_kinect.enableDepth() == false) {
			P.println("Can't open the depthMap, maybe the camera is not connected!"); 
			_kinectActive = false;
		}
	}
	
	public void update() {
		// Get the raw depth as array of integers
		if( _kinectActive == true ) {
			_kinect.update();
			_depthArray = _kinect.depthMap();
			_realWorldMap = _kinect.depthMapRealWorld();
		}
	}
	
	public PImage getDepthImage() {
		return _kinect.depthImage();
	}
	
	public PImage getIRImage() {
		return _kinect.irImage();
	}
	
	public PImage getRgbImage() {
		return _kinect.rgbImage();
	}
	
	public int[] getDepthData() {
		return _depthArray;
	}
	
	public void enableDepth( boolean enable ) {
		if( _kinectActive == true && enable == true ) _kinect.enableDepth();
	}
	
	public void enableIR( boolean enable ) {
		if( _kinectActive == true && enable == true ) _kinect.enableIR();
	}
	
	public void enableRGB( boolean enable ) {
		if( _kinectActive == true && enable == true ) _kinect.enableRGB();
	}
	
	public boolean isActive() {
		return _kinectActive;
	}
	
	public SimpleOpenNI openni() {
		return _kinect;
	}
	
	public void setMirror( boolean mirrored ) {
		_kinect.setMirror( mirrored );
	}
	
	public boolean isMirrored() {
		return _kinect.mirror();
	}
	
	public void tiltUp() {
//		_hardwareTilt += 5;
//		_hardwareTilt = P.constrain(_hardwareTilt, -20, 30);
//		_kinect.tilt(_hardwareTilt);
	}
	
	public void tiltDown() {
//		_hardwareTilt -= 5;
//		_hardwareTilt = P.constrain(_hardwareTilt, -20, 30);
//		_kinect.tilt(_hardwareTilt);
	}
	
	public void drawPointCloudForRect( PApplet p, boolean mirrored, int pixelSkip, float alpha, float scale, float depthClose, float depthFar, int top, int right, int bottom, int left ) {
		p.pushMatrix();

		// Translate and rotate
		int curZ;
		
		// Scale up by 200
		float scaleFactor = scale;
		
		p.noStroke();
		
		for (int x = left; x < right; x += pixelSkip) {
			for (int y = top; y < bottom; y += pixelSkip) {
				curZ = getMillimetersDepthForKinectPixel(x, y);
				// draw a point within the specified depth range
				if( curZ > depthClose && curZ < depthFar ) {
					p.fill( 255, alpha * 255f );
				} else {
					p.fill( 255, 0, 0, alpha * 255f );
				}
				p.pushMatrix();
				p.translate( x * scaleFactor, y * scaleFactor, scaleFactor * curZ/40f );
				// Draw a point
				p.point(0, 0);
				p.rect(0, 0, 4, 4);
				p.popMatrix();
			}
		}
		p.popMatrix();
	}
	
	/**
	 * Shuts down Kinect properly when the PApplet shuts down
	 */
	public void stop() {
		if( _kinectActive ) {
			_kinect.close();
			_kinect.dispose();
		}
	}
	
	public PVector getRealWorldDepthForKinectPixel( int x, int y ) {
		int offset = x + y * KinectWrapper.KWIDTH;
		if( _depthArray[offset] == 0 || offset >= _realWorldMap.length ) {
			return null;
		} else {
			return _realWorldMap[offset];			
		}
	}
	
	public int getMillimetersDepthForKinectPixel( int x, int y ) {
		int offset = x + y * KinectWrapper.KWIDTH;
		if( offset >= _realWorldMap.length ) {
			return 0;
		} else {
			return _depthArray[offset];			
		}
	}
}
