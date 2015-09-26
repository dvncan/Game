package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.Player;
import java.util.List;

/**
 * Stop random event that affect 
 * @author Team 2
 * @since Build 2
 */
public class StopRandomEventLandmarkAffected extends CityCardAction
{
	private final int cost;
	
	public StopRandomEventLandmarkAffected(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.cost = getArgument(args, 0);
		this.areaNumber = getArgument(args, 1);
		addStopTarget(StopTarget.RandomEventLandmark);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getCurrentPlayer();
		if (player.getMoneyAmount() >= cost)
		{
			player.getBankAccount().transfertAmountTo(gameManager.getBoard(), cost, true);
			return true;
		}
		return false;
	}

	/**
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	@Override
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed)
	{
		return false;
	}
}
