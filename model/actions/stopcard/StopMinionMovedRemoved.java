package ankhmorpork.model.actions.stopcard;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.StopAction;

import java.util.List;

/**
 * Pick and discard player card
 * @author Team 2
 * @since Build 2
 */
public class StopMinionMovedRemoved extends StopAction
{
	public StopMinionMovedRemoved(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		addStopTarget(StopTarget.MoveMinion);
		addStopTarget(StopTarget.RemoveMinion);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		return true;
	}
}
