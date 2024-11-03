package model.units.villagers;

import model.CellGrid;
import model.cells.Cell;

import java.awt.geom.Point2D;

public class Position
{

	/**
	 * villager speed, in cells/millisecond
	 */
	private static final double SPEED = 2.0 / 1000;

	private double x, y;
	private double speedX, speedY;
	private long startTime;
	private boolean moving = false;

	public Position(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Position(Point2D.Double point)
	{
		this(point.x, point.y);
	}

	/**
	 * @return the cell at this position
	 */
	public Cell getCell(CellGrid cellGrid)
	{
		return cellGrid.getCell((int) getX(), (int) getY());
	}

	/**
	 * @return the villager x coordinate in pixels
	 * uses interpolation
	 */
	public double getX()
	{
		return interpolate(x, speedX);
	}

	/**
	 * @return the villager y coordinate in pixels
	 * uses interpolation
	 */
	public double getY()
	{
		return interpolate(y, speedY);
	}

	/**
	 * @return the villager position as a Point2D.Double
	 * uses interpolation
	 */
	public Point2D.Double getPoint()
	{
		return new Point2D.Double(getX(), getY());
	}

	/**
	 * Internal function used to interpolate the villager position
	 */
	private double interpolate(double startPos, double speed)
	{
		if(moving)
			return startPos + (System.currentTimeMillis() - startTime) * speed;
		else
			return startPos;
	}

	/**
	 * WARNING, DO NOT USE DIRECTLY, USE THE Villager's moveTo() FUNCTION INSTEAD !
	 * Causes the villager to start moving to the target point.
	 *
	 * @param x the x coordinate to move to
	 * @param y the y coordinate to move to
	 * @return the traject duration in milliseconds.
	 * The calling thread is responsible for calling endMove() after this duration
	 */
	public int move(double x, double y)
	{
		double distanceCells = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));

		speedX = (x - this.x) / distanceCells * SPEED;
		speedY = (y - this.y) / distanceCells * SPEED;

		startTime = System.currentTimeMillis();
		moving = true;

		return (int) (distanceCells / SPEED);
	}

	/**
	 * Used by the villager moveTo() function to instruct the villager to end its movement
	 * End position is provided to ensure the villager always ends in its exact target position
	 * Use fixPosition() instead to fix the villager's position afetr an interrupted movement
	 */
	public void finishMoving(double x, double y)
	{
		moving = false;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the string of the position
	 **/
	@Override
	public String toString()
	{
		return "Position: " + getX() + " " + getY();
	}

}
