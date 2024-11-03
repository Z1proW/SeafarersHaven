package view;

import model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * the main view that displays a window
 * contains two panels for the grid and the actions
 */
public class View extends JFrame
{

	private final VGrid vGrid;
	private final JPanel vRight;
	private final VSelectedCell vSelectedCell;
	private final VVillagerList vVillagerList;
	private final VActions vActions;

	/**
	 * constructor
	 *
	 * @param model the model associated
	 */
	public View(Model model)
	{
		vGrid = new VGrid(model, 20, 20);
		vSelectedCell = new VSelectedCell(model, vGrid.getWidth() / 2, vGrid.getHeight() / 4);
		vVillagerList = new VVillagerList(model);
		vActions = new VActions();

		setLayout(new BorderLayout());
		add(vGrid, BorderLayout.WEST);

		vRight = new JPanel(new BorderLayout());

		vRight.add(vSelectedCell, BorderLayout.NORTH);
		vRight.add(vVillagerList, BorderLayout.CENTER);
		vRight.add(vActions, BorderLayout.SOUTH);

		add(vRight, BorderLayout.EAST);

		setMinimumSize(new Dimension(600, 500));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null); // centre la fenêtre
		setVisible(true);
	}

	/**
	 * @return the panel of the grid
	 */
	public VGrid getVGrid()
	{
		return vGrid;
	}

	/**
	 * @return the right panel
	 */
	public JPanel getvRight()
	{
		return vRight;
	}

	/**
	 * @return the panel of the actions
	 */
	public VSelectedCell getVSelectedCells()
	{
		return vSelectedCell;
	}

	/**
	 * @return the panel of the villager list
	 */
	public VVillagerList getVVillagerList()
	{
		return vVillagerList;
	}

	public VActions getVActions()
	{
		return vActions;
	}

	@Override
	public void repaint()
	{
		super.repaint();
		vGrid.repaint();
		vSelectedCell.repaint();
		vVillagerList.repaint();
		vActions.repaint();
	}

}
