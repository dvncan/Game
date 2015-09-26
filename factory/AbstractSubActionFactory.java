package ankhmorpork.factory;

import java.util.List;

import ankhmorpork.model.Action;

public abstract class AbstractSubActionFactory 
{
	public abstract Action CreateAction(String actionType2, List<String> arguments) throws Exception;
}
