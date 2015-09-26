package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum CityEventXML implements ReversibleEnum
{
	// Unreal estate
	UNREAL_ESTATE__PICK_DISCARD_PLAYER_CARD ("PickAndDiscardPlayerCard"),
	// Isle of god
	ISLE_GOD__PAY_TO_REMOVE_TROUBLE ("PayRemoveTrouble"),
	// Dolly sisters and Dimwell
	DOLLY_DIMWELL__PAY_TO_ADD_MINION_NEIGHTBORHOOD ("PayAddNeighborhoodMinion"),
	// The Shades
	THE_SHADES__ADD_NEIGHBORHOOD_TROUBLE ("AddNeighborhoodTrouble"),
	// The Scours
	THE_SCOURS__DISCARD_PLAYER_CARD_FOR_MONEY ("DiscardPlayerCardForMoney"),
	//The Hippo, Dragon's Landing, Nap Hill, Seven Sleepers and Longwall
	HIPPO_DRAGON_NAP_SLEEPERS_LONGWALL__EARN_MONEY ("EarnMoney"),
	//Small Gods
	SMALL_GODS__STOP_RANDOM_EVENT ("StopRandomEventLandmarkAffected");

	private static Map<String, CityEventXML> xmlToEnumMap = null;
	private final String xmlValue;
	CityEventXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static CityEventXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, CityEventXML>();
			for (CityEventXML action : CityEventXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}