package ankhmorpork.model;

import ankhmorpork.collection.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ankhmorpork.collection.Party;
import ankhmorpork.model.Card;
import ankhmorpork.model.landmark.Demon;
import ankhmorpork.model.landmark.Minion;
import ankhmorpork.model.landmark.Troll;
import ankhmorpork.ui.model.PieceUI;
import ankhmorpork.util.Environment;

/**
 * Board containing areas and pieces
 * @see Area
 * @see Actor
 * @author Team 2
 * @since Build 1
 */
public class Board extends Actor
{
	private List<Area> areaList = new ArrayList<Area>();
	private Die die;
	private Deck<Card> randomEventCardDeck = new Deck<Card>();
	private Deck<Card> personalityCardDeck = new Deck<Card>();
	private Deck<Card> discardDeck = new Deck<Card>();
	private Deck<Card> binDeck = new Deck<Card>();
	private Card lastPlayed = null;
	private Party<Troll> trollList = new Party<Troll>();
	private Party<Demon> demonList = new Party<Demon>();
	private PieceUI troubleUI;
	
	/**
	 * Board constructor which receive all the data from the GameManager
	 * @param personalityCardList list of personality cards
	 * @param playerCardList list of player cards
	 * @param randomEventCardList list of random events cards
	 * @param areaList list of territory
	 * @param nbSideDie the amount of side the die has
	 * @param nbDemons the total amount of demons
	 * @param nbTrolls the total amount of trolls
	 * @param nbMaxGoldTotal the total amount of gold in play
	 */
	public Board(List<Card> personalityCardList,
			List<Card> playerCardList,
			List<Card> randomEventCardList,
			List<Area> areaList,
			int nbSideDie,
			int nbDemons,
			int nbTrolls,
			int nbMaxGoldTotal)
	{
		for(int i = 0; i < nbDemons; i++)
		{
			demonList.add(new Demon());
		}
		
		for(int i = 0; i < nbTrolls; i++)
		{
			trollList.add(new Troll());
		}
		
		this.setTroubleUI(troubleUI);		
		bankAccount = new BankAccount(this, nbMaxGoldTotal);
		personalityCardDeck.addAll(personalityCardList);
		playerCardDeck.addAll(playerCardList);
		randomEventCardDeck.addAll(randomEventCardList);
		this.areaList.addAll(areaList);
		die = new Die(nbSideDie);
	}
	
	/**
	 * Roll the dice
	 * @return the value on the dice
	 */
	public int rollDice()
	{
		return die.castDie(1);
	}
	
	/**
	 * Discard a card
	 * @param card the card to discard
	 */
	public void discardCard(Card card)
	{
		discardDeck.addCard(card);
	}
	
	/**
	 * Discard last played card
	 */
	public void discardLastPlayedCard()
	{
		if (lastPlayed != null)
		{
			discardDeck.addCard(lastPlayed);
		}
	}
	
	/**
	 * Set a card to the last played placeholder
	 * @param card the card just played
	 */
	public void setLastPlayedCard(Card card)
	{
		if (card != null)
		{
			discardLastPlayedCard();
			lastPlayed = card;
		}
	}
	
	/**
	 * Get the last played card
	 * @return the card just played
	 */
	public Card getLastPlayedCard()
	{
		return lastPlayed;
	}
	
	/**
	 *  a card
	 * @param card the card to discard to bin (permanent discard)
	 */
	public void discardCardToBin(Card card)
	{
		binDeck.addCard(card);
	}

	/**
	 * Get the list of areas on board
	 * @return the areaList
	 */
	public List<Area> getAreaList() 
	{
		return Collections.unmodifiableList(areaList);
	}

	/**
	 * Get the area of given number
	 * @param number number of wanted area
	 * @return area of given number
	 */
	public Area getArea(int number) 
	{
		if (areaList != null)
		{
			for(Area area : areaList)
			{
				if (area.getNumber() == number)
				{
					return area;
				}
			}
		}
		return null;
	}

	/**
	 * Get the area of given number and its neighbours
	 * @param number number of wanted area
	 * @return area of given number
	 */
	public List<Area> getAreaNeighborhood(int number) 
	{
		Area area = getArea(number);
		if (area != null)
		{
			return area.getAreaAndNeighborhood();
		}
		return null;
	}

	/**
	 * Get the deck of random event cards
	 * @return the randomEventCardDeck
	 */
	public Deck<Card> getRandomEventCardDeck() 
	{
		return randomEventCardDeck;
	}

	/**
	 * Get the deck of personality cards
	 * @return the personalityCardDeck
	 */
	public Deck<Card> getPersonalityCardDeck() 
	{
		return personalityCardDeck;
	}

	/**
	 * Get the discard deck
	 * @return the discardDeck
	 */
	public Deck<Card> getDiscardDeck() 
	{
		return discardDeck;
	}

	/**
	 * Get the bin (permanent discard) deck
	 * @return the discardDeck
	 */
	public Deck<Card> getBinDeck() 
	{
		return binDeck;
	}
	
	/**
	 * Get the list of trolls on board (not placed in areas)
	 * @return the trollList
	 */
	public Party<Troll> getTrollList() 
	{
		return trollList;
	}

	/**
	 * Add a troll to the board
	 * @param troll the troll to add
	 */
	public void addTroll(Troll troll)
	{
		if (trollList == null && troll != null)
		{
			trollList = new Party<Troll>();
		}
		trollList.add(troll);
	}
	
	/**
	 * Remove a troll from board
	 * @return the removed troll
	 */
	public Troll removeTroll()
	{
		if (trollList != null && !trollList.isEmpty())
		{
			Troll troll = trollList.get(0);
			trollList.remove(troll);
			return troll;
		}
		return null;
	}
	
	/**
	 * Get the list of demons on board (not placed in areas)
	 * @return the demonList
	 */
	public Party<Demon> getDemonList() 
	{
		return demonList;
	}
	
	/**
	 * Add a demon to the board
	 * @param demon the demon to add
	 */
	public void addDemon(Demon demon)
	{
		if (demonList == null && demon != null)
		{
			demonList = new Party<Demon>();
		}
		demonList.add(demon);
	}
	
	/**
	 * Remove a demon from board
	 * @return the removed demon
	 */
	public Demon removeDemon()
	{
		if (demonList != null && !demonList.isEmpty())
		{
			Demon demon = demonList.get(0);
			demonList.remove(demon);
			return demon;
		}
		return null;
	}
	
	/**
	 * Get the current info about the board for each area
	 * Area name, minions, trouble?, building?, demons and trolls
	 * @return Area name, minions, trouble?, building?, demons and trolls for each area
	 */
	public String getCurrentBoardInfo()
	{
		String status = ("Current state of the game board:\n\n");
		status += ("       area           minions  trouble?  building?  demons  trolls\n\n");
		
		for(Area area : this.areaList)
		{
			String minionValue = "";
			for(Minion minion : area.getMinionList())
			{
				if(!minionValue.isEmpty())
				{
					minionValue += ",";
				}
				minionValue += minion.getColor().getInitial();
			}
			if(minionValue.isEmpty())
			{
				minionValue = "none";
			}
			
			String hasTrouble;
			if(area.hasTrouble())
			{
				hasTrouble = "YES";
			}
			else
			{
				hasTrouble = "no";
			}
			String hasBuilding;
			if(area.hasBuilding())
			{
				hasBuilding = area.getBuilding().getColor().toString().toUpperCase();
			}
			else
			{
				hasBuilding = "no";
			}
			status += ("   " 
					+ Environment.padRight(area.getName(), 19) 
					+ Environment.padRight(minionValue, 11) 
					+ Environment.padRight(hasTrouble, 11)
					+ Environment.padRight(hasBuilding, 11)
					+ area.getNbDemon()
					+ "       "
					+ area.getNbTroll() + "\n");
		}
		
		return status;
	}
	
	/**
	 * Return the information about the amount of money left in the bank
	 * @return a string that show how much money left in the bank formatted for the user
	 */
	public String getBankInfo()
	{
		String money = bankAccount.getMoneyAmount() + (bankAccount.getMoneyAmount()>1 ? " Ankh-Morpork dollars" : " Ankh-Morpork dollar");
		return ("The bank has " + money + ".\n");
	}

	/**
	 * @return the troubleUI
	 */
	public PieceUI getTroubleUI() {
		return troubleUI;
	}

	/**
	 * @param troubleUI the troubleUI to set
	 */
	public void setTroubleUI(PieceUI troubleUI) {
		this.troubleUI = troubleUI;
	}
	
	/**
	 * @return the die
	 */
	public Die getDie() 
	{
		return this.die;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString()
	{
		return "The City of Ankh-Morpork";
	}
}
