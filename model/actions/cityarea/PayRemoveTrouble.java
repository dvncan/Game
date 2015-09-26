package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.actions.base.GuardArea;

import java.util.List;

/**
 * Pay to remove trouble
 * @author Team 2
 * @since Build 2
 */
public class PayRemoveTrouble extends CityCardAction
{
	//<CityAreaCard id="28" name="Isle of Gods" area="10" cost="12" scroll="PayRemoveTrouble;2"/>
	
	private final int cost;
	
	public PayRemoveTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.cost = getArgument(args, 0);
		this.areaNumber = getArgument(args, 1);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			if (gameManager.getCurrentPlayer().getMoneyAmount() < this.cost)
			{
				gameManager.getCurrentPlayer().getDataInput().showError("You do not have enough money to remove trouble.");
				return false;
			}
			
			boolean success = new GuardArea().execute(gameManager);
			if (success)
			{
				gameManager.getCurrentPlayer().getBankAccount().transfertAmountTo(gameManager.getBoard(), cost, true);
			}
			return success;
		}
		catch (Exception ex)
		{
			gameManager.getCurrentPlayer().getDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
