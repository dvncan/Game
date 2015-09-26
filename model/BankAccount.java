package ankhmorpork.model;

import ankhmorpork.datainput.AbstractDataInput;
import ankhmorpork.datainput.ConsoleDataInput;

/**
 * Bank account holding money and managing money transfer
 * @author Team 2
 * @since Build 1
 */
public class BankAccount
{
	private int moneyAmount;
	private Actor owner;
	private AbstractDataInput systemDataInput = new ConsoleDataInput(null, false);
	
	/**
	 * Default empty bank account
	 */
	public BankAccount(Actor owner)
	{
		this.moneyAmount = 0;
		this.owner = owner;
	}
	
	/**
	 * Initialize the bank with the amount of gold
	 * @param moneyAmount amount of gold to initialize bank account
	 */
	public BankAccount(Actor owner, int moneyAmount)
	{
		this.moneyAmount = moneyAmount;
		this.owner = owner;
	}

	/**
	 * Find the amount of money in the bank account
	 * @return the amount of money in the bank account
	 */
	public int getMoneyAmount()
	{
		return this.moneyAmount;
	}
	
	/**
	 * Transfer money to another bank account
	 * @param actor who has a bank account
	 * @param iQuantity the quantity to transfer to that bank account
	 * @param isFullAmountRestriction true if must transfer full amount false if can transfer as much as they can
	 * @return whether the transfer succeeded
	 */
	public boolean transfertAmountTo(Actor actor, int iQuantity, boolean isFullAmountRestriction)
	{
		boolean transfertSucceded = false;
		
		if(moneyAmount >= 0 && (moneyAmount >= iQuantity || !isFullAmountRestriction))
		{
			int transferAmount = moneyAmount < iQuantity ? moneyAmount : iQuantity;
			actor.bankAccount.moneyAmount += transferAmount;
			moneyAmount -= transferAmount;
			
			if(this.owner == null)
			{
				systemDataInput.printMessage(actor.toString() + " receives " + transferAmount + " coin" + (transferAmount > 1 ? "s" : "") + ".");
			}
			else
			{
				systemDataInput.printMessage(this.owner.toString() + " transfers " + transferAmount + " coin" + (transferAmount > 1 ? "s" : "") + " to " + actor.toString() + ".");
			}
			transfertSucceded = true;
		}
		
		return transfertSucceded;
	}
}
