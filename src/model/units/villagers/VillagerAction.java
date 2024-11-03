package model.units.villagers;

import view.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * all the different actions to be performed by villagers
 */

// TODO: retirer le vieux bordel
public enum VillagerAction
{

	IDLE,
	MOVING,
	FELLING,
	HARVESTING,
	FISHING,
	REPAIRING,
	BUILDING,
	STORING,
	PANIC,
	DEFENDING,
    PLANTING,
    UPROOTING;

	/**
	 * @return the icon of the action
	 */
	public Image getIcon()
	{
		if(this == IDLE)
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		return Texture.getImage("villagers/actions/" + name().toLowerCase() + ".png");
	}

}
