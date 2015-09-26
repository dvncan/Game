package ankhmorpork.model.landmark;

import ankhmorpork.model.Color;
import ankhmorpork.model.Player;

/**
 * Building piece
 * @see Landmark
 * @author Team 2
 * @since Build 1
 */
public class Building extends Landmark
{
	/**
	 * Create a building
	 */
	public Building()
	{
	}
	
	/**
	 * Get the color of the building
	 * @return the color of the building
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
