package ankhmorpork.model.landmark;

import ankhmorpork.model.Color;
import ankhmorpork.model.Player;

/**
 * Minion character piece
 * @see Character
 * @author Team 2
 * @since Build 1
 */
public class Minion extends Character
{
	/**
	 * Create a minion
	 */
	public Minion()
	{
	}
	
	/**
	 * Get the color of the minion
	 * @return the color of the minion
	 */
	public Color getColor()
	{
		if (this.owner != null && this.owner instanceof Player)
		{
			return ((Player)this.owner).getColor();
		}
		return null;
	}
}
