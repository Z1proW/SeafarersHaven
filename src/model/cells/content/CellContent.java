package model.cells.content;

import view.Texture;

import java.awt.*;

public abstract class CellContent
{

	private final boolean walkable;
	private final Image texture;

	/**
	 * Constructor CellContent
	 *
	 * @param walkable if the unit can walk in that cellcontent
	 * @param fileName of the sprite
	 **/
	public CellContent(boolean walkable, String fileName)
	{
		this.walkable = walkable;
		this.texture = Texture.getImage("content/" + fileName);
	}

	/**
	 * @return true if the unit can walk in that cellcontent
	 **/
	public boolean isWalkable()
	{
		return walkable;
	}

	/**
	 * @return the sprite of the cellcontent
	 **/
	public Image getTexture()
	{
		return texture;
	}

}
