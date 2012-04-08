package com.haxademic.viz;

import com.haxademic.core.util.ColorGroup;

public interface IVizElement {
	public void init();
	public void update();
	public void reset();
	public void dispose();
	
	public void updateColorSet( ColorGroup colors );
	public void updateLineMode();
	public void updateCamera();
}
