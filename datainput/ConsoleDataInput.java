package ankhmorpork.datainput;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Color;
import ankhmorpork.model.ObjectDecorator;
import ankhmorpork.model.Player;
import ankhmorpork.util.Environment;

/**
 * Ankh-Morpork DataInput Console class
 * @author Team 2
 * @since Build 2
 */
public class ConsoleDataInput extends AbstractDataInput
{
	private final AMScanner input;
	private final Player player;
	
	/**
	 * Constructor that receive a Scanner for keyboard input
	 * @param player the player for whom the data input is for
	 * @param isAI if the data input act as an AI
	 */
	public ConsoleDataInput(Player player, boolean isAI)
	{
		this.input = isAI ? new AMScannerAI(): new AMScanner();
		this.player = player;
	}
	
	/**
	 * Change the current selected player
	 * @param defaultValue to assign if the input value is invalid
	 * @return a player number
	 */
	public int getNewCurrentPlayer(int defaultValue, int playerListSize)
	{
		int currentPlayer = 0;
		println("Select a player [1-"+playerListSize+"]");
		String option = input.next();
		try
		{
			currentPlayer = Integer.parseInt(option);
		} catch (Exception e) { }
		
		if(currentPlayer < 1 || currentPlayer > playerListSize)
		{
			currentPlayer = defaultValue;
		}
		return currentPlayer;
	}
	
	/**
	 * Asking the current play what he want to play
	 */
	public <T> T askPlayAction(List<T> listObject) 
	{
		int currentPlayerID = player.getId();
		
		if (listObject != null && !listObject.isEmpty())
		{
			println("Current Player : " + currentPlayerID);
			while(true)
			{
				int count = 1;
				for(T object : listObject)
				{
					println(count + ". " + object.toString());
					count++;
				}

				String option = input.next();

				int index = Environment.parseIntWithoutError(option) - 1;
				if (index >= 0 && index < listObject.size())
				{
					return listObject.get(index);
				}
			}
		}
		return null;
	}
	
	/**
	 * Initialize Players
	 * @throws Exception 
	 */
	public void initializePlayers(GameManager gameManager) throws Exception
	{
		int maxPlayerNumber = gameManager.getMaxAmountPlayers();
		List<Color> availableColors = gameManager.getAvailableColorList();
		
		List<String> availableAINames = Environment.getFamousNames();		
		if (availableColors == null || availableColors.size() < 1)
		{
			return;
		}
		
		int playerNumber = 0;
		Player player = null;
		int option = 0;

		// Add the main player
		player = addPlayer(availableColors, playerNumber, maxPlayerNumber);
		if(player == null)
		{
			throw new Exception("Not Enough Players!");
		}
		gameManager.addPlayer(player);
		playerNumber++;
		
		List<Object> optionList = new ArrayList<Object>();
		optionList.add("Add another player");
		optionList.add("Add a computer");

		option = requestInputAction("Please select an action:", optionList);
		if(option == 0)
		{
			player = addPlayer(availableColors, playerNumber, maxPlayerNumber);
		}
		else
		{
			player = addAComputer(availableColors, availableAINames, playerNumber);
		}
		if(player == null)
		{
			throw new Exception("Not Enough Players!");
		}
		gameManager.addPlayer(player);
		playerNumber++;
		
		optionList.add("Start game");	
		boolean startGame = false;
		while(playerNumber < maxPlayerNumber && !startGame)
		{
			// Prompt for user choice
			option = requestInputAction("Please select an action:", optionList);					
			switch(option)
			{
				case 0:
				{
					// Add another player
					player = addPlayer(availableColors, playerNumber, maxPlayerNumber);
					if(player != null)
					{
						gameManager.addPlayer(player);
						playerNumber++;
					}
					break;
				}
				case 1:
				{
					// Add computers
					player = addAComputer(availableColors, availableAINames, playerNumber);
					if(player != null)
					{
						gameManager.addPlayer(player);
						playerNumber++;
					}
					break;
				}
				case 2:
				{
					startGame = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Ask to choose an option from the list
	 * @param title to show before the list of options
	 * @param optionList list to show as options
	 * @return the index chosen from the list
	 */
	private int requestInputAction(String title, List<Object> optionList)
	{
		int option = 0;
		boolean validInput = false;
		
		while(!validInput)
		{
			validInput = true;
			
			// Prompt for user choice
			println(title);
			for(int i = 0; i < optionList.size(); i++)
			{
				println(String.format("%d. %s", i + 1, optionList.get(i)));
			}
			
			option = Environment.parseIntWithoutError(input.next());
			
			if(option < 1 || option > optionList.size())
			{
				validInput = false;
				println("This is not a valid option.");
			}
		}
		
		// Returning the index of the list instead of the value inputed from the keyboard
		return option - 1;
	}
	
	/**
	 * Add a single computer player
	 * @param availableColors
	 * @param playerNumber
	 * @param maxPlayerNumber
	 * @return
	 * @throws Exception 
	 */
	private Player addAComputer(List<Color> availableColors, List<String> availableNames, int playerNumber) throws Exception 
	{
		int colorIndex = Environment.randInt(0, availableColors.size() - 1);
		int nameIndex = Environment.randInt(0, availableNames.size() - 1);
		
		playerNumber++;
		
		Color chosenColor = availableColors.get(colorIndex);
		String chosenName = availableNames.get(nameIndex);
		
		Player newPlayer = new Player(playerNumber, chosenName, chosenColor, true);
		
		availableColors.remove(colorIndex);
		availableNames.remove(nameIndex);
		
		println(chosenColor.getName() + " computer " + chosenName + " has joined the game.");
		
		return newPlayer;
	}
	
	/**
	 * Add Player to the game
	 * @param availableColors of type List<Color>
	 * @param playerNumber Integer
	 * @param maxPlayerNumber Integer
	 * @throws Exception 
	 */
	private Player addPlayer(List<Color> availableColors, int playerNumber, int maxPlayerNumber) throws Exception
	{
		Player newPlayer = null;
		// Ask for user input
		println(String.format("There is currently %d player%s out of %d.", playerNumber, playerNumber > 1 ? "s" : "", maxPlayerNumber));

		// Get new player name
		println("Please enter a new player's name:");
		String name = input.next();

		// Only ask for player color if there can be more than one player added
		Color color = this.ask("Please choose the player's color:", availableColors);
		boolean isAS = this.askTrueFalseQuestion("Are you an AI?", "Yes", "No");
		if (availableColors.remove(color))
		{
			newPlayer = new Player(playerNumber + 1, name, color, isAS);
		}
		
		return newPlayer;
	}
	
	/**
	 * Show error message in UI
	 * @param errorMsg the error message
	 */
	@Override
	public void showError(String errorMsg) 
	{
		println("Error: " + errorMsg);
	}
	
	/**
	 * Show  message in UI
	 * @param msg
	 */
	@Override
	public void printMessage(String msg) 
	{
		println(msg);
	}
	
	/**
	 * Ask the player to select from a list of objects
	 * @param <T>
	 * @param question
	 * @param listObject
	 * @return 
	 */
	@Override
	public <T> T ask(String question, List<T> listObject)
	{
		if (listObject != null && !listObject.isEmpty())
		{
			while(true)
			{
				if(!question.isEmpty())
				{
					println(question);
				}
				int count = 1;
				for(T object : listObject)
				{
					println(count + ". " + object.toString());
					count++;
				}

				String option = input.next();
				int index = Environment.parseIntWithoutError(option) - 1;
				if (index >= 0 && index < listObject.size())
				{
					return listObject.get(index);
				}
			}
		}
		return null;
	}

	/**
	 * Print play
	 */
	@Override
	public void playGame()
	{
		println("Play!");
	}

	/**
	 * Print a message including prefix of player destination
	 * @param message
	 */
	private void println(String message) 
	{
		String playerName = player == null ? "" : "(" + player.getColor() + ") " +  player.getName() + ": ";
		System.out.println(playerName + message);
	}

	/**
	 * Ask a True or False question
	 * @param message
	 * @param trueOption
	 * @param falseOption
	 * @return 
	 */
	@Override
	public boolean askTrueFalseQuestion(String message, String trueOption, String falseOption)
	{
		List<ObjectDecorator<Boolean>> questionList = new ArrayList<ObjectDecorator<Boolean>>();
		questionList.add(new ObjectDecorator<Boolean>(true, trueOption));
		questionList.add(new ObjectDecorator<Boolean>(false, falseOption));
		if(player == null)
		{
			return ask(message, questionList).getObject();
		}
		else
		{
			return player.getDataInput().ask(message, questionList).getObject();
		}
	}
	
	/**
	 * Ask a question that returns a number between min and max (inclusively)
	 * @param question
	 * @param min
	 * @param max
	 * @return 
	 */
	@Override
	public int askNumberQuestion(String question, int min, int max)
	{
		while(true)
		{
			if(!question.isEmpty())
			{
				println(question);
			}
			
			String number = input.next();
			if (Environment.isInteger(number))
			{
				int numberConverted = Environment.parseIntWithoutError(number);
				if (numberConverted >= min && numberConverted <= max)
				{
					return Environment.parseIntWithoutError(number);
				}
			}
		}
	}

	/**
	 * @return the input scanner
	 */
	public AMScanner getInput() 
	{
		return this.input;
	}

	/**
	 * @return text from keyboard
	 */
	public String getStringFromKeyboard(String message) 
	{
		if(!message.isEmpty())
		{
			println(message);
		}
		return input.next();
	}
}
