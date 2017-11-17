package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Equipment;
import resources.SeaCreature;

public class Player extends authentication.User implements Serializable
{
	// stats
	double cashOnHand;
	private int skillLevel;
	
	// resources
	private ArrayList<SeaCreature> iceChest = new ArrayList<>();
	private ArrayList<Equipment> toolChest = new ArrayList<>();

	
	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.skillLevel = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
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

}
