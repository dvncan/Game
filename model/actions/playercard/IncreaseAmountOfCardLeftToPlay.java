package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.model.actions.base.PlayAnotherCard;

/**
 * Play any nbCardToPlay other cards from your hand.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class IncreaseAmountOfCardLeftToPlay extends PlayerCardAction
{
	//<PlayerCard id="41" color="green" name="Drumknott" actions="TEXT" scroll="30;2"/>
	//<PlayerCard id="117" color="brown" name="Plonder Stibbons" actions="EVNT;TEXT" scroll="30;2"/>
	//optional="Play any two other cards from your hand."

	private final int nbCardToPlay;
	
	/**
	 * Constructor: 
	 * Play any nbCardToPlay other cards from your hand.
	 * @param args contain 4 arguments (Dice value, minion to remove, dice value, minion to lose)
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public IncreaseAmountOfCardLeftToPlay(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.nbCardToPlay = getArgument(args, 0);
	}
	
	/**
	 * Play any nbCardToPlay other cards from your hand.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		for(int i = 0; i < this.nbCardToPlay; i++)
		{
			(new PlayAnotherCard()).execute(gameManager);
		}
		
		return true;
	}
}
