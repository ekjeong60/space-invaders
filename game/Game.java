package game;

/*
CLASS: Game

DESCRIPTION: A painted canvas in its own window, updated every tenth second.
USAGE: Extended by YourGameName.
NOTE: You don't need to understand the details here, no fiddling neccessary.*/
import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * Abstract class representing the base game framework.
 * Handles rendering, updating, game states, and input handling.
 */
abstract class Game extends Canvas {
	
    /** Boolean flag to control the game loop. */
	protected boolean on = true;
    /** The width & height of the game screen. */
	protected int width, height;
    /** The off-screen buffer for double buffering. */
	protected Image buffer;

    /** Boolean flag to track if the game is over. */
	public static boolean gameOver = false;
    /** The player-controlled spaceship. */
	private PlayerShip player;
    /** List of enemy aliens in the game. */
	private ArrayList<EnemyAlien> enemies;
	/** List of bullets fired in the game. */
	private ArrayList<Bullet> bullets = new ArrayList<>();
	/** List of bullets fired in the game. */
	private int score = 0;
	
	private Frame frame;

	// Define the player's ship shape (use correct values from your game)
	private Point[] shipShape = {
			new Point(-10, -10),
			new Point(10, -10),
			new Point(0, 10)
	};
	
	/**
     * Abstract method for rendering the game.
     * 
     * @param brush The Graphics object used for drawing.
     */
	public Game(String name, int inWidth, int inHeight) {
		width = inWidth;
		height = inHeight;
		
		// Frame can be read as 'window' here.
		this.frame = new Frame(name);
		frame.add(this);
		frame.setSize(width,height);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {System.exit(0);} 
		});

		buffer = createImage(width, height);
	}

	/**
     * Abstract method for rendering the game.
     * 
     * @param brush The Graphics object used for drawing.
     */
	abstract public void paint(Graphics brush);
	
	/**
     * Handles rendering updates and game over display.
     * 
     * @param g The Graphics object used for drawing.
     */
	protected void paintComponent(Graphics g) {
		super.paint(g);

		if (gameOver) {
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.setColor(Color.RED);
			g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);

			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Color.WHITE);
			g.drawString("Press ENTER to Restart", getWidth() / 2 - 150, getHeight() / 2 + 50);
			return; // Stop drawing the rest of the game elements
		}

		// Normal game drawing
		player.render(g);
		for (EnemyAlien enemy : enemies) {
			enemy.render(g);
		}
	}

	// 'update' paints to a buffer then to the screen, then waits a tenth of
	// a second before repeating itself, assuming the game is on. This is done
	// to avoid a choppy painting experience if repainted in pieces.
	public void update(Graphics brush) {
		paint(buffer.getGraphics());
		brush.drawImage(buffer,0,0,this);
		if (on) {sleep(10); repaint();}

		// Add other game logic updates here
		if (gameOver) {
			return; // Stop updating if game over
		}

		if (player != null) {  // Check to prevent NullPointerException
			player.move();
		}

		for (EnemyAlien enemy : enemies) {
			enemy.move();
			enemy.shoot();
		}
	}

	 /**
     * Pauses execution for a given time.
     * 
     * @param time The duration in milliseconds.
     */
	private void sleep(int time) {
		try {Thread.sleep(time);} catch(Exception exc){};
	}
	
	 /**
     * Sets the game state to over and triggers a repaint.
     */
	public void setGameOver() {
		gameOver = true;
		repaint(); // Redraw the screen to show "Game Over"
	}
	
	/**
     * Starts a new game by initializing player and enemies.
     */
	public void startGame() {
		gameOver = false;
		enemies = new ArrayList<>(); // Initialize enemies list

		// Initialize the player ship with reference to the game
		player = new PlayerShip(shipShape, new Point(100, 500), 0, getWidth(), this);

		// Add some enemy aliens (example, adjust as needed)
		enemies.add(new EnemyAlien(new Point(200, 100)));
		enemies.add(new EnemyAlien(new Point(300, 100)));
	}
	
	
	 /**
     * Checks if the game is over.
     * 
     * @return True if the game is over, false otherwise.
     */
	public static boolean isGameOver() {
		return gameOver;
	}
}