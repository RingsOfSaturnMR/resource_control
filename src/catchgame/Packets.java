package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Fish;
import resources.SeaCreature;
import resources.Shellfish;

public class Packets
{
	public static class RequestPacket implements Serializable
	{
		public int code;

		RequestPacket(int requestCode)
		{
			this.code = requestCode;
		}
	}

	public static class ResultPacket implements Serializable
	{
		public int code;

		ResultPacket(int resultCode)
		{
			this.code = resultCode;
		}
	}

	public static class LoginPacket implements Serializable
	{
		public String enteredName;
		public String enteredPassword;

		public LoginPacket(String name, String password)
		{
			this.enteredName = name;
			this.enteredPassword = password;
		}
	}

	public static class NewUserPacket implements Serializable
	{
		public String enteredName;
		public String enteredPassword;
		public String enteredPasswordConfirm;

		public NewUserPacket(String name, String password, String passwordConfirm)
		{
			this.enteredName = name;
			this.enteredPassword = password;
			this.enteredPasswordConfirm = passwordConfirm;
		}
	}

	public static class SeaCreaturesPacket implements Serializable
	{
		ArrayList<Fish> codPopulation = new ArrayList<>();
		ArrayList<Fish> salmonPopulation = new ArrayList<>();
		ArrayList<Fish> tunaPopulation = new ArrayList<>();
		ArrayList<Shellfish> oysterPopulation = new ArrayList<>();
		ArrayList<Shellfish> lobsterPopulation = new ArrayList<>();
		ArrayList<Shellfish> crabPopulation = new ArrayList<>();

		SeaCreaturesPacket(ArrayList<Fish> codPopulation, ArrayList<Fish> salmonPopulation,
				ArrayList<Fish> tunaPopulation, ArrayList<Shellfish> oysterPopulation,
				ArrayList<Shellfish> lobsterPopulation, ArrayList<Shellfish> crabPopulation)
		{
			this.codPopulation = codPopulation;
			this.salmonPopulation=salmonPopulation;
			this.tunaPopulation=tunaPopulation;
			this.oysterPopulation=oysterPopulation;
			this.lobsterPopulation=lobsterPopulation;
			this.crabPopulation=crabPopulation;
		}
		SeaCreaturesPacket(){
			
		}
		// ArrayList<Fish>salmonPopulation = new ArrayList<>();
		// ArrayList<Fish>tunaPopulation = new ArrayList<>();

		// resounces.ShellFishSpecies
		// ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
		// ArrayList<Shellfish>crabPopulation = new ArrayList<>();
		// ArrayList<Fish>oysterPopuliation = new ArrayList<>();
	}

	
	public static class ClientSubOceanSeaCreatureStatePacket implements Serializable
	{
		int currentPopulationCod=0;
		int maxPopulationCod=Constants.COD_MAX_POPULATION/10;
		int currentPopulationSalmon=0;
		int maxPopulationSalmon=Constants.SALMON_MAX_POPULATION/10;
		int currentPopulationTuna=0;
		int maxPopulationTuna=Constants.TUNA_MAX_POPULATION/10;
		int currentPopulationOyster=0;
		int maxPopulationOyster=Constants.OYSTER_MAX_POPULATION/10;
		int currentPopulationLobster=0;
		int maxPopulationLobster=Constants.LOBSTER_MAX_POPULATION/10;
		int currentPopulationCrab=0;
		int maxPopulationCrab=Constants.CRAB_MAX_POPULATION/10;

		public ClientSubOceanSeaCreatureStatePacket(int currentPopulationCod, int maxPopulationCod,
				int currentPopulationSalmon, int maxPopulationSalmon,
				int currentPopulationTuna, int maxPopulationTuna,
				int currentPopulationOyster, int maxPopulationOyster,
				int currentPopulationLobster, int maxPopulationLobster,
				int currentPopulationCrab, int maxPopulationCrab)
		{
			this.currentPopulationCod = currentPopulationCod;
			this.maxPopulationCod = maxPopulationCod;
			this.currentPopulationSalmon=currentPopulationSalmon;
			this.maxPopulationSalmon=maxPopulationSalmon;
			this.currentPopulationTuna=currentPopulationTuna;
			this.maxPopulationTuna=maxPopulationTuna;
			this.currentPopulationOyster=currentPopulationOyster;
			this.maxPopulationOyster=maxPopulationOyster;
			this.currentPopulationLobster=currentPopulationLobster;
			this.maxPopulationLobster=maxPopulationLobster;
			this.currentPopulationCrab=currentPopulationCrab;
			this.maxPopulationCrab=maxPopulationCrab;
			
		}

	}
	

}
