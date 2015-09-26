package ankhmorpork.util.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Type from XML
 * @author Team 2
 * @since Build 1
 */
public enum ScrollEventXML implements ReversibleEnum
{
	TAKE_MONEY_OR_GIVE_USELESS_CARD ("TakeMoneyOrGiveUselessCard"),
	TAKE_MONEY_FOR_EACH_TROUBLE_AREA_MINION ("TakeMoneyForEachTroubleAreaMinion"),
	SELECT_PLAYER_GET_MONEY_FOR_CARD ("SelectPlayerGetMoneyForCard"),
	DISCARD_X_CARDS_FOR_MONEY ("DiscardXCardsForMoney"),
	DISCARD_X_CARDS ("DiscardXCards"),
	DISCARD_UP_TO_X_CARDS_AND_FILL_UP_HAND ("DiscardUpToXCardAndFillUpHandAfter"),
	DRAW_CARD_FOR_EACH_BUILDING ("DrawCardForEachBuilding"),
	TAKE_FROM_EACH_PLAYER_MONEY_OR_A_CARD ("TakeFromEachPlayerMoneyOrACard"),
	EARN_MONEY_FOR_EACH_BUILDING_ON_BOARD ("EarnMoneyForEachBuildingOnBoard"),
	EARN_MONEY_FOR_EACH_MINION_IN_AREA ("EarnMoneyForEachMinionInArea"),
	EARN_MONEY_FROM_TROUBLE ("EarnMoneyFromTrouble"),
	EVERY_OTHER_PLAYER_REMOVE_A_MINION ("EveryOtherPlayerRemoveMinion"),
	EXCHANGE_POSITION_MINION_ON_BOARD ("ExchangePositionOfMinionOnBoard"),
	EXCHANGE_HAND_WITH_OTHER_PLAYER ("ExchangeHandWithOtherPlayer"),
	FORCE_PLAYER_TO_GIVE_MONEY_TO_PLAYER ("ForcePlayerToGiveMoneyToPlayer"),
	LOOK_AT_ALL_PERSONALITY_CARD_EXCEPT_ONE ("LookAllUnusedPersonalityCardExceptOne"),
	MOVE_ENEMY_MINION_TO_ADJACENT_AREA ("MoveEnemyMinionToAdjacentArea"),
	MOVE_OWN_MINION_FROM_TROUBLE_TO_ADJACENT ("MoveOwnMinionFromTroubleMarkerToAdjacent"),
	MOVE_OWN_MINION_FROM_ONE_AREA_TO_ANOTHER ("MoveOwnMinionFromOneAreaToAnother"),
	PAY_PLAYER_OF_CHOICE_THEN_ADD_MINION ("PayPlayerOfChoiceThenAddMinion"),
	PAY_PLAYER_OF_CHOICE_THEN_ASSASSINATE_MINION ("PayPlayerOfChoiceThenAssassinateMinion"),
	PLACE_MINION_IN_AREA_WITHOUT_TROUBLE_MARKER ("PlaceMinionInAreaWithoutTroubleMarker"),
	SET_TROUBLE_MARKET_ANYWHERE ("SetTroubleMarkerAnywhere"),
	ADD_MINION_IN_AREA_WITH_TROUBLE ("AddMinionAreaWithTrouble"),
	ADD_MINION_IN_AN_AREA_THAT_YOU_OWN_A_BUILDING ("AddMinionInAreaOwnABuilding"),
	PLACE_MINION_IN_AREA_OR_NEIGHTBOOR ("PlaceMinionInAreaOrNeightboor"),
	SET_TROUBLE_MARKER_TO_NEIGHTBOOR_OF_TROUBLE_MARKER ("SetTroubleMarkerToNeightboorOfTroubleMarker"),
	BORROW_MONEY_FROM_THE_BANK ("BorrowMoneyFromTheBank"),
	INCREASE_AMOUNT_CARD_LEFT_TO_PLAY ("IncreaseAmountOfCardLeftToPlay"),
	REMOVE_MINION_FROM_AREAID ("RemoveMinionFromAreaID"),
	REPLACE_ENEMY_BUILDING_WITH_YOUR_OWN ("ReplaceEnemyBuildingWithYourOwn"),
	ROLL_DIE_REMOVE_MINION_IN_AREA ("RollTheDiceAndRemoveMinionInArea"),
	DICE_HIGH_REMOVE_ANY_MINION_EQUAL_LOSE_YOUR_MINION ("DiceHighRemoveAnyMinionEqualLoseYourMinion"),
	DICE_HIGH_WIN_MONEY_EQUAL_LOSE_MONEY_OR_MINION ("DiceHighWinMoneyEqualLoseMoneyOrMinion"),
	TAKE_MONEY_OR_REMOVE_BUILDING_FROM_SELECTED_PLAYER ("TakeMoneyOrRemoveBuildingFromSelectedPlayer"),
	SELECT_PLAYER_VIEW_ALL_PLAYERCARDS_AND_DISCARD_ONE ("SelectPlayerViewAllPlayerCardAndDiscardOne"),
	SELECT_PLAYER_TO_GIVE_YOU_PLAYERCARDS ("SelectPlayerToGiveYouPlayerCards"),
	LOOK_AT_PLAYER_CARDS_AND_STEAL_ONE ("LookAtPlayerCardsAndStealOneCard"),
	DRAW_PLAYER_CARDS_FROM_DISCARD_PILE ("DrawPlayerCardFromDiscardPile"),
	TAKE_MONEY_FOR_EACH_BUILDING ("TakeMoneyForEachBuilding"),
	STEAL_MONEY_FROM_ALL_PLAYERS ("StealMoneyFromAllPlayers"),
	STEAL_MONEY_FROM_PLAYER_CHOICE ("StealMoneyFromPlayerChoice"),
	PICKUP_PLAYER_CARDS ("PickupPlayerCards"),
	PLACE_BUILDING_HALF_PRICE ("PlaceBuildingHalfPrice"),
	CHANGE_PERSONALITY_CARD ("ChangePersonalityCard");

	private static Map<String, ScrollEventXML> xmlToEnumMap = null;
	private final String xmlValue;
	ScrollEventXML(String xmlValue) 
	{
		this.xmlValue = xmlValue;
	}
	public String xmlValue() 
	{ 
		return this.xmlValue; 
	}

	public static ScrollEventXML reverseXMLtoEnum(String value)
	{
		if(xmlToEnumMap == null)
		{
			xmlToEnumMap = new HashMap<String, ScrollEventXML>();
			for (ScrollEventXML action : ScrollEventXML.values()) 
			{
				xmlToEnumMap.put(action.xmlValue().toLowerCase(), action);
			}
		}
		return xmlToEnumMap.get(value.toLowerCase());
	}
}