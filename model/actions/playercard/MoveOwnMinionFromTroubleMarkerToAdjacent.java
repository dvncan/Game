package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Move one of your minions from an area containing a trouble marker to an adjacent area.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class MoveOwnMinionFromTroubleMarkerToAdjacent extends PlayerCardAction
{
	//<PlayerCard id="70" color="green" name="Rincewind" actions="EVNT;TEXT;CARD" scroll="MoveOwnMinionFromTroubleMarkerToAdjacent"/>
	//optional="Move one of your minions from an area containing a trouble marker to an adjacent area."
	

	/**
	 * Constructor: 
	 * Move one of your minions from an area containing a trouble marker to an adjacent area.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public MoveOwnMinionFromTroubleMarkerToAdjacent(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Move one of your minions from an area containing a trouble marker to an adjacent area.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getCurrentPlayer();
		List<Area> areaList = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasMinion(player) && area.hasTrouble())
			{
				areaList.add(area);
			}
		}

		if(!areaList.isEmpty())
		{
			Area areaOrigin = gameManager.getCurrentPlayer().getDataInput().ask("Area from which you want to move your minion to an adjacent area.", areaList);
			areaList.clear();
			Area areaDestionation = gameManager.getCurrentPlayer().getDataInput().ask("Area to which you want to move your minion.", areaOrigin.getNeighborOnly());
			ActionManager.removeMinion(gameManager, areaOrigin, player);
			ActionManager.addMinion(gameManager, areaDestionation, player);
		}

		return true;
	}
}
