package resources;

import java.util.ArrayList;

public class ShellFishBushel
{
	private ShellFishSpecies speciesInBushel;
	private double totalBushelWeight = 0;
	private ArrayList <ShellFish> items = new ArrayList<>();
	
	public ShellFishBushel(ShellFishSpecies species)
	{
		setBushelContent(species);
	}

	public void add(ShellFish item)
	{
		if(item.getSpecies() == speciesInBushel)
		{
			items.add(item);
		}
		else
		{
			System.out.println("This bushel is for species type " + speciesInBushel.toString() );
		}
		
		updateTotalBushelWeight();
	}

	
	private void updateTotalBushelWeight()
	{
		totalBushelWeight += items.get(items.size()).getWeight();
	}

	public double getWeight()
	{
		return this.totalBushelWeight;
	}

	public ShellFishSpecies getBushelContent()
	{
		return speciesInBushel;
	}

	public void setBushelContent(ShellFishSpecies species)
	{
		this.speciesInBushel = species;
	}
}
