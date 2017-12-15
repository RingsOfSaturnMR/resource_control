package catchgame;

import java.io.Serializable;
import java.util.ArrayList;
import authentication.User;
import catchgame.GameControl.SendStatsHandler;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import resources.BoatTypes;
import resources.Equipment;
import resources.FishSpecies;
import resources.SeaCreature;
import resources.ShellfishSpecies;
import resources.SimpleFishingItemType;

/**
 * @author Nils
 * This class contains all of the resources and stats that belong to the player.
 * It extends authentication.User so that a Player can be authenticated.
 */
public class Player extends User implements Serializable
{
	// general stats
	private double cashOnHand;
	private double totalEarned;
	private int totalCatches;;

	// arrays to hold resources, when serializing
	private SeaCreature[] iceChestArray = null;
	private Equipment[] toolChestArray = null;

	// resources as observable lists, for gameplay
	private transient ObservableList<Equipment> toolChest = null;
	private transient ObservableList<SeaCreature> iceChest = null;

	// flag to mark if the arrays need to be copied to the observable lists
	private boolean observableListsLoaded = false;

	// allows the player to send stats to the server
	private transient SendStatsHandler sendStatsHandler = null;

	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.totalEarned = 0;
		this.totalCatches = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
		if (!observableListsLoaded)
		{
			loadObservableLists();
		}

		iceChest.add(item);
		totalCatches++;
		sendStatsToServer();
	}

	public void addItemToToolChest(Equipment item)
	{
		if (!observableListsLoaded)
		{
			loadObservableLists();
		}

		toolChest.add(item);
		sendStatsToServer();
	}

	public double getCashOnHand()
	{
		return this.cashOnHand;
	}

	public void addMoney(double amount)
	{
		cashOnHand += amount;
		totalEarned += amount;
		sendStatsToServer();
	}

	public void subtractMoney(double amount)
	{
		cashOnHand -= amount;
		sendStatsToServer();
	}

	/**
	 * Must be called prior to serialization. Converts Observable Lists to primitive
	 * arrays.
	 */
	public void prepareToSerialze()
	{
		if (iceChest != null)
		{
			iceChestArray = new SeaCreature[iceChest.size()];

			for (int i = 0; i < iceChestArray.length; i++)
			{
				iceChestArray[i] = iceChest.get(i);
			}
		}

		if (toolChest != null)
		{
			toolChestArray = new Equipment[toolChest.size()];

			for (int i = 0; i < toolChestArray.length; i++)
			{
				toolChestArray[i] = toolChest.get(i);
			}
		}

		observableListsLoaded = false;
	}

	/**
	 * Copies primitive arrays to observable Lists. Will get called if an
	 * observable is null because this object was recently deserialized.
	 */
	private void loadObservableLists()
	{
		// if iceChest is null and the iceChestArray is not null, it means that this
		// object was recently deserialized and needs to have the array copied to the
		// observable list
		if (iceChest == null && iceChestArray != null)
		{
			iceChest = FXCollections.observableArrayList(iceChestArray);
		}
		// if both are null, it means no SeaCreatures have ever been caught. Set the
		// iceChest to be empty;
		else if (iceChest == null && iceChestArray == null)
		{
			iceChest = FXCollections.observableArrayList();
		}

		// follows the same pattern as iceChest and iceChestArray
		if (toolChest == null && toolChestArray != null)
		{
			toolChest = FXCollections.observableArrayList(toolChestArray);
		}

		else if (toolChest == null && toolChestArray == null)
		{
			toolChest = FXCollections.observableArrayList();
		}

		observableListsLoaded = true;
	}

	/**
	 * @param species that you wish to know how many the player has.
	 * @return the number of that species the player has.
	 */
	public int getNumOf(Enum<?> species)
	{
		if (!observableListsLoaded)
		{
			loadObservableLists();
		}
		
		int numSpecies = 0;
		
		for(SeaCreature<?> creature: iceChest)
		{
			if (creature.getSpecies() == species)
			{
				numSpecies++;
			}
		}
		return numSpecies;
	}

	public SeaCreature<?> getSeaNextSeaCreature(Enum<?> species)
	{
		boolean creatureFound = false;
		int i = 0;
		SeaCreature<?> creature = null;

		while (!creatureFound && i < iceChest.size())
		{
			if (iceChest.get(i).getSpecies() == species)
			{
				creature = iceChest.get(i);
				creatureFound = true;
				iceChest.remove(i);
			}
			i++;
		}

		return creature;
	}

	/**
	 * @return the total ammont earned
	 */
	public double getTotalEarned()
	{
		return this.totalEarned;
	}

	/**
	 * @return the total SeaCreatures caught
	 */
	public int getTotalCatches()
	{
		return totalCatches;
	}

	/**
	 * Sends information about players stats to server
	 */
	public void setStatSendHandler(SendStatsHandler sendStatsHandler)
	{
		this.sendStatsHandler = sendStatsHandler;
	}

	/**
	 * will send information to server if the handler is set
	 */
	private void sendStatsToServer()
	{
		if (sendStatsHandler != null)
		{
			sendStatsHandler.send();
		}
	}

	/**
	 * Puts a listener on the iceChest List
	 * @param iceChestChangeListener, the listener to be attached
	 */
	public void addIceChestListner(ListChangeListener<? super SeaCreature> iceChestChangeListener)
	{
		this.iceChest.addListener(iceChestChangeListener);
	}
	
	/**
	 * Puts a listener on the toolChest List
	 * @param toolChestChangeListner, the listener to be attached
	 */
	public void addToolChestListner(ListChangeListener<? super Equipment> toolChestChangeListner)
	{
		this.toolChest.addListener(toolChestChangeListner);
	}
}
