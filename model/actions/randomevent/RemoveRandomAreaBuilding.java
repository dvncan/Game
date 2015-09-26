package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Remove building from random area
 * @author Team 2
 * @since Build 2
 */
public class RemoveRandomAreaBuilding extends RandomEventCardAction
{
	private final int repeat;
	
	/**
	 * Remove building from random area
	 * @param args
	 * @throws Exception 
	 */
	public RemoveRandomAreaBuilding(List<String> args) throws Exception
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
				Area randomArea = ActionManager.getRandomArea(gameManager);
				if (randomArea.hasBuilding())
				{
					Player owner = (Player)randomArea.getBuilding().getOwner();
					boolean stop = ActionManager.stopAction(gameManager, owner, randomArea, new StopTarget[]{StopTarget.RandomEventLandmark});
					if (!stop)
					{
						ActionManager.removeBuilding(gameManager, randomArea, null);
					}
				}
				else
				{
					gameManager.getSystemDataInput().printMessage("There is no building to be destroyed in " + randomArea.getName() + ".");
				}
			}
			catch(Exception ex)
			{
				gameManager.getSystemDataInput().showError(ex.getMessage());
			}
		}
		return true;
	}
}
