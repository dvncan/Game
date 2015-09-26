package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Count trouble and end game if necessary
 * @author Team 2
 * @since Build 2
 */
public class EndGameWithTrouble extends RandomEventCardAction
{
	private final int amount;
	
	/**
	 * Count trouble and end game if necessary
	 * @param args
	 * @throws Exception 
	 */
	public EndGameWithTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
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
			gameManager.endGame(GameManager.EndGameType.riotTroubleEvent);
		}
		return endgame;
	}
}
