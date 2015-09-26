package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * A random city area card is returned to board, its owner must remove a minion in that area
 * @author Team 2
 * @since Build 2
 */
public class RemoveRandomAreaCardAndMinion extends RandomEventCardAction
{
	/**
	 * A random city area card is returned to board, its owner must remove a minion in that area
	 * @param args
	 * @throws Exception 
	 */
	public RemoveRandomAreaCardAndMinion(List<String> args) throws Exception
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
			if (randomArea.hasBuilding())
			{
				Player owner = (Player)randomArea.getBuilding().getOwner();
				boolean stopCard = ActionManager.stopAction(gameManager, owner, randomArea, new StopTarget[]{StopTarget.RandomEventLandmark});
				if (!stopCard)
				{
					owner.loseCityAreaCard(randomArea);
					gameManager.getSystemDataInput().printMessage("The city area card for " + randomArea + " is removed from " + owner.getName() + ".");
				}
				
				if (randomArea.hasMinion(owner))
				{
					boolean stopMinion = ActionManager.stopAction(gameManager, owner, randomArea, new StopTarget[]{StopTarget.RandomEventLandmark, StopTarget.RemoveMinion});
					if (!stopMinion)
					{
						ActionManager.removeMinion(gameManager, randomArea, owner);
					}
				}
			}
			else
			{
				gameManager.getSystemDataInput().printMessage("The city area card for " + randomArea + " is not in play.");
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
