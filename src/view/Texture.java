package view;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Texture
{

	/**
	 * A cache of all the images that have been loaded.
	 */
	private static final Map<String, BufferedImage> imageCache = new HashMap<>();

	/**
	 * Get an image from the cache, or load it if it's not in the cache.
	 *
	 * @param path the path to the image file, with the extension.
	 * @return The image.
	 */
	public static BufferedImage getImage(String path)
	{
		return imageCache.computeIfAbsent(path, Texture::loadImage);
	}

	public static Image getGif(String path)
	{
		try
		{
			URL url = Texture.class.getResource("/images/" + path);
			if(url == null)
				throw new IOException("File not found");

			return Toolkit.getDefaultToolkit().getImage(url);
		}
		catch(IOException e)
		{
			System.err.println("Missing image: " + path);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}

	/**
	 * Load an image from the file system.
	 *
	 * @param path the path to the image file, with the extension.
	 * @return The image.
	 */
	private static BufferedImage loadImage(String path)
	{
		try
		{
			URL url = Texture.class.getResource("/images/" + path);
			if(url == null)
				throw new IOException("File not found");

			return ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Missing image: " + path);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}

}
