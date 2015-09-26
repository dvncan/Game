package ankhmorpork.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ankhmorpork.model.Action;
import ankhmorpork.util.type.CardType;

public class ActionFactory 
{
	private Map<CardType, AbstractSubActionFactory> factoryMap = new HashMap<CardType, AbstractSubActionFactory>();
	
	/**
	 * 
	 * @param actionType
	 * @param actionType2
	 * @param arguments
	 * @return
	 */
	public Action CreateAction(String actionType, String actionType2, List<String> arguments) throws Exception
	{
		return CreateAction(CardType.reverseXMLtoEnum(actionType), actionType2, arguments);
	}
	
	/**
	 * 
	 * @param actionType
	 * @param actionType2
	 * @param arguments
	 * @return
	 */
	public Action CreateAction(CardType actionType, String actionType2, List<String> arguments) throws Exception
	{
		if(actionType == null)
		{
			throw new RuntimeException("Invalid Action Type");
		}
		
		if(factoryMap.containsKey(actionType))
		{
			return factoryMap.get(actionType).CreateAction(actionType2, arguments);
		}
		
		AbstractSubActionFactory factory;
		switch(actionType)
		{
		case BASE:
			factory = new BaseActionFactory();
			break;
		case CITY:
			factory = new CityAreaActionFactory();
			break;
		case EVENT:
			factory = new RandomEventActionFactory();
			break;
		case PERSONALITY:
			factory = new PersonalityActionFactory();
			break;
		case SCROLL:
			factory = new ScrollActionFactory();
			break;
		default:
			throw new RuntimeException("Invalid Action Type");
		}

		factoryMap.put(actionType, factory);		
		return factory.CreateAction(actionType2, arguments);
	}
}
