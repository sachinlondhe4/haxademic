package com.haxademic.sketch.hardware;

import org.openkinect.processing.Kinect;

import processing.core.PApplet;
import processing.core.PFont;

public class KinectTest2 extends PApplet {
	Kinect kinect;
	boolean depth = true;
	boolean rgb = false;
	boolean ir = false;

	float deg = 15; // Start at 15 degrees

	public void setup() {
		size(1280, 520);
		kinect = new Kinect(this);
		kinect.start();
		kinect.enableDepth(depth);
		kinect.enableRGB(rgb);
		kinect.enableIR(ir);
		kinect.tilt(deg);
	}

	/**
	 * Auto-initialization of the root class.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "--hide-stop",
				"--bgcolor=000000", "com.haxademic.sketch.kinect.KinectTest" });
	}

	public void draw() {
		background(0);

		image(kinect.getVideoImage(), 0, 0);
		image(kinect.getDepthImage(), 640, 0);
		fill(255);
		// text("RGB/IR FPS: " + (int) kinect.getVideoFPS() + " Camera tilt: " +
		// (int)deg + " degrees",10,495);
		// text("DEPTH FPS: " + (int) kinect.getDepthFPS(),640,495);
		// text("Press 'd' to enable/disable depth Press 'r' to enable/disable rgb image Press 'i' to enable/disable IR image UP and DOWN to tilt camera Framerate: "
		// + frameRate,10,515);
	}

	public void keyPressed() {
		if (key == 'd') {
			depth = !depth;
			kinect.enableDepth(depth);
		} else if (key == 'r') {
			rgb = !rgb;
			if (rgb)
				ir = false;
			kinect.enableRGB(rgb);
		} else if (key == 'i') {
			ir = !ir;
			if (ir)
				rgb = false;
			kinect.enableIR(ir);
		} else if (key == CODED) {
			if (keyCode == UP) {
				deg++;
			} else if (keyCode == DOWN) {
				deg--;
			}
			deg = constrain(deg, 0, 30);
			kinect.tilt(deg);
		}
	}

	public void stop() {
		kinect.quit();
		super.stop();
	}

}
