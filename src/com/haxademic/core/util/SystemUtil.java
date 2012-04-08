package com.haxademic.core.util;

import processing.core.PApplet;

public class SystemUtil {
	
	public static String getTimestamp( PApplet p ) {
		return  String.valueOf( p.year() ) + "-" + 
				String.valueOf( p.month() ) + "-" + 
				String.valueOf( p.day() ) + "-" + 
				String.valueOf( p.hour() ) + "-" + 
				String.valueOf( p.minute() ) + "-" + 
				String.valueOf( p.second() );
	}
}
