package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;

import java.util.ArrayList;
import java.util.List;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.model.landmark.Demon;
import ankhmorpork.model.landmark.Troll;

/**
 * Exchange the positions of any two minions on the board.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ExchangePositionOfMinionOnBoard extends PlayerCardAction
{
	//<PlayerCard id="87" color="brown" name="The Bursar" actions="EVNT;TEXT;CARD" scroll="ExchangePositionOfMinionOnBoard"/>
	//optional="Exchange the positions of any two minions on the board."
	

	/**
	 * Constructor: 
	 * Exchange the positions of any two minions on the board.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public ExchangePositionOfMinionOnBoard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Exchange the positions of any two minions on the board.
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
			if(area.getNbCharacter() > 0)
			{
				areaList.add(area);
			}
		}

		if(areaList.size() > 1)
		{
			Area areaOrigin = gameManager.getCurrentPlayer().getDataInput().ask("Area from which you want to exchange a minion.", areaList);
			
			List<Object> listPlayersDemonTroll = new ArrayList<Object>();
			for(Player p : gameManager.getPlayerList())
			{
				if (areaOrigin.hasMinion(p))
				{
					listPlayersDemonTroll.add(p);
				}
			}
			if (areaOrigin.hasDemon())
			{
				listPlayersDemonTroll.add(areaOrigin.getDemonList().get(0));
			}
			if (areaOrigin.hasTroll())
			{
				listPlayersDemonTroll.add(areaOrigin.getTrollList().get(0));
			}
			Object choiceSource = player.getDataInput().ask("Select the owner of the minion to exchange from " + areaOrigin.getName() + ".", listPlayersDemonTroll);

			areaList.remove(areaOrigin);
			Area areaDestionation = gameManager.getCurrentPlayer().getDataInput().ask("Area to which you want to exchange the minion.", areaList);
			
			listPlayersDemonTroll.clear();
			for(Player p : gameManager.getPlayerList())
			{
				if (areaOrigin.hasMinion(p))
				{
					listPlayersDemonTroll.add(p);
				}
			}
			if (areaOrigin.hasDemon())
			{
				listPlayersDemonTroll.add(areaOrigin.getDemonList().get(0));
			}
			if (areaOrigin.hasTroll())
			{
				listPlayersDemonTroll.add(areaOrigin.getTrollList().get(0));
			}
			
			Object choiceDestination = player.getDataInput().ask("Select a player to exchange minion from " + areaDestionation.getName() + ".", listPlayersDemonTroll);

			if(choiceSource instanceof Troll)
			{
				ActionManager.removeTroll(gameManager, areaOrigin);
				ActionManager.addTroll(gameManager, areaDestionation);
			}
			else if(choiceSource instanceof Demon)
			{
				ActionManager.removeDemon(gameManager, areaOrigin);
				ActionManager.addDemon(gameManager, areaDestionation);
			}
			else
			{
				Player playerOrigin = (Player)choiceSource;
				boolean stopOrigin = ActionManager.stopAction(gameManager, playerOrigin, areaOrigin, new StopTarget[]{StopTarget.Text, StopTarget.MoveMinion});
				if (!stopOrigin)
				{
					ActionManager.removeMinion(gameManager, areaOrigin, playerOrigin);
					ActionManager.addMinion(gameManager, areaDestionation, playerOrigin);
				}
			}
			

			if(choiceDestination instanceof Troll)
			{
				ActionManager.removeTroll(gameManager, areaDestionation);
				ActionManager.addTroll(gameManager, areaOrigin);
			}
			else if(choiceDestination instanceof Demon)
			{
				ActionManager.removeDemon(gameManager, areaDestionation);
				ActionManager.addDemon(gameManager, areaOrigin);
			}
			else
			{
				Player playerDestination = (Player)choiceDestination;
				boolean stopDestination = ActionManager.stopAction(gameManager, playerDestination, areaDestionation, new StopTarget[]{StopTarget.Text, StopTarget.MoveMinion});
				if (!stopDestination)
				{
					ActionManager.removeMinion(gameManager, areaDestionation, playerDestination);
					ActionManager.addMinion(gameManager, areaOrigin, playerDestination);
				}
			}
		}
		else
		{
			 gameManager.getCurrentPlayer().getDataInput().showError("Not enough minions on different areas to exchange.");
			 return false;
		}

		return true;
	}
}
