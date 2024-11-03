package controller.viewport;

import view.VGrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class CHover extends MouseAdapter
{

	private final VGrid vGrid;

	public CHover(VGrid vGrid)
	{
		this.vGrid = vGrid;
	}

	/**
	 * when the mouse is released sets the currently hovered cell
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		vGrid.setHoveredCell(vGrid.getCell(e.getPoint()));
	}

	/**
	 * when the mouse exits the window clears the hovered cell
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		vGrid.setHoveredCell(null);
	}

	/**
	 * when zooming sets the currently hovered cell
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		vGrid.setHoveredCell(vGrid.getCell(e.getPoint()));
	}

	/**
	 * when the mouse is dragged clears the hovered cell
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		vGrid.setHoveredCell(null);
	}

	/**
	 * when the mouse is moved sets the currently hovered cell
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		vGrid.setHoveredCell(vGrid.getCell(e.getPoint()));
	}

}
