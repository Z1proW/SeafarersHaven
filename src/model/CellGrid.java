package model;

import model.cells.Cell;
import model.cells.CellGround;
import model.cells.content.*;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * the model of the grid
 */
public class CellGrid
{

	private final int width, height;
	private final Cell[][] cells;
	private final Model model;
	private final Random random = new Random();
	private Cell shipwreck = null;

	/**
	 * constructor
	 *
	 * @param width  the number of columns in the grid
	 * @param height the number of rows in the grid
	 */
	CellGrid(int width, int height, Model model)
	{
		this.width = width;
		this.height = height;
		this.model = model;

		cells = new Cell[height][width];

		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				cells[y][x] = randomCell(x, y);

		// let's place the shipwreck, lazily
		while(shipwreck == null)
		{
			int x = random.nextInt(0, width);
			int y = random.nextInt(0, height);

			// TODO: could check for nearby water
			if(cells[y][x].getGround() == CellGround.SAND)
			{
				cells[y][x].setContent(new Shipwreck());
				shipwreck = cells[y][x];
			}
		}
	}

	/**
	 * generate random cells
	 *
	 * @param x column in the grid
	 * @param y row in the grid
	 * @return the generated cell
	 */
	private Cell randomCell(int x, int y)    // TODO: procedural generation in a separate class
	{
		CellGround ground;
		double x2 = x - (width / 2. - 0.5); // centre x
		double y2 = y - (height / 2. - 0.5); // centre y

		if(x2 * x2 + y2 * y2 < width / 3. * height / 3.) // interieur du cercle de rayon sqrt(12)
            ground = CellGround.GRASS;

		else if(x2 * x2 + y2 * y2 < width / 2.5 * height / 2.5) // interieur du cercle de rayon sqrt(16)
			ground = CellGround.SAND;

		else ground = CellGround.WATER; // exterieur

		CellContent content = null;

		if(ground == CellGround.GRASS)
		{
			if(random.nextInt(10) < 2) // 20% chance
				content = new Tree();

			else if(random.nextInt(10) < 1) // 10% chance
				content = new Rock();
		}
		else if(ground == CellGround.SAND)
		{
			if(random.nextInt(50) < 1) // 2% chance
				content = new Tomb(model, x, y);
		}

		return new Cell(x, y, ground, content, model.getVillage());
	}

	public Cell getShipwreck() {
		return shipwreck;
	}

	/**
	 * @return the number of columns in the grid
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return the number of rows in the grid
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param x the x column
	 * @param y the y row
	 * @return the cell at these coordinates
	 */
	public Cell getCell(int x, int y)
	{
		return cells[y][x];
	}

	/**
	 * @param point the coordinates of the cell
	 * @return the cell at these coordinates
	 */
	public Cell getCell(Point2D.Double point)
	{
		return getCell((int) point.x, (int) point.y);
	}

	// Finding places of interest

	/**
	 * Search the nearest cell with content Celltype around the provided grid (not screen) coordinates.
	 *
	 * @return the cell, or null if not found within a sane radius.
	 * The algorithm currently searches tiles in a spiral starting from the given coordinates
	 *
	 * TODO: this could be improved
	 */
	public Cell getNearest(int x, int y, Class<?> cellType)
	{
		//	System.out.println("Start search >>>>>> " + x + "-" + y);

		if(cellIs(x, y, cellType)) return cells[y][x];

		for(int i = 1; i <= 16; i++)
		{
			x += 1;
			y += 1;

			for(int j = 0; j < 2 * i; j++)
			{
				if(cellIs(x, y, cellType)) return cells[y][x];
				x--;
			}
			for(int j = 0; j < 2 * i; j++)
			{
				if(cellIs(x, y, cellType)) return cells[y][x];
				y--;
			}
			for(int j = 0; j < 2 * i; j++)
			{
				if(cellIs(x, y, cellType)) return cells[y][x];
				x++;
			}
			for(int j = 0; j < 2 * i; j++)
			{
				if(cellIs(x, y, cellType)) return cells[y][x];
				y++;
			}
		}

		System.out.println("Not found :'(");
		return null;
	}

	/**
	 * Check if cell at specified grid coordinates has contents cellType.
	 *
	 * @return false if coordinates are out of bounds
	 */
	private boolean cellIs(int x, int y, Class<?> cellType)
	{
		//	System.out.println("Searching thing on cell " + x + "-" + y);

		if(x < 0 || y < 0 || x >= width || y >= height)
			return false;
		else
			return cellType.isInstance(cells[y][x].getContent());
	}

	/**
	 * @return true if the coordinates are within the grid
	 */
	public boolean isInBounds(int x, int y)
	{
		return x >= 0 && y >= 0 && x < width && y < height;
	}

}
