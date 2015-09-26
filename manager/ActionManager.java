package ankhmorpork.manager;

import ankhmorpork.datainput.AbstractDataInput;
import ankhmorpork.model.Action;
import ankhmorpork.model.Action.StopTarget;
import ankhmorpork.model.Area;
import ankhmorpork.model.Card;
import ankhmorpork.model.ObjectDecorator;
import ankhmorpork.model.Player;
import ankhmorpork.model.landmark.Building;
import ankhmorpork.model.landmark.Minion;
import ankhmorpork.util.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * Action helper class
 * @author Team 2
 * @since Build 2
 */
public class ActionManager
{
	public enum TypePlayerDisplay
	{
		Normal,
		Cards,
		Money,
		MoneyAndBuilding
	}
	
	/**
	 * Get a random area
	 * @param gameManager to get access to every object on the board
	 * @return a random area
	 * @throws Exception if there no area to return at the dice position
	 */
	public static Area getRandomArea(GameManager gameManager) throws Exception
	{
		int randomNumber = gameManager.getBoard().rollDice();
		Area randomArea = gameManager.getBoard().getArea(randomNumber);
		
		if (randomArea == null)
		{
			throw new Exception("Invalid random area");
		}
		
		return randomArea;
	}
	
	/**
	 * Get an area of given number
	 * @param gameManager to get access to every object on the board
	 * @param number pointing to an area
	 * @return an area
	 * @throws Exception  if there no area at that position
	 */
	public static Area getNumberedArea(GameManager gameManager, int number) throws Exception
	{
		Area numberedArea = gameManager.getBoard().getArea(number);
		if (numberedArea == null)
		{
			throw new Exception("Invalid area number " + number);
		}
		
		return numberedArea;
	}
	
	/**
	 * Get an area of given number and its neighbors
	 * @param gameManager to get access to every object on the board
	 * @param number
	 * @param player
	 * @param question
	 * @return
	 * @throws Exception 
	 */
	public static Area getPlayerChosenNumberedNeighborhood(GameManager gameManager, int number, Player player, String question) throws Exception
	{
		return player.getDataInput().ask(question, getNumberedArea(gameManager, number).getAreaAndNeighborhood());
	}
		
	/**
	 * Get character type
	 * @param gameManager to get access to every object on the board
	 * @param player
	 * @return
	 * @throws Exception 
	 */
	public static String getPlayerChosenCharacterType(GameManager gameManager, Player player, String question) throws Exception
	{
		ArrayList<String> characterTypes = new ArrayList<String>();
		characterTypes.add(ankhmorpork.model.landmark.Character.CharacterType.MINION);
		characterTypes.add(ankhmorpork.model.landmark.Character.CharacterType.TROLL);
		characterTypes.add(ankhmorpork.model.landmark.Character.CharacterType.DEMON);
		
		String chosenType = player.getDataInput().ask(question, characterTypes);
		if (chosenType == null)
		{
			throw new Exception("Invalid chosen character type");
		}
		
		return chosenType;
	}
	
	/**
	 * Get the list of player starting with the current player if included, or next player if not
	 * @param gameManager game manager
	 * @param includeCurrent true if current player is included in list
	 * @return list of player in order
	 */
	public static List<Player> getPlayerListInOrder(GameManager gameManager, boolean includeCurrent)
	{
		ArrayList<Player> playerList = new ArrayList<Player>();
		int startingIndex = gameManager.getPlayerList().indexOf(gameManager.getCurrentPlayer());
		int playerTotal = gameManager.getPlayerList().size();
		int playerWanted = playerTotal;
		
		if (!includeCurrent)
		{
			playerWanted -= 1;
			startingIndex += 1;
		}
		
		for (int i = 0; i < playerWanted; i++)
		{
			playerList.add(gameManager.getPlayerList().get((startingIndex + i) % playerTotal));
		}
		
		return playerList;
	}
	
	/**
	 * Get an enemy player
	 * @param gameManager to search within the player list
	 * @param eTypeDisplay the method to display players
	 * @param message to display
	 * @return the selected enemy player
	 */
	public static Player getPlayer(GameManager gameManager, Player currentPlayer, TypePlayerDisplay eTypeDisplay, String message)
	{
		List<ObjectDecorator<Player>> playerList = new ArrayList<ObjectDecorator<Player>>();
		for(Player player : gameManager.getPlayerList())
		{
			if(player.getId() != currentPlayer.getId())
			{
				String playerInformation;
				switch(eTypeDisplay)
				{
					case Cards:
						playerInformation = player.getPlayerCardInformation();
						break;
					case Money:
						playerInformation = player.getMoneyInformation();
						break;
					case MoneyAndBuilding:
						playerInformation = player.getMoneyAndBuildingInformation(gameManager.getNbBuildingsPerPlayer());
						break;
					case Normal:
					default:
						playerInformation = player.getFullName();
						break;
				}
				playerList.add(new ObjectDecorator<Player>(player, playerInformation));
			}
		}
		return gameManager.getCurrentPlayer().getDataInput().ask(message, playerList).getObject();
	}
	
	/**
	 * Ask if a player wants to stop an action and if so executes it
	 * @param gameManager game manager
	 * @param targetPlayer player who needs to stop the action
	 * @param targetArea area on which the action is stopped
	 * @param targetActions the type of action to stop
	 * @return stop action has been executed true or false
	 */
	public static boolean stopAction(GameManager gameManager, Player targetPlayer, Area targetArea, StopTarget[] targetActions)
	{
		if (targetPlayer == null)
		{
			return false;
		}
		
		List<Object> stopCards = new ArrayList<Object>();
		for (Card card :targetPlayer.getStopCards(targetActions))
		{
			if(card.canExecute(gameManager))
			{
				stopCards.add(card);
			}
		}
		
		if (!stopCards.isEmpty())
		{
			stopCards.add(new ObjectDecorator<Boolean>(false, Definition.DO_NOT_USE_STOP));
			Object choice = targetPlayer.getDataInput().ask("Would you like to use a stop card?", stopCards);
			if (choice instanceof Card)
			{
				Card chosenStop = (Card)choice;
				if (chosenStop.isEnabled())
				{
					boolean success = true;
					gameManager.setStopPlayer(targetPlayer);
					gameManager.setStopArea(targetArea);
					
					for(Action action : chosenStop.getActions())
					{
						success &= action.execute(gameManager);
					}

					if (chosenStop.getType() != Card.CardType.CityAreaCard)
					{
						targetPlayer.getPlayerCardDeck().remove(chosenStop);
						gameManager.getBoard().discardCard(chosenStop);
					}
					else
					{
						chosenStop.setActiveState(false);
					}
					return success;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Add building
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @param player
	 * @return 
	 */
	public static boolean addBuilding(GameManager gameManager, Area area, Player player)
	{
		try
		{
			boolean success = true;

			if (area.hasBuilding())
			{
				player.getDataInput().showError("There is already a building in " + area.getName() + ".");
				return false;
			}

			if (player.getBuildingCount() == 0)
			{
				ArrayList<Area> buildingAreaList = new ArrayList<Area>();
				for(Area boardArea : gameManager.getBoard().getAreaList())
				{
					if (boardArea.hasBuilding(player) && boardArea != area)
					{
						buildingAreaList.add(boardArea);
					}
				}
				
				Area buildingArea = player.getDataInput().ask("All your buildings are placed. Please chose the area of the building you want to move:", buildingAreaList);
				success = removeBuilding(gameManager, buildingArea, player);
			}

			if (success)
			{
				area.setBuilding(player.removeBuilding());
				player.gainCityAreaCard(area);
				gameManager.getSystemDataInput().printMessage("A " + player.getColor().getName() + " building has been built in " + area.getName() + ".");
			}

			return success;
		}
		catch(Exception ex)
		{
			player.getDataInput().showError(ex.getMessage());
			return false;
		}
	}
	
	/**
	 * Add minion
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @param player
	 * @return 
	 */
	public static boolean addMinion(GameManager gameManager, Area area, Player player)
	{
		try
		{
			boolean success = true;
			
			if (player.getMinionCount() == 0)
			{
				ArrayList<Area> minionAreaList = new ArrayList<Area>();
				for(Area boardArea : gameManager.getBoard().getAreaList())
				{
					if (boardArea.hasMinion(player) && boardArea != area)
					{
						minionAreaList.add(boardArea);
					}
				}
				
				Area minionArea = player.getDataInput().ask("All your minions are placed. Please chose the area of the minion you want to move:", minionAreaList);
				success = removeMinion(gameManager, minionArea, player);
			}
			
			if (success)
			{
				area.addMinion(player.removeMinion());
				gameManager.getSystemDataInput().printMessage("A " + player.getColor().getName() + " minion has been sent to " + area.getName() + ".");
			}
			return success;
		}
		catch(Exception ex)
		{
			player.getDataInput().showError(ex.getMessage());
			return false;
		}
	}
	
	/**
	 * Add troll
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @return 
	 */
	public static boolean addTroll(GameManager gameManager, Area area)
	{
		if (gameManager.getBoard().getTrollList().isEmpty())
		{
			gameManager.getSystemDataInput().showError("There is no available troll to be send to " + area.getName() + ".");
			return false;
		}
		
		area.addTroll(gameManager.getBoard().removeTroll());
		gameManager.getSystemDataInput().printMessage("A troll has been sent to " + area.getName() + ".");
		return true;
	}
	
	/**
	 * Add demon
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @return 
	 */
	public static boolean addDemon(GameManager gameManager, Area area)
	{
		if (gameManager.getBoard().getDemonList().isEmpty())
		{
			gameManager.getSystemDataInput().showError("There is no available demon to be sent to " + area.getName() + ".");
			return false;
		}
		
		area.addDemon(gameManager.getBoard().removeDemon());
		gameManager.getSystemDataInput().printMessage("A demon has been sent to " + area.getName() + ".");
		return true;
	}
	
	/**
	 * Remove building
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @param player
	 * @return 
	 */
	public static boolean removeBuilding(GameManager gameManager, Area area, Player player)
	{
		//int areaMinionNb = player == null ? area.getNbMinion() : area.getMinionList(player).size();
		AbstractDataInput dataInput = player == null ? gameManager.getSystemDataInput() : player.getDataInput();
		
		if (!area.hasBuilding())
		{
			dataInput.showError("There is no building in " + area.getName() + " to be removed.");
			return false;
		}
		
		if (player != null && !area.hasBuilding(player))
		{
			dataInput.showError("There is no " + player.getColor().getName() + " building in " + area.getName() + " to be removed.");
			return false;
		}
		
		Building buildingToRemove = area.removeBuilding();
		Player buildingOwner = (Player)buildingToRemove.getOwner();
		
		buildingOwner.addBuilding(buildingToRemove);
		buildingOwner.loseCityAreaCard(area);
		
		gameManager.getSystemDataInput().printMessage("A "+ buildingToRemove.getColor().getName() + " building has been removed from " + area.getName() + ".");
		return true;
	}
	
	/**
	 * Remove minion
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @param player
	 * @return 
	 */
	public static boolean removeMinion(GameManager gameManager, Area area, Player player)
	{
		int areaMinionNb = player == null ? area.getNbMinion() : area.getMinionList(player).size();
		AbstractDataInput dataInput = player == null ? gameManager.getSystemDataInput() : player.getDataInput();

		if (areaMinionNb == 0)
		{
			dataInput.showError("There is no minion in " + area.getName() + " to be removed.");
			return false;
		}

		Minion minionToRemove;
		if (player == null)
		{
			minionToRemove = area.removeMinion();
		}
		else
		{
			minionToRemove = area.removeMinion(player);
		}

		Player owner = (Player)minionToRemove.getOwner();
		owner.addMinion(minionToRemove);
		gameManager.getSystemDataInput().printMessage("A " + owner.getColor().getName() + " minion has been removed from " + area.getName() + ".");
		return true;
	}
	
	/**
	 * Remove troll
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @return 
	 */
	public static boolean removeTroll(GameManager gameManager, Area area)
	{
		if (area.getNbTroll() == 0)
		{
			gameManager.getSystemDataInput().showError("There is no troll in " + area.getName() + " to be removed.");
			return false;
		}

		gameManager.getBoard().addTroll(area.removeTroll());
		gameManager.getSystemDataInput().printMessage("A troll has been removed from " + area.getName() + ".");
		return true;
	}
	
	/**
	 * Remove demon
	 * @param gameManager to get access to every object on the board
	 * @param area
	 * @return 
	 */
	public static boolean removeDemon(GameManager gameManager, Area area)
	{
		if (!area.hasDemon())
		{
			gameManager.getSystemDataInput().showError("There is no demon in " + area.getName() + " to be removed.");
			return false;
		}

		gameManager.getBoard().addDemon(area.removeDemon());
		gameManager.getSystemDataInput().printMessage("A demon has been removed from " + area.getName() + ".");
		return true;
	}
}
