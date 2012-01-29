package com.p5core.util;

import processing.core.PApplet;

public class MathUtil {
	/**
	 * Return the eased value of a value towards a destination, with a given easing factor
	 * @param current		current value being eased towards target
	 * @param target		target value that current eases towards
	 * @param easingFactor	larger numbers mean slower easing and must be above 1 for easing over time
	 * @return				the eased value
	 */
	public static float easeTo( float current, float target, float easingFactor ) {
		return current -= ( ( current - target ) / easingFactor );
	}
	
	public static boolean randBinary( PApplet p ) {
		return ( p.round( p.random( 0, 1 ) ) == 1 ) ? true : false;
	}
}
