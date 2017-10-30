package resources;

import java.io.Serializable;

public class Fish extends SeaCreature<FishSpecies> implements Serializable
{
	public Fish(){
		super();
	}
	
	public Fish(FishSpecies species, double weight)
	{
		super(species, weight);
	}
}
