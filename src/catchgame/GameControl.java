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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import market.EquipmentMarket;
import market.SeafoodMarket;
import resources.Equipment;
import resources.FishSpecies;
import resources.SeaCreature;
import userinterface.GamePane;
import userinterface.SimpleFishingPane;
import utilities.NumberUtilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
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
	FishingActivityV3 fishingActivityV3;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// for server
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	// markets for buying/selling resources
	private SeafoodMarket seafoodMarket;
	private EquipmentMarket equipMarket;

	// so Catch.java can listen to what is happening in the game
	private SimpleBooleanProperty gameRunning = new SimpleBooleanProperty(false);

	// to flag if a player deletes the account, so we dont ask them if they want to
	// save upon deletion
	private boolean accountDeleted = false;

	// for the backing audio track
	private AudioClip backTrack = null;

	/**
	 * Starts a new game
	 * 
	 * @param serverIpAddress where you are connecting
	 * @param clientPort the port you will connect to
	 * @param enteredName username
	 * @param enternedPasswored enteredPassword
	 * 
	 * @throws any problem that arrises in starting a game
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

		// instantiate markets
		seafoodMarket = new SeafoodMarket("The Fish Wholesaler", new SeafoodPriceSetEventHandler());
		equipMarket = new EquipmentMarket("Ye 'Ol Fishin' Store", new SetCurrentEquipPricesHandler());

		// Display GUI
		gamePane = new GamePane(new SellFishAction(), player, new FishingActivityActions(), new DeleteAccountAction(), new SaveGameAction(), new ExitAction(), seafoodMarket.getName(), equipMarket.getName());
		gameScene = new Scene(gamePane, Constants.INITIAL_GAME_PANE_WIDTH, Constants.INITIAL_GAME_PANE_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.show();
		gameStage.requestFocus();

		// tell user game is started, before you force a price update.
		gamePane.appendOutput("Hello " + player.getUsername() +
				". Welcome.");

		// make the markets update its prices so the GUI can display them
		seafoodMarket.forcePriceUpdate();
		equipMarket.forcUpdate();

		// update the GUI to have most recent player into
		updateNumSellableSeaCreatures();
		updateNumEquipOnHand();

		//////////////////////////////////////////////////////////////////////////
		// ----- define nested listeners to use within scope of GameControl ------
		/////////////////////////////////////////////////////////////////////////
		/**
		 * Listens for changes in the players 'IceChest' and updates seafoodMarketPane
		 * nodes to reflect the change.
		 */
		class IceChestChangeListener implements ListChangeListener<Object>
		{
			@Override
			public void onChanged(Change<? extends Object> arg0)
			{
				updateNumSellableSeaCreatures();
			}
		}

		/**
		 * Listens for changes in the players 'ToolChest' and updates
		 * EquipmentMarketPane nodes to reflect the change.
		 */
		class ToolChestChangeListener implements ListChangeListener<Object>
		{
			@Override
			public void onChanged(Change<? extends Object> arg0)
			{
				updateNumEquipOnHand();
			}
		}

		// add the listeners of this scope
		player.getIceChest().addListener(new IceChestChangeListener());
		player.getToolChest().addListener(new ToolChestChangeListener());

		/////////////////////////////////////////////////////////////
		// ------ add inner level listeners to GUI components ------
		/////////////////////////////////////////////////////////////
		for (int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++)
		{
			// lets the user know if they are attempting a valid sale of SeaCreatures
			gamePane.seafoodMarketPane.addNumToSellTfListener(i, new IsValidQuantityListener(Constants.SUPPORTED_SPECIES[i]));
		}
		for (int i = 0; i < Constants.SUPPORTED_EQUIPMENT.length; i++)
		{
			// add listeners to buttons so users can actually buy equipment
			gamePane.equipmentMarketPane.setBtnSellActionAt(i, new BuyEquipmentHandler(Constants.SUPPORTED_EQUIPMENT[i]));
		}

		// sequence for closing down
		gameStage.setOnCloseRequest(new shutdownHandler());

		// set game to running
		gameRunning.set(true);

		// start looping music
		loopBackTrack();
	}

	private void loopBackTrack()
	{
		try
		{
			backTrack = new AudioClip(getClass().getResource("/sound/water.wav").toURI().toString());
			backTrack.setCycleCount(MediaPlayer.INDEFINITE);
			backTrack.play();
		}
		catch (URISyntaxException e)
		{
			gamePane.appendOutput(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Sets the EquipmentMarketPane to show how many of an Equipment type the user
	 * has.
	 */
	private void updateNumEquipOnHand()
	{
		for (int i = 0; i < Constants.SUPPORTED_EQUIPMENT.length; i++)
		{
			int numPlayerHas = player.getNumOf(Constants.SUPPORTED_EQUIPMENT[i]);
			gamePane.equipmentMarketPane.setEquipOnHandTextAt(i, "You Have: " + Integer.toString(numPlayerHas));
		}
	}

	/**
	 * Course of events to shut down program.
	 */
	private class shutdownHandler implements EventHandler<WindowEvent>
	{
		@Override
		public void handle(WindowEvent event)
		{
			try
			{
				if (!accountDeleted)
				{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Exit");
					alert.setHeaderText("Exit Options");
					alert.setContentText("Choose your option.");

					ButtonType saveAndExit = new ButtonType("Save And Quit");
					ButtonType justExit = new ButtonType("Quit");

					alert.getButtonTypes().setAll(saveAndExit, justExit);

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == saveAndExit)
					{
						saveGame();
						logOut();
					}
					else if (result.get() == justExit)
					{
						logOut();
					}
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
				backTrack.stop();
			}
		}
	}

	/**
	 * Takes user's name and password and logs them in, or throws an exception that
	 * will make GameControl pass out of scope and get propagated back to where it
	 * was instantiated.
	 * 
	 * @param enteredName
	 * @param enteredPassword
	 * @throws Exception for network error, or authentication error
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
			double totalSale = 0;
			double totalWeight = 0;

			for (int curSpeciesIndex = 0; curSpeciesIndex < Constants.SUPPORTED_SPECIES.length; curSpeciesIndex++)
			{
				gamePane.appendOutput((curSpeciesIndex == 0 ? "--- Starting Transaction ---" : ""));

				String str = gamePane.seafoodMarketPane.getNumCreaturesToSellTextFields()[curSpeciesIndex].getText().trim();

				if (str.equals(""))
				{
					str = "0";
				}

				try
				{
					numToSell = Integer.parseInt(str);

					if (numToSell > player.getNumOf(Constants.SUPPORTED_SPECIES[curSpeciesIndex]))
					{
						throw new Exception("Cannot Sell " + " " +
								numToSell +
								" " +
								Constants.SUPPORTED_SPECIES[curSpeciesIndex].toString() +
								", you have " +
								player.getNumOf(Constants.SUPPORTED_SPECIES[curSpeciesIndex]) +
								".");
					}

					for (int curSeaCreature = 0; curSeaCreature < numToSell; curSeaCreature++)
					{
						// get the 'next' creature of the current type from the player
						SeaCreature<?> creature = player.getSeaNextSeaCreature(Constants.SUPPORTED_SPECIES[curSpeciesIndex]);
						// sell the creature
						double money = seafoodMarket.sellItem(creature);
						player.addMoney(money);

						// set the textField to correct number
						String setTo;
						int num = player.getNumOf(Constants.SUPPORTED_SPECIES[curSpeciesIndex]);
						// TOOD conditional here
						if (num == 0)
						{
							setTo = "";
						}
						else
						{
							setTo = (Integer.toString(num));
						}
						gamePane.seafoodMarketPane.setSpeciesToSellFfAt(curSpeciesIndex, setTo);
						// log transaction for player to see

						double price = creature.getWeight() * seafoodMarket.getCurrentPricePerPound((Enum<?>) creature.getSpecies());
						String weight = Double.toString(creature.getWeight());
						gamePane.appendOutput("Selling: " + creature.toString() +
								", -> $" +
								NumberUtilities.round(price, 2));

						totalWeight += creature.getWeight();
						totalSale += price;
					}
				}
				catch (NumberFormatException nfe)
				{
					nfe.printStackTrace();
				}
				catch (Exception ex)
				{
					gamePane.appendOutput(ex.getMessage());
				}

			}
			gamePane.appendOutput("You sold " + totalWeight +
					" pounds of seafood for $" +
					totalSale);
			gamePane.appendOutput("--- End Transaction ---");

			if (totalWeight > 0)
			{
				playCashSound();
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
		gamePane.appendOutput("Game Saved.");
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
					accountDeleted = true;
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
		gamePane.appendOutput("Logging Out");
		toServer.writeObject(new RequestPacket(Codes.LOGOUT_REQUEST_CODE));
	}

	/**
	 * Used to determine if the LoginPane, or NewUserPane shoud close
	 * 
	 * @return status of the game
	 */
	public SimpleBooleanProperty getGameRunning()
	{
		return gameRunning;
	}

	// TODO - Matt
	public class FishingActivityActions
	{
		public void startFishingActivity()
		{
			
			fishingActivityV3 = new FishingActivityV3(gamePane, toServer, fromServer, player);
		}
	}

	/**
	 * Updates the GUI to display current Seafood prices per pound.
	 */
	public class SeafoodPriceSetEventHandler
	{
		public void setPrices()
		{
			for (int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++)
			{
				double currentPrice = seafoodMarket.getCurrentPricePerPound(Constants.SUPPORTED_SPECIES[i]);
				gamePane.seafoodMarketPane.setCurrentPricesTextAt(i, Double.toString(currentPrice));
			}
		}
	}

	/**
	 * updates the GUI to display how many SeaCreatures of each type the player has
	 * to sell
	 */
	private void updateNumSellableSeaCreatures()
	{
		for (int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++)
		{
			int numPlayerHas = player.getNumOf(Constants.SUPPORTED_SPECIES[i]);
			gamePane.seafoodMarketPane.setCreaturesOnHandTextAt(i, "You Have: " + Integer.toString(numPlayerHas));
		}
	}

	/**
	 * Used to listen for changes in SeafoodMarketPane's TextFields. Turns them to
	 * red if they are set to something other than empty (""), an integer, or more
	 * than the player has.
	 */
	public class IsValidQuantityListener implements ChangeListener<Object>
	{
		private TextField textField;
		private Enum<?> speciesType;

		public IsValidQuantityListener(Enum<?> speciesType)
		{
			this.speciesType = speciesType;
		}

		public void setTextField(TextField tf)
		{
			this.textField = tf;
		}

		@Override
		public void changed(ObservableValue<?> arg0, Object oldValue, Object newValue)
		{
			if (textField != null)
			{
				String val = textField.getText().trim();
				if (val.equals(""))
				{
					val = "0";
					setWhite();
				}
				else
				{
					try
					{
						if (Integer.parseInt(val) <= player.getNumOf(speciesType))
						{
							setWhite();
						}
						else
						{
							setRed();
						}
					}
					catch (Exception e)
					{
						setRed();
					}
				}
			}
			else
			{
				System.out.println("listner does not have associated Textfield attached.");
			}
		}

		private void setWhite()
		{
			textField.setStyle("-fx-control-inner-background: white;");
		}

		private void setRed()
		{
			textField.setStyle("-fx-control-inner-background: red;");
		}
	}

	/**
	 * The methods that let a player buy more equipment
	 */
	public class BuyEquipmentHandler implements EventHandler<ActionEvent>
	{
		private Enum<?> itemType;

		public BuyEquipmentHandler(Enum<?> itemType)
		{
			this.itemType = itemType;
		}

		@Override
		public void handle(ActionEvent arg0)
		{
			if (player.getCashOnHand() >= equipMarket.getCurrentPrice(itemType))
			{
				player.addItemToToolChest((Equipment<?>) equipMarket.buyItem(itemType));
				player.subtractMoney(equipMarket.getCurrentPrice(itemType));

				gamePane.appendOutput("New Fishing Gear Purchased: " + itemType.toString());
			}
			else
			{
				gamePane.appendOutput("Transaction Declined, Insufficent Funds");
			}

		}
	}

	public class SetCurrentEquipPricesHandler
	{
		public void setPrices()
		{
			for (int i = 0; i < Constants.SUPPORTED_EQUIPMENT.length; i++)
			{
				gamePane.equipmentMarketPane.setCurrentPricesTextAt(i, Double.toString(equipMarket.getCurrentPrice(Constants.SUPPORTED_EQUIPMENT[i])));
			}
			gamePane.appendOutput("New Market Prices Posted.");
		}
	}

	/**
	 * plays a cash register noise
	 */
	private void playCashSound()
	{
		AudioClip chaChing;
		try
		{
			chaChing = new AudioClip(getClass().getResource("/sound/cash.mp3").toURI().toString());
			chaChing.play();
		}
		catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
