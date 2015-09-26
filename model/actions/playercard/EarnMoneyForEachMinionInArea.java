package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Board;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Earn $X for each minion in a specific area.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class EarnMoneyForEachMinionInArea extends PlayerCardAction
{
	//<PlayerCard id="39" color="green" name="The Dysk" actions="BLDG;TEXT" scroll="EarnMoneyForEachMinionInArea;1;10"/>
	//<PlayerCard id="61" color="green" name="The Opera House" actions="BLDG;TEXT" scroll="EarnMoneyForEachMinionInArea;1;10"/>
	//optional="Earn $X for each minion in a specific area."
	
	private final int areaID;
	private final int profitPerMinion;

	/**
	 * Constructor Earn $X for each minion in a specific area.
	 * @param args contain 2 argument, the amount of money obtained per minion and area ID
	 * @throws Exception if the argument doesn't match what expected
	 */
	public EarnMoneyForEachMinionInArea(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.profitPerMinion = getArgument(args, 0);
		this.areaID = getArgument(args, 1);
	}
	
	/**
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int nbMinions = gameManager.getBoard().getArea(areaID).getNbCharacter();
		gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), profitPerMinion*nbMinions, false);
		
		return true;
	}
}
