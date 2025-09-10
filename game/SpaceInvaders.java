package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

 */
import java.awt.*;


import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main game class for Space Invaders.
 * Manages the player, enemies, bullets, game loop, and collision detection.
 */
class SpaceInvaders extends Game {
	/** Counter used for debugging rendering updates */
	static int counter = 0;
    /** The player-controlled spaceship. */
	private PlayerShip player;
    /** List of bullets fired by the player. */
	private ArrayList<Bullet> bullets = new ArrayList<>();
    /** List of enemy aliens in the game. */
	private ArrayList<EnemyAlien> enemies = new ArrayList<>();
    /** The current game score. */
	private int score = 0;
    /** List of bullets fired by the enemy aliens. */
	private ArrayList<Bullet> alienBullets = new ArrayList<>();
    /** The current round number. */
	private int roundNumber = 1;
    /** Boolean flag to determine whether to display round text. */
	private boolean showRoundText = true;
	
	/**
     * Constructs the SpaceInvaders game.
     */
	public SpaceInvaders() {
		super("SpaceInvaders!",800,600);
		this.setFocusable(true);
		this.requestFocus();

		// Initialize player with proper constructor
		Point[] shipShape = { new Point(-10, -10), new Point(10, -10), new Point(0, 10) };
        player = new PlayerShip(shipShape, new Point(250, 450), 0, 800, this);
        startGame(); // Call startGame AFTER initializing player
        player.registerControls(this);


		// Create aliens in a grid
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 5; col++) {
				enemies.add(new EnemyAlien(new Point(100 + col * 50, 50 + row * 40)));
			}
		}


		// Keyboard input
		this.addKeyListener((new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!isGameOver()) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						bullets.add(new Bullet(new Point(player.getPosition().x, player.getPosition().y - 10), false));
					}
				}
				else {
					return;
				}
			}
		}));

		// Game loop using an Anonymous Class
		Timer gameLoop = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.move();

				// Move player and enemy bullets
				for (Bullet b : bullets) b.move();
				for (Bullet b : alienBullets) b.move(); // Move alien bullets downward

				// Move all enemies
				for(EnemyAlien enemy : enemies) {
					enemy.move();
					
					// Aliens randomly shoot bullets
					Bullet bullet = enemy.shoot();
		            if (bullet != null) {
		                alienBullets.add(bullet);
		            }
				}

				// Check for collisions
				detectCollisions();
				repaint();
				if(player.getHP() == 0) {
					bullets.clear();
					setGameOver();
				}
			}
		});
		gameLoop.start();

	}
	
	/*public SpaceInvaders startGame() {
		return new SpaceInvaders();
	}*/
	
	 /**
     * Paints all game objects on the screen.
     * 
     * @param brush The Graphics object used for drawing.
     */
	public void paint(Graphics brush) {
		//super.paint(brush);
		
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		counter++;
		brush.setColor(Color.white);
		brush.drawString("Counter is " + counter,10,10);
		
		player.paint(brush);
		for (Bullet b : bullets) b.paint(brush);
		for (Bullet b : alienBullets) b.paint(brush);
		for (EnemyAlien enemy : enemies) enemy.paint(brush);
		
		// Display Score
	    brush.setColor(java.awt.Color.WHITE);
	    brush.setFont(new Font("Arial", Font.BOLD, 20));
	    brush.drawString("Score: " + score, 20, 30);
	    brush.drawString("Round: " + roundNumber, 20, 60);
	    
	    // Display "Round X" at the beginning of a round
	    if (showRoundText) {
	        brush.setFont(new Font("Arial", Font.BOLD, 40));
	        brush.drawString("ROUND " + roundNumber, 300, 300);
	    }
	    //altering the screen when the game is over/when character dies
	    if(gameOver) {
	    	brush.setFont(new Font("Arial", Font.BOLD, 40));
	        brush.drawString("GAME OVER", 300, 300);
	        //make the press enter to restart later
			brush.setFont(new Font("Arial", Font.BOLD, 30));
			brush.setColor(Color.WHITE);
			brush.drawString("Your high score: " + score, 300, 350);
	    }
	}
	
	/**
     * Detects collisions between bullets and enemies or player.
     */
	public void detectCollisions() {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			for (Iterator<EnemyAlien> enemyIterator = enemies.iterator(); enemyIterator.hasNext();) {
				EnemyAlien enemy = enemyIterator.next();
				if (bullet.checkCollision(enemy)) {
					enemy.takeDamage(); // One hit kills alien
					enemyIterator.remove(); // Remove alien
					score += 100; // Increase score
					bulletIterator.remove();
					break;
				}
			}
		}

		// Check if enemy bullets hit the player
		Iterator<Bullet> alienBulletIterator = alienBullets.iterator();
		while (alienBulletIterator.hasNext()) {
			Bullet alienBullet = alienBulletIterator.next();
			if (alienBullet.checkCollision(player)) {
				player.takeDamage();
				alienBulletIterator.remove();
			}
		}

		// Start a new round when all aliens are destroyed
		if (enemies.isEmpty()) {
			startNewRound(roundNumber);
		}
		
		if(roundNumber == 1) {
			// Show "ROUND X" for 1 second, then clear it and force a repaint
			Timer roundTimer = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showRoundText = false;
					repaint(); // Ensures the text disappears
				}
			});
			roundTimer.setRepeats(false);
			roundTimer.start();
		}
	}
	
	/**
     * Starts a new round, increasing difficulty.
     * 
     * @param round The current round number.
     */
	private void startNewRound(int round) {
		int speed = 3;
		int shootProb = 80;
		if(!gameOver) {
			roundNumber++;
		
		
			showRoundText = true; // Show "ROUND X" text
			bullets.clear();
			alienBullets.clear();
	
			// Increase difficulty
			speed = 2 + roundNumber; // Aliens move faster
			shootProb = Math.max(20, 100 - (roundNumber * 10)); // Aliens shoot more
		}
		
		

		// Respawn aliens
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 5; col++) {
				EnemyAlien alien = new EnemyAlien(new Point(100 + col * 50, 50 + row * 40));
				alien.setSpeed(speed);
				alien.setShootProbability(shootProb);
				enemies.add(alien);
			}
		}
		

		// Show "ROUND X" for 1 second, then clear it and force a repaint
		Timer roundTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showRoundText = false;
				repaint(); // Ensures the text disappears
			}
		});
		roundTimer.setRepeats(false);
		roundTimer.start();
	}
	
	/**
     * Main method to launch the game.
     */
	public static void main (String[] args) {
		SpaceInvaders a = new SpaceInvaders();
		a.repaint();
	}
}