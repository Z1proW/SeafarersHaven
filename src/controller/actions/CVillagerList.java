package controller.actions;

import model.Model;
import model.units.villagers.Villager;
import view.VGrid;
import view.VVillagerList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Timer;
import java.util.TimerTask;

public class CVillagerList extends MouseAdapter
{

	private final Model model;
	private final VGrid vGrid;
	private final VVillagerList vVillagerList;

	public CVillagerList(Model model, VGrid vGrid, VVillagerList vVillagerList)
	{
		this.model = model;
		this.vGrid = vGrid;
		this.vVillagerList = vVillagerList;
	}

	/**
	 * centers the camera to villager when left clicked
	 * selects villager when right clicked
	 * selects only villager when middle clicked
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		Villager clicked = vVillagerList.getVillager(e.getPoint());

		if(clicked == null) return;

		switch(e.getButton())
		{
			case MouseEvent.BUTTON1 -> // left click
					vGrid.centerToVillager(clicked);
			case MouseEvent.BUTTON2 ->
			{
				// middle click
				model.getSelection().clear();
				model.getSelection().add(clicked);
			}
			case MouseEvent.BUTTON3 ->
			{
				// right click
				if(model.getSelection().getVillagers().contains(clicked))
					model.getSelection().remove(clicked);
				else
					model.getSelection().add(clicked);
			}
		}
	}

	/**
	 * scrolls when mouse wheel is moved
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		new Timer().schedule(new TimerTask()
		{
			int i = 0;
			int scroll = -e.getWheelRotation() * 10;

			@Override
			public void run()
			{
				if(i == 100) cancel();

				scroll = scroll * (100 - i) / 100;
				vVillagerList.scroll(scroll);

				i++;
			}
		}, 0, 20);
	}

}
