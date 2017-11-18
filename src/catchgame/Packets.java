package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import resources.Fish;
import resources.SeaCreature;

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

	public static class FishPacketsPacket implements Serializable
	{
		ArrayList<Fish> codPopulation = new ArrayList<>();

		FishPacketsPacket(ArrayList<Fish> codPopulation)
		{
			this.codPopulation = codPopulation;
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
		int currentPopulationCod;
		int maxPopulationCod;

		public ClientSubOceanSeaCreatureStatePacket(int currentPopulationCod, int maxPopulationCod)
		{
			this.currentPopulationCod = currentPopulationCod;
			this.maxPopulationCod = maxPopulationCod;
		}

	}

}
