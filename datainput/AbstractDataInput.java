package ankhmorpork.datainput;

import java.util.*;

/**
 * Ankh-Morpork DataInput base class
 * @author Team 2
 * @since Build 2
 */
public abstract class AbstractDataInput 
{
	/**
	 * Method that initialize the game play
	 * Ask the amount of players
	 */
	public void playGame()
	{
		
	}

	public void printMessage(String string)
	{
		
	}
	
	public void showError(String string)
	{
		
	}
	
	public <T> T ask(String question, List<T> listObject)
	{ 
		return null; 
	}

	public void initializeGame() 
	{
		
	}

	public <T> T askPlayAction(List<T> listObject) 
	{
		return null;		
	}
	
	public boolean askTrueFalseQuestion(String message, String trueOption, String falseOption)
	{
		return false;
	}
	
	public int askNumberQuestion(String question, int min, int max)
	{
		return 0;
	}

	/**
	 * @return text from keyboard
	 */
	public String getStringFromKeyboard(String message)
	{
		return "";
	}
}
