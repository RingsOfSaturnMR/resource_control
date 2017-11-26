package catchgame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.GamePane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import catchgame.Packets.LoginPacket;
import catchgame.Packets.ResultPacket;

/**
 * This class is a client that lets users start FishingActivities, and controls
 * the flow of the game.
 * 
 * @author Nils Johnson
 *
 */
public class GameControl
{
	private Player player = null;

	// GUI stuff
	private GamePane gamePane;
	FishingActivity fishingActivity;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// server communication stuff
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

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
			try
			{
				saveGame();
				logOut();
				toServer.close();
				fromServer.close();
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally
			{
				toServer = null;
				fromServer = null;
			}

		});

		// Display GUI
		gamePane = new GamePane(new SellFishAction(), player, new FishingActivityActions());
		gameScene = new Scene(gamePane, Constants.INITIAL_GAME_PANE_WIDTH, Constants.INITIAL_GAME_PANE_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();

	}

	/**
	 * Takes user's name and password and logs them in, or throws an exception that
	 * will make GameControl pass out of scope and get propagated back to where it
	 * was instantiated.
	 * 
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
	 * This method sends the player object back to the server, where it is saved as
	 * a file.
	 */
	public void saveGame() throws Exception
	{

		toServer.writeObject(player);
	}

	/**
	 * this method sends a request to the server to remove the player from the list
	 * of active users, and set the DB to have him as offline.
	 */
	private void logOut() throws Exception
	{

		toServer.writeObject(new Packets.RequestPacket(Codes.LOGOUT_REQUEST_CODE));
	}

	public class FishingActivityActions
	{

		public void startFishingActivity()
		{
			fishingActivity = new FishingActivity(gamePane, toServer, fromServer, player);
		}
	}

}