package game;

import java.awt.Graphics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Represents the player's ship in the game. The player can control this ship
 * to move left or right and interact with other game elements.
 */
public class PlayerShip extends Polygon implements Moveable {
	
	/** The movement speed of the ship. */
	private static final int SPEED = 5; 
	/** The screen width boundary to prevent movement off-screen. */
	private int screenWidth; 
    /** Flags to track key press states. */
	private boolean movingLeft, movingRight;
    /** The player's health points. */
	private int hp;
    /** Reference to the Game instance. */
	private Game game; 
	
	 /**
     * Constructs a new PlayerShip object.
     * 
     * @param shape       The shape of the ship.
     * @param position    The initial position of the ship.
     * @param rotation    The rotation angle of the ship.
     * @param screenWidth The screen width boundary.
     * @param game        The reference to the Game instance.
     */
	public PlayerShip(Point[] shape, Point position, double rotation, int screenWidth, Game game) {
		super(shape, position, rotation);
		this.screenWidth = screenWidth;
		this.game = game; // Store the reference to Game
		this.hp = 3;
	}
	
	 /**
     * Moves the player ship within the screen boundaries.
     */
	@Override
	public void move() {
		if (game.isGameOver()) return; // Stop movement if game is over

		if (movingLeft && position.x > 0) {
			position.x -= SPEED;
		}
		if (movingRight && position.x + getWidth() < game.getWidth()) {
			position.x += SPEED;
		}
	}
	
	 /**
     * Reduces the ship's health when it takes damage.
     * If health reaches zero, the game ends.
     */
	public void takeDamage() {
		hp--;
		if (hp <= 0) {
			game.setGameOver(); // Call Game's method to handle game over
		}
	}
	
	/**
     * Registers key controls for moving the ship.
     * Uses a lambda function to simplify key event handling.
     * 
     * @param game The game instance to register key events.
     */
    public void registerControls(Game game) {
        game.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> movingLeft = true; // Move left when left arrow key is pressed
                    case KeyEvent.VK_RIGHT -> movingRight = true; // Move right when right arrow key is pressed
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> movingLeft = false; // Stop moving left when key is released
                    case KeyEvent.VK_RIGHT -> movingRight = false; // Stop moving right when key is released
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }

    /**
     * Renders the player ship on the screen.
     * 
     * @param brush The Graphics object used for drawing.
     */
	@Override
	public void paint(Graphics brush) {
		brush.fillPolygon(getXPoints(), getYPoints(), getXPoints().length);
		paintHealthBar(brush);
	}
	
	/**
     * Draws the player's health bar.
     * 
     * @param brush The Graphics object used for drawing.
     */
	private void paintHealthBar(Graphics brush) {
		brush.setColor(java.awt.Color.GREEN);
		brush.fillRect((int) position.x - 10, (int) (position.y + 20), hp * 20, 5);
	}
	
	 /**
     * Gets the current position of the player ship.
     * 
     * @return The position of the ship.
     */
	public Point getPosition() {
		return this.position;
	}
	
	/**
     * Gets the width of the player ship.
     * 
     * @return The width of the ship.
     */
	public int getWidth() {
		Point[] points = getShape(); // Get the shape from the parent class
		return (int) (points[1].x - points[0].x); // Assuming points[1] is the rightmost point
	}
	
	 /**
     * Renders additional visual elements for the player ship.
     * 
     * @param g The Graphics object used for drawing.
     */
	public void render(Graphics g) {
	    g.setColor(java.awt.Color.GREEN); // Example color
	    g.fillRect((int) position.x, (int) position.y, 50, 30); // Example player size (50x30)
	}
	
	/* getter for player health*/
	public int getHP() {
		return hp;
	}
	
	/* reset health method for restarting game*/
	public void resetHealth() {
		hp = 3;
	}
}
