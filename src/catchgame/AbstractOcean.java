package catchgame;

import java.util.ArrayList;
import java.util.Random;

import resources.Fish;
import resources.FishSpecies;
import resources.SeaCreature;
import resources.Shellfish;
import resources.ShellfishSpecies;

public abstract class AbstractOcean
{
	// have a population for each FishSpecies and ShellfishSpecies
	// resounces.FishSpecies
	protected ArrayList<Fish>codPopulation = new ArrayList<>();
	protected ArrayList<Fish>salmonPopulation = new ArrayList<>(); 
	protected ArrayList<Fish>tunaPopulation = new ArrayList<>();
	
	// resounces.ShellFishSpecies
	protected ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
	protected ArrayList<Shellfish>crabPopulation = new ArrayList<>(); 
	protected ArrayList<Fish>oysterPopuliation = new ArrayList<>();
	
	// for randomly generating weights
	private Random rand = new Random();

	public AbstractOcean()
	{
		
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
	
	abstract public Fish extractFish(ArrayList<Fish>fishPopulation) throws Exception;
	
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
	
	
	// get random weight of each species
	public double getCodWeight() {
		double random = getRandomDouble(Constants.COD_INITIAL_WEIGHT_MIN, Constants.COD_INITIAL_WEIGHT_MAX);
		return random;
	}
		
	public double getSalmonWeight() {
			
		double random = getRandomDouble(Constants.SALMON_INITIAL_WEIGHT_MIN, Constants.SALMON_INITIAL_WEIGHT_MAX);
		return random;
	}
	
	public double getTunaWeight() {
		double random = getRandomDouble(Constants.TUNA_INITIAL_WEIGHT_MIN, Constants.TUNA_INITIAL_WEIGHT_MAX);
		return random;
	}
		
	public double getCrabWeight() {
		double random = getRandomDouble(Constants.CRAB_INITIAL_WEIGHT_MIN, Constants.CRAB_INITIAL_WEIGHT_MAX);
		return random;
	}
		
	public double getLobsterWeight() {
		double random = getRandomDouble(Constants.LOBSTER_INITIAL_WEIGHT_MIN, Constants.LOBSTER_INITIAL_WEIGHT_MAX);
		return random;
	}
		
	public double getOysterWeight() {
		double random = getRandomDouble(Constants.OYSTER_INITIAL_WEIGHT_MIN, Constants.OYSTER_INITIAL_WEIGHT_MAX);
		return random;
	}
	
		// helper function
	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max) + min;
		return randomInt;
	}
	private double getRandomDouble(double max, double min) {
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}
}
