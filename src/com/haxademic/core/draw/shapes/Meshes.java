package com.haxademic.core.draw.shapes;

import java.util.ArrayList;

import toxi.geom.AABB;
import toxi.geom.mesh.WETriangleMesh;


public class Meshes {
	
	public static ArrayList<AABB> invader1Boxes( int state, float scale ) {
		ArrayList<AABB> boxes = new ArrayList<AABB>();

		// add blocks by row
		createBoxAtCoordinate( -1, -4,  0, scale, boxes );
		createBoxAtCoordinate(  1, -4,  0, scale, boxes );

		createBoxAtCoordinate( -2, -3,  0, scale, boxes );
		createBoxAtCoordinate(  1, -3,  0, scale, boxes );
		createBoxAtCoordinate( -1, -3,  0, scale, boxes );
		createBoxAtCoordinate(  2, -3,  0, scale, boxes );

		createBoxAtCoordinate( -3, -2,  0, scale, boxes );
		createBoxAtCoordinate( -2, -2,  0, scale, boxes );
		createBoxAtCoordinate( -1, -2,  0, scale, boxes );
		createBoxAtCoordinate(  1, -2,  0, scale, boxes );
		createBoxAtCoordinate(  2, -2,  0, scale, boxes );
		createBoxAtCoordinate(  3, -2,  0, scale, boxes );

		createBoxAtCoordinate( -4, -1,  0, scale, boxes );
		createBoxAtCoordinate( -3, -1,  0, scale, boxes );
		createBoxAtCoordinate( -1, -1,  0, scale, boxes );
		createBoxAtCoordinate(  1, -1,  0, scale, boxes );
		createBoxAtCoordinate(  3, -1,  0, scale, boxes );
		createBoxAtCoordinate(  4, -1,  0, scale, boxes );

		createBoxAtCoordinate( -4,  1,  0, scale, boxes );
		createBoxAtCoordinate( -3,  1,  0, scale, boxes );
		createBoxAtCoordinate( -2,  1,  0, scale, boxes );
		createBoxAtCoordinate( -1,  1,  0, scale, boxes );
		createBoxAtCoordinate(  1,  1,  0, scale, boxes );
		createBoxAtCoordinate(  2,  1,  0, scale, boxes );
		createBoxAtCoordinate(  3,  1,  0, scale, boxes );
		createBoxAtCoordinate(  4,  1,  0, scale, boxes );
		
		if( state == 1 ) {
			createBoxAtCoordinate( -2,  2,  0, scale, boxes );
			createBoxAtCoordinate(  2,  2,  0, scale, boxes );

			createBoxAtCoordinate( -3,  3,  0, scale, boxes );
			createBoxAtCoordinate( -1,  3,  0, scale, boxes );
			createBoxAtCoordinate(  1,  3,  0, scale, boxes );
			createBoxAtCoordinate(  3,  3,  0, scale, boxes );

			createBoxAtCoordinate( -4,  4,  0, scale, boxes );
			createBoxAtCoordinate( -2,  4,  0, scale, boxes );
			createBoxAtCoordinate(  2,  4,  0, scale, boxes );
			createBoxAtCoordinate(  4,  4,  0, scale, boxes );
		} else {
			createBoxAtCoordinate( -3,  2,  0, scale, boxes );
			createBoxAtCoordinate( -1,  2,  0, scale, boxes );
			createBoxAtCoordinate(  1,  2,  0, scale, boxes );
			createBoxAtCoordinate(  3,  2,  0, scale, boxes );

			createBoxAtCoordinate( -4,  3,  0, scale, boxes );
			createBoxAtCoordinate(  4,  3,  0, scale, boxes );

			createBoxAtCoordinate( -3,  4,  0, scale, boxes );
			createBoxAtCoordinate(  3,  4,  0, scale, boxes );
		}
		
		return boxes;
	}
	
	public static WETriangleMesh invader1( int state, float scale ) {
		// setup / objects
		AABB boxMesh = new AABB( scale );
		WETriangleMesh mesh = new WETriangleMesh();
		
		// TODO: store this box array in a stativ var and check for it before creating again
		ArrayList<AABB> boxes = Meshes.invader1Boxes( state, scale );
		
		for( int i=0; i < boxes.size(); i++ ) {
			addBoxToMesh( boxes.get( i ), mesh );
		}

		return mesh;
	} 
	
	public static WETriangleMesh invader2( int state ) {
		// setup / objects
		float size = 0.5f;
		AABB boxMesh = new AABB( size );
		WETriangleMesh mesh = new WETriangleMesh();
		
		// add blocks by row
		addBoxAtCoordinateToMesh( -3, -4,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3, -4,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -2, -3,  0, boxMesh, mesh );
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

		addBoxAtCoordinateToMesh( -5,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -4,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  5,  1,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -3,  2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2,  2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1,  2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1,  2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2,  2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3,  2,  0, boxMesh, mesh );
	
		addBoxAtCoordinateToMesh( -3,  3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3,  3,  0, boxMesh, mesh );
		
		if( state == 1 ) {			
			addBoxAtCoordinateToMesh( -5, -3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5, -3,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -5, -2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5, -2,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -5, -1,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5, -1,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -4,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  2,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -4,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  4,  0, boxMesh, mesh );
		} else {
			addBoxAtCoordinateToMesh( -5,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5,  2,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -5,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5,  3,  0, boxMesh, mesh );
			
			addBoxAtCoordinateToMesh( -2,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  2,  4,  0, boxMesh, mesh );
		}
		
		return mesh;
	} 
	
	public static WETriangleMesh invader3( int state ) {
		// setup / objects
		float size = 0.5f;
		AABB boxMesh = new AABB( size );
		WETriangleMesh mesh = new WETriangleMesh();
		
		// add blocks by row
		addBoxAtCoordinateToMesh( -2, -4,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -4,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -4,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2, -4,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -5, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -4, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4, -3,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  5, -3,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -6, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -5, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -4, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  5, -2,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  6, -2,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -6, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -5, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -4, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  5, -1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  6, -1,  0, boxMesh, mesh );

		addBoxAtCoordinateToMesh( -6,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -5,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -4,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh( -1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  1,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  2,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  3,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  4,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  5,  1,  0, boxMesh, mesh );
		addBoxAtCoordinateToMesh(  6,  1,  0, boxMesh, mesh );
		
		if( state == 1 ) {
			addBoxAtCoordinateToMesh( -3,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -2,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  2,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  2,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -4,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -3,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  3,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -6,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -5,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  6,  4,  0, boxMesh, mesh );
		} else {
			addBoxAtCoordinateToMesh( -4,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -3,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -2,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  2,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  2,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  2,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -5,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -4,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  1,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  3,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  5,  3,  0, boxMesh, mesh );

			addBoxAtCoordinateToMesh( -4,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh( -3,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  3,  4,  0, boxMesh, mesh );
			addBoxAtCoordinateToMesh(  4,  4,  0, boxMesh, mesh );
		}

		return mesh;
	} 
	
	public static void createBoxAtCoordinate( float x, float y, float z, float size, ArrayList<AABB> boxArray ) {
		float boxRadius = size * 0.5f;
		AABB box = new AABB( boxRadius );
		x *= size;
		y *= size;
		z *= size;
		x = ( x < 0 ) ? x + boxRadius : x - boxRadius;
		y = ( y < 0 ) ? y + boxRadius : y - boxRadius;
		z = ( z < 0 ) ? z + boxRadius : z - boxRadius;
		box.set( x, y, z ); 
		boxArray.add( box );
	}
	
	public static void addBoxToMesh( AABB box, WETriangleMesh mesh ) {
		mesh.addMesh( box.toMesh() );
	}
	
	public static void addBoxAtCoordinateToMesh( float x, float y, float z, AABB box, WETriangleMesh mesh ) {
		x = ( x < 0 ) ? x + 0.5f : x - 0.5f;
		y = ( y < 0 ) ? y + 0.5f : y - 0.5f;
		z = ( z < 0 ) ? z + 0.5f : z - 0.5f;
		box.set( x, y, z ); 
		mesh.addMesh( box.toMesh() );
		
	}
}
