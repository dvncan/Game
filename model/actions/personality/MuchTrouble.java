package ankhmorpork.model.actions.personality;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import java.util.List;

/**
 * Win if enough trouble
 * @author Team 2
 * @since Build 2
 */
public class MuchTrouble extends Action
{
	private final int amount;
	
	public MuchTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int troubleCount = 0;
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if (area.hasTrouble())
			{
				troubleCount += 1;
			}
		}
		
		boolean endgame = troubleCount >= amount;
		if (endgame)
		{
			gameManager.endGame(GameManager.EndGameType.winAtBeginTurn);
		}
		return endgame;
	}
}
