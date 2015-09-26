package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.collection.Deck;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Look at all but one of the unused Personality Cards.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class LookAllUnusedPersonalityCardExceptOne extends PlayerCardAction
{
	//<PlayerCard id="45" color="green" name="Mrs Cake" actions="TEXT;GOLD;BLDG" money="2" scroll="LookAllUnusedPersonalityCardExceptOne"/>
	//optional="Look at all but one of the unused Personality Cards."
	
	/**
	 * Constructor: 
	 * 
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public LookAllUnusedPersonalityCardExceptOne(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Look at all but one of the unused Personality Cards.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Deck<Card> unusedPersonalityCards = gameManager.getBoard().getPersonalityCardDeck();
		for(int i = 0; i < unusedPersonalityCards.size() - 1; i++)
		{
			Card card = unusedPersonalityCards.get(i);
			gameManager.getCurrentPlayer().getDataInput().printMessage(card.toString());
		}
		return true;
	}
}
