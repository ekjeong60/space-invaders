package game;

import java.awt.Graphics;

/*
CLASS: Polygon
DESCRIPTION: A polygon is a sequence of points in space defined by a set of
             such points, an offset, and a rotation. The offset is the
             distance between the origin and the center of the shape.
             The rotation is measured in degrees, 0-360.
USAGE: You are intended to instantiate this class with a set of points that
       forever defines its shape, and then modify it by repositioning and
       rotating that shape. In defining the shape, the relative positions
       of the points you provide are used, in other words: {(0,1),(1,1),(1,0)}
       is the same shape as {(9,10),(10,10),(10,9)}.
NOTE: You don't need to worry about the "magic math" details.

 */

/**
 * A polygon is a sequence of points in space defined by a set of shape points, an offset, and a rotation.
 * The offset represents the position of the polygon in space, and the rotation is measured in degrees (-360 to 360).
 */
class Polygon {
	/** The array of points defining the shape of the polygon. */
	private Point[] shape;  
	/** The offset position of the polygon. */
	public Point position;   
	/** The offset position of the polygon. */
	public double rotation; 

	/**
	 * Constructs a polygon with a given shape, position, and rotation.
	 * 
	 * @param inShape   The array of points defining the shape.
	 * @param inPosition The position of the polygon.
	 * @param inRotation The initial rotation in degrees.
	 */
	public Polygon(Point[] inShape, Point inPosition, double inRotation) {
		shape = inShape;
		position = inPosition;
		rotation = inRotation;

		// First, we find the shape's top-most left-most boundary, its origin.
		Point origin = shape[0].clone();
		for (Point p : shape) {
			if (p.x < origin.x) origin.x = p.x;
			if (p.y < origin.y) origin.y = p.y;
		}

		// Then, we orient all of its points relative to the real origin.
		for (Point p : shape) {
			p.x -= origin.x;
			p.y -= origin.y;
		}
	}

	/**
	 * Gets the points of the polygon after applying rotation and offset.
	 * 
	 * @return An array of Points representing the transformed polygon.
	 */
	public Point[] getPoints() {
		Point center = findCenter();
		Point[] points = new Point[shape.length];
		for (int i = 0; i < shape.length; i++) {
			//    for (Point p : shape) {
			Point p = shape[i];
			double x = ((p.x-center.x) * Math.cos(Math.toRadians(rotation)))
					- ((p.y-center.y) * Math.sin(Math.toRadians(rotation)))
					+ center.x/2 + position.x;
			double y = ((p.x-center.x) * Math.sin(Math.toRadians(rotation)))
					+ ((p.y-center.y) * Math.cos(Math.toRadians(rotation)))
					+ center.y/2 + position.y;
			points[i] = new Point(x,y);
		}
		return points;
	}

	/**
	 * Determines if the polygon contains a given point using the ray-casting algorithm.
   * 
   * @param point The point to check.
   * @return True if the polygon contains the point, otherwise false.
   */
  public boolean contains(Point point) {
	  Point[] points = getPoints();
	  double crossingNumber = 0;
	  for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
		  if ((((points[i].x < point.x) && (point.x <= points[j].x)) ||
				  ((points[j].x < point.x) && (point.x <= points[i].x))) &&
				  (point.y > points[i].y + (points[j].y-points[i].y)/
						  (points[j].x - points[i].x) * (point.x - points[i].x))) {
			  crossingNumber++;
		  }
	  }
	  return crossingNumber%2 == 1;
  }

  /**
   * Rotates the polygon by a given number of degrees.
   * 
   * @param degrees The degrees to rotate the polygon.
   */
  public void rotate(int degrees) {rotation = (rotation+degrees)%360;}

  /*
  The following methods are private access restricted because, as this access
  level always implies, they are intended for use only as helpers of the
  methods in this class that are not private. They can't be used anywhere else.
   */

  // "findArea" implements some more magic math.
  private double findArea() {
	  double sum = 0;
	  for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
		  sum += shape[i].x*shape[j].y-shape[j].x*shape[i].y;
	  }
	  return Math.abs(sum/2);
  }

  // "findCenter" implements another bit of math.
  private Point findCenter() {
	  Point sum = new Point(0,0);
	  for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
		  sum.x += (shape[i].x + shape[j].x)
				  * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
		  sum.y += (shape[i].y + shape[j].y)
				  * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
	  }
	  double area = findArea();
	  return new Point(Math.abs(sum.x/(6*area)),Math.abs(sum.y/(6*area)));
  }

  /**
   * Gets the x-coordinates of the polygon points.
   * 
   * @return An array of x-coordinates.
   */
  public int[] getXPoints() {
	  int[] xPoints = new int[shape.length];
	  for (int i = 0; i < shape.length; i++) {
		  xPoints[i] = (int) (position.x + shape[i].x);
	  }
	  return xPoints;
  }

  /**
   * Gets the y-coordinates of the polygon points.
   * 
   * @return An array of y-coordinates.
   */
  public int[] getYPoints() {
	  int[] yPoints = new int[shape.length];
	  for (int i = 0; i < shape.length; i++) {
		  yPoints[i] = (int) (position.y + shape[i].y);
	  }
	  return yPoints;
  }
  
  /**
   * Gets the shape of the polygon.
   * 
   * @return An array of Points representing the shape.
   */
  public Point[] getShape() {
	  return shape;
  }
  
  /**
   * Renders the polygon using the provided Graphics object.
   * 
   * @param brush The Graphics object used for drawing.
   */
  public void paint(Graphics brush) {
	  brush.fillPolygon(getXPoints(), getYPoints(), getXPoints().length);
  }
  
  /**
   * Checks if this polygon collides with another polygon.
   * 
   * @param other The other polygon to check collision against.
   * @return True if the polygons collide, otherwise false.
   */
  public boolean collides(Polygon other) {
	  for (int i = 0; i < this.getXPoints().length; i++) {
		  int x = this.getXPoints()[i];
		  int y = this.getYPoints()[i];

		  if (other.contains(new Point(x, y))) {
			  return true;
		  }
	  }
	  return false;
  }
}