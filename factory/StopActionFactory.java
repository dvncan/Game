package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.stopcard.*;
import ankhmorpork.util.type.StopEventXML;

public class StopActionFactory extends AbstractSubActionFactory 
{

	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(StopEventXML.reverseXMLtoEnum(actionType), arguments);
	}

	public Action CreateAction(StopEventXML actionType, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case STOP_MINION_MOVED_OR_REMOVED:
			return new StopMinionMovedRemoved(arguments);
		case STOP_MINION_REMOVED:
			return new StopMinionRemoved(arguments);
		case MOVE_MINION_REMOVED:
			return new MoveMinionRemoved(arguments);
		case STOP_TEXT:
			return new StopText(arguments);
		case STOP_TURN:
			return new StopTurnPickLastCardPlayed(arguments);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}

}
