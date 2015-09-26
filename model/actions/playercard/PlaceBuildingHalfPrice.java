package ankhmorpork.model.actions.playercard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.model.actions.base.PlacePlayerBuilding;

/**
 * Place a building for half price
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PlaceBuildingHalfPrice extends PlayerCardAction
{
	//<PlayerCard id="104" color="brown" name="Wee Mad Arthur" actions="TEXT" scroll="PlaceBuildingHalfPrice"/>
	//optional="You may build a building for half price."
	
	/**
	 * Constructor: Place a building for half price
	 * @param args contain 0 argument
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public PlaceBuildingHalfPrice(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}

	/**
	 * Reduce all AreaCard cost to half price
	 * Execute base action PlaceBuilding
	 * Put back the cost of each area to their original price
	 * @param gameManager the controller to execute all the changes on
	 * @return if the operation was successful
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		boolean isSuccessful;
		Map<Integer,Integer> oldPrices = new HashMap<Integer,Integer>();
		
		// Reduce all price of half
		for(Area area : gameManager.getBoard().getAreaList())
		{
			oldPrices.put(area.getNumber(), area.getCost());
			area.setCost(area.getCost() / 2);
		}
		
		//Execute action of PlaceBuilding
		try 
		{
			isSuccessful = (new PlacePlayerBuilding()).execute(gameManager);
		} 
		catch (Exception ex) 
		{
			gameManager.getSystemDataInput().showError(ex.getMessage());
			isSuccessful = false;
		}

		// Put back all prices to original values
		for(Area area : gameManager.getBoard().getAreaList())
		{
			area.setCost(oldPrices.get(area.getNumber()));
		}
		
		return isSuccessful;
	}
}
