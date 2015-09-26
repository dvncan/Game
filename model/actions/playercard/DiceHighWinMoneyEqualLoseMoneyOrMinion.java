package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Roll the die. On a roll of diceValueOrHigher or more you take $moneyAmountWin from the bank.
 * On a roll of 'diceValueEqual' you must pay $payAmount to the bank 
 * or remove one of your minions from the board. 
 * All other results have no effect.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class DiceHighWinMoneyEqualLoseMoneyOrMinion extends PlayerCardAction
{
	//<PlayerCard id="42" color="green" name="CMOT Dibbler" actions="TEXT;CARD" scroll="DiceHighWinMoneyEqualLoseMoneyOrMinion;7;4;1;2"/>
	//<PlayerCard id="56" color="green" name="Here'n'Now" actions="TEXT;CARD" scroll="DiceHighWinMoneyEqualLoseMoneyOrMinion;7;4;1;2"/>
	//optional="Roll the die. On a roll of '7' or more you take $4 from the bank. On a roll of '1' you must pay $2 to the bank or remove one of your minions from the board. All other results have no effect."

	private final int diceValueOrHigher;
	private final int moneyAmountWin;
	private final int diceValueEqual;
	private final int payAmount;

	/**
	 * Constructor: 
	 * Roll the die. On a roll of diceValueOrHigher or more you take $moneyAmountWin from the bank.
	 * On a roll of 'diceValueEqual' you must pay $payAmount to the bank 
	 * or remove one of your minions from the board. 
	 * All other results have no effect.
	 * @param args contain 1 argument, the amount of card to look at
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DiceHighWinMoneyEqualLoseMoneyOrMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 4;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.diceValueOrHigher = getArgument(args, 0);
		this.moneyAmountWin = getArgument(args, 1);
		this.diceValueEqual = getArgument(args, 2);
		this.payAmount = getArgument(args, 3);
	}

	/**
	 * Roll the die. On a roll of diceValueOrHigher or more you take $moneyAmountWin from the bank.
	 * On a roll of 'diceValueEqual' you must pay $payAmount to the bank 
	 * or remove one of your minions from the board. 
	 * All other results have no effect.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		boolean isSuccessful = true;
		int diceValue = gameManager.getBoard().rollDice();
		
		if(diceValue >= this.diceValueOrHigher)
		{
			isSuccessful = gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.moneyAmountWin, false);
		}
		else if (diceValue == this.diceValueEqual)
		{
			Player player = gameManager.getCurrentPlayer();
			boolean acceptToPay = false;
			if(player.getBankAccount().getMoneyAmount() >= this.payAmount)
			{
				String question = "Do you want to give $" + this.payAmount + "? If you refuse, you must discard a minion.";
				String trueOption = "I accept to pay " + this.payAmount + "$.";
				String falseOption = "I refuse to pay " + this.payAmount + "$.";
				acceptToPay = player.getDataInput().askTrueFalseQuestion(question, trueOption, falseOption);
			}

			if(acceptToPay)
			{
				player.getBankAccount().transfertAmountTo(gameManager.getBoard(), this.payAmount, true);
			}
			else
			{
				List<Area> areaMinionsList = new ArrayList<Area>();
				for(Area area : gameManager.getBoard().getAreaList())
				{
					if(area.hasMinion(player))
					{
						areaMinionsList.add(area);
					}
				}

				if(!areaMinionsList.isEmpty())
				{
					Area area = player.getDataInput().ask("Select an area to remove a minion.", areaMinionsList);
					boolean stop = ActionManager.stopAction(gameManager, player, area, new StopTarget[]{StopTarget.Text, StopTarget.RemoveMinion });
					if (!stop)
					{
						ActionManager.removeMinion(gameManager, area, player);
					}
				}
			}
		}
		return isSuccessful;
	}
}
