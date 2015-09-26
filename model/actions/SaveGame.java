package ankhmorpork.model.actions;

import java.io.File;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.util.Definition;

/**
 * Save the game state
 * 
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SaveGame extends Action {
	/**
	 * Save the game state
	 * 
	 * @param gameManager
	 *          the controller to show the game status
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) {
		String filename = gameManager.getCurrentPlayer().getDataInput()
				.getStringFromKeyboard("Input a filename to save the current game state.")
				+ Definition.GAME_FILE_EXTENSION;

		try 
		{
			File fileTemp = new File(filename);
			if (fileTemp.exists()) 
			{
				fileTemp.delete();
			}
			gameManager.saveGame(filename);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * Show personalized message
	 */
	@Override
	public String toString()
	{
		return "Save Current Game State.";
	}
}
