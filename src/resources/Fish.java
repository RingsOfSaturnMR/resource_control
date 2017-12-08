package resources;

import java.io.Serializable;

<<<<<<< HEAD
import graphicclasses.FishGraphic;

public class Fish extends SeaCreature<FishSpecies> implements Serializable
{
	
	FishGraphic fishGraphic=null;
	
=======
public class Fish extends SeaCreature<FishSpecies> implements Serializable
{
>>>>>>> nils_branch
	public Fish(){
		super();
	}
	
	public Fish(FishSpecies species, double weight)
	{
		super(species, weight);
	}
	
	public void setFishBodyByWeight(){
		fishGraphic=new FishGraphic(this);
	}
	
	public FishGraphic getFishGraphic(){
		return fishGraphic;
	}
}
