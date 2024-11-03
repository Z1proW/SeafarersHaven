package model.cells.content.buildings;

import model.Resource;

import java.awt.*;
import java.util.EnumMap;

public enum BuildingType
{

	HOUSE(new EnumMap<>(Resource.class)
	{{
		put(Resource.WOOD, 100);
	}}),
	STORAGE(new EnumMap<>(Resource.class)
	{{
		put(Resource.WOOD, 120);
	}}),
	TAVERN(new EnumMap<>(Resource.class)
	{{
		put(Resource.WOOD, 150);
	}});

	private final EnumMap<Resource, Integer> cost;

	BuildingType(EnumMap<Resource, Integer> cost)
	{
		this.cost = cost;
	}

	/**
	 * Get the cost of a resource for this building
	 * @param resource the resource
	 * @return the cost
	 */
	public int getCost(Resource resource)
	{
		return cost.getOrDefault(resource, 0);
	}

	/**
	 * Get the cost of all resources for this building
	 * @return the cost
	 */
	public EnumMap<Resource, Integer> getCost()
	{
		return cost;
	}

	/**
	 * Get the texture of the building
	 * @return the texture
	 */
	public Image getTexture() // TODO handle more elegantly
	{
		return switch(this)
		{
			case HOUSE -> new House(null, 0, 0).getTexture();
			case STORAGE -> new Storage().getTexture();
			case TAVERN -> new Tavern().getTexture();
		};
	}

}
