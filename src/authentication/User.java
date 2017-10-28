package authentication;

import java.io.Serializable;

/**
 * This class is meant to be used as a base class for users who are logging in
 * using the tools in the authentication class. It is serializable, but will not
 * store the users password as plain text
 * 
 * @author Nils
 *
 */
public class User implements Serializable
{

	private static final long serialVersionUID = 1L;
	protected String userName;
	private String pwCipherText;
	private transient String pwPlainText;

	public User(String userName, String pwCipherText)
	{
		this.userName = userName;
		this.pwCipherText = pwCipherText;

	}

	public User(User u)
	{
		this(u.userName, u.pwCipherText);
	}

	public User()
	{
		this.userName = null;
		this.pwCipherText = null;
	}

	public String getUsername()
	{
		return this.userName;
	}

	public String getPwCipherText()
	{
		return pwCipherText;
	}

	public void setPwPlainText(String pwPlainText)
	{
		this.pwPlainText = pwPlainText;
	}

	public String getPwPlainText()
	{
		return pwPlainText;
	}
}
