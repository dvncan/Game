package ankhmorpork.model;

import java.util.List;

import ankhmorpork.manager.GameManager;
import ankhmorpork.util.Definition;
import ankhmorpork.util.Environment;

import java.util.ArrayList;

/**
 * Action in the game that are contained inside a card
 * @see Card
 * @author Team 2
 * @since Build 2
 */
public abstract class Action 
{
	protected int NUMBER_OF_ARGUMENTS;
		
	public enum StopTarget
	{
		RandomEventLandmark,
		MoveMinion,
		RemoveMinion,
		Text,
		Turn
	}
	
	protected List<StopTarget> stopTargets = new ArrayList<StopTarget>();
	public abstract boolean execute(GameManager gameManager);
	
	/**
	 * Get the argument at given index
	 * @param args
	 * @param index
	 * @return 
	 */
	protected int getArgument(List<String> args, int index) throws Exception
	{
		if (args == null || args.size() < index + 1)
		{
			throw new Exception("Missing Argument");
		}
		return Environment.parseIntWithoutError(args.get(index));
	}
	
	/**
	 * Verify if the amount of arguments match
	 * @param args
	 * @param nbOfArguments
	 * @throws Exception
	 */
	protected void verifyArgumentCount(List<String> args, int nbOfArguments) throws Exception
	{
		if(args == null && nbOfArguments == 0)
		{
			return;
		}
		
		if(args.size() != nbOfArguments)
		{
			throw new Exception(String.format(Definition.ERR_INVALID_NUMBER_ARGS_D2, args.size(), nbOfArguments));
		}
	}

	/**
	 * Inform the card if it can be executed
	 * @param gameManager to verify the logic
	 * @return if it can be executed
	 */
	public boolean canExecute(GameManager gameManager) 
	{
		return true;
	}
	
	/**
	 * Check if this action stops the target
	 * @param target stop target
	 * @return true or false the target is stopped
	 */
	public boolean doesStop(StopTarget target)
	{
		for(StopTarget t : this.stopTargets)
		{
			if (t == target)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a stop target for this action
	 * @param target stop card target
	 */
	public void addStopTarget(StopTarget target)
	{
		this.stopTargets.add(target);
	}
	
	/**
	 * @return card must be played
	 */
	public boolean isOptional()
	{
		return false;
	}
	
	/**
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed)
	{
		return playerCardPlayed;
	}
	
	/**
	 * @return the name of the action class
	 */
	public String getClassName()
	{
		String genericName = getClass().getName();
		int pos = genericName.lastIndexOf(".") + 1;
		if(pos > 1)
		{
			genericName = genericName.substring(pos);
		}
		return genericName;
	}
}
