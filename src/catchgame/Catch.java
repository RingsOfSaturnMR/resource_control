package catchgame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import catchgame.Catch.LoginPacket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.stage.Stage;
import userinterface.LoginPane;

public class Catch extends Application
{
	private LoginPane loginPane;
	private Stage loginStage = new Stage();
	private GameControl gameControl;
	static CatchServer catchServer;
	//initially unconnected
	private boolean connected=false;
	//initially not logged in
	private boolean loggedIn=false;
	//this may not need to be a class variable
	Socket socket=null;

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

	//static int numTries = 0;

	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			/*
			try
			{
				GameControl gameControl = new GameControl(loginPane.getServerIpAddress(), loginPane.getClientPort(), loginPane.getPlayerName(), loginPane.getPlayerPassword());
			}
			catch (Exception ex )
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}			
			*/
			LoginTask loginTask=new LoginTask(loginPane.getServerIpAddress(), 
					loginPane.getClientPort(), loginPane.getPlayerName(), 
					loginPane.getPlayerPassword());
	        new Thread(loginTask).start();
		};
		
	}

	public class NewUserHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("New User Clicked");
		};
	}

	public class NewUserServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("New Server Clicked");
			catchServer = new CatchServer();
		};
	}

	public static class LoginPacket implements Serializable
	{
		public LoginPacket(String name, String password)
		{
			this.enteredName = name;
			this.enteredPassword = password;
		}

		public String enteredName;
		public String enteredPassword;
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (catchServer != null) {
					System.out.println("quit catchServer");
				}
			}
		});
		launch(args);
	}

	//I think with some changes this could be it's own file.
	//it's too long for this file
	private class LoginTask implements Runnable {

		String serverIPAdress;
		int port;
		String enteredName;
		String enteredPassword;

		private LoginTask(String serverIPAdress, int port, String enteredName, String enteredPassword) {
			this.serverIPAdress = serverIPAdress;
			this.port = port;
			this.enteredName = enteredName;
			this.enteredPassword = enteredPassword;
		}

		// in a thread
		public void run() {
			// while not connected, keep looping
			while (!connected) {
				try {
					// exception will be thrown here if not connected
					// then it would skip to the catch block
					socket = new Socket(serverIPAdress, port);
					connected = true; // now connected
					System.out.println("In client connected is true.");

				}
				// control shifts to here if there is no connection
				// catch any exception
				catch (final IOException ex) {
					System.out.println(ex.toString());
				}
			}
			// once connected, it handles the login
			if (connected && !loggedIn) {
				try {
					ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
					DataInputStream fromServer = new DataInputStream(socket.getInputStream());

					LoginPacket loginPacket = new LoginPacket(enteredName, enteredPassword);
					toServer.writeObject(loginPacket);

					System.out.println("In client waiting for server code.");
					int serverCode = fromServer.readInt();
					System.out.println("In client serverCode received.");

					switch (serverCode) {

					case ServerCodeConstants.VALID_PLAYER_AND_PASSWOORD_CODE:
						loggedIn = true; // now logged in
						System.out.println("User logged in.");

						// start gameControl

						Platform.runLater(() -> {
							gameControl = new GameControl(socket, new Player());
						});

					case ServerCodeConstants.INVALID_PASSWORD_CODE:
						System.out.println("Invalid Password.");

					case ServerCodeConstants.INVALID_PLAYER_CODE:
						System.out.println("Not a registered user.");
						;
					}
				} catch (IOException ex) {
					System.out.println(ex.toString());
				}
			}

		}
	}
}
