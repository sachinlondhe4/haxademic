package com.p5core.util;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class OpenGLUtil {
	
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;
	
	public static void SetQuality( PApplet p, int quality ) {
//		p.hint(p.DISABLE_DEPTH_SORT);
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) p.g;
		GL gl = pgl.gl;
		switch ( quality ) {
			case MEDIUM :
				p.hint(p.ENABLE_OPENGL_2X_SMOOTH);
				gl.glHint (gl.GL_LINE_SMOOTH_HINT, gl.GL_FASTEST);
				break;
			case HIGH :
				p.hint(p.ENABLE_OPENGL_4X_SMOOTH);
				gl.glHint (gl.GL_LINE_SMOOTH_HINT, gl.GL_NICEST);
				gl.glEnable (gl.GL_LINE_SMOOTH);
				break;
		}
	}
	
}
