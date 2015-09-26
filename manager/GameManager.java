package ankhmorpork.manager;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ankhmorpork.collection.Deck;
import ankhmorpork.datainput.AbstractDataInput;
import ankhmorpork.datainput.ConsoleDataInput;
import ankhmorpork.datainput.GroupDataInput;
import ankhmorpork.manager.db.XMLFile;
import ankhmorpork.model.*;
import ankhmorpork.model.actions.PlayerCardAction;
import ankhmorpork.model.actions.SaveGame;
import ankhmorpork.model.actions.ShowGameStatus;
import ankhmorpork.model.actions.base.NextPlayerTurn;
import ankhmorpork.model.actions.personality.NoPlayerCard;
import ankhmorpork.model.actions.playercard.BorrowMoneyFromTheBank;
import ankhmorpork.model.landmark.*;
import ankhmorpork.ui.AnkhMorporkUI;
import ankhmorpork.ui.model.AreaUI;
import ankhmorpork.ui.model.PieceUI;
import ankhmorpork.util.Definition;
import ankhmorpork.util.Environment;
import ankhmorpork.util.type.CardType;

/**
 * Manage game flow and state
 * @author Team 2
 * @since Build 1
 */
public class GameManager 
{
	public enum EndGameType
	{
		riotTroubleEvent,
		noPlayerCard,
		winAtBeginTurn
	}
	
	// Game Constant setting
	private List<Color> availableColorList = new ArrayList<Color>();
	private List<Card> restrictedCard2PlayerList = new ArrayList<Card>();
	private HashMap<Integer, Integer> startingMinionPerArea = new HashMap<Integer, Integer>();
	private int nbInitCards;
	private int nbBuildingsPerPlayer;
	private int nbMinionsPerPlayer;
	private int nbGoldStartPerPlayer;
	private int nbMaxNbPlayers;

	// Game Values setting
	private List<Player> playerList = new ArrayList<Player>();
	private Board board = null;
	private int currentPlayerID = 0;
	private int turnNumber = 1;
	private boolean showSaveGame = true;
	
	// Game play settings
	private boolean canPlayPlayerCard = true;
	private List<Action> actionListToExecute = new ArrayList<Action>();
	private boolean isEndGame = false;

	private List<Player> winnerList = new ArrayList<Player>();
	private Player stopPlayer = null;
	private Area stopArea = null;
	
	// System IO
	private ConsoleDataInput systemDataInput = new ConsoleDataInput(null, false);
	
	// UI interface
	private AnkhMorporkUI ankhMorporkUI = null;
	
	/**
	 * Constructor for the GameManager
	 */
	private GameManager() 
	{
		
	}
	
	/**
	 * Add a card that is restricted and should be removed if there only 2 players
	 * @param card
	 * @return
	 */
	private boolean addRestrictedCard2Player(Card card)
	{
		return restrictedCard2PlayerList.add(card);
	}

	/**
	 * Add an active player to the game
	 * @param player to be added to the list of active players
	 * @return if the player was added successfully
	 */
	public boolean addPlayer(Player player)
	{
		return playerList.add(player);
	}
	
	/**
	 * Return the list of players
	 * @return the active list of players
	 */
	public List<Player> getPlayerList()
	{
		return playerList;
	}

	/**
	 * Return the player of given ID
	 * @return the wanted player
	 */
	public Player getPlayer(int id)
	{
		if (playerList != null)
		{
			for(Player player : playerList)
			{
				if (player.getId() == id)
				{
					return player;
				}
			}
		}
		return null;
	}

	/**
	 * Return the list of available colors
	 * @return the list of available colors
	 */
	public List<Color> getAvailableColorList()
	{
		return availableColorList;
	}

	/**
	 * Add a color to the list of available colors to chose by player
	 * @param player1
	 * @return if the player was added correctly
	 */
	private boolean addAvailableColor(Color color)
	{
		return availableColorList.add(color);
	}

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * @return the player playing the game with their color, id and personality card
	 */
	public String getPlayerStatusInfo()
	{
		String status = ("There are " + playerList.size() + " players:\n\n");
		for(Player player : playerList)
		{
			status += ("   Player "+ player.getId() +" " 
					+ Environment.padRight("(" + player.getColor().getName() + ")",8) 
					+ " is playing as " + player.getCardName() + ".\n");
		}
		
		return status;
	}
	
	/**
	 * @return the detailed information of each players
	 */
	public String getPlayersDetailedInfo()
	{
		String status = "";
		for(Player player : playerList)
		{
			status += player.getDetailedInfo();
		}
		return status;
	}
	
	/**
	 * @return the detailed information of the wanted player
	 */
	public String getPlayersDetailedInfo(Player player)
	{
		return player.getDetailedInfo();
	}
	
	/**
	 * @return the current state of the entire game as a string
	 */
	public String getEntireGameStatus()
	{
		String status = ("Game State\n");
		status += ("----------\n\n");
		status += (getPlayerStatusInfo() + "\n");
		status += (getBoard().getCurrentBoardInfo() + "\n");
		status += (getPlayersDetailedInfo());
		status += (getBoard().getBankInfo());
		return status;
	}
	
	/**
	 * @return the current state of the game as viewed by the current player as a string
	 */
	public String getCurrentPlayerGameStatus()
	{
		String status = ("Game State for " + getCurrentPlayer().getFullName() + "\n");
		status += ("----------\n\n");
		status += (getPlayersDetailedInfo(getCurrentPlayer()));
		return status;
	}
	
	/**
	 * After removing restricted cards, shuffle all decks, place green player cards on top of brown player cards in one deck
	 */
	private void initializeDecks()
	{
		discardRestrictedCards();
		
		board.getRandomEventCardDeck().shuffleDeck();
		board.getPersonalityCardDeck().shuffleDeck();
		Deck<Card> brownCards = new Deck<Card>();
		Deck<Card> greenCards = new Deck<Card>();
		for(Card card : board.getPlayerCardDeck())
		{
			if(card.getType() == Card.CardType.GreenPlayerCard)
			{
				greenCards.addCard(card);
			}
			else
			{
				brownCards.addCard(card);
			}
		}
		brownCards.shuffleDeck();
		greenCards.shuffleDeck();
		board.getPlayerCardDeck().clear();
		board.getPlayerCardDeck().addAll(brownCards);
		board.getPlayerCardDeck().addAll(greenCards);
	}
	
	/**
	 * Initialize the game state
	 * @throws Exception 
	 */
	public void initializeGame() throws Exception
	{
		systemDataInput.initializePlayers(this);
		initializeDecks();
		initializePlayers();
		initializeLandmarks();
		initializeStatus();
	}
	
	/**
	 * Distribute cards, minions, buildings and money to players
	 */
	private void initializePlayers()
	{
		for(Player player : playerList)
		{
			// Distribute personality cards
			player.setPersonalityCard(board.getPersonalityCardDeck().pickTopCard());
			
			// Distribute money
			board.getBankAccount().transfertAmountTo(player, nbGoldStartPerPlayer, false);
			
			// Distribute minions
			for(int i = 0; i < nbMinionsPerPlayer; i++)
			{
				player.addMinion(new Minion());
			}
			
			// Distribute buildings
			for(int i = 0; i < nbBuildingsPerPlayer; i++)
			{
				player.addBuilding(new Building());
			}
		}

		// Distribute player cards
		for(int i = 0; i < nbInitCards; i++)
		{
			for(Player player : playerList)
			{
				player.getPlayerCardDeck().addCard(board.getPlayerCardDeck().pickTopCard());
			}
		}
	}
	
	/**
	 * Place landmark (minions) where stated
	 */
	private void initializeLandmarks()
	{
		// Do nothing if not set
		if (startingMinionPerArea == null)
		{
			return;
		}
		
		// Check starting landmark for each area
		for(Area area : board.getAreaList())
		{
			int number = area.getNumber();
			Integer minionCount = startingMinionPerArea.get(number);
			if (minionCount != null)
			{
				for(int i = 0; i < minionCount; i++)
				{
					for(Player player : playerList)
					{
						area.addMinion(player.removeMinion());
					}
				}
			}
		}
	}
	
	/**
	 * Initialize game status (round 1, turn 1) by finding the current (starting) player
	 */
	private void initializeStatus()
	{
		if (playerList == null || playerList.isEmpty())
		{
			return;
		}
		
		// Cast die to determine starting player until one player has a better result (no tie)
		List<Player> startingPlayers = playerList;
		while (startingPlayers.size() > 1)
		{
			List<Player> maxResultPlayers = new ArrayList<Player>();
			int maxResult = 0;
			
			// All players with best result must roll die
			// In the first round that consists of all players
			for(Player player : startingPlayers)
			{
				int result = board.rollDice();
				
				// The player having the better result is remembered
				if (result > maxResult)
				{
					maxResultPlayers.clear();
					maxResultPlayers.add(player);
					maxResult = result;
				}
				// If tied, the player is added for another round of die rolling
				else if (result == maxResult)
				{
					maxResultPlayers.add(player);
				}
			}
			startingPlayers = maxResultPlayers;
		}
		
		// Set current game status
		Player starting = startingPlayers.get(0);
		increaseTurnCount();
		this.currentPlayerID = starting.getId();
	}
	
	/**
	 * Discard the restricted cards if the 2 players condition is met
	 */
	private void discardRestrictedCards()
	{
		if (playerList.size() == 2)
		{
			for(Card card : restrictedCard2PlayerList)
			{
				switch (card.getType())
				{
					case GreenPlayerCard:
					case BrownPlayerCard:
						Card playerCardMatch = board.getPlayerCardDeck().pickCardByID(card.getId());
						if (playerCardMatch != null)
						{
							board.getDiscardDeck().addCard(playerCardMatch);
						}
						break;
					case CityAreaCard:
						break;
					case PersonalityCard:
						Card personalityCardMatch = board.getPersonalityCardDeck().pickCardByID(card.getId());
						if (personalityCardMatch != null)
						{
							board.getDiscardDeck().addCard(personalityCardMatch);
						}
						break;
					case RandomEventCard:
						Card randomEventCardMatch = board.getRandomEventCardDeck().pickCardByID(card.getId());
						if (randomEventCardMatch != null)
						{
							board.getDiscardDeck().addCard(randomEventCardMatch);
						}
						break;
				}
			}
		}
	}
	
	/**
	 * Load the game configuration Data.xml and create the GameController
	 * @param path to the configuration file
	 * @return a game manager instance if it succeed
	 * @throws Exception the game manager wasn't created
	 */
	public static GameManager createGameManager(String path) throws Exception
	{
		GameManager game = new GameManager();
		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		List<Card> personalityCardList = new ArrayList<Card>();
		List<Card> playerCardList = new ArrayList<Card>();
		List<Card> randomEventCardList = new ArrayList<Card>();
		HashMap<Integer, Area> areaMap = new HashMap<Integer, Area>();
		int sideDieNb = 12;
		int initCardNb = 5;
		int demonNb = 4;
		int trollNb = 3;
		int buildingPerPlayerNb = 6;
		int minionPerPlayerNb = 12;
		int goldStartPerPlayerNb = 10;
		int maxGoldTotalNb = 120;
		int nbMaxNbPlayers = 4;
		
		game.ankhMorporkUI = new AnkhMorporkUI(game);

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getDocumentElement().getChildNodes();

		PieceUI minionUI = new PieceUI();
		PieceUI buildingUI = new PieceUI();
		PieceUI demonUI = new PieceUI();
		PieceUI trollUI = new PieceUI();
		PieceUI troubleUI = new PieceUI();

		for (int i = 0; i < nList.getLength(); i++) 
		{
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				String restriction = eElement.getAttribute("restriction");
				boolean isRestriction2Player = (restriction != null && !restriction.isEmpty());
				
				switch (nNode.getNodeName())
				{
				case "color" :
				{
					// All available colors that can be used in the game
					String color = eElement.getAttribute("name");
					String hexColorCode = eElement.getAttribute("hexcode");
					if(Color.getColor(color) == null)
					{
						Color.AddColor(color, hexColorCode);
					}
				}
				break;
				case "minion" :
				{
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							int x = readIntAttrElement(eChildElement, "x", -1);
							int y = readIntAttrElement(eChildElement, "y", -1);
							Point point = new Point(x, y);
							minionUI.addDelimiterPoint(point);
						}
					}
				}
				break;
				case "building" :
				{
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							int x = readIntAttrElement(eChildElement, "x", -1);
							int y = readIntAttrElement(eChildElement, "y", -1);
							Point point = new Point(x, y);
							buildingUI.addDelimiterPoint(point);
						}
					}
				}
				break;
				case "demon" :
				{
					AddPictureSectionPieceUI(eElement, demonUI, null);					
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							int x = readIntAttrElement(eChildElement, "x", -1);
							int y = readIntAttrElement(eChildElement, "y", -1);
							Point point = new Point(x, y);
							demonUI.addDelimiterPoint(point);
						}
					}
				}
				break;
				case "troll" :
				{
					AddPictureSectionPieceUI(eElement, trollUI, null);					
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							int x = readIntAttrElement(eChildElement, "x", -1);
							int y = readIntAttrElement(eChildElement, "y", -1);
							Point point = new Point(x, y);
							trollUI.addDelimiterPoint(point);
						}
					}
				}
				break;
				case "trouble" :
				{
					AddPictureSectionPieceUI(eElement, troubleUI, null);
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							int x = readIntAttrElement(eChildElement, "x", -1);
							int y = readIntAttrElement(eChildElement, "y", -1);
							Point point = new Point(x, y);
							troubleUI.addDelimiterPoint(point);
						}
					}
				}
				break;
				case "player" :
				{
					// player color + configure max amount of players
					String colorName = eElement.getAttribute("color");
					Color color = Color.getColor(colorName);
					game.addAvailableColor(color);
					
					NodeList nChildList = nNode.getChildNodes();
					for (int j = 0; j < nChildList.getLength(); j++) 
					{			
						Node nChildNode = nChildList.item(j);
						if (nChildNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eChildElement = (Element) nChildNode;
							switch (eChildElement.getNodeName())
							{
							case "minion" :
							{
								AddPictureSectionPieceUI(eChildElement, minionUI, color);
							}
							break;
							case "building" :
							{
								AddPictureSectionPieceUI(eChildElement, buildingUI, color);
							}
							break;
							}
						}
					}
				}
				break;
				case "start" :
				{
					// Initial configuration if we want to modify the game rules
					initCardNb = readIntAttrElement(eElement, "playerCard", initCardNb);
					demonNb = readIntAttrElement(eElement, "demonQty", demonNb);
					trollNb = readIntAttrElement(eElement, "trollQty", trollNb);
					buildingPerPlayerNb = readIntAttrElement(eElement, "buildingQty", buildingPerPlayerNb);
					minionPerPlayerNb = readIntAttrElement(eElement, "minionQty", minionPerPlayerNb);
					goldStartPerPlayerNb = readIntAttrElement(eElement, "goldstart", goldStartPerPlayerNb);
					maxGoldTotalNb = readIntAttrElement(eElement, "goldtotal", maxGoldTotalNb);
					sideDieNb = readIntAttrElement(eElement, "nbSideDie", sideDieNb);
					nbMaxNbPlayers = readIntAttrElement(eElement, "nbMaxNbPlayers", nbMaxNbPlayers);
				}
				break;
				case "PersonalityCard" :
				{
					// loading personality cards
					int id = readIntAttrElement(eElement, "id", -1);
					String name = eElement.getAttribute("name");
					String scroll = eElement.getAttribute("scroll");
					List<Action> actionList = new ArrayList<Action>();
          List<String> scrollValues = new ArrayList<String>(Arrays.asList(scroll.split(";")));
          String scrollID = scrollValues.remove(0);
					actionList.add(Environment.getActionFactory().CreateAction(CardType.PERSONALITY, scrollID, scrollValues));
					Card card = new Card(id, name, Card.CardType.PersonalityCard, actionList, scroll);
					personalityCardList.add(card);
					if(isRestriction2Player)
					{
						game.addRestrictedCard2Player(card);
					}
				}
				break;
				case "RandomEventCard" :
				{
					// loading random event cards
					int id = readIntAttrElement(eElement, "id", -1);
					String name = eElement.getAttribute("name");
					String scroll = eElement.getAttribute("scroll");
					List<Action> actionList = new ArrayList<Action>();
          List<String> scrollValues = new ArrayList<String>(Arrays.asList(scroll.split(";")));
          String scrollID = scrollValues.remove(0);
					actionList.add(Environment.getActionFactory().CreateAction(CardType.EVENT, scrollID, scrollValues));
					Card card = new Card(id, name, Card.CardType.RandomEventCard, actionList, scroll);
					randomEventCardList.add(card);
					if(isRestriction2Player)
					{
						game.addRestrictedCard2Player(card);
					}
				}
				break;
				case "CityAreaCard" :
				{
					// loading city area cards
					int id = readIntAttrElement(eElement, "id", -1);
					int areaId = readIntAttrElement(eElement, "area", -1);
					String name = eElement.getAttribute("name");
					String scroll = eElement.getAttribute("scroll");
					if(!areaMap.containsKey(areaId))
					{
						areaMap.put(areaId, new Area(areaId));
						game.ankhMorporkUI.addArea(new AreaUI(areaId));
					}
					Area area = areaMap.get(areaId);
					List<Action> actionList = new ArrayList<Action>();
					
          List<String> scrollValues = new ArrayList<String>(Arrays.asList(scroll.split(";")));
          String scrollID = scrollValues.remove(0);
          scrollValues.add(areaId + "");
					actionList.add(Environment.getActionFactory().CreateAction(CardType.CITY, scrollID, scrollValues));
					Card card = new Card(id, name, Card.CardType.CityAreaCard, actionList, scroll);
					if(isRestriction2Player)
					{
						game.addRestrictedCard2Player(card);
					}
					area.setAreaCard(card);
				}
				break;
				case "PlayerCard" :
				{
					// loading player cards
					int id = readIntAttrElement(eElement, "id", -1);
					String name = eElement.getAttribute("name");

					String colorName = eElement.getAttribute("color");
					String actions = eElement.getAttribute("actions");
					String scroll = eElement.getAttribute("scroll");
					String money = eElement.getAttribute("money");
					
					Color color = Color.getColor(colorName);
					Card.CardType cardType;
					if(color == Color.getColor(Definition.GREEN_CARD_COLOR_NAME))
					{
						cardType = Card.CardType.GreenPlayerCard;
					}
					else
					{
						cardType = Card.CardType.BrownPlayerCard;
					}

					List<Action> actionList = Environment.loadCardActions(actions.split(";"), scroll, money);
					Card card = new Card(id, name, cardType, actionList, scroll);
					
					playerCardList.add(card);
					if(isRestriction2Player)
					{
						game.addRestrictedCard2Player(card);
					}
				}
				break;
				case "Area" :
				{
					// Loading the Area on the board
					int id = readIntAttrElement(eElement, "id", -1);
					String name = eElement.getAttribute("name");
					int cost = Integer.parseInt(eElement.getAttribute("cost"));
					if(!areaMap.containsKey(id))
					{
						areaMap.put(id, new Area(id));
						game.ankhMorporkUI.addArea(new AreaUI(id));
					}
					Area area = areaMap.get(id);
					AreaUI areaUI = game.ankhMorporkUI.getArea(id);
					area.setName(name);
					area.setCost(cost);
					
					String startingPlayerMinionStr = eElement.getAttribute("playerminionstart");
					if (startingPlayerMinionStr != null && Environment.isInteger(startingPlayerMinionStr))
					{
						int areaStartingPlayerMinionNb = Integer.parseInt(startingPlayerMinionStr);
						game.startingMinionPerArea.put(id, areaStartingPlayerMinionNb);
					}
					
					String nearRiverStr = eElement.getAttribute("nearRiver");
					if (nearRiverStr != null)
					{
						boolean nearRiver = Boolean.parseBoolean(nearRiverStr);
						area.setRiverAdjacency(nearRiver);
					}
					
					NodeList nListArea = eElement.getChildNodes();
					for (int j = 0; j < nListArea.getLength(); j++) {
						Node nNodeArea = nListArea.item(j);
						if (nNodeArea.getNodeType() == Node.ELEMENT_NODE) {
							Element eElementArea = (Element) nNodeArea;
							switch (nNodeArea.getNodeName())
							{
							case "pt" :
							{
								// Adding the position and shape of that area
								int x = readIntAttrElement(eElementArea, "x", -1);
								int y = readIntAttrElement(eElementArea, "y", -1);
								areaUI.addDelimiterPoint(new Point(x,y));
								break;
							}
							case "pos" :
							{
								// Adding the position and shape of that area
								int x = readIntAttrElement(eElementArea, "x", -1);
								int y = readIntAttrElement(eElementArea, "y", -1);
								if(!areaUI.addPosition(new Point(x,y)))
								{
									// Invalid position
									areaUI.addPosition(new Point(x,y));
								}
								break;
							}
							case "Neighbor" :
							{
								// Adding a link to the neighbor area
								int neighborId = readIntAttrElement(eElementArea, "id", -1);
								if(!areaMap.containsKey(neighborId))
								{
									areaMap.put(neighborId, new Area(neighborId));
									game.ankhMorporkUI.addArea(new AreaUI(neighborId));
								}
								Area neighborArea = areaMap.get(neighborId);
								area.addNeighbor(neighborArea);
								break;
							}
							}
						}
					}
				}
				break;
				}
			}
		}

		game.ankhMorporkUI.setMinionUI(minionUI);
		game.ankhMorporkUI.setBuildingUI(buildingUI);
		game.ankhMorporkUI.setDemonUI(demonUI);
		game.ankhMorporkUI.setTrollUI(trollUI);
		game.ankhMorporkUI.setTroubleUI(troubleUI);
		
		game.nbMaxNbPlayers = nbMaxNbPlayers;
		game.nbInitCards = initCardNb;
		game.nbBuildingsPerPlayer = buildingPerPlayerNb;
		game.nbMinionsPerPlayer = minionPerPlayerNb;
		game.nbGoldStartPerPlayer = goldStartPerPlayerNb;
		
		game.board = new Board(personalityCardList,
				playerCardList,
				randomEventCardList,
				new ArrayList<Area>(areaMap.values()),
				sideDieNb,
				demonNb,
				trollNb,
				maxGoldTotalNb);
		
		return game;
	}
	
	/**
	 * Read the color of a piece to add to the PieceUI
	 * @param eElement
	 * @param piece
	 * @param color
	 * @throws Exception
	 */
	private static void AddPictureSectionPieceUI(Element eElement, PieceUI piece, Color color) throws Exception
	{
		int x1 = readIntAttrElement(eElement, "x1", -1);
		int y1 = readIntAttrElement(eElement, "y1", -1);
		int x2 = readIntAttrElement(eElement, "x2", -1);
		int y2 = readIntAttrElement(eElement, "y2", -1);
		piece.setPicture(color, Environment.PIECES_IMAGE_PATH, new Point(x1,  y1), new Point(x2, y2));
	}
	
	/**
	 * Read an integer from an XML element and return the value
	 * @param eElement
	 * @param attribute
	 * @param defaultValue //We aren't using default value, we are throwing an exception if there an issue.
	 * @return
	 * @throws Exception
	 */
	private static int readIntAttrElement(Element eElement, String attribute, int defaultValue) throws Exception
	{
		int value = defaultValue;
		String stringValue = eElement.getAttribute(attribute);
		if(stringValue != null && !stringValue.isEmpty())
		{
			value = Integer.parseInt(stringValue);
		}
		else
		{
			// There was an issue with the configuration file.
			throw new Exception("Invalid Integer Value From XML");
		}
		return value;
	}

	/**
	 * @return the currentPlayerID
	 */
	public int getCurrentPlayerID() 
	{
		return currentPlayerID;
	}

	/**
	 * @return the currentPlayer
	 */
	public Player getCurrentPlayer() 
	{
		return this.getPlayer(getCurrentPlayerID());
	}

	/**
	 * @return the turnNumber
	 */
	public int getTurnNumber() 
	{
		return turnNumber;
	}
	
	/**
	 * @param currentPlayerID the currentPlayerID to set
	 */
	public void setCurrentPlayerID(int currentPlayerID) 
	{
		this.currentPlayerID = currentPlayerID;
	}

	/**
	 * @param turnNumber the turnNumber to set
	 */
	public void setTurnNumber(int turnNumber) 
	{
		this.turnNumber = turnNumber;
	}

	/**
	 * Increase the turn count
	 */
	public void increaseTurnCount()
	{
		turnNumber++;
		this.currentPlayerID++;
		this.currentPlayerID = this.currentPlayerID % this.playerList.size();
	}
	
	/**
	 * @return the maximum amount of players
	 */
	public int getMaxAmountPlayers() 
	{
		return nbMaxNbPlayers;
	}
	
	/**
	 * @param player the stopPlayer to set
	 */
	public void setStopPlayer(Player player) 
	{
		stopPlayer = player;
	}
	
	/**
	 * @return the stopPlayer
	 */
	public Player getStopPlayer() 
	{
		return stopPlayer;
	}
	
	/**
	 * @param area the stopArea to set
	 */
	public void setStopArea(Area area) 
	{
		stopArea = area;
	}
	
	/**
	 * @return the stopArea
	 */
	public Area getStopArea() 
	{
		return stopArea;
	}

	/**
	 * Initialize the start of a turn of a new player
	 */
	private void initBeginOfTurn()
	{
		for(Action action : this.getCurrentPlayer().getPersonalityCard().getActions())
		{
			action.execute(this);
		}
		
		canPlayPlayerCard = true;
		this.getCurrentPlayer().getDataInput().printMessage(this.getEntireGameStatus());
		this.getCurrentPlayer().getDataInput().printMessage(this.getCurrentPlayerGameStatus());
		this.showSaveGame = true;
		
		for(Card card : this.getCurrentPlayer().getCityAreaCardDeck())
		{
			card.setActiveState(true);
		}
	}
	
	/**
	 * Initialize the end of a turn of a new player
	 */
	public void nextTurn()
	{
		this.actionListToExecute.clear();
		this.board.discardLastPlayedCard();
		this.getCurrentPlayer().getDataInput().printMessage(this.getCurrentPlayerGameStatus());
		this.getCurrentPlayer().execEndOfTurn();
		
		int nbPlayerCardPickup = this.getAmountOfPlayerCardToFillYourHand() - this.getCurrentPlayer().getPlayerCardDeck().size();
		for(int i = 0; i < nbPlayerCardPickup; i++)
		{
			this.getCurrentPlayer().getPlayerCardDeck().addCard(this.getNextPlayerCard());
		}
		
		currentPlayerID = (currentPlayerID + 1) % this.getPlayerList().size();
		if(currentPlayerID == 0)
		{
			currentPlayerID = this.getPlayerList().size();
		}
		this.getCurrentPlayer().execBeginOfTurn();
		initBeginOfTurn();
	}
	
	/**
	 * A console interface for in game commands.
	 * @throws Exception exception
	 */
	public void playGame() throws Exception
	{
		allPlayersDataInput().playGame();
		
		getCurrentPlayer().execBeginOfTurn();
		initBeginOfTurn();
		
		ankhMorporkUI.setVisible(true);
		Thread.sleep(50); // 50ms for the UI to show up
			
		while(!isEndGame)
		{
			Player currentPlayer = getCurrentPlayer();
			List<Object> possibleActionsList = new ArrayList<Object>();
			List<Card> unusableCardList = new ArrayList<Card>();
			if(!actionListToExecute.isEmpty())
			{
				possibleActionsList.add(actionListToExecute.get(0));
			}
			for(Card card : currentPlayer.getCityAreaCardDeck())
			{
				if(card.isActive() && card.canExecute(this) && card.isEnabled())
				{
					possibleActionsList.add(card);
				}
			}
			for(Card card : currentPlayer.getPlayerCardDeck())
			{
				if(card.canBeExecutedTurnBase(canPlayPlayerCard) && card.canExecute(this) && card.isEnabled())
				{
					possibleActionsList.add(card);
				}
				else
				{
					unusableCardList.add(card);
				}
			}

			if(possibleActionsList.isEmpty())
			{
				nextTurn();
			}
			else
			{
				if(unusableCardList.size() > 0)
				{
					this.getSystemDataInput().printMessage(this.getCurrentPlayer().toString() + ":  - Unusable player card:");
					for(Card card : unusableCardList)
					{
						this.getSystemDataInput().printMessage(this.getCurrentPlayer().toString() + ":  - " + card.toString());
					}
				}
				
				if(this.showSaveGame)
				{
					this.showSaveGame = false;
					possibleActionsList.add(new SaveGame());
				}
				possibleActionsList.add(new ShowGameStatus());
				if(!canPlayPlayerCard && actionListToExecute.isEmpty())
				{
					possibleActionsList.add(new NextPlayerTurn());
				}
				Object selection = currentPlayer.getDataInput().askPlayAction(possibleActionsList);
				if(selection == null)
				{
					return;
				}
				executeCardAction(selection);
			}
		}
	}
	
	/**
	 * A console interface for end game commands.
	 * @param type end game type
	 */
	public void endGame(EndGameType type)
	{
		boolean personalityWin = true;
		this.isEndGame = true;
		switch (type)
		{
			case winAtBeginTurn:
				this.winnerList.add(this.getCurrentPlayer());
				break;
			case noPlayerCard:
				for(Player player : this.getPlayerList())
				{
					if (!player.getPersonalityCard().getActions(NoPlayerCard.class).isEmpty())
					{
						this.winnerList.add(player);
						break;
					}
				}
				break;
		case riotTroubleEvent:
			break;
		}
		
		if (this.winnerList.isEmpty())
		{
			this.winnerList.addAll(calculatePoints());
			personalityWin = false;
		}
		
		this.getSystemDataInput().printMessage("We have " + (this.winnerList.size() == 1 ? "a winner!" : "winners!") + " Congratulations to:");
		for (Player winner : this.winnerList)
		{
			this.getSystemDataInput().printMessage(winner.getFullName() + (personalityWin ? " as " + winner.getCardName() + ".": " with " + winner.getPoints() + " points."));
		}
		
		this.getSystemDataInput().printMessage(""); // spacer
		
		this.getSystemDataInput().askTrueFalseQuestion("The end!", "Terminate!", "Terminate anyway ;p");
	}
	
	/**
	 * Calculate and set the players' points and return the winning players highest points (and most expensive building when tied)
	 * Remark: offer the chance to refund loans before calling this method
	 * @return the list of winning players
	 */
	/**
	 * Calculate and set the players' points and return the winning players highest points (and most expensive building when tied)
	 * Remark: offer the chance to refund loans before calling this method
	 * @return the list of winning players
	 */
	private List<Player> calculatePoints()
	{
		this.systemDataInput.printMessage("Calculating points...");
		
		ArrayList<Player> winningPlayers = new ArrayList<Player>();
		int maxPoints = 0;
		int maxBuilding = 0;
		
		for(Player player : playerList)
		{
			int points = 0;
			int bestBuilding = 0;
			
			for(Area area : board.getAreaList())
			{
				if (!area.hasDemon())
				{
					if (area.hasBuilding(player))
					{
						points += area.getCost();
						if (area.getCost() > bestBuilding)
						{
							bestBuilding = area.getCost();
						}
					}
					points += 5 * area.getMinionList(player).size();
				}
			}
			
			int playerMoney = player.getMoneyAmount();
			points += playerMoney;
			
			for(Card card : player.getInFrontOfHimDeck())
			{
				for(Action action : card.getActions(BorrowMoneyFromTheBank.class))
				{
					int moneyToPayBack = ((BorrowMoneyFromTheBank)action).getMoneyToPayBack();
					if (playerMoney >= moneyToPayBack)
					{
						points -= moneyToPayBack;
						playerMoney -= moneyToPayBack;
					}
					else
					{
						points -= ((BorrowMoneyFromTheBank)action).getPointsToLose();
					}
				}
			}
			
			player.addPoints(points);
			player.getDataInput().printMessage(points + " points");
			
			if (points > maxPoints || (points == maxPoints && bestBuilding > maxBuilding))
			{
				maxPoints = points;
				maxBuilding = bestBuilding;
				winningPlayers.clear();
				winningPlayers.add(player);
			}
			else if (points == maxPoints && bestBuilding == maxBuilding)
			{
				winningPlayers.add(player);
			}
		}
		
		return winningPlayers;
	}
	
	/**
	 * Execute player card
	 * @param object
	 */
	public void executeCardAction(Object object)
	{
		if (this.isEndGame)
		{
			return;
		}
		
		if(object instanceof Action)
		{
			Action action = (Action)object;
			String message = "Do you want to play the action: " + action.toString();
			boolean playAction = true;
			
			if(action.isOptional())
			{
				playAction = this.getCurrentPlayer().getDataInput().askTrueFalseQuestion(message, "Yes", "No");
			}
			
			if(playAction)
			{
				action.execute(this);
			}
			actionListToExecute.remove(action);
		}
		else if(object instanceof Card)
		{
			Card card = (Card)object;
			Card cardToDiscard = null;
			if(card.canExecute(this))
			{
				cardToDiscard = this.getCurrentPlayer().getPlayerCardDeck().pickCardByID(card.getId());
				this.getBoard().setLastPlayedCard(cardToDiscard);
				
				card.setActiveState(false);
				if(!card.getActions().isEmpty())
				{
					for(int i = 0; i < card.getActions().size(); i++)
					{
						actionListToExecute.add(i, card.getActions().get(i));
					}
				}
			}
			
			// if the action isn't from a playercard, we execute it right away.
			if(actionListToExecute.size() > 0 && !(actionListToExecute.get(0) instanceof PlayerCardAction))
			{
				executeCardAction(actionListToExecute.remove(0));
			}
			
			if(cardToDiscard != null)
			{
				canPlayPlayerCard = false;
			}
			else
			{
				this.getBoard().discardCard(this.getCurrentPlayer().getInFrontOfHimDeck().pickCardByID(card.getId()));
			}
		}
	}
	
	public AbstractDataInput allPlayersDataInput()
	{
		return new GroupDataInput(this.getPlayerList());
	}

	public AbstractDataInput getSystemDataInput()
	{
		// Print one message in console mode
		// This is to be replaced by a GroupDataInput in TCP / IP mode
		return systemDataInput;
	}

	/**
	 * Save game to file
	 * @param path where to save the game state
	 * @throws Exception
	 * @see loadGame
	 */
	public void saveGame(String path) throws Exception
	{
		XMLFile db = new XMLFile(path);
		db.save(this);
		System.out.println("The file '" + path + "' has been saved successfully.");
	}

	/**
	 * Load game from file
	 * @param path to the game file
	 * @throws Exception
	 * @see saveGame
	 */
	public static GameManager loadGame(String path) throws Exception
	{
		XMLFile db = new XMLFile(path + Definition.GAME_FILE_EXTENSION);
		GameManager gameManager = db.load();
		
		System.out.println(gameManager.getEntireGameStatus());
		return gameManager;
	}
	
	/**
	 * @return the nbBuildingsPerPlayer
	 */
	public int getNbBuildingsPerPlayer() 
	{
		return this.nbBuildingsPerPlayer;
	}

	/**
	 * @return the amount of player card to fill up your hand
	 */
	public int getAmountOfPlayerCardToFillYourHand()
	{
		return this.nbInitCards;
	}

	/**
	 * @return the canPlayPlayerCard
	 */
	public boolean isCanPlayPlayerCard() 
	{
		return this.canPlayPlayerCard;
	}
	
	/**
	 * Retrieve the next top player card from board
	 * @return the deck of player cards
	 */
	public Card getNextPlayerCard()
	{
		Deck<Card> playerCards = board.getPlayerCardDeck();
		if (playerCards.isEmpty())
		{
			systemDataInput.printMessage("There is no player card to play the game.");
			endGame(EndGameType.noPlayerCard);
			return null;
		}
		else
		{
			Card topCard = playerCards.pickTopCard();
			if (playerCards.size() == 1)
			{
				systemDataInput.printMessage("The last player card " + topCard.getName() + " has been picked up. This is the end of the game.");
				endGame(EndGameType.noPlayerCard);
			}
			
			return topCard;
		}
	}
	
	/**
	 * @return the isEndGame
	 */
	public boolean isEndGame() 
	{
		return this.isEndGame;
	}
	
	/**
	 * @return the winnerList
	 */
	public List<Player> getWinnerList() 
	{
		return this.winnerList;
	}
}
