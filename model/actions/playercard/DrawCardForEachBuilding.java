package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.PlayerCardAction;


/**
 * Draw one card for each building you have on the board.
 * @see Board
 * @author Team 2
 * @since Build 2
 */


public class DrawCardForEachBuilding extends PlayerCardAction
{
	//<PlayerCard id="131" color="brown" name="Gargoyles" actions="TEXT;BLDG" scroll="DrawCardForEachBuilding"/>
	//optional=Draw one card for each building you have on the board.

	
	/**
	 * Constructor: Draw one card for each building you have on the board.
	 * @param args none
	 * @throws Exception if the argument doesn't match what expected 
	 */
	public DrawCardForEachBuilding(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 0;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
	}
	
	/**
	 * Draw one card for each building you have on the board.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		int amountOfBuildings = 0;
		Player player = gameManager.getCurrentPlayer();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasBuilding(player))
			{
				amountOfBuildings++;
			}
		}
			
		for(int i=0; i< amountOfBuildings; i++)
		{
			player.getPlayerCardDeck().addCard(gameManager.getNextPlayerCard());
		}
		
		return true;
	}
}
