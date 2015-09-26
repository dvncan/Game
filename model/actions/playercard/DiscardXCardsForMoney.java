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
 * DiscardXCardsForMoney
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DiscardXCardsForMoney extends PlayerCardAction
{
	//<PlayerCard id="67" color="green" name="Shonky Shop" actions="TEXT;BLDG" scroll="DiscardXCardsForMoney;1"/>
	//<PlayerCard id="57" color="green" name="Harry King" actions="PERS;TEXT" scroll="DiscardXCardsForMoney;2"/>
	//optional="Discard as many cards as you wish and take $1 for each one discarded."
	//optional="Discard as many cards as you wish and take $2 for each one discarded."

	private final int profitPerCard;
	
	/**
	 * Constructor Discard x cards and earn money for each card
	 * @param args two arguments, number of cards and profit per card
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DiscardXCardsForMoney(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		profitPerCard = getArgument(args, 0);
	}
	
	/**
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		
		ObjectDecorator<Boolean> noMoreCard = new ObjectDecorator<Boolean>(false, Definition.STOP_DISCARDING_CARDS);
		
		String message = "Select a card to discard and earn $" + this.profitPerCard;
		Player player = gameManager.getCurrentPlayer();
		
		boolean stopDiscarding = false;
		while(!stopDiscarding && !player.getPlayerCardDeck().isEmpty())
		{
			List<Object> listChoices = new ArrayList<Object>();
			listChoices.add(noMoreCard);
			listChoices.addAll(player.getPlayerCardDeck());
			Object choice = player.getDataInput().ask(message, listChoices);
			if(choice instanceof Card)
			{
				gameManager.getBoard().discardCard(player.getPlayerCardDeck().pickCardByID(((Card)choice).getId()));
				gameManager.getBoard().getBankAccount().transfertAmountTo(player, profitPerCard, false);
			}
			else
			{
				stopDiscarding = true;
			}
		}
		return true;
	}
}
