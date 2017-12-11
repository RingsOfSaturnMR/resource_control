package catchgame;

/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/
/*
Purpose: To create a server that loads the ui and let's users create acounts, login 
logout, and save and to launch ServerSideGameControl threads (a nested class of 
CatchServer) to do this and respond to requests for SeaCreatures from clients 
that are actively being used to play the game.  UI let's the user stop the server
launch a DBManipulator (now pretty minimal), and launch a frequency histogram of 
the server's ocean.

Modification info:
User's can now logout, save, and quit
ServerSideGameControl threads will stop if user logs out
Can launch a DBManipulator and a FrequencyHistogram
*/

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
import authentication.BadLoginException.LoginError;
import authentication.BadPasswordException;
import authentication.BadUsernameException;
import authentication.NewUserException;
import authentication.NewUserException.UsernameError;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.LeaderBoardRow;
import catchgame.Packets.LoginPacket;
import catchgame.Packets.NewUserPacket;
import catchgame.Packets.ResultPacket;
import catchgame.Packets.RequestPacket;
import catchgame.Packets.SeaCreaturesPacket;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import resources.Fish;
import resources.FishSpecies;
import resources.Shellfish;
import resources.ShellfishSpecies;
import userinterface.ServerPane;

/**
 * This class Handles requests from clients for logging in, making new accounts
 * and extracting SeaCreatures from the Ocean. 
 * 
 * @author Nils Johnson
 * @author Matt Roberts
 */
public class CatchServer
{
	private Stage serverStage = new Stage();
	private ServerPane serverPane = null;
	private UserDAO userDAO = null;
	private int serverSocketPort;
	private SimpleBooleanProperty listeningForNewClients = new SimpleBooleanProperty(false);
	private Thread newClientThread = null;

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
			newClientThread = new Thread(new HandleNewRequestsTask());
			newClientThread.start();
			
			serverStage.setOnCloseRequest(e ->
			{
				// TODO logic for shutdown of server goes here
				System.out.println("Server Shutdown action fired");
				// tell server to stop listening, maybe flush the streams somehow
				listeningForNewClients.set(false);
				// get all the streams that are open to close somehow?
			});
		}
		catch (SQLException e)
		{
			Platform.runLater(() -> serverPane.appendToOutput(e.getMessage()));
			e.printStackTrace();
		}
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
				// let it decide for itself, set to '0'
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
									// if the name is unavailable, return that code. Dont continue checking.
									if (exception.getErrorList().get(i) == UsernameError.UNAVAILABLE)
									{
										serverCode = Codes.NEW_USER_ERR_NAME_TAKEN_CODE;
										break;
									}
									// if any other BadUserName exception, just return generic code, dont continue checking;
									else 
									{
										serverCode = Codes.NEW_USER_ERR_ILLEGAL_NAME_CODE;
										break;
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
			System.out.println("Server Is No Longer Listening For New Clients. Thread Stopped.");
			return;
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
		private LeaderBoardDAO leaderBoardDAO;

		HandleServerSideGameControl(ObjectOutputStream toClient, ObjectInputStream fromClient, String username)
		{
			this.toClient = toClient;
			this.fromClient = fromClient;
			this.username = username;
			
			try
			{
				leaderBoardDAO = new LeaderBoardDAO();
			}
			catch(SQLException sqlE)
			{
				sqlE.printStackTrace();
			}
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
						Platform.runLater(() -> serverPane.appendToOutput(username + " Saved their game."));
						continue;
					}
					
					// if it is an entry for the leaderboard, add it to the leaderboard
					if(recievedObject instanceof LeaderBoardRow)
					{
						LeaderBoardRow row = (LeaderBoardRow) recievedObject;
						System.out.println("Server Side: " + row.toString());
						leaderBoardDAO.update(row);
					}

					if (recievedObject instanceof RequestPacket)
					{
						RequestPacket packet = (RequestPacket) recievedObject;
						int code = packet.code;

						switch (code)
						{
						case Codes.LOGOUT_CODE:
							loggedIn = false;
							continue;
							
						case Codes.DELETE_ACCOUNT_CODE:
							Platform.runLater(() -> serverPane.appendToOutput(username + " sent an account delete request."));
							loggedIn = false;
							userDAO.deleteUser(username);
							continue;
							
						case Codes.GET_LEADER_BOARD_CODE:
							toClient.writeObject(leaderBoardDAO.getLeaderBoard());
							continue;
							
						default:
							Platform.runLater(() -> serverPane.appendToOutput("Unknown Request from " + username + " recieved"));
						}
					}

					// if is a ClientSubOceanSeaCreaturePacket, send a FishPacket back
					if (recievedObject instanceof ClientSubOceanSeaCreatureStatePacket)
					{
						ClientSubOceanSeaCreatureStatePacket clientSubOceanSeaCreatureStatePacket = (ClientSubOceanSeaCreatureStatePacket) recievedObject;
						//System.out.println("clientSubOcean.currentPopulationCod: "+clientSubOcean.currentPopulationCod);
						//System.out.println("clientSubOcean.maxPopulationCod: "+clientSubOcean.maxPopulationCod);
						//System.out.println("subocean crab: "+clientSubOceanSeaCreatureStatePacket.currentPopulationCrab);
						//System.out.println("subocean crab: "+clientSubOceanSeaCreatureStatePacket.maxPopulationCrab);
						ArrayList<Fish> codPacket = ocean.extractAndReturnABunchOfFish(FishSpecies.COD, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationCod, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationCod);
						ArrayList<Fish> salmonPacket = ocean.extractAndReturnABunchOfFish(FishSpecies.SALMON, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationSalmon, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationSalmon);
						ArrayList<Fish> tunaPacket = ocean.extractAndReturnABunchOfFish(FishSpecies.TUNA, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationTuna, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationTuna);
						ArrayList<Shellfish> oysterPacket = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.OYSTER, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationOyster, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationOyster);
						ArrayList<Shellfish> lobsterPacket = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.LOBSTER, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationLobster, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationLobster);
						ArrayList<Shellfish> crabPacket = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.CRAB, 
								clientSubOceanSeaCreatureStatePacket.currentPopulationCrab, 
								clientSubOceanSeaCreatureStatePacket.maxPopulationCrab);
						//System.out.println("crabPacket size: "+crabPacket.size());
						 //System.out.println("cod cuurent population: "
						 //+clientSubOceanSeaCreatureStatePacket.currentPopulationCod);
						 //System.out.println("cod max population: "
						 //+clientSubOceanSeaCreatureStatePacket.maxPopulationCod);
						// System.out.println("Num cod in packet:"+codPacket.size());
						toClient.writeObject(new SeaCreaturesPacket(codPacket, salmonPacket, tunaPacket,
								oysterPacket,lobsterPacket,crabPacket));
					}
				}
				// TODO Consider refactoring this into fewer catch blocks
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
	 * @return the server socket port
	 */
	public int getServerSocketPort()
	{
		return this.serverSocketPort;
	}
	
	/**
	 * @return True or False for if the server is listening for new clients
	 */
	public SimpleBooleanProperty isListeningForClients()
	{
		return listeningForNewClients;
	}

	/**
	 * Action to trigger a WINDOW_CLOSE_REQUEST
	 */
	private class ShutdownServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent arg0)
		{
			serverStage.fireEvent(new WindowEvent(serverStage, WindowEvent.WINDOW_CLOSE_REQUEST));	
		}
	}
	
	/**
	 * Action for creating a new DatabaseManipulator object.
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
