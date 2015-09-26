package ankhmorpork.datainput;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Ankh-Morpork Keyboard Scanner Adapter
 * This can be used for testing purposes or execute action with predefined answers
 * @author Pascal
 * @since Build 2
 */
public class AMScanner
{
	private final Scanner input;
	private final Queue<String> predefinedAnswer = new LinkedList<String>();
	private boolean allowKeyboardInput = true;
	
	/**
	 * Constructor
	 */
	public AMScanner()
	{
		this.input = new Scanner(System.in);
	}
	
	/**
	 * @return predefined answer or a value from the keyboard
	 */
	public String next()
	{
		String answer;
		if(predefinedAnswer.isEmpty())
		{
			if(this.allowKeyboardInput)
			{
				answer = this.input.next();
			}
			else
			{
				throw new RuntimeException("Scanner Answer hasn't been added!");
			}
		}
		else
		{
			answer = predefinedAnswer.remove();
		}
		return answer;
	}
	
	/**
	 * Add an item to the queue
	 * @param value
	 */
	public void addPredefinedAnswer(String value)
	{
		predefinedAnswer.add(value);
	}
	
	/**
	 * Add a collection to the queue
	 * @param valueList
	 */
	public void addPredefinedAnswers(Collection<String> valueList)
	{
		predefinedAnswer.addAll(valueList);
	}

	/**
	 * @return the allowKeyboardInput
	 */
	public boolean isAllowKeyboardInput() 
	{
		return allowKeyboardInput;
	}

	/**
	 * @param allowKeyboardInput the allowKeyboardInput to set
	 */
	public void setAllowKeyboardInput(boolean allowKeyboardInput) 
	{
		this.allowKeyboardInput = allowKeyboardInput;
	}
}
