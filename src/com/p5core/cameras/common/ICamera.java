package com.p5core.cameras.common;

public interface ICamera 
{
	public void init();
	public void update();
	public void reset();  
	public void setPosition( int offsetX, int offsetY, int offsetZ );
	public void setTarget( int targetX, int targetY, int targetZ );
}
