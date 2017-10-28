package catchgame;

import java.util.ArrayList;

import resources.Equipment;
import resources.SeaCreature;
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
	String name;
	String password;
	double cashOnHand;
	private ArrayList<SeaCreature> iceChest = new ArrayList<>();
	// TODO, new name
	private ArrayList<Equipment> toolChest = new ArrayList<>();
	private int skillLevel;

	public Player(String name, String password, double cashOnHand, ArrayList<SeaCreature> iceChest, ArrayList<Equipment> toolChest)
	{
		this.name = name;
		this.password = password;
		this.cashOnHand = cashOnHand;
		this.iceChest = iceChest;
		this.toolChest = toolChest;
	}
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
