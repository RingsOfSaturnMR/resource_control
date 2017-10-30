package catchgame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import resources.Fish;

public class ClientSubOcean extends AbstractOcean{
	
	ObjectOutputStream toServer;
	ObjectInputStream fromServer;
	
	ClientSubOcean(ObjectOutputStream toServer, ObjectInputStream fromServer){
		this.toServer=toServer;
		this.fromServer=fromServer;
	}
	
	 public Fish extractFish(ArrayList<Fish>fishPopulation) throws Exception{
		 Fish fish=fishPopulation.get(fishPopulation.size()-1);
			fishPopulation.remove(fishPopulation.size()-1);
			
			return fish;
	}
	
	 /*
	 public void updateFishPopulationsFromServer(){
		 SubOceanFishStatePacket codStatePacket=new SubOceanFishStatePacket(super.codPopulation.size(), 100);
		 try{
		 toServer.writeObject(codStatePacket);
		 System.out.println("Sent codStatePacket");
		 FishPacketsPacket fishPacketPacket=(FishPacketsPacket)fromServer.readObject();
		 addPacketOfCod(fishPacketPacket.codPopulation);
		 }catch(IOException ex){
			 System.out.println(ex.toString());
		 }catch(ClassNotFoundException ex){
			 System.out.println(ex.toString());
			 System.out.println("Over here");
		 }
	 }
	 */
	 
	 public void addPacketOfCod(ArrayList<Fish> codPopulation){
		 super.codPopulation.addAll(codPopulation);
	 }
}
