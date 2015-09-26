package ankhmorpork.manager;

/**
 * Abstract manager for file save/load
 * @author Team 2
 * @since Build 1
 */
public abstract class AbstractFileManager
{
	/**
	 * @return Load a save file to create a game manager
	 * @throws Exception GameManager wasn't created
	 */
	public GameManager load() throws Exception
	{
		return null;
	}

	/**
	 * File Save
	 * @param gameManager to serialize
	 * @throws Exception the save file wasn't created
	 */
	public void save(GameManager gameManager) throws Exception
	{
	}
}
