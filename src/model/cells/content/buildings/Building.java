package model.cells.content.buildings;

import model.cells.content.DestructibleContent;

public abstract class Building extends DestructibleContent
{

	public Building(int health, boolean walkable, String fileName)
	{
		super(health, walkable, fileName);
	}

}
