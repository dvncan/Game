package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place one minion in an area containing a trouble marker.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class AddMinionAreaWithTrouble extends PlayerCardAction
{
	//<PlayerCard id="112" color="brown" name="The Smoking Gnu" actions="TEXT;CARD" scroll="24"/>
	//optional="Place one minion in an area containing a trouble marker."
	
	/**
	 * Constructor: 
	 * Place one minion in an area containing a trouble marker.
	 * @param args contain the amount of minion to place in an area containing a trouble marker
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public AddMinionAreaWithTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Place one minion in an area containing a trouble marker.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		List<Area> areaList = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasTrouble())
			{
				areaList.add(area);
			}
		}
		
		if (areaList.isEmpty())
		{
			gameManager.getCurrentPlayer().getDataInput().showError("No area has trouble.");
			return false;
		}

		Area area = gameManager.getCurrentPlayer().getDataInput().ask("Area in which you want to add a minion.", areaList);
		ActionManager.addMinion(gameManager, area, gameManager.getCurrentPlayer());
		return true;
	}
}
