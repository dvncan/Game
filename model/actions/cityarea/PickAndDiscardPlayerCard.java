package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import java.util.List;

/**
 * Pick and discard player card
 * @author Team 2
 * @since Build 2
 */
public class PickAndDiscardPlayerCard extends CityCardAction
{
	//<CityAreaCard id="24" name="Unreal Estate" area="2" cost="18" scroll="PickAndDiscardPlayerCard"/>
	
	public PickAndDiscardPlayerCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.areaNumber = getArgument(args, 0);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		boolean success = false;
		Player currentPlayer = gameManager.getCurrentPlayer();
		if (!gameManager.getBoard().getPlayerCardDeck().isEmpty())
		{
			Card newCard = gameManager.getNextPlayerCard();
			if (newCard != null)
			{
				currentPlayer.getPlayerCardDeck().addCard(newCard);
				currentPlayer.getDataInput().printMessage("You have picked up " + newCard.getName() + ".");
				
				Card cardToDiscard = gameManager.getCurrentPlayer().getDataInput().ask("Select a PlayerCard to discard.", currentPlayer.getPlayerCardDeck());
				if(currentPlayer.getPlayerCardDeck().remove(cardToDiscard))
				{
					gameManager.getBoard().discardCard(cardToDiscard);
					currentPlayer.getDataInput().printMessage("You have discarded " + cardToDiscard.getName() + ".");
					success = true;
				}
			}
		}
		else
		{
			currentPlayer.getDataInput().showError("There is no player card to pick up.");
		}
		return success;
	}
}
