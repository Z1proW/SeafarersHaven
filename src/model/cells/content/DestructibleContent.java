package model.cells.content;

public abstract class DestructibleContent extends CellContent
{

	private final int maxHealth;
	private int health;

	/**
	 * Constructor
	 *
	 * @param health   of the destructible content
	 * @param walkable if the unit can walk on the content
	 * @param fileName of the sprite of the DestructibleContent
	 **/
	public DestructibleContent(int health, boolean walkable, String fileName)
	{
		super(walkable, fileName);
		this.health = health;
		this.maxHealth = health;
	}

	/**
	 * @return current health of content
	 **/
	public int getHealth()
	{
		return health;
	}

	/**
	 * @return max health of content
	 **/
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * @param damage taken damage to health
	 **/
	public void damage(int damage)
	{
		health -= damage;
	}

	/**
	 * @return string of the class
	 **/
	public String toString()
	{
		return getClass().getSimpleName() + ": " + health + "/" + maxHealth + " HP";
	}

}
