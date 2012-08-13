package com.haxademic.sketch.three_d;


/**
 * stereo viewing taken from http://wiki.processing.org/index.php?title=Stereo_viewing
 * @author John Gilbertson
 */

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
public class GLStereo extends PApplet {

	GL gl;
	float rotation;

	public void setup() 
	{
		size(500,300,OPENGL);
		gl=((PGraphicsOpenGL)g).gl; //We need this to set some OPENGL options
		perspective(PI/3.0f,1f,0.1f,1000f); //this is needed ot stop the images being squashed
		noStroke();
		rotation=0;
	}

	public void draw()
	{
		rotation+=0.01; //you can vary the speed of rotation by altering this value
		ambientLight(64,64,64); //some lights to aid the effect
		pointLight(128,128,128,0,20,-50);

		background(0);
		fill(255);

		// Left Eye
		// Using Camera gives us much more control over the eye position
		camera(-10,0,-100,0,0,0,0,-1,0);
		// This means anything we draw will be limited to the left half of the screen 
		gl.glViewport(50,50,200,200); 
		pushMatrix();
		rotateX(rotation);
		rotateY(rotation/2.3f);
		box(30);
		translate(0,0,30);
		box(10);
		popMatrix();

		// Right Eye
		camera(10,0,-100,0,0,0,0,-1,0);
		gl.glViewport(250,50,200,200);
		pushMatrix();
		rotateX(rotation);
		rotateY(rotation/2.3f);
		box(30);
		translate(0,0,30);
		box(10);
		popMatrix();
	}
}
