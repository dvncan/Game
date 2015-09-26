package ankhmorpork.model.actions.playercard;

import java.util.List;
import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;


/**
 * Select a player which must give you X cards of their choices
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class SelectPlayerToGiveYouPlayerCards extends PlayerCardAction
{
	//<PlayerCard id="34" color="green" name="The beggars' Guild" actions="TEXT;PERS" scroll="SelectPlayerToGiveYouPlayerCards;2"/>
	//<PlayerCard id="72" color="green" name="Queen Molly" actions="PERS;TEXT" scroll="SelectPlayerToGiveYouPlayerCards;2"/>
	//optional="Select one player. They must give you X cards of their choice."
	
	private final int amountOfCardsToReceive;
	
	/**
	 * Constructor: Select a player which must give you X cards of their choices
	 * @param args contain 1 argument, the amount of card to look at
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public SelectPlayerToGiveYouPlayerCards(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountOfCardsToReceive = getArgument(args, 0);
	}
	
	/**
	 * Select a player which must give you X cards of their choices
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		String message = "Select a player to get " + this.amountOfCardsToReceive + " cards from.";
		Player currentPlayer = gameManager.getCurrentPlayer();
		Player player = ActionManager.getPlayer(gameManager, currentPlayer, ActionManager.TypePlayerDisplay.Cards, message);
		boolean stop = ActionManager.stopAction(gameManager, player, null, new StopTarget[]{ StopTarget.Text });
		if (!stop)
		{
			for(int i = 0; i < this.amountOfCardsToReceive && !player.getPlayerCardDeck().isEmpty(); i++)
			{
				Card card = player.getDataInput().ask("Select a player card to give to " + gameManager.getCurrentPlayer().getFullName() + ".", player.getPlayerCardDeck());
				if(player.getPlayerCardDeck().remove(card))
				{
					gameManager.getCurrentPlayer().getPlayerCardDeck().addCard(card);
				}
			}

			return true;
		}
		
		return false;
	}
}
