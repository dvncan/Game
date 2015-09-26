package ankhmorpork.model.actions.playercard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.util.Environment;

/**
 * Pay a player of your choice $X. Then choose a minion to assassinate.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class PayPlayerOfChoiceThenAssassinateMinion extends PlayerCardAction
{
	//<PlayerCard id="86" color="brown" name="Burleigh &amp; Stronginthearm" actions="TEXT;BLDG" scroll="PayPlayerOfChoiceThenAssassinateMinion;2"/>
	//optional="Pay a player of your choice $2. Then choose a minion to assassinate."
	
	private final int amount;

	/**
	 * Constructor: 
	 * Pay a player of your choice $X. Then choose a minion to assassinate.
	 * @param args amount to be paid
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public PayPlayerOfChoiceThenAssassinateMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}
	
	/**
	 * Pay a player of your choice $X. Then choose a minion to assassinate.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getCurrentPlayer();
		try
		{
			if(player.getMoneyAmount() >= this.amount)
			{
				Player selectPlayer = ActionManager.getPlayer(gameManager, player, ActionManager.TypePlayerDisplay.Normal, "Select a player to pay him "+this.amount+"$.");
				
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
					Area area = gameManager.getCurrentPlayer().getDataInput().ask("Select an area to assassinate a minion.", areaMinionsList);
					HashSet<Player> setPlayer = new HashSet<Player>();
					for(Player p : gameManager.getPlayerList())
					{
						if (area.hasMinion(p))
						{
							setPlayer.add(p);
						}
					}
					
					Player playerMinion = gameManager.getCurrentPlayer().getDataInput().ask("Select the player whose minion you want to assassinate in " + area.getName() + ".", Environment.sortPlayerList(setPlayer));
					boolean stop = ActionManager.stopAction(gameManager, playerMinion, area, new StopTarget[]{ StopTarget.Text });
					if (!stop)
					{
						player.getBankAccount().transfertAmountTo(selectPlayer, this.amount, true);
						stop = ActionManager.stopAction(gameManager, playerMinion, area, new StopTarget[]{ StopTarget.RemoveMinion });
						if (!stop)
						{
							ActionManager.removeMinion(gameManager, area, playerMinion);
						}
					}
				}
			}
			return true;
		}
		catch (Exception ex)
		{
			player.getDataInput().showError(ex.getMessage());
			return false;
		}
	}
}
