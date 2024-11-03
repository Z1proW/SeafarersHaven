package model.cells;

import model.Village;
import model.cells.content.CellContent;
import model.cells.content.buildings.Building;

public class Cell
{

	private final int x, y;
	private final CellGround ground;
	private CellContent content;
	private final Village village;

	public Cell(int x, int y, CellGround ground, CellContent content, Village village)
	{
		this.x = x;
		this.y = y;
		this.ground = ground;
		this.content = content;
		this.village = village;

		if(content instanceof Building) village.registerCell(this);
	}

	/**
	 * @return the cell's x coordinate
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the cell's y coordinate
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @return the cell's ground
	 */
	public CellGround getGround()
	{
		return ground;
	}

	/**
	 * @return the cell's content
	 */
	public CellContent getContent()
	{
		return content;
	}

	/**
	 * Set the cell's content to something that is not a building part of the village.
	 * If a building was previously there, it will be unregistered from the village.
	 *
	 * @param content the cell's new content
	 */
	public void setContent(CellContent content)
	{
		if(this.content instanceof Building)
			village.unregisterCell(this);

		if(content instanceof Building)
			village.registerCell(this);

		this.content = content;
	}

	/**
	 * @return if the cell is empty or not
	 */
	public boolean isEmpty()
	{
		return content == null;
	}

	/**
	 * @return if the cell is a water cell or not
	 */
	public boolean isSailable()
	{
		return ground == CellGround.WATER && isWalkable();
	}

	/**
	 * @return if the cell is walkable or not
	 */
	public boolean isWalkable()
	{
		return ground.isWalkable() && (content == null || content.isWalkable());
	}

	@Override
	public String toString()
	{
		return "cell(" + x + "," + y + ") -> " + content;
	}

}
