package controller;

import controller.actions.CActions;
import controller.actions.CGrid;
import controller.actions.CVillagerList;
import controller.viewport.CHover;
import controller.viewport.CPanMap;
import controller.viewport.CSelection;
import controller.viewport.CZoom;
import model.Model;
import view.VGrid;
import view.VVillagerList;
import view.View;

/**
 * main controller of the game
 */
public class Control
{

	/**
	 * creates all the controllers
	 * and adds the listeners to the view
	 */
	public Control(Model model, View view)
	{
		new CRepaint(view);

		VGrid vGrid = view.getVGrid();

		CSelection cSelection = new CSelection(model, vGrid);
		vGrid.addMouseListener(cSelection);
		vGrid.addMouseMotionListener(cSelection);

		CPanMap cPanMap = new CPanMap(vGrid);
		vGrid.addMouseListener(cPanMap);
		vGrid.addMouseMotionListener(cPanMap);

		CZoom cZoom = new CZoom(vGrid);
		vGrid.addMouseWheelListener(cZoom);

		CHover cHover = new CHover(vGrid);
		vGrid.addMouseListener(cHover);
		vGrid.addMouseWheelListener(cHover);
		vGrid.addMouseMotionListener(cHover);

		CResize cResize = new CResize(vGrid, view.getvRight(), view.getVSelectedCells(), view.getVVillagerList(), view.getVActions());
		view.addComponentListener(cResize);

		CGrid cGrid = new CGrid(model, vGrid);
		vGrid.addMouseListener(cGrid);

		new CActions(model, view.getVActions());

		VVillagerList vVillagerList = view.getVVillagerList();
		CVillagerList cVillagerList = new CVillagerList(model, vGrid, vVillagerList);
		vVillagerList.addMouseListener(cVillagerList);
		vVillagerList.addMouseWheelListener(cVillagerList);
	}

}
