package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.playercard.*;
import ankhmorpork.util.type.ScrollEventXML;

public class ScrollActionFactory extends AbstractSubActionFactory 
{

	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(ScrollEventXML.reverseXMLtoEnum(actionType), arguments);
	}

	public Action CreateAction(ScrollEventXML actionType, List<String> arguments) throws Exception 
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case TAKE_MONEY_OR_GIVE_USELESS_CARD:
			return new TakeMoneyOrGiveUselessCard(arguments);
		case TAKE_MONEY_FOR_EACH_TROUBLE_AREA_MINION:
			return new TakeMoneyForEachTroubleAreaMinion(arguments);
		case SELECT_PLAYER_GET_MONEY_FOR_CARD:
			return new SelectPlayerGetMoneyForCard(arguments);
		case DISCARD_X_CARDS_FOR_MONEY:
			return new DiscardXCardsForMoney(arguments);
		case DISCARD_X_CARDS:
			return new DiscardXCards(arguments);
		case DISCARD_UP_TO_X_CARDS_AND_FILL_UP_HAND:
			return new DiscardUpToXCardAndFillUpHandAfter(arguments);
		case DRAW_CARD_FOR_EACH_BUILDING:
			return new DrawCardForEachBuilding(arguments);
		case TAKE_FROM_EACH_PLAYER_MONEY_OR_A_CARD:
			return new TakeFromEachPlayerMoneyOrACard(arguments);
		case EARN_MONEY_FOR_EACH_BUILDING_ON_BOARD:
			return new EarnMoneyForEachBuildingOnBoard(arguments);
		case EARN_MONEY_FOR_EACH_MINION_IN_AREA:
			return new EarnMoneyForEachMinionInArea(arguments);
		case EARN_MONEY_FROM_TROUBLE:
			return new EarnMoneyFromTrouble(arguments);
		case EVERY_OTHER_PLAYER_REMOVE_A_MINION:
			return new EveryOtherPlayerRemoveMinion(arguments);
		case EXCHANGE_POSITION_MINION_ON_BOARD:
			return new ExchangePositionOfMinionOnBoard(arguments);
		case EXCHANGE_HAND_WITH_OTHER_PLAYER:
			return new ExchangeHandWithOtherPlayer(arguments);
		case FORCE_PLAYER_TO_GIVE_MONEY_TO_PLAYER:
			return new ForcePlayerToGiveMoneyToPlayer(arguments);
		case LOOK_AT_ALL_PERSONALITY_CARD_EXCEPT_ONE:
			return new LookAllUnusedPersonalityCardExceptOne(arguments);
		case MOVE_ENEMY_MINION_TO_ADJACENT_AREA:
			return new MoveEnemyMinionToAdjacentArea(arguments);
		case MOVE_OWN_MINION_FROM_TROUBLE_TO_ADJACENT:
			return new MoveOwnMinionFromTroubleMarkerToAdjacent(arguments);
		case MOVE_OWN_MINION_FROM_ONE_AREA_TO_ANOTHER:
			return new MoveOwnMinionFromOneAreaToAnother(arguments);
		case PAY_PLAYER_OF_CHOICE_THEN_ADD_MINION:
			return new PayPlayerOfChoiceThenAddMinion(arguments);
		case PAY_PLAYER_OF_CHOICE_THEN_ASSASSINATE_MINION:
			return new PayPlayerOfChoiceThenAssassinateMinion(arguments);
		case PLACE_MINION_IN_AREA_WITHOUT_TROUBLE_MARKER:
			return new PlaceMinionInAreaWithoutTroubleMarker(arguments);
		case SET_TROUBLE_MARKET_ANYWHERE:
			return new SetTroubleMarkerAnywhere(arguments);
		case ADD_MINION_IN_AREA_WITH_TROUBLE:
			return new AddMinionAreaWithTrouble(arguments);
		case ADD_MINION_IN_AN_AREA_THAT_YOU_OWN_A_BUILDING:
			return new AddMinionInAreaOwnABuilding(arguments);
		case PLACE_MINION_IN_AREA_OR_NEIGHTBOOR:
			return new PlaceMinionInAreaOrNeightboor(arguments);
		case SET_TROUBLE_MARKER_TO_NEIGHTBOOR_OF_TROUBLE_MARKER:
			return new SetTroubleMarkerToNeighborOfTroubleMarker(arguments);
		case BORROW_MONEY_FROM_THE_BANK:
			return new BorrowMoneyFromTheBank(arguments);
		case INCREASE_AMOUNT_CARD_LEFT_TO_PLAY:
			return new IncreaseAmountOfCardLeftToPlay(arguments);
		case REMOVE_MINION_FROM_AREAID:
			return new RemoveMinionFromAreaID(arguments);
		case REPLACE_ENEMY_BUILDING_WITH_YOUR_OWN:
			return new ReplaceEnemyBuildingWithYourOwn(arguments);
		case ROLL_DIE_REMOVE_MINION_IN_AREA:
			return new RollTheDiceAndRemoveMinionInArea(arguments);
		case DICE_HIGH_REMOVE_ANY_MINION_EQUAL_LOSE_YOUR_MINION:
			return new DiceHighRemoveAnyMinionEqualLoseYourMinion(arguments);
		case DICE_HIGH_WIN_MONEY_EQUAL_LOSE_MONEY_OR_MINION:
			return new DiceHighWinMoneyEqualLoseMoneyOrMinion(arguments);
		case TAKE_MONEY_OR_REMOVE_BUILDING_FROM_SELECTED_PLAYER:
			return new TakeMoneyOrRemoveBuildingFromSelectedPlayer(arguments);
		case SELECT_PLAYER_VIEW_ALL_PLAYERCARDS_AND_DISCARD_ONE:
			return new SelectPlayerViewAllPlayerCardAndDiscardOne(arguments);
		case SELECT_PLAYER_TO_GIVE_YOU_PLAYERCARDS:
			return new SelectPlayerToGiveYouPlayerCards(arguments);
		case LOOK_AT_PLAYER_CARDS_AND_STEAL_ONE:
			return new LookAtPlayerCardsAndStealOneCard(arguments);
		case DRAW_PLAYER_CARDS_FROM_DISCARD_PILE:
			return new DrawPlayerCardFromDiscardPile(arguments);
		case TAKE_MONEY_FOR_EACH_BUILDING:
			return new TakeMoneyForEachBuilding(arguments);
		case STEAL_MONEY_FROM_ALL_PLAYERS:
			return new StealMoneyFromAllPlayers(arguments);
		case STEAL_MONEY_FROM_PLAYER_CHOICE:
			return new StealMoneyFromPlayerChoice(arguments);
		case PICKUP_PLAYER_CARDS:
			return new PickupPlayerCards(arguments);
		case PLACE_BUILDING_HALF_PRICE:
			return new PlaceBuildingHalfPrice(arguments);
		case CHANGE_PERSONALITY_CARD:
			return new ChangePersonalityCard(arguments);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}

}
