package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import java.util.ArrayList;

/**
 * SelectPlayerGetMoneyForCard
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SelectPlayerGetMoneyForCard extends PlayerCardAction
{
	//<PlayerCard id="69" color="green" name="Rosie Palm" actions="PERS;TEXT" scroll="SelectPlayerGetMoneyForCard;2"/>
	//<PlayerCard id="77" color="green" name="The Seamstresses' Guild" actions="TEXT;PERS" scroll="SelectPlayerGetMoneyForCard;2"/>
	//optional="Choose one player. Give them one of your cards. They must give you $2 in return."
	
	private final int amount;
	
	/**
	 * Constructor: Steal money from a player of your choice
	 * @param args contain 1 argument, the amount of money to steal
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public SelectPlayerGetMoneyForCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);

	}

	/**
	 * Select a player and take X$ from him
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select a player to receive " + this.amount + "$ by giving them a card.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		
		if (currentPlayer.getPlayerCardDeck().isEmpty())
		{
			currentPlayer.getDataInput().showError("You do not have player cards to give in exchange for money.");
			return false;
		}
		
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Money, message);
		boolean stop = ActionManager.stopAction(gameManager, player, null, new StopTarget[]{ StopTarget.Text });
		if (!stop)
		{
			Card cardToGive = currentPlayer.getDataInput().ask("Which card do you want to give away?", new ArrayList<Card>(currentPlayer.getPlayerCardDeck()));

			player.getPlayerCardDeck().addCard(currentPlayer.getPlayerCardDeck().pickCardByID(cardToGive.getId()));
			player.getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.amount, false);
			return true;
		}
		
		return false;
	}
}
