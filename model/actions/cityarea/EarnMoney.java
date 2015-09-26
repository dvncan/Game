package ankhmorpork.model.actions.cityarea;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.CityCardAction;

import java.util.List;

/**
 * Earn money action
 * @author Team 2
 * @since Build 2
 */
public class EarnMoney extends CityCardAction
{
	//<CityAreaCard id="23" name="Dragon's Landing" area="3" cost="12" scroll="EarnMoney;2"/>
	//<CityAreaCard id="20" name="The Hippo" area="6" cost="12" scroll="EarnMoney;2"/>
	//<CityAreaCard id="29" name="Longwall" area="9" cost="12" scroll="EarnMoney;1"/>
	//<CityAreaCard id="27" name="Seven Sleepers" area="11" cost="18" scroll="EarnMoney;3"/>
	//<CityAreaCard id="26" name="Nap Hill" area="12" cost="12" scroll="EarnMoney;1"/>
	
	private final int amount;
	
	public EarnMoney(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 2;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
		this.areaNumber = getArgument(args, 1);
	}
	
	/**
	 * Execute the action
	 * @param gameManager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		return gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), this.amount, false);
	}
}
