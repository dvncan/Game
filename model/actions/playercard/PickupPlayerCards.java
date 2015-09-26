package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Pickup extra card from the player card deck
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PickupPlayerCards extends PlayerCardAction
{
//<PlayerCard id="55" color="green" name="Hex" actions="TEXT;BLDG" scroll="PickupPlayerCards;3"/>
//<PlayerCard id="65" color="green" name="Librarian" actions="TEXT" scroll="PickupPlayerCards;4"/>
//<PlayerCard id="66" color="green" name="Leonard of Quirm" actions="TEXT" scroll="PickupPlayerCards;4"/>
//<PlayerCard id="80" color="brown" name="Sergeant Cheery Littlebottom" actions="TEXT;NOTR" scroll="PickupPlayerCards;2"/>
//<PlayerCard id="82" color="brown" name="The Clacks" actions="TEXT;GOLD;CARD" money="2" scroll="PickupPlayerCards;2"/>
//<PlayerCard id="114" color="brown" name="Moist von Lipwig" actions="PERS;GOLD;TEXT;CARD" money="3" scroll="PickupPlayerCards;2"/>
//<PlayerCard id="120" color="brown" name="Professor of Recent Runes" actions="EVNT;TEXT;CARD" scroll="PickupPlayerCards;2"/>
//optional="Take two cards from the draw deck."
//optional="Take three cards from the draw deck."
//optional="Take four cards from the draw deck."
	
	private final int nbOfCard;

	/**
	 * Constructor Pickup extra card from the player card deck
	 * @param args contain 1 argument, the amount of card to pickup
	 * @throws Exception if the argument doesn't match what expected
	 */
	public PickupPlayerCards(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.nbOfCard = getArgument(args, 0);
	}
	
	/**
	 * Pickup cards from the player card deck on the board
	 * @param gameManager
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(int i = 0; i < this.nbOfCard; i++)
		{
			gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(gameManager.getNextPlayerCard());
		}
		return true;
	}
}
