package view;

import model.CellGrid;
import model.Model;
import model.cells.Cell;
import model.units.Unit;
import model.units.enemies.Enemy;
import model.units.villagers.Villager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * the view of the world map
 */
public class VGrid extends JPanel
{

	/**
	 * colors of the health bar
	 */
	private static final Color
			HP_COLOR = Color.GREEN,
			NO_HP_COLOR = new Color(100, 0, 0);

	private final Model model;
	private final CellGrid cellGrid;
	private final VSelection vSelection;
	private final ArrayList<Villager> villagers;
	private final ArrayList<Enemy> enemies;
	private final Point position;
	private double vWidth;
	private double vHeight;
	private int cellSize;
	private Cell hoveredCell;
	private Villager targetVillager;

	/**
	 * constructor
	 *
	 * @param vWidth  the initial size of the viewport
	 * @param vHeight the initial size of the viewport
	 */
	VGrid(Model model, int vWidth, int vHeight)
	{
		this.vWidth = vWidth;
		this.vHeight = vHeight;
		cellSize = (int) ((Toolkit.getDefaultToolkit().getScreenSize().height * 0.8) / vHeight);

		this.model = model;
		cellGrid = model.getGrid();
		vSelection = new VSelection(model.getSelection(), this);

		villagers = model.getVillagers();
		enemies = model.getEnemies();

		Villager captain = villagers.get(0);
		position = new Point((int) (captain.getX() - vWidth / 2) * cellSize, (int) (captain.getY() - vHeight / 2) * cellSize);

		setPreferredSize(new Dimension(vWidth * cellSize, vHeight * cellSize));
		setSize(getPreferredSize());

		setBackground(Color.WHITE);
	}

	/**
	 * @return the width or height of the cells in pixels
	 */
	public int getCellSize()
	{
		return cellSize;
	}

	/**
	 * @return the position of the top left of the screen
	 */
	public Point2D.Double getPosition()
	{
		return new Point2D.Double(
				((double) position.x) / cellSize,
				((double) position.y) / cellSize);
	}

	/**
	 * @param pixel the pixel
	 * @return the cell at the pixel
	 */
	public Cell getCell(Point pixel)
	{
		return cellGrid.getCell((pixel.x + position.x) / cellSize, (pixel.y + position.y) / cellSize);
	}

	/**
	 * updates the hovered cell
	 *
	 * @param hoveredCell the hovered cell
	 */
	public void setHoveredCell(Cell hoveredCell)
	{
		this.hoveredCell = hoveredCell;
	}

	/**
	 * moves the viewport positione
	 *
	 * @param dx the x coordinate of the distance to be moved in pixels
	 * @param dy the y coordinate of the distance to be moved in pixels
	 */
	public void movePosition(int dx, int dy)
	{
		position.x += dx;
		position.y += dy;

		moveInBounds();
	}

	/**
	 * moves the viewport back in bounds if it is not
	 */
	private void moveInBounds()
	{
		if(position.x < 0) position.x = 0;
		if(position.y < 0) position.y = 0;

		int maxX = (int) ((cellGrid.getWidth() - vWidth) * cellSize);
		int maxY = (int) ((cellGrid.getHeight() - vHeight) * cellSize);

		if(position.x > maxX) position.x = maxX;
		if(position.y > maxY) position.y = maxY;
	}

	/**
	 * zooms in or out the viewport
	 *
	 * @param dz the zoom offset in pixels
	 * @param mouseX the x coordinate of the mouse in pixels
	 * @param mouseY the y coordinate of the mouse in pixels
	 */
	public void zoom(int dz, int mouseX, int mouseY)
	{
		// zoom (in/out) a tenth of a cell
		dz = dz * cellSize / 10;

		// move position to mouse
		position.x += mouseX;
		position.y += mouseY;

		// position coordinates in cells
		double posX = position.x / (double) cellSize;
		double posY = position.y / (double) cellSize;

		// adjust cell size based on zoom delta
		cellSize += dz;

		// limit cell size to keep a reasonable number of cells visible
		final int minVisibleCells = 10, maxVisibleCells = 50;

		if(cellSize * minVisibleCells > getHeight()) cellSize = getHeight() / minVisibleCells;
		else if(cellSize * maxVisibleCells < getHeight()) cellSize = getHeight() / maxVisibleCells;

		// update dimensions of the grid
		vWidth = (double) getWidth() / cellSize;
		vHeight = (double) getHeight() / cellSize;

		// put position to old mouse position
		position.x = (int) (posX * cellSize);
		position.y = (int) (posY * cellSize);

		// move position back to top left of the screen
		position.x -= mouseX;
		position.y -= mouseY;

		// fixes unzooming causing showing outside of map
		moveInBounds();
	}

	/**
	 * resizes the viewport when window is resized
	 *
	 * @param width  the width of the window
	 * @param height the height of the window
	 */
	public void onResize(int width, int height)
	{
		double newWidth = (double) width / cellSize;
		double newHeight = (double) height / cellSize;

		// move position to center
		position.x += vWidth * cellSize / 2;
		position.y += vHeight * cellSize / 2;

		// position coordinates in cells
		double posX = position.x / (double) cellSize;
		double posY = position.y / (double) cellSize;

		// update dimensions of the grid
		vWidth = newWidth;
		vHeight = newHeight;

		// put position in the center using old position
		position.x = (int) (posX * cellSize);
		position.y = (int) (posY * cellSize);

		// move position back to top left of the screen
		position.x -= (vWidth * cellSize) / 2.0;
		position.y -= (vHeight * cellSize) / 2.0;

		setPreferredSize(new Dimension(width, height));
		setSize(getPreferredSize());

		// fixes resizing window showing outside of map
		moveInBounds();
	}

	/**
	 * draws the map
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		drawCellsGround(g);
		drawCellsContent(g);
		drawUnits(g);
		vSelection.drawRectangle(g);
		drawDayNumber((Graphics2D) g);
		drawShipWreckCompletionBar((Graphics2D) g);
	}

	/**
	 * draws the hp bar of the ship wreck
	 */
	private void drawShipWreckCompletionBar(Graphics2D g)
	{
		if(model.getShipWreck() == null) return;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Completion text
		g.setFont(new Font("Arial", Font.BOLD, 20));
		String text = "Shipwreck";
		int textWidth = g.getFontMetrics().stringWidth(text);
		int textHeight = g.getFontMetrics().getHeight();

		final int margin = 10,
				barTextY = 25,
				barWidth = 200,
				barX = getWidth() - barWidth - margin,
				barY = barTextY + (int) g.getFontMetrics().getStringBounds(text, g).getY(),
				barTextX = barX - textWidth - margin;

		// draw transparent background
		g.setColor(new Color(0, 0, 0, 127));
		g.fillRect(barTextX - margin, barY - margin, textWidth + barWidth + margin * 3, textHeight + margin * 2);

		// draw text
		g.setColor(Color.WHITE);
		g.drawString(text, barTextX, barTextY);

		// Completion background
		g.setColor(Color.BLACK);
		g.fillRect(barX, barY, barWidth, textHeight);

		// Completion foreground
		g.setColor(Color.RED);
		g.fillRect(barX, barY, (int) (model.getShipWreck().getPercentComplete() * barWidth), textHeight);
	}

	/**
	 * draws the day number
	 */
	private void drawDayNumber(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		String text = "Day " + model.getCalendar().getDay();
		g.setFont(new Font("Arial", Font.BOLD, 20));

		// draw tranlucent background
		g.setColor(new Color(0, 0, 0, 127));
		g.fillRect(0, 0, g.getFontMetrics().stringWidth(text) + 20, 35);

		// draw text
		g.setColor(Color.WHITE);
		g.drawString(text, 10, 25);
	}

	/**
	 * draws the visible ground of the map
	 */
	private void drawCellsGround(Graphics g)
	{
		forEachVisibleCell(c -> drawGround(g, c));
	}

	/**
	 * draws the visible content of the cells
	 */
	private void drawCellsContent(Graphics g)
	{
		forEachVisibleCell(c ->
		{
			if(!c.isEmpty())
				drawContent(g, c);
		});
	}

	/**
	 * draws the content of the cell
	 *
	 * @param c the cell which content is going to be drawn
	 */
	private void drawContent(Graphics g, Cell c)
	{
		Image img = c.getContent().getTexture();

		if(c == hoveredCell)
		{
			img = brightenImage(img);
		}

		int width = cellSize;
		int height = width * img.getHeight(null) / img.getWidth(null);
		int x = c.getX() * cellSize - position.x;
		int y = c.getY() * cellSize - position.y + cellSize - height;

		g.drawImage(img, x, y, width, height, null);
	}

	/**
	 * draws the visible villagers
	 */
	private void drawUnits(Graphics g)
	{
		synchronized(villagers)
		{
			for(Villager villager : villagers)
			{
				drawUnit(g, villager);
			}
		}

		synchronized(enemies)
		{
			for(Enemy enemy : enemies)
			{
				drawUnit(g, enemy);
			}
		}
	}

	/**
	 * draws the unit
	 * @param unit the unit to draw
	 */
	private void drawUnit(Graphics g, Unit unit)
	{
		if(!unit.isAlive()) return; // TODO: necesary ?

		Image img = unit.getTexture();
		int width = cellSize;
		int height = width * img.getHeight(null) / img.getWidth(null);

		// position in pixels translated in the view
		int x = (int) (unit.getX() * cellSize - position.x);
		int y = (int) (unit.getY() * cellSize - position.y);

		// if not visible don't draw
		if(x + width < 0 || vWidth * cellSize < x
		|| y + height < 0 || vHeight * cellSize < y)
			return;

		// center horizontally
		x -= width / 2;

		// bottom align (position = feet)
		y -= height;

		g.drawImage(img, x, y, width, height, null);
	}

	/**
	 * do the action on every visible cell
	 *
	 * @param action the action to be performed for the cells
	 */
	public void forEachVisibleCell(Consumer<Cell> action)
	{
		Objects.requireNonNull(action);

		for(int x = position.x / cellSize; x <= (double) position.x / cellSize + vWidth; x++)
			for(int y = position.y / cellSize; y <= (double) position.y / cellSize + vHeight; y++)
			{
				if(0 <= x && x < cellGrid.getWidth()
						&& 0 <= y && y < cellGrid.getHeight())
					action.accept(cellGrid.getCell(x, y));
			}
	}

	/**
	 * draws the ground of the cell
	 *
	 * @param c the cell which ground is going to be drawn
	 */
	private void drawGround(Graphics g, Cell c)
	{
		Image img = c.getGround().getTexture();

		if(c == hoveredCell)
		{
			img = brightenImage(img);
		}

		int x = c.getX() * cellSize - position.x;
		int y = c.getY() * cellSize - position.y;

		g.drawImage(img, x, y, cellSize, cellSize, null);
	}

	/**
	 * @param img the image to be brightened
	 * @return the image brightened by a value of 80
	 */
	private Image brightenImage(Image img)
	{
		if(img.getWidth(null) <= 0 || img.getHeight(null) <= 0) return img;

		// convert Image to bufferedImage
		BufferedImage bImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bImg.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// equivalent to bImg.forEachPixel(p -> p.rgb = p.rgb * 1 + 80);
		RescaleOp rescaleOp = new RescaleOp(1, 80, null);
		rescaleOp.filter(bImg, bImg);

		return bImg;
	}

	/**
	 * @param pixel the pixel to be converted to cell fraction
	 * @return the position of the pixel in cells
	 */
	public Point2D.Double pixelToPoint(Point pixel)
	{
		return new Point2D.Double((double) (pixel.x + position.x) / cellSize, (double) (pixel.y + position.y) / cellSize);
	}

	/**
	 * @param clicked the villager to center the view on
	 */
	public void centerToVillager(Villager clicked)
	{
		Point targetPoint = new Point();
		targetPoint.x = (int) (clicked.getX() * cellSize - vWidth * cellSize / 2.0);
		targetPoint.y = (int) (clicked.getY() * cellSize - vHeight * cellSize / 2.0);

		targetVillager = clicked;

		new Timer().schedule(new TimerTask()
		{
			int i = 0;

			@Override
			public void run()
			{
				if(i == 30 || targetVillager != clicked)
					cancel();

				position.x += (targetPoint.x - position.x) / 10;
				position.y += (targetPoint.y - position.y) / 10;

				i++;
			}
		}, 0, 10);

		moveInBounds();
	}

}
