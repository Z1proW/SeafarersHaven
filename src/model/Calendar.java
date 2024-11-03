package model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Calendar extends TimerTask
{

	private int day = 0;
	private final int dayLength = 1000 * 120;  // 1 day = 2 minutes
	private static final Season season = Season.values()[new Random().nextInt(Season.values().length)];

	Calendar()
	{
		new Timer().schedule(this, 0, dayLength);
	}

	@Override
	public void run()
	{
		day++;
	}

	/**
	 * @return the current calendar day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the length of a day in milliseconds
	 */
	public int getDayLength() {
		return dayLength;
	}

	/**
	 * @return the current season
	 */
	public static Season getSeason() // TODO remove static
	{
		return season;
	}

}
