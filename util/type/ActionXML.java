package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum ActionXML implements ReversibleEnum
{
	GUARD ("NOTR"),
	KILL ("KILL"),
	EVENT ("EVNT"),
	MINION ("PERS"),
	GOLD ("GOLD"),
	SCROLL ("TEXT"),
	CARD ("CARD"),
	BUILDING ("BLDG"),
	STOP ("STOP");

	private static Map<String, ActionXML> xmlToEnumMap = null;
	private final String xmlValue;
	ActionXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static ActionXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, ActionXML>();
			for (ActionXML action : ActionXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}