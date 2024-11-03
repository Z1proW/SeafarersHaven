package model.cells.content.buildings;

import model.Model;
import model.units.villagers.TM;
import model.units.villagers.Villager;
import model.units.villagers.VillagerType;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class House extends Building
{

	private final int x, y;
	private final Model model;

	public House(Model model, int x, int y)
	{
		super(100, true, "shack.png");

		this.x = x;
        this.y = y;
		this.model = model;
	}

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void startSpawning()
    {
		new Thread(() ->
		{
			Random random = new Random();
			int spawnCount = 0;

			// spawn more lumberjacks than farmers, those are useless
			VillagerType[] possibleTypes = {VillagerType.LUMBERJACK, VillagerType.LUMBERJACK, VillagerType.FARMER};

			TM.sleep(random.nextInt(5000, 10000), () -> {});

			while(getHealth() > 0 && spawnCount < 3) // TODO spawnCount per house type ?
			{
				new Villager(model,
						possibleTypes[ random.nextInt(possibleTypes.length) ],
						new Point2D.Double(
								x + 0.5 + random.nextInt(-1, 1),
								y + 0.5 + random.nextInt(-1, 1)));

				spawnCount++;

				// needs to be at the end to avoid spawning a villager after the house is destroyed
				TM.sleep(random.nextInt(5000, 10000), () -> {});
			}
		}).start();
	}

}
