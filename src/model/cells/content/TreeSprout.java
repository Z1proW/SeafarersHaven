package model.cells.content;

import model.Calendar;
import model.cells.Cell;
import model.units.villagers.TM;
import view.Texture;

import java.awt.*;

public class TreeSprout extends DestructibleContent
{

	/**
	 * First stage of the tree
	 **/
	public TreeSprout()
	{
		super(5, true, "tree_summer_sprout.png");
	}

	/**
	 * @return the texture of the sprout depending on the season
	 */
	@Override
	public Image getTexture()
	{
		return Texture.getImage("content/tree_" + Calendar.getSeason().name().toLowerCase() + "_sprout.png");
	}

	/**
	 * Start the growing of the tree
	 * @param cell the cell where the tree is
	 */
	public void startGrowing(Cell cell)
	{
		new Thread(() ->
		{
			final int GROWING_TIME = 60 * 1000;
			TM.sleep(GROWING_TIME, () -> {});
			TreeGrowing treeGrowing = new TreeGrowing();
			treeGrowing.startGrowing(cell);
			cell.setContent(treeGrowing);
		}).start();
	}

}
