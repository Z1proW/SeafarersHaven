package model.units;

import model.CellGrid;
import model.cells.Cell;
import model.units.villagers.Position;
import model.units.villagers.TM;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Unit
{

	private final Position position;
	private final int maxHealth;
	private int health;
	private final Image texture;

	/**
	 * Constructor of unit
	 *
	 * @param spawnPoint of the unit
	 * @param maxHealth  of the unit
	 * @param texture    of the unit
	 **/
	public Unit(Point2D.Double spawnPoint, int maxHealth, Image texture)
	{
		this.position = new Position(spawnPoint);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.texture = texture;
	}

	/**
	 * @return the position of the unit
	 **/
	public Point2D.Double getPosition()
	{
		return position.getPoint();
	}

	/**
	 * @return the x of the unit
	 **/
	public double getX()
	{
		return position.getX();
	}

	/**
	 * @return the y of the unit
	 **/
	public double getY()
	{
		return position.getY();
	}

	/**
	 * @param cellGrid where we take a cell of
	 * @return the cell where the unit is currently at
	 **/
	public Cell getCell(CellGrid cellGrid)
	{
		return position.getCell(cellGrid);
	}

	/**
	 * @return the max health of a unit
	 **/
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * @return the health of the unit
	 **/
	public int getHealth()
	{
		return health;
	}

	/**
	 * @return true if the unit is alive
	 **/
	public boolean isAlive()
	{
		return health > 0;
	}

	/**
	 * @return the texture of the unit
	 **/
	public Image getTexture()
	{
		return texture;
	}

	/**
	 * damage the unit
	 *
	 * @param damageDealt the amount of damage dealt
	 */
	public void damage(int damageDealt)
	{
		health -= damageDealt;

		if(health <= 0)
		{
			health = 0;

			kill();
		}
	}

	/**
	 * heal the unit
	 *
	 * @param amount the amount of health restored
	 */
	public void heal(int amount)
	{
		health += amount;

		if(health > maxHealth) health = maxHealth;
	}

	abstract public void kill();

	/**
	 * Causes the unit to move to the target position, and waits for travel's end
	 */
	public void moveTo(double x, double y)  // TODO erreur si pas un thread
	{
		int travelTime = position.move(x, y);

		TM.sleep(travelTime, () -> position.finishMoving(getX(), getY()));

		position.finishMoving(x, y);
	}

}
