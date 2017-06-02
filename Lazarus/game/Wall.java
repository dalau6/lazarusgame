package Lazarus.game;

import java.awt.Image;
import java.awt.Point;

import Lazarus.game.enemy.Box;

public class Wall extends Box{
        
  public Wall(int x, int y, Image image){
      super(new Point(x*40, y*40), new Point(0, 0), y, image);
  }
}
  

