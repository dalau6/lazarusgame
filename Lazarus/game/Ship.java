package Lazarus.game;

import java.awt.Image;
import java.awt.Point;

import Lazarus.LazarusWorld;
import Lazarus.modifiers.motions.MotionController;

/* Ships are things that have weapons and health */
public class Ship extends MoveableObject {
	protected int health;
	protected Point gunLocation;
	
    public Ship(Point location, Point speed, int strength, Image img){
    	super(location,speed,img);
    	this.strength=strength;
    	this.health=strength;
    	this.gunLocation = new Point(15,20);
    }
    
    public Ship(int x, Point speed, int strength, Image img){
    	this(new Point(x,-90), speed, strength, img);
    }
    
    public void damage(int damageDone){
    	this.health -= damageDone;
    	if(health<=0){
    		this.die();
    	}
    	return;
    }
    
    public void die(){
    	this.show=false;
    	motion.deleteObserver(this);
    	LazarusWorld.getInstance().removeClockObserver(motion);
    }
    
    public void collide(GameObject otherObject){
    }
    
    /* some setters and getters!*/
    public void setHealth(int health){
    	this.health = health;
    }
    
    public int getHealth(){
    	return health;
    }
    
    public MotionController getMotion(){
    	return this.motion;
    }
    
    public void setMotion(MotionController motion){
    	this.motion = motion;
    }
}