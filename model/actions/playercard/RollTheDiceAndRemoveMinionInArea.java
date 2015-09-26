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
 * Roll the die twice and remove one minion of your choice
 * from those areas, even if there is no trouble there.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class RollTheDiceAndRemoveMinionInArea extends PlayerCardAction
{
	//<PlayerCard id="90" color="brown" name="Carcer" actions="TEXT;CARD" scroll="RollTheDiceAndRemoveMinionInArea;2"/>
	//optional="Roll the die twice and remove one minion of your choice from those areas, even if there is no trouble there."
	
	private final int minionToRemove;

	/**
	 * Constructor: 
	 * Roll the die twice and remove one minion of your choice
	 * from those areas, even if there is no trouble there.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public RollTheDiceAndRemoveMinionInArea(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.minionToRemove = getArgument(args, 0);
	}
	
	/**
	 * Roll the die twice and remove one minion of your choice
	 * from those areas, even if there is no trouble there.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(int i = 0; i < minionToRemove; i++)
		{
			int diceValue = gameManager.getBoard().rollDice();
			Area area = gameManager.getBoard().getArea(diceValue);
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
		}
		
		return true;
	}
}
