package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.Area;
import java.util.ArrayList;

import java.util.List;

/**
 * Add trouble in neighborhood
 * @author Team 2
 * @since Build 2
 */
public class AddNeighborhoodTrouble extends CityCardAction
{
	//<CityAreaCard id="31" name="The Shades" area="7" cost="6" scroll="AddNeighborhoodTrouble"/>
	
	/**
	 * Add trouble in neighborhood
	 * @param args
	 * @throws Exception 
	 */
	public AddNeighborhoodTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.areaNumber = getArgument(args, 0);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			ArrayList<Area> areaToTrouble = new ArrayList<Area>();
			for (Area area : gameManager.getBoard().getArea(this.areaNumber).getAreaAndNeighborhood())
			{
				if (!area.hasTrouble())
				{
					areaToTrouble.add(area);
				}
			}
			
			if (areaToTrouble.isEmpty())
			{
				gameManager.getCurrentPlayer().getDataInput().showError("All areas have trouble already.");
				return false;
			}
			
			Area chosenArea = gameManager.getCurrentPlayer().getDataInput().ask("Please chose the area where you want to add trouble:", areaToTrouble);
			chosenArea.setTroubleMarker(true);
			gameManager.getCurrentPlayer().getDataInput().printMessage("Trouble has been added to " + areaToTrouble.toString() + ".");
			return true;
		}
		catch (Exception ex)
		{
			gameManager.getCurrentPlayer().getDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
