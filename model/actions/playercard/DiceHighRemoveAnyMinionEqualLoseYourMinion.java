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
 * Roll the die. On a roll of diceValueOrHigher or more you remove 
 * minionToRemove minion of your choice from an area containing a trouble marker.
 * On a roll of 'diceValueEqual' you must remove one of your minions from the 
 * board. All other results have no effect.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DiceHighRemoveAnyMinionEqualLoseYourMinion extends PlayerCardAction
{
	//<PlayerCard id="130" color="brown" name="Errol" actions="TEXT;CARD" scroll="DiceHighRemoveAnyMinionEqualLoseYourMinion;7;1"/>
	//optional="Roll the die. On a roll of '7' or more you remove a minion of your choice from an area containing a trouble marker. On a roll of '1' you must remove one of your own minions."
	
	private final int diceValueOrHigher;
	private final int diceValueEqual;

	/**
	 * Constructor: 
	 * Roll the die. On a roll of diceValueOrHigher or more you remove 
	 * minionToRemove minion of your choice from an area containing a trouble marker.
	 * On a roll of 'diceValueEqual' you must remove discardMinionAmount
	 * of your minions from the board. All other results have no effect.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DiceHighRemoveAnyMinionEqualLoseYourMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.diceValueOrHigher = getArgument(args, 0);
		this.diceValueEqual = getArgument(args, 1);
	}

	/**
	 * Roll the die. On a roll of diceValueOrHigher or more you remove 
	 * minionToRemove minion of your choice from an area containing a trouble marker.
	 * On a roll of 'diceValueEqual' you must remove discardMinionAmount
	 * of your minions from the board. All other results have no effect.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int diceValue = gameManager.getBoard().rollDice();
		Player player = gameManager.getCurrentPlayer();
		
		gameManager.getSystemDataInput().printMessage(player.getFullName() + " Rolled a " + diceValue);
		
		if(diceValue >= this.diceValueOrHigher)
		{
			List<Area> areaMinionsList = new ArrayList<Area>();
			for(Area area : gameManager.getBoard().getAreaList())
			{
				if(area.hasMinionExceptPlayer(player) && area.hasTrouble())
				{
					areaMinionsList.add(area);
				}
			}

			if(!areaMinionsList.isEmpty())
			{
				Area area = player.getDataInput().ask("Select an area to remove a minion.", areaMinionsList);
				HashSet<Player> setPlayer = new HashSet<Player>();
				for(Player p : gameManager.getPlayerList())
				{
					if (p != player && area.hasMinion(p))
					{
						setPlayer.add(p);
					}
				}
				
				Player playerMinion = player.getDataInput().ask("Select a player whose minion to remove from " + area.getName() + ".", Environment.sortPlayerList(setPlayer));
				boolean stop = ActionManager.stopAction(gameManager, playerMinion, area, new StopTarget[]{StopTarget.Text, StopTarget.RemoveMinion });
				if (!stop)
				{
					ActionManager.removeMinion(gameManager, area, playerMinion);
				}
			}
		}
		else if (diceValue == this.diceValueEqual)
		{
			List<Area> areaMinionsList = new ArrayList<Area>();
			for(Area area : gameManager.getBoard().getAreaList())
			{
				if(area.hasMinion(player))
				{
					areaMinionsList.add(area);
				}
			}

			if(!areaMinionsList.isEmpty())
			{
				Area area = player.getDataInput().ask("Select an area to remove a minion.", areaMinionsList);
				boolean stop = ActionManager.stopAction(gameManager, player, area, new StopTarget[]{StopTarget.Text, StopTarget.RemoveMinion });
				if (!stop)
				{
					ActionManager.removeMinion(gameManager, area, player);
				}
			}
		}
		else
		{
			gameManager.getSystemDataInput().printMessage("Nothing happen.");
		}

		return true;
	}
}