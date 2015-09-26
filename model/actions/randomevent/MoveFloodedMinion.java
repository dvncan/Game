package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Move minion from flooded areas
 * @author Team 2
 * @since Build 2
 */
public class MoveFloodedMinion extends RandomEventCardAction
{
	private final int repeat;
	
	/**
	 * Move minion from flooded areas
	 * @param args
	 * @throws Exception 
	 */
	public MoveFloodedMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.repeat = getArgument(args, 0);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			ArrayList<Area> floodedArea = new ArrayList<Area>();
			for(int i = 0; i < repeat; i++)
			{
				Area area = ActionManager.getRandomArea(gameManager);
				if (area.isNearRiver())
				{
					if (floodedArea.contains(area))
					{
						gameManager.getSystemDataInput().printMessage(area.getName() + " is even more flooded.");
					}
					else
					{
						floodedArea.add(area);
						gameManager.getSystemDataInput().printMessage(area.getName() + " is flooded.");
					}
				}
				else
				{
					gameManager.getSystemDataInput().printMessage(area.getName() + " is not next to the river, therefore unaffected by flood.");
				}
			}
			
			boolean hasMinion = false;
			for(Player player : ActionManager.getPlayerListInOrder(gameManager, true))
			{
				for (Area areaOrigin : floodedArea)
				{
					while (areaOrigin.hasMinion(player))
					{
						hasMinion = true;
						List<Area> neighbors = areaOrigin.getNeighborOnly();
						neighbors.removeAll(floodedArea);
						
						Area areaDestionation = player.getDataInput().ask("Area to which you want to move a minion.", neighbors);
						boolean stop = ActionManager.stopAction(gameManager, player, areaOrigin, new StopTarget[]{StopTarget.RandomEventLandmark, StopTarget.MoveMinion});
						if (!stop)
						{
							ActionManager.removeMinion(gameManager, areaOrigin, player);
							ActionManager.addMinion(gameManager, areaDestionation, player);
						}
					}
				}
			}
			
			if (!hasMinion)
			{
				gameManager.getSystemDataInput().printMessage("There is no minion to be displaced by the flood.");
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
