package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Move a minion belonging to another player from one area to an adjacent area.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class MoveEnemyMinionToAdjacentArea extends PlayerCardAction
{
	//<PlayerCard id="40" color="green" name="The Duckman" actions="TEXT" scroll="MoveEnemyMinionToAdjacentArea"/>
	//<PlayerCard id="50" color="green" name="Foul Ole Ron" actions="TEXT;CARD" scroll="MoveEnemyMinionToAdjacentArea"/>
	//<PlayerCard id="89" color="brown" name="Canting Crew" actions="TEXT;PERS" scroll="MoveEnemyMinionToAdjacentArea"/>
	//optional="Move a minion belonging to another player from one area to an adjacent area."
	

	/**
	 * Constructor: 
	 * Move a minion belonging to another player from one area to an adjacent area.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public MoveEnemyMinionToAdjacentArea(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Move a minion belonging to another player from one area to an adjacent area.
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
			if(area.hasMinion() && area.getMinionList(player).size() != area.getNbMinion())
			{
				areaList.add(area);
			}
		}

		if(!areaList.isEmpty())
		{
			Area areaOrigin = gameManager.getCurrentPlayer().getDataInput().ask("Area from which you want to move an enemy minion.", areaList);
			HashSet<Player> setPlayer = new HashSet<Player>();
			for(Player p : gameManager.getPlayerList())
			{
				if (p.getId() != player.getId() && areaOrigin.hasMinion(p))
				{
					setPlayer.add(p);
				}
			}
			
			Player playerMinion = gameManager.getCurrentPlayer().getDataInput().ask("Select the player whose minion you want to move from '" + areaOrigin.getName() + "' to an adjacent area.", Environment.sortPlayerList(setPlayer));
			boolean stop = ActionManager.stopAction(gameManager, player, areaOrigin, new StopTarget[]{ StopTarget.Text, StopTarget.MoveMinion });
			if (!stop)
			{
				Area areaDestionation = gameManager.getCurrentPlayer().getDataInput().ask("Area to which you want to move the minion.", areaOrigin.getNeighborOnly());
				ActionManager.removeMinion(gameManager, areaOrigin, playerMinion);
				ActionManager.addMinion(gameManager, areaDestionation, playerMinion);
			}
		}

		return true;
	}
}
