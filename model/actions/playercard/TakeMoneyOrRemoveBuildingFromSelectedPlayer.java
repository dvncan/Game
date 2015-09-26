package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Select another player. If they do not give you $X then place this card in front of them.
 * This card now counts towards their hand size of five cards when they come to refill their hand.
 * They cannot get rid of this card.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class TakeMoneyOrRemoveBuildingFromSelectedPlayer extends PlayerCardAction
{
	//<PlayerCard id="52" color="green" name="The Fire Brigade" actions="TEXT;CARD" scroll="1;5;1"/>
	//optional="Choose a player. If he does not pay you $5 then you can remove one of his buildings from the board"
	
	private final int amount;
	
	/**
	 * Constructor:
	 * Choose a player. If he does not pay you $5 then you can remove one of his buildings from the board.
	 * @param args contain 1 argument, the amount of money to steal
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public TakeMoneyOrRemoveBuildingFromSelectedPlayer(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}

	/**
	 * Choose a player. If he does not pay you $5 then you can remove one of his buildings from the board.
	 * @param args contain 1 argument, the amount of money to steal
	 * @throws Exception if the argument doesn't match what expected 
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select another player. If he doesn't pay you $" + this.amount + " then you can remove one of his buildings from the board.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.MoneyAndBuilding, message);
		
		boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
		if (!stop)
		{
			boolean acceptToPay = false;
			if(player.getBankAccount().getMoneyAmount() > this.amount)
			{
				String question = "Do you want to give $" + this.amount + "? If you refuse, " + currentPlayer.getFullName() + " will remove one of your buildings from the board.";
				String trueOption = "I accept to pay " + this.amount + "$.";
				String falseOption = "I refuse to pay " + this.amount + "$.";
				acceptToPay = player.getDataInput().askTrueFalseQuestion(question, trueOption, falseOption);
			}

			if(acceptToPay)
			{
				player.getBankAccount().transfertAmountTo(currentPlayer, this.amount, true);
			}
			else
			{
				List<Area> areaWithBuildingList = new ArrayList<Area>();
				for(Area area : gameManager.getBoard().getAreaList())
				{
					if(area.hasBuilding(player))
					{
						areaWithBuildingList.add(area);
					}
				}

				if(areaWithBuildingList.isEmpty())
				{
					currentPlayer.getDataInput().printMessage("The player has no building to remove.");
				}
				else
				{
					Area area = currentPlayer.getDataInput().ask("Which building do you want to remove?", areaWithBuildingList);
					ActionManager.removeBuilding(gameManager, area, player);
				}
			}
			return true;
		}
		
		return false;
	}
}
