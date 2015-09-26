package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * TakeMoneyOrGiveUselessCard
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class TakeMoneyOrGiveUselessCard extends PlayerCardAction
{
	//<PlayerCard id="51" color="green" name="The Fools' Guild" actions="TEXT;PERS" scroll="36;5"/>
	//<PlayerCard id="75" color="green" name="Dr WhiteFace" actions="TEXT;PERS" scroll="36;5"/>
	//optional="Select another player. If they do not give you $5 then place this card in front of them. This card now counts towards their hand size of five cards when they come to refill their hand. They cannot get rid of this card."

	private final int amount;
	
	/**
	 * @param args contain 1 argument, the amount of money to take
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public TakeMoneyOrGiveUselessCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}

	/**
	 * Select another player. If they do not give you $X then place this card in front of them.
	 * This card now counts towards their hand size of five cards when they come to refill their hand.
	 * They cannot get rid of this card.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select another player. If they do not give you $" + this.amount + " then place this card in front of them. This card now counts towards their hand size of five cards when they come to refill their hand. They cannot get rid of this card.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.MoneyAndBuilding, message);
		
		boolean stop = ActionManager.stopAction(gameManager, player, null, new Action.StopTarget[]{ Action.StopTarget.Text });
		if (!stop)
		{
			boolean acceptToPay = false;
			if(player.getBankAccount().getMoneyAmount() > this.amount)
			{
				String question = "Do you want to give $" + this.amount + "? If you refuse, " + currentPlayer.getFullName() + " will place this card in front of you. This card now counts towards your hand size of five cards when you come to refill their hand. You cannot get rid of this card.";
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
				Card uselessCard = gameManager.getBoard().getLastPlayedCard();
				uselessCard.disableCard();
				player.getPlayerCardDeck().addCard(uselessCard);
			}

			return true;
		}
		
		return false;
	}
}
