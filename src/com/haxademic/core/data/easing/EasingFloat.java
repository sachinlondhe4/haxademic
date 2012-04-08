package com.haxademic.core.data.easing;


public class EasingFloat
implements IEasingValue {
	
	public float _val, _target, _easeFactor;
		   
	public EasingFloat( float value, float easeFactor ) {
		_val = value;
		_target = value;
		_easeFactor = easeFactor;
	}
	
	public float value() {
		return _val;
	}
	
	public void setTarget( float value ) {
		_target = value;
	}
	
	public void setEaseFactor( float value ) {
		_easeFactor = value;
	}
	
	public void update() {
		_val -= ( ( _val - _target ) / _easeFactor );
	}
	
}
