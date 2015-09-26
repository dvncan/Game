package ankhmorpork.model.actions.playercard;

import ankhmorpork.manager.ActionManager;
import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;

/**
 * You can replace nbBuildingReplace player's building with one of your own.
 * Pay the cost of the building to the original owner.
 * It must be an area that hasTroubleMarker in it.
 * @see Board
 * @author Team 2
 * @since Build 2
 */
public class ReplaceEnemyBuildingWithYourOwn extends PlayerCardAction
{
	//<PlayerCard id="101" color="brown" name="Sybil Vimes" actions="GOLD;TEXT" money="3" scroll="ReplaceEnemyBuildingWithYourOwn;0"/>
	//<PlayerCard id="119" color="brown" name="Reacher Gift" actions="TEXT" scroll="ReplaceEnemyBuildingWithYourOwn;1"/>
	//optional="You can replace another player's building with one of your own. Pay the cost of the building to the original owner. It must be an area that doesn't have/has a trouble marker in it."
	
	private final boolean hasTroubleMarker;
	
	/**
	 * Constructor:
	 * You can replace nbBuildingReplace player's building with one of your own.
	 * Pay the cost of the building to the original owner.
	 * @param args 1 boolean argument hasTroubleMarker, true (1) if area should have a trouble marker in it, false (0) if it shoudln't. 
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public ReplaceEnemyBuildingWithYourOwn(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.hasTroubleMarker = getArgument(args, 0) != 0;
	}
	
	/**
	 * You can replace nbBuildingReplace player's building with one of your own.
	 * Pay the cost of the building to the original owner.
	 * It must be an area that hasTroubleMarker in it.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player player = gameManager.getCurrentPlayer();
		List<Area> areaListBuyBuilding = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasBuilding() 
					&& !area.hasBuilding(player) 
					&& area.getCost() <= player.getMoneyAmount() 
					&& area.hasTrouble() == this.hasTroubleMarker)
			{
				areaListBuyBuilding.add(area);
			}
		}
		if(!areaListBuyBuilding.isEmpty())
		{
			Area area = player.getDataInput().ask("Which area building do you want to buy?", areaListBuyBuilding);
			Player owner = (Player)area.getBuilding().getOwner();
			boolean stop = ActionManager.stopAction(gameManager, owner, area, new StopTarget[]{ StopTarget.Text });
			if (!stop)
			{
				ActionManager.removeBuilding(gameManager, area, null);
				ActionManager.addBuilding(gameManager, area, player);
				player.getBankAccount().transfertAmountTo(owner, area.getCost(), true);
			}
		}
		return true;
	}
}
