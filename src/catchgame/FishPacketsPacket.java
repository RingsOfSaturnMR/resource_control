package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Fish;
import resources.Shellfish;

public class FishPacketsPacket implements Serializable{
	ArrayList<Fish>codPopulation = new ArrayList<>();
	
	FishPacketsPacket(ArrayList<Fish>codPopulation){
		this.codPopulation=codPopulation;
	}
	//ArrayList<Fish>salmonPopulation = new ArrayList<>(); 
	//ArrayList<Fish>tunaPopulation = new ArrayList<>();
	
	// resounces.ShellFishSpecies
	//ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
	//ArrayList<Shellfish>crabPopulation = new ArrayList<>(); 
	//ArrayList<Fish>oysterPopuliation = new ArrayList<>();
}
