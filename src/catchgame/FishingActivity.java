package catchgame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.FishPacketsPacket;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import resources.Fish;
import userinterface.GamePane;
import utilities.NumberUtilities;

/**
 * 
 * @author Matt Roberts
 *
 */
public class FishingActivity {
	
	private GamePane gamePane;
	
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;
	
	private Player player;
	
	private ClientSubOcean clientSubOcean;
	
	public FishingActivity(GamePane gamePane, ObjectOutputStream toServer, 
			ObjectInputStream fromServer, Player player){
		clientSubOcean=new ClientSubOcean();
		this.gamePane=gamePane;
		this.player=player;
		this.toServer=toServer;
		this.fromServer=fromServer;
		updateFishOnScreen();
	}
	
	/*
	// update fish stuff - put this elsewhere? like, when you actually start fishing, call this.
			// also, I dont think we need any new threads for this
			clientSubOcean=new ClientSubOcean(toServer, fromServer);
			UpdateFishOnScreenTask updateFishOnScreenTask=new UpdateFishOnScreenTask();
			new Thread(updateFishOnScreenTask).start();
			*/
	/**
	 * Requests new SeaCreatures from Server, and adds them to the screen
	 * if any or sent
	 */
	class UpdateFishOnScreenTask implements Runnable
	{

		public void run()
		{
			updateFishOnScreen();
		}
	}

	private void updateFishOnScreen() {
		// System.out.println("before");
		ArrayList<Fish> codUpdate;
		// System.out.println("get from server");
		codUpdate = getUpdateSeaCreaturesPacketFromServer();

		clientSubOcean.currentPopulationCod += codUpdate.size();
		// System.out.println("got from server");
		// clientSubOcean.addPacketOfCod(codUpdate);
		addFishPacketToScreen(codUpdate, 0, 0, Color.BLUE);
		// givePacketGUI(codUpdate);
		// System.out.println("added cod");
	}

	/**
	 * Actually makes the request for new SeaCreatures from the server and sends
	 * a new ClientSubOceanSeaCreatureStatePacket so that the server can no what
	 * to send, listens for SeaCreatures sent from server and returns any
	 * 
	 * @return The SeaCreatures sent by the server
	 */
	public ArrayList<Fish> getUpdateSeaCreaturesPacketFromServer()
	{
		// System.out.println("before getting size");
		ClientSubOceanSeaCreatureStatePacket codStatePacket = new ClientSubOceanSeaCreatureStatePacket(clientSubOcean.currentPopulationCod, clientSubOcean.maxPopulationCod);
		// System.out.println("after getting size");
		try
		{
			toServer.writeObject(codStatePacket);
			// System.out.println("Sent codStatePacket");
			// System.out.println("before getting fish packet");
			FishPacketsPacket fishPacketPacket = (FishPacketsPacket) fromServer.readObject();
			// System.out.println("after getting fish packet");
			return fishPacketPacket.codPopulation;
		}
		catch (IOException ex)
		{
			System.out.println(ex.toString());
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println(ex.toString());
			// System.out.println("Over here");
		}
		return new ArrayList<Fish>();
	}

	// should be put in SimpleFishingPane
	/**
	 * Adds fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param fishUpdate the fish to add to the screen
	 * @param topOffset the highest the fish can be on the screen
	 * @param bottomOffset the lowest the fish can be on the screen
	 * @param color the color the fish should be given
	 */
	public void addFishPacketToScreen(ArrayList<Fish> fishUpdate, int topOffset, int bottomOffset, Color color)
	{
		for (int i = 0; i <= fishUpdate.size() - 1; i++)
		{
			addFishToScreen(fishUpdate.get(i), topOffset, bottomOffset, color);
		}
	}

	// should be put in SimpleFishingPane
	/**
	 * Adds an individual fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param fish the fish to add to the screen
	 * @param topOffset the highest the fish can be on the screen
	 * @param bottomOffset the lowest the fish can be on the screen
	 * @param color the color the fish should be given
	 */
	public void addFishToScreen(Fish fish, int topOffset, int bottomOffset, Color color)
	{
		fish.SetBodyByWeight();
		fish.setBodyColor(color);
		// System.out.print(fish.getBody().toString());
		double width = gamePane.simpleFishingPane.getMinWidth();
		double height = gamePane.simpleFishingPane.getMinHeight();
		fish.getBody().setCenterX(NumberUtilities.getRandomDouble(0, width));
		fish.getBody().setCenterY(NumberUtilities.getRandomDouble(150 + topOffset, height - bottomOffset));
		// System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				gamePane.simpleFishingPane.getChildren().add(fish.getBody());
				fish.getBody().setOnMouseClicked(new ExtractFishAction(fish));
			}
		});
	}

	/**
	* Action that occurs when a fish is caught.
	 */
	private class ExtractFishAction implements EventHandler<MouseEvent>
	{
		Fish fish;

		ExtractFishAction(Fish fish)
		{
			this.fish = fish;
		}

		@Override
		public void handle(MouseEvent e)
		{
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try
			{
				// Something like this here?
				player.addSeaCreatureToIceChest(fish);
				
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{

						gamePane.simpleFishingPane.getChildren().remove(fish.getBody());

						clientSubOcean.currentPopulationCod--;

						UpdateFishOnScreenTask updateFishOnScreenTask = new UpdateFishOnScreenTask();
						new Thread(updateFishOnScreenTask).start();
					}
				});
			}
			catch (Exception ex)
			{
				System.out.println(ex.toString());
			}
		};
	}
	
	private class ClientSubOcean{
		
		int currentPopulationCod=0;
		int maxPopulationCod=100;
		
	}
}
