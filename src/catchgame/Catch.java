package catchgame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import authentication.NewUserException;
import catchgame.Catch.LoginPacket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.stage.Stage;
import resources.SeaCreature;
import userinterface.LoginPane;
import userinterface.NewUserPane;

/**
 * This class is used to start a game of 'Catch!'. Primarily, it can instantiate a CatchServer,
 * instantiate a GameControl and communicate with CatchServer to make new accounts. It is also used to define packets to transfer information from
 * CatchServer to GameControl and vice versa.
 * 
 * @author Nils Johnson
 * @author Matt Roberts
 */
public class Catch extends Application
{
	private LoginPane loginPane;
	private Stage loginStage = new Stage();
	private GameControl gameControl;
	static CatchServer catchServer;
	private Socket socket = null;

	@Override
	public void start(Stage primaryStage)
	{
		launchLoginPane();
	}

	public void launchLoginPane()
	{
		loginPane = new LoginPane(new LoginHandler(), new NewUserHandler(), new NewUserServerHandler());
		Scene loginScene = new Scene(loginPane, Constants.LOGIN_PANE_WIDTH, Constants.LOGIN_PANE_HEIGHT);
		loginStage.setScene(loginScene);
		loginStage.setTitle("Catch! Log-in");
		loginStage.centerOnScreen();
		loginStage.show();
		loginStage.requestFocus();
	}

	
	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			tryToLogin(loginPane.getServerIpAddress(), loginPane.getClientPort(), 
					loginPane.getPlayerName(), loginPane.getPlayerPassword());
			//launchGameControl(loginPane.getServerIpAddress(), loginPane.getClientPort(), loginPane.getPlayerName(), loginPane.getPlayerPassword());
		}
	}
	

	public class NewUserHandler implements EventHandler<ActionEvent>
	{
		private Stage newUserStage = new Stage();
		private NewUserPane newUserPane = null;
		private ObjectOutputStream toServer = null;
		private ObjectInputStream fromServer = null;

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

						if (data instanceof ServerCodePacket)
						{
							ServerCodePacket packet = (ServerCodePacket) data;
							switch (packet.SERVER_CODE)
							{
							case ServerCodeConstants.NEW_USER_ERR_ILLEGAL_NAME_CODE:
								newUserPane.setErrorText("Illegal Name");
								break;
							case ServerCodeConstants.NEW_USER_ERR_ILLEGAL_PW_CODE:
								newUserPane.setErrorText("Illegal Password");
								break;
							case ServerCodeConstants.NEW_USER_ERR_NAME_TAKEN_CODE:
								newUserPane.setErrorText("Name Unavailable");
								break;
							case ServerCodeConstants.NEW_USER_SUCESS_CODE:
								newUserStage.close();
								loginStage.close();
								//tryToLogin(newUserPane.getServerIpAddress(), newUserPane.getClientPort(), newUserPane.getDesiredName(), newUserPane.getDesiredPassword());
								Platform.runLater(() -> {
									gameControl = new GameControl(toServer, fromServer);
								});
								break;
							}
						}

					}
					catch (IOException | ClassNotFoundException e1)
					{
						newUserPane.setErrorText(e1.getMessage());
						e1.printStackTrace();
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
			Scene newUserScene = new Scene(newUserPane, Constants.NEW_USER_PANE_WIDTH, Constants.NEW_USER_PANE_HEIGHT);
			newUserStage.setScene(newUserScene);
			newUserStage.setTitle("Catch! New Account");
			newUserStage.show();
			newUserStage.requestFocus();
		};
	}

	public class NewUserServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			catchServer = new CatchServer();
		};
	}

	/*
	public void launchGameControl(String serverIpAddress, int clientPort, String playerName, String playerPassword)
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
	*/

	// main method to launch program
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

	// Packets for server/client communication
	public static class LoginPacket implements Serializable
	{
		public String enteredName;
		public String enteredPassword;

		public LoginPacket(String name, String password)
		{
			this.enteredName = name;
			this.enteredPassword = password;
		}
	}

	public static class NewUserPacket implements Serializable
	{
		public String enteredName;
		public String enteredPassword;
		public String enteredPasswordConfirm;

		public NewUserPacket(String name, String password, String passwordConfirm)
		{
			this.enteredName = name;
			this.enteredPassword = password;
			this.enteredPasswordConfirm = passwordConfirm;
		}
	}

	public static class SeaCreaturePacket implements Serializable
	{
		public SeaCreature creature;

		public SeaCreaturePacket(SeaCreature creature)
		{
			this.creature = creature;
		}
	}

	public static class SeaCreatureRequestPacket implements Serializable
	{
		int code;

		public SeaCreatureRequestPacket(int code)
		{
			this.code = code;
		}
	}

	// I think with some changes this could be it's own file.
	// it's too long for this file

	private void tryToLogin(String serverIPAdress, int port, String enteredName, String enteredPassword) {

		boolean connected = false;

		try {
			// exception will be thrown here if not connected
			// then it would skip to the catch block
			socket = new Socket(serverIPAdress, port);
			connected = true;
			System.out.println("In client connected is true.");

		}
		// control shifts to here if there is no connection
		// catch any exception
		catch (final IOException ex) {
			System.out.println(ex.toString());
		}

		// once connected, it handles the login
		if (connected) {
			try {
				ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

				LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
				toServer.writeObject(loginPacket);

				System.out.println("In client waiting for server code.");
				try {
					ServerCodePacket serverCodePacket = (ServerCodePacket) fromServer.readObject();

					if (serverCodePacket.SERVER_CODE != ServerCodeConstants.LOGIN_SUCCESS_CODE) {
						String errorMessage;
						switch (serverCodePacket.SERVER_CODE) {
						case ServerCodeConstants.LOGIN_ERR_INVALID_PASSWORD_CODE:
							errorMessage = "Invalid Password";
							break;
						case ServerCodeConstants.LOGIN_ERR_NO_USERS_FOUND_CODE:
							errorMessage = "This Server Has No Users Yet!";
							break;
						case ServerCodeConstants.LOGIN_ERR_USER_NOT_FOUND_CODE:
							errorMessage = "Invalid Username";
							break;
						case ServerCodeConstants.LOGIN_ERR_UNKNOWN_ERROR_CODE:
							errorMessage = "The Server Doesnt Know Why You Cant Login :(";
							break;
						default:
							errorMessage = "The server responded '" + serverCodePacket.SERVER_CODE
									+ "', I dont know what that means :(";
							break;
						}
						Platform.runLater(() -> {
							loginPane.setErrorText(errorMessage);
						});
					} else {
						Platform.runLater(() -> {
							gameControl = new GameControl(toServer, fromServer);
						});
					}
				} catch (ClassNotFoundException ex) {
					System.out.println(ex.toString());
				}
				System.out.println("In client serverCode received.");
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}

	}

}
