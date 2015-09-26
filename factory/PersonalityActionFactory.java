package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.personality.*;
import ankhmorpork.util.type.PersonalityEventXML;

public class PersonalityActionFactory extends AbstractSubActionFactory 
{

	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(PersonalityEventXML.reverseXMLtoEnum(actionType), arguments);
	}

	public Action CreateAction(PersonalityEventXML actionType, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case CHRYSOPRASE__MUCH_ASSETS:
			return new MuchAssets(arguments);
		case COMMANDER_VIMES__NO_PLAYER_CARD:
			return new NoPlayerCard(arguments);
		case DRAGON_KING_OF_ARMS__MUCH_TROUBLE:
			return new MuchTrouble(arguments);
		case LORD_DEWORDE_SELACHII_RUST__AREA_CONTROL:
			return new AreaControl(arguments);
		case LORD_VETINARI__AREA_SPY:
			return new AreaSpy(arguments);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}

}
