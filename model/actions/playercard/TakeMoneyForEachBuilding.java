package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Take X$ gold for each building on the board
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class TakeMoneyForEachBuilding extends PlayerCardAction
{
	//<PlayerCard id="118" color="brown" name="The Post Office" actions="TEXT;PERS" scroll="TakeMoneyForEachBuilding;1"/>
	//optional="Take $1 for each building on the board."
	
	private final int amountPerBuilding;
	
	/**
	 * Constructor: Take X$ gold for each building on the board
	 * @param args contain 1 argument, the amount of money to take per building
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public TakeMoneyForEachBuilding(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amountPerBuilding = getArgument(args, 0);
	}

	/**
	 * Take X$ gold for each building on the board
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int buildingCount = 0;
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasBuilding())
			{
				buildingCount++;
			}
		}
		int amountToTransfer = this.amountPerBuilding * buildingCount;
		gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), amountToTransfer, false);
		
		return true;
	}
}
