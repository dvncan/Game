package ankhmorpork.model.actions.base;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * End the current player turn, init next player turn
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class NextPlayerTurn extends Action
{
	/**
	 * End the current player turn, init next player turn
	 * @param gameManager the controller to show the game status
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		gameManager.nextTurn();
		
		return true;
	}

	/**
	 * Show personalized message
	 */
	@Override
	public String toString()
	{
		return "End your turn.";
	}
}
