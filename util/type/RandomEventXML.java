package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum RandomEventXML implements ReversibleEnum
{
	FIRE__SPREAD_BUILDING_FIRE ("SpreadBuildingFire"),
	FOG__DISCARD_BOARD_PLAYER_CARD ("DiscardBoardPlayerCard"),
	MURDERS__ALL_MURDER_MINION ("AllPlayerMurderMinion"),
	RIOTS__END_GAME_TROUBLE ("EndGameWithTrouble"),
	SUBSIDENCE__ALL_PAY_OR_LOSE_BUILDING ("AllPlayerPayOrLoseBuilding"),
	TROLLS__ADD_RANDOM_AREA_TROLL ("AddRandomAreaTroll"),
	JOHNSON__REMOVE_RANDOM_AREA_CARD_AND_MINION ("RemoveRandomAreaCardAndMinion"),
	DEMONS__ADD_RANDOM_AREA_DEMONS ("AddRandomAreaDemon"),
	DRAGON__REMOVE_RANDOM_AREA_LANDMARK ("RemoveRandomAreaLandmark"),
	EARTHQUAKE_EXPLOSION__REMOVE_BUILDING ("RemoveRandomAreaBuilding"),
	FLOOD__MOVE_FLOODED_MINION ("MoveFloodedMinion");

	private static Map<String, RandomEventXML> xmlToEnumMap = null;
	private final String xmlValue;
	RandomEventXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static RandomEventXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, RandomEventXML>();
			for (RandomEventXML action : RandomEventXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}