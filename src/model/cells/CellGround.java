package model.cells;

import model.Calendar;
import view.Texture;

import java.awt.*;

/**
 * all the different possible ground tiles
 */
public enum CellGround
{

	GRASS("grass.png", true),
	SAND("sand.png", true),
	WATER("water.gif", false);

	private final Image texture;
	private final boolean walkable;

	/**
	 * constructor
	 *
	 * @param fileName the file name of the texture including the file extension
	 * @param walkable indicates whether or not villagers are able to walk on the cell
	 */
	CellGround(String fileName, boolean walkable)
	{
		texture = Texture.getImage("ground/" + fileName);
		this.walkable = walkable;
	}

	/**
	 * @return the ground texture
	 */
	public Image getTexture()
	{
		if(this == WATER)
			return Texture.getGif("ground/water.gif");

		if(this == GRASS)
			return Texture.getImage("ground/grass_" + Calendar.getSeason().name().toLowerCase() + ".png");

		return texture;
	}

	/**
	 * @return indicates whether or not the villagers are able to walk on the cell
	 */
	public boolean isWalkable()
	{
		return walkable;
	}

}
