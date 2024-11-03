package sound;

import java.util.Random;

public enum Music implements Hearable
{

	DUEL("Duel"),
	FOREST("Forest"),
	SHADOWS("Shadows"),
	TITAN("Titan");

	private final String fileName;

	/**
	 * constructor
	 *
	 * @param fileName the name of the file
	 */
	Music(String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * plays music randomly in a loop
	 */
	public static void playRandomMusic()
	{
		Music[] MUSIC = Music.values();
		Hearable music = MUSIC[new Random().nextInt(MUSIC.length)];

		music.play(Music::playRandomMusic);
	}

}
