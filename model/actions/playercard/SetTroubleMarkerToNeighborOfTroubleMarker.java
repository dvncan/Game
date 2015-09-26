package ankhmorpork.model.actions.playercard;

import java.util.HashSet;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Place one trouble marker in an area adjacent to one already containing a trouble marker.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SetTroubleMarkerToNeighborOfTroubleMarker extends PlayerCardAction
{
	//<PlayerCard id="126" color="brown" name="The mob" actions="TEXT;PERS;CARD" scroll="SetTroubleMarkerToNeightboorOfTroubleMarker"/>
	//optional="Place one trouble marker in an area adjacent to one already containing a trouble marker."

	/**
	 * Constructor: 
	 * Place one trouble marker in an area adjacent to one already containing a trouble marker.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public SetTroubleMarkerToNeighborOfTroubleMarker(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Place one trouble marker in an area adjacent to one already containing a trouble marker.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		HashSet<Area> areaSet = new HashSet<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasTrouble())
			{
				for(Area areaNeightboor : area.getNeighborOnly())
				{
					if(!areaNeightboor.hasTrouble() && !areaSet.contains(areaNeightboor))
					{
						areaSet.add(areaNeightboor);
					}
				}
			}
		}
		
		if (areaSet.isEmpty())
		{
			gameManager.getCurrentPlayer().getDataInput().showError("All or none of the areas have trouble.");
			return false;
		}
		
		Player player = gameManager.getCurrentPlayer();
		Area area = player.getDataInput().ask("Which area do you want to add a trouble marker?", Environment.sortAreaList(areaSet));
		area.setTroubleMarker(true);
		
		return true;
	}	
}
