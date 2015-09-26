package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Add demons to random area
 * @author Team 2
 * @since Build 2
 */
public class AddRandomAreaDemon extends RandomEventCardAction
{
	private final int repeat;
	
	/**
	 * Add demons to random area
	 * @param args
	 * @throws Exception 
	 */
	public AddRandomAreaDemon(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.repeat = getArgument(args, 0);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for (int i = 0; i < repeat; i ++)
		{
			try
			{
				boolean eventStopped = false;
				Area randomArea = ActionManager.getRandomArea(gameManager);
				for(Player player : ActionManager.getPlayerListInOrder(gameManager, true))
				{
					if (randomArea.hasBuilding(player) || randomArea.hasMinion(player))
					{
						boolean stop = ActionManager.stopAction(gameManager, player, randomArea, new StopTarget[]{StopTarget.RandomEventLandmark});
						if (stop)
						{
							eventStopped = true;
							break;
						}
					}
				}
				
				if (!eventStopped)
				{
					ActionManager.addDemon(gameManager, randomArea);
					break;
				}
			}
			catch (Exception ex)
			{
				gameManager.getSystemDataInput().showError(ex.getMessage());
			}
		}
		
		return true;
	}
	
}
