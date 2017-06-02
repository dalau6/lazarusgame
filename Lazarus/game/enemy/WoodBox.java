package Lazarus.game.enemy;

import java.awt.Point;

import Lazarus.LazarusWorld;

public class WoodBox extends Box{
    public WoodBox(int x, int y){
        super(new Point(x, y), new Point(0, 0), 2, LazarusWorld.sprites.get("wood"));
    }
}
