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
 * Move one of your minions from one area to any other area on the board.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class MoveOwnMinionFromOneAreaToAnother extends PlayerCardAction
{
	//<PlayerCard id="93" color="brown" name="Dorfl" actions="TEXT;CARD" scroll="MoveOwnMinionFromOneAreaToAnother"/>
	//<PlayerCard id="96" color="brown" name="Adora Belle Dearheart" actions="TEXT;PERS;BLDG" scroll="MoveOwnMinionFromOneAreaToAnother"/>
	//optional="Move one of your minions from one area to any other area on the board."
	

	/**
	 * Constructor: 
	 * Move one of your minions from one area to any other area on the board.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public MoveOwnMinionFromOneAreaToAnother(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Move one of your minions from one area to any other area on the board.
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
			if(area.hasMinion(player))
			{
				areaList.add(area);
			}
		}

		if(!areaList.isEmpty())
		{
			Area areaOrigin = gameManager.getCurrentPlayer().getDataInput().ask("Area from which you want to move your minion to any other area.", areaList);
			areaList.clear();
			areaList.addAll(gameManager.getBoard().getAreaList());
			areaList.remove(areaOrigin);
			Area areaDestionation = gameManager.getCurrentPlayer().getDataInput().ask("Area to which you want to move a minion.", areaList);
			ActionManager.removeMinion(gameManager, areaOrigin, player);
			ActionManager.addMinion(gameManager, areaDestionation, player);
		}

		return true;
	}
}
