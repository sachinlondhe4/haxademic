package com.p5core.util;
import processing.core.PApplet;
import colorLib.webServices.Kuler;
import colorLib.webServices.KulerTheme;

public class ColorUtil {
	
	public static String KULER_API_KEY = "072CCA4BC700A87DD621590E0512DAD4";
/*

 * kt = k.getRecent();
 * kt = k.getPopular();
 * kt = k.getHighestRated();
 * kt = k.getRandom();

 * kt = k.search("tag", "rainbow");
  Get all palettes tagged with the word ‘rainbow’.
  
 * kt = k.search("title", "forest");
  Get all palettes with the word ‘forest’ in the title
  
 * kt = k.search("hex", "32948a");
  Get all palettes which contain the hexadecimal color ‘32948a’.
  	
 */
	public static KulerTheme[] getRecentColors(PApplet p) 
	{
		Kuler kuler = new Kuler(p);
		kuler.setKey(ColorUtil.KULER_API_KEY);
		kuler.setMaxItems(20);
		return kuler.getRecent(); //get the 0 - 100 
	}

	public static KulerTheme[] getPopularColors(PApplet p) 
	{
		Kuler kuler = new Kuler(p);
		kuler.setKey(ColorUtil.KULER_API_KEY);
		kuler.setMaxItems(20);
		return kuler.getPopular(); //get the 0 - 100 
	}

	public static KulerTheme[] getColorsByTag(PApplet p, String tag) 
	{
		Kuler kuler = new Kuler(p);
		kuler.setKey(ColorUtil.KULER_API_KEY);
		kuler.setMaxItems(20);
		return kuler.search("tag", tag); 
	}
}

