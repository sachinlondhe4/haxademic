package com.haxademic.core.math;

import processing.core.PApplet;

import com.haxademic.core.app.P;

/**
 * A series of common, static math helper methods
 */
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

	public static boolean randBoolean( PApplet p ) {
		return ( p.random( 0f, 1f ) > 0.5f ) ? true : false;
	}

	/**
	 *	Calculates a random number within a minimum and maximum range.
	 *	@param	min		the value for the bottom range.
	 *	@param	max		the value for the upper range.
	 *	@return			the random number within the range.
	 * 	@use			{@code var vRandRange = MathUtil.randRange( 0, 999999 );}
	 */
	public static int randRange( float min, float max ) {
		return (int) ( Math.round( Math.random() * ( max - min ) ) + min );
	}

	/**
	 *  Calculates a random number within a minimum and maximum range.
	 *  @param  min   the value for the bottom range.
	 *  @param  max   the value for the upper range.
	 *  @return the random number within the range.
	 *  @use    {@code var vRandRange = MathUtil.randRange( 0, 999999 );}
	 */
	public static float randRangeDecimel( float min, float max ) {	
		return (float) Math.random() * ( max - min ) + min;
	}

	/**
	 *  Returns a percentage of a value in between 2 other numbers.
	 *  @param  bottomRange   low end of the range.
	 *  @param  topRange      top end of the range.
	 *  @param  valueInRange  value to find a range percentage of.
	 *  @return The percentage [0-1] of valueInRange in the range.
	 *  @use    {@code var vPercent = MathUtil.getPercentWithinRange( 50, 150, 100 );  // displays 50 }
	 */
	public static float getPercentWithinRange( float bottomRange, float topRange, float valueInRange ) {
		// normalize values to work positively from zero
		topRange += -bottomRange;
		valueInRange += -bottomRange;
		bottomRange += -bottomRange;  // last to not break other offsets
		// return percentage or normalized values 
		return ( valueInRange / ( topRange - bottomRange ) );
	}

	public static float pythagDistance( float a, float b ) {
		return Math.abs( (float)Math.sqrt(a*a + b*b) );
	}

	/**
	 *  Get distance between 2 points with the pythagorean theorem.
	 *  @param  x1  first point's x position
	 *  @param  y1  first point's y position
	 *  @param  x2  second point's x position
	 *  @param  y2  second point's y position
	 *  @return The distance between point 1 and 2
	 *  @use    {@code var distance = MathUtil.getDistance( 7, 5, 3, 2 );}
	 */
	public static float getDistance( float x1, float y1, float x2, float y2 ) {
		float a = x1 - x2;
		float b = y1 - y2;
		return Math.abs( (float) Math.sqrt(a*a + b*b) );
	};

	/**
	 *  Convert a number from Degrees to Radians.
	 *  @param  d degrees (45�, 90�)
	 *  @return radians (3.14..., 1.57...)
	 *  @use    {@code var vRadians = MathUtil.degreesToRadians( 180 );}
	 */

	public static float degreesToRadians( float d ) {
		return d * ( P.PI / 180f );
	}

	/**
	 *  Convert a number from Radians to Degrees.
	 *  @param  r radians (3.14..., 1.57...)
	 *  @return degrees (45�, 90�)
	 *  @use    {@code var vDegrees = MathUtil.radiansToDegrees( 3.14 );}
	 */

	public static float radiansToDegrees( float r ) {
		return r * ( 180f / P.PI );
	}

	/**
	 *  Convert a number from a Percentage to Degrees (based on 360�).
	 *  @param  n percentage (1, .5)
	 *  @return degrees (360�, 180�)
	 *  @use    {@code var vDegreesPercent = MathUtil.percentToDegrees( 50 );}
	 */

	public static float percentToDegrees( float n ) {
		return ( ( Math.abs( n / 100f ) ) * 360f ) * 100f;	
	}

	/**
	 *  Convert a number from Degrees to a Percentage (based on 360�).
	 *  @param  n degrees (360�, 180�)
	 *  @return percentage (1, .5)
	 *  @use    {@code var vPercentDegrees = MathUtil.degreesToPercent( 180 );}
	 */

	public static float degreesToPercent( float n ) {
		return ( Math.abs( n / 360f ) );
	}

	/**
	 *	Linear interpolate between two values.  
	 *	@param		lower	first value (-1.0, 43.6)
	 *	@param		upper	second value (-100.0, 3.1415)
	 *	@param		n	point between values (0.0, 1.0)
	 * 	@return 		number (12.3, 44.555)
	 * 	@use			{@code var value = MathUtil.interp( 10, 20, .5 );  //returns 15}
	 */
	public static float interp( float lower, float upper, float n ) {
		return ( ( upper - lower ) * n ) + lower;
	}

	/**   
	 *	Re-maps a number from one range to another. 
	 *	@param		value  The incoming value to be converted
	 *	@param		lower1 Lower bound of the value's current range
	 *	@param		upper1 Upper bound of the value's current range
	 *	@param		lower2 Lower bound of the value's target range
	 *	@param		upper2 Upper bound of the value's target range
	 * 	@return 	number (12.3, 44.555)
	 * 	@use		{@code var value = MathUtil.remap( 10, 0, 20, 1, 2 );  //returns 1.5}
	 */
	public static float remap( float value, float lower1, float upper1, float lower2, float upper2 ) {
		return interp( lower2, upper2, getPercentWithinRange( lower1, upper1, value ) / 100f );
	}


	/**
	 *  Keep an angle between 0-360
	 *  @param  angle the angle to constrain
	 *  @return The normalized angle
	 *  @use    {@code var angle = MathUtil.constrainAngle( 540 );}
	 */
	public static float constrainAngle( float angle ) {
		if( angle < 0f ) return angle + 360f;
		if( angle > 360f ) return angle - 360f;
		return angle;
	};

	/**
	 *  Get the angle fron current coordinate to target coordinate
	 *  @param  x1  first point's x position
	 *  @param  y1  first point's y position
	 *  @param  x2  second point's x position
	 *  @param  y2  second point's y position
	 *  @return The angle from point 1 and 2
	 *  @use    {@code var angle = MathUtil.getAngleToTarget( 0, 0, 5, 5 );}
	 */
	public static float getAngleToTarget( float x1, float y1, float x2, float y2 ) {
		return constrainAngle( (float) -Math.atan2( x1 - x2, y1 - y2 ) * 180f / P.PI );
	};

	/**
	 *  Figures out which way to rotate, for the shortest path from current to target angle
	 *  @param  curAngle    starting angle
	 *  @param  targetAngle destination angle
	 *  @return +1 for clockwise, -1 for counter-clockwise
	 *  @use    {@code var direction = MathUtil.rotationDirectionToTarget( 90, 180 );}
	 */
	public static float getRotationDirectionToTarget( float curAngle, float targetAngle ) {
		// calculate the difference between the current angle and destination angle
		float angleDifference = Math.abs( curAngle - targetAngle );
		// turn left or right to get to the target
		if( curAngle > targetAngle ){
			return (angleDifference < 180f) ? -1 : 1;
		} else {
			return (angleDifference < 180f) ? 1 : -1;
		}
	};


	public static float saw( float rads ) {
		rads += P.PI / 2;									// add to sync up with sin(0)
		float percent = ( rads % P.PI ) / P.PI;				
		int dir = ( rads % P.TWO_PI > P.PI ) ? -1 : 1;
		percent *= 2 * dir;
		percent -= dir;
		return percent;
	}

	public static float sawTan( float rads ) {
		rads += P.PI;
		float percent = ( rads % P.TWO_PI ) / P.TWO_PI;
		percent *= 2;
		percent -= 1;
		return percent;
	}
}
