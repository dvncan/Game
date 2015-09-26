package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place a minion in any area and do not place a trouble marker.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PlaceMinionInAreaWithoutTroubleMarker extends PlayerCardAction
{
	//<PlayerCard id="95" color="brown" name="Deep dwarves" actions="TEXT;CARD" scroll="PlaceMinionInAreaWithoutTroubleMarker"/>
	//<PlayerCard id="110" color="brown" name="Mr Shine" actions="TEXT" scroll="PlaceMinionInAreaWithoutTroubleMarker"/>
	//optional="Place a minion in any area and do not place a trouble marker."

	/**
	 * Constructor: 
	 * Place a minion in any area and do not place a trouble marker.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public PlaceMinionInAreaWithoutTroubleMarker(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Place a minion in any area and do not place a trouble marker.
	 * @param gameManager the controller to execute all the changes on
	 * @return true if it succeed
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Area area = gameManager.getCurrentPlayer().getDataInput().ask("Area in which you want to add a minion.", gameManager.getBoard().getAreaList());
		boolean hasTrouble = area.hasTrouble();
		boolean success = ActionManager.addMinion(gameManager, area, gameManager.getCurrentPlayer());
		area.setTroubleMarker(hasTrouble);
		return success;
	}
}
