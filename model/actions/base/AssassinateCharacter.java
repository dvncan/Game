package ankhmorpork.model.actions.base;

import ankhmorpork.manager.ActionManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.model.actions.BasicAction;

import java.util.ArrayList;

/**
 * Assassinate a character
 * @author Team 2
 * @since Build 2
 */
public class AssassinateCharacter extends BasicAction
{

	@Override
	public boolean execute(GameManager gameManager) 
	{
		Player currentPlayer = gameManager.getCurrentPlayer();
		ArrayList<Area> areaToKill = new ArrayList<Area>();
		for (Area area : gameManager.getBoard().getAreaList())
		{
			if (area.hasTrouble() && area.getNbCharacter() > 0 && area.getNbCharacter() != area.getMinionList(currentPlayer).size())
			{
				areaToKill.add(area);
			}
		}

		if (areaToKill.isEmpty())
		{
			gameManager.getCurrentPlayer().getDataInput().showError("There is no character you may assassinate in the game.");
			return false;
		}

		Area chosenArea = currentPlayer.getDataInput().ask("Please chose the area where you want to send an assassin:", areaToKill);

		ArrayList<String> characterType = new ArrayList<String>();
		if (chosenArea.hasMinion())
		{
			characterType.add(ankhmorpork.model.landmark.Character.CharacterType.MINION);
		}
		if (chosenArea.hasTroll())
		{
			characterType.add(ankhmorpork.model.landmark.Character.CharacterType.TROLL);
		}
		if (chosenArea.hasDemon())
		{
			characterType.add(ankhmorpork.model.landmark.Character.CharacterType.DEMON);
		}

		String type = currentPlayer.getDataInput().ask("Please chose the type of character you want to assassinate:", characterType);

		boolean success = false;
		switch(type)
		{
			case ankhmorpork.model.landmark.Character.CharacterType.MINION:
				ArrayList<Player> playerWithAreaMinion = new ArrayList<Player>();
				for (Player player : gameManager.getPlayerList())
				{
					if (player != currentPlayer && chosenArea.hasMinion(player))
					{
						playerWithAreaMinion.add(player);
					}
				}
				Player chosenPlayer = currentPlayer.getDataInput().ask("Please chose the player whose minion you want to assassinate:", playerWithAreaMinion);
				boolean stop = ActionManager.stopAction(gameManager, chosenPlayer, chosenArea, new StopTarget[]{ StopTarget.RemoveMinion });
				if (!stop)
				{
					success = ActionManager.removeMinion(gameManager, chosenArea, chosenPlayer);
				}
				break;

			case ankhmorpork.model.landmark.Character.CharacterType.TROLL:
				success = ActionManager.removeTroll(gameManager, chosenArea);
				break;

			case ankhmorpork.model.landmark.Character.CharacterType.DEMON:
				success = ActionManager.removeDemon(gameManager, chosenArea);
				break;
		}

		return success;
	}
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Assassinate";
	}
}
