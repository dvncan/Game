package ankhmorpork.model.actions.personality;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.playercard.BorrowMoneyFromTheBank;
import java.util.List;

/**
 * Win if enough assets
 * @author Team 2
 * @since Build 2
 */
public class MuchAssets extends Action
{
	private final int amount;
	
	public MuchAssets(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player currentPlayer = gameManager.getCurrentPlayer();
		int assets = currentPlayer.getMoneyAmount();
		for(Card loanCard : currentPlayer.getInFrontOfHimDeck())
		{
			for(Action loanAction : loanCard.getActions(BorrowMoneyFromTheBank.class))
			{
				assets -= ((BorrowMoneyFromTheBank)loanAction).getMoneyToPayBack();
			}
		}
		
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if (!area.hasDemon() && area.hasBuilding(currentPlayer))
			{
				assets += area.getCost();
			}
		}
		
		boolean endgame = assets >= amount;
		if (endgame)
		{
			gameManager.endGame(GameManager.EndGameType.winAtBeginTurn);
		}
		return endgame;
	}
}
