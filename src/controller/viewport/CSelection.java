package controller.viewport;

import model.Model;
import model.Selection;
import model.units.villagers.Villager;
import view.VGrid;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * enables drag selection of villagers
 */
public class CSelection extends MouseAdapter
{

	private final Selection selection;
	private final Model model;
	private final VGrid vGrid;

	/**
	 * constructor
	 */
	public CSelection(Model model, VGrid vGrid)
	{
		selection = model.getSelection();
		this.model = model;
		this.vGrid = vGrid;
	}

	/**
	 * when the right mouse button is pressed
	 * set the initial position of the selection
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(!SwingUtilities.isRightMouseButton(e)) return;

		selection.setInitialPosition(vGrid.pixelToPoint(e.getPoint()));
	}

	/**
	 * when the right mouse button is released
	 * clear the selection and add the villagers
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(!SwingUtilities.isRightMouseButton(e)) return;

		selection.getVillagers().clear();

		for(Villager villager : model.getVillagers())
		{
			if(selection.contains(villager))
				selection.getVillagers().add(villager);
		}

		selection.unselect();
	}

	/**
	 * when the right mouse button is dragged
	 * set the dragged position of the selection
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(!SwingUtilities.isRightMouseButton(e)) return;

		selection.setDraggedPosition(vGrid.pixelToPoint(e.getPoint()));
	}

}
