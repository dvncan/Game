package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum StopEventXML implements ReversibleEnum
{
	STOP_MINION_MOVED_OR_REMOVED ("StopMinionMovedRemoved"),
	STOP_MINION_REMOVED ("StopMinionRemoved"),
	MOVE_MINION_REMOVED ("MoveMinionRemoved"),
	STOP_TEXT ("StopText"),
	STOP_TURN ("StopTurnPickLastCardPlayed");

	private static Map<String, StopEventXML> xmlToEnumMap = null;
	private final String xmlValue;
	StopEventXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static StopEventXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, StopEventXML>();
			for (StopEventXML action : StopEventXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}