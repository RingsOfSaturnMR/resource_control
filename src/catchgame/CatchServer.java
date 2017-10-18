package catchgame;

import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.GamePane;
import userinterface.ServerPane;

public class CatchServer
{
	private Stage serverStage=new Stage();
	
	public Ocean ocean = new Ocean();
	
	private ServerPane serverPane = new ServerPane();
	
	public CatchServer() {
		loadServerPane();
		serverPane.appendTaOutput("Server started.\n");
	}
	/*
	 server luanches
	 while true thread waiting for connections in a thread
	 
	 new thread from the while true that takes the connected socket as parameter
	 
	 there will be a thread for each client, and one thread for listening for new people who want to play
	 
	 ocean should get locked whenever a request is made
	 
	 
	 */
//	public class DAO
//	{
//		public String getUserByName() {
//			
//		}
//	}
	
	public void loadServerPane()
	{
		// the height and width
		int GAME_WIDTH = 400;
		int GAME_HEIGHT = 400;


		Scene gameScene = new Scene(serverPane, GAME_WIDTH, GAME_HEIGHT);

		// show GamePane
		serverStage.setScene(gameScene);
		serverStage.setTitle("Catch Server");
		serverStage.centerOnScreen();
		serverStage.show();
		serverStage.requestFocus();
	}
}
