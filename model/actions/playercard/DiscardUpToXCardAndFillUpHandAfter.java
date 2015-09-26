package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.ObjectDecorator;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Definition;

/**
 * Discard up to X cards and refill your hand after (normally 5 cards)
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DiscardUpToXCardAndFillUpHandAfter extends PlayerCardAction
{
	//<PlayerCard id="97" color="brown" name="The Alchemists' Guild" actions="TEXT;BLDG" scroll="DiscardUpToXCardAndFillUpHandAfter;3"/>
	//optional="Discard up to X cards and refill your hand after (normally 5 cards)."

	private final int amountOfCardsToDiscard;
	
	/**
	 * Constructor: Discard up to X cards and refill your hand after (normally 5 cards).
	 * @param args contain 1 argument, the amount of card to discard
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DiscardUpToXCardAndFillUpHandAfter(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountOfCardsToDiscard = getArgument(args, 0);
	}
	
	/**
	 * Discard up to X cards and refill your hand after (normally 5 cards).
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		ObjectDecorator<Boolean> noMoreCard = new ObjectDecorator<Boolean>(false, Definition.STOP_DISCARDING_CARDS);
		
		String message = "Select a card to discard (up to " + this.amountOfCardsToDiscard + " cards) you will refill your hand to 5 cards afterward.";
		Player player = gameManager.getCurrentPlayer();
		
		boolean stopDiscarding = false;
		for(int i = 0; i < this.amountOfCardsToDiscard && !stopDiscarding; i++)
		{
			List<Object> listChoices = new ArrayList<Object>();
			listChoices.add(noMoreCard);
			listChoices.addAll(player.getPlayerCardDeck());
			Object choice = player.getDataInput().ask(message, listChoices);
			if(choice instanceof Card)
			{
				gameManager.getBoard().discardCard(player.getPlayerCardDeck().pickCardByID(((Card)choice).getId()));
			}
			else
			{
				stopDiscarding = true;
			}
		}
		
		int amountCardToPick = gameManager.getAmountOfPlayerCardToFillYourHand() - player.getPlayerCardDeck().size();
		for(int i = 0; i < amountCardToPick; i++)
		{
			player.getPlayerCardDeck().addCard(gameManager.getNextPlayerCard());
		}

		return true;
	}
}
