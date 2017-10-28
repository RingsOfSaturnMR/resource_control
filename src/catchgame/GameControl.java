package catchgame;

import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import resources.Boat;
import resources.BoatTypes;
import resources.Equipment;
import resources.Fish;
import resources.FishSpecies;
import resources.SeaCreature;
import userinterface.GamePane;
import userinterface.LoginPane;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import catchgame.Catch.LoginPacket;
import catchgame.Catch.SeaCreaturePacket;
import catchgame.Catch.SeaCreatureRequestPacket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameControl
{
	private Player player = null;

	// constants
	private static final int INITIAL_WIDTH = 500;
	private static final int INITIAL_HEIGHT = 500;

	// GUI stuff
	private GamePane gamePane;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// server communication stuff
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	private Random rand = new Random();

	public GameControl(String serverIPAdress, int port, String enteredName, String enteredPassword) throws Exception
	{
		// set sockets and streams
		try
		{
			socket = new Socket(serverIPAdress, port);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());

			// make a LoginPacket and send to server
			LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
			toServer.writeObject(loginPacket);
			toServer.flush();

			// get response from server
			ServerCodePacket serverCode = (ServerCodePacket) fromServer.readObject();

			if (serverCode.SERVER_CODE != ServerCodeConstants.LOGIN_SUCCESS_CODE)
			{
				throw new Exception("Invalid Name or Password");
			}
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println(ex.toString());
		}

		// if no exception was thrown, the player is authenticated.
		// TODO, actually get the file with the players previous state from the server
		setPlayer();

		// Display GUI
		gamePane = new GamePane(new ExtractFishAction(), new SellFishAction(), player);
		gameScene = new Scene(gamePane, INITIAL_WIDTH, INITIAL_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();

		// general logic for getting resources from server, this is pretty much going to
		// be an extraction method
		try
		{
			for (int i = 0; i < 5; i++)
			{
				SeaCreatureRequestPacket request = new SeaCreatureRequestPacket(ServerCodeConstants.REQUEST_COD_CODE);

				toServer.writeObject(request);
				SeaCreaturePacket creaturePacket = (SeaCreaturePacket) fromServer.readObject();

				SeaCreature creature = creaturePacket.creature;

				System.out.println("The server extracted a " + creature.getSpecies());
				creature.SetBodyByWeight();

				gamePane.simpleFishingPane.addCreature(creature);
			}

		}
		catch (IOException | ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}

	}

	// TODO, get the actual player from a file, or fetch from server
	private void setPlayer()
	{
		// STUB
		ArrayList<SeaCreature> resources = new ArrayList<>();
		resources.add(new Fish(FishSpecies.COD, 12));

		ArrayList<Equipment> tools = new ArrayList<>();
		tools.add(new Boat(BoatTypes.FISHING_SKIFF));

		this.player = new Player("Fred", "pass", 12.0, resources, tools);
	}

	private class ExtractFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Extract fish action " + "triggered(fish caught)");

		}

	}

	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Sell Fish action triggered(fish sold to market)");
		}
	}
}
