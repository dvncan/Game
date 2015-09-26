package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place a trouble marker in an area of your choice.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SetTroubleMarkerAnywhere extends PlayerCardAction
{
	//<PlayerCard id="121" color="brown" name="Doctor Hix" actions="EVNT;TEXT;CARD" scroll="SetTroubleMarkerAnywhere"/>
	//optional="Place a trouble marker in an area of your choice."

	/**
	 * Constructor: 
	 * Place a trouble marker in an area of your choice.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public SetTroubleMarkerAnywhere(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Place a trouble marker in an area of your choice.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		List<Area> areaList = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(!area.hasTrouble())
			{
				areaList.add(area);
			}
		}

		if (areaList.isEmpty())
		{
			gameManager.getCurrentPlayer().getDataInput().showError("All areas have trouble.");
			return false;
		}

		Area area = gameManager.getCurrentPlayer().getDataInput().ask("Which area do you want to add a trouble marker to?", areaList);
		area.setTroubleMarker(true);

		return true;
	}
}
