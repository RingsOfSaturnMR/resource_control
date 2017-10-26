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
			SubOceanFishStatePacket subOceanFishStatePacket=
					(SubOceanFishStatePacket)fromClient.readObject();
			ArrayList<Fish> codPacket=ocean.givePacketOfFish(FishSpecies.COD,
					subOceanFishStatePacket.currentPopulation, 
					subOceanFishStatePacket.maxPopulation);
			System.out.println("cod cuurent population: "
					+subOceanFishStatePacket.currentPopulation);
			System.out.println("cod max population: "
					+subOceanFishStatePacket.maxPopulation);
			System.out.println("Num cod in packet:"+codPacket.size());
			toClient.writeObject(new FishPacketsPacket(codPacket));
			}catch(ClassNotFoundException ex){
				
			}catch(IOException ex){
				
			}
			catch(Exception ex){
				
			}
		}
	}
}
