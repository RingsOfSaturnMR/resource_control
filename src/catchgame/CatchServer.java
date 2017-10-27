package catchgame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.lang.model.element.ElementKind;

import authentication.BadLoginException;
import authentication.BadPasswordException;
import authentication.BadUsernameException;
import authentication.LoginError;
import authentication.NewUserException;
import authentication.PasswordError;
import authentication.UsernameError;
import catchgame.Catch.LoginPacket;
import catchgame.Catch.NewUserPacket;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.ServerPane;

public class CatchServer
{
	private Stage serverStage = new Stage();
	private ServerPane serverPane = new ServerPane();
	private ArrayList<ObjectOutputStream> ouputToClientStreamList = new ArrayList<ObjectOutputStream>();
	// maybe useful later
	boolean quit = false;

	private ServerSocket serverSocket;
	private UserDAO userDAO = new UserDAO();

	public Ocean ocean = new Ocean();

	public CatchServer()
	{
		loadServerPane();

		new Thread(() -> {
			try
			{
				serverSocket = new ServerSocket(8000);

				Platform.runLater(() -> {
					serverPane.appendToOutput("Server Started at: " + new Date());
					serverPane.appendToOutput("Open to clients.");
				});
				// listens for initial requests
				while (true)
				{
					// set to -1 because needs requires initialization
					int serverCode = -1;
					Socket socket = serverSocket.accept();

					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
					ouputToClientStreamList.add(toClient);
					ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

					final Object userData = fromClient.readObject();

					// if a user tries to login
					if (userData instanceof LoginPacket)
					{
						LoginPacket loginPacket = (LoginPacket) userData;
						LoginError loginError = null;

						try
						{
							if (userDAO.isValidUser(loginPacket.enteredName, loginPacket.enteredPassword))
							{
								serverCode = ServerCodeConstants.LOGIN_SUCCESS_CODE;
								//ServerSideGameControl serverSideGameControl = new ServerSideGameControl(toClient, fromClient, ocean);
								HandleServerSideGameControl handleServerSideGameControl = new HandleServerSideGameControl(toClient, fromClient);
								new Thread(handleServerSideGameControl).start();
							}
						}
						catch (BadLoginException e)
						{
							loginError = e.getError();

							switch (loginError)
							{
							case INVALID_PASSWORD:
								serverCode = ServerCodeConstants.LOGIN_ERR_INVALID_PASSWORD_CODE;
								break;
							case USER_NOT_FOUND:
								serverCode = ServerCodeConstants.LOGIN_ERR_USER_NOT_FOUND_CODE;
								break;
							case NO_USERS:
								serverCode = ServerCodeConstants.LOGIN_ERR_NO_USERS_FOUND_CODE;
								break;
							default:
								serverCode = ServerCodeConstants.LOGIN_ERR_UNKNOWN_ERROR_CODE;
							}
						}

						toClient.writeObject(new ServerCodePacket(serverCode));

						int code = serverCode;
						Platform.runLater(() -> {
							serverPane.appendToOutput("Login Attempted, Username: " + loginPacket.enteredName);
							serverPane.appendToOutput("Result: " + (code == ServerCodeConstants.LOGIN_SUCCESS_CODE ? "Success" : "Not Success"));
						});

					}

					// if someone wants to make a new account
					else if (userData instanceof NewUserPacket)
					{
						NewUserPacket newUserPacket = (NewUserPacket) userData;
						
						Platform.runLater(() -> {
							serverPane.appendToOutput("New Account Attempt, Desired Username: " + newUserPacket.enteredName);
						});
						
						try
						{
							userDAO.createUser(newUserPacket.enteredName, newUserPacket.enteredPassword, newUserPacket.enteredPasswordConfirm);
							serverCode = ServerCodeConstants.NEW_USER_SUCESS_CODE;
						}
						catch (NewUserException e)
						{
							if (e instanceof BadPasswordException)
							{
								serverCode = ServerCodeConstants.NEW_USER_ERR_ILLEGAL_PW_CODE;
							}
							else if (e instanceof BadUsernameException)
							{
								BadUsernameException exception = (BadUsernameException) e;

								for (int i = 0; i < exception.getErrorList().size(); i++)
								{
									if (exception.getErrorList().get(i) == UsernameError.UNAVAILABLE)
									{
										serverCode = ServerCodeConstants.NEW_USER_ERR_NAME_TAKEN_CODE;
									}
									else if (exception.getErrorList().get(i) == UsernameError.HAS_ILLEGAL_CHAR)
									{
										serverCode = ServerCodeConstants.NEW_USER_ERR_ILLEGAL_NAME_CODE;
									}
								}
							}
						}

						toClient.writeObject(new ServerCodePacket(serverCode));

						int code = serverCode;
						
						Platform.runLater(() -> 
						{
							String result;
							
							switch(code)
							{
								case ServerCodeConstants.NEW_USER_SUCESS_CODE:
									result = "Sucess!";
									break;
								case ServerCodeConstants.NEW_USER_ERR_ILLEGAL_NAME_CODE:
									result = "Illegal Username";
									break;
								case ServerCodeConstants.NEW_USER_ERR_ILLEGAL_PW_CODE:
									result = "Illegal Password";
									break;
								case ServerCodeConstants.NEW_USER_ERR_NAME_TAKEN_CODE:
									result = "Name Taken";
									break;
								default:
									result = "unknown error";
									break;
							}
							
							serverPane.appendToOutput("Result: " + result);
						});
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}).start();

		// start server
		// HandlesConnectionsTask handlesConnectionTask = new
		// HandlesConnectionsTask(serverSocket);
		// new Thread(handlesConnectionTask).start();

	}

	/*class HandlesConnectionsTask implements Runnable
	{

		ServerSocket serverSocket;

		HandlesConnectionsTask(ServerSocket serverSocket)
		{
			this.serverSocket = serverSocket;
		}

		// in a thread
		public void run()
		{
			while (!quit)
			{
				try
				{
					// wait for the client
					Socket socket = serverSocket.accept();
					// tell connection was made
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							serverPane.appendToOutput("Client socket connected.\n");
							new Thread(new HandleAClient(socket)).start();
						}
					});
				}
				// catch any exception
				catch (final IOException ex)
				{
					System.out.println(ex.toString());
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							serverPane.appendToOutput(ex.toString());
						}
					});
				}

			}
		}
	}*/

	// get a client logged in,
	/*
	 * class HandleAClient implements Runnable { private Socket socket;
	 * 
	 * public HandleAClient(Socket socket) { this.socket = socket; }
	 * 
	 * @Override public void run() { try {
	 * 
	 * ObjectOutputStream toClient = new
	 * ObjectOutputStream(socket.getOutputStream());
	 * ouputToClientStreamList.add(toClient); ObjectInputStream fromClient = new
	 * ObjectInputStream(socket.getInputStream());
	 * 
	 * // loop each time a client's LoginPacket is received until logged in boolean
	 * loggedIn = false; while (!loggedIn) {
	 * 
	 * try {
	 * 
	 * // await client data final Object userData = fromClient.readObject();
	 * 
	 * // if it's a loginPacket, verify if (userData instanceof LoginPacket) { //
	 * data will be read from file/database // for now we will hard code a user
	 * String user = "user"; String password = "pass";
	 * 
	 * LoginPacket loginPacket = (LoginPacket) userData;
	 * 
	 * if (user.equals(loginPacket.enteredName)) { if
	 * (password.equals(loginPacket.enteredPassword)) {
	 * 
	 * serverCode = ServerCodeConstants.VALID_PLAYER_AND_PASSWOORD_CODE; loggedIn =
	 * true;// logged in // I think we should have a member class here for //
	 * handling the actual interaction with game control // ServerSideGameControl
	 * serverSideGameControl // =new ServerSideGameControl(toClient, fromClient,
	 * ocean); HandleServerSideGameControl handleServerSideGameControl = new
	 * HandleServerSideGameControl(toClient, fromClient); new
	 * Thread(handleServerSideGameControl).start(); } else { serverCode =
	 * ServerCodeConstants.INVALID_PASSWORD_CODE; } } else { serverCode =
	 * ServerCodeConstants.INVALID_PLAYER_CODE; }
	 * 
	 * // tell the outcome to client toClient.writeObject(new
	 * ServerCodePacket(serverCode));
	 * 
	 * // update serverPane ui Platform.runLater(() -> { switch (serverCode) { case
	 * ServerCodeConstants.VALID_PLAYER_AND_PASSWOORD_CODE:
	 * serverPane.appendToOutput(loginPacket.enteredName +
	 * " entered a valid password and is now logged in.\n"); break; case
	 * ServerCodeConstants.INVALID_PASSWORD_CODE:
	 * serverPane.appendToOutput(loginPacket.enteredName +
	 * " entered an invalid password.\n"); break; case
	 * ServerCodeConstants.INVALID_PLAYER_CODE:
	 * serverPane.appendToOutput(loginPacket.enteredName +
	 * " is not a registered user.\n"); break; } }); } if (userData instanceof
	 * NewUserPacket) { Platform.runLater(() -> { NewUserPacket packet =
	 * (NewUserPacket) userData;
	 * serverPane.appendToOutput("Attempting to make new user\n\t Name: " +
	 * packet.enteredName + "\n\tpw: " + packet.enteredPassword + "\n\tpw confirm: "
	 * + packet.enteredPasswordConfirm); }); }
	 * 
	 * } // otherwise tell it's garbage catch (ClassNotFoundException ex) {
	 * Platform.runLater(new Runnable() {
	 * 
	 * @Override public void run() {
	 * serverPane.appendToOutput("Received garbage data from " +
	 * "client instead of LoginPacket."); } }); }
	 * 
	 * } } catch (IOException ex) { Platform.runLater(new Runnable() {
	 * 
	 * @Override public void run() { serverPane.appendToOutput(ex.toString()); } });
	 * } } }
	 */

	// This is now subdivided into HandleConnectionsTask and HandleAClient
	/*
	 * class ValidationTask implements Runnable {
	 * 
	 * @Override public void run() { try {
	 * serverPane.appendToOutput("Server Started: " + new Date() + "\n");
	 * 
	 * while (true) { Socket socket = serverSocket.accept(); fromClient = new
	 * ObjectInputStream(socket.getInputStream()); toClient = new
	 * DataOutputStream(socket.getOutputStream());
	 * 
	 * boolean validUser; LoginPacket loginPacket = (LoginPacket)
	 * fromClient.readObject();
	 * 
	 * if (loginPacket.enteredName.equals("user") &&
	 * loginPacket.enteredPassword.equals("pass")) { toClient.writeInt(0); validUser
	 * = true; } else { toClient.writeInt(1); validUser = false; }
	 * 
	 * Platform.runLater(() -> { if (validUser) { serverPane.appendToOutput(
	 * loginPacket.enteredName + ", " + loginPacket.enteredPassword +
	 * " Is Valid\n"); } else { serverPane.appendToOutput( loginPacket.enteredName +
	 * ", " + loginPacket.enteredPassword + " Is NOT Valid\n"); } });
	 * 
	 * } } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); } }
	 * }
	 */

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

	class HandleServerSideGameControl implements Runnable
	{
		ObjectOutputStream toClient;
		ObjectInputStream fromClient;

		HandleServerSideGameControl(ObjectOutputStream toClient, ObjectInputStream fromClient)
		{
			this.toClient = toClient;
			this.fromClient = fromClient;
		}

		public void run()
		{
			ServerSideGameControl serverSideGameControl = new ServerSideGameControl(toClient, fromClient, ocean);
		}
	}
}
