package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Spread fire and burn buildings
 * @author Team 2
 * @since Build 2
 */
public class SpreadBuildingFire extends RandomEventCardAction
{
	/**
	 * Spread fire and burn buildings
	 * @param args
	 * @throws Exception 
	 */
	public SpreadBuildingFire(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			Area randomArea = ActionManager.getRandomArea(gameManager);
			Area previousArea = null;
			
			boolean spread = true;
			while(spread)
			{
				gameManager.getSystemDataInput().printMessage(randomArea.getName() + " is on fire.");
				boolean hasBuilding = randomArea.hasBuilding();
				boolean isNeighbor = false;
				if (hasBuilding)
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
					gameManager.getSystemDataInput().printMessage("There is no building to burn down in " + randomArea.getName() + ".");
				}
				
				if (hasBuilding)
				{
					previousArea = randomArea;
					randomArea = ActionManager.getRandomArea(gameManager);
					isNeighbor = randomArea.isNeighbor(previousArea);
					if (previousArea == randomArea)
					{
						gameManager.getSystemDataInput().printMessage("There is nothing left to burn in " + randomArea.getName() + ".");
					}
					else if (!isNeighbor)
					{
						gameManager.getSystemDataInput().printMessage(randomArea.getName() + " is not a neighbor of " + previousArea.getName() + ".");
					}
				}
				
				spread = hasBuilding && isNeighbor;
			}
			
			gameManager.getSystemDataInput().printMessage("The fire stops spreading.");
			return true;
		}
		catch (Exception ex)
		{
			gameManager.getSystemDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
