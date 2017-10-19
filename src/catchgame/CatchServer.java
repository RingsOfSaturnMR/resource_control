package catchgame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import catchgame.Catch.LoginPacket;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import userinterface.GamePane;
import userinterface.ServerPane;

public class CatchServer
{
	private Stage serverStage = new Stage();
	private ServerPane serverPane = new ServerPane();

	public Ocean ocean = new Ocean();
	
/*	// server response codes, Thoughts?
	public static int VALID_PLAYER_CODE = 0;
	public static int INVALID_PLAYER_CODE = 1;
	public static int INVALID_PASSWORD_CODE = 2;
	// do one for each ocean method?
	public static int EXTRACT_RANDOM_CODE = 20;
	public static int EXTRACT_COD_CODE = 21;
	public static int EXTRACT_CRAB_CODE = 22;
	public static int EXTRACT_LOBSTER_CODE = 23;

	private ObjectInputStream fromClient = null;
	private DataOutputStream toClient = null;*/



	public CatchServer()
	{
		loadServerPane();
		serverPane.appendTaOutput("Server Started at: " + new Date() + "\n" );

		/*class ValidationTask implements Runnable
		{
			@Override
			public void run()
			{
				try
				{
					ServerSocket serverSocket = new ServerSocket(8030);
					serverPane.appendTaOutput("Server Started: " + new Date() + "\n");

					while (true)
					{
						Socket socket = serverSocket.accept();
						fromClient = new ObjectInputStream(socket.getInputStream());
						toClient = new DataOutputStream(socket.getOutputStream());

						boolean validUser;
						LoginPacket loginPacket = (LoginPacket) fromClient.readObject();

						if (loginPacket.enteredName.equals("user") && loginPacket.enteredPassword.equals("pass"))
						{
							toClient.writeInt(0);
							validUser = true;
						}
						else
						{
							toClient.writeInt(1);
							validUser = false;
						}
						
						Platform.runLater(() -> {
							if (validUser)
							{
								serverPane.appendTaOutput(loginPacket.enteredName + ", " + loginPacket.enteredPassword + " Is Valid\n");
							}
							else
							{
								serverPane.appendTaOutput(loginPacket.enteredName + ", " + loginPacket.enteredPassword + " Is NOT Valid\n");
							}
						});

					}
				}
				catch (IOException | ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}

		// Start server and initialize streams
		new Thread(new ValidationTask()).start();*/

	}

	public void loadServerPane()
	{
		// the height and width
		int GAME_WIDTH = 400;
		int GAME_HEIGHT = 400;

		Scene gameScene = new Scene(serverPane, GAME_WIDTH, GAME_HEIGHT);

		// show GamePane
		serverStage.setScene(gameScene);
		serverStage.setTitle("Catch Server");
		serverStage.centerOnScreen();
		serverStage.show();
		serverStage.requestFocus();
	}
}
