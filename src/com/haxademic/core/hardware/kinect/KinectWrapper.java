package com.haxademic.core.hardware.kinect;

import org.openkinect.processing.Kinect;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Vec3D;

import SimpleOpenNI.SimpleOpenNI;

import com.haxademic.app.P;

public class KinectWrapper {
	
	protected PApplet p;
	protected SimpleOpenNI _kinect;
	protected boolean _kinectActive = true;

	protected int _hardwareTilt = 0;
	public static int KWIDTH = 640;
	public static int KHEIGHT = 480;
//	public static int THRESHOLD_LOW = 1;
	
//	protected PVector _loc; 		// Raw location
//	protected PVector _lerpedLoc;	// Interpolated location
	public int[] _depthArray;
	public float[] _depthLookUp = new float[2048];
	public PVector[] _realWorldMap;


	public KinectWrapper( PApplet p, boolean initDepth, boolean initRGB, boolean initDepthImage ) {
		this.p = p;

		_kinect = new SimpleOpenNI(p);
		_kinect.setMirror(false);
		_kinect.enableDepth();
		_kinect.enableIR();
		_kinect.enableRGB();
				
		// enable depthMap generation 
		if(_kinect.enableDepth() == false) {
			P.println("Can't open the depthMap, maybe the camera is not connected!"); 
			_kinectActive = false;
		}
		
		// Lookup table for all possible depth values (0 - 2047)
		for (int i = 0; i < _depthLookUp.length; i++) {
			_depthLookUp[i] = rawDepthToMeters(i);
		}		
//		_loc = new PVector(0, 0);
//		_lerpedLoc = new PVector(0, 0);
	}
	
	public void update() {
		// Get the raw depth as array of integers
		_kinect.update();
		_depthArray = _kinect.depthMap();
		_realWorldMap = _kinect.depthMapRealWorld();
	}
	
//	public void updateCenterOfRectangle() {
//		// Being overly cautious here
//		if (_depthArray == null) return;
//		
//		float sumX = 0;
//		float sumY = 0;
//		float count = 0;
//		int rawDepth;
//
//		for (int x = 0; x < KWIDTH; x++) {
//			for (int y = 0; y < KHEIGHT; y++) {
//				// Mirroring the image
//				int offset = KWIDTH - x - 1 + y * KWIDTH;
//				// Testing against threshold
//				rawDepth = _depthArray[offset];
//				if (rawDepth < THRESHOLD_LOW) {
//					sumX += x;
//					sumY += y;
//					count++;
//				}
//			}
//		}
//		// As long as we found something
//		if (count != 0) {
//			_loc = new PVector(sumX / count, sumY / count);
//		}
//	
//		// Interpolating the location, doing it arbitrarily for now
//		_lerpedLoc.x = PApplet.lerp(_lerpedLoc.x, _loc.x, 0.3f);
//		_lerpedLoc.y = PApplet.lerp(_lerpedLoc.y, _loc.y, 0.3f);
//	}
	
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
	

//	PVector getLerpedPos() {
//		return _lerpedLoc;
//	}
//	
//	PVector getPos() {
//		return _loc;
//	}
	
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
		int v;
		
		// Scale up by 200
		float scaleFactor = scale;
		
		p.noStroke();
		
		for (int x = left; x < right; x += pixelSkip) {
			for (int y = top; y < bottom; y += pixelSkip) {
				// Convert kinect data to world xyz coordinate
//				rawDepth = getDepthMetersForKinectPixel( x, y, mirrored );
//				depthMeters = rawDepthToMeters( rawDepth );
				v = getMillimetersDepthForKinectPixel(x, y);
				
//				P.println(v);
				// draw a point within the specified depth range
				if( v > depthClose && v < depthFar ) {
					p.fill( 255, alpha * 255f );
				} else {
					p.fill( 255, 0, 0, alpha * 255f );
				}
				p.pushMatrix();
				p.translate( x * scaleFactor, y * scaleFactor, scaleFactor * v );
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
			_kinect.delete();
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
	
	/**
	 * Internal calculations
	 * These functions come from:
	 * http://graphics.stanford.edu/~mdfisher/Kinect.html
	 * @param depthValue	The raw Kinect data
	 * @return	raw depth to meters
	 */
	public float rawDepthToMeters(int depthValue) {
		if(depthValue < 2047) {
			return (float) (1.0 / ((double) (depthValue) * -0.0030711016 + 3.3309495161));
		}
		return 0.0f;
	}

	final double fx_d = 1.0 / 5.9421434211923247e+02;
	final double fy_d = 1.0 / 5.9104053696870778e+02;
	final double cx_d = 3.3930780975300314e+02;
	final double cy_d = 2.4273913761751615e+02;

	public PVector depthToWorld(int x, int y, int depthValue) {
		PVector result = new PVector();
		if(depthValue >= 2048) depthValue = 2047;
		double depth = _depthLookUp[depthValue];// rawDepthToMeters(depthValue);
		result.x = (float) ((x - cx_d) * depth * fx_d);
		result.y = (float) ((y - cy_d) * depth * fy_d);
		result.z = (float) (depth);
		return result;
	}
	
	public Vec3D depthToWorldToxi(int x, int y, int depthValue, int zMultiplier) {
		Vec3D result = new Vec3D();
		double depth = _depthLookUp[depthValue];// rawDepthToMeters(depthValue);
		result.x = (float) ((x - cx_d) * depth * fx_d);
		result.y = (float) ((y - cy_d) * depth * fy_d);
		result.z = (float) (depth) * zMultiplier;
		return result;
	}


}
