package game;
import java.awt.Graphics;
import java.util.Random;

/**
 * Represents an enemy alien in the game.
 * Aliens move horizontally and reverse direction upon reaching the screen edge.
 * They can take damage and be destroyed.
 */
public class EnemyAlien extends Polygon implements Moveable {
	
	/** The health points of the alien (requires multiple hits to destroy). */
	private int hp = 2; 
	/** The speed at which the alien moves. */
	private int speed = 3;
    /** The movement direction of the alien (1 for right, -1 for left). */
	private int direction = 1;
    /** The probability of shooting (lower value means more frequent shots). */
	private int shootProbability = 80;
    /** Random generator for determining shooting behavior. */
	private Random random = new Random();
	
	/**
     * Constructs an EnemyAlien object at a given position.
     * 
     * @param position The initial position of the alien.
     */
	public EnemyAlien(Point position) {
		super(new Point[]{
				new Point(-10, 10), new Point(10, 10), new Point(0, -10) // Upside-down triangle
		}, position, 0);
	}
	
	 /**
     * Moves the alien in its current direction.
     * If it reaches the screen edge, it reverses direction and moves downward.
     */
	@Override
	public void move() {
		if(Game.isGameOver())	{
			return;
		}
		else {
			position.x += speed * direction;
	
			// Check if alien hits screen edge
			if (position.x <= 0 || position.x >= 780) {
				reverseDirection();
			}
		}
	}	

	
	/**
     * Reverses the alien's movement direction and moves it downward.
     */
	public void reverseDirection() {
		direction *= -1; // Reverse movement direction
		position.y += 20; // Move aliens down
	}
	
	/**
     * Reduces the alien's health when it takes damage.
     * If health reaches zero, it is considered destroyed.
     */
	public void takeDamage() {
		hp = 0; // Dies in one hit
	}
	
	 /**
     * Checks if the alien is destroyed.
     * 
     * @return true if the alien's health is zero or below, false otherwise.
     */
	public boolean isDestroyed() {
		return hp <= 0;
	}
	
	/**
     * Renders the alien on the screen.
     * 
     * @param brush The Graphics object used for drawing.
     */
	@Override
	public void paint(Graphics brush) {
		brush.fillPolygon(getXPoints(), getYPoints(), getXPoints().length);
	}

	/**
     * Represents a wave of aliens and manages their movement.
     */
	public static class AlienWave {
		/** Array of EnemyAliens forming the wave. */
		private EnemyAlien[] aliens;
		
		/**
         * Constructs an AlienWave with the specified number of rows and columns.
         * 
         * @param rows Number of rows of aliens.
         * @param cols Number of columns of aliens.
         */
		public AlienWave(int rows, int cols) {
			aliens = new EnemyAlien[rows * cols];
			for (int i = 0; i < aliens.length; i++) {
				aliens[i] = new EnemyAlien(new Point(i * 40, 50));
			}
		}
		
		/**
         * Moves all aliens in the wave.
         */
		public void moveAll() {
			for (EnemyAlien alien : aliens) alien.move();
		}
	}
	
	/**
     * Determines whether the alien should shoot based on probability.
     * 
     * @return A new Bullet if the alien decides to shoot, otherwise null.
     */
	public Bullet shoot() {
		if(Game.isGameOver()) {
			return null;
		}
		if (random.nextInt(shootProbability) == 0) { // Random chance to shoot
			return new Bullet(new Point(position.x, position.y + 10), true);
		}
		return null;
	}
	
	/**
     * Sets the speed of the alien.
     * 
     * @param newSpeed The new speed value.
     */
	public void setSpeed(int newSpeed) {
	    this.speed = newSpeed;
	}
	
	/**
     * Sets the shooting probability of the alien.
     * 
     * @param newShootRate The new shooting probability.
     */
	public void setShootProbability(int newShootRate) {
	    this.shootProbability = newShootRate;
	}
	
	/**
     * Renders the alien with a specific color and size.
     * 
     * @param g The Graphics object used for drawing.
     */
	public void render(Graphics g) {
	    g.setColor(java.awt.Color.RED); // Example enemy color
	    g.fillRect((int) position.x, (int) position.y, 40, 30); // Example enemy size (40x30)
	}

}
