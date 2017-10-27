package authentication;


public abstract class NewUserException extends Exception
{
	protected String message;
	
	public String getErrorMessage()
	{
		return this.message;
	}

}
