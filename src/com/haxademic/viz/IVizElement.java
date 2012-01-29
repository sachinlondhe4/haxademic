package com.haxademic.viz;

import toxi.color.TColor;

public interface IVizElement {
	public void init();
	public void update();
	public void reset();
	public void dispose();
	
	public void updateColor( TColor color );
	public void updateLineMode();
	public void updateCamera();
}
