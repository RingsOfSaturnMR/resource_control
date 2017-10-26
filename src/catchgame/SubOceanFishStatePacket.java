package catchgame;

import java.io.Serializable;

public class SubOceanFishStatePacket implements Serializable{
	int currentPopulation;
	int maxPopulation;
	
	public SubOceanFishStatePacket(int currentPopulation, int maxPopulation){
		this.currentPopulation=currentPopulation;
		this.maxPopulation=maxPopulation;
	}
	
	
}