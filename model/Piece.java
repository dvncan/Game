package ankhmorpork.model;

import java.awt.Point;

/**
 * Abstract piece of game item that belongs to an owner
 * @author Team 2
 * @since Build 1
 */
public abstract class Piece
{
	protected Actor owner;

	private Point pointUI_topLeft;
	private Point pointUI_bottomRight;

	/**
	 * Create an piece without an owner
	 */
	public Piece()
	{
		
	}	

	/**
	 * Create a piece with an owner
	 * @param owner the owner of the piece
	 */
	public Piece(Actor owner)
	{
		this.owner = owner;
	}

	/**
	 * Retrieve the owner of the piece
	 * @return the owner of the piece
	 */
	public Actor getOwner()
	{
		return this.owner;
	}

	/**
	 * Assign an owner to the piece
	 * @param owner the owner of the piece
	 */
	public void setOwner(Actor owner)
	{
		this.owner = owner;
	}

	/**
	 * Find whether the piece is owned by the given actor
	 * @param actor the actor to test whether they own this piece or not
	 * @return true if the actor owns this piece
	 */
	public boolean isOwnedBy(Actor actor)
	{
		return this.owner == actor;
	}

	/**
	 * @return the pointUI_bottomRight
	 */
	public Point getPointUI_bottomRight() {
		return pointUI_bottomRight;
	}

	/**
	 * @param pointUI_bottomRight the pointUI_bottomRight to set
	 */
	public void setPointUI_bottomRight(Point pointUI_bottomRight) {
		this.pointUI_bottomRight = pointUI_bottomRight;
	}

	/**
	 * @return the pointUI_topLeft
	 */
	public Point getPointUI_topLeft() {
		return pointUI_topLeft;
	}

	/**
	 * @param pointUI_topLeft the pointUI_topLeft to set
	 */
	public void setPointUI_topLeft(Point pointUI_topLeft) {
		this.pointUI_topLeft = pointUI_topLeft;
	}
}
