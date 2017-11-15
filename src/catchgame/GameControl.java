package catchgame;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import resources.Boat;
import resources.BoatTypes;
import resources.Equipment;
import resources.Fish;
import resources.FishSpecies;
import resources.SeaCreature;
import userinterface.GamePane;
import userinterface.LoginPane;

import java.io.DataInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Random;

import java.util.Date;
import java.util.Random;



import catchgame.Catch.LoginPacket;
import catchgame.Catch.SeaCreaturePacket;
import catchgame.Catch.SeaCreatureRequestPacket;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Boat;
import resources.BoatTypes;
import resources.Equipment;
import resources.Fish;
import resources.FishSpecies;
import resources.SeaCreature;
import userinterface.GamePane;

/**
 * This class is a client that lets users extract resources, and drives the GUI
 * for the gameplay.
 * 
 * @author Nils Johnson
 * @author Matt Roberts
 *
 */
public class GameControl
{
	private Player player = null;

	// GUI stuff
	private GamePane gamePane;
	private Stage gameStage = new Stage();
	private Scene gameScene;

	// server communication stuff
	private Socket socket;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;


	private Random rand = new Random();

	/**
	 * Starts a new game
	 * 
	 * @param toServer The ObjectOutputStream to the server.
	 * @param fromServer The ObjectInputStream from the server.
	 */
	public GameControl(ObjectOutputStream toServer, ObjectInputStream fromServer)
	{
		setPlayer();
		this.toServer=toServer;
		this.fromServer=fromServer;
		clientSubOcean=new ClientSubOcean(toServer, fromServer);
		UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
		new Thread(updateFishOnScreenTask).start();
		

		// if no exception was thrown, the player is authenticated.
		// TODO, actually get the file with the players previous state from the server
		setPlayer();

		// Display GUI
		gamePane = new GamePane(new SellFishAction(), player);
		gameScene = new Scene(gamePane, Constants.INITIAL_GAME_PANE_WIDTH, Constants.INITIAL_GAME_PANE_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();

		// general logic for getting resources from server, this is pretty much going to
		// be an extraction method
		try
		{
			for (int i = 0; i < 5; i++)
			{
				SeaCreatureRequestPacket request = new SeaCreatureRequestPacket(ServerCodeConstants.REQUEST_COD_CODE);

				toServer.writeObject(request);
				SeaCreaturePacket creaturePacket = (SeaCreaturePacket) fromServer.readObject();

				SeaCreature creature = creaturePacket.creature;

				System.out.println("Client Has Recieved a " + creature.getSpecies());
				creature.SetBodyByWeight();

				//gamePane.simpleFishingPane.addCreature(creature);
			}

		}
		catch (IOException | ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}

	}

	// TODO, get the actual player from a file, or fetch from server
	private void setPlayer()
	{
		// STUB
		ArrayList<SeaCreature> resources = new ArrayList<>();
		resources.add(new Fish(FishSpecies.COD, 12));

		ArrayList<Equipment> tools = new ArrayList<>();
		tools.add(new Boat(BoatTypes.FISHING_SKIFF));

		this.player = new Player("Fred", "pass", 12.0, resources, tools);
	}

	// TODO, get the actual player from a file, or fetch from server
	private void setPlayer()
	{
		// STUB
		ArrayList<SeaCreature> resources = new ArrayList<>();
		resources.add(new Fish(FishSpecies.COD, 12));

		ArrayList<Equipment> tools = new ArrayList<>();
		tools.add(new Boat(BoatTypes.FISHING_SKIFF));

		this.player = new Player("Fred", "pass", 12.0, resources, tools);

	}


	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Sell Fish action triggered(fish sold to market)");
		}
	}
	
	class UpdateFishOnScreenTask implements Runnable{
		
		public void run(){
			//System.out.println("before");
			ArrayList<Fish> codUpdate;
			//System.out.println("get from server");
			codUpdate=getUpdateSeaCreaturesPacketFromServer();
			
			clientSubOcean.currentPopulationCod+=codUpdate.size();
			//System.out.println("got from server");
			//clientSubOcean.addPacketOfCod(codUpdate);
			addFishPacketToScreen(codUpdate,0,0, Color.BLUE);
			//givePacketGUI(codUpdate);
			//System.out.println("added cod");
		}
	}
	
	public ArrayList<Fish> getUpdateSeaCreaturesPacketFromServer(){
		 //System.out.println("before getting size");
		ClientSubOceanSeaCreatureStatePacket codStatePacket=new ClientSubOceanSeaCreatureStatePacket(
				clientSubOcean.currentPopulationCod, clientSubOcean.maxPopulationCod);
		 //System.out.println("after getting size");
		 try{
		 toServer.writeObject(codStatePacket);
		 //System.out.println("Sent codStatePacket");
		 //System.out.println("before getting fish packet");
		 FishPacketsPacket fishPacketPacket=(FishPacketsPacket)fromServer.readObject();
		 //System.out.println("after getting fish packet");
		 return fishPacketPacket.codPopulation;
		 }catch(IOException ex){
			 System.out.println(ex.toString());
		 }catch(ClassNotFoundException ex){
			 System.out.println(ex.toString());
			 //System.out.println("Over here");
		 }
		 return new ArrayList<Fish>();
	 }
	
	//should be put in SimpleFishingPane
	public void addFishPacketToScreen(ArrayList<Fish> fishUpdate, int topOffset, 
			int bottomOffset, Color color){
		for (int i=0; i<=fishUpdate.size()-1; i++){
			addFishToScreen(fishUpdate.get(i),topOffset,bottomOffset, color);
		}
	}
	//should be put in SimpleFishingPane
	public void addFishToScreen(Fish fish, int topOffset, int bottomOffset, Color color){
		fish.SetBodyByWeight();
		fish.setBodyColor(color);
		//System.out.print(fish.getBody().toString());
		double width=gamePane.simpleFishingPane.getMinWidth();
		double height=gamePane.simpleFishingPane.getMinHeight();
		fish.getBody().setCenterX(getRandomDouble(0,width));
		fish.getBody().setCenterY(getRandomDouble(150+topOffset,height-bottomOffset));
		//System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gamePane.simpleFishingPane.getChildren().add(fish.getBody());
				fish.getBody().setOnMouseClicked(new ExtractFishAction(fish));
			}
		});
	}
	
	private class ExtractFishAction implements EventHandler<MouseEvent>
	{
		Fish fish;
		
		ExtractFishAction(Fish fish){
			this.fish=fish;
		}
		
		@Override
		public void handle(MouseEvent e)
		{
			//System.out.println("Extract fish action "
					//+ "triggered(fish caught)");
			try{
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
					
						gamePane.simpleFishingPane.getChildren().remove(fish.getBody());
						
						clientSubOcean.currentPopulationCod--;
						
						UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
						new Thread(updateFishOnScreenTask).start();
					}
				});
			}
			catch (Exception ex){
				System.out.println(ex.toString());
			}
		};
	}
	
	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max) + min;
		return randomInt;
	}
	private double getRandomDouble(double max, double min) {
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}
}
