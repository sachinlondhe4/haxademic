package com.cacheart.shader_test;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import saito.objloader.OBJModel;
import codeanticode.glgraphics.GLModel;

import com.p5core.draw.model.ObjPool;
import com.p5core.draw.util.ThreeDeeUtil;
import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.GLUT;

// from: http://stackoverflow.com/questions/1847940/how-to-change-a-glsl-shader-parameter-in-processing

public class ShaderTest 
extends PApplet {
	PGraphicsOpenGL pgl;
	GL gl;
	GLSL glsl;
	GLU glu;
	GLUT glut;
	boolean glInit;
	int glutSolidIndex = 7;
	GLModel glmesh;
	OBJModel objModel;

	public ShaderTest() {

	}
	public void setup()
	{
		size(600, 500, OPENGL);

		glu = new GLU();
		glut = new GLUT();

		pgl = (PGraphicsOpenGL) g;
		gl = pgl.gl;
		
		
		ObjPool _objPool = new ObjPool( this );
		_objPool.loadObj( "DISCOVERY", 900, "../data/models/pointer_cursor_2.obj" );
		glmesh = ThreeDeeUtil.GetGLModelFromToxiMesh( this, _objPool.getMesh( "DISCOVERY" ) );
		objModel = _objPool.getModel( "DISCOVERY" );
//		objModel.disableMaterial();
//		objModel.disableTexture();
		objModel.enableMaterial();
		objModel.setDrawMode( TRIANGLES );
		objModel.setupGL();
	}

	public void draw()
	{
		background(0);
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;
		GL gl = pgl.beginGL();

		if(!glInit){
			glsl=new GLSL();
			glsl.loadVertexShader("shaders/toon.vs");
			glsl.loadFragmentShader("shaders/toon.fs");
			glsl.useShaders();

			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LESS);
			gl.glShadeModel(GL.GL_SMOOTH);
			glInit = true;
			

		}

		gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

		//TRS
		gl.glTranslatef(width * .5f, height * .5f,0.0f);
		gl.glRotatef(160,1,0,0);
		gl.glRotatef(frameCount * .5f,0,1,0);
		gl.glRotatef(frameCount * .5f,0,0,1);
		gl.glScalef(80,80,80);
		// draw 
		glsl.startShader();
		gl.glColor3f(1.0f, 0.5f, 0.0f);
		gl.glFrontFace(gl.GL_CW);
		glsl.uniform3f(glsl.getUniformLocation("LightPosition"), 10.0f, 10.0f, 20.0f);
//		glutSolid();
		objModel.drawGL();
		gl.glFrontFace(gl.GL_CCW);
		glsl.endShader();

		
		
		pgl.endGL();
		
		
		// normal p5 drawing
		translate(width/2, height/2);
		rotateX(0.01f*frameCount);
		stroke(255);
		noFill();
		box(500);
		
		
		// custom object drawing
//		GLGraphics renderer = (GLGraphics)g;
//		renderer.beginGL();
//		ThreeDeeUtil.setGLProps( renderer );
//		renderer.model(glmesh);
//		renderer.endGL();

	}

	void glutSolid(){
		switch(glutSolidIndex){
			case 0:
				glut.glutSolidCube(1);
				break;
			case 1:
				glut.glutSolidTetrahedron();
				break;
			case 2:
				glut.glutSolidOctahedron();
				break;
			case 3:
				glut.glutSolidDodecahedron();
				break;
			case 4:
				glut.glutSolidIcosahedron();
				break;
			case 5:
				glut.glutSolidSphere(1,8,6);
				break;
			case 6:
				glut.glutSolidTorus(1,1.5,8,6);
				break;
			case 7:
				glut.glutSolidTeapot(1);
				break;
		}
	}
	public void keyPressed(){
		if((int)key >= 49 && (int)key <= 56) glutSolidIndex = (int)(key) - 49;
	}


	class GLSL
	{
		int programObject;
		GL gl;
		boolean vertexShaderEnabled;
		boolean vertexShaderSupported; 
		int vs;
		int fs;

		GLSL()
		{
			PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;
			gl = pgl.gl;
			//gl=((PGraphicsGL)g).gl;
			String extensions = gl.glGetString(GL.GL_EXTENSIONS);
			vertexShaderSupported = extensions.indexOf("GL_ARB_vertex_shader") != -1;
			vertexShaderEnabled = true;    
			programObject = gl.glCreateProgramObjectARB(); 
			vs=-1;
			fs=-1;
		}

		void loadVertexShader(String file)
		{
			String shaderSource=join(loadStrings(file),"\n");
			vs = gl.glCreateShaderObjectARB(GL.GL_VERTEX_SHADER_ARB);
			gl.glShaderSourceARB(vs, 1, new String[]{shaderSource},(int[]) null, 0);
			gl.glCompileShaderARB(vs);
			checkLogInfo(gl, vs);
			gl.glAttachObjectARB(programObject, vs); 
		}

		void loadFragmentShader(String file)
		{
			String shaderSource=join(loadStrings(file),"\n");
			fs = gl.glCreateShaderObjectARB(GL.GL_FRAGMENT_SHADER_ARB);
			gl.glShaderSourceARB(fs, 1, new String[]{shaderSource},(int[]) null, 0);
			gl.glCompileShaderARB(fs);
			checkLogInfo(gl, fs);
			gl.glAttachObjectARB(programObject, fs); 
		}

		int getAttribLocation(String name)
		{
			return(gl.glGetAttribLocationARB(programObject,name));
		}

		int getUniformLocation(String name)
		{
			return(gl.glGetUniformLocationARB(programObject,name));
		}

		void useShaders()
		{
			gl.glLinkProgramARB(programObject);
			gl.glValidateProgramARB(programObject);
			checkLogInfo(gl, programObject);
		}

		void startShader()
		{
			gl.glUseProgramObjectARB(programObject); 
		}

		void endShader()
		{
			gl.glUseProgramObjectARB(0); 
		}

		void checkLogInfo(GL gl, int obj)  
		{
			IntBuffer iVal = BufferUtil.newIntBuffer(1);
			gl.glGetObjectParameterivARB(obj, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

			int length = iVal.get();
			if (length <= 1)  
			{
				return;
			}
			ByteBuffer infoLog = BufferUtil.newByteBuffer(length);
			iVal.flip();
			gl.glGetInfoLogARB(obj, length, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			println("GLSL Validation >> " + new String(infoBytes));
		} 

		void uniform3f(int location, float v0, float v1, float v2)
		{
			gl.glUniform3fARB(location, v0, v1, v2);
		}
	}
	
	
}