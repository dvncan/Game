package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Steal X$ money from every players
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class StealMoneyFromAllPlayers extends PlayerCardAction
{
	//<PlayerCard id="79" color="green" name="The thieves' guild" actions="TEXT;PERS" scroll="StealMoneyFromAllPlayers;2"/>
	//<PlayerCard id="32" color="green" name="Mr Boggis" actions="TEXT;PERS" scroll="StealMoneyFromAllPlayers;2"/>
	//optional="Take $2, if possible from every other player."

	private final int amount;
	
	/**
	 * Constructor: Steal X$ money from every players
	 * @param args contain 1 argument, the amount of money to steal
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public StealMoneyFromAllPlayers(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}

	/**
	 * Take X$ from every players
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(Player player : gameManager.getPlayerList())
		{
			if(player.getId() != gameManager.getCurrentPlayerID())
			{
				boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
				if (!stop)
				{
					player.getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.amount, false);
				}
			}
		}
		
		return true;
	}
}
