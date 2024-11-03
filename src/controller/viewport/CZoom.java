package controller.viewport;

import view.VGrid;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * controls the mouse wheel to zoom in/out
 */
public class CZoom implements MouseWheelListener
{

	private final VGrid vGrid;

	public CZoom(VGrid vGrid)
	{
		this.vGrid = vGrid;
	}

	/**
	 * zooms when mouse wheel is moved
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		vGrid.zoom(-e.getWheelRotation(), e.getX(), e.getY());
	}

}
