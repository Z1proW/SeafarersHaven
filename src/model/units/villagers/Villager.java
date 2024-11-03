package model.units.villagers;

import jdk.jshell.spi.ExecutionControl;
import model.Model;
import model.Resource;
import model.cells.Cell;
import model.cells.content.DestructibleContent;
import model.cells.content.TreeTrunk;
import model.units.Unit;

import java.awt.geom.Point2D;
import java.util.Set;

public class Villager extends Unit
{

	private final VillagerManager manager;
	private final VillagerType type;
	private final Inventory inventory;

	/**
	 * Constructor Villager
	 *
	 * @param model      of the villager
	 * @param type       of the villager
	 * @param spawnPoint of the villager
	 **/
	public Villager(Model model, VillagerType type, Point2D.Double spawnPoint)
	{
		super(spawnPoint, type.getMaxHealth(), type.getTexture());

		this.type = type;
		this.inventory = new Inventory(type.getMaxWeight());

		manager = new VillagerManager(model, this); // TODO find a better way to not need Model
		manager.start();

		model.addVillager(this);
	}

	/**
	 * @return the manager of the villager
	 **/
	public VillagerManager getManager()
	{
		return manager;
	}

	/**
	 * @return the type of the villager
	 **/
	public VillagerType getType()
	{
		return type;
	}

	/**
	 * @return the inventory of the villager
	 **/
	public Inventory getInventory()
	{
		return inventory;
	}

	/**
	 * @return the type of action the villager is allowed to do
	 **/
	public Set<VillagerAction> getAllowedActions()
	{
		return type.allowedActions();
	}

	/**
	 * @param content is damaged by the villager
	 **/
	public void damageContent(DestructibleContent content)
	{
		content.damage(type.getStrength());
	}

	/**
	 * @param cell fell tree so it became a tree trunk
	 **/
	public void fellTree(Cell cell)
	{
		inventory.addResourceRandomRange(Resource.WOOD, 12, 18);
		cell.setContent(new TreeTrunk());
	}

	private void build() throws ExecutionControl.NotImplementedException // TODO
	{
		throw new ExecutionControl.NotImplementedException("build not implemented");
	}

	// TODO add methods here instead of in the thread

	/**
	 * kill the villager
	 **/
	@Override
	public void kill()
	{
		manager.onDeath();
	}

	/**
	 * act when in range of an enemy unit
	 **/
	public void detectEnemy()
	{
		manager.enemyAttack();
	}

}
