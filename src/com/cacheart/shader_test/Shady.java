package com.cacheart.shader_test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

import com.sun.opengl.util.BufferUtil;

public class Shady 
extends PApplet {
//		ToxiclibsSupport toxi;
	PApplet p;
	
	
	GLSL glsl;
	int coordsloc, WandHpos, c0loc, clrLUTloc, NC = 200;
	float[] clrdata;
	 
	float X0 = -2.5F, Y0 = -1.25F, X1 = 1.0F, Y1 = 1.25F;
	float tmpX0 = 0.0F, tmpY0 = 0.0F, tmpX1 = 0.0F, tmpY1 = 0.0F;
	float nX0 = 0.0F, nY0 = 0.0F, nX1 = 0.0F, nY1 = 0.0F;
	float XW, YW, curCoordPos = 0f, coordSpeed = 0.05f;
	boolean onceClicked = false, coordChanged = false;
	 
	public void setup() {
	  size(640, 480, OPENGL);
	  rectMode(CORNERS);
	 
	  glsl = new GLSL();
	  glsl.loadVertexShader("shaders/toon.vs");
	  glsl.loadFragmentShader("shaders/toon.fs");
	  glsl.useShaders();
	 
	  coordsloc = glsl.getAttribLocation("compBox");
	  c0loc = glsl.getUniformLocation("c0");
	  clrLUTloc = glsl.getUniformLocation("clrLUT");
	  WandHpos = glsl.getUniformLocation("WandH");
	 
	  preparePalette();
	 
	  XW = (X1 - X0) / (float) width;
	  YW = (Y1 - Y0) / (float) height;
	 
	  println("Press any key to reset.");
	 
	  // uniform properties of the shaders. setup just once.
	  glsl.startShader();
	  glsl.gl.glUniform4f(c0loc, 0.0F, 0.0F, 0.0F, 1.0F);
	  glsl.gl.glUniform4fv(clrLUTloc, NC, FloatBuffer.wrap(clrdata));
	  glsl.gl.glUniform2f(WandHpos, (float) width, (float) height);
	  glsl.endShader();
	  fill(255);
	  noStroke();
	}
	 
	public void draw() {
	  glsl.startShader();
	 
	  if (coordChanged) {
	    if (curCoordPos >= 1f) {
	      X0 = nX0;
	      Y0 = nY0;
	      X1 = nX1;
	      Y1 = nY1;
	      coordChanged = false;
	      glsl.gl.glVertexAttrib4f(coordsloc, X0, Y0, X1, Y1);
	    }
	    else {
	      // Parallel processing using GLSL is quite fast on my iMac (GeForce 120 GT) it gives almost 400fps (on Java 1.6)
	      // So we have plenty of time for interpolating the zoom-in zoom-out action.
	      // Cosine interpolation is used here.
	      float tx0, tx1, ty0, ty1;
	      double cosineInterpolate = 0.5 + 0.5 * Math.cos(curCoordPos * PI);
	      tx0 = (float) (nX0 + (X0 - nX0) * cosineInterpolate);
	      ty0 = (float) (nY0 + (Y0 - nY0) * cosineInterpolate);
	      tx1 = (float) (nX1 + (X1 - nX1) * cosineInterpolate);
	      ty1 = (float) (nY1 + (Y1 - nY1) * cosineInterpolate);
	      glsl.gl.glVertexAttrib4f(coordsloc, tx0, ty0, tx1, ty1);
	      curCoordPos += coordSpeed;
	    }
	  }
	  else
	    glsl.gl.glVertexAttrib4f(coordsloc, X0, Y0, X1, Y1);
	 
	  quad(0, 0, 0, height, width, height, width, 0);
	  glsl.endShader();
	 
	  if (onceClicked) {
	    tmpX1 = (float) mouseX;
	    // (int) may be confusing, but OpenGl renders sharp lines if the co-ordinates are like x.5 etc. (through the midle of a pixel)
	    // I've added those 0.5 in the 'rect' function call below. So we need a whole number here.
	    tmpY1 =  (float) tmpY0 + (int)((mouseX - tmpX0) * 0.75F);
	 
	    pushStyle();
	    noFill();
	    stroke(200);
	    rect(tmpX0 - 0.5f, tmpY0 - 0.5f, tmpX1 + 0.5f, tmpY1 + 0.5f);
	    popStyle();
	  }
	}
	 
	public void mousePressed() {
	  if (!onceClicked && !coordChanged) {
	    onceClicked = true;
	    tmpX0 = (float) mouseX;
	    tmpY0 = (float) mouseY;
	  }
	}
	 
	public void mouseReleased() {
	  if (onceClicked) {
	    onceClicked = false;
	    float tmpa, tmpb;
	    tmpa = X0 + tmpX0 * XW;
	    tmpb = Y0 + tmpY0 * YW;
	    nX1 = X0 + (float) mouseX * XW;
	    nY1 = Y0 + (tmpY0 + (mouseX - tmpX0) * 0.75F) * YW;
	    //          Y1 = Y0 + (float) mouseY * YW;
	    nX0 = tmpa;
	    nY0 = tmpb;
	 
	    curCoordPos = 0f;
	    coordChanged = true; // Animate zooming-in
	 
	    XW = (nX1 - nX0) / (float) width;
	    YW = (nY1 - nY0) / (float) height;
	  }
	}
	 
	public void keyPressed() {
	  onceClicked = false;
	  nX0 = -2.5F;
	  nY0 = -1.25F;
	  nX1 = 1F;
	  nY1 = 1.25F;
	  curCoordPos = 0f;
	  coordChanged = true; // Animate zooming-in
	 
	  XW = (nX1 - nX0) / (float) width;
	  YW = (nY1 - nY0) / (float) height;
	}
	 
	// color palette
	void preparePalette() {
	  int NC4 = NC / 4, NC2 = NC / 2, NC34 = NC4 + NC2;
	  clrdata = new float[4 * NC];
	  float r1, r2, g1, g2, b1, b2, rr, gg, bb;
	  double scoeff = HALF_PI / NC4;
	 
	  r1 = 1f;
	  g1 = .9f;
	  b1 = .9f;
	  r2 = .6f;
	  g2 = 0f;
	  b2 = 0f;
	  rr = r2 - r1;
	  gg = g2 - g1;
	  bb = b2 - b1;
	  for (int i = 0; i < NC4; i++) {
	    clrdata[i * 4] = (float) (r1 + rr * Math.sin(i * scoeff));
	    clrdata[i * 4 + 1] = (float) (g1 + gg * Math.sin(i * scoeff));
	    clrdata[i * 4 + 2] = (float) (b1 + bb * Math.sin(i * scoeff));
	    clrdata[i * 4 + 3] = 1f;
	  }
	  r1 = r2;
	  g1 = g2;
	  b1 = b2;
	  r2 = .8f;
	  g2 = .95f;
	  b2 = .15f;
	  rr = r2 - r1;
	  gg = g2 - g1;
	  bb = b2 - b1;
	  for (int i = NC4; i < NC2; i++) {
	    clrdata[i * 4] = (float) (r1 + rr * Math.sin((i - NC4) * scoeff));
	    clrdata[i * 4 + 1] = (float) (g1 + gg * Math.sin((i - NC4) * scoeff));
	    clrdata[i * 4 + 2] = (float) (b1 + bb * Math.sin((i - NC4) * scoeff));
	    clrdata[i * 4 + 3] = 1f;
	  }
	  r1 = r2;
	  g1 = g2;
	  b1 = b2;
	  r2 = 1f;
	  g2 = .09f;
	  b2 = .57f;
	  rr = r2 - r1;
	  gg = g2 - g1;
	  bb = b2 - b1;
	  for (int i = NC2; i < NC34; i++) {
	    clrdata[i * 4] = (float) (r1 + rr * Math.sin((i - NC2) * scoeff));
	    clrdata[i * 4 + 1] = (float) (g1 + gg * Math.sin((i - NC2) * scoeff));
	    clrdata[i * 4 + 2] = (float) (b1 + bb * Math.sin((i - NC2) * scoeff));
	    clrdata[i * 4 + 3] = 1f;
	  }
	  r1 = r2;
	  g1 = g2;
	  b1 = b2;
	  r2 = 0f;
	  g2 = .7f;
	  b2 = 1f;
	  rr = r2 - r1;
	  gg = g2 - g1;
	  bb = b2 - b1;
	  for (int i = NC34; i < NC; i++) {
	    clrdata[i * 4] = (float) (r1 + rr * Math.sin((i - NC34) * scoeff));
	    clrdata[i * 4 + 1] = (float) (g1 + gg * Math.sin((i - NC34) * scoeff));
	    clrdata[i * 4 + 2] = (float) (b1 + bb * Math.sin((i - NC34) * scoeff));
	    clrdata[i * 4 + 3] = 1f;
	  }
	}
	
	
	/**
	 * Type - GLSL
	 *
	 *
	 *
	 */
	class GLSL {
	    int programObject;
	    GL gl;
	    boolean vertexShaderEnabled;
	    boolean vertexShaderSupported;
	    int vs;
	    int fs;
	     
	    GLSL() {
	        gl = ((PGraphicsOpenGL) g).gl;
	        String extensions = gl.glGetString(GL.GL_EXTENSIONS);
	        vertexShaderSupported = extensions.indexOf("GL_ARB_vertex_shader") != -1;
	        vertexShaderEnabled = true;
	        programObject = gl.glCreateProgramObjectARB();
	        vs = -1;
	        fs = -1;
	    }
	     
	    void loadVertexShader(String file) {
	        String shaderSource = join(loadStrings(file), "\n");
	        vs = gl.glCreateShaderObjectARB(GL.GL_VERTEX_SHADER_ARB);
	        gl.glShaderSourceARB(vs, 1, new String[] { shaderSource }, (int[]) null, 0);
	        gl.glCompileShaderARB(vs);
	        checkLogInfo(gl, vs);
	        gl.glAttachObjectARB(programObject, vs);
	    }
	     
	    void loadFragmentShader(String file) {
	        String shaderSource = join(loadStrings(file), "\n");
	        fs = gl.glCreateShaderObjectARB(GL.GL_FRAGMENT_SHADER_ARB);
	        gl.glShaderSourceARB(fs, 1, new String[] { shaderSource }, (int[]) null, 0);
	        gl.glCompileShaderARB(fs);
	        checkLogInfo(gl, fs);
	        gl.glAttachObjectARB(programObject, fs);
	    }
	     
	    int getAttribLocation(String name) {
	        return (gl.glGetAttribLocationARB(programObject, name));
	    }
	     
	    int getUniformLocation(String name) {
	        return (gl.glGetUniformLocationARB(programObject, name));
	    }
	     
	    void useShaders() {
	        gl.glLinkProgramARB(programObject);
	        gl.glValidateProgramARB(programObject);
	        checkLogInfo(gl, programObject);
	    }
	     
	    void startShader() {
	        gl.glUseProgramObjectARB(programObject);
	    }
	     
	    void endShader() {
	        gl.glUseProgramObjectARB(0);
	    }
	     
	    void checkLogInfo(GL gl, int obj) {
	        IntBuffer iVal = BufferUtil.newIntBuffer(1);
	        gl.glGetObjectParameterivARB(obj, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);
	         
	        int length = iVal.get();
	        if (length <= 1) {
	            return;
	        }
	        ByteBuffer infoLog = BufferUtil.newByteBuffer(length);
	        iVal.flip();
	        gl.glGetInfoLogARB(obj, length, iVal, infoLog);
	        byte[] infoBytes = new byte[length];
	        infoLog.get(infoBytes);
	        PApplet.println("GLSL Validation >> " + new String(infoBytes));
	    }
	}
}
