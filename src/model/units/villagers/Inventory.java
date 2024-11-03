package model.units.villagers;

import model.Container;
import model.Resource;

import java.util.EnumMap;
import java.util.Random;

public class Inventory implements Container
{

	private final Random random = new Random();

	private final EnumMap<Resource, Integer> resources = new EnumMap<>(Resource.class);
	private final int MAX_WEIGHT;
	private int weight = 0;

	public Inventory(int maxWeight)
	{
		MAX_WEIGHT = maxWeight;
	}

	/*
	 *
	 *
	 * CONTAINER INTERFACE
	 *
	 * See documentation in interface
	 *
	 *
	 *
	 */

	@Override
	public int getResourceNumber(Resource resource)
	{
		return resources.getOrDefault(resource, 0);
	}

	@Override
	public void removeResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(previousQuantity < quantity) throw new ContainerException("Not enough resources");

		resources.put(resource, previousQuantity - quantity);
		weight -= resource.getWeight() * quantity;
	}

	@Override
	public int getRemainingSpace(Resource resource)
	{
		return (MAX_WEIGHT - weight) / resource.getWeight();
	}

	@Override
	public void addResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(getRemainingSpace(resource) < quantity) throw new ContainerException("Not enough space");

		resources.put(resource, previousQuantity + quantity);
		weight += resource.getWeight() * quantity;

        System.out.println("Got " + quantity + " " + resource + ", weight = " + weight + "/" + MAX_WEIGHT);
	}

	/*
	 *
	 *
	 *
	 * END CONTAINER INTERFACE
	 *
	 *
	 *
	 *
	 */

	/**
	 * Safely add resources to the inventory, discarding any leftover if available space is insufficient
	 *
	 * @param resource the resource to add
	 * @param quantity the quantity to add
	 */
	public void addSafely(Resource resource, int quantity)
	{
		addResource(resource, Math.min(quantity, getRemainingSpace(resource)));
	}

	/**
	 * Adds a random quantity of the resource between minimum and maximum
	 *
	 * @param resource the resource to be given
	 * @param minimum  the minimum quantity
	 * @param maximum  the maximum quantity
	 */
	public void addResourceRandomRange(Resource resource, int minimum, int maximum)
	{
		addSafely(resource, random.nextInt(minimum, maximum + 1));
	}

	/**
	 * @return the total weight of the resources
	 */
	public int getWeight()
	{
		return weight;
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append("Inventory\n{\n");
		s.append("weight: ").append(weight).append("/").append(MAX_WEIGHT).append("\n");

		for(Resource resource : Resource.values())
			s.append(resource).append(": ").append(getResourceNumber(resource)).append("\n");
		s.append("\n");

		return s.toString();
	}

}
