package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Force one player to give another player $X (you cannot choose yourself).
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ForcePlayerToGiveMoneyToPlayer extends PlayerCardAction
{
	//<PlayerCard id="123" color="brown" name="Hubert" actions="TEXT;PERS" scroll="ForcePlayerToGiveMoneyToPlayer;3"/>
	//optional="Force one player to give another player $3 (you cannot choose yourself)."
	
	private final int amount;

	/**
	 * Constructor: 
	 * Force one player to give another player $X (you cannot choose yourself).
	 * @param args amount of money to transfer
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public ForcePlayerToGiveMoneyToPlayer(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		amount = getArgument(args, 0);
	}
	
	/**
	 * Force one player to give another player $X (you cannot choose yourself).
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player currentPlayer = gameManager.getCurrentPlayer();
		if(gameManager.getPlayerList().size() == 2)
		{
			// Card won't work, not enough players
			currentPlayer.getDataInput().showError("Not enough players to force one to give money to another.");
		}
		else
		{
			Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Money, "Select player to give " + this.amount + "$ to another player.");
			boolean stop = ActionManager.stopAction(gameManager, player, null, new StopTarget[]{StopTarget.Text });
			if (!stop)
			{
				List<Player> playerList = new ArrayList<Player>();
				playerList.addAll(gameManager.getPlayerList());
				playerList.remove(currentPlayer);
				playerList.remove(player);

				Player playerDestination = gameManager.getCurrentPlayer().getDataInput().ask("Who should be getting the money?", playerList);
				player.getBankAccount().transfertAmountTo(playerDestination, this.amount, false);
				return true;
			}
		}
		
		return false;
	}
}
