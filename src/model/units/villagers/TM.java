package model.units.villagers;

public abstract class TM
{

	/**
	 * Sleeps for a given time, and runs a given code if the thread is interrupted
	 * @param sleepTime time to sleep
	 * @param onInterrupt code to run if the thread is interrupted
	 */
	public static void sleep(int sleepTime, Runnable onInterrupt)
	{
		if(Thread.interrupted())
		{
			onInterrupt.run();
			throw new Interrupted();
		}

		try
		{
			Thread.sleep(sleepTime);
		}
		catch(InterruptedException e)
		{
			onInterrupt.run();
			throw new Interrupted();
		}
	}

	/**
	 * Runs a given code, and runs a given code if the thread is interrupted
	 * @param code code to run
	 * @param onFail code to run if the thread throws an Exited exception
	 * @param onInterrupt code to run if the thread is interrupted
	 */
	public static void runFail(Runnable code, Runnable onFail, Runnable onInterrupt)
	{
		try
		{
			code.run();
		}
		catch(Interrupted e)
		{
			onInterrupt.run();
			throw new Interrupted();
		}
		catch(Exited e)
		{
			onFail.run();
		}
	}

	/**
	 * Checks if a given condition is true, and throws an Exited exception if it is not
	 * @param condition condition to check
	 */
	public static void check(boolean condition)
	{
		if(!condition) throw new Exited();
	}

	public static class Exited extends RuntimeException {}

	public static class Interrupted extends RuntimeException {}

}
