package ankhmorpork.model.actions.stopcard;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.StopAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Stop and move removed minion
 * @author Team 2
 * @since Build 2
 */
public class MoveMinionRemoved extends StopAction
{
	public MoveMinionRemoved(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		addStopTarget(StopTarget.RemoveMinion);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getStopPlayer();
		Area originArea = gameManager.getStopArea();
		if (player == null || originArea == null)
		{
			gameManager.getSystemDataInput().showError("No target to stop.");
		}
		
		try
		{
			List<Area> minionAreaList = new ArrayList<Area>();
			minionAreaList.addAll(gameManager.getBoard().getAreaList());
			minionAreaList.remove(originArea);
			
			Area destinationArea = player.getDataInput().ask("Please chose the area where you want to move your minion:", minionAreaList);
			ActionManager.removeMinion(gameManager, originArea, player);
			ActionManager.addMinion(gameManager, destinationArea, player);
		}
		catch(Exception ex)
		{
			player.getDataInput().showError(ex.getMessage());
		}
		return false;
	}
}
