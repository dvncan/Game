package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Steal money from a player of your choice
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class StealMoneyFromPlayerChoice extends PlayerCardAction
{
	//<PlayerCard id="62" color="green" name="Nobby Nobbs" actions="TEXT;CARD" scroll="StealMoneyFromPlayerChoice;3"/>
	//optional="Take $3 from a player of your choice."

	private final int amount;
	
	/**
	 * Constructor: Steal money from a player of your choice
	 * @param args contain 1 argument, the amount of money to steal
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public StealMoneyFromPlayerChoice(List<String> args) throws Exception
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
		String message = "Select a player to steal " + this.amount + "$ from.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Money, message);
		boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
		if (!stop)
		{
			player.getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.amount, false);
		}
		
		return true;
	}
}
