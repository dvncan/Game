package ankhmorpork.model.actions.base;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.actions.BasicAction;

import java.util.ArrayList;

/**
 * Guard an area (removes trouble)
 * @author Team 2
 * @since Build 2
 */
public class GuardArea extends BasicAction
{
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			ArrayList<Area> areaToGuard = new ArrayList<Area>();
			for (Area area : gameManager.getBoard().getAreaList())
			{
				if (area.hasTrouble())
				{
					areaToGuard.add(area);
				}
			}
			
			if (areaToGuard.isEmpty())
			{
				gameManager.getCurrentPlayer().getDataInput().showError("There is no trouble in Ankh-Morpork.");
				return false;
			}
			
			Area chosenArea = gameManager.getCurrentPlayer().getDataInput().ask("Please chose the area where you want to remove trouble:", areaToGuard);
			chosenArea.setTroubleMarker(false);
			gameManager.getCurrentPlayer().getDataInput().printMessage("Trouble has been removed from " + chosenArea.toString() + ".");
			return true;
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
		return "Guard";
	}
}
