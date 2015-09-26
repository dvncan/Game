package ankhmorpork.model.landmark;

import ankhmorpork.model.Actor;
import ankhmorpork.model.Area;
import ankhmorpork.model.Piece;

/**
 * 
 * @see Piece
 * @see Character
 * @see Building
 * @author Team 2
 * @since Build 1
 */
public abstract class Landmark extends Piece
{
	private Area location;
	
	/**
	 * Create a landmark
	 */
	public Landmark()
	{
	}
	
	/**
	 * Create a landmark having an owner
	 * @param owner the owner of the landmark
	 */
	public Landmark(Actor owner)
	{
		super(owner);
	}

	/**
	 * Get the location of the landmark
	 * @return the location of the landmark
	 */
	public Area getLocation()
	{
		return this.location;
	}

	/**
	 * Set the location of the landmark
	 * @param location the area where the landmark will be located
	 */
	public void setLocation(Area location)
	{
		this.location = location;
	}
}
