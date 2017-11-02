package catchgame;

import javafx.scene.Scene;
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
import java.util.Date;
import java.util.Random;

import catchgame.Catch.LoginPacket;
import catchgame.Catch.SeaCreaturePacket;
import catchgame.Catch.SeaCreatureRequestPacket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
	ClientSubOcean clientSubOcean;

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
		

		// Display GUI
		gamePane = new GamePane(new ExtractFishAction(), new SellFishAction(), player);
		gameScene = new Scene(gamePane, Constants.INITIAL_GAME_PANE_WIDTH, Constants.INITIAL_GAME_PANE_HEIGHT);
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();
	
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

	private class ExtractFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Extract fish action "
					+ "triggered(fish caught)");
			try{
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println(gamePane.simpleFishingPane.getChildren().remove(clientSubOcean.codPopulation.get(
								clientSubOcean.codPopulation.size()-1).getBody()));
						try{
						clientSubOcean.extractFish(clientSubOcean.codPopulation);
						}
						catch (Exception ex){
							System.out.println(ex.toString());
						}
						
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
			//System.out.println("got from server");
			clientSubOcean.addPacketOfCod(codUpdate);
			givePacketGUI(codUpdate);
			//System.out.println("added cod");
		}
	}
	
	public ArrayList<Fish> getUpdateSeaCreaturesPacketFromServer(){
		 //System.out.println("before getting size");
		 SubOceanFishStatePacket codStatePacket=new SubOceanFishStatePacket(clientSubOcean.codPopulation.size(), 100);
		 //System.out.println("after getting size");
		 try{
		 toServer.writeObject(codStatePacket);
		 System.out.println("Sent codStatePacket");
		 //System.out.println("before getting fish packet");
		 FishPacketsPacket fishPacketPacket=(FishPacketsPacket)fromServer.readObject();
		 //System.out.println("after getting fish packet");
		 return fishPacketPacket.codPopulation;
		 }catch(IOException ex){
			 System.out.println(ex.toString());
		 }catch(ClassNotFoundException ex){
			 System.out.println(ex.toString());
			 System.out.println("Over here");
		 }
		 return new ArrayList<Fish>();
	 }
	
	public void givePacketGUI(ArrayList<Fish> fishUpdate){
		for (int i=0; i<=fishUpdate.size()-1; i++){
			makeSeaCreatureGUI(fishUpdate.get(i));
		}
	}
	public void makeSeaCreatureGUI(Fish fish){
		fish.SetBodyByWeight();
		System.out.print(fish.getBody().toString());
		double width=gamePane.simpleFishingPane.getMinWidth();
		double height=gamePane.simpleFishingPane.getMinHeight();
		fish.getBody().setCenterX(getRandomDouble(0,width));
		fish.getBody().setCenterY(getRandomDouble(150,height));
		System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gamePane.simpleFishingPane.getChildren().add(fish.getBody());
				//gamePane.getChildren().addAll(fish.GUICircle);
			}
		});
		//gamePane.getChildren().addAll(fish.GUICircle);
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
