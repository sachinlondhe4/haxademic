package com.p5core.draw.shapes;

import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.OBJModel;
import toxi.geom.Vec3D;
import toxi.geom.mesh.LaplacianSmooth;
import toxi.geom.mesh.TriangleMesh;
import toxi.geom.mesh.WETriangleMesh;

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

}
