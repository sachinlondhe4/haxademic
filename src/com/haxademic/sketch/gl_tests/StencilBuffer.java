package com.haxademic.sketch.gl_tests;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLCapabilities;

import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

public class StencilBuffer
extends PApplet {
	// BaubleGL acd 2012

	final static float MAX_SPEED = 0.05f;
	final static float MAX = 512f;
	final static float TEX = 512f;

	PImage tex1, tex2, tex3;

	float rx, ry, rz;
	float dx, dy, dz;

	public void setup() {
		size(640, 480, OPENGL);
		rx = random(TWO_PI);
		ry = random(TWO_PI);
		rz = random(TWO_PI);
		dx = (float)TWO_PI / 480f; //random(-MAX_SPEED, MAX_SPEED);
		dy = -(float)TWO_PI / 240f; //random(-MAX_SPEED, MAX_SPEED);
		dz = (float)TWO_PI / 120f; //random(-MAX_SPEED, MAX_SPEED);

		tex1 = loadImage("../data/images/stencil/pattern-01-bw.jpg");
		tex2 = loadImage("../data/images/stencil/pattern-02-bw.jpg");
		tex3 = loadImage("../data/images/stencil/pattern-03-bw.jpg");
		noStroke();

		// debug?
		GLCapabilities capabilities = new GLCapabilities();
		int bits = capabilities.getStencilBits();
		println("StencilBits1: " + bits);
		capabilities.setStencilBits(8);
		bits = capabilities.getStencilBits();
		println("StencilBits2: " + bits);
	}

	public void draw() {
		fill(255, 0, 0);

		float s = 150f;

		camera(0f, 0f, 1000f, 0f, 0f, 0f, 0f, 1f, 0f);

		rx += dx;
		ry += dy;
		rz += dz;

		pushMatrix();

		GL gl = ((PGraphicsOpenGL)g).gl;
		gl.glClearStencil(0x0); /* IMPORTANT */
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT | GL.GL_STENCIL | GL.GL_DEPTH_BUFFER_BIT);
		gl.glClearStencil(0);
//		gl.glEnable(GL.GL_DEPTH_TEST);
//		gl.glClear( 0 );
//		gl.glDisable(GL.GL_STENCIL_TEST);
//		gl.glColorMask( true, true, true, false );
		
//		gl.glColorMask(GL.GL_FALSE, GL.GL_FALSE, GL.GL_FALSE, GL.GL_FALSE);
//		gl.glDepthMask(GL.GL_FALSE);
//		gl.glEnable(GL.GL_STENCIL_TEST);

		rotateX(rx);
		rotateY(ry);
		rotateZ(rz);

		IntBuffer i = IntBuffer.allocate(100);
		gl.glGetIntegerv(GL.GL_STENCIL_BITS, i);

		background(255);
		pushMatrix();

		stencilOn(gl, 1);
		box(s, 3 * s, 5 * s);

//		rotateX(HALF_PI);
//		pushMatrix();
//		rotateY(HALF_PI);
//		fill(0, 255, 0);
//		stencilOn(gl, 2);
//		box(s, 3 * s, 5 * s);
//		popMatrix();
//
//		rotateZ(HALF_PI);
//		fill(0, 0, 255);
//		stencilOn(gl, 4);
//		box(s, 3 * s, 5 * s);
		popMatrix();

		popMatrix();

		// now draw stripes
		gl.glDisable(GL.GL_DEPTH_TEST);
		stencilOff(gl, 1);
		stripes(tex1);
//		stencilOff(gl, 2);
//		stripes(tex2);
//		stencilOff(gl, 4);
//		stripes(tex3);
		
		println(frameCount+","+rx);
	}

	//void keyPressed() {
	// saveFrame("baubleGL####.png");
	// delay(5);
	//}

	// draw to stencil buffer
	void stencilOn(GL gl, int plane) {
		gl.glEnable(GL.GL_STENCIL_TEST);
		gl.glStencilFunc(GL.GL_ALWAYS, plane, 0xffffffff); /* IMPORTANT */ // func, ref, mask
		gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE); // sfail, dpfail, dppass
	}

	// now draw to screen proper
	void stencilOff(GL gl, int plane) {
		gl.glColorMask(true, false, false, true); // re-enable colours
		gl.glStencilFunc(GL.GL_EQUAL, plane, 0xffffffff); // mask /* IMPORTANT */
		gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP); // don't change buffer
	}

	void stripes(PImage tex) {
		beginShape();
		texture(tex);
		textureMode(NORMALIZED);
		vertex(MAX, MAX, 0f, 0f);
		vertex(MAX, -MAX, 0f, 1f);
		vertex(-MAX, -MAX, 1f, 1f);
		vertex(-MAX, MAX, 1f, 0f);
		endShape();
	}
}
