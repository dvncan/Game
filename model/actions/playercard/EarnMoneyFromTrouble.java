package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Earn $X for each trouble marker on the board.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class EarnMoneyFromTrouble extends PlayerCardAction
{
	//<PlayerCard id="68" color="green" name="Sacharissa Cripslock" actions="TEXT;PERS" scroll="EarnMoneyFromTrouble;1"/>
	//<PlayerCard id="81" color="brown" name="Otto Chriek" actions="TEXT;BLDG" scroll="EarnMoneyFromTrouble;1"/>
	//<PlayerCard id="105" color="brown" name="William de Worde" actions="PERS;TEXT" scroll="EarnMoneyFromTrouble;1"/>
	//optional="Earn $1 for each trouble marker on the board."
	
	private final int amount;

	/**
	 * Constructor: 
	 * Earn $X for each trouble marker on the board.
	 * @param args contain ...
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public EarnMoneyFromTrouble(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
	/**
	 * Earn $X for each trouble marker on the board.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int troubleCount = 0;
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasTrouble())
			{
				troubleCount++;
			}
		}
		gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), troubleCount * amount, false);
		return true;
	}
}
