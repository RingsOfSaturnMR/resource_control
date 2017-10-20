package catchgame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
	private ArrayList <DataOutputStream> ouputToClientStreamList=new ArrayList<DataOutputStream>();
	//maybe useful later
	boolean quit=false;
	//a starting code (no meaning)
	int serverCode=-1;

	public Ocean ocean = new Ocean();
	
	// server response codes, Thoughts?
	// seems goods
	public final static int VALID_PLAYER_AND_PASSWOORD_CODE = 0;
	public final static int INVALID_PASSWORD_CODE = 1;
	public final static int INVALID_PLAYER_CODE = 2;
	// do one for each ocean method?
	//Let's talk about this part
	public static int EXTRACT_RANDOM_CODE = 20;
	public static int EXTRACT_COD_CODE = 21;
	public static int EXTRACT_CRAB_CODE = 22;
	public static int EXTRACT_LOBSTER_CODE = 23;




	public CatchServer()
	{
		loadServerPane();
		
		try{
			
		ServerSocket serverSocket= new ServerSocket(8030);
		
		serverPane.appendToOutput("Server Started at: " + new Date() + "\n");
		serverPane.appendToOutput("Open to clients.\n");
		
		//start server
		HandlesConnectionsTask handlesConnectionTask=new HandlesConnectionsTask(serverSocket);
		new Thread(handlesConnectionTask).start();
		
		}
		catch(IOException ex){
			System.out.println(ex.toString());
		}

	}

	class HandlesConnectionsTask implements Runnable {

		ServerSocket serverSocket;
		
		HandlesConnectionsTask(ServerSocket serverSocket){
			this.serverSocket=serverSocket;
		}
		
		// in a thread
		public void run() {
			while (!quit) {
				try {
					// wait for the client
					Socket socket = serverSocket.accept();
					// tell connection was made
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							serverPane.appendToOutput("Client socket connected.\n");
							new Thread(new HandleAClient(socket)).start();
						}
					});
				}
				// catch any exception
				catch (final IOException ex) {
					System.out.println(ex.toString());
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							serverPane.appendToOutput(ex.toString());
						}
					});
				}

			}
		}
	}

	//get a client logged in,
	class HandleAClient implements Runnable {
		private Socket socket;

		public HandleAClient(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				
				DataOutputStream toClient=new DataOutputStream(socket.getOutputStream());
				ouputToClientStreamList.add(toClient);
				ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
				
				//loop each time a client's LoginPacket is received until logged in
				boolean loggedIn=false;
                while (!loggedIn) {

                    try {
                    	
                        //await client data
                        final Object userData = fromClient.readObject();
                        
                        //if it's a loginPacket, verify
                        if (userData instanceof LoginPacket) {
                        	//data will be read from file/database
                        	//for now we will hard code a user
                        	String user="user";
                        	String password="pass";
                        	
                        	LoginPacket loginPacket=(LoginPacket)userData;
                        	
                        	if (user.equals(loginPacket.enteredName)){
                        		if (password.equals(loginPacket.enteredPassword)){
                        			
                        			serverCode=VALID_PLAYER_AND_PASSWOORD_CODE;
                        			loggedIn=true;//logged in
                        			//I think we should have a member class here for 
                        			//handling the actual interaction with game control
                        		}
                        		else{
                        			serverCode=INVALID_PASSWORD_CODE;
                        		}
                        	}
                        	else{
                        		serverCode=INVALID_PLAYER_CODE;
                        	}
                        	
                        	//tell the outcome to client
                        	toClient.writeInt(serverCode);
                        	
                        	System.out.println("In server serverCode wrote.");
                        	
                        	//update serverPane ui
                        	Platform.runLater(() -> {
        						switch(serverCode){
        						case VALID_PLAYER_AND_PASSWOORD_CODE:
        							serverPane.appendToOutput(loginPacket.enteredName + 
        									" entered a valid password and is now logged in.\n");
        							break;
        						case INVALID_PASSWORD_CODE:
        							serverPane.appendToOutput(loginPacket.enteredName + 
        									" entered an invalid password.\n");
        							break;
        						case INVALID_PLAYER_CODE:
        							serverPane.appendToOutput(loginPacket.enteredName + 
        									" is not a registered user.\n");
        							break;
        						}
        					});
                        }
                        
                    }
                    //otherwise tell it's garbage
                    catch (ClassNotFoundException ex) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                serverPane.appendToOutput("Received garbage data from "
                                		+ "client instead of LoginPacket.");
                            }
                        });
                    }

                }
			} catch (IOException ex) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						serverPane.appendToOutput(ex.toString());
					}
				});
			}
		}
	}

	
	//This is now subdivided into HandleConnectionsTask and HandleAClient
	/*
	class ValidationTask implements Runnable {
		@Override
		public void run() {
			try {
				serverPane.appendToOutput("Server Started: " + new Date() + "\n");

				while (true) {
					Socket socket = serverSocket.accept();
					fromClient = new ObjectInputStream(socket.getInputStream());
					toClient = new DataOutputStream(socket.getOutputStream());

					boolean validUser;
					LoginPacket loginPacket = (LoginPacket) fromClient.readObject();

					if (loginPacket.enteredName.equals("user") && loginPacket.enteredPassword.equals("pass")) {
						toClient.writeInt(0);
						validUser = true;
					} else {
						toClient.writeInt(1);
						validUser = false;
					}

					Platform.runLater(() -> {
						if (validUser) {
							serverPane.appendToOutput(
									loginPacket.enteredName + ", " + loginPacket.enteredPassword + " Is Valid\n");
						} else {
							serverPane.appendToOutput(
									loginPacket.enteredName + ", " + loginPacket.enteredPassword + " Is NOT Valid\n");
						}
					});

				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	*/

	public void loadServerPane() {
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
