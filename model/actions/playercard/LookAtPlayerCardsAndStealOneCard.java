package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Look at X PlayerCards from one player and choose one to steal
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class LookAtPlayerCardsAndStealOneCard extends PlayerCardAction
{
	//<PlayerCard id="113" color="brown" name="Stanley" actions="TEXT;PERS" scroll="LookAtPlayerCardsAndStealOneCard;2"/>
	//optional="Select two cards randomly from one player and choose one to keep. Return the other card."

	private final int amountOfCardsToLookAt;
	
	/**
	 * Constructor: Look at X PlayerCards from one player and choose one to steal
	 * @param args contain 1 argument, the amount of card to look at
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public LookAtPlayerCardsAndStealOneCard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountOfCardsToLookAt = getArgument(args, 0);
	}
	
	/**
	 * Look at X player card from a player of your choice and steal one of them
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select a player to look at " + this.amountOfCardsToLookAt + " cards and steal one.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Cards, message);
		
		boolean stop = ActionManager.stopAction(gameManager, player, null, new StopTarget[]{StopTarget.Text });
		if (!stop)
		{
			List<Card> cardList = new ArrayList<Card>();
			for(int i = 0; i < this.amountOfCardsToLookAt && !player.getPlayerCardDeck().isEmpty(); i++)
			{
				int cardPosition = Environment.randInt(0, player.getPlayerCardDeck().size() - 1);
				cardList.add(player.getPlayerCardDeck().remove(cardPosition));
			}

			if (cardList.isEmpty())
			{
				gameManager.getCurrentPlayer().getDataInput().showError(player.getFullName() + " does not have card to steal.");
			}

			Card cardToSteal = gameManager.getCurrentPlayer().getDataInput().ask("Select a player card to steal.", cardList);
			if(cardList.remove(cardToSteal))
			{
				gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(cardToSteal);
			}

			for(Card card : cardList)
			{
				player.getPlayerCardDeck().addCard(card);
			}

			return true;
		}
		return false;
	}
}
