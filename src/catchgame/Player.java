package catchgame;

import java.util.ArrayList;
import resources.SeaCreature;
import resources.Equipment;
import resources.Fish;
/*
TODO

1.) getters/setters for the following
	String - name
	double - cashOnHand
	int - skillLevel
	ArrayList<SeaCreature> IceChest
	ArrayList<Equipment> EquipList
	
2.) constructor that sets all the values	

*/
public class Player
{
	String name = "Player_Name";
	double cashOnHand = 10.0;
	private ArrayList<SeaCreature> iceChest = new ArrayList<>();
	private ArrayList<Equipment> toolChest = new ArrayList<>();
	private int skillLevel;

	public void addItemToIceChest(SeaCreature item)
	{
		iceChest.add(item);
	}
	
	public void setCashOnHand(double val)
	{
		this.cashOnHand = val;
	}

	public double getCashOnHand()
	{
		return 12.3;
	}
	
	public String getName()
	{
		return this.name;
	}

	public int getSkillLevel()
	{
		return this.skillLevel;
	}
	
}
