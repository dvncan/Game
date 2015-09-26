package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Pickup card from discard pile
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DrawPlayerCardFromDiscardPile extends PlayerCardAction
{
	//<PlayerCard id="54" color="green" name="History Monks" actions="TEXT;PERS" scroll="DrawPlayerCardFromDiscardPile;4"/>
	//optional="Shuffle the discard pile and draw four cards randomly. Place the remaining cards back as the discard pile."

	private final int amountOfCards;
	
	/**
	 * Constructor Pickup card from discard pile
	 * @param args contain 0 argument
	 * @throws Exception if the argument doesn't match what expected
	 */
	public DrawPlayerCardFromDiscardPile(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountOfCards = getArgument(args, 0);
	}
	
	/**
	 * Shuffle the discard card deck
	 * Pickup X random card within the deck.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		gameManager.getBoard().getDiscardDeck().shuffleDeck();
		for(int i = 0; i < this.amountOfCards && !gameManager.getBoard().getDiscardDeck().isEmpty(); i++)
		{
			int cardPosition = Environment.randInt(0, gameManager.getBoard().getDiscardDeck().size() - 1);
			gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(gameManager.getBoard().getDiscardDeck().remove(cardPosition));
		}

		return true;
	}
}
