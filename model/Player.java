package ankhmorpork.model;

import ankhmorpork.collection.Deck;
import ankhmorpork.datainput.AbstractDataInput;
import ankhmorpork.datainput.ConsoleDataInput;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.model.Card;
import ankhmorpork.model.landmark.Building;
import ankhmorpork.model.landmark.Minion;

/**
 * Player who participates in the game
 * @see Actor
 * @author Team 2
 * @since Build 1
 */
public class Player extends Actor
{
	private final int id;
	private final String name;
	private final Color color;
	private List<Minion> minionList = new ArrayList<Minion>();
	private List<Building> buildingList = new ArrayList<Building>();
	private Card personalityCard = null;
	private Deck<Card> cityAreaCardDeck = new Deck<Card>();
	private Deck<Card> inFrontOfPlayerDeck = new Deck<Card>();
	private boolean isComputerAI = false;
	private AbstractDataInput dataInput;
	private int points = 0;

	/**
	 * Creates a player with an ID, name and color
	 * @param id ID of the player
	 * @param name name of the player
	 * @param color color of the player
	 * @throws Exception 
	 */
	public Player(int id, String name, Color color, boolean isComputerAI) throws Exception
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.isComputerAI = isComputerAI;
		
		this.dataInput = new ConsoleDataInput(this, isComputerAI);
	}
	
	/**
	 * Creates a player with an ID and color
	 * @param id ID of the player
	 * @param color color of the player
	 */
	public Player(int id, Color color)
	{
		this.id = id;
		this.name = "";
		this.color = color;
	}
	
	/**
	 * Get the deck of city area cards
	 * @return the deck of city area cards
	 */
	public Deck<Card> getCityAreaCardDeck()
	{
		return cityAreaCardDeck;
	}

	/**
	 * Get the deck of card in front of the player for end game consideration
	 * @return the deck of card in front of the player
	 */
	public Deck<Card> getInFrontOfHimDeck() 
	{
		return inFrontOfPlayerDeck;
	}
	
	/**
	 * Gain a new city area card
	 * @param area city area
	 */
	public void gainCityAreaCard(Area area)
	{
		Card areaCard = area.getAreaCard();
		if (areaCard != null)
		{
			cityAreaCardDeck.addCard(areaCard);
			areaCard.setActiveState(false);
		}
	}
	
	/**
	 * Lose a city area card
	 * @param area city area
	 * @return the lost area card
	 */
	public Card loseCityAreaCard(Area area)
	{
		Card areaCard = area.getAreaCard();
		if (areaCard != null)
		{
			cityAreaCardDeck.pickCardByID(areaCard.getId());
		}
		return areaCard;
	}
	
	/**
	 * Get the ID of the player
	 * @return the ID of the player
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Get the name of the player
	 * @return the name of the player
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the color assigned to the player
	 * @return the color assigned to the player
	 */
	public Color getColor()
	{
		return this.color;
	}
	
	/**
	 * Get the points of the player
	 * @return the points of the player
	 */
	public int getPoints()
	{
		return this.points;
	}
	
	/**
	 * @param points to add
	 */
	public void addPoints(int points) 
	{
		this.points += points;
	}
	
	/**
	 * Add a minion to the player pieces
	 * @param minion the minion to add
	 */
	public void addMinion(Minion minion)
	{
		if (minion != null)
		{
			minion.setOwner(this);
			this.minionList.add(minion);
		}
	}
	
	/**
	 * Remove a minion from the player pieces
	 * @return the minion removed
	 */
	public Minion removeMinion()
	{
		if (minionList != null && !minionList.isEmpty())
		{
			return minionList.remove(0);
		}
		
		return null;
	}
	
	/**
	 * Find the number of minions the player currently holds (not placed on board)
	 * @return the number of minions the player currently holds
	 */
	public int getMinionCount()
	{
		if (this.minionList != null)
		{
			return this.minionList.size();
		}
		
		return 0;
	}
	
	/**
	 * Add a building to the player pieces
	 * @param building the building to add
	 */
	public void addBuilding(Building building)
	{
		if (building != null)
		{
			building.setOwner(this);
			this.buildingList.add(building);
		}
	}
	
	/**
	 * Remove a building from the player pieces
	 * @return the building removed
	 */
	public Building removeBuilding()
	{
		if (!buildingList.isEmpty())
		{
			Building building = buildingList.get(0);
			buildingList.remove(building);
			return building;
		}
		
		return null;
	}

	/**
	 * Find the number of buildings the player currently holds (not placed on board)
	 * @return the number of buildings the player currently holds
	 */
	public int getBuildingCount()
	{
		return this.buildingList.size();
	}

	/**
	 * Find the personality card the player currently holds
	 * @return the personality card the player currently holds
	 */
	public Card getPersonalityCard()
	{
		return this.personalityCard;
	}
	
	/**
	 * Find the name of the personality card the player currently holds
	 * @return the name of the personality card the player currently holds
	 */
	public String getCardName()
	{
		if(this.personalityCard == null)
		{
			return "none";
		}
		return this.personalityCard.getName();
	}

	/**
	 * Assign a personality card to the player
	 * @param personalityCard personality card assigned to the player
	 */
	public void setPersonalityCard(Card personalityCard)
	{
		this.personalityCard = personalityCard;
	}
	
	/**
	 * Return detailed information about the player including
	 * The amount of minions, buildings, gold, city area card and player cards
	 * @return a string containing the detailed information of a player
	 */
	public String getDetailedInfo()
	{
		String status = ("Player "+ getId() +"'s current inventory:\n\n");

		String minion = getMinionCount() + ((getMinionCount()>1) ? " minions" : " minion");
		String building = getBuildingCount() + ((getBuildingCount()>1) ? " buildings" : " building");
		String dollar = getMoneyAmount() + ((getMoneyAmount()>1) ? " Ankh-Morpork dollars" : " Ankh-Morpork dollar");
		status += ("   - " + minion + ", " + building + ", " + dollar + "\n");
		status += ("   - City Area cards:\n");
		if(cityAreaCardDeck.isEmpty())
		{
			status += ("        none\n");
		}
		else
		{
			for(Card card : cityAreaCardDeck)
			{
				status += ("        " + card.getName() + "\n");
			}
		}
		status += ("\n   - Player cards:\n");
		String greenCard = "";
		String brownCard = "";
		for(Card card : getPlayerCardDeck())
		{
			if(card.getType() == Card.CardType.GreenPlayerCard)
			{
				if(!greenCard.isEmpty())
				{
					greenCard += "\n               ";
				}
				greenCard += card.getName();
			}
			else
			{
				if(!brownCard.isEmpty())
				{
					brownCard += "\n               ";
				}
				brownCard += card.getName();
			}
		}
		if(greenCard.isEmpty())
		{
			greenCard = "Green: none";
		}
		else
		{
			greenCard = "Green: " + greenCard;
		}
		if(brownCard.isEmpty())
		{
			brownCard = "Brown: none";
		}
		else
		{
			brownCard = "Brown: " + brownCard;
		}
		
		status += ("        " + greenCard) + "\n";
		status += ("        " + brownCard) + "\n\n";
		return status;
	}
	
	/**
	 * @return the isComputerAI
	 */
	public boolean isComputerAI() 
	{
		return isComputerAI;
	}
	
	/**
	 * @return the dataInput
	 */
	public AbstractDataInput getDataInput() 
	{
		return this.dataInput;
	}
	
	/**
	 * Initialize the start of a player turn
	 */
	public void execBeginOfTurn()
	{
		for(Card card : this.getPlayerCardDeck())
		{
			card.setActiveState(true);
		}
		for(Card card : this.getCityAreaCardDeck())
		{
			card.setActiveState(true);
		}
		for(Card card : this.getInFrontOfHimDeck())
		{
			card.setActiveState(true);
		}
	}
	
	/**
	 * Initialize the end of a player turn
	 */
	public void execEndOfTurn()
	{
	}

	/**
	 * @return the player name and the amount of money the player has
	 */
	public String getMoneyInformation() 
	{
		return this.getFullName() + " (" + this.bankAccount.getMoneyAmount() + "$)";
	}

	public String getPlayerCardInformation() 
	{
		return this.getFullName() + " (" + this.playerCardDeck.size() + " PlayerCard" + (this.playerCardDeck.size()<2 ? "" : ")" + ")");
	}

	/**
	 * @return the color and player name
	 */
	public String getFullName() 
	{
		return "(" + this.getColor() + ") " + this.getName();
	}

	/**
	 * @return money and building information
	 */
	public String getMoneyAndBuildingInformation(int totalBuildingPerPlayers) 
	{
		int buildingCount = totalBuildingPerPlayers - buildingList.size();
		return this.getFullName() + " (" + this.bankAccount.getMoneyAmount() + "$ and " + buildingCount + " building" + (buildingCount > 1 ? "s" : "") + ")";
	}
	
	/**
	 * Get the list of cards that have actions to stop the given target
	 * @param targetList list of target action for stop card
	 * @return list of cards that stop target
	 */
	public List<Card> getStopCards(Action.StopTarget[] targetList)
	{
		ArrayList<Card> stopCards = new ArrayList<Card>();
		if (this.playerCardDeck != null)
		{
			for(Card card : this.playerCardDeck)
			{
				for(Action.StopTarget target : targetList)
				{
					if (card.doesStop(target))
					{
						stopCards.add(card);
						break;
					}
				}
			}
		}
		if (this.cityAreaCardDeck != null)
		{
			for(Card card : this.cityAreaCardDeck)
			{
				if (card.isEnabled())
				{
					for(Action.StopTarget target : targetList)
					{
						if (card.doesStop(target))
						{
							stopCards.add(card);
							break;
						}
					}
				}
			}
		}
		return stopCards;
	}
	
	/**
	 * Default to string description
	 * @return the player's name
	 */
	@Override
	public String toString()
	{
		return this.getFullName();
	}
}
