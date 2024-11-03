package view;

import model.Model;
import model.Resource;
import model.cells.content.CellContent;
import model.cells.content.DestructibleContent;
import model.cells.content.buildings.ConstructionZone;
import model.cells.content.buildings.Storage;

import javax.swing.*;
import java.awt.*;

public class VSelectedCell extends JPanel
{

	/**
	 * colors of the health bar
	 */
	private static final Color
			HP_COLOR = Color.GREEN,
			NO_HP_COLOR = new Color(100, 0, 0),
			CP_COLOR = Color.BLUE,
			NO_CP_COLOR = NO_HP_COLOR;

	private final Model model;
	private final int width;
	private final int margin = 10;

	VSelectedCell(Model model, int width, int height)
	{
		this.model = model;
		this.width = width;
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		drawSelectedCell((Graphics2D) g);
	}

	/**
	 * Draws the selected cell
	 */
	private void drawSelectedCell(Graphics2D g)
	{
		if(model.getSelectedCell() == null) return;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		CellContent content = model.getSelectedCell().getContent();
        if(content == null)return;
		Image img = content.getTexture();

		int imgWidth = width / 3, imgHeight = imgWidth * img.getHeight(null) / img.getWidth(null);

		g.drawImage(img, 0, 0, imgWidth, imgHeight, null);

		String name = content.getClass().getSimpleName();

		g.setFont(new Font("Calibri", Font.BOLD, 14));
		g.setColor(Color.BLACK);
		g.drawString(name, 2 * width / 3 - g.getFontMetrics().stringWidth(name) / 2, margin + 10);

		drawHPBar(g, content);
		drawResources(g, content);
        drawConstructionBuilding(g, content);
	}

	/**
	 * Draws the health bar of the selected cell
	 */
	private void drawHPBar(Graphics g, CellContent content)
	{
		if(!(content instanceof DestructibleContent destructibleContent)) return;

		int posX = width / 3 + 20;
		int posY = margin + 30;

		// HP text
		g.setColor(Color.BLACK);
		g.drawString("HP:", posX, posY);

		// HP background
		g.setColor(NO_HP_COLOR);
		g.fillRect(posX + 30, posY - 15, width / 2, 20);

		// HP foreground
		float currentHealth = destructibleContent.getHealth();
		float maxHealth = destructibleContent.getMaxHealth();
		float precision = currentHealth / maxHealth;
		int hpWidth = (int) (width / 2 * precision);

		g.setColor(HP_COLOR);
		g.fillRect(posX + 30, posY - 15, hpWidth, 20);
	}

	/**
	 * Draws the resources of the selected cell
	 */
	private void drawResources(Graphics2D g, CellContent content)
	{
		if(!(content instanceof Storage storage)) return; // TODO same for constructionzone

		int posX = width / 3 + 20;
		int posY = margin + 60;

		g.setColor(Color.BLACK);

		g.drawString("CP:", posX, posY);

		int line = 0;

		for(Resource resource : Resource.values())
		{
			g.drawString(resource.toString() + ": " + storage.getResourceNumber(resource), posX + 20, posY + 20 * ++line);
		}

		// draw the capacity bar
		int totalStock = 0;
		for(Resource resource : Resource.values()) totalStock += storage.getResourceNumber(resource);
		int maxStock = storage.getMaxStock();
		int resourceWidth = (int) (width / 2 * (totalStock / (float) maxStock));
		g.setColor(NO_CP_COLOR);
		g.fillRect(posX + 30 + width / 2 - resourceWidth, posY - 15, resourceWidth, 20);

		// background of the bar
		g.setColor(CP_COLOR);
		g.fillRect(posX + 30, posY - 15, width / 2 - resourceWidth, 20);
	}

	/**
	 * Draws the construction building of the selected cell
	 */
    private void drawConstructionBuilding(Graphics2D g, CellContent content)
    {
        if(!(content instanceof ConstructionZone constructionZone)) return;

        int posX = width / 3 + 20;
        int posY = margin + 60;

        g.setColor(Color.BLACK);

        g.drawString(constructionZone.getBuildingType().toString(), posX, posY);
    }

}
