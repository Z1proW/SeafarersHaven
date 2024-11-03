import controller.Control;
import model.Model;
import sound.Music;
import view.Texture;
import view.View;

import java.awt.*;

public class Game
{

	/**
	 * the name of the game
	 */
	public static final String TITLE = "Seafarer's Haven";
	/**
	 * the icon of the window
	 */
	public static final Image ICON = Texture.getImage("icon.png");

	/**
	 * creates the MVC architecture
	 */
	public static void start(int mapSize)
	{
		Model model = new Model(mapSize, mapSize);
		View view = new View(model);
		new Control(model, view);

		view.setTitle(TITLE);
		view.setIconImage(ICON);

		// démarre la musique
		Music.playRandomMusic();
	}

}
