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
		createMeshPool();
	}
	
	protected void createMeshPool() {
		// make sure geomerative is ready
		if( RG.initialized() == false ) RG.init( p );
		
		// load fonts
		RFont _fontHelloDenver = new RFont( "../data/fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
		RFont _fontBitLow = new RFont( "../data/fonts/bitlow.ttf", 200, RFont.CENTER);
		
		// "create denver presents"
		p.meshPool.addMesh( p.CREATE_DENVER, MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 0.8f ), 1 );
		p.meshPool.addMesh( p.PRESENTS_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "PRESENTS", -1, 2, 0.4f ), 1 );
//		_textCreateDenver = MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f ), 20 );
//		_textCreateDenver = MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, -1, "CREATE DENVER", -1, 3, 1f );
		
		// Kacheout logo
		p.meshPool.addMesh( p.KACHEOUT_LOGO, MeshUtil.meshFromImg( p, "../data/images/kacheout/kacheout.gif", 1f ), 20f );

		// Built by text & ufo
		p.meshPool.addMesh( p.BUILT_BY_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "BUILT BY:", -1, 2, 0.5f ), 1 );
		p.meshPool.addMesh( p.UFO, MeshUtil.meshFromImg( p, "../data/images/kacheout/ufo.gif", 1f ), 15f );
		
		// cacheflowe / mode set
		p.meshPool.addMesh( p.MODE_SET_LOGO, MeshUtil.meshFromOBJ( p, "../data/models/mode-set.obj", 1f ), 150 );
		p.meshPool.addMesh( p.MODE_SET_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/modeset-logotype.svg", -1, 6, 0.35f ), 4 ), 1 );
		p.meshPool.addMesh( p.CACHEFLOWE_LOGO, MeshUtil.meshFromOBJ( p, "../data/models/cacheflowe-3d.obj", 80f ), 1f );
		p.meshPool.addMesh( p.CACHEFLOWE_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, "../data/svg/cacheflowe-logotype.svg", -1, 6, 0.6f ), 4 ), 1 );

		// design credits
		p.meshPool.addMesh( p.DESIGN_BY, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "DESIGN BY:", -1, 2, 0.3f ), 1 );
		p.meshPool.addMesh( p.JON_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "JON TRAISTER", -1, 2, 0.4f ), 1 );
		p.meshPool.addMesh( p.RYAN_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LATENIGHT WEEKNIGHT", -1, 2, 0.4f ), 1 );
		
		// countdown
		p.meshPool.addMesh( p.COUNTDOWN_TEXT_1, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "1", -1, 2, 3f ), 4 ), 1 );
		p.meshPool.addMesh( p.COUNTDOWN_TEXT_2, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "2", -1, 2, 3f ), 4 ), 1 );
		p.meshPool.addMesh( p.COUNTDOWN_TEXT_3, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "3", -1, 2, 3f ), 4 ), 1 );
		
		// win/lose
		p.meshPool.addMesh( p.WIN_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "WIN", -1, 2, 0.4f ), 10 ), 1 );
		p.meshPool.addMesh( p.LOSE_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LOSE", -1, 2, 0.4f ), 10 ), 1 );

		
	}

}
