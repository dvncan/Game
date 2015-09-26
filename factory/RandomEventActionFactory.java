package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.randomevent.*;
import ankhmorpork.util.type.RandomEventXML;

public class RandomEventActionFactory extends AbstractSubActionFactory 
{

	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(RandomEventXML.reverseXMLtoEnum(actionType), arguments);
	}

	public Action CreateAction(RandomEventXML actionType, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case FIRE__SPREAD_BUILDING_FIRE:
			return new SpreadBuildingFire(arguments);
		case FOG__DISCARD_BOARD_PLAYER_CARD:
			return new DiscardBoardPlayerCard(arguments);
		case MURDERS__ALL_MURDER_MINION:
			return new AllPlayerMurderMinion(arguments);
		case RIOTS__END_GAME_TROUBLE:
			return new EndGameWithTrouble(arguments);
		case SUBSIDENCE__ALL_PAY_OR_LOSE_BUILDING:
			return new AllPlayerPayOrLoseBuilding(arguments);
		case TROLLS__ADD_RANDOM_AREA_TROLL:
			return new AddRandomAreaTroll(arguments);
		case JOHNSON__REMOVE_RANDOM_AREA_CARD_AND_MINION:
			return new RemoveRandomAreaCardAndMinion(arguments);
		case DEMONS__ADD_RANDOM_AREA_DEMONS:
			return new AddRandomAreaDemon(arguments);
		case DRAGON__REMOVE_RANDOM_AREA_LANDMARK:
			return new RemoveRandomAreaLandmark(arguments);
		case EARTHQUAKE_EXPLOSION__REMOVE_BUILDING:
			return new RemoveRandomAreaBuilding(arguments);
		case FLOOD__MOVE_FLOODED_MINION:
			return new MoveFloodedMinion(arguments);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}

}
