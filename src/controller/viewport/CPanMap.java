package controller.viewport;

import view.VGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * enables the panning of the viewport
 */
public class CPanMap extends MouseAdapter
{

	private final VGrid vGrid;

	private Point previousPosition;

	public CPanMap(VGrid vGrid)
	{
		this.vGrid = vGrid;
	}

	/**
	 * when the left mouse is pressed:
	 * sets the previous position to the mouse position
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(!SwingUtilities.isLeftMouseButton(e)) return;

		previousPosition = e.getPoint();
	}

	/**
	 * when the mouse is dragged moves the viewport
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(!SwingUtilities.isLeftMouseButton(e)) return;

		Point draggedPosition = e.getPoint();

		vGrid.movePosition(
				previousPosition.x - draggedPosition.x,
				previousPosition.y - draggedPosition.y);

		previousPosition = draggedPosition;
	}

}
