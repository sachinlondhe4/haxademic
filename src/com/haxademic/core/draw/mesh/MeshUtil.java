package com.haxademic.core.draw.mesh;

import geomerative.RCommand;
import geomerative.RFont;
import geomerative.RG;
import geomerative.RGroup;
import geomerative.RMesh;
import geomerative.RPoint;
import geomerative.RSVG;
import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.OBJModel;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.WETriangleMesh;

public class MeshUtil {
	
	public static WETriangleMesh meshFromOBJ( PApplet p, String file, float scale ) {
		// load and scale the .obj file. convert to mesh and pass back 
		OBJModel obj = new OBJModel( p, file, OBJModel.RELATIVE );
		WETriangleMesh mesh = MeshUtil.ConvertObjModelToToxiMesh( p, obj );
		mesh.scale( scale );
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


	public static WETriangleMesh meshFromSVG( PApplet p, String file, float scale ) {
		if( RG.initialized() == false ) RG.init( p );
		
		// TODO: make this configurable?
		RCommand.setSegmentLength(8);
		RCommand.setSegmentator(RCommand.UNIFORMLENGTH);

		// build from font path or a passed-in RFont
		RSVG rSvg = new RSVG();
		RGroup grp = rSvg.toGroup( file );
		grp.centerIn( p.g );  // this centers the shape in the screen
		
		RPoint[] pnts = grp.getPoints();

		RMesh rMesh = grp.toMesh();
		WETriangleMesh mesh = rMeshToToxiMesh( rMesh );
		mesh.scale( scale );
		return mesh;
	}
	
	public static WETriangleMesh mesh2dFromTextFont( PApplet p, RFont font, String fontPath, int fontSize, String text, float scale ) {
		if( RG.initialized() == false ) RG.init( p );
		
		// TODO: make this configurable?
		RCommand.setSegmentLength(10);
		RCommand.setSegmentator(RCommand.UNIFORMLENGTH);

		// build from font path or a passed-in RFont
		if( font == null ) font = new RFont( fontPath, fontSize, RFont.CENTER);
		RGroup grp = font.toGroup( text );
		RPoint[] pnts = grp.getPoints();

		RMesh rMesh = grp.toMesh();
		WETriangleMesh mesh = rMeshToToxiMesh( rMesh );
		mesh.scale( scale );
		return mesh;
	}
	
	public static WETriangleMesh rMeshToToxiMesh( RMesh rMesh ) {
		WETriangleMesh mesh = new WETriangleMesh();

		// copy faces to toxi mesh
		for ( int i = 0; i < rMesh.strips.length; i++ ) {
			RPoint[] meshPoints = rMesh.strips[i].getPoints();

			for ( int ii = 0; ii < meshPoints.length - 2; ii++ ) {
				mesh.addFace( 
						new Vec3D( meshPoints[ii].x, meshPoints[ii].y, 0 ), 
						new Vec3D( meshPoints[ii+1].x, meshPoints[ii+1].y, 0 ), 
						new Vec3D( meshPoints[ii+2].x, meshPoints[ii+2].y, 0 ) 
				);
			}
		}
		return mesh;
	}
	
	public static WETriangleMesh getExtrudedMesh( WETriangleMesh mesh, float depth ) {
		WETriangleMesh mesh3d = new WETriangleMesh();
		
		Face face;
		for ( int i = 0; i < mesh.faces.size(); i++ ) {
			face = mesh.faces.get( i );
			
			// draw front/back faces
			mesh3d.addFace( 
					new Vec3D( face.a.x, face.a.y, depth ), 
					new Vec3D( face.b.x, face.b.y, depth ), 
					new Vec3D( face.c.x, face.c.y, depth ) 
			);
			mesh3d.addFace( 
					new Vec3D( face.a.x, face.a.y, -depth ), 
					new Vec3D( face.b.x, face.b.y, -depth ), 
					new Vec3D( face.c.x, face.c.y, -depth ) 
			);
			
			// draw walls - close the 2 triangles drawn above
			addTriQuadToMeshWith4Points( 
					mesh3d, 
					new Vec3D( face.a.x, face.a.y, depth ),
					new Vec3D( face.a.x, face.a.y, -depth ),
					new Vec3D( face.b.x, face.b.y, -depth ),
					new Vec3D( face.b.x, face.b.y, depth )
			);

			addTriQuadToMeshWith4Points( 
					mesh3d, 
					new Vec3D( face.b.x, face.b.y, depth ),
					new Vec3D( face.b.x, face.b.y, -depth ),
					new Vec3D( face.c.x, face.c.y, -depth ),
					new Vec3D( face.c.x, face.c.y, depth )
			);

			addTriQuadToMeshWith4Points( 
					mesh3d, 
					new Vec3D( face.c.x, face.c.y, depth ),
					new Vec3D( face.c.x, face.c.y, -depth ),
					new Vec3D( face.a.x, face.a.y, -depth ),
					new Vec3D( face.a.x, face.a.y, depth )
			);

		}
		return mesh3d;
	}
	
	public static void addTriQuadToMeshWith4Points( WETriangleMesh mesh, Vec3D pt1, Vec3D pt2, Vec3D pt3, Vec3D pt4 ) {
		// make sure 4 points go in order around the square
		mesh.addFace( pt1, pt2, pt3 );
		mesh.addFace( pt3, pt4, pt1 );
	}
}
