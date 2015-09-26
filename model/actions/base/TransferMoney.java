package ankhmorpork.model.actions.base;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.actions.BasicAction;

/**
 * Transfer money action
 * @author Team 2
 * @since Build 2
 */
public class TransferMoney extends BasicAction
{
	public enum TransferMoneyType
	{
		fromBank,
		toBank
	}

	private final int amount;
	private final TransferMoneyType typeTransfer;
	private final boolean isFullAmountRestriction;

	/**
	 * Transfer money action
	 * @param typeTransfer type of transfer
	 * @param amount to transfer
	 * @param isFullAmountRestriction true if must transfer the full amount, false if can transfer partial amount without action failure
	 */
	public TransferMoney(TransferMoneyType typeTransfer, int amount, boolean isFullAmountRestriction)
	{
		this.amount = amount;
		this.typeTransfer = typeTransfer;
		this.isFullAmountRestriction = isFullAmountRestriction;
	}

	/**
	 * Execute the action
	 * @param gameManager the game manager
	 * @return true if execution success, false if failure
	 */
	@Override
	public boolean execute(GameManager gameManager) 
	{
		if (amount < 0)
		{
			gameManager.getCurrentPlayer().getDataInput().showError("Cannot transfer a negative amount of money.");
			return false;
		}
		
		switch(this.typeTransfer)
		{
			case fromBank:
				return TransferFromBank(gameManager);
			case toBank:
				return TransferToBank(gameManager);
		}
		return false;
	}

	/**
	 * TransferToBank
	 * @param gameManager the game manager
	 * @return true if transfer success, false if failure
	 */
	private boolean TransferToBank(GameManager gameManager) 
	{
		boolean isTransfer = true;
		int amountToTake = this.amount;
		if(gameManager.getBoard().getMoneyAmount() < this.amount)
		{
			if(this.isFullAmountRestriction)
			{
				// not enough money saved by player
				gameManager.getCurrentPlayer().getDataInput().showError("Not enough money saved.");
				isTransfer = false;
			}
			else
			{
				amountToTake = gameManager.getCurrentPlayer().getBankAccount().getMoneyAmount();
			}
		}
		
		// if we are still allowed to transfer the money
		if(isTransfer)
		{
			//gameManager.getDataInput().inform();
			isTransfer = gameManager.getCurrentPlayer().getBankAccount().transfertAmountTo(gameManager.getBoard(), amountToTake, isFullAmountRestriction);
			gameManager.getCurrentPlayer().getDataInput().printMessage(amountToTake + "$ paid to bank.");
		}
		
		return isTransfer;
	}
	
	/**
	 * TransferFromBank
	 * @param gameManager the game manager
	 * @return true if transfer success, false if failure
	 */
	private boolean TransferFromBank(GameManager gameManager)
	{
		boolean isTransfer = true;
		int amountToTake = this.amount;
		if(gameManager.getBoard().getMoneyAmount() < this.amount)
		{
			if(this.isFullAmountRestriction)
			{
				// not enough money left in the bank
				gameManager.getCurrentPlayer().getDataInput().showError("Not enough money left in the bank.");
				isTransfer = false;
			}
			else
			{
				amountToTake = gameManager.getBoard().getMoneyAmount();
			}
		}
		
		// if we are still allowed to transfer the money
		if(isTransfer)
		{
			//gameManager.getDataInput().inform();
			isTransfer = gameManager.getBoard().getBankAccount().transfertAmountTo(gameManager.getCurrentPlayer(), amountToTake, isFullAmountRestriction);
			gameManager.getCurrentPlayer().getDataInput().printMessage(amountToTake + "$ taken from bank.");
		}
		
		return isTransfer;
	}
	
	/**
	 * Action description
	 * @return description
	 */
	@Override
	public String toString()
	{
		return "Collect " + amount + "$";
	}
}
