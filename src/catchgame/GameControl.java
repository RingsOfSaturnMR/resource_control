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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameControl
{
	ClientSubOcean clientSubOcean;
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
		socket = new Socket(serverIPAdress, port);
		ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

		// make a LoginPacket and send to server
		LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
		toServer.writeObject(loginPacket);

		try
		{
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

		clientSubOcean = new ClientSubOcean(toServer, fromServer);
		UpdateFishOnScreenTask updateFishOnScreenTask = new UpdateFishOnScreenTask();
		new Thread(updateFishOnScreenTask).start();

		// Display GUI
		gamePane = new GamePane(new ExtractFishAction(), new SellFishAction(), player);
		gameScene = new Scene(gamePane, INITIAL_WIDTH, INITIAL_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();

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
			try
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						System.out.println(gamePane.simpleFishingPane.getChildren().remove(clientSubOcean.codPopulation.get(clientSubOcean.codPopulation.size() - 1).GUICircle));
						try
						{
							clientSubOcean.extractFish(clientSubOcean.codPopulation);
						}
						catch (Exception ex)
						{
							System.out.println(ex.toString());
						}

						UpdateFishOnScreenTask updateFishOnScreenTask = new UpdateFishOnScreenTask();
						new Thread(updateFishOnScreenTask).start();
					}
				});
			}
			catch (Exception ex)
			{
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

	class UpdateFishOnScreenTask implements Runnable
	{
		public void run()
		{
			// System.out.println("before");
			ArrayList<Fish> codUpdate;
			// System.out.println("get from server");
			codUpdate = getUpdateSeaCreaturesPacketFromServer();
			// System.out.println("got from server");
			clientSubOcean.addPacketOfCod(codUpdate);
			givePacketGUI(codUpdate);
			// System.out.println("added cod");
		}
	}

	public ArrayList<Fish> getUpdateSeaCreaturesPacketFromServer()
	{
		// System.out.println("before getting size");
		SubOceanFishStatePacket codStatePacket = new SubOceanFishStatePacket(clientSubOcean.codPopulation.size(), 100);
		// System.out.println("after getting size");
		try
		{
			toServer.writeObject(codStatePacket);
			System.out.println("Sent codStatePacket");
			// System.out.println("before getting fish packet");
			FishPacketsPacket fishPacketPacket = (FishPacketsPacket) fromServer.readObject();
			// System.out.println("after getting fish packet");
			return fishPacketPacket.codPopulation;
		}
		catch (IOException ex)
		{
			System.out.println(ex.toString());
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println(ex.toString());
			System.out.println("Over here");
		}
		return new ArrayList<Fish>();
	}

	public void givePacketGUI(ArrayList<Fish> fishUpdate)
	{
		for (int i = 0; i <= fishUpdate.size() - 1; i++)
		{
			makeSeaCreatureGUI(fishUpdate.get(i));
		}
	}

	public void makeSeaCreatureGUI(Fish fish)
	{
		fish.GUICircle = new Circle(5);
		System.out.print(fish.GUICircle.toString());
		double width = gamePane.simpleFishingPane.getMinWidth();
		double height = gamePane.simpleFishingPane.getMinHeight();
		fish.GUICircle.setCenterX(getRandomDouble(0, width));
		fish.GUICircle.setCenterY(getRandomDouble(100, height));
		System.out.print(fish.GUICircle.toString());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				gamePane.simpleFishingPane.getChildren().add(fish.GUICircle);
				// gamePane.getChildren().addAll(fish.GUICircle);
			}
		});
		// gamePane.getChildren().addAll(fish.GUICircle);
	}

	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max) + min;
		return randomInt;
	}

	private double getRandomDouble(double max, double min)
	{
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}
}
