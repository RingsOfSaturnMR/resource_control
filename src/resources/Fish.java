package resources;

import java.io.Serializable;

import graphicclasses.FishGraphic;

public class Fish extends SeaCreature<FishSpecies> implements Serializable
{
	
	FishGraphic fishGraphic=null;

	public Fish(){
		super();
	}
	
	public Fish(FishSpecies species, double weight)
	{
		super(species, weight);
		super.setSpeed(10);
	}
	
	public void setFishBodyByWeight(){
		fishGraphic=new FishGraphic(this);
	}
	
	public FishGraphic getFishGraphic(){
		return fishGraphic;
	}
}
