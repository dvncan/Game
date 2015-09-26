package ankhmorpork.model.actions.personality;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import java.util.List;

/**
 * Win if control enough areas
 * @author Team 2
 * @since Build 2
 */
public class AreaControl extends Action
{
	private final int amountForTwo;
	private final int amountForThree;
	private final int amountForFour;
	
	public AreaControl(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 3;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountForTwo = getArgument(args, 0);
		this.amountForThree = getArgument(args, 1);
		this.amountForFour = getArgument(args, 2);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		//boolean win = false;
		int playerCount = gameManager.getPlayerList().size();
		int amount = playerCount == 2 ? amountForTwo : playerCount == 3 ? amountForThree : amountForFour;
		int control = 0;
		
		for(Area area : gameManager.getBoard().getAreaList())
		{
			int maxOccupied = 0;
			Player controller = null;
			if (!area.hasDemon())
			{
				maxOccupied = area.getNbTroll();
				for (Player player : gameManager.getPlayerList())
				{
					int occupied = area.hasBuilding(player) ? 1 : 0;
					occupied += area.getMinionList(player).size();
					if (occupied > maxOccupied)
					{
						controller = player;
						maxOccupied = occupied;
					}
					else if (occupied == maxOccupied)
					{
						controller = null;
					}
				}
				if (controller == gameManager.getCurrentPlayer())
				{
					control += 1;
				}
			}
		}
		
		boolean endgame = control >= amount;
		if (endgame)
		{
			gameManager.endGame(GameManager.EndGameType.winAtBeginTurn);
		}
		return endgame;
	}
}

