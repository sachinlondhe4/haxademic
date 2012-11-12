package com.haxademic.app.matchgame.game;

import java.util.ArrayList;

import processing.core.PImage;

import com.haxademic.app.P;
import com.haxademic.app.matchgame.MatchGame;

public class MatchGameAssets {
	protected MatchGame p;
	
	public static PImage PIECE_BACKFACE;
	public static PImage PIECE_1;
	public static PImage PIECE_2;
	public static PImage PIECE_3;
	public static PImage PIECE_4;
	public static PImage PIECE_5;
	public static PImage PIECE_6;
	public static ArrayList<PImage> PIECE_IMAGES;
	
	public MatchGameAssets() {
		p = (MatchGame) P.p;

		PIECE_BACKFACE =	p.loadImage( "../data/images/match-game/match-piece-backface.png" );
		PIECE_1 = 			p.loadImage( "../data/images/match-game/match-piece-01.png" );
		PIECE_2 = 			p.loadImage( "../data/images/match-game/match-piece-02.png" );
		PIECE_3 = 			p.loadImage( "../data/images/match-game/match-piece-03.png" );
		PIECE_4 = 			p.loadImage( "../data/images/match-game/match-piece-04.png" );
		PIECE_5 = 			p.loadImage( "../data/images/match-game/match-piece-05.png" );
		PIECE_6 = 			p.loadImage( "../data/images/match-game/match-piece-06.png" );

		// use this array to use piece images based on array indexes 
		PIECE_IMAGES = new ArrayList<PImage>();
		PIECE_IMAGES.add( PIECE_1 );
		PIECE_IMAGES.add( PIECE_2 );
		PIECE_IMAGES.add( PIECE_3 );
		PIECE_IMAGES.add( PIECE_4 );
		PIECE_IMAGES.add( PIECE_5 );
		PIECE_IMAGES.add( PIECE_6 );
	}
	

}
