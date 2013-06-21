package com.haxademic.sketch.test;

import com.haxademic.app.P;
import com.haxademic.app.PAppletHax;
import com.haxademic.core.data.FloatBuffer;

public class FloatBufferTest
extends PAppletHax  
{
	protected FloatBuffer buff;
	
	public void setup() {
		super.setup();		
		buff = new FloatBuffer(10);
	}

	public void drawApp() {
		buff.update((float)Math.sin(p.frameCount/20f) * 10f);
		
		P.println(buff.sum()+" | "+buff.sumPos()+" | "+buff.sumNeg());
	}
}
