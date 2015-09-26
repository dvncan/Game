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
 * Every other player, in player order, must remove one of their minions from the board.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class EveryOtherPlayerRemoveMinion extends PlayerCardAction
{
	//<PlayerCard id="98" color="brown" name="The Auditors" actions="TEXT" scroll="EveryOtherPlayerRemoveMinion"/>
	//optional="Every other player, in player order, must remove one of their minions from the board."
	
	/**
	 * Constructor: 
	 * Every other player, in player order, must remove one of their minions from the board.
	 * @param args contain ...
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public EveryOtherPlayerRemoveMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Every other player, in player order, must remove one of their minions from the board.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(Player player : ActionManager.getPlayerListInOrder(gameManager, false))
		{
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
				Area area = player.getDataInput().ask("Area from which you want to take a minion.", areaList);
				boolean stop = ActionManager.stopAction(gameManager, player, area, new StopTarget[]{StopTarget.Text, StopTarget.RemoveMinion });
				if (!stop)
				{
					ActionManager.removeMinion(gameManager, area, player);
				}
			}
			else
			{
				gameManager.getSystemDataInput().printMessage(player.getFullName() + " has no minions on board.");
			}
		}

		return true;
	}
}
