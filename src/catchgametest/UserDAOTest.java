/*package catchgametest;

import java.io.IOException;
import java.sql.SQLException;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Documentation;

import authentication.BadLoginException;
import authentication.NewUserException;
import catchgame.Player;
import catchgame.UserDAO;
import javafx.collections.ObservableList;

public class UserDAOTest
{
	public static void main(String[] args)
	{
		try
		{
			UserDAO dao = new UserDAO();
			
			dao.createUser("Thomas1", "Pass123$", "Pass123$");
			dao.createUser("Thomas2", "Pass123$", "Pass123$");
			dao.createUser("Thomas3", "Pass123$", "Pass123$");
			dao.createUser("Thomas4", "Pass123$", "Pass123$");
			
			
			Player p = dao.getUser("Thomas1", "Pass123$");
			Player p1 = dao.getUser("Thomas2", "Pass123$");
			Player p2 = dao.getUser("Thomas3", "Pass123$");
			Player p3 = dao.getUser("Thomas4", "Pass123$");
			
			ObservableList list = dao.getOnlinePlayerList();
			
			for(int i = 0; i < list.size(); i++)
			{
				System.out.println(list.get(i));
			}
			
			dao.logPlayerOut(p);
			dao.logPlayerOut(p1);
			dao.logPlayerOut(p2);
			//dao.logPlayerOut(p3);
			
			for(int i = 0; i < list.size(); i++)
			{
				System.out.println(list.get(i));
			}
			
		}
		catch (SQLException | NewUserException | IOException | BadLoginException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
*/