package model.cells.content;

import model.Calendar;
import model.cells.Cell;
import model.units.villagers.TM;
import view.Texture;

import java.awt.*;

public class TreeGrowing extends DestructibleContent
{

	/**
	 * Second stage of the tree
	 **/
	public TreeGrowing()
	{
		super(5, true, "tree_summer_growing.png");
	}

	/**
	 * @return the texture of the growing tree depending on the season
	 */
	@Override
	public Image getTexture()
	{
		return Texture.getImage("content/tree_" + Calendar.getSeason().name().toLowerCase() + "_growing.png");
	}

	/**
	 * Starts the growing process of the tree
	 * @param cell the cell where the tree is growing
	 **/
	public void startGrowing(Cell cell)
	{
		new Thread(() ->
		{
			final int GROWING_TIME = 60 * 1000;
			TM.sleep(GROWING_TIME, () -> {});
			cell.setContent(new Tree());
		}).start();
	}

}
