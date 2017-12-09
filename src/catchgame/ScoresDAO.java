/*package catchgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import authentication.Authenticator;

public class ScoresDAO
{
	public ScoresDAO()
	{
		String createQuery = "CREATE TABLE IF NOT EXISTS HighScores" +
				" (\n" + "userName varchar(" +
				Authenticator.MAX_NAME_LENGTH +
				") not null, \n" +
				"passwordCipher varchar(" +
				Authenticator.MAX_PW_LENGTH +
				") not null, \n" +
				"filePath varchar(" +
				FILE_PATH_LENGTH +
				") not null, \n" +
				"isOnline bit not null," +
				"PRIMARY KEY (userName)" +
				");";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement())
		{
			stmt.execute(createQuery);
		}
	}
}
*/