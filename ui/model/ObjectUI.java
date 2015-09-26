package ankhmorpork.ui.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectUI 
{
	protected final int MIN_DISTANCE = 10;
	protected final double K_RATIO = 0.35;
	private List<Point> listPoint = new ArrayList<Point>();
	private Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	private Point maxPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);	

	/**
	 * Add a delimiter point
	 * @param point to be added to the list
	 */
	public void addDelimiterPoint(Point point)
	{
		listPoint.add(point);

		if(point.x < minPoint.x) 
		{
			minPoint.x = point.x;
		}
		
		if(point.x > maxPoint.x) 
		{
			maxPoint.x = point.x;
		}
		
		if(point.y < minPoint.y) 
		{
			minPoint.y = point.y;
		}
		
		if(point.y > maxPoint.y) 
		{
			maxPoint.y = point.y;
		}
	}

	/**
	 * @return the minPoint
	 */
	public Point getMinPoint()
	{
		return minPoint;
	}
	
	/**
	 * @return the maxPoint
	 */
	public Point getMaxPoint()
	{
		return maxPoint;
	}

	/**
	 * @return the listPoint
	 */
	public List<Point> getListPointDelimiter()
	{
		return Collections.unmodifiableList(listPoint);
	}
}
