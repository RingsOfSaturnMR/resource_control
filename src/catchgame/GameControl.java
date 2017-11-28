package catchgame;

/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/
/*
Purpose: logs in user or throws an exception, can save a game,
can log a player out, can load a GamePane, passes the 
sellFishAction to the gamePane, and the functor 
FishingActivityActions to the gamePane

Modification info:
can save a game, can log a player out, the FishingActivity 
code has been moved to FishingActivity (was just free floating in 
GameControl, and the functor and its passing have been added
*/

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import market.SeafoodMarket;
import resources.FishSpecies;
import userinterface.GamePane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.Random;
import catchgame.Packets.LoginPacket;
import catchgame.Packets.ResultPacket;
import catchgame.Packets.RequestPacket;

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

	// for user interface
	private GamePane gamePane;
	FishingActivity fishingActivity;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// for server
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	// for exchanginng resources for money
	SeafoodMarket market = new SeafoodMarket("The Fish Market");

	// so Catch.java can listen to what is happening in the game
	private SimpleBooleanProperty gameRunning = new SimpleBooleanProperty(false);

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
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exit");
			alert.setHeaderText("Exit Options");
			alert.setContentText("Choose your option.");

			ButtonType saveAndExit = new ButtonType("Save And Quit");
			ButtonType justExit = new ButtonType("Quit");

			alert.getButtonTypes().setAll(saveAndExit, justExit);

			Optional<ButtonType> result = alert.showAndWait();
			try
			{
				if (result.get() == saveAndExit)
				{
					saveGame();
					logOut();
				}
				else if (result.get() == justExit)
				{
					logOut();
				}

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
				gameRunning.set(false);

			}
		});

		// get markets
		SeafoodMarket seafoodMarket = new SeafoodMarket("Caileigh's Market");

		// set game to running
		gameRunning.set(true);

		// Display GUI
		gamePane = new GamePane(new SellFishAction(), player, new FishingActivityActions(), new DeleteAccountAction(), new SaveGameAction(), new ExitAction(), seafoodMarket);
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
	 * Action that occurs when user desires to exit.
	 */
	private class ExitAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			gameStage.fireEvent(new WindowEvent(gameStage, WindowEvent.WINDOW_CLOSE_REQUEST));
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
			System.out.println("Starting Transaction");

			boolean validTransaction = true;

			// input validation
			for (int i = 0; i < Constants.supportedSpecies.size(); i++)
			{
				String str = gamePane.marketsPane.getSpeciesTextFieldList().get(i).getText();

				if (str.equals(""))
				{
					str = "0";
				}

				int numToSell = Integer.parseInt(str);

				if (numToSell > player.getNumOf(Constants.supportedSpecies.get(i)))
				{
					gamePane.marketsPane.getSpeciesTextFieldList().get(i).setStyle("-fx-control-inner-background: red;");
					validTransaction = false;
				}
			}

			if (validTransaction)
			{
				for (int i = 0; i < Constants.supportedSpecies.size(); i++)
				{
					// reset to white
					gamePane.marketsPane.getSpeciesTextFieldList().get(i).setStyle("-fx-control-inner-background: white;");
					
					String str = gamePane.marketsPane.getSpeciesTextFieldList().get(i).getText();

					if (str.equals(""))
					{
						str = "0";
					}

					int numToSell = Integer.parseInt(str);

					for (int j = 0; j < numToSell; j++)
					{
						player.addMoney(market.sellItem(player.getSeaNextSeaCreature(Constants.supportedSpecies.get(i))));
					}
				}
				System.out.println("Transaction sucess");
			}
			else
			{
				System.out.println("Transaction didnt happen");
			}
			
		}
	}

	/**
	 * This method sends the player object back to the server, where it is saved as
	 * a file.
	 */
	public void saveGame() throws Exception
	{
		player.prepareToSerialze();
		toServer.writeObject(player);
	}

	/**
	 * Action that deletes a user account.
	 */
	private class DeleteAccountAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Delete This Account");
			alert.setContentText("Are you Sure? This cannot be undone.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK)
			{
				try
				{
					toServer.writeObject(new RequestPacket(Codes.DELETE_ACCOUNT_CODE));
					gameStage.fireEvent(new WindowEvent(gameStage, WindowEvent.WINDOW_CLOSE_REQUEST));
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
			{
				return;
			}
		}
	}

	/**
	 * Action that saves a game.
	 */
	private class SaveGameAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			try
			{
				saveGame();
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * this method sends a request to the server to remove the player from the list
	 * of active users, and set the DB to have him as offline.
	 */
	private void logOut() throws Exception
	{
		toServer.writeObject(new RequestPacket(Codes.LOGOUT_REQUEST_CODE));
	}

	public SimpleBooleanProperty getGameRunning()
	{
		return gameRunning;
	}

	public class FishingActivityActions
	{
		public void startFishingActivity()
		{
			fishingActivity = new FishingActivity(gamePane, toServer, fromServer, player);
		}
	}

}
