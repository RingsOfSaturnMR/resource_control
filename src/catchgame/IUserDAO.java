package catchgame;

import authentication.BadLoginException;
import authentication.NewUserException;
import authentication.User;

public interface IUserDAO
{
	boolean isValidUser(String userName, String password) throws BadLoginException;
	void createUser(String userName, String enteredPassword, String enteredPasswordConfirm) throws NewUserException;
	User getUser(String enteredUserName, String enteredPassword) throws BadLoginException;
	boolean isAvailable(String enteredUserName);
	int getNumberOfUsers();
}
