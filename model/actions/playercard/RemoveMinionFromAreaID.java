package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;

import java.util.HashSet;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Remove a minion from area areaId
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class RemoveMinionFromAreaID extends PlayerCardAction
{
	//<PlayerCard id="84" color="brown" name="The Dean" actions="EVNT;TEXT;CARD" scroll="31;2"/>
	//optional="Remove one minion from Unreal Estate."

	private final int areaId;

	/**
	 * Constructor: 
	 * Remove nbMinionToRemove minion from area areaId
	 * @param args contain 1 argument area of the Minion
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public RemoveMinionFromAreaID(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.areaId = getArgument(args, 0);
	}
	
	/**
	 * Remove a minion from area areaId
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Area area = gameManager.getBoard().getArea(this.areaId);
		HashSet<Player> setPlayer = new HashSet<Player>();
		for(Player p : gameManager.getPlayerList())
		{
			if (area.hasMinion(p))
			{
				setPlayer.add(p);
			}
		}
		
		if(!setPlayer.isEmpty())
		{
			Player playerMinion = gameManager.getCurrentPlayer().getDataInput().ask("Select a player to remove minion from " + area.getName() + ".", Environment.sortPlayerList(setPlayer));
			boolean stop = ActionManager.stopAction(gameManager, playerMinion, area, new StopTarget[]{ StopTarget.Text, StopTarget.RemoveMinion });
			if (!stop)
			{
				ActionManager.removeMinion(gameManager, area, playerMinion);
			}
		}
		return true;
	}
}
