package ankhmorpork.ui.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ankhmorpork.util.Environment;

public class AreaUI extends ObjectUI
{
	private final int areaID;
	private List<Point> listUIObject = new ArrayList<Point>();

	public AreaUI(int id) 
	{
		this.areaID = id;
	}

	public int getAreaID() 
	{
		return this.areaID;
	}

	public Point getPosition(int i) 
	{
		if(i < listUIObject.size())
		{
			return listUIObject.get(i);
		}
		
		
		
		// point to next index
		i = listUIObject.size();
		
		HashMap<Integer, AreaUI> areaUIMap = new HashMap<Integer, AreaUI>();
		areaUIMap.put(this.getAreaID(), this);
		boolean found = false;
		while(!found)
		{
			int x = Environment.randInt(getMinPoint().x + MIN_DISTANCE, getMaxPoint().x - MIN_DISTANCE);
			int y = Environment.randInt(getMinPoint().y + MIN_DISTANCE, getMaxPoint().y - MIN_DISTANCE);
			
			boolean positionStacked = false;
			for(Point point : listUIObject)
			{
				if(Math.abs(point.x-x) + Math.abs(point.y-y) < 10)
				{
					positionStacked = true;
					break;
				}
			}
			
			if(!positionStacked && Environment.FindZone(areaUIMap.entrySet().iterator(), x, y) == this.getAreaID())
			{
				listUIObject.add(new Point(x, y));
				found = true;
			}
		}

		return listUIObject.get(i);
	}
	
	/**
	 * Add a predefined position in area to place UIPieces
	 * @param point
	 * @return
	 */
	public boolean addPosition(Point point) 
	{
		HashMap<Integer, AreaUI> areaUIMap = new HashMap<Integer, AreaUI>();
		areaUIMap.put(this.getAreaID(), this);
		boolean found = false;

		boolean positionStacked = false;
		for(Point p : listUIObject)
		{
			if(Math.abs(p.x-point.x) + Math.abs(p.y-point.y) < 10)
			{
				positionStacked = true;
				break;
			}
		}
		
		if(!positionStacked && Environment.FindZone(areaUIMap.entrySet().iterator(), point.x, point.y) == this.getAreaID())
		{
			listUIObject.add(point);
			found = true;
		}

		return found;
	}
}
