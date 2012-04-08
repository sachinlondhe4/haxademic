package com.haxademic.core.draw.util;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.OBJModel;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.TriangleMesh;
import toxi.geom.mesh.WETriangleMesh;
import codeanticode.glgraphics.GLGraphics;
import codeanticode.glgraphics.GLModel;

public class ThreeDeeUtil {

	public static WETriangleMesh ConvertObjModelToToxiMesh( PApplet p, OBJModel model ) {
		WETriangleMesh mesh = new WETriangleMesh();
		
		for( int i = 0; i < model.getFaceCount(); i++ ) {
			PVector[] facePoints = model.getFaceVertices( i );
			mesh.addFace( 
					new Vec3D( facePoints[0].x, facePoints[0].y, facePoints[0].z ), 
					new Vec3D( facePoints[1].x, facePoints[1].y, facePoints[1].z ), 
					new Vec3D( facePoints[2].x, facePoints[2].y, facePoints[2].z )
			);
		}	
		return mesh;
	}

	public static void SmoothToxiMesh( PApplet p, WETriangleMesh mesh, int numSmoothings ) {
		for( int i = 0; i < numSmoothings; i++ ) {
			new LaplacianSmooth().filter( mesh, 1 );
		}
	}
	
	public static WETriangleMesh GetWETriangleMeshFromTriangleMesh( TriangleMesh mesh ) {
		WETriangleMesh weMesh = new WETriangleMesh();
		weMesh.addMesh(mesh);
		return weMesh;
	}
	
	/**
	 * Returns a GLModel, suitable to load into a fragment shader
	 * From: http://codeanticode.wordpress.com/2011/03/28/integrating-toxilibs-and-glgraphics/
	 * @param p
	 * @param mesh
	 * @return
	 */
	public static GLModel GetGLModelFromToxiMesh( PApplet p, WETriangleMesh mesh ){
		mesh.computeVertexNormals();
		float[] verts = mesh.getMeshAsVertexArray();
		int numV = verts.length / 4; // The vertices array from the mesh object has a spacing of 4.
		float[] norms = mesh.getVertexNormalsAsArray();
		
		GLModel glmesh = new GLModel(p, numV, p.TRIANGLES, GLModel.STATIC);
		glmesh.beginUpdateVertices();
		for (int i = 0; i < numV; i++) glmesh.updateVertex(i, verts[4 * i], verts[4 * i + 1], verts[4 * i + 2]);
		glmesh.endUpdateVertices(); 
		
		glmesh.initNormals();
		glmesh.beginUpdateNormals();
		for (int i = 0; i < numV; i++) glmesh.updateNormal(i, norms[4 * i], norms[4 * i + 1], norms[4 * i + 2]);
		glmesh.endUpdateNormals();
		  
		return glmesh;
	}
	
	public static void setGLProps( GLGraphics renderer ) {
		renderer.gl.glEnable(GL.GL_LIGHTING);
		renderer.gl.glDisable(GL.GL_COLOR_MATERIAL);
		renderer.gl.glEnable(GL.GL_LIGHT0);
		renderer.gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, new float[]{0.1f,0.1f,0.1f,1}, 0);
		renderer.gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, new float[]{1,0,0,1}, 0);
		renderer.gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] {-1000, 600, 2000, 0 }, 0);
		renderer.gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, new float[] { 1, 1, 1, 1 }, 0);
	}
}
