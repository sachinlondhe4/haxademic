package com.haxademic.app.kacheout.media;

import geomerative.RFont;
import geomerative.RG;

import com.haxademic.app.PAppletHax;
import com.haxademic.app.kacheout.KacheOut;
import com.haxademic.core.draw.mesh.MeshUtil;

public class AssetLoader {
	
	protected KacheOut p;
	
	public AssetLoader() {
		p = (KacheOut)PAppletHax.getInstance();
	}
	
	public void createMeshPool() {
		// make sure geomerative is ready
		if( RG.initialized() == false ) RG.init( p );
		
		// load fonts
		RFont _fontHelloDenver = new RFont( "../data/fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
		RFont _fontBitLow = new RFont( "../data/fonts/bitlow.ttf", 200, RFont.CENTER);
		
		// "create denver presents"
		
		p.meshPool.addMesh( KacheOut.CREATE_DENVER_LOGO, MeshUtil.meshFromSVG( p, "../data/svg/create-denver-logo.svg", -1, 6, 0.35f ), 1 );
		p.meshPool.addMesh( KacheOut.CREATE_DENVER, MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 0.8f ), 1 );
		p.meshPool.addMesh( KacheOut.PRESENTS_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "PRESENTS", -1, 2, 0.4f ), 1 );
//		_textCreateDenver = MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f ), 20 );
//		_textCreateDenver = MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f );
		
		// Kacheout logo
		p.meshPool.addMesh( KacheOut.KACHEOUT_LOGO, MeshUtil.meshFromImg( p, "../data/images/kacheout/kacheout.gif", 1.2f ), 20f );
		p.meshPool.addMesh( KacheOut.KACHEOUT_LOGO_ALT, MeshUtil.meshFromImg( p, "../data/images/kacheout/kacheout_alt.gif", 1.2f ), 20f );

		// Built by text & ufo
		p.meshPool.addMesh( KacheOut.BUILT_BY_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "BUILT BY:", -1, 2, 0.5f ), 1 );
		p.meshPool.addMesh( KacheOut.UFO_1, MeshUtil.meshFromImg( p, "../data/images/kacheout/ufo_1.gif", 1.4f ), 15f );
		p.meshPool.addMesh( KacheOut.UFO_2, MeshUtil.meshFromImg( p, "../data/images/kacheout/ufo_2.gif", 1.4f ), 15f );
		p.meshPool.addMesh( KacheOut.UFO_3, MeshUtil.meshFromImg( p, "../data/images/kacheout/ufo_3.gif", 1.4f ), 15f );
		
		// cacheflowe / mode set
		p.meshPool.addMesh( KacheOut.MODE_SET_LOGO, MeshUtil.meshFromOBJ( p, "../data/models/mode-set.obj", 1f ), 150 );
		p.meshPool.addMesh( KacheOut.MODE_SET_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/modeset-logotype.svg", -1, 6, 0.35f ), 4 ), 1 );
		p.meshPool.addMesh( KacheOut.CACHEFLOWE_LOGO, MeshUtil.meshFromOBJ( p, "../data/models/cacheflowe-3d.obj", 80f ), 1f );
		p.meshPool.addMesh( KacheOut.CACHEFLOWE_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/cacheflowe-logotype.svg", -1, 6, 0.6f ), 4 ), 1 );

		// design credits
		p.meshPool.addMesh( KacheOut.DESIGN_BY, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "DESIGN BY:", -1, 2, 0.3f ), 1 );
		p.meshPool.addMesh( KacheOut.JON_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "JON TRAISTER", -1, 2, 0.4f ), 1 );
		p.meshPool.addMesh( KacheOut.RYAN_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LATENIGHT WEEKNIGHT", -1, 2, 0.4f ), 1 );
		
		// instructions screen
		p.meshPool.addMesh( KacheOut.STEP_UP_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "STEP UP", -1, 2, 0.4f ), 10 ), 1 );
		p.meshPool.addMesh( KacheOut.READY_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "READY!", -1, 2, 0.4f ), 10 ), 1 );
		
		// countdown
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_1, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "1", -1, 2, 2f ), 40 ), 1 );
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_2, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "2", -1, 2, 2f ), 40 ), 1 );
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_3, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "3", -1, 2, 2f ), 40 ), 1 );
		
		// win/lose
		p.meshPool.addMesh( KacheOut.WIN_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "WIN", -1, 2, 0.8f ), 10 ), 1 );
		p.meshPool.addMesh( KacheOut.LOSE_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LOSE", -1, 2, 0.5f ), 10 ), 1 );

		
	}

}
