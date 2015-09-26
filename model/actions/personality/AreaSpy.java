package ankhmorpork.model.actions.personality;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import java.util.List;

/**
 * Win if enough spies
 * @author Team 2
 * @since Build 2
 */
public class AreaSpy extends Action
{
	private final int amountForTwo;
	private final int amountForThree;
	private final int amountForFour;
	
	public AreaSpy(List<String> args) throws Exception
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
		int playerCount = gameManager.getPlayerList().size();
		int amount = playerCount == 2 ? amountForTwo : playerCount == 3 ? amountForThree : amountForFour;
		int spy = 0;
		
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if (!area.hasDemon() && area.hasMinion(gameManager.getCurrentPlayer()))
			{
				spy += 0;
			}
		}
		
		boolean endgame = spy >= amount;
		if (endgame)
		{
			gameManager.endGame(GameManager.EndGameType.winAtBeginTurn);
		}
		return endgame;
	}
}
