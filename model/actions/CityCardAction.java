package ankhmorpork.model.actions;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;

/**
 * PlayerCardsAction
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public abstract class CityCardAction extends Action
{
	protected int areaNumber;
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "City Action " + getClassName();
	}
	
	/**
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed)
	{
		return true;
	}

	/**
	 * Inform the card if it can be executed
	 * @param gameManager to verify the logic
	 * @return if it can be executed
	 */
	public boolean canExecute(GameManager gameManager) 
	{
		return !gameManager.getBoard().getArea(areaNumber).hasDemon();
	}
}
