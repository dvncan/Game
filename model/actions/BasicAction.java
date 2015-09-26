package ankhmorpork.model.actions;

import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * PlayerCardsAction
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public abstract class BasicAction extends Action
{
	/**
	 * @return card is always optional except event card... for basic actions
	 */
	@Override
	public boolean isOptional()
	{
		return true;
	}
}
