package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * Pay $X to a player of your choice. Then move one of your minions to any area you wish.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PayPlayerOfChoiceThenAddMinion extends PlayerCardAction
{
	//<PlayerCard id="122" color="brown" name="Hobson's Livery Stable" actions="TEXT;BLDG" scroll="20;2"/>
	//optional="Pay $2 to a player of your choice. Then move one of your minions to any area you wish."
	
	private final int amount;

	/**
	 * Constructor: 
	 * Pay $X to a player of your choice. Then move one of your minions to any area you wish.
	 * @param args Amount to be paid
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public PayPlayerOfChoiceThenAddMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
	/**
	 * Pay $X to a player of your choice. Then move one of your minions to any area you wish.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getCurrentPlayer();
		if(player.getMoneyAmount() >= this.amount)
		{
			Player selectPlayer = ActionManager.getPlayer(gameManager, player, ActionManager.TypePlayerDisplay.Normal, "Select a player to pay him "+this.amount+"$.");
			player.getBankAccount().transfertAmountTo(selectPlayer, this.amount, true);
			
			List<Area> areaMinionsList = new ArrayList<Area>();
			for(Area area : gameManager.getBoard().getAreaList())
			{
				if(area.hasMinion())
				{
					areaMinionsList.add(area);
				}
			}
			
			if(!areaMinionsList.isEmpty())
			{
				Area area = gameManager.getCurrentPlayer().getDataInput().ask("Select an area to add-move a minion.", areaMinionsList);
				ActionManager.addMinion(gameManager, area, gameManager.getCurrentPlayer());
			}
		}
		return true;
	}
}
