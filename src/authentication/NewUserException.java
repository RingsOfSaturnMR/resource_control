package authentication;

/**
 * This class is base class for different types of exceptions that can be thrown
 * when making a new account.
 * 
 * @author Nils Johnson
 */

public abstract class NewUserException extends Exception
{
	// TODO - set this up use the built in method for messages.
	protected String message;

	public String getErrorMessage()
	{
		return this.message;
	}

}
