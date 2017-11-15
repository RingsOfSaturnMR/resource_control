package catchgame;

import java.io.Serializable;

public class ClientSubOceanSeaCreatureStatePacket implements Serializable{
	int currentPopulationCod;
	int maxPopulationCod;
	
	public ClientSubOceanSeaCreatureStatePacket(int currentPopulationCod, int maxPopulationCod){
		this.currentPopulationCod=currentPopulationCod;
		this.maxPopulationCod=maxPopulationCod;
	}
	
	
}