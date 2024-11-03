package model;

import model.cells.Cell;
import model.cells.content.Shipwreck;
import model.units.enemies.Enemy;
import model.units.villagers.Villager;
import model.units.villagers.VillagerType;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * the main model
 * contains the villagers,
 * the grid and the selected villagers
 */
public class Model
{

	private final ArrayList<Villager> villagers = new ArrayList<>();
	private final ArrayList<Enemy> enemies = new ArrayList<>();
	private final CellGrid cellGrid;
	private final Selection selection = new Selection();
	private final Village village = new Village();
	private final Calendar calendar = new Calendar();
	private Shipwreck shipwreck;

	private Cell selectedCell;

	/**
	 * constructor
	 * adds a first villager to the list
	 */
	public Model(int width, int height)
	{
		cellGrid = new CellGrid( width, height, this );

		Cell shipWreckCell = cellGrid.getShipwreck();
		shipwreck = (Shipwreck) shipWreckCell.getContent();

        new Villager( this, VillagerType.CAPTAIN, new Point2D.Double( shipWreckCell.getX(), shipWreckCell.getY() ) );
	}

	/**
	 * @return the village model
	 */
	public Village getVillage()
	{
		return village;
	}

	/**
	 * @return the calendar
	 */
	public Calendar getCalendar()
	{
		return calendar;
	}

	public Shipwreck getShipWreck() {
		return shipwreck;
	}

	/**
	 * @return the list of villagers
	 */
	public ArrayList<Villager> getVillagers()
	{
		return villagers;
	}

	/**
	 * @return the list of enemies
	 **/
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}

	/**
	 * @return the grid
	 */
	public CellGrid getGrid()
	{
		return cellGrid;
	}

	/**
	 * @return the selected villagers
	 */
	public Selection getSelection()
	{
		return selection;
	}

	/**
	 * @return selected cell
	 **/
	public Cell getSelectedCell()
	{
		return selectedCell;
	}

	/**
	 * @param cell put cell as selectedCell
	 **/
	public void setSelectedCell(Cell cell)
	{
		selectedCell = cell;
	}

	/**
	 * @param villager remove the villager in the arrayList villagers
	 **/
	public void removeVillager(Villager villager)
	{
		villagers.remove(villager);
	}

	/**
	 * @param enemy remove enemm from the arrayList enemies
	 **/
	public void removeEnemy(Enemy enemy)
	{
		synchronized(enemies)
		{
			enemies.remove(enemy);
		}
	}

	/**
	 * @param villager add villager to the list of villagers
	 **/
	public void addVillager(Villager villager)
	{
		synchronized(villagers)
		{
			villagers.add(villager);
		}
	}

	/**
	 * @param enemy add villager to the list of enemies
	 **/
	public void addEnemy(Enemy enemy)
	{
		synchronized(enemies)
		{
			enemies.add(enemy);
		}
	}

}
