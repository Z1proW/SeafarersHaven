package model.units.enemies;

import model.Model;
import model.units.Unit;

import java.awt.geom.Point2D;

/**
 * Enemy class
 **/
public class Enemy extends Unit
{

	private final EnemyManager manager;
	private final EnemyType type;

	/**
	 * Constructor
	 *
	 * @param model      where we put the enemy
	 * @param type       of the enemy
	 * @param spawnPoint of the enemy
	 **/
	public Enemy(Model model, EnemyType type, Point2D.Double spawnPoint)
	{
		super(spawnPoint, type.getMaxHealth(), type.getTexture());

		this.type = type;
		this.manager = new EnemyManager(model, this);
		manager.start();

		model.addEnemy(this);
	}

	/**
	 * @return its manager(controler thread)
	 **/
	public EnemyManager getManager()
	{
		return manager;
	}

	/**
	 * @return type of the enemy
	 **/
	public EnemyType getType()
	{
		return type;
	}

	/**
	 * kill the enemy
	 **/
	@Override
	public void kill()
	{
		manager.onDeath();
	}


}
