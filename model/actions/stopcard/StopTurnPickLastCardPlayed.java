package ankhmorpork.model.actions.stopcard;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.StopAction;

import java.util.List;

/**
 * Pick and discard player card
 * @author Team 2
 * @since Build 2
 */
public class StopTurnPickLastCardPlayed extends StopAction
{
	public StopTurnPickLastCardPlayed(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		addStopTarget(StopTarget.Turn);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Card card = gameManager.getBoard().getLastPlayedCard();
		gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(card);
		gameManager.nextTurn();
		return true;
	}
	
	/**
	 * We invert the logic, this card can only be played after a player card has been played
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	@Override
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed)
	{
		return !playerCardPlayed;
	}

	/**
	 * We invert the logic, this card can only be played after a player card has been played
	 * @param gameManager to verify the logic
	 * @return if it can be executed
	 */
	@Override
	public boolean canExecute(GameManager gameManager) 
	{
		return !gameManager.isCanPlayPlayerCard();
	}
}
