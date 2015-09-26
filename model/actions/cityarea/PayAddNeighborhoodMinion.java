package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;

import java.util.List;

/**
 * Pay to add minion in area or neighbors
 * @author Team 2
 * @since Build 2
 */
public class PayAddNeighborhoodMinion extends CityCardAction
{
	//<CityAreaCard id="25" name="Dolly Sisters" area="1" cost="6" scroll="PayAddNeighborhoodMinion;3"/>
	
	private final int cost;

	public PayAddNeighborhoodMinion(List<String> args) throws Exception
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
			Player currentPlayer = gameManager.getCurrentPlayer();
			if (currentPlayer.getMoneyAmount() < this.cost)
			{
				currentPlayer.getDataInput().showError("You do not have enough money to add a minion.");
				return false;
			}
			
			Area chosenArea = ActionManager.getPlayerChosenNumberedNeighborhood(gameManager, areaNumber, currentPlayer, "Please chose the area where you want to add a minion:");
			boolean success = ActionManager.addMinion(gameManager, chosenArea, currentPlayer);
			if (success)
			{
				currentPlayer.getBankAccount().transfertAmountTo(gameManager.getBoard(), cost, true);
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
