package model.units;

import model.Model;
import model.units.villagers.TM;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

public abstract class UnitManager extends Thread
{

	protected final Model model;
	protected double targetX, targetY;

	protected Unit unit;

	/**
	 * Constructor
	 *
	 * @param model of the manager
	 * @param unit  of the manager
	 **/
	public UnitManager(Model model, Unit unit)
	{
		this.model = model;
		this.unit = unit;
	}

	abstract public void run();

	/**
	 * kill the unit and stop the manager
	 **/
	abstract public void onDeath();


	/**
	 * Villager moving using A* algorithm
	 **/
	public void move(double x, double y)
	{
        List<Point2D.Double> positions = ThetaStar.findPathPosition(model, unit.getPosition(), new Point2D.Double(x, y));

        for(Point2D.Double position : positions)
            unit.moveTo(position.getX(), position.getY());
    }

	/**
	 * When state is idle move randomly every 1 to 6 second
	 **/
	public void idle()
	{
		// will be interrupted in case of order

        Random random = new Random();
        TM.sleep(random.nextInt(1000, 5000), () -> {});

        double x = unit.getX() + random.nextDouble(-5, 5);
        double y = unit.getY() + random.nextDouble(-5, 5);

        move(x, y);
	}

}
