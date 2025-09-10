package game;

import java.awt.Graphics;

public class Bullet extends Polygon implements Moveable {
	/**
	 * Represents a bullet in the game, which can be fired by the player or enemies.
	 * The bullet moves in a straight line and follows movement rules based on its owner.
	 */
	
	/**
     * The speed at which the bullet moves.
     */
	private int speed = 5;
	
	 /**
     * Constructs a new Bullet object.
     * 
     * @param position The starting position of the bullet.
     * @param isEnemy  Whether the bullet belongs to an enemy.
     */
	public Bullet(Point position, boolean isEnemy) {
		super(new Point[]{
				new Point(-2, -5), new Point(2, -5), new Point(2, 5), new Point(-2, 5)
		}, position, 0);

		// Set bullet speed direction
		this.speed = isEnemy ? 5 : -5; // Enemy bullets go down, player bullets go up
	}

	/**
     * Moves the bullet in its designated direction.
     */
	@Override
	public void move() {
		position.y += speed; // Moves up or down based on speed
	}
	
	 /**
     * Checks if the bullet collides with another polygonal object.
     * 
     * @param object The polygon to check for collision.
     * @return true if the bullet collides with the object, false otherwise.
     */
	public boolean checkCollision(Polygon object) {
		return this.collides(object);
	}
	
	/**
     * Renders the bullet on the screen.
     * 
     * @param g The Graphics object used for drawing.
     */
	@Override
	public void paint(Graphics brush) {
		brush.fillPolygon(getXPoints(), getYPoints(), getXPoints().length);
	}
	
	 /**
     * Updates the bullet's position by moving it upwards.
     */
	public void update() {
	    position.y -= 5; // Move bullet upwards (adjust speed as needed)
	}

	 /**
     * Inner class representing an explosion effect when a bullet hits an object.
     */
	class ExplosionEffect {
		
		/**
         * Duration of the explosion effect in frames.
         */
		private int duration = 20; // Frames of explosion
		
		 /**
         * Renders the explosion effect at the bullet's position.
         * 
         * @param brush The Graphics object used for drawing.
         */
		public void paint(Graphics brush) {
			brush.fillOval((int) position.x - 5, (int) position.y - 5, 10, 10);
			duration--;
		}
	}
}
