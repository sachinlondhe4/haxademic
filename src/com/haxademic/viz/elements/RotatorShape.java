package com.haxademic.viz.elements;

import processing.core.PApplet;
import toxi.color.TColor;
import toxi.geom.Line3D;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

import com.haxademic.viz.ElementBase;
import com.haxademic.viz.IVizElement;
import com.p5core.audio.AudioInputWrapper;
import com.p5core.draw.color.ColorRGBA;
import com.p5core.util.MathUtil;

public class RotatorShape 
extends ElementBase 
implements IVizElement {
	
	final int POINTS_MIN = 6;
	final int POINTS_MAX = 12;	// 2;//
	final int COORD_MIN = 0;
	final int COORD_MAX = 500;

	protected PointGroup _curPointGroup = null;
	protected int _numRotations;
	protected int _pointsPerGroup;
	protected TColor _baseColor;
	
	protected float _baseRotZAdd = 0;
	protected float _baseRotZTarget = 0;
	protected float _rotDir = 0;
	
	protected WETriangleMesh _mesh;
	
	public RotatorShape( PApplet p, ToxiclibsSupport toxi, AudioInputWrapper audioData, int numRotations ) {
		super( p, toxi, audioData );

		_numRotations = numRotations;//p.round( p.random( ROTATION_MIN, ROTATION_MAX ) );
		if( _numRotations % 2 != 0 ) _numRotations++;	// keep even numbers for proper reflection
				
		init();
	}
	
	public void init() {
		reset();
		_curPointGroup = new PointGroup( _pointsPerGroup );
		_curPointGroup.reset();
	}

	public void updateColor( TColor color ) {
		_baseColor = color;
	}

	public void update() {
		// rotate beginning z
		_baseRotZAdd = MathUtil.easeTo( _baseRotZAdd, _baseRotZTarget, 20 );
		p.rotateZ( _rotDir * p.frameCount - _baseRotZAdd );

		_curPointGroup.update();
		float rotationIncrement = p.TWO_PI / _numRotations;
		_curPointGroup.createPointsTrianglesSmoothed();
		float spectrumData;
		int fillColor = _baseColor.toARGB();
		p.noStroke();
		for( int i = 0; i < _numRotations; i++ ) {
			spectrumData = _audioData.getFFT().spectrum[ 20 + (int) (i * (255f/_numRotations)) ];
			p.fill( fillColor, spectrumData * 255 * 3 );
//			p.stroke( spectrumData * 255, spectrumData * 127 );
			p.pushMatrix();
			p.rotateZ( rotationIncrement * i );
			if( _mesh != null ) toxi.mesh( _mesh );
			p.popMatrix();
		}
	}
	
	public void reset() {
		// come up with random numbers
		_pointsPerGroup = p.round( p.random( POINTS_MIN, POINTS_MAX ) );
		if( _curPointGroup != null ) _curPointGroup.reset();
	}
	
	public void updateLineMode() {
		
	}
	
	public void updateCamera() {
		_baseRotZAdd = ( MathUtil.randBinary( p ) ) ? 0.8f : -0.8f;
		_rotDir = p.random( -4, 4 )/ 1000;
	}


	
	public void dispose() {
		_curPointGroup.dispose();
		_baseColor = null;
	}
	
	public class Point {
		
		public Vec3D _pos;
		public Vec3D _posBase;
		public Vec3D _speed;
		public Vec3D _size;
		protected float _inc;
		protected float _incSpeed;
		protected float _radius;

		public Point(){
			_pos = new Vec3D(0,0,0);
			_posBase = new Vec3D(0,0,0);
			_speed = new Vec3D(0,0,0);
			_size = new Vec3D(0,0,0);
			reset();
		}
		
		public void reset() {
			// reset positions
			_pos.x = _posBase.x = p.round( p.random( -COORD_MAX, COORD_MAX ) );
			_pos.y = _posBase.y = p.round( p.random( -COORD_MAX, COORD_MAX ) );
			_pos.z = _posBase.z = p.round( p.random( -COORD_MAX, COORD_MAX ) );
			_size.y = p.random( 2, 5 );
			_size.x = p.random( 2, 5 );
			_size.z = p.random( 2, 5 );
			_inc = p.random( 0, 1f );
			_incSpeed = p.random( -0.005f, 0.005f );
			_radius = p.random( 500f, 800f );
		}
		
		public void update() {
			_inc += _incSpeed;
			_pos.x = _posBase.x + p.sin( _inc ) * _radius;
			_pos.y = _posBase.y + p.sin( _inc ) * _radius;
			_pos.z = _posBase.y + p.cos( _inc ) * _radius;
		}

		public void dispose() {
			_pos = null;
			_posBase = null;
			_speed = null;
			_size = null;
		}
	}	
	
	public class PointGroup {
		
		public Point[] _points;
//		protected AABB[] _boxes;
		public int _numPoints;

		public PointGroup( int numPoints ){
			_numPoints = numPoints;
			_points = new Point[ _numPoints ];
			for( int i = 0; i < _numPoints; i++ ) {
				_points[ i ] = new Point();
			}
			reset();
		}
		
		public void reset() {
			for( int i = 0; i < _numPoints; i++ ) {
				_points[ i ].reset();
			}
			// procedurally place the points
			for( int i = 1; i < _numPoints; i++ ) {
				_points[ i ]._pos.x = _points[ i ]._posBase.x = _points[ i - 1 ]._pos.x += (p.random(1) < 0.5f) ? 100 : -100;
				_points[ i ]._pos.y = _points[ i ]._posBase.y = _points[ i - 1 ]._pos.y += (p.random(1) < 0.5f) ? 100 : -100;
				_points[ i ]._pos.z = _points[ i ]._posBase.z = _points[ i - 1 ]._pos.z += (p.random(1) < 0.5f) ? 100 : -100;
			}
		}

		public void update() {
			for( int i = 0; i < _numPoints; i++ ) {
				_points[ i ].update();
			}
		}

		protected void drawPointsTriangles() {
			Line3D line;
			Triangle3D tri;
			for( int i = 1; i < _numPoints; i++ ) {
				// draw a line - currently disabled from noStroke()
				line = new Line3D( _points[ i - 1 ]._pos, _points[ i ]._pos ); 
				toxi.line( line );
				
				// from 3rd point on, start connecting triangles
				if( i >= 2 ) {
					tri = new Triangle3D( _points[ i - 2 ]._pos, _points[ i - 1 ]._pos, _points[ i ]._pos ); 
					toxi.triangle( tri );
				}
			}
		}

		protected void createPointsTrianglesSmoothed() {
			_mesh = new WETriangleMesh();
			for( int i = 1; i < _numPoints; i++ ) {
				// from 3rd point on, start connecting triangles
				if( i >= 2 ) {
					_mesh.addFace( _points[ i - 2 ]._pos, _points[ i - 1 ]._pos, _points[ i ]._pos );
				}
			}
			
//			SubdivisionStrategy subdiv = new MidpointDisplacementSubdivision( _mesh.computeCentroid(), -0.22f );
//			SubdivisionStrategy subdiv = new NormalDisplacementSubdivision(0.25f);
//			SubdivisionStrategy subdiv = new NormalDisplacementSubdivision(0.3f);
//			_mesh.subdivide( subdiv );
//			SubdivisionStrategy subdiv = new NormalDisplacementSubdivision(0.2f);
//			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.17f);
//			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.13f);
//			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.1f);
//			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.075f);
//			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.05f);
//			_mesh.subdivide( subdiv );
////			subdiv = new NormalDisplacementSubdivision(0.03f);
////			_mesh.subdivide( subdiv );
//			subdiv = new NormalDisplacementSubdivision(0.01f);
//			_mesh.subdivide( subdiv );
//			_mesh.subdivide( subdiv, 10 );
//			_mesh.subdivide( subdiv, 10 );
//			_mesh.subdivide( subdiv, 10 );
			
//			SubdivisionStrategy subdiv = new TriSubdivision();
//			_mesh.subdivide( subdiv );
//			_mesh.subdivide( subdiv );
////			_mesh.subdivide( subdiv );
////			_mesh.subdivide( subdiv );
//			ThreeDeeUtil.SmoothToxiMesh( p, _mesh, 10 );
		}

		protected void drawPointsTrianglesSmoothed() {
			if( toxi != null && _mesh != null ) toxi.mesh( _mesh );
		}

		public void dispose() {
			for( int i = 0; i < _numPoints; i++ ) {
				_points[ i ].dispose();
			}
			_points = null;
		}
	}	
}
