package com.p5core.data;

import java.io.IOException;
import java.util.Properties;

import processing.core.PApplet;

/**
 * simple convenience wrapper object for the standard
 * Properties class to return pre-typed data
 */
public class P5Properties extends Properties {
	protected PApplet p;
	public P5Properties(PApplet p) {
		super();
		try {
			load(p.createInput("run.properties"));
		} catch(IOException e) {
			p.println("couldn't read config file...");
		}
	}
 
	public String getStringProperty(String id, String defState) {
		return getProperty(id,defState);
	}
 
	public boolean getBooleanProperty(String id, boolean defState) {
		return Boolean.parseBoolean(getProperty(id,""+defState));
	}
 
	public int getIntProperty(String id, int defVal) {
		return Integer.parseInt(getProperty(id,""+defVal));
	}
 
	public float getFloatProperty(String id, float defVal) {
		return new Float(getProperty(id,""+defVal)); 
  	}  
}