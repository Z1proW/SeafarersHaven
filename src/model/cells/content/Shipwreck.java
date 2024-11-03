package model.cells.content;

import model.Container;
import model.Resource;

public class Shipwreck extends DestructibleContent implements Container
{

	private final int NEEDED_WOOD = 2500;
	private int numWood = 0;

	public Shipwreck()
	{
		super(1500, true, "shipwreck.png");
	}

	/**
	 *
	 * @return the percent of wood collected
	 */
	public float getPercentComplete()
	{
	//	System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + numWood);
	//	System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + (numWood / (float) NEEDED_WOOD));

		return numWood / (float) NEEDED_WOOD;
	}

	/*
	 *
	 * CONTAINER INTERFACE
	 *
	 * See documentation in interface
	 *
	 * We'll be lazy here and consider everything to always be wood because we don't use any other resource.
	 *
	 *
	 */

	public int getResourceNumber(Resource resource)
	{
		return numWood;
	}

	public void removeResource(Resource resource, int quantity)
	{
		numWood -= quantity;
	}

	public int getRemainingSpace(Resource resource)
	{
		return NEEDED_WOOD - numWood;
	}

	public void addResource(Resource resource, int quantity)
	{
		numWood += quantity;
	}

}
