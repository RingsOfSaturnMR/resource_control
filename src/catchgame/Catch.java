package catchgame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import authentication.NewUserException;
import catchgame.Catch.LoginPacket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.stage.Stage;
import userinterface.LoginPane;
import userinterface.NewUserPane;
import userinterface.RealNewUserPane;

public class Catch extends Application
{
	private LoginPane loginPane;
	private Stage loginStage = new Stage();
	private GameControl gameControl;
	static CatchServer catchServer;

	Socket socket = null;

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
			launchGameControl(loginPane.getServerIpAddress(), loginPane.getClientPort(), loginPane.getPlayerName(), loginPane.getPlayerPassword());
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
								launchGameControl(newUserPane.getServerIpAddress(), newUserPane.getClientPort(), newUserPane.getDesiredName(), newUserPane.getDesiredPassword());
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
	
	public void launchGameControl(String serverIpAddress, int clientPort, String playerName, String playerPassword)
	{
		try
		{
			new GameControl(serverIpAddress, clientPort, playerName, playerPassword);
		}
		catch (Exception e1)
		{
			if(loginPane != null)
			{
			loginPane.setErrorText(e1.getMessage());
			}
			else
			{
				System.out.println("A new GameControl was made from outside of a LoginPane (proabably a NewUserPane), and threw an exception.\n" + 
				e1.getMessage());
				//e1.printStackTrace();
			}
		}	
	}
}

/*
 * // I think with some changes this could be it's own file. // it's too long
 * for this file private class LoginTask implements Runnable {
 * 
 * private String serverIPAdress; private int port; private String enteredName;
 * private String enteredPassword;
 * 
 * private LoginTask(String serverIPAdress, int port, String enteredName, String
 * enteredPassword) { this.serverIPAdress = serverIPAdress; this.port = port;
 * this.enteredName = enteredName; this.enteredPassword = enteredPassword; }
 * 
 * public void run() { while (!connected) { try { // exception will be thrown
 * here if not connected // then it would skip to the catch block socket = new
 * Socket(serverIPAdress, port); connected = true; // now connected
 * System.out.println("In client connected is true.");
 * 
 * } // control shifts to here if there is no connection // catch any exception
 * catch (final IOException ex) { System.out.println(ex.toString()); } } // once
 * connected, it handles the login if (connected && !loggedIn) { try {
 * ObjectOutputStream toServer = new
 * ObjectOutputStream(socket.getOutputStream()); ObjectInputStream fromServer =
 * new ObjectInputStream(socket.getInputStream());
 * 
 * LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
 * toServer.writeObject(loginPacket);
 * 
 * try { ServerCodePacket serverCode = (ServerCodePacket)
 * fromServer.readObject(); switch (serverCode.SERVER_CODE) {
 * 
 * case ServerCodeConstants.VALID_PLAYER_AND_PASSWOORD_CODE: loggedIn = true;
 * System.out.println("Server Response: User logged in.");
 * 
 * Platform.runLater(() -> { Player player = new Player(enteredName,
 * enteredPassword); // gameControl = new GameControl(toServer, fromServer,
 * player); }); break;
 * 
 * case ServerCodeConstants.INVALID_PASSWORD_CODE:
 * System.out.println("Server Response: Invalid Password.");
 * Platform.runLater(() -> {
 * 
 * });
 * 
 * break;
 * 
 * case ServerCodeConstants.INVALID_PLAYER_CODE:
 * System.out.println("Not a registered user.");
 * 
 * break; } } catch (ClassNotFoundException ex) {
 * System.out.println(ex.toString()); }
 * System.out.println("In client serverCode received."); } catch (IOException
 * ex) { System.out.println(ex.toString()); } } } }
 */