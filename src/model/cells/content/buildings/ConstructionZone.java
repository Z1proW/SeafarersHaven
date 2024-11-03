package model.cells.content.buildings;

import model.Container;
import model.Resource;
import model.cells.content.CellContent;

import java.util.EnumMap;
import java.util.Objects;

public class ConstructionZone extends CellContent implements Container
{

	private final EnumMap<Resource, Integer> materials = new EnumMap<>(Resource.class);
	private final BuildingType type;

	public ConstructionZone(BuildingType type)
	{
		super(true, "construction_zone.png");
		this.type = type;
	}

	/**
	 * Return the buildingType it constructs
	 */
	public BuildingType getBuildingType()
	{
		return type;
	}

	/**
	 * Get the material that are needed to build the building
	 */
	public EnumMap<Resource, Integer> getMaterialNeeded()
	{
		return type.getCost();
	}

	/**
	 * Returns true if zone has enough of given resource to finish building
	 *
	 * @param resource the resource to probe for
	 * @return true if enough
	 */
	public boolean hasEnough(Resource resource)
	{
		return Objects.equals(materials.getOrDefault(resource, 0), type.getCost(resource));
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
		return materials.getOrDefault(resource, 0);
	}

	@Override
	public void removeResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(previousQuantity < quantity) throw new ContainerException("Not enough resources");

		materials.put(resource, previousQuantity - quantity);
	}

	@Override
	public int getRemainingSpace(Resource resource)
	{
		return type.getCost().getOrDefault(resource, 0) - materials.getOrDefault(resource, 0);
	}

	@Override
	public void addResource(Resource resource, int quantity)
	{
		int previousQuantity = getResourceNumber(resource);
		if(getRemainingSpace(resource) < quantity) throw new ContainerException("Not enough space");

		System.out.println("Add " + quantity + " " + resource + " to zone = " + (previousQuantity + quantity) + " / " + type.getCost(resource));

		materials.put(resource, previousQuantity + quantity);
	}

}
