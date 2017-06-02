package Lazarus.game.enemy;

import java.awt.Image;
import java.awt.Point;

import Lazarus.game.Ship;

public class Box extends Ship{
	public Box(Point location, Point speed, int strength, Image img){
        super(location, speed, strength, img);
    }
    
    public int getStrength(){
        return strength;
    }
}
