package com.haxademic.core.draw.shapes;

import processing.core.PApplet;

public class Shapes {
	
	public static void drawDisc3D( PApplet p, float radius, float innerRadius, float cylinderHeight, int numSegments, int color, int wallcolor )
	{
		// draw triangles
		p.beginShape(p.TRIANGLES);
		
		float segmentCircumference = (2f*p.PI) / numSegments;
		float halfHeight = cylinderHeight / 2;

		for( int i = 0; i < numSegments; i++ )
		{
			p.fill( color );

			// top disc
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, halfHeight );
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius, halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, halfHeight );
			
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius, halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, halfHeight );
			
			// bottom disc
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, -halfHeight );
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius, -halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, -halfHeight );
			
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, -halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius, -halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, -halfHeight );
			
			p.fill( wallcolor );
			// outer wall
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius, halfHeight );
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius, -halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, halfHeight );
			
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius, -halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, halfHeight );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius, -halfHeight );
			
			// only draw inner radius if needed
			if( innerRadius > 0 )
			{
				p.fill(wallcolor);
				// inner wall
				p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, halfHeight );
				p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, -halfHeight );
				p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius, halfHeight );
				
				p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius, -halfHeight );
				p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius, halfHeight );
				p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius, -halfHeight );
			}
		}
		
		p.endShape();
	}

	public static void drawDisc( PApplet p, float radius, float innerRadius, int numSegments )
	{
		p.pushMatrix();

		// draw triangles
		p.beginShape(p.TRIANGLES);
		
		for( int i = 0; i < numSegments; i++ )
		{
			float segmentCircumference = (2f*p.PI) / numSegments;
			
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius );
			p.vertex( p.sin( i * segmentCircumference ) * radius, p.cos( i * segmentCircumference ) * radius );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius );
			
			p.vertex( p.sin( i * segmentCircumference ) * innerRadius, p.cos( i * segmentCircumference ) * innerRadius );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * innerRadius, p.cos( (i + 1) * segmentCircumference ) * innerRadius );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * radius, p.cos( (i + 1) * segmentCircumference ) * radius );
		}
		
		p.endShape();
		
		p.popMatrix();
	}
	
	public static void drawStar( PApplet p, float spikes, float outerrad, float innerradpercent, float h, float rot)
	{
		p.pushMatrix();

		int pi, pj;
		float futil;
		p.beginShape(p.TRIANGLE_STRIP);
		for(pi=0;pi<spikes+1;pi++)
		{
			p.vertex(0,0,h/2);
		    futil=(pi/spikes)  * p.TWO_PI;    //current angle 
		    p.vertex(  p.cos(futil+rot)*outerrad, p.sin(futil+rot)*outerrad, 0);
		    futil=futil+ (  (1/spikes)/2 *p.TWO_PI  );
		    p.vertex(  p.cos(futil+rot)*outerrad*innerradpercent, p.sin(futil+rot)*outerrad*innerradpercent, 0);
		}
		p.endShape();
		p.beginShape(p.TRIANGLE_STRIP);
		for(pi=0;pi<spikes+1;pi++)
		{
			p.vertex(0,0,-h/2);
			futil=(pi/spikes)  * p.TWO_PI;    //current angle 
			p.vertex(  p.cos(futil+rot)*outerrad, p.sin(futil+rot)*outerrad, 0);
			futil=futil+ (  (1/spikes)/2 *p.TWO_PI  );
			p.vertex(  p.cos(futil+rot)*outerrad*innerradpercent, p.sin(futil+rot)*outerrad*innerradpercent, 0);
		}
		p.endShape();
		
		p.popMatrix();
	}

	public static void drawPyramid( PApplet p, float shapeHeight, int baseWidth, boolean drawBase )
	{
		baseWidth *= p.HALF_PI;
		
		p.pushMatrix();
		p.rotateZ(p.radians(-45.0f));
		p.beginShape(p.TRIANGLES);
		
		int numSides = 4;
		float segmentCircumference = (2f*p.PI) / numSides;
		float halfBaseW = baseWidth / 2;

		for( int i = 0; i < numSides; i++ )
		{
			p.vertex( 0, 0, shapeHeight );
			p.vertex( p.sin( i * segmentCircumference ) * halfBaseW, p.cos( i * segmentCircumference ) * halfBaseW, 0 );
			p.vertex( p.sin( (i + 1) * segmentCircumference ) * halfBaseW, p.cos( (i + 1) * segmentCircumference ) * halfBaseW, 0 );
		}
		
		if( drawBase ) {
			// base
			p.vertex( p.sin( 0 * segmentCircumference ) * halfBaseW, p.cos( 0 * segmentCircumference ) * halfBaseW, 0 );
			p.vertex( p.sin( 1 * segmentCircumference ) * halfBaseW, p.cos( 1 * segmentCircumference ) * halfBaseW, 0 );
			p.vertex( p.sin( 2 * segmentCircumference ) * halfBaseW, p.cos( 2 * segmentCircumference ) * halfBaseW, 0 );
	
			p.vertex( p.sin( 2 * segmentCircumference ) * halfBaseW, p.cos( 2 * segmentCircumference ) * halfBaseW, 0 );
			p.vertex( p.sin( 3 * segmentCircumference ) * halfBaseW, p.cos( 3 * segmentCircumference ) * halfBaseW, 0 );
			p.vertex( p.sin( 0 * segmentCircumference ) * halfBaseW, p.cos( 0 * segmentCircumference ) * halfBaseW, 0 );
		}
		
		p.endShape();
		p.popMatrix();
	}
}
