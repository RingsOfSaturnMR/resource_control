package catchgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import authentication.BadLoginException;
import authentication.BadPasswordException;
import authentication.BadUsernameException;
import authentication.LoginError;
import authentication.NewUserException;
import authentication.UsernameError;

import catchgame.Packets.LoginPacket;
import catchgame.Packets.NewUserPacket;
import catchgame.Packets.ResultPacket;
import catchgame.Packets.RequestPacket;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.FishPacketsPacket;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Fish;
import resources.FishSpecies;
import userinterface.ServerPane;

/**
 * This class Handles requests from clients for logging in, making new accounts
 * and extracting SeaCreatures from the Ocean. 
 * 
 * @author Matt Roberts
 * @author Nils Johnson
 */
public class CatchServer
{
	private Stage serverStage = new Stage();
	private ServerPane serverPane = null;
	private UserDAO userDAO = null;
	private int serverSocketPort;
	private SimpleBooleanProperty listeningForNewClients = new SimpleBooleanProperty(false);

	public Ocean ocean = new Ocean();

	/**
	 * Loads the GUI and starts listening for requests to login or make a new
	 * account.
	 */
	public CatchServer()
	{
		loadServerPane();
		try
		{
			userDAO = new UserDAO();
		}
		catch (SQLException e)
		{
			Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
			e.printStackTrace();
		}
		new Thread(new HandleNewRequestsTask()).start();
	}

	/**
	 * Action event that creates a new Frequency Histogram object 
	 * and associated GUI.
	 */
	private class MakeFrequencyHistogramAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			FrequencyHistogram fh=new FrequencyHistogram(ocean);
		}
	}
	
	/**
	 * Loads the Server Pane with a LaunchDbManipulatorHandler,
	 * ShutdownServerHandler, MakeFrequencyHistogramAction given 
	 * to the pane
	 */
	private void loadServerPane()
	{
		// makes a scene with a serverPane
		serverPane = new ServerPane(new LaunchDbManipulatorHandler(), new ShutdownServerHandler(), new MakeFrequencyHistogramAction());
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
				// let it decide for itself
				ServerSocket serverSocket = new ServerSocket(0);
				serverSocketPort = serverSocket.getLocalPort();
				
				Platform.runLater(() ->
				{
					serverPane.appendToOutput("Server Started at: " + new Date());
					serverPane.appendToOutput("Open to clients on port " + serverSocketPort );
				});
				
				// set server to listen
				listeningForNewClients.set(true);
				
				// start listening
				while (listeningForNewClients.get())
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
						Player player = null;
						try
						{
							// try to get player with entered login credentials
							player = userDAO.getUser(loginPacket.enteredName, loginPacket.enteredPassword);
							serverCode = Codes.LOGIN_SUCCESS_CODE;
							
							// start serving that user on a new thread
							HandleServerSideGameControl handleServerSideGameControl = new HandleServerSideGameControl(toClient, fromClient, loginPacket.enteredName);
							new Thread(handleServerSideGameControl).start();

						}
						catch (BadLoginException e)
						{
							loginError = e.getError();

							switch (loginError)
							{
							case INVALID_PASSWORD:
								serverCode = Codes.LOGIN_ERR_INVALID_PASSWORD_CODE;
								break;
							case USER_NOT_FOUND:
								serverCode = Codes.LOGIN_ERR_USER_NOT_FOUND_CODE;
								break;
							case NO_USERS:
								serverCode = Codes.LOGIN_ERR_NO_USERS_FOUND_CODE;
								break;
							case INVALID_ATTEMPT:
								serverCode = Codes.LOGIN_ERR_INVALID_ATTEMPT_CODE;
								break;
							default:
								serverCode = Codes.LOGIN_ERR_UNKNOWN_ERROR_CODE;
							}
						}

						toClient.writeObject(new ResultPacket(serverCode));

						if (serverCode == Codes.LOGIN_SUCCESS_CODE)
						{

							toClient.writeObject(player);
						}

						// reassign for scoping reasons
						final int code = serverCode;
						LoginError e = loginError;
						Platform.runLater(() ->
						{
							serverPane.appendToOutput("Login Attempted, Username: " + loginPacket.enteredName);
							serverPane.appendToOutput("Result: " + (e != null ? e : "Sucess!"));
						});

					}

					// if someone wants to make a new account
					else if (userData instanceof NewUserPacket)
					{
						NewUserPacket newUserPacket = (NewUserPacket) userData;

						Platform.runLater(() -> serverPane.appendToOutput("New Account Attempt, Desired Username: " + newUserPacket.enteredName));

						try
						{
							userDAO.createUser(newUserPacket.enteredName, newUserPacket.enteredPassword, newUserPacket.enteredPasswordConfirm);
							serverCode = Codes.NEW_USER_SUCESS_CODE;
							HandleServerSideGameControl handleServerSideGameControl = new HandleServerSideGameControl(toClient, fromClient, newUserPacket.enteredName);
							new Thread(handleServerSideGameControl).start();

						}
						catch (NewUserException e)
						{
							if (e instanceof BadPasswordException)
							{
								serverCode = Codes.NEW_USER_ERR_ILLEGAL_PW_CODE;
							}
							else if (e instanceof BadUsernameException)
							{
								BadUsernameException exception = (BadUsernameException) e;

								for (int i = 0; i < exception.getErrorList().size(); i++)
								{
									if (exception.getErrorList().get(i) == UsernameError.UNAVAILABLE)
									{
										serverCode = Codes.NEW_USER_ERR_NAME_TAKEN_CODE;
									}
									else if (exception.getErrorList().get(i) == UsernameError.HAS_ILLEGAL_CHAR)
									{
										serverCode = Codes.NEW_USER_ERR_ILLEGAL_NAME_CODE;
									}
								}
							}
						}

						toClient.writeObject(new ResultPacket(serverCode));

						int code = serverCode;

						Platform.runLater(() ->
						{
							String result;

							switch (code)
							{
							case Codes.NEW_USER_SUCESS_CODE:
								result = "Sucess!";
								break;
							case Codes.NEW_USER_ERR_ILLEGAL_NAME_CODE:
								result = "Illegal Username";
								break;
							case Codes.NEW_USER_ERR_ILLEGAL_PW_CODE:
								result = "Illegal Password";
								break;
							case Codes.NEW_USER_ERR_NAME_TAKEN_CODE:
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
				serverPane.appendToOutput(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Communicates between CatchServer and GameControl during a game,
	 * and is designed to be given to a thread so that this communication
	 * can occur freely while the rest of the program runs.
	 */
	class HandleServerSideGameControl implements Runnable
	{
		private ObjectOutputStream toClient;
		private ObjectInputStream fromClient;
		private String username;
		private boolean loggedIn = true;

		HandleServerSideGameControl(ObjectOutputStream toClient, ObjectInputStream fromClient, String username)
		{
			this.toClient = toClient;
			this.fromClient = fromClient;
			this.username = username;
		}

		public void run()
		{
			// I moved this code back in here so it could communicate with the rest of the
			// server. Particularly, when it needs to append text to the server log
			while (loggedIn)
			{
				try
				{
					// get object from client
					Object recievedObject = fromClient.readObject();

					// if it is a player object, save it
					if (recievedObject instanceof Player)
					{
						Player player = (Player) recievedObject;
						userDAO.savePlayer(player);
						continue;
					}

					if (recievedObject instanceof RequestPacket)
					{
						RequestPacket packet = (RequestPacket) recievedObject;
						int code = packet.code;

						switch (code)
						{
						case Codes.LOGOUT_REQUEST_CODE:
							Platform.runLater(() -> serverPane.appendToOutput("Logout Request Recieved!"));
							loggedIn = false;
							continue;
						default:

						}
					}

					// if is a ClientSubOceanSeaCreaturePacket, send a FishPacket back
					if (recievedObject instanceof ClientSubOceanSeaCreatureStatePacket)
					{
						ClientSubOceanSeaCreatureStatePacket subOceanFishStatePacket = (ClientSubOceanSeaCreatureStatePacket) recievedObject;

						ArrayList<Fish> codPacket = ocean.extractAndReturnABunchOfFish(FishSpecies.COD, 
								subOceanFishStatePacket.currentPopulationCod, 
								subOceanFishStatePacket.maxPopulationCod);
						// System.out.println("cod cuurent population: "
						// +subOceanFishStatePacket.currentPopulationCod);
						// System.out.println("cod max population: "
						// +subOceanFishStatePacket.maxPopulationCod);
						// System.out.println("Num cod in packet:"+codPacket.size());
						toClient.writeObject(new FishPacketsPacket(codPacket));
					}
				}
				// TODO Consider refactor this into fewer catch blocks
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
					Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
				}

			}

			Platform.runLater(() -> serverPane.appendToOutput(username + " has logged out and is no longer being served."));
			// to stop thread from running
			return;
		}
	}
	
	/**
	 * Simple getter for the server socket port
	 * @return the server socket port
	 */
	public int getServerSocketPort()
	{
		return this.serverSocketPort;
	}
	
	public SimpleBooleanProperty isListeningForClients()
	{
		return listeningForNewClients;
	}

	public void setListeningForClients(boolean val)
	{
		this.listeningForNewClients.set(val);
	}
	
	/**
	 * Right now this action is a stub that just says the user has tried to 
	 * shut down the server
	 */
	private class ShutdownServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent arg0)
		{
			System.out.println("Server Shutdown action fired");
		}
		
	}
	
	/**
	 * Action for creating anew DatabaseManipulator object.
	 */
	private class LaunchDbManipulatorHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent arg0)
		{
			new DatabaseManipulator();
		}
		
	}
}
