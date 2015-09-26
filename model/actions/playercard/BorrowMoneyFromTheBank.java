package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Place this card in front of you and take a loan of $moneyToReceive
 * from the bank. At the end of the game you must pay
 back $moneyToPayBack or lose pointsToLose points.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class BorrowMoneyFromTheBank extends PlayerCardAction
{
	//<PlayerCard id="33" color="green" name="Mr Bent" actions="TEXT;CARD" scroll="29;10;12;15"/>
	//<PlayerCard id="35" color="green" name="The Bank of Ankh-Morpork" actions="TEXT;CARD" scroll="29;10;12;15"/>
	//optional="Place this card in front of you and take a loan of $10 from the bank. At the end of the game you must pay back $12 or lose 15 points."

	private final int moneyToReceive;
	private final int moneyToPayBack;
	private final int pointsToLose;

	/**
	 * Constructor: 
	 * Place this card in front of you and take a loan of $moneyToReceive
	 * from the bank. At the end of the game you must pay
 back $moneyToPayBack or lose pointsToLose points.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public BorrowMoneyFromTheBank(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 3;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.moneyToReceive = getArgument(args, 0);
		this.moneyToPayBack = getArgument(args, 1);
		this.pointsToLose = getArgument(args, 2);
	}
	
	/**
	 * Place this card in front of you and take a loan of $moneyToReceive
	 * from the bank. At the end of the game you must pay
 	 * back $moneyToPayBack or lose pointsToLose points.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.moneyToReceive, false);
		Card currentCard = gameManager.getBoard().getLastPlayedCard();
		gameManager.getCurrentPlayer().getInFrontOfHimDeck().addCard(currentCard);
		return true;
	}

	/**
	 * @return the moneyToPayBack
	 */
	public int getMoneyToPayBack() 
	{
		return moneyToPayBack;
	}

	/**
	 * @return the getPointsToLose
	 */
	public int getPointsToLose() 
	{
		return pointsToLose;
	}
}
