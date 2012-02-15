package com.p5core.hardware.kinect;

import org.openkinect.processing.Kinect;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Vec3D;

public class KinectWrapper {
	
	protected PApplet p;
	protected Kinect _kinect;
	protected boolean _kinectActive = true;

	protected int _hardwareTilt = 0;

	public static int KWIDTH = 640;
	public static int KHEIGHT = 480;

	PVector _loc; 		// Raw location
	PVector _lerpedLoc;	// Interpolated location

	public int[] _depthArray;
	public float[] _depthLookUp = new float[2048];

	public PImage _display;
	public PImage _img;
	
	public KinectWrapper(PApplet pApp) {
		p = pApp;

		init();
	}
	
	protected void init() {

		_kinect = new Kinect(p);
		_kinect.start();
		try {
			_kinect.enableRGB(true);
			_kinect.enableDepth(true);
			_kinect.processDepthImage(false);
		} catch (NullPointerException e) {
			_kinectActive = false;
			p.println("kinect not kinected");
		}
		// Lookup table for all possible depth values (0 - 2047)
		for (int i = 0; i < _depthLookUp.length; i++) {
			_depthLookUp[i] = rawDepthToMeters(i);
		}		
	
		_display = p.createImage(KWIDTH, KHEIGHT, PConstants.RGB);
	
		_loc = new PVector(0, 0);
		_lerpedLoc = new PVector(0, 0);

	}
	
	public void update() {
		track();
	}
	
	public PImage getDepthImage() {
		return _kinect.getDepthImage();
	}
	
	public PImage getVideoImage() {
		return _kinect.getVideoImage();
	}
	
	public int[] getDepthData() {
		return _depthArray;
	}
	
	public boolean getIsActive() {
		return _kinectActive;
	}
	
	public void tiltUp() {
		_hardwareTilt += 5;
		_hardwareTilt = p.constrain(_hardwareTilt, 0, 30);
		_kinect.tilt(_hardwareTilt);
	}
	
	public void tiltDown() {
		_hardwareTilt -= 5;
		_hardwareTilt = p.constrain(_hardwareTilt, 0, 30);
		_kinect.tilt(_hardwareTilt);
	}
	
	
	void track() {
	
		// Get the raw depth as array of integers
		_depthArray = _kinect.getRawDepth();
	
		// Being overly cautious here
		if (_depthArray == null)
			return;
	
		float sumX = 0;
		float sumY = 0;
		float count = 0;

		for (int x = 0; x < KWIDTH; x++) {
			for (int y = 0; y < KHEIGHT; y++) {
				// Mirroring the image
				int offset = KWIDTH - x - 1 + y * KWIDTH;
				// Grabbing the raw depth
				int rawDepth = _depthArray[offset];
	
//				// Testing against threshold
//				if (rawDepth < _thresholdLow) {
//					sumX += x;
//					sumY += y;
//					count++;
//				}
			}
		}
		// As long as we found something
		if (count != 0) {
			_loc = new PVector(sumX / count, sumY / count);
		}
	
		// Interpolating the location, doing it arbitrarily for now
		_lerpedLoc.x = PApplet.lerp(_lerpedLoc.x, _loc.x, 0.3f);
		_lerpedLoc.y = PApplet.lerp(_lerpedLoc.y, _loc.y, 0.3f);
	}
	
	PVector getLerpedPos() {
		return _lerpedLoc;
	}
	
	PVector getPos() {
		return _loc;
	}
		
	
	// These functions come from:
	// http://graphics.stanford.edu/~mdfisher/Kinect.html
	public float rawDepthToMeters(int depthValue) {
		if (depthValue < 2047) {
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


	public void stop() {
		if( _kinectActive ) _kinect.quit();
	}

}
