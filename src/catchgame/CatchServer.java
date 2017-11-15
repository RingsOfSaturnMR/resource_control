package catchgame;

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
import authentication.UsernameError;

import catchgame.Catch.LoginPacket;
import catchgame.Catch.NewUserPacket;
import catchgame.Catch.SeaCreaturePacket;
import catchgame.Catch.SeaCreatureRequestPacket;

import authentication.PasswordError;
import authentication.UsernameError;
import catchgame.Catch.LoginPacket;
import catchgame.Catch.NewUserPacket;
import catchgame.Catch.SeaCreatureRequestPacket;
import catchgame.Catch.SeaCreaturePacket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.SeaCreature;
import userinterface.ServerPane;

/**
 * This class Handles requests from clients for logging in, making new accounts
 * and extracting SeaCreatures from the Ocean.
 * 
 * @author Matt Roberts
 * @author Nils Johnson
 *
 */

public class CatchServer
{
	private Stage serverStage = new Stage();
	private ServerPane serverPane = new ServerPane();
	private ServerSocket serverSocket;
	private UserDAO userDAO = new UserDAO();

	public Ocean ocean = new Ocean();

	/**
	 * Loads the GUI and starts listening for requests to login or make a new account.
	 */
	public CatchServer()
	{
		loadServerPane();
		new Thread(new HandleNewRequestsTask()).start();
	}

	private void loadServerPane()
	{
		// makes a scene with a serverPane
		Scene serverScene = new Scene(serverPane, Constants.INITIAL_SERVER_PANE_WIDTH, Constants.INITIAL_SERVER_PANE_HEIGHT);
		// show serverPane
		serverStage.setScene(serverScene);
		serverStage.setTitle("Catch Server");
		serverStage.centerOnScreen();
		serverStage.show();
		serverStage.requestFocus();
	}

	/**
	 * Listens for initial requests to login or make new accounts.
	 */
	private class HandleNewRequestsTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				serverSocket = new ServerSocket(8000);

				Platform.runLater(() ->

				{
					serverPane.appendToOutput("Server Started at: " + new Date());
					serverPane.appendToOutput("Open to clients.");
				});
				// listens for initial requests
				while (true)
				{
					// set to -1 because needs requires initialization
					int serverCode = -1;
					Socket socket = new Socket();
					socket = serverSocket.accept();

					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
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

								HandleServerSideGameControl handleServerSideGameControl=
	                					new HandleServerSideGameControl(toClient, fromClient);
	                			new Thread(handleServerSideGameControl).start();
								// start serving this client
								//new Thread(new ServeOceanTask(socket, toClient, fromClient)).start();
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

						// reassign for scoping reasons
						final int code = serverCode;
						LoginError e = loginError;
						Platform.runLater(() ->
						{
							serverPane.appendToOutput("Login Attempted, Username: " + loginPacket.enteredName);
							//serverPane.appendToOutput("Result: " + (code == ServerCodeConstants.LOGIN_SUCCESS_CODE ? "Success" : "Not Success"));
							serverPane.appendToOutput("Result: " + (e != null ? e : "Sucess!" ));
						});

					}

					// if someone wants to make a new account
					else if (userData instanceof NewUserPacket)
					{
						NewUserPacket newUserPacket = (NewUserPacket) userData;

						Platform.runLater(() ->

						{
							serverPane.appendToOutput("New Account Attempt, Desired Username: " + newUserPacket.enteredName);
						});

						try
						{
							userDAO.createUser(newUserPacket.enteredName, newUserPacket.enteredPassword, newUserPacket.enteredPasswordConfirm);
							serverCode = ServerCodeConstants.NEW_USER_SUCESS_CODE;
							HandleServerSideGameControl handleServerSideGameControl=
                					new HandleServerSideGameControl(toClient, fromClient);
                			new Thread(handleServerSideGameControl).start();

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

							switch (code)
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
		}
	}

	
	/**
	 * Listens for requests from logged in users to extract resources from Ocean
	 */
	/*
	private class ServeOceanTask implements Runnable
	{
		private Socket socket;
		private ObjectOutputStream toClient;
		private ObjectInputStream fromClient;

		ServeOceanTask(Socket socket, ObjectOutputStream toClient, ObjectInputStream fromClient)
		{
			this.socket = socket;
			this.toClient = toClient;
			this.fromClient = fromClient;
		}

		@Override
		public void run()
		{
			System.out.println("Started spinning a new 'while true' loop to listen for request.");
			try
			{
				while (true)
				{
					SeaCreature creature = null;
					SeaCreatureRequestPacket packet = (SeaCreatureRequestPacket) fromClient.readObject();

					switch (packet.code)
					{
					case ServerCodeConstants.REQUEST_RANDOM_SEACREATURE_CODE:
						creature = ocean.extractRandomSeaCreature();
						break;
					case ServerCodeConstants.REQUEST_COD_CODE:
						creature = ocean.extractCod();
						break;
					case ServerCodeConstants.REQUEST_SALMON_CODE:
						creature = ocean.extractSalmon();
						break;
					case ServerCodeConstants.REQUEST_TUNA_CODE:
						creature = ocean.extractTuna();
						break;
					default:
						creature = null;
						System.out.println("Request Code Not recognized.");
					}
					System.out.println("sending resouce packet with " + creature.getSpecies());
					SeaCreaturePacket sendPacket = new SeaCreaturePacket(creature);
					toClient.writeObject(sendPacket);
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}
	*/
	
	class HandleServerSideGameControl implements Runnable{
		ObjectOutputStream toClient;
		ObjectInputStream fromClient;
		
		HandleServerSideGameControl(ObjectOutputStream toClient, 
				ObjectInputStream fromClient){
			this.toClient=toClient;
			this.fromClient=fromClient;
		}
		
		public void run(){
			ServerSideGameControl serverSideGameControl
			=new ServerSideGameControl(toClient, fromClient, ocean);
		}
	}
}
