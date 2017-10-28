package catchgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import authentication.Authenticator;
import authentication.BadLoginException;
import authentication.BadPasswordException;
import authentication.BadUsernameException;
import authentication.EncryptionFilter;
import authentication.LoginError;
import authentication.NewUserException;
import authentication.PasswordError;
import authentication.User;
import authentication.UsernameError;

/**
 * 
 * @author Nils
 * This DAO is intended as a stub. It currently uses a serialized array list, written to a file to store user information.
 * It needs to be updated to a database.
 * The rest of the program shouldnt break as long as this class implements IUserDAO
 */
public class UserDAO implements IUserDAO
{
	// file for to store users
	private static final String FILE_NAME = "users.dat";
	private static File userListFile = new File(FILE_NAME);

	// structure to hold users
	private static ArrayList<User> usersList = null;

	@Override
	public boolean isValidUser(String enteredUserName, String enteredPassword) throws BadLoginException
	{

		// temp user and index variable to go through array list of users
		User temp = new User();
		String nameInFile, pwCipher;
		String decryptedPw = null;
		int i = 0;

		// load the user list to memory for indexing
		loadUserList();

		// if there are no users in the file, throw exception
		if (usersList.size() == 0)
		{
			BadLoginException e = new BadLoginException(LoginError.NO_USERS);
			throw (e);
		}
		// otherwise, start comparing users
		else
		{
			do
			{
				temp = usersList.get(i);
				nameInFile = temp.getUsername();
				pwCipher = temp.getPwCipherText();
				decryptedPw = EncryptionFilter.decrypt(pwCipher, enteredPassword);
				i++;
			} while (i < usersList.size() && !nameInFile.equals(enteredUserName));
		}

		// there is a match between the username and the password decrypted with the
		// password, return true
		if (temp.getUsername().equals(enteredUserName) && decryptedPw.equals(enteredPassword))
		{
			return true;
		}
		// otherwise, depending on the mismatch, throw an exception
		else
		{
			if (!temp.getUsername().equals(enteredUserName))
			{
				BadLoginException e = new BadLoginException(LoginError.USER_NOT_FOUND);
				throw (e);
			}
			else if (temp.getUsername().equals(enteredUserName) && !decryptedPw.equals(enteredPassword))
			{
				BadLoginException e = new BadLoginException(LoginError.INVALID_PASSWORD);
				throw (e);
			}
		}
		return false;
	}

	@Override
	public void createUser(String enteredUserName, String enteredPassword, String enteredPwConfirm) throws NewUserException
	// public static void makeNewAccount) throws NewUserException,
	// FileNotFoundException, IOException
	{
		// verifiy a legally formatted name is entered
		ArrayList<UsernameError> usernameErrorList = Authenticator.checkUsernameLegality(enteredUserName);

		if (usernameErrorList != null)
		{
			BadUsernameException e = new BadUsernameException(usernameErrorList);
			throw (e);
		}

		// if list hasnt been loaded, loads it.
		if (usersList == null)
		{
			loadUserList();
		}

		if (!usernameIsAvailable(enteredUserName))
		{
			ArrayList<UsernameError> errors = new ArrayList<>();
			errors.add(UsernameError.UNAVAILABLE);
			NewUserException e = new BadUsernameException(errors);
			throw (e);
		}

		// verify password legality
		ArrayList<PasswordError> pwErrorList = Authenticator.checkPasswordLegality(enteredPassword);

		// if pw was illegal and they match
		if (pwErrorList != null && enteredPassword.equals(enteredPwConfirm))
		{
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}
		// if pw was illegal and they don't match
		if (pwErrorList != null && !enteredPassword.equals(enteredPwConfirm))
		{
			pwErrorList.add(PasswordError.MIS_MATCH);
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}
		// if 'entredPassword' was legal but doesnt match the confirm
		if (!enteredPassword.equals(enteredPwConfirm))
		{
			pwErrorList = new ArrayList<>();
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}

		// puts user in array list of existing users
		usersList.add(new User(enteredUserName, EncryptionFilter.encrypt(enteredPassword, enteredPassword)));
		// writes the new list to the file
		writeUsersToFile();

		// TOOD put this not in accounttools
		// make a new file for that user
		File pwListFile = new File(enteredUserName + ".dat");
		try
		{
			pwListFile.createNewFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public User getUser(String enteredUserName, String enteredPassword) throws BadLoginException
	{
		if (usersList == null)
		{
			loadUserList();
		}

		boolean foundUser = false;
		int i = 0;
		if (isValidUser(enteredUserName, enteredPassword))
		{
			// this block gets the index of the user
			do
			{
				if (usersList.get(i).getUsername().equals(enteredUserName))
				{
					foundUser = true;
				}
				else
				{
					i++;
				}
			} while (foundUser == false);
			return usersList.get(i);
		}
		else
		{
			return null;
		}
	}

	private static void loadUserList()
	{
		if (userListFile.length() > 0)
		{
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(userListFile)))
			{
				try
				{
					usersList = new ArrayList<User>((ArrayList<User>) (input.readObject()));
				}
				catch (ClassNotFoundException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if (usersList == null)
		{
			usersList = new ArrayList<User>();
		}
	}

	@Override
	public boolean usernameIsAvailable(String enteredUserName)
	{
		String nameInFile;
		int i = 0;
		while (i < usersList.size())
		{
			nameInFile = usersList.get(i).getUsername();
			if (nameInFile.equals(enteredUserName))
			{
				return false;
			}
			i++;
		}
		return true;

	}
	
	@Override
	public int getNumberOfUsers()
	{
		if (usersList == null)
		{
			loadUserList();
		}
		return usersList.size();
	}
	
	private static void writeUsersToFile()
	{
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(userListFile, false));)
		{
			output.writeObject(usersList);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
