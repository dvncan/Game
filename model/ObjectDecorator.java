package ankhmorpork.model;

/**
 * Used as a decorator to change the toString() value sent to the console when asking user a question
 * This is helpful in case you send a player object for different reason and want to send different message
 * @author Pascal
 * @param <T> The original object that we want to change it behavior for the toString()
 */
public class ObjectDecorator<T>
{
	private final T object;
	private final String message;
	public ObjectDecorator(T object, String message)
	{
		this.object = object;
		this.message = message;
	}
	
	/**
	 * @return the original object
	 */
	public T getObject()
	{
		return this.object;
	}
	
	/**
	 * Show a custom message for this object
	 */
	@Override
	public String toString()
	{
		return this.message;
	}
}
