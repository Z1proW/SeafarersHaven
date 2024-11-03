package model.units.villagers;

import model.Model;
import model.Resource;
import model.cells.Cell;
import model.cells.CellGround;
import model.cells.content.*;
import model.cells.content.buildings.ConstructionZone;
import model.cells.content.buildings.House;
import model.cells.content.buildings.Storage;
import model.cells.content.buildings.Tavern;
import model.units.UnitManager;
import model.units.enemies.Enemy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public class VillagerManager extends UnitManager
{

	private final Villager villager;
	private VillagerAction villagerAction = VillagerAction.IDLE;
	private boolean survival = false;

	public VillagerManager(Model model, Villager villager)
	{
		super(model, villager);
		this.villager = villager;
	}

	/**
	 * @return the action the villager is currently doing
	 */
	public VillagerAction getMode()
	{
		if(survival)    return VillagerAction.DEFENDING;
		else            return villagerAction;
	}

	/**
	 * sets the current action of the villager
	 * @param mode the action to do
	 * @param x the x coordinate of the target
	 * @param y the y coordinate of the target
	 */
	public void setMode(VillagerAction mode, double x, double y)
	{
		villagerAction = mode;
		this.targetX = x;
		this.targetY = y;
		this.interrupt();
	}

	/**
	 * sets the current action of the villager
	 * at the current position of the villager
	 * @param mode the action to do
	 */
	public void setMode(VillagerAction mode)
	{
		setMode(mode, villager.getX(), villager.getY());
	}

	@Override
	public void run()
	{
		while(villager.getHealth() > 0)
		{
			try
			{
				System.out.println("Received order to " + villagerAction);

				// go idle if action is disallowed for this villager's type
				if(!villager.getAllowedActions().contains(villagerAction))
					throw new TM.Exited();

				// if in survival mode, defend or panick, then return to normal mode afterwards
				if(survival) {
					TM.runFail(
						() -> guard(),              // throws TM.exited on finish
						() -> survival = false,     // set survival to false when function finishes
						() -> {}                    // if interrupted by another order, leave survival mode enabled
					);
				}

				// if less than half life, go to the tavern to regenerate
				// using runFail to avoid going idle on failure
				if( villager.getHealth() < villager.getMaxHealth() / 2 )
					TM.runFail( () ->  heal(), () -> {}, () -> {} );

				// then, accomplish received order
				switch(villagerAction)
				{
					case IDLE -> idle();
					case REPAIRING -> repairShipwreck( model.getGrid().getCell((int) targetX, (int) targetY) );
					case FELLING -> harvestTrees(targetX, targetY);
					case STORING -> store(targetX, targetY);
					case BUILDING -> build( model.getGrid().getCell((int) targetX, (int) targetY) );
					case MOVING -> move(targetX, targetY);
					case PANIC -> panic();
					case DEFENDING -> guard();
                    case UPROOTING -> uprooting();
                    case PLANTING -> planting(targetX, targetY);
				}

				// go idle after return
				villagerAction = VillagerAction.IDLE;
			}
			catch(TM.Interrupted e)
			{
				// Interrupt received to change mode - nothing to do, continue looping
			}
			catch(TM.Exited e)
			{
				// Interrupted from the inside to indicate the current mode can't run anymore - set mode to IDLE
				villagerAction = VillagerAction.IDLE;
			}
		}
	}

	/*
	 *
	 *
	 *
	 * MOVEMENT
	 *
	 *
	 *
	 *
	 */

	/**
	 * Find and move to the nearest cell with this content.
	 *
	 * @param cellType the cell content to find
	 */
	private void moveToNearest(Class<?> cellType)
	{
		Cell cell = villager.getCell(model.getGrid());
		Cell targetCell = model.getGrid().getNearest(cell.getX(), cell.getY(), cellType);

		TM.check(targetCell != null);

		double targetX = targetCell.getX() + Math.random();
		double targetY = targetCell.getY() + Math.random();

		move(targetX, targetY);
	}

	/**
	 * Move to this cell.
	 *
	 * @param cell the cell to move to
	 */
	private void moveToCell(Cell cell)
	{
		double targetX = cell.getX() + Math.random();
		double targetY = cell.getY() + Math.random();

		move(targetX, targetY);
	}

	/*
	 *
	 *
	 *
	 * CUT TREES
	 *
	 *
	 *
	 *
	 */

	/**
	 * Villager go the fell tree and when inventory not full go fell another tree
	 *
	 * @param x of the tree
	 * @param y of the tree
	 **/
	private void harvestTrees(double x, double y)
	{
		while(true)
		{
			move(x, y);
			harvestTreesUntilInventoryFull();
			dumpInventory();
		}
	}

	/**
	 * The name says it all, doesn't it ?
	 */
	private void harvestTreesUntilInventoryFull()
	{
		while(villager.getInventory().getRemainingSpace(Resource.WOOD) > 0)
		{
			moveToNearest(Tree.class);
			cutTree();
		}
	}

	/**
	 * Causes the villager to cut the tree on its current cell.
	 */
	private void cutTree()
	{
		double x = villager.getX();    // needed for animation purposes
		double y = villager.getY();    // TODO: replace with GIF animation

		Cell cell = villager.getCell(model.getGrid());
		TM.check(cell.getContent() instanceof Tree);

		while(((DestructibleContent) cell.getContent()).getHealth() > 0)
		{
			TM.sleep(
					500,
					() -> {}
			);

			villager.moveTo(x, y - 0.2);

			TM.sleep(
					100,
					() -> {}
			);

			villager.moveTo(x, y);

			TM.check(cell.getContent() != null);
			villager.damageContent((DestructibleContent) cell.getContent());
		}

		villager.fellTree(cell);
	}

	/*
	 *
	 *
	 *
	 * ACQUIRING RESOURCES
	 *
	 *
	 *
	 *
	 */

	private void lumberJackAcquireWood()
	{
		TM.runFail(
				() -> getResourceFromStorage(Resource.WOOD),
				() -> {},   // D'ont idle on failure, instead continue below
				() -> {}
		);

		if(villager.getInventory().getRemainingSpace(Resource.WOOD) > 0)
			harvestTreesUntilInventoryFull();
	}

	/**
	 * Get a resource from the nearest storage unit
	 * @param resource the resource to get
	 */
	private void getResourceFromStorage(Resource resource)
	{
		Cell target = model.getVillage().findStorageWith((int) villager.getX(), (int) villager.getY(), resource);
		TM.check(target != null);

		if(!(target.getContent() instanceof Storage storage))
			throw new TM.Exited();

		moveToCell(target);

		int transferred = storage.tranferTo(villager.getInventory(), resource, 100);
		TM.sleep( 20 * transferred, () -> {} );
	}

	/*
	 *
	 *
	 *
	 * STORAGE SYSTEM
	 *
	 *
	 *
	 *
	 */

	/**
	 * Dump inventory in the nearest non-full storage unit
	 */
	private void dumpInventory()
	{
		Cell target = model.getVillage().findNonFullStorage((int) villager.getX(), (int) villager.getY());
		TM.check(target != null);

		moveToCell(target);
		store();
	}

	/**
	 * Causes the villager to dump all its inventory in the selected cell, if it is a storage cell.
	 */
	private void store(double x, double y)
	{
		move(x, y);
		store();
	}

	/**
	 * Causes the villager to dump all its inventory in the current cell, if it is a storage cell.
	 */
	private void store()
	{
		Cell cell = villager.getCell(model.getGrid());
		TM.check(cell.getContent() instanceof Storage);

		for(Resource resource : Resource.values())
			storeRessource(resource);
	}

	/**
	 * Store the resource to a storage from a villager
	 */
	private void storeRessource(Resource resource)
	{
		CellContent content = villager.getCell(model.getGrid()).getContent();
		if(!(content instanceof Storage storage)) throw new TM.Exited();

		int transferred = villager.getInventory().tranferTo(storage, resource, 100);
		TM.sleep( 20 * transferred, () -> {} );
	}

	/*
	 *
	 *
	 *
	 * BUILDING SYSTEM
	 *
	 *
	 *
	 *
	 */

	/**
	 * Build the building on the specified ConstructionZone
	 */
	private void build(Cell cell)
	{
		if(!(cell.getContent() instanceof ConstructionZone zone)) throw new TM.Exited();

		EnumMap<Resource, Integer> materialNeeded = zone.getMaterialNeeded();
		Inventory inventory = villager.getInventory();

		for(Resource resource : materialNeeded.keySet())
		{
			while(!zone.hasEnough(resource))
			{
				if(inventory.getResourceNumber(resource) == 0)
					lumberJackAcquireWood();

				moveToCell(cell);

				int transferred = inventory.tranferTo(zone, resource, 100);
				TM.sleep( 20 * transferred, () -> {} );
			}
		}

		switch(zone.getBuildingType())
		{
			case HOUSE ->
			{
				House house = new House(model, cell.getX(), cell.getY());
				cell.setContent(house);
				house.startSpawning();
			}
			case STORAGE -> cell.setContent(new Storage());
			case TAVERN -> cell.setContent(new Tavern());
		}
	}

	/**
	 * Repair the shipwreck on the specified cell
	 */
	private void repairShipwreck(Cell cell)
	{
		if(!(cell.getContent() instanceof Shipwreck ship)) throw new TM.Exited();

		Inventory inventory = villager.getInventory();

		// TODO: does this work lol ?
		while(ship.getPercentComplete() < 1.0)
		{
			if(inventory.getResourceNumber(Resource.WOOD) == 0)
				lumberJackAcquireWood();

			moveToCell(cell);

			int transferred = inventory.tranferTo(ship, Resource.WOOD, 100);
			TM.sleep( 20 * transferred, () -> {} );
		}
	}

	/*
	 *
	 *
	 *
	 * META MANAGEMENT
	 *
	 *
	 *
	 *
	 */

	/**
	 * put the villager into panic mode or defending by the enemy
	 **/
	public void enemyAttack()
	{
		// if the villager is not already in survival mode, and is not doing a whitelisted action
		if(
			villagerAction != VillagerAction.MOVING &&
			villagerAction != VillagerAction.PLANTING &&
			!survival
		)
		// then set it to survival mode
		{
			survival = true;
			interrupt();
		}
	}

	/**
	 * removes the villager from the village
	 * and interrupts this thread
	 */
	@Override
	public void onDeath()
	{
		model.removeVillager(villager);
		model.getSelection().getVillagers().remove(villager); // TODO fix might throw ConcurrentModificationException
		interrupt();
	}

	/*
	 *
	 *
	 *
	 * COMBAT SECTION
	 *
	 *
	 *
	 *
	 */

	/**
	 * the villager is panicking and move randomly
	 **/
	public void panic()
	{
		while(true)
		{
			double x = villager.getX();
			double y = villager.getY();
			Random random = new Random();
			move(x + random.nextDouble(-3, 3), y + random.nextDouble(-3, 3));
			TM.sleep(100, () -> {});
		}
	}

	/**
	 * When state Defending guard its place and go to enemy when near the enemy
	 *
	 * This method will throw TM.Exited when finished
	 **/
	public void guard()
	{
		while(true)
		{
			double radius = 10;
			double attackRadius = 5;
			Enemy currentEnemy = null;
			ArrayList<Enemy> enemies = model.getEnemies();

			for(Enemy enemy : enemies)
			{
				double x2 = villager.getX() - enemy.getX();
				double y2 = villager.getY() - enemy.getY();

				if(x2 * x2 + y2 * y2 < radius)
				{
					radius = x2 * x2 + y2 * y2;
					currentEnemy = enemy;
				}
			}

			TM.check(currentEnemy != null);

			if(radius <= attackRadius)
				currentEnemy.damage( villager.getType().getStrength() );
			else
			{
				targetX = currentEnemy.getX();
				targetY = currentEnemy.getY();
				move(villager.getX() - (villager.getX() - targetX) / 2, villager.getY() - (villager.getY() - targetY) / 2);
			}

			TM.sleep(500, () -> {});
		}
	}

	/**
	 * Go to the tavern and heal
	 *
	 * Throws TM.Exited if no tavern exists, exits when fully healed
	 **/
	public void heal() {
		// locate and go to the tavern
		Cell target = model.getVillage().findNearest((int) villager.getX(), (int) villager.getY(), Tavern.class);
		TM.check(target != null);

		moveToCell(target);

		// heal 10 points/second while the tavern exists
		while(villager.getHealth() < villager.getMaxHealth()) {
			TM.sleep(1000, () -> {});
			TM.check(target != null);
			villager.heal(10);
		}
	}

	/*
	 *
	 *
	 *
	 * FARMER
	 *
	 *
	 *
	 *
	 */

    /**
     *  Plant a new tree on a place where is nothing
     **/
    void planting(double x, double y)
    {
        move(x, y);
        Cell cell = villager.getCell(model.getGrid());
        TM.check(cell.getGround() == CellGround.GRASS && cell.getContent() == null);
		TreeSprout treeSprout = new TreeSprout();
		treeSprout.startGrowing(cell);
        cell.setContent(treeSprout);
    }

    /**
     *  Uproot any tree trunk they see
     **/
    void uprooting()
    {
        while(true)
		{
            moveToNearest(TreeTrunk.class);
            Cell cell = villager.getCell(model.getGrid());

            TM.check(cell.getContent() instanceof TreeTrunk);
            TM.sleep(500, () -> {});

            cell.setContent(null);
        }
    }

}
