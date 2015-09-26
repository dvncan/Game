package ankhmorpork.model.actions;

import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * PlayerCardsAction
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public abstract class RandomEventCardAction extends Action
{
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Random Event Action " + getClassName();
	}
}
