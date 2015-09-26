package ankhmorpork.model.actions;

import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * PlayerCardsAction
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public abstract class StopAction extends Action
{
	/**
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	@Override
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed)
	{
		return false;
	}
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Stop " + getClassName();
	}
}
