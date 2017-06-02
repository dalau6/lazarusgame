package Lazarus;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JFrame;

import Lazarus.game.*;
import Lazarus.ui.InterfaceObject;

import Lazarus.modifiers.AbstractGameModifier;
import Lazarus.modifiers.motions.MotionController;
import Lazarus.game.enemy.Box;
import Lazarus.modifiers.*;

// extending JPanel to hopefully integrate this into an applet
// but I want to separate out the Applet and Application implementations
public class LazarusWorld extends GameWorld {
    
    private Thread thread;
    
    // GameWorld is a singleton class!
    private static LazarusWorld game = new LazarusWorld();
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();
 
    public Level level;
    Random generator = new Random();
    int sizeX, sizeY;
    
    /*Some ArrayLists to keep track of game things*/
    private ArrayList<BackgroundObject> background;
    private ArrayList<Box> boxes;
    private ArrayList<PlayerShip> players;
    private ArrayList<InterfaceObject> ui;
    private ArrayList<Wall> walls;
    
    public static HashMap<String, Image> sprites = GameWorld.sprites;
    private BufferedImage bimg;

    public static HashMap<String, MotionController> motions = new HashMap();
    public static boolean gameFinished, gameOver, gameWon;
    ImageObserver observer;

    // constructors makes sure the game is focusable, then
    // initializes a bunch of ArrayLists  
    private LazarusWorld(){
        this.setFocusable(true);
        background = new ArrayList<BackgroundObject>();
        boxes = new ArrayList<Box>();
        sprites = new HashMap<String, Image>();
    }
  
    /* This returns a reference to the currently running game*/
    public static LazarusWorld getInstance(){
    	return game;
    }
  
    /*Game Initialization*/
    public void init() {
        loadSprites();
        gameOver = false;
	background = new ArrayList();
	players = new ArrayList();
	ui = new ArrayList();
	boxes = new ArrayList();
	walls = new ArrayList();
   	level = new Level("Resources/level.txt");
	level.addObserver(this);
	clock.addObserver(this.level);
        gameFinished = false;
        gameOver = false;
        gameWon = false;
	observer = this;
        
	addBackground(new Background(this.level.w, this.level.h, GameWorld.getSpeed(),sprites.get("background")));
	level.load();
  }

    public void resetGame() {
        boxes.clear();
        gameFinished = false;
        init();
    }
    
    /*Functions for loading image resources*/
    protected void loadSprites(){
	    sprites.put("background", getSprite("Resources/Background.png"));
	    sprites.put("button", getSprite("Resources/Button.png"));
            sprites.put("card", getSprite("Resources/CardBox.png"));               
            sprites.put("mesh", getSprite("Resources/Mesh.png"));
            sprites.put("metal", getSprite("Resources/MetalBox.png"));
            sprites.put("stone", getSprite("Resources/StoneBox.png"));
            sprites.put("wall", getSprite("Resources/Wall.png"));
	    sprites.put("wood", getSprite("Resources/WoodBox.png"));
            sprites.put("Lazarus_afraid", getSprite("Resources/Lazarus_afraid_strip10.png"));
            sprites.put("Lazarus_jump_left", getSprite("Resources/Lazarus_jump_left_strip7.png"));
	    sprites.put("Lazarus_jump_right", getSprite("Resources/Lazarus_jump_right_strip7.png"));
	    sprites.put("Lazarus_left", getSprite("Resources/Lazarus_left_strip7.png"));
	    sprites.put("Lazarus_right", getSprite("Resources/Lazarus_right_strip7.png"));
	    sprites.put("Lazarus_squished", getSprite("Resources/Lazarus_squished_strip11.png"));
	    sprites.put("Lazarus_stand", getSprite("Resources/Lazarus_stand.png"));
            sprites.put("youwon", getSprite("Resources/youWin.png"));
    }
    	  
    public Image getSprite(String name) {
        URL url = LazarusWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }

    /********************************
     * 	These functions GET things	*
     * 		from the game world		*
     ********************************/
    
    public int getFrameNumber(){
    	return clock.getFrame();
    }
    
    public int getTime(){
    	return clock.getTime();
    }
    
    public void removeClockObserver(Observer theObject){
    	clock.deleteObserver(theObject);
    }
    
    public ListIterator<BackgroundObject> getBackgroundObjects(){
    	return background.listIterator();
    }
        
    public ListIterator<PlayerShip> getPlayers(){
    	return players.listIterator();
    }
    
    public void setDimensions(int w, int h){
    	this.sizeX = w;
    	this.sizeY = h;
    }

  /********************************
   *   These functions ADD things *
   * 	to the game world	      *
   ********************************/
    
    public void addBackground(BackgroundObject...newObjects){
    	for(BackgroundObject object : newObjects){
    		background.add(object);
    	}
    }
    
    public void addBackground(Wall... newObjects){
        Wall[] wall;
        int length = (wall = newObjects).length;
        for(int index=0; index<length; index++){
            Wall object = wall[index];
            walls.add(object);
        }
    }

    public void addPlayer(PlayerShip...newObjects){
    	for(PlayerShip player : newObjects){
    		players.add(player);
    	}
    }  

    public void addClockObserver(Observer theObject){
    	clock.addObserver(theObject);
    }
    
    // this is the main function where game stuff happens!
    // each frame is also drawn here
    public void drawFrame(int w, int h, Graphics2D g2) {
        ListIterator<?> iterator = getBackgroundObjects();
        
        // iterate through all blocks
        while(iterator.hasNext()){
            BackgroundObject obj = (BackgroundObject) iterator.next();
            obj.update(w, h);
            obj.draw(g2, this);
        }
      
    	if (!gameFinished) {
            // update players and draw
            iterator = getPlayers();
            while(iterator.hasNext()){
            	PlayerShip player = (PlayerShip) iterator.next();
            	
            	if(player.isDead()){
        	    gameOver=true;
            	}else{
                    ListIterator<Wall> wallList = this.walls.listIterator();
                    while(wallList.hasNext()){
                        Wall numWall = (Wall)wallList.next();
                        numWall.draw(g2, this);
                    }
                }
                
      PlayerShip p1 = (PlayerShip)this.players.get(0);
      p1.update(w, h);
      p1.draw(g2, this);
 
      if (!this.gameWon){
          resetGame();
      } 
      else{
          g2.drawImage(sprites.get("youwon"), sizeX/4, 200, null);
          boxes.clear();
    	  this.thread.interrupt();
      }
    }
  }
 }
  
    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    /* paint each frame */
    public void paint(Graphics g) {
        if(players.size()!=0)
        	clock.tick();
    	Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(windowSize.width, windowSize.height);
        drawFrame(windowSize.width, windowSize.height, g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }

    /* start the game thread*/
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
  
    /* run the game */
    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
        	this.requestFocusInWindow();
            repaint();
          
          try {
                thread.sleep(23); // pause a little to slow things down
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }

    /* End the game, and signal either a win or loss */
    public void endGame(boolean win){
    	this.gameOver = true;
    }
    
    public boolean isGameOver(){
    	return gameOver;
    }
    
    // signal that we can stop entering the game loop
    public void finishGame(){
    	gameFinished = true;
    }
  
    /*I use the 'read' function to have observables act on their observers.
     */
	@Override
    public void update(Observable o, Object arg) {
	AbstractGameModifier modifier = (AbstractGameModifier) o;
	modifier.read(this);
	}

    public static void main(String argv[]) {
	final LazarusWorld game = LazarusWorld.getInstance();
	JFrame f = new JFrame("Lazarus");
	f.addWindowListener(new WindowAdapter() {
	    public void windowGainedFocus(WindowEvent e) {
		game.requestFocusInWindow();
	    }
	});
	f.getContentPane().add("Center", game);
	f.pack();
	f.setSize(new Dimension(640, 500));
	game.setDimensions(640, 500);
	game.init();
	f.setVisible(true);
	f.setResizable(false);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	game.start();
    }
}
