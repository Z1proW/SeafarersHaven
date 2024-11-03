package sound;

public enum Sound implements Hearable
{

	CLICK("click");
	// TODO add sounds

	private final String fileName;

	/**
	 * constructor
	 *
	 * @param fileName the name of the file
	 */
	Sound(String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

}
