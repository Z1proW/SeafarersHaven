package controller.actions;

import model.Model;
import model.cells.Cell;
import model.cells.content.buildings.BuildingType;
import model.cells.content.buildings.ConstructionZone;
import model.units.villagers.VillagerAction;
import view.VActions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static model.units.villagers.VillagerAction.*;

/**
 * Controller for the actions panel.
 */
public class CActions
{

	private final Model model;
	private final ArrayList<JToggleButton> buttons = new ArrayList<>();
	private final ArrayList<JButton> buildingButtons = new ArrayList<>();

	/**
	 * Creates a new controller for the actions panel.
	 * @param model The model.
	 * @param vActions The actions panel.
	 */
	public CActions(Model model, VActions vActions)
	{
		this.model = model;

		VillagerAction[] possibleActions = {FELLING, HARVESTING, FISHING, BUILDING};
		for(VillagerAction action : possibleActions)
		{
			JToggleButton button = new JToggleButton();
			button.setFocusable(false);

			int size = 40; // TODO get size from somewhere else

			Image image = action.getIcon();
			image = image.getScaledInstance(size, size, Image.SCALE_FAST);
			button.setIcon(new ImageIcon(image));

			button.setToolTipText(action.name().toLowerCase());

			button.setSize(size, size);

			if(action == BUILDING)
			{
				button.addActionListener(e ->
				{
					for(JButton b : buildingButtons)
						b.setVisible(button.isSelected());
				});
			}
			else
			{
				button.addActionListener(e ->
				{
					if(button.isSelected())
					{
						for(JToggleButton b : buttons)
							if(b != button)
								b.setSelected(false);

						setMode(action);
					}
					else setMode(IDLE);
				});
			}

			vActions.add(button);
			buttons.add(button);
		}

		BuildingType[] possibleBuildings = BuildingType.values();
		for(BuildingType building : possibleBuildings)
		{
			JButton button = new JButton();
			button.setFocusable(false);

			int size = 40; // TODO get size from somewhere else

			Image image = building.getTexture();
			image = image.getScaledInstance(size, size, Image.SCALE_FAST);
			button.setIcon(new ImageIcon(image));

			button.setToolTipText(building.name().toLowerCase());

			button.setSize(size, size);

			button.setVisible(false);

			button.addActionListener(e ->
			{
				for(JToggleButton b : buttons)
					b.setSelected(false);

				model.setSelectedCell(null);

				new Timer().schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						Cell cell = model.getSelectedCell();

						if(cell != null && cell.isEmpty() && cell.isWalkable())
						{
							switch(building)
							{
								case HOUSE -> cell.setContent(new ConstructionZone(BuildingType.HOUSE));
								case STORAGE -> cell.setContent(new ConstructionZone(BuildingType.STORAGE));
								case TAVERN -> cell.setContent(new ConstructionZone(BuildingType.TAVERN));
							}

							SwingUtilities.invokeLater(() ->
									model.getSelection().getVillagers().forEach(villager ->
											villager.getManager().setMode(BUILDING, cell.getX(), cell.getY())));

							for(JButton b : buildingButtons)
								b.setVisible(false);

							cancel();
						}
					}
				}, 0, 100);
			});

			vActions.add(button);
			buildingButtons.add(button);
		}
	}

	/**
	 * Sets the mode of all villagers.
	 * @param mode The mode to set.
	 */
	private void setMode(VillagerAction mode)
	{
		model.getSelection().getVillagers().forEach(villager -> villager.getManager().setMode(mode));
	}

}
