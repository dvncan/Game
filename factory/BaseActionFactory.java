package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;
import ankhmorpork.model.actions.base.*;
import ankhmorpork.model.actions.base.TransferMoney.TransferMoneyType;
import ankhmorpork.util.type.ActionXML;

public class BaseActionFactory extends AbstractSubActionFactory
{
	private AbstractSubActionFactory scrollFactory = null;
	private AbstractSubActionFactory stopFactory = null;

	/**
	 * CreateAction
	 */
	@Override
	public Action CreateAction(String actionType, List<String> arguments) throws Exception
	{
		return CreateAction(ActionXML.reverseXMLtoEnum(actionType), arguments);
	}

	/**
	 * CreateAction
	 * @param actionType
	 * @param arguments
	 * @return
	 */
	public Action CreateAction(ActionXML actionType, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		switch(actionType)
		{
		case BUILDING:
			return new PlacePlayerBuilding();
		case CARD:
			return new PlayAnotherCard();
		case EVENT:
			return new PlayRandomEvent();
		case GOLD:
			if(arguments.size() != 1)
			{
				throw new RuntimeException("Invalid Gold Quantity");
			}
			return new TransferMoney(TransferMoneyType.fromBank, Integer.parseInt(arguments.get(0)), false);
		case GUARD:
			return new GuardArea();
		case KILL:
			return new AssassinateCharacter();
		case MINION:
			return new PlacePlayerMinion();
		case SCROLL:
			if(arguments.size() < 1)
			{
				throw new RuntimeException("Argument missing!");
			}
			
			if(scrollFactory == null)
			{
				scrollFactory = new ScrollActionFactory();
			}
			
			String action = arguments.get(0);
			List<String> args = arguments.subList(1, arguments.size());
			return scrollFactory.CreateAction(action, args);
		case STOP:
			if(arguments.size() < 1)
			{
				throw new RuntimeException("Argument missing!");
			}
			
			if(stopFactory == null)
			{
				stopFactory = new StopActionFactory();
			}

			String stopAction = arguments.get(0);
			List<String> stopArgs = arguments.subList(1, arguments.size());
			return stopFactory.CreateAction(stopAction, stopArgs);
		default:
			throw new RuntimeException("Invalid Action Type");
		}
	}
}
