package ankhmorpork.model.actions.base;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.BasicAction;

/**
 * PlayRandomEvent
 * @author Team 2
 * @since Build 2
 */
public class PlayRandomEvent extends BasicAction
{
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Card nextRandomEvent = gameManager.getBoard().getRandomEventCardDeck().pickTopCard();
		if (nextRandomEvent != null)
		{
			gameManager.getSystemDataInput().printMessage("Random event " + nextRandomEvent.toString());
			gameManager.getBoard().discardCardToBin(nextRandomEvent);
			for(Action action : nextRandomEvent.getActions())
			{
				action.execute(gameManager);
			}
		}
		return true;
	}
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Random Event";
	}

	/**
	 * @return card must be played
	 */
	@Override
	public boolean isOptional()
	{
		return false;
	}
}
