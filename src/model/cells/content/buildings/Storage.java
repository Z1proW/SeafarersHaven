package model.cells.content.buildings;

import model.Container;
import model.Resource;

import java.util.EnumMap;

public class Storage extends Building implements Container
{

	private final EnumMap<Resource, Integer> stock;
	private final int maxStock;

	public Storage()
	{
		super(100, true, "storage.png");

		this.stock = new EnumMap<>(Resource.class);
		this.maxStock = 300;
	}

	public int getMaxStock()
	{
		return maxStock;
	}

	public boolean isFull()
	{
		int totalStock = 0;
		for(Integer i : stock.values()) totalStock += i;

		System.out.println("Is full: " + (totalStock >= maxStock) + " (" + totalStock + " / " + maxStock + ")");
		return totalStock >= maxStock;
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
		return stock.getOrDefault(resource, 0);
	}

	@Override
	public void removeResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(previousQuantity < quantity) throw new ContainerException("Not enough resources");

		stock.put(resource, previousQuantity - quantity);
	}

	@Override
	public int getRemainingSpace(Resource resource)
	{
		int totalStock = 0;
		for(Integer i : stock.values()) totalStock += i;

		return maxStock - totalStock;
	}

	@Override
	public void addResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(getRemainingSpace(resource) < quantity) throw new ContainerException("Not enough space");

		stock.put(resource, previousQuantity + quantity);
	}

}
