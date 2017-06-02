package Lazarus.modifiers;

import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import Lazarus.game.*;
import Lazarus.LazarusWorld;

/*This is where enemies are introduced into the game according to a timeline*/
public class Level extends AbstractGameModifier implements Observer{
	int start;
        int length;
        BufferedReader level;
	Integer position;
	Random generator = new Random();
        String filename;
        String line;
        public int w, h;
	public static int endgameDelay = 5;	// don't immediately end on game end
        
    public Level(String filename){
        this.filename = filename;
        
        try{
            this.level = new BufferedReader(new InputStreamReader(LazarusWorld.class.getResource(filename).openStream()));
            line = this.level.readLine();
            w = line.length();
            h = 0;
            while(line!=null){
                h++;
                line = this.level.readLine();
            }
            this.level.close();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void load(){
        LazarusWorld world = LazarusWorld.getInstance();
        
        try{
            this.level = new BufferedReader(new InputStreamReader(LazarusWorld.class.getResource(this.filename).openStream()));
            line = level.readLine();
            w = line.length();
            h = 0;
            while(line!=null){
                int index = 0;
                for (length=line.length(); index<length; index++){
                    char object= line.charAt(index);
                    
                    if(object=='1'){
                        Wall wall = new Wall(index, h, world.sprites.get("wall"));
                        world.addBackground(wall);
                    }
                    if(object=='2'){
                        Wall wall = new Wall(index, h, world.sprites.get("button"));
                        world.addBackground(wall);
                    }
                    if(object=='3'){
                        int[] controls = {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
                        world.addPlayer(new PlayerShip(new Point(index*40, h*40), new Point (0,0), world.sprites.get("Lazarus_stand"), controls, "Lazarus"));
                    }
                }
                h++;
                line=level.readLine();
            }
            level.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void read(Object theObject){
    }
    
    public void update(Observable o, Object arg){
    LazarusWorld world = LazarusWorld.getInstance();
        setChanged();
        notifyObservers();  
    
    if (world.isGameOver()){
        if(endgameDelay<=0){
            world.removeClockObserver(this);
            world.finishGame();
        }else{
            endgameDelay --;
        }
    }
    }
  
/*    public Box getRandomBox(int x, int y){
        Box newBox = null;
        
        int object = generator.nextInt(4);
        switch(object){
            case 0:
                newBox = new CardBox(x, y);
                break;
            case 1:
                newBox = new WoodBox(x, y);
                break;
            case 2:
                newBox = new MetalBox(x, y);
                break;
            case 3:
                newBox = new StoneBox(x, y);
                break;
        }
        return newBox;
    }  */
}
