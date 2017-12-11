package resources;

import graphicclasses.ShellfishGraphic;

public class Shellfish extends SeaCreature <ShellfishSpecies>
{
	
	ShellfishGraphic shellfishGraphic=null;
	
	public Shellfish(ShellfishSpecies species, double weight)
	{
		super(species, weight);
		super.setSpeed(1);
	}
	
	public void setShellfishBodyByWeight(){
		shellfishGraphic=new ShellfishGraphic(this);
	}
	
	public ShellfishGraphic getShellfishGraphic(){
		return shellfishGraphic;
	}
	
}
