

package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Earn $X for each building on the board (any color).
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class EarnMoneyForEachBuildingOnBoard extends PlayerCardAction
{
	//<PlayerCard id="92" color="brown" name="Sir Charles Lavatory" actions="TEXT;BLDG" scroll="EarnMoneyForEachBuildingOnBoard;1"/>
	//optional="Earn $X for each building on the board (any color)."
	
	/**
	 * Constructor Earn $X for each building on the board (any color).
	 * @param args contain 0 argument
	 * @throws Exception if the argument doesn't match what expected
	 */
	private final int  moneyPerBuilding;
	public EarnMoneyForEachBuildingOnBoard(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.moneyPerBuilding = getArgument(args, 0);
	}
	
	/**
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int amountOfBuilding = 0;
		for (Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasBuilding())
			{
				amountOfBuilding++;
			}
		}
		gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.moneyPerBuilding * amountOfBuilding, false);
		return true;
	}
}
