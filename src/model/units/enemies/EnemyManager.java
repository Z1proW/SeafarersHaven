package model.units.enemies;


import model.Model;
import model.units.UnitManager;
import model.units.villagers.TM;
import model.units.villagers.Villager;

public class EnemyManager extends UnitManager
{

	private final Enemy enemy;
	private EnemyAction enemyAction = EnemyAction.IDLE;
	private Villager selectedVillager;

	/**
	 * Constructor EnemyManager
	 *
	 * @param model of the manager
	 * @param enemy of the manager thread
	 **/
	public EnemyManager(Model model, Enemy enemy)
	{
		super(model, enemy);
		this.enemy = enemy;
	}

	public void setMode(EnemyAction enemyAction, double x, double y)
	{
		this.enemyAction = enemyAction;
		this.targetX = x;
		this.targetY = y;
		this.interrupt();
	}

	@Override
	public void run()
	{
		while(enemy.getHealth() > 0)
		{
			try
			{
				switch(enemyAction)
				{
					case IDLE ->
					{
						dectectVillagerOnRadius(5);
						idle();
					}
					case CHASING -> moveToNearestVillager();

				}

				// If the controller simply finished running, switch to idle state
			}
			catch(TM.Interrupted e)
			{
				// Interrupt received to change mode - nothing to do, continue looping
			}
			catch(TM.Exited e)
			{
				// Interrupted from the inside to indicate the current mode can't run anymore - set mode to IDLE
				this.enemyAction = EnemyAction.IDLE;
			}
		}
	}

	/**
	 * The enemy move to the nearest villager dectected in its range
	 **/
	public void moveToNearestVillager()
	{
		getNearestVillager(6);

		if(selectedVillager == null)
		{
			enemyAction = EnemyAction.IDLE;
			return;
		}

		moveToVillager(selectedVillager);
	}

	/**
	 * the enemy move toward the nearest villager
	 * and damage it if in range
	 **/
	public void moveToVillager(Villager villager)
	{

		double
				hitRadius = 0.5,
				x2 = villager.getX() - enemy.getX(),
				y2 = villager.getY() - enemy.getY();
		// Damage the villager if in range
		if(x2 * x2 + y2 * y2 < hitRadius * hitRadius)
		{
			villager.damage(enemy.getType().getDamage());
			TM.sleep(
					500,
					() -> {}
			);
		}

		targetX = villager.getX();
		targetY = villager.getY();

		move(enemy.getX() - (enemy.getX() - targetX) / 2, enemy.getY() - (enemy.getY() - targetY) / 2);
	}

	/**
	 * detect villager in its radius
	 * and select the nearest one
	 **/
	public void getNearestVillager(double radius)
	{
		selectedVillager = null;
		double newRadius = radius;

		for(Villager villager : model.getVillagers())
		{
			double x2 = villager.getX() - enemy.getX();

			double y2 = villager.getY() - enemy.getY();

			double sum = x2 * x2 + y2 * y2;
			if(sum < radius * radius)
			{
				if(sum < newRadius * newRadius)
				{
					newRadius = sum;
					selectedVillager = villager;
				}
				villager.detectEnemy();
			}
		}
	}

	/**
	 * the enemy dectect if there is an unit from the village
	 * and himself in chasing if found something
	 **/
	private void dectectVillagerOnRadius(double radius)
	{
		for(Villager villager : model.getVillagers())
		{
			double x2 = villager.getX() - enemy.getX();

			double y2 = villager.getY() - enemy.getY();

			if(x2 * x2 + y2 * y2 < radius * radius)
			{
				enemyAction = EnemyAction.CHASING;
				return;
			}
		}
	}

	/**
	 * removes the ennemy from the village
	 * and interrupts this thread
	 */
	@Override
	public void onDeath()
	{
		model.removeEnemy(enemy);
		interrupt();
	}

}
