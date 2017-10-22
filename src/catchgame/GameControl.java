package catchgame;

import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.GamePane;
import userinterface.LoginPane;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import catchgame.Catch.LoginPacket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameControl
{
	Player player = new Player();
	ClientSubOcean fishOnScreen;
	
	// constants
	private static final int INITIAL_WIDTH = 500;
	private static final int INITIAL_HEIGHT = 500;
	
	// GUI stuff
	private GamePane gamePane;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// server communication stuff;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	//public GameControl(String serverIpAddress, int clientPort, String enteredName, String enteredPassword) throws Exception
	public GameControl(ObjectOutputStream toServer, ObjectInputStream fromServer, Player loggedInPlayer)
	{
		fishOnScreen=new ClientSubOcean(toServer, fromServer);
		UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
		new Thread(updateFishOnScreenTask).start();
		//fishOnScreen.updateFishPopulationsFromServer();
		/*
		 could be nice to have a clientSubOcean//seaCreaturesOnScreen
		 it could contain all the seaCreatures available to the user
		 could be subclassed for different fishing activities if necessary
		 */
		//I commented this out because I think it belongs in catch,
		//and the game should handle once the person is logged in
		/*
		// make a loginPacket to send to server
		LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
		Socket socket = new Socket(serverIpAddress, clientPort);
		System.out.println("connected");
		
		try
		{
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new DataInputStream(socket.getInputStream());
		
		toServer.writeObject(loginPacket);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		int response =  fromServer.readInt();
		
		
		
		if (response == 1)
		{
			throw new Exception("Invalid Login Credentials - Server Response: " + response);
		}
		else if (response == 0)
		{
			System.out.println("Server Response: " + response);
			// set streams to null until they're needed again
			ObjectOutputStream toServer = null;
			DataInputStream fromServer = null;
		}
		*/
		

		// Display GUI
		gamePane = new GamePane(new ExtractFishAction(), new SellFishAction(), player);
		gameScene = new Scene(gamePane, INITIAL_WIDTH, INITIAL_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();
	
	}

	private class ExtractFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Extract fish action "
					+ "triggered(fish caught)");
			try{
			fishOnScreen.extractFish(fishOnScreen.codPopulation);
			UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
			new Thread(updateFishOnScreenTask).start();
			}
			catch (Exception ex){
				System.out.println(ex.toString());
			}
		};
	}

	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Sell Fish action triggered(fish sold to market)");
		};
	}
	
	class UpdateFishOnScreenTask implements Runnable{
		
		public void run(){
			fishOnScreen.updateFishPopulationsFromServer();
		}
	}
}
