package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import catchgame.Constants;

public class Ocean implements Serializable
{
	// have a population for each FishSpecies and ShellfishSpecies
	// resounces.FishSpecies
	private ArrayList<Fish>codPopulation = new ArrayList<>();
	private ArrayList<Fish>salmonPopulation = new ArrayList<>(); 
	private ArrayList<Fish>tunaPopulation = new ArrayList<>();
	
	// resounces.ShellFishSpecies
	private ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
	private ArrayList<Shellfish>crabPopulation = new ArrayList<>(); 
	private ArrayList<Fish>oysterPopuliation = new ArrayList<>();
	
	// for randomly generating weights
	private Random rand = new Random();
	
	public Ocean()
	{
		// populate each array list with appropriate SeaCreatures.
		// Randomly generate each SeaCreature's weight by randomly generating a number between
		// the INITIAL_WEIGHT_MIN and INITAL_WEIGHT_MAX
		// get values from catchgame.Constants
		System.out.println("In the Ocean Constructor");
	}
	
	
	public SeaCreature extractRandomSeaCreature()
	{
		// STUBB
		int random = getRandomInt(0, 6);
		
		switch (random)
		{
		case 0:
			return new Fish(FishSpecies.COD, 1);
		case 1:
			return new Fish(FishSpecies.SALMON, 1);
		case 2:
			return new Fish(FishSpecies.TUNA, 1);
		case 3:
			return new Shellfish(ShellfishSpecies.CRAB, 1);
		case 4:
			return new Shellfish(ShellfishSpecies.LOBSTER, 1);
		case 5:
			return new Shellfish(ShellfishSpecies.OYSTER, 1);	
		default:
			return null;
		}
	}
	
	public Fish extractCod()
	{
		return new Fish(FishSpecies.COD, 1);
	}
	
	public Fish extractSalmon()
	{
		return new Fish(FishSpecies.SALMON, 1);
	}
	
	public Fish extractTuna()
	{
		return new Fish(FishSpecies.TUNA, 1);
	}
	
	public Shellfish extractCrab()
	{
		return new Shellfish(ShellfishSpecies.CRAB, 1);
	}
	
	public Shellfish extractLobster()
	{
		return new Shellfish(ShellfishSpecies.LOBSTER, 1);
	}
	
	public Shellfish extractOyster()
	{
		return new Shellfish(ShellfishSpecies.OYSTER, 1);
	}
	
	public int getCurrentCodPopulation()
	{
		int random = getRandomInt(0, Constants.COD_INITIAL_POPULATION);
		return random;
	}
	
	public int getCurrentSalmonPopulation()
	{
		int random = getRandomInt(0, Constants.SALMON_INITIAL_POPULATION);
		return random;
	}
	
	public int getCurrentTunaPopulation()
	{
		int random = getRandomInt(0, Constants.TUNA_INITIAL_POPULATION);
		return random;
	}
	
	public int getCurrentCrabPopulation()
	{
		int random = getRandomInt(0, Constants.CRAB_INITIAL_POPULATION);
		return random;
	}
	
	public int getCurrentLobsterPopulation()
	{
		int random = getRandomInt(0, Constants.LOBSTER_INITIAL_POPULATION);
		return random;
	}
	
	public int getCurrentOysterPopulation()
	{
		int random = getRandomInt(0, Constants.OYSTER_INITIAL_POPULATION);
		return random;
	}
	
	
	// helper function
	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max) + min;
		return randomInt;
	}
}
