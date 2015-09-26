package ankhmorpork.model;

import ankhmorpork.collection.Deck;
import ankhmorpork.model.Card;

/**
 * Abstract actor in the game that can own pieces and money
 * @see Player
 * @see Board
 * @author Team 2
 * @since Build 1
 */
public abstract class Actor
{
	protected Deck<Card> playerCardDeck = new Deck<Card>();
	protected BankAccount bankAccount = new BankAccount(this);
	
	/**
	 * Default actor with no card and no money
	 */
	public Actor()
	{
		
	}
	
	/**
	 * Retrieve the actor's bank account
	 * @return the actor's bank account
	 */
	public BankAccount getBankAccount()
	{
		return bankAccount;
	}
	
	/**
	 * Retrieve the amount of money in the actor's bank account
	 * @return the money amount in the actor's bank account
	 */
	public int getMoneyAmount()
	{
		return bankAccount.getMoneyAmount();
	}
	
	/**
	 * Retrieve the actor's player card deck
	 * @return the deck of player cards
	 */
	public Deck<Card> getPlayerCardDeck()
	{
		return playerCardDeck;
	}
}
