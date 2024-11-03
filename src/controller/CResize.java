package controller;

import view.VActions;
import view.VGrid;
import view.VSelectedCell;
import view.VVillagerList;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * controls the resizing of the main window
 */
public class CResize extends ComponentAdapter
{

	private final VGrid vGrid;
	private final JPanel vRight;
	private final VSelectedCell vSelectedCell;
	private final VVillagerList vVillagerList;
	private final VActions vActions;

	public CResize(VGrid vGrid, JPanel vRight, VSelectedCell vSelectedCell, VVillagerList vVillagerList, VActions vActions)
	{
		this.vGrid = vGrid;
		this.vRight = vRight;
		this.vSelectedCell = vSelectedCell;
		this.vVillagerList = vVillagerList;
		this.vActions = vActions;
	}

	/**
	 * updates the vGrid to change viewport size when the window is resized
	 */
	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = e.getComponent().getWidth();
		int height = e.getComponent().getHeight();

		vGrid.onResize(width * 2 / 3, height);
		vRight.setBounds(width * 2 / 3, 0, width / 3, height);
		vSelectedCell.setBounds(0, 0, width / 3, height / 4);
		vVillagerList.setBounds(0, height / 4, width / 3, height / 2);
		vActions.setBounds(0, height * 3 / 4, width / 3, height / 4);
	}

}
