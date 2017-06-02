package Lazarus.game.enemy;

import java.awt.Point;

import Lazarus.LazarusWorld;

public class CardBox extends Box{
	public CardBox(int x, int y){
        super(new Point(x, y), new Point(0, 0), 1, LazarusWorld.sprites.get("card"));
    }
}
