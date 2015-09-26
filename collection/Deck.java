package ankhmorpork.collection;

import java.util.ArrayList;

import ankhmorpork.model.Card;
import ankhmorpork.util.Environment;

/**
 * Deck of card
 * @param <T> Type of card in the deck
 * @see Card
 * @author Team 2
 * @since Build 1
 */
@SuppressWarnings("serial")
public class Deck<T> extends ArrayList<T>
{
	// http://forum.codecall.net/topic/59840-encapsulation-question-regarding-arraylists/?p=578711
	/**
	 * Create an empty deck
	 */
	public Deck()
	{
		
	}
	
	/**
	 * Pick the top (last) card of the deck and remove it from the deck
	 * @return The top card in the deck
	 */
	public T pickTopCard()
	{
		// No card is returned from an empty deck
		if (this.isEmpty())
		{
			return null;
		}
		
		// Select the last card and remove it from the deck
		int lastCardIndex = this.size() - 1;
		T card = this.get(lastCardIndex);
		this.remove(lastCardIndex);
		
		// Return the card selected
		return card;
	}
	
	/**
	 * Pick a card by its ID and remove it from the deck
	 * @param id The ID of the wanted card
	 * @return The top card in the deck with the given ID (or null if the deck is empty or the card is not found)
	 */
	public T pickCardByID(int id)
	{
		// No card is returned from an empty deck
		if (this.isEmpty())
		{
			return null;
		}
		
		// Filter the deck by the cards' ID (check that the deck contains cards)
		T selectedCard = null;
		for(T obj : this)
		{
			if(obj instanceof Card && ((Card)obj).getId() == id)
			{
				// Take the top card that matches the ID (all IDs should be unique) and remove it from the deck
				selectedCard = obj;
				this.remove(selectedCard);
				break;
			}
		}

		// Return the card selected
		return selectedCard;
	}
	
	/**
	 * Add the given card to the top of the deck (last card)
	 * @param card The card to be added on top of the deck
	 */
	public void addCard(T card)
	{
		if (card != null)
		{
			this.add(card);
		}
	}
	
	/**
	 * Shuffle the cards in the deck pair by pair a default number of times
	 */
	public void shuffleDeck()
	{
		shuffleDeck(Environment.DEFAULT_SHUFFLE_TIMES);
	}
	
	/**
	 * Shuffle the cards in the deck pair by pair a given number of times
	 * @param times The number of times a random pair of cards should be shuffled 
	 */
	public void shuffleDeck(int times)
	{
		int size = this.size();
		
		// Only shuffle if there are more than one card
		if (size < 2)
		{
			return;
		}
		
		// Select a random pair of cards and swap them a given number of times
		for(int i = 0; i < times; i++) 
		{
			// We pick 2 cards to swap, after picking the first one, the size is smaller thus -2
			int index1 = Environment.randInt(0, size - 1);
			int index2 = Environment.randInt(0, size - 2);
			
			if (index2 >= index1)
			{
				// if the 2nd index is bigger or equal to the first index,
				// we must add +1 for the index1 card that was removed
				index2++;
			}

			// Swap cards
			T card = this.get(index1);
			this.set(index1, this.get(index2));
			this.set(index2, card);
		}
	}
}
