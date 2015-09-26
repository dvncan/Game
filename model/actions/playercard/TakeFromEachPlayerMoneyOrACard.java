package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Each player must give you $X or one of their cards.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class TakeFromEachPlayerMoneyOrACard extends PlayerCardAction
{
	//<PlayerCard id="36" color="green" name="The Ankh Morpork Sunshine Dragon Sanctuary" actions="TEXT;CARD" scroll="TakeFromEachPlayerMoneyOrACard;1"/>
	//optional="Each player must give you $1 or one of their cards."
	
	private final int cost;
	
	/**
	 * Constructor: Each player must give you $X or one of their cards.
	 * @param args contain 1 argument, the cost to pay for not losing a card
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public TakeFromEachPlayerMoneyOrACard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.cost = getArgument(args, 0);
	}
	
	/**
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(Player player : ActionManager.getPlayerListInOrder(gameManager, false))
		{
			boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
			if (!stop)
			{
				int rndCardIndex = Environment.randInt(0, player.getPlayerCardDeck().size() - 1);

				if (player.getMoneyAmount() >= cost && !player.getPlayerCardDeck().isEmpty())
				{
					String question = "Do you want to give $" + this.cost + " or a card ?";
					String trueOption = "I accept to pay " + this.cost + "$ to avoid giving a card.";
					String falseOption = "I refuse to pay " + this.cost + "$ and will give a card.";

					boolean acceptToPay = player.getDataInput().askTrueFalseQuestion(question, trueOption, falseOption);
					if (acceptToPay)
					{
						player.getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.cost, true);
					}
					else
					{
						gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(player.getPlayerCardDeck().remove(rndCardIndex));
					}
				}
				else if (player.getMoneyAmount() < this.cost && !player.getPlayerCardDeck().isEmpty())
				{
					player.getDataInput().printMessage("You do not have enough money and will lose a card");
					gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(player.getPlayerCardDeck().remove(rndCardIndex));
				}
				else if (player.getMoneyAmount() >= this.cost && player.getPlayerCardDeck().isEmpty())
				{
					player.getDataInput().printMessage("You do not have cards and will lose a $"+ this.cost);
					player.getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.cost, true);
				}
				else
				{
					player.getDataInput().printMessage("You do not have enough money and you have no cards, no action will be taken");
				}
			}
		}
		return true;
	}
}
