package ankhmorpork.model.actions.base;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.BasicAction;

/**
 * PlayAnotherCard
 * @author Team 2
 * @since Build 2
 */
public class PlayAnotherCard extends BasicAction
{
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		List<Card> cardList = new ArrayList<Card>();
		for(Card card : gameManager.getCurrentPlayer().getPlayerCardDeck())
		{
			if(card.canBeExecutedTurnBase(true) && card.isActive() && card.canExecute(gameManager) && card.isEnabled())
			{
				cardList.add(card);
			}
		}

		if(!cardList.isEmpty())
		{
			Card card = gameManager.getCurrentPlayer().getDataInput().ask("Select Card to Play :", cardList);
			gameManager.executeCardAction(card);
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
		return "Play another card";
	}
}
