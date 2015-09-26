package ankhmorpork.datainput;

import ankhmorpork.model.Player;

public class AIDataInput extends ConsoleDataInput
{
	public AIDataInput(Player player) 
	{
		super(player, true);
		//super.getInput().Add("Set a keyboard answer ^^");
	}
}
