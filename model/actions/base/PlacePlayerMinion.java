package ankhmorpork.model.actions.base;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.BasicAction;

import java.util.ArrayList;

/**
 * Place minion
 * @author Team 2
 * @since Build 2
 */
public class PlacePlayerMinion extends BasicAction
{
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			Player currentPlayer = gameManager.getCurrentPlayer();
			ArrayList<Area> areaToSendMinion = new ArrayList<Area>();
			for (Area area : gameManager.getBoard().getAreaList())
			{
				if (area.hasMinion(currentPlayer) || area.hasNeighborMinion(currentPlayer))
				{
					areaToSendMinion.add(area);
				}
			}
			
			if (areaToSendMinion.isEmpty())
			{
				gameManager.getCurrentPlayer().getDataInput().showError("You do not meet the requirements to send a minion.");
				return false;
			}
			
			Area chosenArea = gameManager.getCurrentPlayer().getDataInput().ask("Please chose the area where you want to send a minion:", areaToSendMinion);
			return ActionManager.addMinion(gameManager, chosenArea, currentPlayer);
		}
		catch (Exception ex)
		{
			gameManager.getCurrentPlayer().getDataInput().showError(ex.getMessage());
			return false;
		}
	}
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Place minion";
	}
}
