package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place up to nbMaxMinionToPlace minions in or adjacent to areaId Area.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PlaceMinionInAreaOrNeightboor extends PlayerCardAction
{
	//<PlayerCard id="107" color="brown" name="Archchancellor Ridcully" actions="EVNT;TEXT" scroll="26;2;2"/>
	//<PlayerCard id="109" color="brown" name="The Senior Wrangler" actions="EVNT;TEXT;CARD" scroll="26;1;2"/>
	//optional="Place one or two minions in or adjacent to Unreal Estate."

	private final int nbMaxMinionToPlace;
	private final int areaId;

	/**
	 * Constructor: 
	 * Place up to nbMaxMinionToPlace minions in or adjacent to areaId Area.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public PlaceMinionInAreaOrNeightboor(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.nbMaxMinionToPlace = getArgument(args, 0);
		this.areaId = getArgument(args, 1);
	}
	
	/**
	 * Place up to nbMaxMinionToPlace minions in or adjacent to areaId Area.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int placedMinionCount = 0;
		do
		{
			List<Area> areaList = new ArrayList<Area>();
			areaList.add(gameManager.getBoard().getArea(this.areaId));
			areaList.addAll(gameManager.getBoard().getArea(this.areaId).getAreaAndNeighborhood());
			
			
			Area area = gameManager.getCurrentPlayer().getDataInput().ask("Select an area to add a minion.", areaList);
			ActionManager.addMinion(gameManager, area, gameManager.getCurrentPlayer());

			placedMinionCount++;
		} while(placedMinionCount < this.nbMaxMinionToPlace 
				&& gameManager.getCurrentPlayer().getDataInput().askTrueFalseQuestion("Do you want to place more minion?", "Yes", "No"));
		
		return true;
	}
}
