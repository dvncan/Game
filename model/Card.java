package ankhmorpork.model;

import java.util.Collections;
import java.util.List;

import ankhmorpork.manager.GameManager;
import java.util.ArrayList;

/**
 * Card piece with an ID and a name
 * @author Team 2
 * @since Build 1
 */
public class Card
{
	public enum CardType
	{
		GreenPlayerCard,
		BrownPlayerCard,
		CityAreaCard,
		RandomEventCard,
		PersonalityCard
	}
	
	protected final int id;     
	protected final CardType type;
	protected final String name;
	protected final List<Action> actions;
	private boolean isActive;
	private boolean isEnabled;
	protected  String scroll;
	

	/**
	 * Create a card with an ID and a name
	 * @param id ID of the card
	 * @param name Name of the card
	 */
	public Card(int id, String name, CardType type, List<Action> actions, String scroll)
	{
		this.type = type;
		this.id = id;
		this.name = name;
		this.actions = actions;
		this.isActive = true;
		this.isEnabled = true;
		this.scroll = scroll;
		
	}
	
	/**
	 * Get card's ID
	 * @return card's ID
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Get card's name
	 * @return card's name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return the card type
	 */
	public CardType getType() 
	{
		return type;
	}
	
	/**
	 * @return the actions to be executed
	 */
	public List<Action> getActions() 
	{
		return Collections.unmodifiableList(this.actions);
	}
	
	/**
	 * Get the action of the corresponding class
	 * @param actionClass
	 * @return 
	 */
	public List<Action> getActions(Class<?> actionClass)
	{
		ArrayList<Action> actionList = new ArrayList<Action>();
		for(Action action : actions)
		{
			if (action.getClass().equals(actionClass))
			{
				actionList.add(action);
			}
		}
		return actionList;
	}
	
	/**
	 * @return if the card is in an active state (can execute actions)
	 */
	public boolean isActive()
	{
		return this.isActive;
	}
	
	/**
	 * Set card active state
	 * @param active whether true of false the card is in an active state
	 */
	public void setActiveState(boolean active)
	{
		this.isActive = active;
	}
	
	/**
	 * @return if the card is in an enabled state (can execute actions)
	 */
	public boolean isEnabled()
	{
		return this.isEnabled;
	}
	
	/**
	 * Set card to disabled
	 */
	public void disableCard()
	{
		this.isEnabled = false;
	}

	/**
	 * Show the card information
	 */
	@Override
	public String toString()
	{
		return this.getId() + " " + this.type + " - " + this.name + ": " + this.actions.toString();
	}
	
	/**
	 * @return if the card can be executed
	 */
	public boolean canExecute(GameManager gameManager)
	{
		if(!getActions().isEmpty())
		{
			return getActions().get(0).canExecute(gameManager);
		}
		return true;
	}
	
	/**
	 * Check if this action stops the target
	 * @param target stop target
	 * @return true or false the target is stopped
	 */
	public boolean doesStop(Action.StopTarget target)
	{
		if (this.actions != null)
		{
			for(Action action : this.actions)
			{
				if (action.doesStop(target))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param playerCardPlayed if the player card has been played or not
	 * @return Verify the action to know if it can be executed or not
	 */
	public boolean canBeExecutedTurnBase(boolean playerCardPlayed) 
	{
		boolean canBeExecuted = playerCardPlayed;
		if(!actions.isEmpty())
		{
			canBeExecuted = actions.get(0).canBeExecutedTurnBase(playerCardPlayed);
		}
		return canBeExecuted;
	}
}
