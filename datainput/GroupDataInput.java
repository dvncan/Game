package ankhmorpork.datainput;

import java.util.List;

import ankhmorpork.model.Player;

public class GroupDataInput extends AbstractDataInput
{
	private final List<Player> playerList;
	
	public GroupDataInput(List<Player> playerList)
	{
		this.playerList = playerList;
	}
	
	@Override
	public void playGame()
	{
		for(Player player : playerList)
		{
			player.getDataInput().playGame();
		}
	}

	@Override
	public void showError(String string) 
	{
		for(Player player : playerList)
		{
			player.getDataInput().showError(string);
		}
	}
	
	@Override
	public <T> T ask(String question, List<T> listObject)
	{
		throw new RuntimeException("Not Allowed");
	}
	
}
