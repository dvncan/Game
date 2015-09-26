package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Look at a player PlayerCards and select X to be discarded
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SelectPlayerViewAllPlayerCardAndDiscardOne extends PlayerCardAction
{
	//<PlayerCard id="88" color="brown" name="Cable Street Particulards" actions="TEXT;PERS" scroll="SelectPlayerViewAllPlayerCardAndDiscardOne"/>
	//optional="Select one player. Look at his cards. Choose one of them to be discarded."

	/**
	 * Constructor: Look at a player PlayerCards and select X to be discarded
	 * @param args contain 1 argument, the amount of card to look at
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public SelectPlayerViewAllPlayerCardAndDiscardOne(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Look at a player PlayerCards and select X to be discarded
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select a player to look at all he's player cards and discard one.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Cards, message);
		
		if (player.getPlayerCardDeck().isEmpty())
		{
			currentPlayer.getDataInput().showError("This player has no player card to discard.");
			return false;
		}
		
		boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
		if (!stop)
		{

			Card cardToDiscard = gameManager.getCurrentPlayer().getDataInput().ask("Select a PlayerCard to discard.", player.getPlayerCardDeck());
			if(player.getPlayerCardDeck().remove(cardToDiscard))
			{
				gameManager.getBoard().getDiscardDeck().addCard(cardToDiscard);
			}
			return true;
		}
		
		return false;
	}
}
