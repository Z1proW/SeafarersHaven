package model;

import model.units.villagers.Villager;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * the selected villagers
 * and the selection rectangle
 */
public class Selection
{

	private final ArrayList<Villager> villagers = new ArrayList<>();
	private Point2D.Double initialPosition, draggedPosition;

	/**
	 * @return the list of selected villagers
	 */
	public ArrayList<Villager> getVillagers()
	{
		return villagers;
	}

	/**
	 * adds a villager to the list
	 */
	public void add(Villager villager)
	{
		villagers.add(villager);
	}

	/**
	 * removes a villager from the list
	 */
	public void remove(Villager villager)
	{
		villagers.remove(villager);
	}

	/**
	 * clears the list
	 */
	public void clear()
	{
		villagers.clear();
	}

	/**
	 * @return the villagers in the rectangle
	 */
	public boolean contains(Villager villager)
	{
		return getRectangle().contains(villager.getX(), villager.getY());
	}

	/**
	 * @return the selection rectangle
	 */
	public Rectangle2D.Double getRectangle()
	{
		return new Rectangle2D.Double(
				Math.min(initialPosition.x, draggedPosition.x),
				Math.min(initialPosition.y, draggedPosition.y),
				Math.abs(initialPosition.x - draggedPosition.x),
				Math.abs(initialPosition.y - draggedPosition.y));
	}

	/**
	 * @return if the rectangle is cleared
	 */
	public boolean isUnselected()
	{
		return initialPosition == null;
	}

	/**
	 * sets initialPosition and draggedPosition is reset
	 */
	public void setInitialPosition(Point2D.Double initialPosition)
	{
		this.initialPosition = initialPosition;
		this.draggedPosition = initialPosition;
	}

	/**
	 * sets draggedPosition
	 */
	public void setDraggedPosition(Point2D.Double draggedPosition)
	{
		this.draggedPosition = draggedPosition;
	}

	/**
	 * sets initialPosition to null
	 */
	public void unselect()
	{
		this.initialPosition = null;
	}

}
