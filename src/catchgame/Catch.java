package catchgame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import catchgame.Packets.NewUserPacket;
import catchgame.Packets.ResultPacket;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.SeaCreature;
import userinterface.LoginPane;
import userinterface.NewUserPane;

/**
 * 
 * This class is used to start a game of 'Catch!'. Primarily, it can instantiate
 * a CatchServer, instantiate a GameControl and communicate with CatchServer to
 * make new accounts. It is also used to define packets to transfer information
 * from CatchServer to GameControl and vice versa.
 * 
 * @author Nils Johnson
 * @author Matt Roberts
 */
public class Catch extends Application
{
	private LoginPane loginPane=null;
	private Stage loginStage = new Stage();
	private GameControl gameControl=null;
	static CatchServer catchServer=null;
	private Socket socket = null;

	/**
	 * Loads the initial login pane, which also is passed 
	 * actions for making a new user and creating a server
	 */
	@Override
	public void start(Stage primaryStage)
	{
		loadLoginPane();
	}

	/**
	 * Loads a LoginPane.
	 */
	public void loadLoginPane()
	{
		loginPane = new LoginPane(new LoginHandler(), new NewUserHandler(), new NewServerHandler());
		Scene loginScene = new Scene(loginPane, Constants.LOGIN_PANE_WIDTH, Constants.LOGIN_PANE_HEIGHT);
		loginStage.setScene(loginScene);
		loginStage.setTitle("Catch! Log-in");
		loginStage.centerOnScreen();
		loginStage.show();
		loginStage.requestFocus();
	}

	/**
	 * Launches GameControl, where login is actually handled with info
	 * from the loginPane used for logging in.
	 */
	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			launchGameControl(loginPane.getServerIpAddress(), loginPane.getClientPort(), loginPane.getPlayerName(), loginPane.getPlayerPassword());
		}
	}

	/**
	 * Loads a NewUserPane where a new user can be created, and if 
	 * successful, loads GameControl
	 */
	public class NewUserHandler implements EventHandler<ActionEvent>
	{
		Stage newUserStage = new Stage();
		private NewUserPane newUserPane = null;

		@Override
		public void handle(ActionEvent e)
		{
			// Action for clicking "Make new account"
			class CreateNewUser implements EventHandler<ActionEvent>
			{
				@Override
				public void handle(ActionEvent e)
				{
					// set sockets and streams
					try
					{
						socket = new Socket(newUserPane.getServerIpAddress(), newUserPane.getClientPort());
						ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

						// make a NewUserPacket and send to server
						NewUserPacket newUserPacket = new NewUserPacket(newUserPane.getDesiredName(), newUserPane.getDesiredPassword(), newUserPane.getDesiredPasswordConfirm());

						toServer.writeObject(newUserPacket);
						Object data = fromServer.readObject();

						if (data instanceof ResultPacket)
						{
							ResultPacket packet = (ResultPacket) data;
							switch (packet.code)
							{
							case Codes.NEW_USER_ERR_ILLEGAL_NAME_CODE:
								newUserPane.setErrorText("Illegal Name");
								break;
							case Codes.NEW_USER_ERR_ILLEGAL_PW_CODE:
								newUserPane.setErrorText("Illegal Password");
								break;
							case Codes.NEW_USER_ERR_NAME_TAKEN_CODE:
								newUserPane.setErrorText("Name Unavailable");
								break;
							case Codes.NEW_USER_SUCESS_CODE:
								newUserStage.close();
								loginStage.close();
								launchGameControl(newUserPane.getServerIpAddress(), newUserPane.getClientPort(), newUserPane.getDesiredName(), newUserPane.getDesiredPassword());
								break;
							}
						}

					}
					catch (IOException e1)
					{
						newUserPane.setErrorText(e1.getMessage() + "\nMake sure your port # is the same as the server you're connecting to :)");
						e1.printStackTrace();
					}
					catch (ClassNotFoundException e2)
					{
						newUserPane.setErrorText(e2.getMessage());
						e2.printStackTrace();
					}
				};
			}

			class CancelHandler implements EventHandler<ActionEvent>
			{
				@Override
				public void handle(ActionEvent e)
				{
					newUserStage.close();
				};
			}

			newUserPane = new NewUserPane(new CreateNewUser(), new CancelHandler());
			if (catchServer != null)
			{
				newUserPane.setTfClientPort(catchServer.getServerSocketPort());
			}
			Scene newUserScene = new Scene(newUserPane, Constants.NEW_USER_PANE_WIDTH, Constants.NEW_USER_PANE_HEIGHT);
			newUserStage.setScene(newUserScene);
			newUserStage.setTitle("Catch! New Account");
			newUserStage.show();
			newUserStage.requestFocus();
		};
	}

	/**
	 *	Launches a new CtachServer
	 */
	public class NewServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			catchServer = new CatchServer();
			// wait for server to start listening, then get port number, otherwise the port wont be set in time
			catchServer.isListeningForClients().addListener(ov -> {
				if(loginPane != null)
				{
					loginPane.setTfClientPort(catchServer.getServerSocketPort());
				}});
		}
	}
	
	/**
	 * Launches a new instance of GameControl with the user's login info
	 * @param serverIpAddress the address of the server to log into
	 * @param clientPort the port the client is to use
	 * @param playerName the player's username for logging in
	 * @param playerPassword the player's password for logging in
	 */
	private void launchGameControl(String serverIpAddress, int clientPort, String playerName, String playerPassword)
	{
		try
		{
			new GameControl(serverIpAddress, clientPort, playerName, playerPassword);
		}
		catch (Exception e1)
		{
			loginPane.setErrorText(e1.getMessage());
		}
	}
	

	// main method to launch program
	/**
	 * Main method for launching application
	 * @param args command line array of String arguments
	 */
	public static void main(String[] args)
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				if (catchServer != null)
				{
					System.out.println("quit catchServer");
				}
			}
		});
		launch(args);
	}	
}
