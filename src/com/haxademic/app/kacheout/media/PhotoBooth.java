package com.haxademic.app.kacheout.media;

import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.app.PAppletHax;
import com.haxademic.core.util.ImageUtil;
import com.haxademic.core.util.ScreenUtil;
import com.haxademic.core.util.SystemUtil;

public class PhotoBooth {
	public static void snapGamePhoto( PAppletHax p, int stageWidth, int stageHeight ) {
		// save screenshot and open it back up
		String screenshotFile = ScreenUtil.screenshotToJPG( p, "bin/output/kacheout/kacheout-" );
		PImage screenshot = p.loadImage( screenshotFile );

		// save kinect
		p.kinectWrapper.getVideoImage().save( "bin/output/kacheout/kacheout-" + SystemUtil.getTimestampFine( p ) + "-rgb.png" );

		if( p.kinectWrapper.isActive() ) {
			float screenToOutputWidthRatio = 640f / (float)stageWidth;
			int screenShotHeight = Math.round( stageHeight * screenToOutputWidthRatio );
			PImage img = p.createImage(640, 480 + screenShotHeight, P.RGB);

			// paste 2 images together and save
			img.copy( ImageUtil.getReversePImage( p.kinectWrapper.getVideoImage() ), 0, 0, 640, 480, 0, 0, 640, 480 );
			img.copy( screenshot, 0, 0, stageWidth, stageHeight, 0, 481, 640, screenShotHeight );
			img.save( "bin/output/kacheout/kacheout-" + SystemUtil.getTimestampFine( p ) + "-comp.png" );
		}

		// clean up screenshot
		//			boolean success = ( new File( screenshotFile ) ).delete();
		//			if (!success) p.println("counldn't delete screenshot");
	}

}
