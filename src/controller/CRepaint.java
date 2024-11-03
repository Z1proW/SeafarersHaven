package controller;

import view.View;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * refreshes the view
 */
class CRepaint extends TimerTask
{

	/**
	 * frame rate in frames per second
	 * synchronized with the display refresh rate
	 */
	private static final int FRAMERATE =
			GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDisplayMode().getRefreshRate();

	private final View view;

	/**
	 * constructor
	 * starts the task with a timer
	 */
	CRepaint(View view)
	{
		this.view = view;
		new Timer().schedule(this, 0, 1000 / FRAMERATE);
	}

	@Override
	public void run()
	{
		view.repaint();
	}

}
