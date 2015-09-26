package ankhmorpork.model.actions.personality;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import java.util.List;

/**
 * Win if no player card
 * @author Team 2
 * @since Build 2
 */
public class NoPlayerCard extends Action
{	
	public NoPlayerCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		boolean endgame = gameManager.getBoard().getPlayerCardDeck().isEmpty();
		if (endgame)
		{
			gameManager.endGame(GameManager.EndGameType.winAtBeginTurn);
		}
		return endgame;
	}
}
