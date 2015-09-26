package ankhmorpork.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import ankhmorpork.datainput.AbstractDataInput;
import ankhmorpork.datainput.ConsoleDataInput;

/**
 * Regular multisided die with consecutive number from 1 to n where n equals to the number of sides of the die
 * @author Team 2
 * @since Build 1
 */
public class Die
{
	private int side;
	private final Queue<Integer> predefinedAnswer = new LinkedList<Integer>();
	private boolean allowRandomValue = true;
	private AbstractDataInput systemDataInput;
	
	/**
	 * Create a die with the given number of sides
	 * @param side the amount of sides to the die
	 */
	public Die(int side)
	{
		this.side = side;
		this.systemDataInput = new ConsoleDataInput(null, false);
	}

	/**
	 * Find the number of sides of the die
	 * @return the number of sides of the die
	 */
	public int getSide()
	{
		return this.side;
	}
	
	/**
	 * Cast die a given number of time and return the result
	 * The value of the sides goes from 1 to n, where n is the total number of sides
	 * @param times The number of times the die is cast
	 * @return The cumulative value of all casts
	 */
	public int castDie(int times)
	{
		// The die should be cast at least 1 time, return 0 if that is not the case
		if (times < 1)
		{
			return 0;
		}
		
		int result = 0;
		Random randomNumberGenerator = new Random();
		
		// Generate random cast result a given number of times
		for(int i = 0; i < times; i++) 
		{
			int value;
			if(!predefinedAnswer.isEmpty())
			{
				value = predefinedAnswer.remove();
			}
			else
			{
				if(allowRandomValue)
				{
					// result = generated integer + 1 because nextInt gives value from 0 (inclusive) to n (exclusive)
					value = randomNumberGenerator.nextInt(this.side) + 1;
				}
				else
				{
					throw new RuntimeException("RandomValueNotAllowed!");
				}
			}
			result += value;
			systemDataInput.printMessage("Dice Roll... " + value + "!");
		}
		
		return result;
	}
	
	/**
	 * Add an item to the queue
	 * @param value
	 */
	public void addPredefinedAnswer(int value)
	{
		predefinedAnswer.add(value);
	}
	
	/**
	 * Add a collection to the queue
	 * @param valueList
	 */
	public void addPredefinedAnswers(Collection<Integer> valueList)
	{
		predefinedAnswer.addAll(valueList);
	}

	/**
	 * @return the allowRandomValue
	 */
	public boolean isAllowRandomValue() 
	{
		return allowRandomValue;
	}

	/**
	 * @param allowRandomValue the allowRandomValue to set
	 */
	public void setAllowRandomValue(boolean allowRandomValue) 
	{
		this.allowRandomValue = allowRandomValue;
	}
}
