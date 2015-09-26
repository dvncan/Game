package ankhmorpork.main;

import java.util.Scanner;

import ankhmorpork.manager.GameManager;
import ankhmorpork.util.Environment;

/**
 * Ankh-Morpork game
 * @author Team 2
 * @since Build 1
 */
public class Main {
	
	/**
	 * Main Thread to start the application
	 * @param args list of argument to the main application
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		Scanner input = new Scanner(System.in);
		GameManager gameManager = null;
		System.out.println("Welcome to Ankh-Morpork!");
		
		String option = "";
		boolean doQuit = true;
		
		do
		{
			// Ask for user input
			System.out.println("Please select an action:");
			System.out.println("N- New game");
			System.out.println("L- Load game");
			System.out.println("Q- Quit");
			option = input.next();
			
			// By default quit this menu after doing an action
			doQuit = true;
			
			// Check the user input
			switch(option.toUpperCase())
			{
				case "N":
				{
					// Create new game
					gameManager = GameManager.createGameManager(Environment.CONFIGURATION_PATH);
					gameManager.initializeGame();					
					System.out.println(gameManager.getEntireGameStatus());
					break;
				}
				case "L":
				{
					// Load existing game
					System.out.println("Write the name of file to load without extension.");
					String path = input.next();
					gameManager = GameManager.loadGame(path);
					break;
				}
				case "Q":
				{
					// Quit game
					// Do nothing
					break;
				}
				default:
				{
					// Warn that the option is invalid and set the condition to ask for user input again
					System.out.println("This is not a valid option.");
					doQuit = false;
					break;
				}
			}
		}
		while(!doQuit);
		
		if(gameManager != null)
		{
			gameManager.playGame();
		}
		
		input.close();
		System.exit(0);
	}
}
