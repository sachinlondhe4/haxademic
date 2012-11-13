
package com.haxademic.sketch.text;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

public class Text2DTTF
extends PApplet {

	PFont ff;
	PImage ww;
	String sentence;
	int fontSize = 40;
	PGraphics pg;
	PImage w;
	
	public void setup()
	{
		size(800, 600, P3D);
		ff = createFont("../data/fonts/HelloDenverDisplay-Regular.ttf",fontSize);	//"Arial"
		sentence = "HELLO. WHAT'S UP?";
		pg = createGraphics(400,50,JAVA2D);
	}

	public void draw() {
		background(255);
		lights();
		translate(mouseX, height/2, 0); 
		crImage();
		image(pg, 0, 0);
	}

	PImage crImage() {
		
		pg.beginDraw();
		pg.background(255);
		pg.fill(250,0,0);
		pg.textAlign(CENTER);
		pg.textFont(ff, fontSize);
		pg.text(sentence, 0, 0, 400, 50);
		pg.endDraw();
		return w;
	}

}
