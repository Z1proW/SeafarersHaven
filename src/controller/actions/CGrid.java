package controller.actions;

import model.Model;
import model.cells.Cell;
import model.cells.content.CellContent;
import model.cells.content.Shipwreck;
import model.cells.content.Tree;
import model.cells.content.TreeTrunk;
import model.cells.content.buildings.ConstructionZone;
import model.cells.content.buildings.Storage;
import model.units.villagers.Villager;
import model.units.villagers.VillagerAction;
import model.units.villagers.VillagerType;
import view.VGrid;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static model.units.villagers.VillagerAction.*;

public class CGrid extends MouseAdapter
{

	private final Model model;
	private final VGrid vGrid;

	public CGrid(Model model, VGrid vGrid)
	{
		this.model = model;
		this.vGrid = vGrid;
	}

	/**
	 * Sets the selected cell and the action of the selected villagers
	 * when the user clicks on the grid.
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// clic gauche
		if(e.getButton() != MouseEvent.BUTTON1) return;

		ArrayList<Villager> selectedVillagers = model.getSelection().getVillagers();

		Cell cell = vGrid.getCell(e.getPoint());
		model.setSelectedCell(cell);

		Point2D.Double target = vGrid.pixelToPoint(e.getPoint());

		CellContent content = cell.getContent();

		VillagerAction villagerAction;
		if(content instanceof Tree) villagerAction = FELLING;
		else if(content instanceof Storage) villagerAction = STORING;
		else if(content instanceof ConstructionZone) villagerAction = BUILDING;
		else if(content instanceof TreeTrunk) villagerAction = UPROOTING;
		else if(content instanceof Shipwreck) villagerAction = REPAIRING;
		else villagerAction = MOVING;

		selectedVillagers.forEach(villager ->
		{
			if(villager.getType() == VillagerType.FARMER && villagerAction == MOVING)
				villager.getManager().setMode(PLANTING, target.x, target.y);
			else
				villager.getManager().setMode(villagerAction, target.x, target.y);
		});
	}

}
