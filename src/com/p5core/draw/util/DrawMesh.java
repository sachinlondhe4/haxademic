package com.p5core.draw.util;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.WETriangleMesh;

import com.p5core.audio.AudioInputWrapper;

public class DrawMesh {
	public static Matrix4x4 normalMap = new Matrix4x4().translateSelf(128,128,128).scaleSelf(127);

	public static void drawMeshWithAudio( PApplet p, WETriangleMesh mesh, AudioInputWrapper audioData, float spectrumFaceRatio, boolean isWireframe, TColor fillColor, TColor strokeColor, float baseAlpha ) {
		p.beginShape(PConstants.TRIANGLES);
		int faceIndex = 0;
		int color = fillColor.toARGB();
		int colorStroke = strokeColor.toARGB();
		Face f;
		float alpha;
		Vec3D n;
		baseAlpha = baseAlpha * 255;
		for (Iterator i = mesh.faces.iterator(); i.hasNext();) {
			// set colors
			alpha = audioData.getFFT().spectrum[(int)(faceIndex/spectrumFaceRatio) % 512] * 1.3f;
			if( isWireframe ) {
				p.noFill();
				p.stroke( colorStroke, baseAlpha + alpha * 255 );
			} else {
				p.noStroke();
				p.fill( color, baseAlpha + alpha * 255 );
			}
			
			f = (Face) i.next();
			
			n = normalMap.applyTo(f.a.normal);
			p.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
			p.vertex(f.a.x, f.a.y, f.a.z);
			n = normalMap.applyTo(f.b.normal);
			p.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
			p.vertex(f.b.x, f.b.y, f.b.z);
			n = normalMap.applyTo(f.c.normal);
			p.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
			p.vertex(f.c.x, f.c.y, f.c.z);
			
//			p.box( color, baseAlpha + alpha * 255 );
			
			faceIndex ++;
		}
		p.endShape();
	}
	
	public static void drawPointsWithAudio( PApplet p, WETriangleMesh mesh, AudioInputWrapper audioData, float spectrumFaceRatio, float pointSize, TColor fillColor, TColor strokeColor, float baseAlpha ) {
		p.rectMode( p.CENTER );
		int faceIndex = 0;
		int color = fillColor.toARGB();
		Face f;
		float alpha;
		Vec3D n;
		baseAlpha = baseAlpha * 255;
		p.noStroke();
		for (Iterator i = mesh.faces.iterator(); i.hasNext();) {
			if( faceIndex % 2 == 0 ) {
				// set colors
				alpha = audioData.getFFT().spectrum[(int)(faceIndex/spectrumFaceRatio) % 512] * 1.3f;
				p.fill( color, baseAlpha + alpha * 255 );
				
				p.pushMatrix();
				f = (Face) i.next();
				Vec3D center = f.getCentroid();
				
				p.translate( center.x, center.y, center.z );
				p.rotateX( f.normal.x );
				p.rotateY( f.normal.y );
				p.rotateZ( f.normal.z );
				p.rect( 0, 0, pointSize + pointSize * alpha, pointSize + pointSize * alpha );
				p.popMatrix();
				
			}
			faceIndex ++;
		}
	}

}
