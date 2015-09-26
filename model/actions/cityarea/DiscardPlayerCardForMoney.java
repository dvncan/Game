package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;

import java.util.List;

/**
 * Discard player card and get money
 * @author Team 2
 * @since Build 2
 */
public class DiscardPlayerCardForMoney extends CityCardAction
{
	//<CityAreaCard id="21" name="The Scours" area="5" cost="6" scroll="DiscardPlayerCardForMoney;2"/>
	
	private final int amount;
	
	/**
	 * Discard player card and get money
	 * @param args
	 * @throws Exception 
	 */
	public DiscardPlayerCardForMoney(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
		this.areaNumber = getArgument(args, 1);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player currentPlayer = gameManager.getCurrentPlayer();
		if (currentPlayer.getPlayerCardDeck().isEmpty())
		{
			currentPlayer.getDataInput().showError("You do not have player card to discard.");
			return false;
		}
		
		Card cardToDiscard = gameManager.getCurrentPlayer().getDataInput().ask("Select a PlayerCard to discard.", currentPlayer.getPlayerCardDeck());
		if(currentPlayer.getPlayerCardDeck().remove(cardToDiscard))
		{
			gameManager.getBoard().discardCard(cardToDiscard);
			currentPlayer.getDataInput().printMessage("You have discarded " + cardToDiscard.getName() + ".");
			gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.amount, false);
		}
		return true;
	}
}
