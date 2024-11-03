package model.cells.content;

import model.Model;
import model.units.enemies.Enemy;
import model.units.enemies.EnemyType;
import model.units.villagers.TM;

import java.awt.geom.Point2D;
import java.util.Random;

public class Tomb extends DestructibleContent
{

	private final int x;
	private final int y;
	private final Random random = new Random();
	private final Model model;

	/** Number of days before the skeleton apocalypse */
	private static final int APOCALYPSE = 10;

	/**
	 * Create a new tomb and start the spawning of skeletons
	 * @param model the model
	 * @param x the x position
	 * @param y the y position
	 */
	public Tomb(Model model, int x, int y)
	{
		super(100, true, "tombs.png");
		this.x = x;
		this.y = y;
		this.model = model;

		new Thread(() ->
		{
			TM.sleep(2 * model.getCalendar().getDayLength(), () -> {});			// no ennemies first two days

			while(getHealth() > 0 && model.getCalendar().getDay() <= APOCALYPSE)			// before apocalypse, normal spawn, limit = 50
			{
				TM.sleep(getDurationFromDay(), () -> {});

				if(model.getEnemies().size() <= 20)
				{
					new Enemy(model, EnemyType.SKELETON,
							new Point2D.Double(
									getX() + 0.5 + random.nextDouble(1),
									getY() + 0.5 + random.nextDouble(1)));
				}
			}

			while(getHealth() > 0)											// apocalypse
			{
				TM.sleep(1000, () -> {});

				if(model.getEnemies().size() <= 200)
				{
					new Enemy(model, EnemyType.SKELETON,
							new Point2D.Double(
									getX() + 0.5 + random.nextDouble(1),
									getY() + 0.5 + random.nextDouble(1)));
				}
			}
		}).start();
	}

	/**
	 * Get the duration of the next spawn
	 * @return the duration in ms
	 */
	private int getDurationFromDay()
	{
		int baseUnit = model.getCalendar().getDayLength();
		int progress = (APOCALYPSE - model.getCalendar().getDay()) + 1;

		// sleep time = (1 - % to apocalypse) * unit * random(0.5, 1.0)
		// in this case (1 - % to apocalypse) = progress / APOCALYPSE

		//	System.out.println("Spawn baseUnit = " + baseUnit + ", progress = " + progress + ", equals = " + result);

		return progress * baseUnit * random.nextInt(50, 100) / (100 * APOCALYPSE);
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

}
