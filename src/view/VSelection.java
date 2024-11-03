package view;

import model.Selection;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * view for the selection rectangle
 */
class VSelection
{

	private static final Color selectionColor = new Color(0, 100, 255, 100);

	private final Selection selection;
	private final VGrid vGrid;

	/**
	 * constructor
	 *
	 * @param selection the selection of villagers
	 */
	VSelection(Selection selection, VGrid vGrid)
	{
		this.selection = selection;
		this.vGrid = vGrid;
	}

	/**
	 * draws the selection rectangle
	 */
	void drawRectangle(Graphics g)
	{
		if(selection.isUnselected()) return;

		g.setColor(selectionColor);

		Rectangle2D.Double rectangle = selection.getRectangle();
		double cellSize = vGrid.getCellSize();

		int x = (int) ((rectangle.x - vGrid.getPosition().x) * cellSize);
		int y = (int) ((rectangle.y - vGrid.getPosition().y) * cellSize);
		int width = (int) (rectangle.width * cellSize);
		int height = (int) (rectangle.height * cellSize);

		g.fillRect(x, y, width, height);
	}

}
