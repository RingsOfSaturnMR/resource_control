package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Equipment;
import resources.SeaCreature;

public class Player extends authentication.User implements Serializable
{
	// stats
	double cashOnHand;
	private int skillLevel;
	
	// resources
	// to be serialized
	private SeaCreature[] iceChestArray = null;
	private ArrayList<Equipment> toolChest = new ArrayList<>();
	
	private transient ObservableList<SeaCreature> iceChest = FXCollections.observableArrayList();
	
	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.skillLevel = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
		if(iceChest == null)
		{
			iceChest = FXCollections.observableArrayList(iceChestArray);
		}
		iceChest.add(item);
	}
	
	public void addItemToToolChest(Equipment item)
	{
		toolChest.add(item);
	}
	
	public double getCashOnHand()
	{
		return this.cashOnHand;
	}

	public int getSkillLevel()
	{
		return skillLevel;
	}
	
	public void addMoney(int amount)
	{
		cashOnHand += amount;
	}
	
	public void subtractMoney(int amount)
	{
		cashOnHand -= amount;
	}
	
	public ObservableList<SeaCreature> getIceChest()
	{
		return this.iceChest;
	}
	
	/**
	 * Must be called prior to serialization.
	 * Converts Observable Lists to primitive arrays. 
	 */
	public void prepareToSerialze()
	{
		iceChestArray = new SeaCreature[iceChest.size()];
		
		for(int i = 0; i < iceChestArray.length; i++)
		{
			iceChestArray[i] = iceChest.get(i);
		}
	}

}
