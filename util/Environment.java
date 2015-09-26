package ankhmorpork.util;

import ankhmorpork.factory.ActionFactory;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import ankhmorpork.model.Action;
import ankhmorpork.model.Area;
import ankhmorpork.model.Player;
import ankhmorpork.ui.model.AreaUI;
import ankhmorpork.util.type.ActionXML;
import ankhmorpork.util.type.CardType;

/**
 * Environment settings, global variables, housekeeping functions
 * @author Team 2
 * @since Build 1
 */
public class Environment
{
	public static Random rand = new Random();
	
	public static final String CONFIGURATION_PATH = ".\\data\\data.xml";
	public static final String GAME_BOARD_IMAGE_PATH = ".\\data\\am_map_uk.jpg";
	public static final String PIECES_IMAGE_PATH = ".\\data\\pieces.png";
	public static final String CARDS_IMAGE_PATH = ".\\data\\cards\\%03d.png";
	public static final String AI_NAMES_PATH = ".\\data\\ai_names.cfg";
	public static final int DEFAULT_SHUFFLE_TIMES = 200;
	
	private static ActionFactory actionFactory = null;

	/**Add formatting on the right of a keyword for aligning text
	 * 
	 * @param s : string
	 * @param n : size of string to add padding if it smaller
	 * @return the resulting string with the padding
	 */
	public static String padRight(String s, int n) 
	{
    return String.format("%1$-" + n + "s", s);  
	}

	/**
	 * If the string is numeric
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
/**
 * Find the Area where the position X,Y is inside
 * @param board the game board which contain every area
 * @param x position
 * @param y position
 * @return the area ID if it found it otherwise -1
 */
	public static int FindZone(Iterator<Entry<Integer, AreaUI>> areaUIMapIterator, int x, int y)
	{
		int areaID = -1;
		Point position = new Point(x, y);
		while (areaUIMapIterator.hasNext())
		{
      Map.Entry<Integer, AreaUI> pair = (Map.Entry<Integer, AreaUI>)areaUIMapIterator.next();
			AreaUI area = pair.getValue();
			if(RayTracing(position, area))
			{
				areaID = area.getAreaID();
				break;
			}
		}
		return areaID;
	}

	/**
	 * Returns if the position is inside or outside the shape of the list of points
	 * <p>
	 * This formula use the RayTracing logic learned in COMP6761 Advanced Computer Graphics
	 * We are casting a ray on the X axis from left up until the current position we want to analyze.
	 * We than count how many time we hit the border of the shape.
	 * 
	 * If the count is even we aren't over the shape anymore, if it odd, we are on the shape.
	 * First border we cross is to go over the shape, if we cross another border we are leaving the shape etc...
	 *
	 * @param  position the position which we want to test if it's within the shape or not
	 * @param  area the area object which contain a list of points to delimiter the area
	 * @return      if a ray on the X axis pass between p1 and p2 to reach position
	 */
	public static boolean RayTracing(Point position, AreaUI area)
	{
		// PreCondition
		if(position.x < area.getMinPoint().x 
				|| position.x > area.getMaxPoint().x 
				|| position.y < area.getMinPoint().y || 
				position.y > area.getMaxPoint().y)
		{
			return false;
		}
		List<Point> lstPoint = area.getListPointDelimiter();
		int nCountRayCrossLineAxisY = 0;		
		for(int i = 0; i < lstPoint.size()-1; i++)
		{
			Point p1 = lstPoint.get(i);
			Point p2 = lstPoint.get(i + 1);
			boolean isValidPointPosition = true;
			if(p1.x > position.x && p2.x > position.x)
			{
				// we ignore those coordinate they are on the right side of our position
			}
			else
			{
				if(position.y == p1.y && p1.y == p2.y)
				{
					// both are at the same position, ignore
				}
				else
				{
					int previousPointIndex = i;
					while(position.y == p1.y)
					{
						previousPointIndex--;
						if(previousPointIndex < 0)
						{
							previousPointIndex = lstPoint.size() - 1;
						}
						// if the ray cross on the dot of P1, we analyze with the dot before and after P1
						p1 = lstPoint.get(previousPointIndex);
						isValidPointPosition = false;
					}
		
					if(IsRayCrossLineAxisY(p1, p2, position, isValidPointPosition))
					{
						nCountRayCrossLineAxisY++;
					}
				}
			}
		}

		// If the amount of time that the line was crossed is odds, than we are inside the shape.
		// If the amount of time that the line was crossed is even, than we are outside the shape.
		return (nCountRayCrossLineAxisY % 2 == 1);
	}

	/**
	 * Returns if a ray on the X axis pass between p1 and p2 to reach position
	 * <p>
	 * This method is the core logic behind ray-tracing
	 *
	 * @param  p1 A point that delimiter the shape
	 * @param  p2 the point that follow p1 inside the shape
	 * @param  position the position which we want to test if it's within the shape or not
	 * @param  isValidPointPosition if the 2 Points given have been alternated
	 * @return      if a ray on the X axis pass between p1 and p2 to reach position
	 */
	private static boolean IsRayCrossLineAxisY(Point p1, Point p2, Point position, boolean isValidPointPosition)
	{
		boolean isRayCrossLineAxisY = false;
		int minYvalue = Math.min(p1.y, p2.y);
		int maxYvalue = Math.max(p1.y, p2.y);
		if(position.y > minYvalue && position.y < maxYvalue)
		{
			// The ray traverse the line
			if(p1.x < position.x && p2.x < position.x)
			{
				isRayCrossLineAxisY = true;
			}
			else if (!isValidPointPosition)
			{
				isRayCrossLineAxisY = true;
			}
			else
			{
				// We have a diagonal line were the dot can either be on the left side
				// of the shape, which mean outside of the shape or it can be on the right
				// side of the line which would mean inside the shape

				// y = ax+b
				double a = (p2.y - p1.y) / (double)(p2.x - p1.x);
				double b = p1.y - a * p1.x;
				double x_linePos = (position.y - b) / a;
				if(x_linePos < position.x)
				{
					// dot is on the right side of the line
					isRayCrossLineAxisY = true;
				}
			}
		}
		return isRayCrossLineAxisY;
	}
	
	/**
	 * Load card information
	 * @param lstActions
	 * @param scroll
	 * @param money
	 * @return
	 * @throws Exception 
	 */
	public static List<Action> loadCardActions(String[] lstActions, String scroll, String money) throws Exception
	{		
		List<Action> actionList = new ArrayList<Action>();
		
		for(String actionName : lstActions)
		{
			if(!actionName.isEmpty())
			{
				List<String> scrollValues;
				if(ActionXML.reverseXMLtoEnum(actionName) == ActionXML.GOLD)
				{
					scrollValues = new ArrayList<String>();
					scrollValues.add(money);
				}
				else
				{
					scrollValues = new ArrayList<String>(Arrays.asList(scroll.split(";")));
				}
				actionList.add(Environment.getActionFactory().CreateAction(CardType.BASE, actionName, scrollValues));
			}
		}
		return actionList;
	}
	
	/**
	 * http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) 
	{
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/**
	 * @return List of AI names from file
	 */
	public static List<String> getFamousNames() 
	{
		List<String> famousName = new ArrayList<String>();
		
		BufferedReader br;
		try 
		{
			String line;
			br = new BufferedReader(new FileReader(AI_NAMES_PATH));
			while ((line = br.readLine()) != null)
			{
				famousName.add(line);
			}
			br.close();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return famousName;
	}
	
	/**
	 * Parse an integer without throwing any exception, return -1 in case of error
	 * @param value string to be converted as integer
	 */
	public static int parseIntWithoutError(String value)
	{
		int number;
		try
		{
			number = Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			number = -1;
		}
		return number;
	}
	
	/**
	 * If the value is an integer
	 * @param value to analyze
	 * @return if the value is an integer
	 */
	public static boolean isInteger(String value)
	{
		try  
		{  
			Integer.parseInt(value);  
			return true;  
		}
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}
	}
	
	/**
	 * Sort a hash set of player by ID
	 * @param setPlayer
	 * @return
	 */
	public static List<Player> sortPlayerList(HashSet<Player> setPlayer)
	{
		//Sorting Player
		List<Player> listPlayers = new ArrayList<Player>(setPlayer);
		Collections.sort(listPlayers, new Comparator<Player>() {
		        @Override
		        public int compare(Player player1, Player player2)
		        {
		        	return  Integer.compare(player1.getId(), player2.getId());
		        }
		    });
		return listPlayers;
	}

	/**
	 * Sort a hash set of area by ID
	 * @param areaSet
	 * @return
	 */
	public static List<Area> sortAreaList(HashSet<Area> areaSet) 
	{
		//Sorting to always have the same order
		List<Area> listArea = new ArrayList<Area>(areaSet);
		Collections.sort(listArea, new Comparator<Area>() 
			{
	        @Override
	        public int compare(Area area1, Area area2)
	        {
	        	return Integer.compare(area1.getNumber(), area2.getNumber());
	        }
	    });
		return listArea;
	}

	/**
	 * @return the factory to create actions
	 */
	public static ActionFactory getActionFactory()
	{
		if(actionFactory == null)
		{
			actionFactory = new ActionFactory();
		}
		return actionFactory;
	}
}