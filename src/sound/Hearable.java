package sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.logging.Logger;

/**
 * all the sounds of the program
 */
interface Hearable
{

	/**
	 * @return the name of the file without its extension
	 */
	String getFileName();

	/**
	 * plays this sound
	 */
	default void play()
	{
		play(() -> {});
	}

	/**
	 * plays this sound
	 *
	 * @param onStop code to be executed when sound stops playing
	 */
	default void play(Runnable onStop)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("resources/sounds/" + getFileName() + ".wav")));
			clip.start();

			clip.addLineListener(event ->
			{
				if(event.getType() == LineEvent.Type.STOP)
					onStop.run();
			});
		}
		catch(Exception e)
		{
			Logger.getLogger(this.toString()).info("Hearable could not be played");
			e.printStackTrace();
		}
	}

}
