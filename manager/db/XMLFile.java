package ankhmorpork.manager.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import ankhmorpork.manager.AbstractFileManager;
import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Board;
import ankhmorpork.model.Card;
import ankhmorpork.model.Color;
import ankhmorpork.collection.Deck;
import ankhmorpork.model.Player;
import ankhmorpork.model.landmark.Building;
import ankhmorpork.model.landmark.Minion;
import ankhmorpork.util.Environment;

/**
 * XML file save/load handler
 * @author Team 2
 * @since Build 1
 */
public class XMLFile extends AbstractFileManager
{
	private final String ANKH_MORPORK_TAG = "AnkhMorpork";
	private final String PLAYER_LIST_TAG = "PlayerList";
	private final String PLAYER_TAG = "Player";	
	private final String BOARD_TAG = "Board";
	private final String MINION_LIST_TAG = "MinionList";
	private final String MINION_TAG = "Minion";
	private final String AREA_LIST_TAG = "AreaList";
	private final String AREA_TAG = "Area";	
	private final String BUILDING_TAG = "Building";
	private final String CITY_AREA_CARD_DECK_TAG = "CityAreaCardDeck";
	private final String RANDOM_EVENT_CARD_DECK_TAG = "RandomEventCardDeck";
	private final String PLAYER_CARD_DECK_TAG = "PlayerCardDeck";
	private final String CARD_TAG = "Card";

	private final String COLOR_ATTR = "color";
	private final String ID_ATTR = "id";
	private final String HAS_AREA_CARD_ATTR = "has_area_card";
	private final String HAS_TROUBLE_ATTR = "has_trouble";
	private final String TROLL_ATTR = "troll";
	private final String DEMON_ATTR = "demon";
	private final String NAME_ATTR = "name";
	private final String MONEY_ATTR = "money";
	private final String MINION_ATTR = "minion";
	private final String BUILDING_ATTR = "building";
	private final String PERSONALITY_ATTR = "personality";
	private final String TURN_NUMBER_ATTR = "turn_number";
	private final String CURRENT_PLAYER_ID_ATTR = "current_player_id";
	
	protected String path;
	
	/**
	* Constructor for the file managing system
	* @param path Path to the file to load
	*/
	public XMLFile(String path)
	{
		this.path = path;
	}

	/**
	 * Save the game to an XML file
	 * @param gameManager to be serialized in xml
	 */
	@Override
	public void save(GameManager gameManager) throws Exception
	{
		// Create the XML document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();

		// create the root element node
		Element element = doc.createElement(ANKH_MORPORK_TAG);
		doc.appendChild(element);

		// Save attributes
		element.setAttribute(TURN_NUMBER_ATTR, Integer.toString(gameManager.getTurnNumber()));
		element.setAttribute(CURRENT_PLAYER_ID_ATTR, Integer.toString(gameManager.getCurrentPlayerID()));
		
		// Save the list of players and board state
		saveListPlayer(doc, element, gameManager.getPlayerList(), gameManager);
		saveBoard(doc, element, gameManager.getBoard());

		// Serialize everything into a file
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Result output = new StreamResult(new File(this.path));
		Source input = new DOMSource(doc);
		tf.transform(input, output);
	}
	
	/**
	* File load
	 * @throws Exception load exceptions
	*/
	@Override
	public GameManager load() throws Exception
	{
		GameManager gameManager = GameManager.createGameManager(Environment.CONFIGURATION_PATH);

		// Load the XML document
		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// Selecting the AnkhMorpork first root element
		Element element = doc.getDocumentElement();
		if(element != null && element.getNodeName().equals(ANKH_MORPORK_TAG))
		{
			// Load attributes
			gameManager.setTurnNumber(Integer.parseInt(element.getAttribute(TURN_NUMBER_ATTR)));
			gameManager.setCurrentPlayerID(Integer.parseInt(element.getAttribute(CURRENT_PLAYER_ID_ATTR)));
			
	    NodeList nodeList = element.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++)
	    {
	      Node currentNode = nodeList.item(i);
	      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
	      {
	      	switch(currentNode.getNodeName())
	      	{
	      	case PLAYER_LIST_TAG :
	      		loadListPlayer((Element)currentNode, gameManager);
	      		break;   
	      	case BOARD_TAG :
	      		loadBoard((Element)currentNode, gameManager);
	      		break;        		
	      	}
	      }
	    }
		}
		
		return gameManager;
	}

	/**
	 * Save the list of players
	 * @param doc the DOM document
	 * @param node the current node
	 * @param listPlayer the list of player to save
	 * @param gameManager the game manager
	 */
	protected void saveListPlayer(Document doc, Node node, List<Player> listPlayer, GameManager gameManager)
	{
		// create the root element node
		Element element = doc.createElement(PLAYER_LIST_TAG);
		node.appendChild(element);

		for(Player player : listPlayer)
		{
			savePlayer(doc, element, player, gameManager);
		}
	}

	/**
	 * Load a list of player
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @throws Exception load exceptions
	 */
	protected void loadListPlayer(Element element, GameManager gameManager) throws Exception
	{
    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case PLAYER_TAG :
      		loadPlayer((Element)currentNode, gameManager);
      		break;     		
      	}
      }
    }
	}
	
	/**
	 * Saving a player object
	 * @param doc the DOM document
	 * @param node the current node
	 * @param player to be serialized
	 * @param gameManager to access the list of areas
	 */
	protected void savePlayer(Document doc, Node node, Player player, GameManager gameManager)
	{
		// create the root element node
		Element element = doc.createElement(PLAYER_TAG);
		node.appendChild(element);

		int minionCount = player.getMinionCount();
		int buildingCount = player.getBuildingCount();
		for(Area area : gameManager.getBoard().getAreaList())
		{
			for(Minion minion : area.getMinionList())
			{
				if(minion.isOwnedBy(player))
				{
					minionCount++;
				}
			}
			if(area.hasBuilding() && area.getBuilding().isOwnedBy(player))
			{
				buildingCount++;
			}
		}
		
		element.setAttribute(ID_ATTR, Integer.toString(player.getId()));
		element.setAttribute(NAME_ATTR, player.getName());
		element.setAttribute(COLOR_ATTR, player.getColor().getName());
		element.setAttribute(MINION_ATTR, Integer.toString(minionCount));
		element.setAttribute(BUILDING_ATTR, Integer.toString(buildingCount));
		element.setAttribute(PERSONALITY_ATTR, Integer.toString(player.getPersonalityCard().getId()));	
		element.setAttribute(MONEY_ATTR, Integer.toString(player.getMoneyAmount()));
		saveCardDeck(doc, element, player.getCityAreaCardDeck(), CITY_AREA_CARD_DECK_TAG);
		saveCardDeck(doc, element, player.getPlayerCardDeck(), PLAYER_CARD_DECK_TAG);
	}

	/**
	 * Load Player
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @throws Exception load exceptions
	 */
	protected void loadPlayer(Element element, GameManager gameManager) throws Exception
	{
		int playerID = Integer.parseInt(element.getAttribute(ID_ATTR));
		String playerName = element.getAttribute(NAME_ATTR);
		String colorName = element.getAttribute(COLOR_ATTR);
		int nbMinion = Integer.parseInt(element.getAttribute(MINION_ATTR));
		int nbBuilding = Integer.parseInt(element.getAttribute(BUILDING_ATTR));
		int personalityCardID = Integer.parseInt(element.getAttribute(PERSONALITY_ATTR));
		int moneyAmount = Integer.parseInt(element.getAttribute(MONEY_ATTR));
		
		Color color = Color.getColor(colorName);
		Player player = new Player(playerID, playerName, color, false);
		if(!gameManager.getBoard().getBankAccount().transfertAmountTo(player, moneyAmount, true))
		{
			throw new Exception("No money left in the bank!");
		}	
		
		// Distribute minions
		for(int i = 0; i < nbMinion; i++)
		{
			player.addMinion(new Minion());
		}
		
		// Distribute buildings
		for(int i = 0; i < nbBuilding; i++)
		{
			player.addBuilding(new Building());
		}

		Card card = gameManager.getBoard().getPersonalityCardDeck().pickCardByID(personalityCardID);
		if(card == null)
		{
			throw new Exception("Invalid Card");
		}
		player.setPersonalityCard(card);

    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case CITY_AREA_CARD_DECK_TAG :
	      	{
	      		List<Card> listCard = loadCardDeck((Element)currentNode, gameManager);
	      		player.getCityAreaCardDeck().addAll(listCard);
	      		break;     	
	      	}
      	case PLAYER_CARD_DECK_TAG :
	      	{
	      		List<Card> listCard = loadCardDeck((Element)currentNode, gameManager);
	      		player.getPlayerCardDeck().addAll(listCard);
	      		break;     		
	      	}
      	}
      }
    }
		gameManager.addPlayer(player);
	}

	/**
	* Save city area cards
	 * @param doc the DOM document
	 * @param node the current node
	 * @param deck to be serialized
	 */
	protected void saveCardDeck(Document doc, Node node, Deck<Card> deck, String typeOfDeck)
	{
		// create the root element node
		Element element = doc.createElement(typeOfDeck);
		node.appendChild(element);

		for(Card card : deck)
		{
			saveCard(doc, element, card, CARD_TAG);
		}
	}
	
	/**
	 * Load a deck of card
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @return the list of loaded city area cards
	 * @throws Exception load exception
	 */
	protected List<Card> loadCardDeck(Element element, GameManager gameManager) throws Exception
	{
		List<Card> listCard = new ArrayList<Card>();
    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case CARD_TAG :
      		listCard.add((Card)loadCard((Element)currentNode, gameManager));
      		break;     		
      	}
      }
    }
    return listCard;
	}

	/**
	* Save cards
	 * @param doc the DOM document
	 * @param node the current node
	 * @param card to be serialized
	 * @param type of card to be serialized
	 */
	protected void saveCard(Document doc, Node node, Card card, String type)
	{
		// create the root element node
		Element element = doc.createElement(type);
		node.appendChild(element);

		element.setAttribute(ID_ATTR, Integer.toString(card.getId()));
	}
	
	/**
	 * Load cards
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @return the list of loaded cards
	 * @throws Exception load exception
	 */
	protected Card loadCard(Element element, GameManager gameManager) throws Exception
	{
		int cardID = Integer.parseInt(element.getAttribute(ID_ATTR));
		Card card = gameManager.getBoard().getPlayerCardDeck().pickCardByID(cardID);
		
		if(card == null)
		{
			card = gameManager.getBoard().getRandomEventCardDeck().pickCardByID(cardID);
		}

		if(card == null)
		{
			card = gameManager.getBoard().getPersonalityCardDeck().pickCardByID(cardID);
		}
		
		if(card == null)
		{
			for(Area area : gameManager.getBoard().getAreaList())
			{
				if(area.getAreaCard().getId() == cardID)
				{
					card = area.getAreaCard();
					break;
				}
			}
		}
		
		if(card == null)
		{
			throw new Exception("Invalid Card");
		}
		
		return card;
	}

	/**
	* Save board
	 * @param doc the DOM document
	 * @param node the current node
	 * @param board to be serialized
	 */
	protected void saveBoard(Document doc, Node node, Board board)
	{
		// create the root element node
		Element element = doc.createElement(BOARD_TAG);
		node.appendChild(element);

		element.setAttribute(TROLL_ATTR, Integer.toString(board.getTrollList().size()));
		element.setAttribute(DEMON_ATTR, Integer.toString(board.getDemonList().size()));
		saveCardDeck(doc, element, board.getPlayerCardDeck(), PLAYER_CARD_DECK_TAG);
		saveCardDeck(doc, element, board.getRandomEventCardDeck(), RANDOM_EVENT_CARD_DECK_TAG);
		saveAreaList(doc, element, board.getAreaList());
	}	

	/**
	 * Load board
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @throws Exception load exception
	 */
	protected void loadBoard(Element element, GameManager gameManager) throws Exception
	{
    NodeList nodeList = element.getChildNodes();
    List<Card> randomEventCardList = new ArrayList<Card>();
    List<Card> playerCardList = new ArrayList<Card>();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case RANDOM_EVENT_CARD_DECK_TAG :
      		randomEventCardList.addAll(loadCardDeck((Element)currentNode, gameManager));
      		break;
      	case PLAYER_CARD_DECK_TAG :
      		playerCardList.addAll(loadCardDeck((Element)currentNode, gameManager));
      		break;
      	case AREA_LIST_TAG :
      		loadAreaList((Element)currentNode, gameManager);
      		break;
      	}
      }
    }
    
    // Can verify that the board is in a valid state based on those lists of cards
    gameManager.getBoard().getDiscardDeck().addAll(gameManager.getBoard().getPlayerCardDeck());
    gameManager.getBoard().getDiscardDeck().addAll(gameManager.getBoard().getRandomEventCardDeck());
    gameManager.getBoard().getPlayerCardDeck().clear();
    gameManager.getBoard().getRandomEventCardDeck().clear();

    for(Card card : playerCardList)
    {
    	gameManager.getBoard().getPlayerCardDeck().addCard(card);
    }
    
    for(Card card : randomEventCardList)
    {
    	gameManager.getBoard().getRandomEventCardDeck().addCard(card);
    }
	}

	/**
	* Save area list
	 * @param doc the DOM document
	 * @param node the current node
	 * @param listArea to be serialized
	 */
	protected void saveAreaList(Document doc, Node node, List<Area> listArea)
	{
		// create the root element node
		Element element = doc.createElement(AREA_LIST_TAG);
		node.appendChild(element);

		for(Area area : listArea)
		{
			saveArea(doc, element, area);
		}		
	}	

	/**
	 * Load area list
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @throws Exception load exception
	 */
	protected void loadAreaList(Element element, GameManager gameManager) throws Exception
	{
    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case AREA_TAG :
    			loadArea((Element)currentNode, gameManager);
      		break;     		
      	}
      }
    }
	}	

	/**
	* Save area
	 * @param doc the DOM document
	 * @param node the current node
	 * @param area to be serialized
	 */
	protected void saveArea(Document doc, Node node, Area area)
	{
		// create the root element node
		Element element = doc.createElement(AREA_TAG);
		node.appendChild(element);

		element.setAttribute(ID_ATTR, Integer.toString(area.getNumber()));
		element.setAttribute(HAS_AREA_CARD_ATTR, Boolean.toString(area.getAreaCard() != null));
		element.setAttribute(HAS_TROUBLE_ATTR, Boolean.toString(area.hasTrouble()));
		element.setAttribute(TROLL_ATTR, Integer.toString(area.getNbTroll()));
		element.setAttribute(DEMON_ATTR, Integer.toString(area.getNbDemon()));
		if(area.hasBuilding())
		{
			saveBuilding(doc, element, area.getBuilding());
		}
		saveMinionList(doc, element, area.getMinionList());
	}	

	/**
	 * Load area
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @throws Exception load exception
	 */
	protected void loadArea(Element element, GameManager gameManager) throws Exception
	{
		int id = Integer.parseInt(element.getAttribute(ID_ATTR));
		// Area card depend, a player could of pick it up in the load of player depending on loading order
		//boolean hasAreaCard = Boolean.parseBoolean(element.getAttribute(HAS_AREA_CARD_ATTR));
		boolean hasTrouble = Boolean.parseBoolean(element.getAttribute(HAS_TROUBLE_ATTR));
		int nbTroll = Integer.parseInt(element.getAttribute(TROLL_ATTR));
		int nbDemon = Integer.parseInt(element.getAttribute(DEMON_ATTR));
		
		Area area = gameManager.getBoard().getAreaList().get(id-1);
		area.setTroubleMarker(hasTrouble);

		if(gameManager.getBoard().getTrollList().size() < nbTroll)
		{
			throw new Exception("Not enought troll");
		}
		else
		{
			for(int i = 0; i < nbTroll; i++)
			{
				area.addTroll(gameManager.getBoard().removeTroll());
			}
		}
		
		if(gameManager.getBoard().getDemonList().size() < nbDemon)
		{
			throw new Exception("Not enought demon");
		}
		else
		{
			for(int i = 0; i < nbDemon; i++)
			{
				area.addDemon(gameManager.getBoard().removeDemon());
			}
		}
		
    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case BUILDING_TAG :
      		area.setBuilding(loadBuilding((Element)currentNode, gameManager));
      		break;     		
      	case MINION_LIST_TAG :
      		area.getMinionList().addAll(loadMinionList((Element)currentNode, gameManager));
      		break;
      	}
      }
    }
	}
	
	/**
	* Save building
	 * @param doc the DOM document
	 * @param node the current node
	 * @param building to be serialized
	 */
	protected void saveBuilding(Document doc, Node node, Building building)
	{
		// create the root element node
		Element element = doc.createElement(BUILDING_TAG);
		node.appendChild(element);

		element.setAttribute(COLOR_ATTR, building.getColor().getName());
	}	

	/**
	 * Load building
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @return the loaded building
	 * @throws Exception load exception
	 */
	protected Building loadBuilding(Element element, GameManager gameManager) throws Exception
	{
		Building building = null;
		String colorName = element.getAttribute(COLOR_ATTR);
		Color color = Color.getColor(colorName);
		for(Player player : gameManager.getPlayerList())
		{
			if(player.getColor() == color)
			{
				building = player.removeBuilding();
				if(building == null)
				{
					throw new Exception("No more building left");
				}
				break;
			}
		}
		return building;
	}
	
	/**
	* Save minion list
	 * @param doc the DOM document
	 * @param node the current node
	 * @param listMinion to be serialized
	 */
	protected void saveMinionList(Document doc, Node node, List<Minion> listMinion)
	{
		// create the root element node
		Element element = doc.createElement(MINION_LIST_TAG);
		node.appendChild(element);

		for(Minion minion : listMinion)
		{
			saveMinion(doc, element, minion);
		}
	}	

	/**
	 * Load minion list
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @return the list of loaded minions
	 * @throws Exception load exception
	 */
	protected List<Minion> loadMinionList(Element element, GameManager gameManager) throws Exception
	{
		List<Minion> minionList = new ArrayList<Minion>();
    NodeList nodeList = element.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
      {
      	switch(currentNode.getNodeName())
      	{
      	case MINION_TAG :
      		minionList.add(loadMinion((Element)currentNode, gameManager));
      		break;     		
      	}
      }
    }
    return minionList;
	}
	
	/**
	* Save minion
	 * @param doc the DOM document
	 * @param node the current node
	 * @param minion to be serialized
	 */
	protected void saveMinion(Document doc, Node node, Minion minion)
	{
		// create the root element node
		Element element = doc.createElement(MINION_TAG);
		node.appendChild(element);

		element.setAttribute(COLOR_ATTR, minion.getColor().getName());
	}	

	/**
	 * Load minion
	 * @param element to be deserialized
	 * @param gameManager to load the data into
	 * @return the loaded minion
	 * @throws Exception load exception
	 */
	protected Minion loadMinion(Element element, GameManager gameManager) throws Exception
	{
		Minion minion = null;
		String colorName = element.getAttribute(COLOR_ATTR);
		Color color = Color.getColor(colorName);
		for(Player player : gameManager.getPlayerList())
		{
			if(player.getColor() == color)
			{
				minion = player.removeMinion();
				if(minion == null)
				{
					throw new Exception("No more minion left");
				}
				break;
			}
		}
		return minion;
	}
}
