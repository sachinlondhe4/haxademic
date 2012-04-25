package com.haxademic.core.draw.mesh;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import saito.objloader.OBJModel;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;

public class MeshUtil {
	
	public static WETriangleMesh meshFromOBJ( PApplet p, String file, float scale ) {
		// load and scale the .obj file. convert to mesh and pass back 
		OBJModel obj = new OBJModel( p, file, OBJModel.RELATIVE );
		obj.scale( scale );
		WETriangleMesh mesh = MeshUtil.ConvertObjModelToToxiMesh( p, obj );
		return mesh;
	}
	
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


	public static WETriangleMesh meshFromSVG( String file, float scale ) {
		return null;
	}
	public static WETriangleMesh meshFromTextFont( PFont font, float scale ) {
		return null;
	}
}
