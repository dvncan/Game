package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum CardType implements ReversibleEnum
{
	BASE ("Base"),
	SCROLL ("PlayerCard"),
	CITY ("CityAreaCard"),
	PERSONALITY ("PersonalityCard"),
	EVENT ("RandomEventCard");

	private static Map<String, CardType> xmlToEnumMap = null;
	private final String xmlValue;
	CardType(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static CardType reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, CardType>();
			for (CardType action : CardType.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}