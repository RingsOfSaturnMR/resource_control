/*package catchgametest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

*//**
 *
 * @author sqlitetutorial.net
 *//*
public class TestClassDelte
{

	*//**
	 * Create a new table in the test database
	 *
	 *//*
	public static void createNewTable()
	{
		// SQLite connection string
		String url = "jdbc:sqlite:C://sqlite/db/tests.db";

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n" +
				"	id integer PRIMARY KEY,\n" +
				"	name text NOT NULL,\n" +
				"	capacity real\n" +
				");";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement())
		{
			// create a new table
			stmt.execute(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	*//**
	 * @param args the command line arguments
	 *//*
	public static void main(String[] args)
	{
		createNewTable();
	}

	// file for to store users private static final String FILE_NAME =
	"users.dat";

	private static File userListFile = new File(FILE_NAME);

	// structure to hold users private static ArrayList<User> usersList = null;

@Override public boolean isValidUser(String enteredUserName, String
	enteredPassword) throws BadLoginException {
	
	 // temp user and index variable to go through array list of users User temp =
	new User(); String nameInFile, pwCipher; String decryptedPw = null; int i =
	0;
	
	  // load the user list to memory for indexing loadUserList();
	
	  // if there are no users in the file, throw exception if (usersList.size() ==
	0) { BadLoginException e = new BadLoginException(LoginError.NO_USERS); throw
	  (e); } // otherwise, start comparing users else { do { temp =
	  	usersList.get(i); nameInFile = temp.getUsername(); pwCipher =
	  	temp.getPwCipherText(); decryptedPw = EncryptionFilter.decrypt(pwCipher,
	  		enteredPassword); i++; }while(i<usersList.size()&&!nameInFile.equals(enteredUserName));}

	// there is a match between the username and the password decrypted with the
	// password, return true if (temp.getUsername().equals(enteredUserName) &&
	decryptedPw.equals(enteredPassword)){return true;} // otherwise, depending

	on the mismatch,throw
	an exception else
	{
		if (!temp.getUsername().equals(enteredUserName))
		{
			BadLoginException e = new BadLoginException(LoginError.USER_NOT_FOUND);
			throw (e);
		}
		else if (temp.getUsername().equals(enteredUserName) &&
				!decryptedPw.equals(enteredPassword))
		{
			BadLoginException e = new BadLoginException(LoginError.INVALID_PASSWORD);
			throw (e);
		}
	}return false;
	}

	@Override public void createUser(String enteredUserName, String
	  enteredPassword, String enteredPwConfirm) throws NewUserException { //
				verifiy a legally formatted name is entered ArrayList<UsernameError>
				usernameErrorList = Authenticator.checkUsernameLegality(enteredUserName);
				
				if (usernameErrorList != null) { BadUsernameException e = new
					BadUsernameException(usernameErrorList); throw (e); }
					
	  // if list hasnt been loaded, loads it. if (usersList == null) {
					loadUserList(); }

	if(!usernameIsAvailable(enteredUserName)) { ArrayList<UsernameError> errors
						= new ArrayList<>(); errors.add(UsernameError.UNAVAILABLE); NewUserException
						e = new BadUsernameException(errors); throw (e); }
						
	  // verify password legality ArrayList<PasswordError> pwErrorList =
						Authenticator.checkPasswordLegality(enteredPassword);
						
	  // if pw was illegal and they match if (pwErrorList != null &&
						enteredPassword.equals(enteredPwConfirm)) { BadPasswordException e = new
	  BadPasswordException(pwErrorList); throw (e); } // if pw was illegal and the don't match if (pwErrorList != null &&
	  !enteredPassword.equals(enteredPwConfirm)) {
		pwErrorList.add(PasswordError.MIS_MATCH); BadPasswordException e = new
	  BadPasswordException(pwErrorList); throw (e); } // if 'entredPassword' was
	  legal but doesnt match the confirm if
	  (!enteredPassword.equals(enteredPwConfirm)) { pwErrorList = new
	  	ArrayList<>(); BadPasswordException e = new
	  	BadPasswordException(pwErrorList); throw (e); }
	  	
	  // puts user in array list of existing users usersList.add(new
	  	User(enteredUserName, EncryptionFilter.encrypt(enteredPassword,
	  enteredPassword))); // writes the new list to the file writeUsersToFile();
	
	  // TOOD put this not in accounttools // make a new file for that user File
	pwListFile = new File(enteredUserName + ".dat"); try {
		pwListFile.createNewFile();
		
		try (ObjectOutputStream output = new ObjectOutputStream(new
			FileOutputStream(pwListFile.getPath()));) { output.writeObject(new
			catchgame.Player(enteredPassword, enteredPassword, 0, null, null)); } catch
			(FileNotFoundException ex) { ex.printStackTrace(); } catch (IOException ex) {
				ex.printStackTrace(); }
				
	  } catch (IOException e) { // TODO Auto-generated catch block
	  	e.printStackTrace(); } }

	@Override public User getUser(String enteredUserName, String enteredPassword)
	  	throws BadLoginException { if (usersList == null) { loadUserList(); }
	  	
	  	boolean foundUser = false; int i = 0; if (isValidUser(enteredUserName,
	  enteredPassword)) { // this block gets the index of the user do { if
	  		(usersList.get(i).getUsername().equals(enteredUserName)) { foundUser = true;
	  		} else { i++; } } while (foundUser == false); return usersList.get(i); }else

	{
		return null;
	}
	}

	public Player getPlayer(String enteredUserName, String enteredPassword)
	  		throws BadLoginException { if (usersList == null) { loadUserList(); }
	  		
	  		boolean foundUser = false; int i = 0; if (isValidUser(enteredUserName,
	  enteredPassword)) { // this block gets the index of the user do { if
	  			(usersList.get(i).getUsername().equals(enteredUserName)) { foundUser = true;
	  			} else { i++; } } while (foundUser == false); return usersList.get(i); }else

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
				{ // TODO Auto-generated catch block
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

	private static void writeUsersToFile() { try (ObjectOutputStream output = new
	  					ObjectOutputStream(new FileOutputStream(userListFile, false));) {
	  output.writeObject(usersList); } catch (FileNotFoundException e) { // TODO
	  Auto-generated catch block e.printStackTrace(); } catch (IOException e) { //
	  	TODO Auto-generated catch block e.printStackTrace();
	  	System.out.println(e.getMessage()); } }

}*/