package com.haxademic.core.util;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

import com.haxademic.app.P;

public class OpenGLUtil {
	
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;
	
	public static void SetQuality( PApplet p, int quality ) {
//		p.hint(p.DISABLE_DEPTH_SORT);
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) p.g;
		GL gl = pgl.gl;
		switch ( quality ) {
			case MEDIUM :
				p.hint(P.ENABLE_OPENGL_2X_SMOOTH);
				gl.glHint (GL.GL_LINE_SMOOTH_HINT, GL.GL_FASTEST);
				gl.glHint (GL.GL_POINT_SMOOTH_HINT, GL.GL_FASTEST);
				gl.glHint (GL.GL_POLYGON_SMOOTH_HINT, GL.GL_FASTEST);
				break;
			case HIGH :
				p.hint(P.ENABLE_OPENGL_4X_SMOOTH);
				gl.glHint (GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
				gl.glHint (GL.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);
				gl.glHint (GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
				gl.glEnable (GL.GL_LINE_SMOOTH);
				break;
		}
	}
	
}
