package catchgame;

import java.io.FileNotFoundException;
import java.io.IOException;

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
	void createUser(String userName, String enteredPassword, String enteredPasswordConfirm) throws NewUserException, FileNotFoundException, IOException;
	
	void deleteUser(String username);
	
	User getUser(String enteredUserName, String enteredPassword) throws BadLoginException, FileNotFoundException, IOException;
	
	boolean usernameIsAvailable(String enteredUserName);
	
	int getNumberOfUsers();
}

