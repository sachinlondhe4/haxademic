package com.haxademic.sketch.three_d;

import java.util.ArrayList;

import toxi.color.TColor;
import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.geom.ZAxisCylinder;
import toxi.geom.mesh.TriangleMesh;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.color.TColorBlendBetween;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat3d;

@SuppressWarnings("serial")
public class Flocking3DAttractors 
extends PAppletHax {

	public ArrayList<MovingBox> boxes;
	public ArrayList<Attractor> attractors;

	public void setup() {
		super.setup();
		initBoxes();
	}
	
	protected void initBoxes() {
		attractors = new ArrayList<Attractor>();
		for( int i=0; i < 5; i++ ) {
			attractors.add( new Attractor() );
		}

		boxes = new ArrayList<MovingBox>();
		for( int i=0; i < 500; i++ ) {
			boxes.add( new MovingBox() );
		}
	}

	protected void overridePropsFile() {
		_appConfig.setProperty( "sunflow", "false" );
		_appConfig.setProperty( "rendering", "false" );
		_appConfig.setProperty( "width", "1000" );
		_appConfig.setProperty( "height", "750" );
		_appConfig.setProperty( "fills_screen", "false" );
	}

	public void drawApp() {
		setupScene();

		for( int i=0; i < attractors.size(); i++ ) {
			attractors.get(i).update();
		}

//		int count = ( p.frameCount < boxes.size() ) ? p.frameCount : boxes.size();
		int count = boxes.size();
		for( int i=0; i < count; i++ ) {
			boxes.get(i).update();
		}
		
//		if( p.frameCount == 700 ) p.exit();
	}
	
	public void mouseClicked() {
		initBoxes();
	}
	
	protected void setupScene() {
		DrawUtil.resetGlobalProps( p );
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
		p.smooth();

		p.translate( 0, 0, -700 );
		
		TColor fill = new TColor( TColor.WHITE ).setAlpha( 1.0f );
		TColor stroke = TColor.newRGB( 200, 200, 200 ).setAlpha( 0.3f );
		p.fill( fill.toARGB() );
		p.stroke( stroke.toARGB() );

//		p.rotateY( p.mouseX / 100f );
//		p.rotateZ( p.mouseY / 100f );
		p.rotateZ( p.frameCount / 70f );
	}
	
	public class Attractor {
		public Sphere box = new Sphere(30f);
		protected float xOscDivisor = p.random(10f,20f);
		protected float yOscDivisor = p.random(10f,20f);
		protected float zOscDivisor = p.random(10f,20f);
		protected TColor fillColor = TColor.newHex("00ff00");
		
		public Attractor() {
			
		}

		public void update() {
			// oscillate position
			box.set(
					P.sin(p.frameCount/xOscDivisor) * 200f,
					P.cos(p.frameCount/yOscDivisor) * 200f,
					P.cos(p.frameCount/zOscDivisor) * 500f
				);

			// draw attractor
			p.fill( fillColor.toARGB() );
			p.noStroke();
			p.toxi.mesh( box.toMesh(20) );
		}
	}
	
	public class MovingBox {

		protected Vec3D positionLast = new Vec3D();
		protected Vec3D position = new Vec3D();
		protected Vec3D vector = new Vec3D();
		protected Vec3D target;
		protected float accel = 1;
		protected float maxSpeed = 1;

		protected TriangleMesh mesh;
		protected float distToDest;
		protected TColorBlendBetween color;

		public MovingBox() {
			accel = p.random(0.5f, 2.0f);
			maxSpeed = p.random(8f, 25f);
			
			color = new TColorBlendBetween( TColor.newHex("dddddd"), TColor.newHex("00ff00") );
			float size = p.random(20f,50f);
			ZAxisCylinder cylinder = new ZAxisCylinder(new Vec3D(), size/8, size ); 
			mesh = (TriangleMesh)cylinder.toMesh();
//			mesh = MeshUtil.meshFromOBJ( p, "../data/models/pointer_cursor_2_hollow.obj", 0.005f * size );
//			mesh.rotateX(P.PI/2f);
		}

		public void update() {
			// color - if closer than threshold, ease towards saturated color
			p.noStroke();
			if( distToDest < 200 ) {
				p.fill(color.argbWithPercent(1f - distToDest/200f));
			} else {
				p.fill(color.argbWithPercent(0));
			}
			
			// make sure we're moving towards the closest attractor
			findClosestAttractor();
			
			// store last position for pointing towards heading
			positionLast.set(position);

			// always accelerate towards destination using basic xyz comparison & cap speed
			vector.x += ( position.x < target.x ) ? accel : -accel;
			vector.x = P.constrain(vector.x, -maxSpeed, maxSpeed);
			vector.y += ( position.y < target.y ) ? accel : -accel;
			vector.y = P.constrain(vector.y, -maxSpeed, maxSpeed);
			vector.z += ( position.z < target.z ) ? accel : -accel;
			vector.z = P.constrain(vector.z, -maxSpeed, maxSpeed);
			position.addSelf(vector);
						
			// point and position
			p.toxi.mesh( mesh.copy().pointTowards(positionLast.sub(position), Vec3D.Z_AXIS).translate(position) );
		}
		
		protected void findClosestAttractor() {
			// loop through attractors and store the closest & our distance for coloring
			float minDist = 999999;
			float distToAttractor;
			for(int i=0; i < attractors.size(); i++) {
				distToAttractor = position.distanceTo(attractors.get(i).box); 
				if( distToAttractor < minDist ) {
					minDist = distToAttractor;
					distToDest = minDist;
					target = attractors.get(i).box;
				}
			}
		}
	}


	public class MovingBox2 {
		public Vec3D position = new Vec3D(0,0,0);
		public Vec3D vector = new Vec3D();
		public Vec3D vectorEase = new Vec3D();
		public TriangleMesh mesh;
		public ZAxisCylinder cylinder;
		public float speed = 0;
		public EasingFloat3d vectorEasing;
		protected Vec3D target;

		public MovingBox2() {
			vectorEasing = new EasingFloat3d(0, 0, 0, 20);
			speed = p.random(0.01f, 0.02f);
			float size = p.random(10,40);
			cylinder = new ZAxisCylinder(new Vec3D(), size/4, size ); 
			mesh = (TriangleMesh)cylinder.toMesh();
		}

		public void update() {
			p.noStroke();
			p.fill(255);
			
			findClosestAttractor();
			
			// eases towards destination
//			position.interpolateTo(focus, 0.9f); 
			vectorEase.x = P.lerp( vectorEase.x, target.x, speed );
			vectorEase.y = P.lerp( vectorEase.y, target.y, speed );
			vectorEase.z = P.lerp( vectorEase.z, target.z, speed );

			vector = vectorEase.sub(position); 
			position.addSelf(vector);
			
			p.toxi.mesh( mesh.copy().pointTowards(target.sub(position), Vec3D.Z_AXIS).translate(position) );
		}

		protected void findClosestAttractor() {
			// loop through attractors and store the closest & our distance for coloring
			float minDist = 999999;
			float distToAttractor;
			for(int i=0; i < attractors.size(); i++) {
				distToAttractor = position.distanceTo(attractors.get(i).box); 
				if( distToAttractor < minDist ) {
					minDist = distToAttractor;
					target = attractors.get(i).box;
				}
			}
		}
	}
}
