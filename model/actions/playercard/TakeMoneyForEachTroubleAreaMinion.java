package ankhmorpork.model.actions.playercard;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import java.util.ArrayList;


public class TakeMoneyForEachTroubleAreaMinion extends Action
{
	private final int amount;

	/**
	 * Chose a troubled area, get money for each minion on it
	 * @param args contain 1 argument, the amount of money to take
	 * @throws Exception if the argument doesn't match what expected 
	 */

	//<PlayerCard id="111" color="brown" name="Mr Slant" actions="TEXT;BLDG" scroll="2;1;2"/>
	//optional="Choose a player. If he does not pay you $2 then you can kill one of their characters from the board"
	public TakeMoneyForEachTroubleAreaMinion(List<String> args) throws Exception
	{
		NUMBER_OF_ARGUMENTS = 1;
		verifyArgumentCount(args, NUMBER_OF_ARGUMENTS);
		this.amount = getArgument(args, 0);
	}


	/**
	 * Select another player. If they do not give you $X then place this card in front of them.
	 * This card now counts towards their hand size of five cards when they come to refill their hand.
	 * They cannot get rid of this card.
	 * @param gameManager the controller to execute all the changes on
	 * @return true all the time as this operation can never fail
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player currentPlayer = gameManager.getCurrentPlayer();
		List<Area> areaList = new ArrayList<Area>();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			if(area.hasTrouble())
			{
				areaList.add(area);
			}
		}

		if (areaList.isEmpty())
		{
			currentPlayer.getDataInput().showError("No area has trouble.");
			return false;
		}
		
		Area area = currentPlayer.getDataInput().ask("For which area do you want to get " + amount + "$ per minion stationed?", areaList);
		gameManager.getBoard().getBankAccount().transfertAmountTo(currentPlayer, area.getNbCharacter() * amount, false);
		
		return true;
	}
}


