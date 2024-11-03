package model.cells.content;

import model.Calendar;
import view.Texture;

import java.awt.*;

public class Tree extends DestructibleContent
{

	public Tree()
	{
		super(40, true, "tree_summer.png");
	}

	/**
	 * @return the texture of the tree depending on the season
	 */
	@Override
	public Image getTexture()
	{
		return Texture.getImage("content/tree_" + Calendar.getSeason().name().toLowerCase() + ".png");
	}

}
