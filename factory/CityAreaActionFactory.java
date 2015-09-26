package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.cityarea.*;
import ankhmorpork.util.type.CityEventXML;

public class CityAreaActionFactory extends AbstractSubActionFactory
{

	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(CityEventXML.reverseXMLtoEnum(actionType), arguments);
	}

	public Action CreateAction(CityEventXML actionType, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case DOLLY_DIMWELL__PAY_TO_ADD_MINION_NEIGHTBORHOOD:
			return new PayAddNeighborhoodMinion(arguments);
		case HIPPO_DRAGON_NAP_SLEEPERS_LONGWALL__EARN_MONEY:
			return new EarnMoney(arguments);
		case ISLE_GOD__PAY_TO_REMOVE_TROUBLE:
			return new PayRemoveTrouble(arguments);
		case SMALL_GODS__STOP_RANDOM_EVENT:
			return new StopRandomEventLandmarkAffected(arguments);
		case THE_SHADES__ADD_NEIGHBORHOOD_TROUBLE:
			return new AddNeighborhoodTrouble(arguments);
		case THE_SCOURS__DISCARD_PLAYER_CARD_FOR_MONEY:
			return new DiscardPlayerCardForMoney(arguments);
		case UNREAL_ESTATE__PICK_DISCARD_PLAYER_CARD:
			return new PickAndDiscardPlayerCard(arguments);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}
}
