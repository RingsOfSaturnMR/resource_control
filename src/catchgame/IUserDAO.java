package catchgame;

import authentication.BadLoginException;
import authentication.NewUserException;
import authentication.User;

/**
 * This interface states which methods the DAO is to implement. This gives us
 * the advantage of being able to work on the program now and have it function as it
 * will, when the database is actually implemented.
 * 
 * @author Nils
 *
 */
public interface IUserDAO
{
	// TODO this method will never return false, because it throws an exception. Consider refactoring to be void. 
	boolean isValidUser(String userName, String password) throws BadLoginException;

	void createUser(String userName, String enteredPassword, String enteredPasswordConfirm) throws NewUserException;

	User getUser(String enteredUserName, String enteredPassword) throws BadLoginException;

	boolean usernameIsAvailable(String enteredUserName);

	int getNumberOfUsers();
}

