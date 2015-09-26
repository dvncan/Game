package ankhmorpork.model.actions;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * Show the entire game status
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ShowGameStatus extends Action
{
	/**
	 * Show the entire game status
	 * @param gameManager the controller to show the game status
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		gameManager.getCurrentPlayer().getDataInput().printMessage(gameManager.getEntireGameStatus());
		
		return true;
	}

	/**
	 * Show personalized message
	 */
	@Override
	public String toString()
	{
		return "Show the current game state.";
	}
}
