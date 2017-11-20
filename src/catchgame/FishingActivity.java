package catchgame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.SeaCreaturesPacket;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import resources.Fish;
import resources.FishSpecies;
import resources.Shellfish;
import resources.ShellfishSpecies;
import userinterface.GamePane;
import utilities.NumberUtilities;

/**
 * This is responsible for sending info to the server about the ClientSubOcean conditions,
 * receiving SeaCreature packets, adding them to the screen, and providing action events for when
 * a seaCreature is caught.
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
	
	/**
	 * This constructs a FishingActivity by putting SeaCreatures on the screen.
	 * @param gamePane the gamepane where the FishingActivity's UI will be held
	 * @param toServer an ObjectOutputStream to the server
	 * @param fromServer an ObjectInputStream from the server
	 * @param player the player who will be affected by the user fishing
	 */
	public FishingActivity(GamePane gamePane, ObjectOutputStream toServer, 
			ObjectInputStream fromServer, Player player){
		clientSubOcean=new ClientSubOcean();
		this.gamePane=gamePane;
		this.player=player;
		this.toServer=toServer;
		this.fromServer=fromServer;
		updateSeaCreaturesOnScreen();
	}
	
	
	/**
	 * Requests new SeaCreatures from Server, and adds them to the screen
	 * if any are sent
	 */
	class UpdateFishOnScreenTask implements Runnable
	{

		public void run()
		{
			updateSeaCreaturesOnScreen();
		}
	}

	/**
	 * Gets a SeaCreaturesPacket from the server, and adds all the 
	 * Sea Creatures to the screen, and increments the ClientSubOcean
	 * count for all the SeaCreatures added
	 */
	private void updateSeaCreaturesOnScreen() {
		
		SeaCreaturesPacket seaCreaturesPacket;
		seaCreaturesPacket = getUpdateSeaCreaturesPacketFromServer();
		
		clientSubOcean.currentPopulationCod +=
				seaCreaturesPacket.codPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.codPopulation, 0, 0, Color.BLUE);
		
		clientSubOcean.currentPopulationSalmon+=
				seaCreaturesPacket.salmonPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.salmonPopulation, 0, 0, Color.PINK);
		
		clientSubOcean.currentPopulationTuna+=
				seaCreaturesPacket.tunaPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.tunaPopulation, 0, 0, Color.GRAY);
		
		clientSubOcean.currentPopulationOyster+=
				seaCreaturesPacket.oysterPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.oysterPopulation, 0, 0, Color.YELLOW);
		
		clientSubOcean.currentPopulationLobster+=
				seaCreaturesPacket.lobsterPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.lobsterPopulation, 0, 0, Color.RED);
		
		clientSubOcean.currentPopulationCrab+=
				seaCreaturesPacket.crabPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.crabPopulation, 0, 0, Color.BLACK);
		
	}

	/**
	 * Actually makes the request for new SeaCreatures from the server and sends
	 * a new ClientSubOceanSeaCreatureStatePacket so that the server can no what
	 * to send, listens for SeaCreatures sent from server and returns any
	 * 
	 * @return The SeaCreatures sent by the server
	 */
	public SeaCreaturesPacket getUpdateSeaCreaturesPacketFromServer()
	{
		try
		{
			System.out.println("in client clientSubOcean.currentPopulationCod: "+clientSubOcean.currentPopulationCod);
			ClientSubOceanSeaCreatureStatePacket clientSubOceanSeaCreatureStatePacket=
					new ClientSubOceanSeaCreatureStatePacket(clientSubOcean.currentPopulationCod, 
							clientSubOcean.maxPopulationCod, 
							clientSubOcean.currentPopulationSalmon, clientSubOcean.maxPopulationSalmon,
							clientSubOcean.currentPopulationTuna, clientSubOcean.maxPopulationTuna,
							clientSubOcean.currentPopulationOyster, clientSubOcean.maxPopulationOyster,
							clientSubOcean.currentPopulationLobster, clientSubOcean.maxPopulationLobster,
							clientSubOcean.currentPopulationCrab, clientSubOcean.maxPopulationCrab);
			toServer.writeObject(clientSubOceanSeaCreatureStatePacket);
			 System.out.println("Sent codStatePacket");
			 System.out.println("before getting fish packet");
			SeaCreaturesPacket seaCreaturesPacket = (SeaCreaturesPacket) fromServer.readObject();
			 System.out.println("after getting fish packet");
			return seaCreaturesPacket;
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
		return new SeaCreaturesPacket();
	}

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
	
	/**
	 * Adds shellfish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param shellfishUpdate the shellfish to add to the screen
	 * @param topOffset the highest the shellfish can be on the screen
	 * @param bottomOffset the lowest the shellfish can be on the screen
	 * @param color the color the shellfish should be given
	 */
	public void addShellfishPacketToScreen(ArrayList<Shellfish> shellfishUpdate, int topOffset, int bottomOffset, Color color)
	{
		for (int i = 0; i <= shellfishUpdate.size() - 1; i++)
		{
			addShellfishToScreen(shellfishUpdate.get(i), topOffset, bottomOffset, color);
		}
	}

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
	 * Adds an individual shellfish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * @param shellfish the shellfish to add to the screen
	 * @param topOffset the highest the shellfish can be on the screen
	 * @param bottomOffset the lowest the shellfish can be on the screen
	 * @param color the color the shellfish should be given
	 */
	public void addShellfishToScreen(Shellfish shellfish, int topOffset, int bottomOffset, Color color)
	{
		shellfish.SetBodyByWeight();
		shellfish.setBodyColor(color);
		// System.out.print(fish.getBody().toString());
		double width = gamePane.simpleFishingPane.getMinWidth();
		double height = gamePane.simpleFishingPane.getMinHeight();
		shellfish.getBody().setCenterX(NumberUtilities.getRandomDouble(0, width));
		shellfish.getBody().setCenterY(NumberUtilities.getRandomDouble(150 + topOffset, height - bottomOffset));
		// System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				gamePane.simpleFishingPane.getChildren().add(shellfish.getBody());
				shellfish.getBody().setOnMouseClicked(new ExtractShellfishAction(shellfish));
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
						if (fish.getSpecies()==FishSpecies.COD);
							clientSubOcean.currentPopulationCod--;
						if (fish.getSpecies()==FishSpecies.SALMON);
							clientSubOcean.currentPopulationSalmon--;
						if (fish.getSpecies()==FishSpecies.TUNA);
							clientSubOcean.currentPopulationTuna--;

							updateSeaCreaturesOnScreen();
					}
				});
			}
			catch (Exception ex)
			{
				System.out.println(ex.toString());
			}
		};
	}
	
	/**
	* Action that occurs when a shellfish is caught.
	 */
	private class ExtractShellfishAction implements EventHandler<MouseEvent>
	{
		Shellfish shellfish;

		ExtractShellfishAction(Shellfish shellfish)
		{
			this.shellfish = shellfish;
		}

		@Override
		public void handle(MouseEvent e)
		{
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try
			{
				// Something like this here?
				player.addSeaCreatureToIceChest(shellfish);
				
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{

						gamePane.simpleFishingPane.getChildren().remove(shellfish.getBody());
						if (shellfish.getSpecies()==ShellfishSpecies.CRAB);
							clientSubOcean.currentPopulationCrab--;
						if (shellfish.getSpecies()==ShellfishSpecies.LOBSTER);
							clientSubOcean.currentPopulationLobster--;
						if (shellfish.getSpecies()==ShellfishSpecies.OYSTER);
							clientSubOcean.currentPopulationOyster--;

						updateSeaCreaturesOnScreen();
					}
				});
			}
			catch (Exception ex)
			{
				System.out.println(ex.toString());
			}
		};
	}
	
	/**
	 * For keeping track of all the SeaCreatures on the client's side.
	 */
	class ClientSubOcean{
		
		int currentPopulationCod=0;
		int maxPopulationCod=Constants.COD_MAX_POPULATION/10;
		int currentPopulationSalmon=0;
		int maxPopulationSalmon=Constants.SALMON_MAX_POPULATION/10;
		int currentPopulationTuna=0;
		int maxPopulationTuna=Constants.TUNA_MAX_POPULATION/10;
		int currentPopulationOyster=0;
		int maxPopulationOyster=Constants.OYSTER_MAX_POPULATION/10;
		int currentPopulationLobster=0;
		int maxPopulationLobster=Constants.LOBSTER_MAX_POPULATION/10;
		int currentPopulationCrab=0;
		int maxPopulationCrab=Constants.CRAB_MAX_POPULATION/10;
		}

}
