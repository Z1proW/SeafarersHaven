package model.units.villagers;

import view.Texture;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static model.units.villagers.VillagerAction.*;

/**
 * all the possible types of villagers
 */
public enum VillagerType
{

	CAPTAIN(200, 15, 120, "captain.png", FELLING, REPAIRING, HARVESTING, BUILDING, DEFENDING),
	LUMBERJACK(100, 12, 150, "lumberjack.png", FELLING, REPAIRING, BUILDING, DEFENDING),
	FARMER(100, 8, 100, "farmer.png", UPROOTING, PLANTING , HARVESTING, BUILDING, DEFENDING),
	FISHER(100, 1, 100, "fisher.png", FISHING, PANIC);

	private final int maxHealth, maxWeight, strength; // TODO speed
	private final Image texture, icon;
	private final Set<VillagerAction> villagerActions;

	/**
	 * constructor
	 *
	 * @param maxHealth       the maximum health of the villager
	 * @param strength        the strength of the villager
	 * @param maxWeight       the maximum weight the villager can carry
	 * @param fileName        the file name of the sprite including the file extension
	 * @param villagerActions all the possible actions for this type
	 */
	VillagerType(int maxHealth, int strength, int maxWeight, String fileName, VillagerAction... villagerActions)
	{
		this.maxHealth = maxHealth;
		this.strength = strength;
		this.maxWeight = maxWeight;
		this.texture = Texture.getImage("villagers/" + fileName);
		this.icon = Texture.getImage("villagers/icon/" + fileName);
		this.villagerActions = new HashSet<>(List.of(villagerActions)); // TODO do it another way to avoid the copy
		this.villagerActions.add(IDLE);
        this.villagerActions.add(MOVING);
	}

	/**
	 * @return the villager's sprite
	 */
	public Image getTexture()
	{
		return texture;
	}

	/**
	 * @return the villager's icon
	 */
	public Image getIcon()
	{
		return icon;
	}

	/**
	 * @return the set of possible actions
	 */
	public Set<VillagerAction> allowedActions()
	{
		return villagerActions;
	}

	/**
	 * @return the maximum health of this type of villager
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * @return the strength of this type of villager
	 */
	public int getStrength()
	{
		return strength;
	}

	/**
	 * @return the maximum weight of this type of villager
	 */
	public int getMaxWeight()
	{
		return maxWeight;
	}

}
