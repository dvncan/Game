package ankhmorpork.model.actions.base;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.BasicAction;

import java.util.ArrayList;

/**
 * Place building
 * @author Team 2
 * @since Build 2
 */
public class PlacePlayerBuilding extends BasicAction
{	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			Player currentPlayer = gameManager.getCurrentPlayer();
			ArrayList<Area> areaToBuild = new ArrayList<Area>();
			for (Area area : gameManager.getBoard().getAreaList())
			{
				if (!area.hasTrouble() && !area.hasBuilding() && area.hasMinion(currentPlayer) && area.getCost() <= currentPlayer.getMoneyAmount())
				{
					areaToBuild.add(area);
				}
			}
			
			if (areaToBuild.isEmpty())
			{
				gameManager.getCurrentPlayer().getDataInput().showError("You do not meet the requirements to build in an area.");
				return false;
			}
			
			Area chosenArea = gameManager.getCurrentPlayer().getDataInput().ask("Please chose the area where you want to build:", areaToBuild);
			currentPlayer.getBankAccount().transfertAmountTo(gameManager.getBoard(), chosenArea.getCost(), true);
			return ActionManager.addBuilding(gameManager, chosenArea, currentPlayer);
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
		return "Place building";
	}
}
