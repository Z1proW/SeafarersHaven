package model.units;

import model.CellGrid;
import model.Model;

import java.awt.geom.Point2D;
import java.util.*;

public class ThetaStar
{

	private static final double positiveInfinity = Double.POSITIVE_INFINITY;

	/**
	 * Find the path of the cell between the start and the end
	 *
	 * @param model where we do the pathing
	 * @param start position of the path
	 * @param end   position of the path
	 * @return a list of cell where we need to pass
	 **/
	public static ArrayList<Point2D.Double> findPath(Model model, Point2D.Double start, Point2D.Double end)
	{
		// Set up data structures for A* algorithm
		// The pathing
		Map<Point2D.Double, Point2D.Double> cameFrom = new HashMap<>();
		// cost map
		Map<Point2D.Double, Double> gScore = new HashMap<>();
		// heuristic map
		Map<Point2D.Double, Double> fScore = new HashMap<>();

		PriorityQueue<Point2D.Double> openList = new PriorityQueue<>(
				Comparator.comparingDouble(p -> fScore.getOrDefault(p, positiveInfinity)));
		// we put the first cell at cost 0 gscore
		gScore.put(start, 0.0);
		// we put the first heuristic on the fscore
		fScore.put(end, heuristicCost(start, end));
		// cameFrom is for the parent of the cell
		openList.offer(start);

		while(!openList.isEmpty())
		{
			Point2D.Double current = openList.poll();
			// end if we find the cell of the end position
			if((int) current.x == (int) end.x && (int) current.y == (int) end.y)
			{
				// Reconstruct path from cameFrom
				ArrayList<Point2D.Double> path = new ArrayList<>();
				path.add(current);
				while(cameFrom.containsKey(current))
				{
					current = cameFrom.get(current);
					path.add(0, current);
				}
				return path;
			}

			// Get the neighbors of the current cell
            ArrayList<Point2D.Double> neighbors = getNeighbors(model.getGrid(), current);

			// Test if the neighbor is one of the most optimal path
			for(Point2D.Double neighbor : neighbors)
			{
				// addition for Theta* = A* with check of line of sight
				// if the parent is in the line of sight of the current cell
				// we can add the path from the parent to the neighbor
				if(cameFrom.get(current) != null && inLineOfSight(cameFrom.get(current), neighbor, model.getGrid()))
					current = cameFrom.get(current);

				double tentativeGScore = gScore.getOrDefault(current, positiveInfinity) + heuristicCost(current, neighbor);

				if(tentativeGScore < gScore.getOrDefault(neighbor, positiveInfinity))
				{
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tentativeGScore);
					fScore.put(neighbor, tentativeGScore + heuristicCost(neighbor, end));
					// if neighbor is not in the neighbor list put it inside
					if(!openList.contains(neighbor)) openList.offer(neighbor);
				}
			}
		}

		// If path not found
		return new ArrayList<>();
	}


	/**
	 * Uses an alternative of the Bresenham algorithm to check if the end cell is in the line of sight of the start cell
	 * @param start the start cell
	 * @param end the end cell
	 * @return true if the end cell is in the line of sight of the start cell
	 */
	private static boolean inLineOfSight(Point2D.Double start, Point2D.Double end, CellGrid grid)
	{
		double x1 = start.x;
		double y1 = start.y;
		double x2 = end.x;
		double y2 = end.y;

		double dx = Math.abs(x2 - x1);
		double dy = Math.abs(y2 - y1);

		double sx = x1 < x2 ? 1 : -1;
		double sy = y1 < y2 ? -1 : 1;

		double err = dx - dy;

		while(true)
		{
			if(!grid.getCell((int) x1, (int) y1).isWalkable()) return false;

			if(x1 == x2 && y1 == y2) break;

			double e2 = 2 * err;

			if(e2 > -dy)
			{
				err -= dy;
				x1 += sx;
			}

			if(e2 < dx)
			{
				err += dx;
				y1 += sy;
			}
		}

		return true;
	}

	/**
	 * Get the neighbors of the current cell
	 * @param grid the grid where we do the pathing
	 * @param current the current cell
	 * @return a list of the neighbors of the current cell
	 */
	private static ArrayList<Point2D.Double> getNeighbors(CellGrid grid, Point2D.Double current)
	{
		ArrayList<Point2D.Double> neighbors = new ArrayList<>();

		int x = (int) current.x;
		int y = (int) current.y;

		if(grid.getCell(x - 1, y).isWalkable() && (grid.getCell(x - 1, y - 1).isWalkable() || grid.getCell(x - 1, y).isWalkable()))
			neighbors.add(new Point2D.Double(x - 1, y));

		if(grid.getCell(x + 1, y).isWalkable() && grid.getCell(x, y).isWalkable())
			neighbors.add(new Point2D.Double(x + 1, y));

		if(grid.getCell(x, y - 1).isWalkable() && (grid.getCell(x - 1, y - 1).isWalkable() || grid.getCell(x, y - 1).isWalkable()))
			neighbors.add(new Point2D.Double(x, y - 1));

		if(grid.getCell(x, y + 1).isWalkable() && grid.getCell(x, y).isWalkable())
			neighbors.add(new Point2D.Double(x, y + 1));

		return neighbors;
	}

	/**
	 * Calculate the distance between the first and second cell
	 *
	 * @param p1 start cell
	 * @param p2 end cell
	 **/
	public static double heuristicCost(Point2D.Double p1, Point2D.Double p2)
	{
		double x = p1.getX() - p2.getX();
		double y = p1.getY() - p2.getY();

		// here heuristic is the distance between the current cell and the end cell
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Find the path of the position between the start and the end with the list of Cell
	 *
	 * @param model where we do the pathing
	 * @param start position of the path
	 * @param end   position of the path
	 * @return a list of Position where we need to pass
	 **/
	public static ArrayList<Point2D.Double> findPathPosition(Model model, Point2D.Double start, Point2D.Double end)
	{
		ArrayList<Point2D.Double> path = findPath(model, start, end);

		// replace the last position by the end position
		if(path.size() > 0)
			path.set(path.size() - 1, end);

		return path;
	}

}
