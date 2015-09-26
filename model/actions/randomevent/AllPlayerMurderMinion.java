package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Each player must kill a minion on a random area
 * @author Team 2
 * @since Build 2
 */
public class AllPlayerMurderMinion extends RandomEventCardAction
{
	/**
	 * Each player must kill a minion on a random area
	 * @param args
	 * @throws Exception 
	 */
	public AllPlayerMurderMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			for(Player player : ActionManager.getPlayerListInOrder(gameManager, true))
			{
				Area randomArea = ActionManager.getRandomArea(gameManager);
				if (randomArea.hasMinion())
				{
					ArrayList<Player> playerHavingMinion = new ArrayList<Player>();
					for (Player gamePlayer: gameManager.getPlayerList())
					{
						if (randomArea.hasMinion(gamePlayer))
						{
							playerHavingMinion.add(gamePlayer);
						}
					}

					Player minionPlayer = player.getDataInput().ask("Select the player whose minion you want to remove:", new ArrayList<Player>(playerHavingMinion));
					boolean stop = ActionManager.stopAction(gameManager, minionPlayer, randomArea, new StopTarget[]{StopTarget.RandomEventLandmark, StopTarget.RemoveMinion});
					if (!stop)
					{
						ActionManager.removeMinion(gameManager, randomArea, minionPlayer);
					}
				}
				else
				{
					gameManager.getSystemDataInput().printMessage("No minion in " + randomArea + " for " + player.getName() + " to murder.");
				}
			}
			return true;
		}
		catch (Exception ex)
		{
			gameManager.getSystemDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
