package Lazarus.game.enemy;

import java.awt.Point;

import Lazarus.LazarusWorld;

public class MetalBox extends Box{
    public MetalBox(int x, int y){
        super(new Point(x, y), new Point(0, 0), 3, LazarusWorld.sprites.get("metal"));
    }
}
