package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Exchange your hand with that of another player.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ExchangeHandWithOtherPlayer extends PlayerCardAction
{
	//<PlayerCard id="91" color="brown" name="The Chair of Indefinite Studies" actions="EVNT;TEXT;CARD" scroll="ExchangeHandWithOtherPlayer"/>
	//optional="Exchange your hand with that of another player."
	

	/**
	 * Constructor: 
	 * Exchange your hand with that of another player.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public ExchangeHandWithOtherPlayer(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Exchange your hand with that of another player.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = ActionManager.getPlayer(gameManager, gameManager.getCurrentPlayer(), ActionManager.TypePlayerDisplay.Cards, "Exchange hand with which player?");
		boolean stop = ActionManager.stopAction(gameManager, player, null, new StopTarget[]{StopTarget.Text });
		if (!stop)
		{
			List<Card> cardList = new ArrayList<Card>();
			cardList.addAll(gameManager.getCurrentPlayer().getPlayerCardDeck());
			gameManager.getCurrentPlayer().getPlayerCardDeck().clear();
			gameManager.getCurrentPlayer().getPlayerCardDeck().addAll(player.getPlayerCardDeck());
			player.getPlayerCardDeck().clear();
			player.getPlayerCardDeck().addAll(cardList);
			return true;
		}
		
		return false;
	}
}
