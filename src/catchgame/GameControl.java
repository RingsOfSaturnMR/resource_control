package catchgame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import resources.Fish;
import userinterface.GamePane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.FishPacketsPacket;
import catchgame.Packets.LoginPacket;
import catchgame.Packets.ResultPacket;

/**
 * This class is a client that lets users extract resources, and drives the GUI
 * the gameplay.
 * 
 * @author Nils Johnson
 * @author Matt Roberts
 *
 */
public class GameControl
{
	private Player player = null;

	// GUI stuff
	private GamePane gamePane;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// server communication stuff
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	private ClientSubOcean clientSubOcean;

	private Random rand = new Random();

	/**
	 * Starts a new game
	 * 
	 * @param toServer The ObjectOutputStream to the server.
	 * @param fromServer The ObjectInputStream from the server.
	 */
	public GameControl(String serverIpAddress, int clientPort, String enteredName, String enteredPassword) throws Exception
	{
		// set the socket
		this.socket = new Socket(serverIpAddress, clientPort);

		// set the streams
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());

		// authenticate player
		logPlayerIn(enteredName, enteredPassword);

		
		// sequence for closing down
		gameStage.setOnCloseRequest(e ->
		{
			saveGame();
			logOut();
		});
		
		// update fish stuff - put this elsewhere? like, when you actually start fishing, call this.
		// also, I dont think we need any new threads for this
		clientSubOcean=new ClientSubOcean(toServer, fromServer);
		UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
		new Thread(updateFishOnScreenTask).start();
		
		

		// Display GUI
		gamePane = new GamePane(new SellFishAction(), player);
		gameScene = new Scene(gamePane, Constants.INITIAL_GAME_PANE_WIDTH, Constants.INITIAL_GAME_PANE_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();

	}

	/**
	 * Takes user's name and password and logs them in, or throws an 
	 * exception that will make GameControl pass out of scope and get 
	 * propagated back to where it was instantiated.
	 * @param enteredName
	 * @param enteredPassword
	 * @throws Exception
	 */
	private void logPlayerIn(String enteredName, String enteredPassword) throws Exception
	{
		// send login info to server
		LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
		toServer.writeObject(loginPacket);

		// get response
		ResultPacket resultPacket = (ResultPacket) fromServer.readObject();

		// if the response code is not 'LOGIN_SUCCESS_CODE'
		if (resultPacket.code != Codes.LOGIN_SUCCESS_CODE)
		{
			String errorMessage;

			switch (resultPacket.code)
			{
			case Codes.LOGIN_ERR_INVALID_PASSWORD_CODE:
				errorMessage = "Invalid Password";
				break;
			case Codes.LOGIN_ERR_NO_USERS_FOUND_CODE:
				errorMessage = "This Server Has No Users Yet!";
				break;
			case Codes.LOGIN_ERR_USER_NOT_FOUND_CODE:
				errorMessage = "Invalid Username";
				break;
			case Codes.LOGIN_ERR_INVALID_ATTEMPT_CODE:
				errorMessage = "Invalid Attempt";
				break;
			case Codes.LOGIN_ERR_UNKNOWN_ERROR_CODE:
				errorMessage = "The Server Doesnt Know Why You Cant Login :(";
				break;
			default:
				errorMessage = "The server responded '" + resultPacket.code +
						"', I dont know what that means :(";
				break;
			}

			throw new Exception(errorMessage);
		}
		// if the response code is 'LOGIN_SUCCESS_CODE, expect to recieve the user's
		// Object'
		else if (resultPacket.code == Codes.LOGIN_SUCCESS_CODE)
		{
			System.out.println("Response to login request: " + resultPacket.code +
					", aka 'LOGIN_SUCCESS_CODE' - Waiting to recieve user's Player object");
			this.player = (Player) fromServer.readObject();
			System.out.println("returned players name: " + player.getUsername());
		}

	}

	/**
	 * Action that occurs when a fish is sold.
	 */
	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Sell Fish action triggered(fish sold to market)");
		}
	}

	/**
	 * Requests new SeaCreatures from Server, and adds them to the screen
	 * if any or sent
	 */
	class UpdateFishOnScreenTask implements Runnable
	{

		public void run()
		{
			// System.out.println("before");
			ArrayList<Fish> codUpdate;
			// System.out.println("get from server");
			codUpdate = getUpdateSeaCreaturesPacketFromServer();

			clientSubOcean.currentPopulationCod += codUpdate.size();
			// System.out.println("got from server");
			// clientSubOcean.addPacketOfCod(codUpdate);
			addFishPacketToScreen(codUpdate, 0, 0, Color.BLUE);
			// givePacketGUI(codUpdate);
			// System.out.println("added cod");
		}
	}

	/**
	 * Actually makes the request for new SeaCreatures from the server
	 * and sends a new ClientSubOceanSeaCreatureStatePacket so that
	 * the server can no what to send, listens for SeaCreatures sent 
	 * from server and returns any
	 * 
	 * @return The SeaCreatures sent by the server
	 */
	public ArrayList<Fish> getUpdateSeaCreaturesPacketFromServer()
	{
		// System.out.println("before getting size");
		ClientSubOceanSeaCreatureStatePacket codStatePacket = new ClientSubOceanSeaCreatureStatePacket(clientSubOcean.currentPopulationCod, clientSubOcean.maxPopulationCod);
		// System.out.println("after getting size");
		try
		{
			toServer.writeObject(codStatePacket);
			// System.out.println("Sent codStatePacket");
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
			// System.out.println("Over here");
		}
		return new ArrayList<Fish>();
	}

	// should be put in SimpleFishingPane
	/**
	 * Adds fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param fishUpdate the fish to add to the screen
	 * @param topOffset the highest the fish can be on the screen
	 * @param bottomOffset the lowest the fish can be on the screen
	 * @param color the color the fish should be given
	 */
	public void addFishPacketToScreen(ArrayList<Fish> fishUpdate, int topOffset, int bottomOffset, Color color)
	{
		for (int i = 0; i <= fishUpdate.size() - 1; i++)
		{
			addFishToScreen(fishUpdate.get(i), topOffset, bottomOffset, color);
		}
	}

	// should be put in SimpleFishingPane
	/**
	 * Adds an individual fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param fish the fish to add to the screen
	 * @param topOffset the highest the fish can be on the screen
	 * @param bottomOffset the lowest the fish can be on the screen
	 * @param color the color the fish should be given
	 */
	public void addFishToScreen(Fish fish, int topOffset, int bottomOffset, Color color)
	{
		fish.SetBodyByWeight();
		fish.setBodyColor(color);
		// System.out.print(fish.getBody().toString());
		double width = gamePane.simpleFishingPane.getMinWidth();
		double height = gamePane.simpleFishingPane.getMinHeight();
		fish.getBody().setCenterX(getRandomDouble(0, width));
		fish.getBody().setCenterY(getRandomDouble(150 + topOffset, height - bottomOffset));
		// System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				gamePane.simpleFishingPane.getChildren().add(fish.getBody());
				fish.getBody().setOnMouseClicked(new ExtractFishAction(fish));
			}
		});
	}

	/**
	* Action that occurs when a fish is caught.
	 */
	private class ExtractFishAction implements EventHandler<MouseEvent>
	{
		Fish fish;

		ExtractFishAction(Fish fish)
		{
			this.fish = fish;
		}

		@Override
		public void handle(MouseEvent e)
		{
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try
			{
				// Something like this here?
				player.addSeaCreatureToIceChest(fish);
				
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{

						gamePane.simpleFishingPane.getChildren().remove(fish.getBody());

						clientSubOcean.currentPopulationCod--;

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

	/**
	 * Returns random int in the specified range.
	 * @param min the minimum value for the range
	 * @param max the maximum value for the range
	 * @return A random int in the specified range
	 */
	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max) + min;
		return randomInt;
	}

	/**
	 * Returns random double in the specified range.
	 * @param min min the minimum value for the range
	 * @param max the minimum value for the range
	 * @return A random double in the specified range
	 */
	private double getRandomDouble(double max, double min)
	{
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}

	/**
	 * This method sends the player object back to the server, where it is saved as
	 * a file.
	 */
	public void saveGame()
	{
		try
		{
			toServer.writeObject(player);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * this method sends a request to the server to remove the player from the list
	 * of active users, and set the DB to have him as offline.
	 */
	private void logOut()
	{
		try
		{
			// TODO
			toServer.writeObject(new Packets.RequestPacket(Codes.LOGOUT_REQUEST_CODE));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
