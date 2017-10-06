package resources;

import java.util.ArrayList;

public class Boat
{
	private String boatName; 
	private double boatLength;
	private double costPerDay;
	private int fishCapacity; // how many fish it can hold before unloading
	
	public Boat(String id, double len)
	{
		try {
			this.setName(id);
			this.setLength(len);
		} catch (Throwable e) throw e; // re throw to main

		// now set costPerDay and capacity based on length
		setCostAndCapacity();
	}

	public String getName() { return boatName; }

	private void setName(String id) { this.boatName = id; }

	private void setLength(String len)
	{
		if (len <= 0) throw new Throwable("Invalid length");
		this.boatLength = len;
	}

	private void setCostAndCapacity() // could do sep func 
	{	// the literals are arbitrary
		if (this.length < 10)
		{ // could be another constant
			this.costPerDay = this.length * 5;
			this.fishCapacity = this.length * 5;
		}
		else if (this.length >= 10 && this.length < 100)
		{ // square
			this.costPerDay = this.length * this.length;
			this.fishCapacity = this.length * this.length;
		}
		else 
		{ // cube
			this.costPerDay = this.length * this.length * this.length;
			this.fishCapacity = this.length * this.length * this.length;
		}
	}
	
	public Boat sellBoat(String newID) {
		setName(newID);
	}

	public double repair(String issue){ // could be an enum
		// returns cost of repair
		// switch case for repair costs if we want to have this functionality
		return 0;
	}
	
}