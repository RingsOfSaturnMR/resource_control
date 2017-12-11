package animation_work_and_driver;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/
/*
Purpose: to allow the user to catch SeaCreatures by getting
SeaCreaturesfrom the server and adding them to the user's
SimpleFishingPane with onClick event handler's theat extract
a fish when the fish's GUI is clicked on and add it to
the Player's IceChest.  SeaCreatures are currently rendered
with circles colored by species and sized by SeaCreature's
weight.

Modification Info:
These methods were originally found in GameControl.
Colors have been added, and the ability to get all SeaCreatures
(as opposed to just cod) has been added.  The ability to add the
SeaCreature to the player's ice chest is new too.
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import catchgame.Constants;
import catchgame.Ocean;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.SeaCreaturesPacket;
import catchgame.Player;
import graphicclasses.FishImageView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import resources.Fish;
import resources.FishSpecies;
import resources.Shellfish;
import resources.ShellfishSpecies;
import userinterface.GamePane;
import utilities.ArrayListUtilities;
import utilities.NumberUtilities;

/**
 * This is responsible for sending info to the server about the ClientSubOcean
 * conditions, receiving SeaCreature packets, adding them to the screen, and
 * providing action events for when a seaCreature is caught.
 * 
 * @author Matt Roberts
 *
 */
public class FishingActivityV2 {

	private SimpleFishingPane simpleFishingPane;

	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	private Player player;

	private ClientSubOcean clientSubOcean;
	private ClientFishingActivityFishManager clientFishingActivityFishManager;
	// private Image fishImage=Constants.getImage(FishSpecies.COD);
	// private ImageView fishImageView=new ImageView();

	// for debugging
	private Ocean ocean = new Ocean();

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
	public FishingActivityV2(
			SimpleFishingPane simpleFishingPane, /*
													 * ObjectOutputStream toServer,
													 * ObjectInputStream
													 * fromServer,
													 */
			Player player) {
		clientSubOcean = new ClientSubOcean();
		clientFishingActivityFishManager = new ClientFishingActivityFishManager(simpleFishingPane);
		this.simpleFishingPane = simpleFishingPane;
		this.player = player;
		this.toServer = toServer;
		this.fromServer = fromServer;
		testAddFishToFishingActivity();
		clientFishingActivityFishManager.doBasicClientSubOceanAnimation();
		// fishImageView.setImage(fishImage);
		// fishImageView.setOnMouseClicked(new
		// ExtractImageViewAction(fishImageView));
		// this.simpleFishingPane.getChildren().add(fishImageView);
		// updateSeaCreaturesOnScreen();
	}

	/**
	 * Requests new SeaCreatures from Server, and adds them to the screen if any
	 * are sent
	 */
	class UpdateFishOnScreenTask implements Runnable {

		public void run() {
			// updateSeaCreaturesOnScreen();
		}
	}

	/**
	 * Gets a SeaCreaturesPacket from the server, and adds all the Sea Creatures
	 * to the screen, and increments the ClientSubOcean count for all the
	 * SeaCreatures added
	 */
	/*
	 * private void updateSeaCreaturesOnScreen() {
	 * 
	 * SeaCreaturesPacket seaCreaturesPacket; seaCreaturesPacket =
	 * getUpdateSeaCreaturesPacketFromServer();
	 * 
	 * clientSubOcean.currentPopulationCod +=
	 * seaCreaturesPacket.codPopulation.size();
	 * addFishPacketToScreen(seaCreaturesPacket.codPopulation, 0, 0,
	 * Color.BLUE);
	 * 
	 * clientSubOcean.currentPopulationSalmon +=
	 * seaCreaturesPacket.salmonPopulation.size();
	 * addFishPacketToScreen(seaCreaturesPacket.salmonPopulation, 0, 0,
	 * Color.PINK);
	 * 
	 * clientSubOcean.currentPopulationTuna +=
	 * seaCreaturesPacket.tunaPopulation.size();
	 * addFishPacketToScreen(seaCreaturesPacket.tunaPopulation, 0, 0,
	 * Color.GRAY);
	 * 
	 * clientSubOcean.currentPopulationOyster +=
	 * seaCreaturesPacket.oysterPopulation.size();
	 * addShellfishPacketToScreen(seaCreaturesPacket.oysterPopulation, 0, 0,
	 * Color.YELLOW);
	 * 
	 * clientSubOcean.currentPopulationLobster +=
	 * seaCreaturesPacket.lobsterPopulation.size();
	 * addShellfishPacketToScreen(seaCreaturesPacket.lobsterPopulation, 0, 0,
	 * Color.RED);
	 * 
	 * //System.out.println("Adding crab to subocean: " +
	 * seaCreaturesPacket.crabPopulation.size());
	 * clientSubOcean.currentPopulationCrab +=
	 * seaCreaturesPacket.crabPopulation.size();
	 * addShellfishPacketToScreen(seaCreaturesPacket.crabPopulation, 0, 0,
	 * Color.BLACK);
	 * 
	 * }
	 */
	/**
	 * Actually makes the request for new SeaCreatures from the server and sends
	 * a new ClientSubOceanSeaCreatureStatePacket so that the server can no what
	 * to send, listens for SeaCreatures sent from server and returns any
	 * 
	 * @return The SeaCreatures sent by the server
	 */
	/*
	 * public SeaCreaturesPacket getUpdateSeaCreaturesPacketFromServer() { try {
	 * // System.out.println("in client // clientSubOcean.currentPopulationCod:
	 * // "+clientSubOcean.currentPopulationCod);
	 * ClientSubOceanSeaCreatureStatePacket clientSubOceanSeaCreatureStatePacket
	 * = new ClientSubOceanSeaCreatureStatePacket(
	 * clientSubOcean.currentPopulationCod, clientSubOcean.maxPopulationCod,
	 * clientSubOcean.currentPopulationSalmon,
	 * clientSubOcean.maxPopulationSalmon, clientSubOcean.currentPopulationTuna,
	 * clientSubOcean.maxPopulationTuna, clientSubOcean.currentPopulationOyster,
	 * clientSubOcean.maxPopulationOyster,
	 * clientSubOcean.currentPopulationLobster,
	 * clientSubOcean.maxPopulationLobster,
	 * clientSubOcean.currentPopulationCrab, clientSubOcean.maxPopulationCrab);
	 * // System.out.println("in client // clientSubOcean.currentPopulationCod:
	 * // "+clientSubOcean.currentPopulationCod); // System.out.println("in
	 * client // clientSubOcean.currentPopulationSalmon: //
	 * "+clientSubOcean.currentPopulationSalmon); // System.out.println("in
	 * client // clientSubOcean.currentPopulationTuna: //
	 * "+clientSubOcean.currentPopulationTuna); // System.out.println("in client
	 * // clientSubOcean.currentPopulationOyster: //
	 * "+clientSubOcean.currentPopulationOyster); // System.out.println("in
	 * client // clientSubOcean.currentPopulationLobster: //
	 * "+clientSubOcean.currentPopulationLobster); // System.out.println("in
	 * client // clientSubOcean.currentPopulationCrab: //
	 * "+clientSubOcean.currentPopulationCrab);
	 * toServer.writeObject(clientSubOceanSeaCreatureStatePacket); //
	 * System.out.println("Sent codStatePacket"); //
	 * System.out.println("before getting fish packet"); SeaCreaturesPacket
	 * seaCreaturesPacket = (SeaCreaturesPacket) fromServer.readObject(); //
	 * System.out.println("after getting fish packet"); return
	 * seaCreaturesPacket; } catch (IOException ex) {
	 * System.out.println(ex.toString()); } catch (ClassNotFoundException ex) {
	 * System.out.println(ex.toString()); // System.out.println("Over here"); }
	 * return new SeaCreaturesPacket(); }
	 */

	public void testAddFishToFishingActivity() {
		try {
			ArrayList<Fish> sampleCod = ocean.extractAndReturnABunchOfFish(FishSpecies.COD,
					(clientSubOcean.currentPopulationCod + 90), clientSubOcean.maxPopulationCod);
			clientFishingActivityFishManager.codPopulation.addAll(sampleCod);
			ArrayList<Fish> sampleSalmon = ocean.extractAndReturnABunchOfFish(FishSpecies.SALMON,
					(clientSubOcean.currentPopulationSalmon + 60), clientSubOcean.maxPopulationSalmon);
			clientFishingActivityFishManager.salmonPopulation.addAll(sampleSalmon);
			ArrayList<Fish> sampleTuna = ocean.extractAndReturnABunchOfFish(FishSpecies.TUNA,
					(clientSubOcean.currentPopulationTuna + 60), clientSubOcean.maxPopulationTuna);
			clientFishingActivityFishManager.tunaPopulation.addAll(sampleTuna);
			ArrayList<Shellfish> sampleLobsters = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.LOBSTER,
					(clientSubOcean.currentPopulationLobster + 20), clientSubOcean.maxPopulationLobster);
			clientFishingActivityFishManager.lobsterPopulation.addAll(sampleLobsters);
			ArrayList<Shellfish> sampleCrab = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.CRAB,
					(clientSubOcean.currentPopulationCrab + 60), clientSubOcean.maxPopulationCrab);
			clientFishingActivityFishManager.crabPopulation.addAll(sampleCrab);
			ArrayList<Shellfish> sampleOysters = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.OYSTER,
					(clientSubOcean.currentPopulationOyster + 40), clientSubOcean.maxPopulationOyster);
			clientFishingActivityFishManager.oysterPopuliation.addAll(sampleOysters);
			// System.out.println("sample cod: "+sampleCod.size());
			addFishPacketToScreen(sampleCod, 125, 100);
			addFishPacketToScreen(sampleSalmon, 125, 100);
			addFishPacketToScreen(sampleTuna, 125, 100);
			addShellfishPacketToScreen(sampleLobsters, 450, 20);
			addShellfishPacketToScreen(sampleCrab, 450, 20);
			addShellfishPacketToScreen(sampleOysters, 450, 20);
			// System.out.println("sample cod: "+sampleCod.size());
		} catch (Exception e) {

		}
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
		// fish.SetBodyByWeight();
		// fish.setBodyColor(color);
		fish.setFishBodyByWeight();
		System.out.println("set FishBody");
		// System.out.print(fish.getBody().toString());
		double width = simpleFishingPane.getMinWidth();
		double height = simpleFishingPane.getMinHeight();
		fish.getFishGraphic().getFishImageView().setTranslateX(
				NumberUtilities.getRandomDouble(0, width - fish.getFishGraphic().getFishImageView().getFitWidth()));
		fish.getFishGraphic().getFishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - fish.getFishGraphic().getFishImageView().getFitHeight()));
		// System.out.print(fish.getBody().toString());
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
		// shellfish.setBodyColor(color);
		// System.out.print(fish.getBody().toString());
		double width = simpleFishingPane.getMinWidth();
		double height = simpleFishingPane.getMinHeight();
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateX(NumberUtilities.getRandomDouble(0,
				width - shellfish.getShellfishGraphic().getShellfishImageView().getFitWidth()));
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - shellfish.getShellfishGraphic().getShellfishImageView().getFitHeight()));
		// System.out.print(fish.getBody().toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				simpleFishingPane.getChildren().add(shellfish.getShellfishGraphic().getShellfishImageView());
				shellfish.getShellfishGraphic().getShellfishImageView().setOnMouseClicked(new ExtractShellfishAction(shellfish));
				// shellfish.getBody().setOnMouseClicked(new
				// ExtractShellfishAction(shellfish));
			}
		});
	}

	/**
	 * Action that occurs when a fish is caught.
	 */
	private class OldExtractFishAction implements EventHandler<MouseEvent> {
		Fish fish;

		OldExtractFishAction(Fish fish) {
			this.fish = fish;
		}

		@Override
		public void handle(MouseEvent e) {
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try {
				// Something like this here?
				player.addSeaCreatureToIceChest(fish);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						simpleFishingPane.getChildren().remove(fish.getBody());
						if (fish.getSpecies() == FishSpecies.COD) {
							// System.out.println("It's a cod");
							clientSubOcean.currentPopulationCod--;
						}
						if (fish.getSpecies() == FishSpecies.SALMON) {
							// System.out.println("It's a salmon");
							clientSubOcean.currentPopulationSalmon--;
						}
						if (fish.getSpecies() == FishSpecies.TUNA) {
							// System.out.println("");
							clientSubOcean.currentPopulationTuna--;
						}

						// updateSeaCreaturesOnScreen();
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		};
	}
	/*
	 * takes a fishimageview removes node finds object remove fish
	 */

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
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try {
				// Something like this here?
				// player.addSeaCreatureToIceChest(fish);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(fish);
						simpleFishingPane.getChildren().remove(fish.getFishGraphic().getFishImageView());
						switch (fish.getSpecies()) {
						case COD:
							ArrayListUtilities.removeObjectFromArrayList(clientFishingActivityFishManager.codPopulation,
									fish);
							System.out.println("Cod: "+clientFishingActivityFishManager.codPopulation.size());
							clientSubOcean.currentPopulationCod--;
							break;
						case SALMON:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.salmonPopulation, fish);
							System.out.println("Salmon: "+clientFishingActivityFishManager.salmonPopulation.size());
							clientSubOcean.currentPopulationSalmon--;
							break;

						case TUNA:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.tunaPopulation, fish);
							System.out.println("Tuna: "+clientFishingActivityFishManager.tunaPopulation.size());
							clientSubOcean.currentPopulationTuna--;
							break;
						}
						/*
						 * if (fish.getSpecies() == FishSpecies.COD) {
						 * //System.out.println("It's a cod");
						 * clientSubOcean.currentPopulationCod--; } if
						 * (fish.getSpecies() == FishSpecies.SALMON) {
						 * //System.out.println("It's a salmon");
						 * clientSubOcean.currentPopulationSalmon--; } if
						 * (fish.getSpecies() == FishSpecies.TUNA) {
						 * //System.out.println("");
						 * clientSubOcean.currentPopulationTuna--; }
						 */
						// updateSeaCreaturesOnScreen();
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
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
			// System.out.println("Extract fish action "
			// + "triggered(fish caught)");
			try {
				// Something like this here?
				//player.addSeaCreatureToIceChest(shellfish);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(shellfish);
						simpleFishingPane.getChildren().remove(shellfish.getShellfishGraphic().getShellfishImageView());
						switch (shellfish.getSpecies()) {
						case LOBSTER:
							ArrayListUtilities.removeObjectFromArrayList(clientFishingActivityFishManager.lobsterPopulation,
									shellfish);
							System.out.println("Lobster: "+clientFishingActivityFishManager.lobsterPopulation.size());
							clientSubOcean.currentPopulationLobster--;
							break;
						case CRAB:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.crabPopulation, shellfish);
							System.out.println("Crab: "+clientFishingActivityFishManager.crabPopulation.size());
							clientSubOcean.currentPopulationCrab--;
							break;

						case OYSTER:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.oysterPopuliation, shellfish);
							System.out.println("Oyster: "+clientFishingActivityFishManager.oysterPopuliation.size());
							
							clientSubOcean.currentPopulationOyster--;
							break;
						}
						
						/*
						simpleFishingPane.getChildren().remove(shellfish.getBody());
						if (shellfish.getSpecies() == ShellfishSpecies.CRAB)
							clientSubOcean.currentPopulationCrab--;
						if (shellfish.getSpecies() == ShellfishSpecies.LOBSTER)
							clientSubOcean.currentPopulationLobster--;
						if (shellfish.getSpecies() == ShellfishSpecies.OYSTER)
							clientSubOcean.currentPopulationOyster--;
						*/
						// updateSeaCreaturesOnScreen();
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		};
	}

	/**
	 * For keeping track of all the SeaCreatures on the client's side.
	 */
	class ClientSubOcean {

		int currentPopulationCod = 0;
		int maxPopulationCod = Constants.COD_MAX_POPULATION / 10;
		int currentPopulationSalmon = 0;
		int maxPopulationSalmon = Constants.SALMON_MAX_POPULATION / 10;
		int currentPopulationTuna = 0;
		int maxPopulationTuna = Constants.TUNA_MAX_POPULATION / 10;
		int currentPopulationOyster = 0;
		int maxPopulationOyster = Constants.OYSTER_MAX_POPULATION / 10;
		int currentPopulationLobster = 0;
		int maxPopulationLobster = Constants.LOBSTER_MAX_POPULATION / 10;
		int currentPopulationCrab = 0;
		int maxPopulationCrab = Constants.CRAB_MAX_POPULATION / 10;
	}
}