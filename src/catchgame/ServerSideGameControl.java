package catchgame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import resources.Fish;
import resources.FishSpecies;

public class ServerSideGameControl {

	ServerSideGameControl(ObjectOutputStream toClient, 
			ObjectInputStream fromClient, Ocean ocean){
		while(true){
			try{
				ClientSubOceanSeaCreatureStatePacket subOceanFishStatePacket=
					(ClientSubOceanSeaCreatureStatePacket)fromClient.readObject();
			ArrayList<Fish> codPacket=ocean.givePacketOfFish(FishSpecies.COD,
					subOceanFishStatePacket.currentPopulationCod, 
					subOceanFishStatePacket.maxPopulationCod);
			//System.out.println("cod cuurent population: "
					//+subOceanFishStatePacket.currentPopulationCod);
			//System.out.println("cod max population: "
					//+subOceanFishStatePacket.maxPopulationCod);
			//System.out.println("Num cod in packet:"+codPacket.size());
			toClient.writeObject(new FishPacketsPacket(codPacket));
			}catch(ClassNotFoundException ex){
				
			}catch(IOException ex){
				
			}
			catch(Exception ex){
				
			}
		}
	}
}
