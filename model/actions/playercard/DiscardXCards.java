package ankhmorpork.model.actions.playercard;

import java.util.List;


import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Discard x cards.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DiscardXCards extends PlayerCardAction
{
 //<PlayerCard id="125" color="brown" name="The Luggage" actions="KILL;TEXT" scroll="DiscardXCards;1"/>
 //<PlayerCard id="63" color="green" name="Modo" actions="TEXT;PERS" scroll="DiscardXCards;1"/>
 //scroll="5;1"	 optional="Discard one card."
	
	private final int nbCardsToDiscard;
	
	/**
	 * Constructor: Discard x cards.
	 * @param args contain 1 argument, the amount of card to discard
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DiscardXCards(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.nbCardsToDiscard = getArgument(args, 0);
	}
	
	
	@Override
	/**
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 * 
	 */
	public boolean execute(GameManager gameManager) 
	{
		
		Player player = gameManager.getCurrentPlayer();
		
		if(!player.getPlayerCardDeck().isEmpty())
		{
			for(int i =0 ; i<nbCardsToDiscard && !player.getPlayerCardDeck().isEmpty(); i++)
			{
				Card cardToDiscard = player.getDataInput().ask("Select a PlayerCard to discard.", player.getPlayerCardDeck());
				if(player.getPlayerCardDeck().remove(cardToDiscard))
				{
					gameManager.getBoard().getDiscardDeck().addCard(cardToDiscard);
				}	
			}
		}
		else 
		{
			player.getDataInput().printMessage("You don't have any cards to be taken.");
		}
		return true;
		
	}
}
