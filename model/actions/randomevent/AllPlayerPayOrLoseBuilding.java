package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Each player must pay or lose their building
 * @author Team 2
 * @since Build 2
 */
public class AllPlayerPayOrLoseBuilding extends RandomEventCardAction
{
	private final int cost;
	
	/**
	 * Each player must pay or lose their building
	 * @param args
	 * @throws Exception 
	 */
	public AllPlayerPayOrLoseBuilding(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.cost = getArgument(args, 0);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		try
		{
			for(Player player : ActionManager.getPlayerListInOrder(gameManager, true))
			{
				boolean hasBuilding = false;
				for(Area area : gameManager.getBoard().getAreaList())
				{
					if (area.hasBuilding(player))
					{
						hasBuilding = true;
						boolean loseBuilding = true;
						boolean stop = ActionManager.stopAction(gameManager, player, area, new StopTarget[]{StopTarget.RandomEventLandmark});
						if (!stop)
						{
							if (player.getMoneyAmount() >= cost)
							{
								String question = "Do you want to give $" + this.cost + " to keep your building on " + area.getName() + ".";
								String trueOption = "I accept to pay " + this.cost + "$ to keep my building.";
								String falseOption = "I refuse to pay " + this.cost + "$ and will lose my building.";
								boolean acceptToPay = player.getDataInput().askTrueFalseQuestion(question, trueOption, falseOption);
								if (acceptToPay)
								{
									player.getBankAccount().transfertAmountTo(gameManager.getBoard(), this.cost, true);
									loseBuilding = false;
								}
							}
							else
							{
								player.getDataInput().printMessage("You do not have enough money and will lose your building on " + area.getName() + ".");
							}
							
							if (loseBuilding)
							{
								ActionManager.removeBuilding(gameManager, area, player);
							}
						}
					}
				}
				if (!hasBuilding)
				{
					gameManager.getSystemDataInput().printMessage(player.getFullName() + " does not have buildings to pay for.");
				}
			}
			return true;
		}
		catch (Exception ex)
		{
			gameManager.getSystemDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
