package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum PersonalityEventXML implements ReversibleEnum
{
	COMMANDER_VIMES__NO_PLAYER_CARD ("NoPlayerCard"),
	LORD_DEWORDE_SELACHII_RUST__AREA_CONTROL ("AreaControl"),
	LORD_VETINARI__AREA_SPY ("AreaSpy"),
	DRAGON_KING_OF_ARMS__MUCH_TROUBLE ("MuchTrouble"),
	CHRYSOPRASE__MUCH_ASSETS ("MuchAssets");

	private static Map<String, PersonalityEventXML> xmlToEnumMap = null;
	private final String xmlValue;
	PersonalityEventXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static PersonalityEventXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, PersonalityEventXML>();
			for (PersonalityEventXML action : PersonalityEventXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}