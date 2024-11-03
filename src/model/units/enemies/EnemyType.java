package model.units.enemies;

import view.Texture;

import java.awt.*;

public enum EnemyType // TODO: VillagerType and EnemyType are very similar, maybe make UnitType ?
{
	SKELETON("skeleton.png", 50, 5),
	ZOMBIE("zombie.png", 100, 2),
	SPECTER("specter.png", 20, 7),
	GASPER("gasper.png", 40, 2),
	PIRATE("pirate.png", 100, 3);

	private final int health;
	private final int damage;

	private final Image texture;

	/**
	 * Constructor of the enemyType
	 *
	 * @param health   of the enemy
	 * @param damage   of the enemy on unit of village or building
	 * @param fileName link to the sprite of the enemy
	 **/
	EnemyType(String fileName, int health, int damage)
	{
		this.health = health;
		this.damage = damage;

		this.texture = Texture.getImage("enemies/" + fileName);
	}

	/**
	 * @return the max of hp this particular type of enemy
	 **/
	public int getMaxHealth()
	{
		return health;
	}

	/**
	 * @return the damage done for a particular type of enemy
	 **/
	public int getDamage()
	{
		return damage;
	}

	/**
	 * @return the enemy's sprite
	 */
	public Image getTexture()
	{
		return texture;
	}

}
