package catchgame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.SeaCreature;
import userinterface.LoginPane;
import userinterface.NewUserPane;

/**
 * This class is used to start a came of 'Catch!'. Primarily, it can instantiate a CatchServer,
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

}
