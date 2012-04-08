package com.p5core.hardware.kinect;

import org.openkinect.processing.Kinect;

import processing.core.PApplet;
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
	public static int THRESHOLD_LOW = 1;
	
	protected PVector _loc; 		// Raw location
	protected PVector _lerpedLoc;	// Interpolated location
	public int[] _depthArray;
	public float[] _depthLookUp = new float[2048];

	public KinectWrapper( PApplet p, boolean initDepth, boolean initRGB, boolean initDepthImage ) {
		this.p = p;

		_kinect = new Kinect(p);
		_kinect.start();
		
		try {
			_kinect.enableRGB(initRGB);
			_kinect.enableDepth(initDepth);
			_kinect.processDepthImage(initDepthImage);
		} catch (NullPointerException e) {
			_kinectActive = false;
			p.println("kinect not kinected");
		}
		
		// Lookup table for all possible depth values (0 - 2047)
		for (int i = 0; i < _depthLookUp.length; i++) {
			_depthLookUp[i] = rawDepthToMeters(i);
		}		
		_loc = new PVector(0, 0);
		_lerpedLoc = new PVector(0, 0);
	}
	
	public void update() {
		// Get the raw depth as array of integers
		_depthArray = _kinect.getRawDepth();
	}
	
	public void updateCenterOfRectangle() {
		// Being overly cautious here
		if (_depthArray == null) return;
		
		float sumX = 0;
		float sumY = 0;
		float count = 0;
		int rawDepth;

		for (int x = 0; x < KWIDTH; x++) {
			for (int y = 0; y < KHEIGHT; y++) {
				// Mirroring the image
				int offset = KWIDTH - x - 1 + y * KWIDTH;
				// Testing against threshold
				rawDepth = _depthArray[offset];
				if (rawDepth < THRESHOLD_LOW) {
					sumX += x;
					sumY += y;
					count++;
				}
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
	
	public PImage getDepthImage() {
		return _kinect.getDepthImage();
	}
	
	public PImage getVideoImage() {
		return _kinect.getVideoImage();
	}
	
	public int[] getDepthData() {
		return _depthArray;
	}
	
	public void enableDepth( boolean enable ) {
		_kinect.enableDepth( enable );
	}
	
	public void enableRGB( boolean enable ) {
		_kinect.enableRGB( enable );
	}
	
	public void enableDepthImage( boolean enable ) {
		_kinect.processDepthImage( enable );
	}
	
	public boolean isActive() {
		return _kinectActive;
	}
	
	PVector getLerpedPos() {
		return _lerpedLoc;
	}
	
	PVector getPos() {
		return _loc;
	}
	
	public void tiltUp() {
		_hardwareTilt += 5;
		_hardwareTilt = p.constrain(_hardwareTilt, -20, 30);
		_kinect.tilt(_hardwareTilt);
	}
	
	public void tiltDown() {
		_hardwareTilt -= 5;
		_hardwareTilt = p.constrain(_hardwareTilt, -20, 30);
		_kinect.tilt(_hardwareTilt);
	}
	
	public void drawPointCloudForRect( PApplet p, boolean mirrored, float alpha, float depthClose, float depthFar, int top, int right, int bottom, int left ) {
		p.pushMatrix();
		// We're just going to calculate and draw every 4th pixel
		int skip = 4;

		// Translate and rotate
		PVector v;
		float depthMeters;
		int rawDepth = 0, 
			offset = 0;
		
		// Scale up by 200
		float scaleFactor = 200;
		float factorM = ( mirrored == true ) ? -scaleFactor : scaleFactor;
		
		p.noStroke();
		p.fill( 255, alpha * 255f );
		
		for (int x = 0; x < KWIDTH; x += skip) {
			for (int y = 0; y < KHEIGHT; y += skip) {
				if( x >= left && x <= right && y >= top && y <= bottom ) {
					offset = x + y * KWIDTH;
	
					// Convert kinect data to world xyz coordinate
					rawDepth = _depthArray[offset];
					depthMeters = rawDepthToMeters( rawDepth );
					v = depthToWorld(x, y, rawDepth);
	
					// draw a point within the specified depth range
					if( depthMeters > depthClose && depthMeters < depthFar ) {
						p.pushMatrix();
						p.translate( v.x * factorM, v.y * scaleFactor, scaleFactor - v.z * factorM/4f );
						// Draw a point
						p.point(0, 0);
						p.rect(0, 0, 2, 2);
						p.popMatrix();
					}
				}
			}
		}
		p.popMatrix();
	}
	
	/**
	 * Shuts down Kinect properly when the PApplet shuts down
	 */
	public void stop() {
		if( _kinectActive ) _kinect.quit();
	}
	
	public float getDepthMetersForKinectPixel( int x, int y, boolean mirrored ) {
		int xOffset = ( mirrored == true ) ? KinectWrapper.KWIDTH - 1 - x : x;
		return rawDepthToMeters( _depthArray[xOffset + y * KinectWrapper.KWIDTH] );

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
