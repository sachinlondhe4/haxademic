package com.p5core.draw.shapes;

import toxi.geom.AABB;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.WETriangleMesh;


public class Meshes {
	
	public static WETriangleMesh invader1( int state ) {
		// setup / objects
		float size = 0.5f;
		AABB boxMesh = new AABB( size );
		WETriangleMesh mesh = new WETriangleMesh();
		
		// add blocks by row
		addBoxAtCoordinateToMesh( -1, -4,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -4,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -2, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2, -3,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -3, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3, -2,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -4, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4, -1,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -4,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4,  1,  0, boxMesh, mesh );
		
		if( state == 1 ) {
			addBoxAtCoordinateToMesh( -2,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  2,  2,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -3,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  3,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -4,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -2,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  2,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  4,  0, boxMesh, mesh );
		} else {
			addBoxAtCoordinateToMesh( -3,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -1,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  1,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  2,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -4,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  3,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -3,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  4,  0, boxMesh, mesh );
		}

		return mesh;
	} 
	
	public static void addBoxAtCoordinateToMesh( float x, float y, float z, AABB box, WETriangleMesh mesh ) {
		x = ( x < 0 ) ? x + 0.5f : x - 0.5f;
		y = ( y < 0 ) ? y + 0.5f : y - 0.5f;
		z = ( z < 0 ) ? z + 0.5f : z - 0.5f;
		box.set( x, y, z ); 
		mesh.addMesh( box.toMesh() );
		
	}
}
