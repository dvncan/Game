package ankhmorpork.model.actions;

import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * PlayerCardsAction
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public abstract class PlayerCardAction extends Action
{
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Scroll " + getClassName();
	}

	/**
	 * @return card must be played
	 */
	@Override
	public boolean isOptional()
	{
		return true;
	}
}
