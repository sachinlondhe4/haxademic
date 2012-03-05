package com.cacheart.three_d_shapes;

import krister.Ess.AudioInput;
import processing.core.PApplet;
import processing.core.PConstants;
import toxi.color.TColor;
import toxi.geom.mesh.WETriangleMesh;

import com.p5core.audio.AudioInputWrapper;
import com.p5core.draw.shapes.Meshes;
import com.p5core.draw.util.DrawMesh;

public class Invaders
	extends PApplet
{
	protected WETriangleMesh _invaderMesh_01, _invaderMesh_01_alt;
	protected AudioInputWrapper _audioInput;
	protected TColor _stroke, _fill;

	public void setup () {
		// set up stage and drawing properties
//		size( 800, 800, "hipstersinc.P5Sunflow" );
		size( 800, 800, PConstants.OPENGL );				//size(screen.width,screen.height,P3D);
		frameRate( 30 );
		colorMode( PConstants.RGB, 255, 255, 255, 255 );
		background( 0 );
		shininess(1000); 
		lights();
		
		
		
		
		_invaderMesh_01 = Meshes.invader1( 1 );
		_invaderMesh_01_alt = Meshes.invader1( 2 );
		_invaderMesh_01.scale( 70 );
		_invaderMesh_01_alt.scale( 70 );
		
		// set up audio input
		_audioInput = new AudioInputWrapper( this, false );
		_audioInput.setNumAverages( 3 );
		_audioInput.setDampening(.4f);
		
		_stroke = new TColor( TColor.WHITE );
		_fill = new TColor( TColor.WHITE );
	}

	public void draw() {
		if(frameCount < 2) return;
		background( 0 );
		
		translate( width/2, height/2, 0 );
		rotateX(this.PI/8f);
		rotateY(0.5f + frameCount/100f);
		
		WETriangleMesh curMesh = ( this.round( this.frameCount / 30f ) % 2 == 0 ) ? _invaderMesh_01 : _invaderMesh_01_alt;
		DrawMesh.drawMeshWithAudio( (PApplet)this, curMesh, _audioInput, 3f, false, _fill, _stroke, 0.25f );
	}
	
	// PApp-level listener for audio input data ------------------------ 
	public void audioInputData( AudioInput theInput ) {
		_audioInput.getFFT().getSpectrum(theInput);
		_audioInput.detector.detect(theInput);
	}

}
