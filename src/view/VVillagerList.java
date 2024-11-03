package view;

import model.Model;
import model.units.villagers.Villager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VVillagerList extends JPanel // TODO: make this resizable and scrollable (JScrollPane) and add a search bar (JTextField)
{

	private final Model model;
	private int position;

	private final int imgHeight = 30, margin = 20;

	VVillagerList(Model model)
	{
		this.model = model;
		setBackground(Color.WHITE);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		drawVillagerList((Graphics2D) g);
	}

	/**
	 * Draws the list of villagers
	 */
	private void drawVillagerList(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int line = 0;

		synchronized(model.getVillagers())
		{
			for(Villager villager : model.getVillagers())
			{
				Image icon = villager.getType().getIcon();
				int imgWidth = imgHeight * icon.getWidth(null) / icon.getHeight(null);

				// blue highlight if selected
				if(model.getSelection().getVillagers().contains(villager))
				{
					g.setColor(new Color(0, 0, 255, 50));
					g.fillRect(0, position + imgHeight * line, getWidth(), imgHeight);
				}

				// draw the villager icon
				g.drawImage(icon, imgWidth / 2, position + imgHeight * line, imgWidth, imgHeight, null);

				g.setFont(new Font("Calibri", Font.BOLD, 14));
				g.setColor(Color.BLACK);

				String name = villager.getType().name();

				// draw the villager name
				g.drawString(name, 2 * margin + imgWidth, position + imgHeight * line + (int) (g.getFontMetrics().getStringBounds(name, g).getHeight()) / 2 + imgHeight / 2);

				// draw the villager health
				// TODO draw the villager health bar

				// draw the villager action icon
				Image actionIcon = villager.getManager().getMode().getIcon();
				int actionImgWidth = imgHeight * actionIcon.getWidth(null) / actionIcon.getHeight(null);
				g.drawImage(actionIcon, getWidth() - actionImgWidth - margin, position + imgHeight * line, actionImgWidth, imgHeight, null);

				line++;
			}
		}
	}

	/**
	 * Gets the villager at the given point
	 * @param point the point to check
	 * @return the villager at the given point, or null if there is no villager at the given point
	 */
	public Villager getVillager(Point point)
	{
		ArrayList<Villager> villagers = model.getVillagers();

		int line = 0;

		for(Villager villager : villagers)
		{
			if(point.x > margin
			&& point.y - position > margin + imgHeight * line - imgHeight / 2
			&& point.y - position < margin + imgHeight * line + imgHeight / 2)
				return villager;

			line++;
		}

		return null;
	}

	/**
	 * @param dy the number of pixels to scroll
	 */
	public void scroll(int dy)
	{
		// don't scroll if the list is too short to scroll
		if(model.getVillagers().size() * imgHeight < getHeight())
			position = 0;
		// don't scroll if the top of the list is visible
		else if(position + dy > 0)
			position = 0;
		// don't scroll if the bottom of the list is visible
		else if(position + dy < getHeight() - model.getVillagers().size() * imgHeight)
			position = getHeight() - model.getVillagers().size() * imgHeight;
		else // scroll
			position += dy;
	}

}
