package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * May Change Personality Card
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ChangePersonalityCard extends PlayerCardAction
{
	//<PlayerCard id="74" color="green" name="Zorgo the Retro-phrenologist" actions="TEXT;BLDG" scroll="ChangePersonalityCard"/>
	//optional="You may exchange your Personality card with one drawn randomly from those not in use."
	
	/**
	 * Constructor May Change Personality Card
	 * @param args contain 0 argument
	 * @throws Exception if the argument doesn't match what expected
	 */
	public ChangePersonalityCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Shuffle the personality card deck
	 * Pickup the top random personality card.
	 * Ask the current player which one he want to keep.
	 * Replace the personality card if needed
	 * Put the other personality card back in the unused personality card deck
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		gameManager.getBoard().getPersonalityCardDeck().shuffleDeck();
		Card randomCard = gameManager.getBoard().getPersonalityCardDeck().pickTopCard();
		List<Card> lstPersonalityCard = new ArrayList<Card>();
		lstPersonalityCard.add(randomCard);
		lstPersonalityCard.add(gameManager.getCurrentPlayer().getPersonalityCard());
		Card cardToKeep = gameManager.getCurrentPlayer().getDataInput().ask("Which personality card do you want to keep?", lstPersonalityCard);
		
		if(!gameManager.getCurrentPlayer().getPersonalityCard().equals(cardToKeep))
		{
			// we switch the card with the one pickup and put other one back in the unused personality card deck
			gameManager.getBoard().getPersonalityCardDeck().addCard(gameManager.getCurrentPlayer().getPersonalityCard());
			gameManager.getCurrentPlayer().setPersonalityCard(cardToKeep);
		}

		return true;
	}
}
