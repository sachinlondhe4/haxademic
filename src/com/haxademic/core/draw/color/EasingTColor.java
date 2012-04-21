package com.haxademic.core.draw.color;

import toxi.color.TColor;

public class EasingTColor {
	
	public TColor curColor;
	public TColor targetColor;
	protected float _easing;
	
	/**
	 * Eases a TColor towards another.
	 * @param color	
	 * @param ease	should be < 1.0. smaller numbers ease slower
	 */
	public EasingTColor( TColor color, float ease ) {
		this.curColor = color.copy();
		this.targetColor = color.copy();
		_easing = ease;
	}
	
	public void setCurColor( TColor color ) {
		this.curColor = color.copy();
	}
	
	public void setTargetColor( TColor color ) {
		this.targetColor = color.copy();
	}
	
	public void setCurAndTargetColors( TColor cur, TColor target ) {
		this.curColor = cur.copy();
		this.targetColor = target.copy();
	}
	
	public void update() {
		curColor.blend( targetColor, _easing );
	}
	
	public TColor color() {
		return curColor;
	}
}
