package model;

import model.cells.Cell;
import model.cells.content.buildings.Storage;

import java.util.HashSet;
import java.util.function.Predicate;

/**
 * This class contains information about the buildings in the village and allows to
 * get aggregate information on them
 */
public class Village
{

	private final HashSet<Cell> buildings = new HashSet<>();

	/**
	 * Register a building in the village.
	 * Used to provide "nearest building" information to villagers.
	 *
	 * @param cell the cell containing the building.
	 */
	public void registerCell(Cell cell)
	{
		System.out.println(">>> BUILDING REGISTERED: " + cell.getContent());
		buildings.add(cell);
	}

	/**
	 * Unegister a building from the village.
	 * The cell will no longer be tested when searching for buildings.
	 *
	 * @param cell the cell containing the building.
	 */
	public void unregisterCell(Cell cell)
	{
		buildings.remove(cell);
	}

	/**
	 * Find the nearest non-full storage room
	 *
	 * @param x position to search around (in cells)
	 * @param y position to search around (in cells)
	 * @return the nearest selected cell, or null if not found
	 */
	public Cell findNonFullStorage(int x, int y)
	{
		System.out.println("Find non-full storage");
		return findNearest(x, y, Storage.class, c -> !((Storage) c.getContent()).isFull());
	}

	/**
	 * Find the nearest storage with some of the specified resource
	 *
	 * @param res the resource to search for
	 * @return the nearest selected cell, or null if not found
	 */
	public Cell findStorageWith(int x, int y, Resource res)
	{
		System.out.println("Find storage with resource");
		return findNearest(x, y, Storage.class, c -> ((Storage) c.getContent()).getResourceNumber(res) > 10);
	}

	/**
	 * Find the nearest cell containing the given type of content
	 *
	 * @param x        position to search around (in cells)
	 * @param y        position to search around (in cells)
	 * @param cellType cell content to search for
	 * @return the nearest selected cell, or null if not found
	 */
	public Cell findNearest(int x, int y, Class<?> cellType)
	{
		System.out.println("Find nearest " + cellType);
		return findNearest(x, y, cellType, c -> true);
	}

	/**
	 * Find the nearest cell containing the given type of content and repecting the given predicate
	 *
	 * @param x        position to search around (in cells)
	 * @param y        position to search around (in cells)
	 * @param celltype cell content to search for
	 * @param test     predicate
	 * @return the nearest selected cell, or null if not found
	 */
	private Cell findNearest(int x, int y, Class<?> celltype, Predicate<Cell> test)
	{
	//	System.out.println("Let's find a building ! Type: " + celltype);
	//	System.out.println("In: " + buildings);

		double minDist = Double.MAX_VALUE;
		Cell selectedCell = null;

		for(Cell cell : buildings)
		{
			double dist = Math.pow(cell.getX() - x, 2) + Math.pow(cell.getY() - y, 2);

			//  if(celltype.isInstance(cell.getContent())) System.out.println("I mean, that looks like a " + celltype);

			if(celltype.isInstance(cell.getContent()) // select cell type
					&& Math.pow(cell.getX() - x, 2) + Math.pow(cell.getY() - y, 2) < minDist // minimum distance
					&& test.test(cell) // test for predicate
			)
			{
				selectedCell = cell;
				minDist = dist;
			}
		}

		return selectedCell;
	}

}
