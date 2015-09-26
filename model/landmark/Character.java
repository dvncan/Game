package ankhmorpork.model.landmark;

import ankhmorpork.model.Actor;

/**
 * Character piece
 * @see Landmark
 * @see Minion
 * @see Troll
 * @see Demon
 * @author Team 2
 * @since Build 1
 */
public abstract class Character extends Landmark
{
	public static class CharacterType
	{
		public static final String MINION = "Minion";
		public static final String TROLL = "Troll";
		public static final String DEMON = "Demon";
	}
	
	/**
	 * Create a character
	 */
	public Character()
	{
		
	}
	
	/**
	 * Create a character having an owner
	 * @param owner the owner of the character
	 */
	public Character(Actor owner)
	{
		super(owner);
	}
}
