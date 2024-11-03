package model;

/**
 * all the different existing resources
 */
public enum Resource
{

	WOOD(3),
	STONE(5),
	IRON(6),
	FISH(2),
	MEAT(4),
	FLINT(2),
	FEATHER(1),
	GOLD(8),
	DIAMOND(10),
	ONYX(20);

	private final int weight;

	/**
	 * constructor
	 *
	 * @param weight the weight of the resource
	 */
	Resource(int weight)
	{
		this.weight = weight;
	}

	/**
	 * @return le poids de la ressource
	 */
	public int getWeight()
	{
		return weight;
	}

	@Override
	public String toString()
	{
		return this.name();
	}
}
