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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import market.EquipmentMarket;
import market.SeafoodMarket;
import resources.FishSpecies;
import resources.SeaCreature;
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

	// markets for buying/selling resources
	SeafoodMarket seafoodMarket;
	EquipmentMarket equipMarket;

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

		// 'get' markets
		seafoodMarket = new SeafoodMarket("The Fish Wholesaler");
		equipMarket = new EquipmentMarket("Ye 'Ol General Store");

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

		// define listeners to use in the game
		
		/**
		 * Listens for changes in the players 'IceChest' and updates seafoodMarketPane nodes to reflect the change.
		 * @author Nils
		 */
		class IceChestChangeListener implements ListChangeListener<Object>
		{
			@Override
			public void onChanged(Change<? extends Object> arg0)
			{
				for (int i = 0; i < Constants.supportedSpecies.length; i++)
				{
					int numPlayerHas = player.getNumOf(Constants.supportedSpecies[i]);
					gamePane.marketsPane.setCreaturesOnHandTextAt(i, "You Have: " + Integer.toString(numPlayerHas));
				}
			}
		}

		/**
		 * This class listens for changes in a TextField, and turns them to red if they
		 * are set to something other than empty ("") or an integer
		 * 
		 * @author Nils
		 */
		class IsIntegerTextFieldListener implements ChangeListener
		{
			private TextField textField;

			public IsIntegerTextFieldListener(TextField textField)
			{
				this.textField = textField;
			}

			@Override
			public void changed(ObservableValue arg0, Object oldValue, Object newValue)
			{
				String val = textField.getText().trim();
				if (val.equals(""))
				{
					val = "0";
					textField.setStyle("-fx-control-inner-background: white;");
				}
				else
				{
					try
					{
						Integer.parseInt(val);
					}
					catch (Exception e)
					{
						textField.setStyle("-fx-control-inner-background: red;");
					}
				}

			}
		}

		for (TextField tf : gamePane.marketsPane.getNumCreaturesToSellTextFields())
		{
			tf.textProperty().addListener(new IsIntegerTextFieldListener(tf));
		}
		
		// add listener to players ice chest
		player.getIceChest().addListener(new IceChestChangeListener());

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

		// if code is 'LOGIN_SUCCESS_CODE, expect to receive the user's Object
		else if (resultPacket.code == Codes.LOGIN_SUCCESS_CODE)
		{
			this.player = (Player) fromServer.readObject();
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
	 * Action that occurs user presses button to sell their fish
	 */
	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			int numToSell = 0;

			for (int curSpeciesIndex = 0; curSpeciesIndex < Constants.supportedSpecies.length; curSpeciesIndex++)
			{
				String str = gamePane.marketsPane.getNumCreaturesToSellTextFields()[curSpeciesIndex].getText().trim();

				if (str.equals(""))
				{
					str = "0";
				}

				try
				{
					numToSell = Integer.parseInt(str);

					if (numToSell > player.getNumOf(Constants.supportedSpecies[curSpeciesIndex]))
					{
						throw new Exception("Cannot Sell " + " " +
								numToSell + " " +
								Constants.supportedSpecies[curSpeciesIndex].toString() +
								". You only have " +
								player.getNumOf(Constants.supportedSpecies[curSpeciesIndex]));
					}

					for (int curSeaCreature = 0; curSeaCreature < numToSell; curSeaCreature++)
					{
						// get the 'next' creature of the current type from the player
						SeaCreature creature = player.getSeaNextSeaCreature(Constants.supportedSpecies[curSpeciesIndex]);
						// sell the creature
						double money = seafoodMarket.sellItem(creature);
						player.addMoney(money);
						
						// set the textField to correct number
						String setTo;
						int num = player.getNumOf(Constants.supportedSpecies[curSpeciesIndex]);
						// TOOD conditional here
						if(num == 0)
						{
							setTo = "";
						}
						else
						{
							setTo = (Integer.toString(num));
						}
						gamePane.marketsPane.setSpeciesToSellTextFieldAt(curSpeciesIndex, setTo);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
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
