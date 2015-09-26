package ankhmorpork.model.actions.randomevent;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Card;
import ankhmorpork.model.actions.RandomEventCardAction;

import java.util.List;

/**
 * Discard top board player card
 * @author Team 2
 * @since Build 2
 */
public class DiscardBoardPlayerCard extends RandomEventCardAction
{
	private final int repeat;
	
	/**
	 * Discard top board player card
	 * @param args
	 * @throws Exception 
	 */
	public DiscardBoardPlayerCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.repeat = getArgument(args, 0);
	}
	
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for (int i = 0; i < repeat; i ++)
		{
			if (!gameManager.getBoard().getPlayerCardDeck().isEmpty())
			{
				Card discarded = gameManager.getNextPlayerCard();
				gameManager.getBoard().discardCard(discarded);
				gameManager.getSystemDataInput().printMessage(discarded.getName() + " has been discarded.");
			}
		}
		return true;
	}
}