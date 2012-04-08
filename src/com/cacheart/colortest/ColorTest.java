package com.cacheart.colortest;

import processing.core.PApplet;
import processing.core.PConstants;
import colorLib.webServices.KulerTheme;

import com.haxademic.core.util.ColorUtil;

/**
 * 
 * @author justin
 *
 */
public class ColorTest
	extends PApplet
{

	KulerTheme[] kt;
	
	public void setup ()
	{
		// set up stage and drawing properties
		size( screen.width,screen.height, P3D );				//size(screen.width,screen.height,P3D);
		colorMode( PConstants.RGB, 1, 1, 1, 1 );
		background( 0 );
		smooth();
		noStroke();
		
		//kt = ColorUtil.getRecentColors(this);
		kt = ColorUtil.getColorsByTag(this, "bright");
	}

	public void draw() 
	{
		for (int i = 0; i < kt.length; i++) {
			kt[i].sortByHue();
			for (int j = 0; j < kt[i].totalSwatches(); j++) {
				println((int)kt[i].getColor(j));
				fill(kt[i].getColor(j));
				rect(j*90, i*20, 90, 20);
			}
		}

	}
	
	/**
	 * Key handling for rendering functions - stopping and saving an image
	 */
	public void keyPressed()
	{
		//if( key == 'p' ) _render.renderFrame();
	}  
	
	public void mouseClicked()
	{
	}
}
