package ankhmorpork.model;
import java.util.HashMap;

/**
 * Named color with corresponding hexadecimal color code to visualize it on screen
 * @author Team 2
 * @since Build 1
 */
public class Color
{
	private final String name;
	private final int hexColor;
	/** The static palette of color in order to ensure that all colors are unique */
	private static HashMap<String, Color> colorMap = new HashMap<String, Color>();
	
	/**
	 * Create a new color
	 * @param name the name of the color
	 * @param hexColorCode the hexadecimal code of the color as string
	 * @throws Exception the color already exists
	 */
	private Color(String name, String hexColorCode) throws Exception
	{
		String keyName = name.toLowerCase();
		if(colorMap.containsKey(keyName))
		{
			throw new Exception("Color already exist");
		}
		
		this.name = name;
		this.hexColor = Integer.parseInt(hexColorCode, 16);
		colorMap.put(keyName, this);
	}
	
	/**
	 * Create a new color
	 * @param name the name of the color
	 * @param hexColorCode the hexadecimal code of the color as integer
	 * @throws Exception the color already exists
	 */
	private Color(String name, int hexColor) throws Exception
	{
		String keyName = name.toLowerCase();
		if(colorMap.containsKey(keyName))
		{
			throw new Exception("Color already exist");
		}
		this.name = name;
		this.hexColor = hexColor;
		colorMap.put(keyName, this);
	}
	
	/**
	 * Find the color in the palette/map according to its name
	 * @param name the name of the color
	 * @return the color of that name
	 */
	public static Color getColor(String name)
	{
		return colorMap.getOrDefault(name, null);
	}

	/**
	 * Add a color to the palette/map and return the newly created color
	 * @param name the name of the color
	 * @param hexColorCode the hexadecimal code of the color as string
	 * @return the newly created color
	 * @throws Exception the color already exists
	 */
	public static Color AddColor(String name, String hexColorCode) throws Exception
	{
		String keyName = name.toLowerCase();
		return new Color(keyName, hexColorCode);
	}
	
	/**
	 * Add a color to the palette/map and return the newly created color
	 * @param name the name of the color
	 * @param hexColor the hexadecimal code of the color as integer
	 * @return the newly created color
	 * @throws Exception the color already exists
	 */
	public static Color AddColor(String name, int hexColor) throws Exception
	{
		String keyName = name.toLowerCase();
		return new Color(keyName, hexColor);
	}

	/**
	 * Find the name of the color
	 * @return the name of the color
	 */
	public String getName() 
	{
		return this.name;
	}

	/**
	 * Find the initial of the color
	 * @return the initial of the color
	 */
	public String getInitial() 
	{
		if (this.name != null && !this.name.isEmpty())
		{
			return this.name.substring(0,1).toUpperCase();
		}
		return "";
	}

	/**
	 * Find the integer form of the hexadecimal code of the color
	 * @return the hexadecimal code of the color as integer
	 */
	public int getHexColor() 
	{
		return this.hexColor;
	}
	
	/**
	 * Find the string form of the hexadecimal code of the color
	 * @return the hexadecimal code of the color as string
	 */
	public String getHexColorString() 
	{
		return Integer.toHexString(this.hexColor);
	}
	
	/**
	 * Show the color name
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}
