package com.haxademic.sketch.gl_tests;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class Stencil
extends PApplet{
	
	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		// "--present", 
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "com.haxademic.sketch.shader_test.Stencil" });
	}

	GL gl;
	
	int YELLOWMAT = 1;
	int BLUEMAT = 2;

	float[] yellow_diffuse = { 0.7f, 0.7f, 0.0f, 1.0f };
	float[] yellow_specular = { 1.0f, 1.0f, 1.0f, 1.0f };

	float[] blue_diffuse = { 0.1f, 0.1f, 0.7f, 1.0f };
	float[] blue_specular = { 0.1f, 1.0f, 1.0f, 1.0f };

	float[] position_one = { 1.0f, 1.0f, 1.0f, 0.0f };

	public void setup() {
		size(640, 480, OPENGL);
		gl = ((PGraphicsOpenGL)g).gl;

//		frame.removeNotify(); 		  
//		frame.setBackground(new java.awt.Color(0,0,0));
//		frame.setUndecorated(true); 
	}
	public void init() {
		  frame.removeNotify(); 
		  frame.setUndecorated(true); 
		  // addNotify, here i am not sure if you have  
		  // to add notify again.   
		  frame.addNotify(); 
		  super.init();
	}
	
	public void draw() {
		

		background(0);
		translate( width/2, height/2 );
		rotateX(frameCount/100f);
		rotateY(frameCount/100f);

		gl.glNewList(YELLOWMAT, GL.GL_COMPILE);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, FloatBuffer.wrap( yellow_diffuse ));
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, FloatBuffer.wrap( yellow_specular ));
		gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 64.0f);
		gl.glEndList();

		gl.glNewList(BLUEMAT, GL.GL_COMPILE);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, FloatBuffer.wrap( blue_diffuse ));
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, FloatBuffer.wrap( blue_specular ));
		gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 45.0f);
		gl.glEndList();

		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, FloatBuffer.wrap( position_one ));

		  
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_DEPTH_TEST);
		

		gl.glClearStencil(0x0);
		gl.glEnable(GL.GL_STENCIL_TEST);

		
		
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;  // g may change
		GL gl = pgl.beginGL();  // always use the GL object returned by beginGL
		
		// Do some things with gl.xxx functions here.
		// For example, the program above is translated into:
		gl.glColor4f(0.7f, 0.7f, 0.7f, 0.8f);
		gl.glTranslatef(width/2, height/2, 0);
		gl.glRotatef(frameCount/10f, 1, 0, 0);
		gl.glRotatef(frameCount/10f*2, 0, 1, 0);
		gl.glRectf(-200, -200, 200, 200);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRectf(-200, -200, 200, 200);
		
		pgl.endGL();
		
		
//		pushMatrix();
//		fill(100);
//		box(200);
//		popMatrix();

	}
}
