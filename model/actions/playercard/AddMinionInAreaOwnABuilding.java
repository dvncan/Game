package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place one minion in an area that you have a building in.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class AddMinionInAreaOwnABuilding extends PlayerCardAction
{
	//<PlayerCard id="106" color="brown" name="Willikins" actions="TEXT" scroll="25"/>
	//optional="Place one minion in an area that you have a building in."

	/**
	 * Constructor: 
	 * Place one minion in an area that you have a building in.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public AddMinionInAreaOwnABuilding(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Place one minion in an area that you have a building in.
	 * @param gameManager the controller to execute all the changes on
	 * @return true if the minion was added
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		boolean isSuccessful;
		
		List<Area> areaList = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasBuilding(gameManager.getCurrentPlayer()))
			{
				areaList.add(area);
			}
		}
		
		if(!areaList.isEmpty())
		{
			Area area = gameManager.getCurrentPlayer().getDataInput().ask("Select an area to add a minion.", areaList);
			isSuccessful = ActionManager.addMinion(gameManager, area, gameManager.getCurrentPlayer());
		}
		else
		{
			isSuccessful = false;
		}

		return isSuccessful;
	}
}
