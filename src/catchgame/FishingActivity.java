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
import resources.Fish;
import resources.Shellfish;
import userinterface.GamePane;
import userinterface.SimpleFishingPane;
import utilities.ArrayListUtilities;
import utilities.NumberUtilities;

public class FishingActivity {
	private SimpleFishingPane simpleFishingPane;

	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	private Player player;

	private ClientSubOcean clientSubOcean;
<<<<<<< HEAD
	private ClientSubOceanAnimator clientFishingActivityFishManager;

	
	public ClientSubOceanAnimator getClientFishingActivityFishManager(){
=======
	private ClientFishingActivityFishManager clientFishingActivityFishManager;

	
	public ClientFishingActivityFishManager getClientFishingActivityFishManager(){
>>>>>>> master
		return clientFishingActivityFishManager;
	}
	/**
	 * This constructs a FishingActivity by putting SeaCreatures on the screen.
	 * 
	 * @param gamePane
	 *            the gamepane where the FishingActivity's UI will be held
	 * @param toServer
	 *            an ObjectOutputStream to the server
	 * @param fromServer
	 *            an ObjectInputStream from the server
	 * @param player
	 *            the player who will be affected by the user fishing
	 * @throws IOException
	 */
	public FishingActivity(GamePane gamepane, ObjectOutputStream toServer, 
			ObjectInputStream fromServer, Player player) {
		this.simpleFishingPane = gamepane.simpleFishingPane;
		clientSubOcean = new ClientSubOcean();
<<<<<<< HEAD
		clientFishingActivityFishManager = new ClientSubOceanAnimator(simpleFishingPane);
=======
		clientFishingActivityFishManager = new ClientFishingActivityFishManager(simpleFishingPane);
>>>>>>> master
		this.player = player;
		this.toServer = toServer;
		this.fromServer = fromServer;
		updateSeaCreaturesOnScreen();
		clientFishingActivityFishManager.doBasicClientSubOceanAnimation();
	}

	/**
	 * Requests new SeaCreatures from Server, and adds them to the screen if any
	 * are sent
	 */
	class UpdateFishOnScreenTask implements Runnable {

		public void run() {
			updateSeaCreaturesOnScreen();
		}
	}

	/**
	 * Gets a SeaCreaturesPacket from the server, and adds all the Sea Creatures
	 * to the screen, and increments the ClientSubOcean count for all the
	 * SeaCreatures added
	 */

	private void updateSeaCreaturesOnScreen() {

		SeaCreaturesPacket seaCreaturesPacket;
		seaCreaturesPacket = getUpdateSeaCreaturesPacketFromServer();

		clientSubOcean.currentPopulationCod += seaCreaturesPacket.codPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.codPopulation, Constants.DISTANCE_FROM_TOP, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.BOTTOM_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);

		clientSubOcean.currentPopulationSalmon += seaCreaturesPacket.salmonPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.salmonPopulation, Constants.DISTANCE_FROM_TOP, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.BOTTOM_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);

		clientSubOcean.currentPopulationTuna += seaCreaturesPacket.tunaPopulation.size();
		addFishPacketToScreen(seaCreaturesPacket.tunaPopulation, Constants.DISTANCE_FROM_TOP, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.BOTTOM_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);

		clientSubOcean.currentPopulationOyster += seaCreaturesPacket.oysterPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.oysterPopulation, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.TOP_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);

		clientSubOcean.currentPopulationLobster += seaCreaturesPacket.lobsterPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.lobsterPopulation, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.TOP_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);

		System.out.println("Adding crab to subocean: " + seaCreaturesPacket.crabPopulation.size());
		clientSubOcean.currentPopulationCrab += seaCreaturesPacket.crabPopulation.size();
		addShellfishPacketToScreen(seaCreaturesPacket.crabPopulation, 
				(int)(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.TOP_COEFFICIENT)+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);

		clientFishingActivityFishManager.codPopulation.addAll(seaCreaturesPacket.codPopulation);
		clientFishingActivityFishManager.salmonPopulation.addAll(seaCreaturesPacket.salmonPopulation);
		clientFishingActivityFishManager.tunaPopulation.addAll(seaCreaturesPacket.tunaPopulation);
		clientFishingActivityFishManager.lobsterPopulation.addAll(seaCreaturesPacket.lobsterPopulation);
		clientFishingActivityFishManager.crabPopulation.addAll(seaCreaturesPacket.crabPopulation);
		clientFishingActivityFishManager.oysterPopuliation.addAll(seaCreaturesPacket.oysterPopulation);
	}

	/**
	 * Actually makes the request for new SeaCreatures from the server and sends
	 * a new ClientSubOceanSeaCreatureStatePacket so that the server can no what
	 * to send, listens for SeaCreatures sent from server and returns any
	 * 
	 * @return The SeaCreatures sent by the server
	 */

	public SeaCreaturesPacket getUpdateSeaCreaturesPacketFromServer() {
		try {
			ClientSubOceanSeaCreatureStatePacket clientSubOceanSeaCreatureStatePacket = new ClientSubOceanSeaCreatureStatePacket(
					clientSubOcean.currentPopulationCod, clientSubOcean.maxPopulationCod,
					clientSubOcean.currentPopulationSalmon, clientSubOcean.maxPopulationSalmon,
					clientSubOcean.currentPopulationTuna, clientSubOcean.maxPopulationTuna,
					clientSubOcean.currentPopulationOyster, clientSubOcean.maxPopulationOyster,
					clientSubOcean.currentPopulationLobster, clientSubOcean.maxPopulationLobster,
					clientSubOcean.currentPopulationCrab, clientSubOcean.maxPopulationCrab);
			toServer.writeObject(clientSubOceanSeaCreatureStatePacket); //
			System.out.println("Sent codStatePacket"); //
			System.out.println("before getting fish packet");
			SeaCreaturesPacket seaCreaturesPacket = (SeaCreaturesPacket) fromServer.readObject(); //
			System.out.println("after getting fish packet");
			return seaCreaturesPacket;
		} catch (IOException ex) {
			System.out.println(ex.toString());
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());

		}
		return new SeaCreaturesPacket();
	}

	/**
	 * Adds fish to the screen between the topOffset and bottomOffset and of the
	 * given color
	 * 
	 * @param fishUpdate
	 *            the fish to add to the screen
	 * @param topOffset
	 *            the highest the fish can be on the screen
	 * @param bottomOffset
	 *            the lowest the fish can be on the screen
	 * @param color
	 *            the color the fish should be given
	 */
	public void addFishPacketToScreen(ArrayList<Fish> fishUpdate, int topOffset, int bottomOffset) {
		for (int i = 0; i <= fishUpdate.size() - 1; i++) {
			addFishToScreen(fishUpdate.get(i), topOffset, bottomOffset);
		}
	}

	/**
	 * Adds shellfish to the screen between the topOffset and bottomOffset and
	 * of the given color
	 * 
	 * @param shellfishUpdate
	 *            the shellfish to add to the screen
	 * @param topOffset
	 *            the highest the shellfish can be on the screen
	 * @param bottomOffset
	 *            the lowest the shellfish can be on the screen
	 * @param color
	 *            the color the shellfish should be given
	 */
	public void addShellfishPacketToScreen(ArrayList<Shellfish> shellfishUpdate, int topOffset, int bottomOffset) {
		for (int i = 0; i <= shellfishUpdate.size() - 1; i++) {
			addShellfishToScreen(shellfishUpdate.get(i), topOffset, bottomOffset);
		}
	}

	/**
	 * Adds an individual fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * 
	 * @param fish
	 *            the fish to add to the screen
	 * @param topOffset
	 *            the highest the fish can be on the screen
	 * @param bottomOffset
	 *            the lowest the fish can be on the screen
	 * @param color
	 *            the color the fish should be given
	 */
	public void addFishToScreen(Fish fish, int topOffset, int bottomOffset) {
		fish.setFishBodyByWeight();
		System.out.println("set FishBody");
		double width = simpleFishingPane.getMinWidth();
		double height = simpleFishingPane.getMinHeight();
		fish.getFishGraphic().getFishImageView().setTranslateX(
				NumberUtilities.getRandomDouble(0, width - fish.getFishGraphic().getFishImageView().getFitWidth()));
		fish.getFishGraphic().getFishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - fish.getFishGraphic().getFishImageView().getFitHeight()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				simpleFishingPane.getChildren().add(fish.getFishGraphic().getFishImageView());
				fish.getFishGraphic().getFishImageView().setOnMouseClicked(new ExtractFishAction(fish));
			}
		});
	}

	/**
	 * Adds an individual shellfish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * 
	 * @param shellfish
	 *            the shellfish to add to the screen
	 * @param topOffset
	 *            the highest the shellfish can be on the screen
	 * @param bottomOffset
	 *            the lowest the shellfish can be on the screen
	 * @param color
	 *            the color the shellfish should be given
	 */
	public void addShellfishToScreen(Shellfish shellfish, int topOffset, int bottomOffset) {
		shellfish.setShellfishBodyByWeight();
		double width = Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH;//simpleFishingPane.getMinWidth();
		double height = Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT;//simpleFishingPane.getMinHeight();
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateX(NumberUtilities.getRandomDouble(0,
				width - shellfish.getShellfishGraphic().getShellfishImageView().getFitWidth()));
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - shellfish.getShellfishGraphic().getShellfishImageView().getFitHeight()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				simpleFishingPane.getChildren().add(shellfish.getShellfishGraphic().getShellfishImageView());
				shellfish.getShellfishGraphic().getShellfishImageView()
						.setOnMouseClicked(new ExtractShellfishAction(shellfish));
			}
		});
	}

	/**
	 * Action that occurs when a fish is caught.
	 */
	private class ExtractFishAction implements EventHandler<MouseEvent> {
		Fish fish;

		ExtractFishAction(Fish fish) {
			this.fish = fish;
		}

		@Override
		public void handle(MouseEvent e) {
			try {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(fish);
						simpleFishingPane.getChildren().remove(fish.getFishGraphic().getFishImageView());
						switch (fish.getSpecies()) {
						case COD:
							ArrayListUtilities.removeObjectFromArrayList(clientFishingActivityFishManager.codPopulation,
									fish);
							System.out.println("Cod: " + clientFishingActivityFishManager.codPopulation.size());
							clientSubOcean.currentPopulationCod--;
							break;
						case SALMON:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.salmonPopulation, fish);
							System.out.println("Salmon: " + clientFishingActivityFishManager.salmonPopulation.size());
							clientSubOcean.currentPopulationSalmon--;
							break;

						case TUNA:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.tunaPopulation, fish);
							System.out.println("Tuna: " + clientFishingActivityFishManager.tunaPopulation.size());
							clientSubOcean.currentPopulationTuna--;
							break;
						}

					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			updateSeaCreaturesOnScreen();
		};
	}

	/**
	 * Action that occurs when a shellfish is caught.
	 */
	private class ExtractShellfishAction implements EventHandler<MouseEvent> {
		Shellfish shellfish;

		ExtractShellfishAction(Shellfish shellfish) {
			this.shellfish = shellfish;
		}

		@Override
		public void handle(MouseEvent e) {
			try {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(shellfish);
						simpleFishingPane.getChildren().remove(shellfish.getShellfishGraphic().getShellfishImageView());
						switch (shellfish.getSpecies()) {
						case LOBSTER:
							ArrayListUtilities.removeObjectFromArrayList(
									clientFishingActivityFishManager.lobsterPopulation, shellfish);
							System.out.println("Lobster: " + clientFishingActivityFishManager.lobsterPopulation.size());
							clientSubOcean.currentPopulationLobster--;
							break;
						case CRAB:
							ArrayListUtilities.removeObjectFromArrayList(
									clientFishingActivityFishManager.crabPopulation, shellfish);
							System.out.println("Crab: " + clientFishingActivityFishManager.crabPopulation.size());
							clientSubOcean.currentPopulationCrab--;
							break;

						case OYSTER:
							ArrayListUtilities.removeObjectFromArrayList(
									clientFishingActivityFishManager.oysterPopuliation, shellfish);
							System.out.println("Oyster: " + clientFishingActivityFishManager.oysterPopuliation.size());

							clientSubOcean.currentPopulationOyster--;
							break;
						}
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			updateSeaCreaturesOnScreen();
		};
	}

	/**
	 * For keeping track of all the SeaCreatures on the client's side.
	 */
	class ClientSubOcean {

		int currentPopulationCod = 0;
		int maxPopulationCod = Constants.COD_MAX_POPULATION / 100;
		int currentPopulationSalmon = 0;
		int maxPopulationSalmon = Constants.SALMON_MAX_POPULATION / 100;
		int currentPopulationTuna = 0;
		int maxPopulationTuna = Constants.TUNA_MAX_POPULATION / 100;
		int currentPopulationOyster = 0;
		int maxPopulationOyster = Constants.OYSTER_MAX_POPULATION / 100;
		int currentPopulationLobster = 0;
		int maxPopulationLobster = Constants.LOBSTER_MAX_POPULATION / 100;
		int currentPopulationCrab = 0;
		int maxPopulationCrab = Constants.CRAB_MAX_POPULATION / 100;
	}
}
