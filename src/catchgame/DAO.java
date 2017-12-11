package catchgame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO
{
	// for DB connection
		public static String url = "jdbc:sqlite:catch.db";
		public static Connection connection = null;
		
		/**
		 * use to close connection after accessing database
		 */
		protected static void closeConnection()
		{
			try
			{
				if (connection != null)
				{
					connection.close();
				}
			}
			catch (SQLException e)
			{
				System.out.print(e.getMessage());
				e.printStackTrace();
			}
		}
		
		/**
		 * use to open connection prior to accessing database
		 */
		protected static void openConnection()
		{
			try
			{
				if (connection == null || connection.isClosed())
				{
					connection = DriverManager.getConnection(url);
				}
			}
			catch (SQLException e)
			{
				System.out.print(e.getMessage());
				e.printStackTrace();
			}
		}
	
	
}
