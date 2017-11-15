package catchgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import resources.Fish;
import resources.FishSpecies;
import resources.SeaCreature;
import resources.Shellfish;
import resources.ShellfishSpecies;

/**
 * This class is a wrapper for SeaCreatures. It has methods to pull creatures
 * from the population, and will regenerate itself to its max 'carrying
 * capacity' based on how many resouces have been extracted.
 * 
 * @author Thanh Lam
 *
 */
public class Ocean implements Serializable
{
	// have a population for each FishSpecies and ShellfishSpecies
	// resounces.FishSpecies
	private ArrayList<Fish> codPopulation = new ArrayList<>();
	private ArrayList<Fish> salmonPopulation = new ArrayList<>();
	private ArrayList<Fish> tunaPopulation = new ArrayList<>();

	// resounces.ShellFishSpecies
	private ArrayList<Shellfish> lobsterPopulation = new ArrayList<>();
	private ArrayList<Shellfish> crabPopulation = new ArrayList<>();
	private ArrayList<Shellfish> oysterPopulation = new ArrayList<>();

	// for randomly generating weights
	private Random rand = new Random();
	private double relativeGrowthRate = 0.02;

	public Ocean()
	{
		//System.out.println("In the Ocean Constructor");
		boolean newGame = true;
		if (newGame)
		{
			fillOceanInitially();
		}
		else
		{
			// load old ocean
		}
		regenerateOcean();

	}

	public void regenerateOcean()
	{
		Timer timer = new Timer();
		TimerTask task;
		task = new TimerTask()
		{

			@Override
			public void run()
			{
				int codToAdd = updateCodPopulation(10);
				// System.out.println("Cod to add "+codToAdd);
				addABunchOfFish(codPopulation, FishSpecies.COD, codToAdd);
			}

		};
		timer.schedule(task, 0, 5_000);
	}

	private int updateCodPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.COD_MAX_POPULATION;
		int lastPopulation = codPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		// make it an int becuase we can't have (viable) fractions of seafood
		int updatedPopulation = (int) rawPopulation;
		// next line for debug
		//System.out.println(updatedPopulation);
		return updatedPopulation - codPopulation.size();
	}

	private int updateSalmonPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.SALMON_MAX_POPULATION;
		int lastPopulation = salmonPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		int updatedPopulation = (int) rawPopulation;
		//System.out.println(updatedPopulation);
		return updatedPopulation - salmonPopulation.size();

	}

	private int updateTunaPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.TUNA_MAX_POPULATION;
		int lastPopulation = tunaPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		int updatedPopulation = (int) rawPopulation;
		System.out.println(updatedPopulation);
		return updatedPopulation - tunaPopulation.size();

	}

	private int updateOysterPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.OYSTER_MAX_POPULATION;
		int lastPopulation = oysterPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		int updatedPopulation = (int) rawPopulation;

		System.out.println(updatedPopulation);

		return updatedPopulation - oysterPopulation.size();
	}

	private int updateLobsterPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.LOBSTER_MAX_POPULATION;
		int lastPopulation = lobsterPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		int updatedPopulation = (int) rawPopulation;

		System.out.println(updatedPopulation);
		return updatedPopulation - lobsterPopulation.size();
	}

	private int updateCrabPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.CRAB_MAX_POPULATION;
		int lastPopulation = crabPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		int updatedPopulation = (int) rawPopulation;

		System.out.println(updatedPopulation);

		return updatedPopulation - crabPopulation.size();
	}

	// this is a general algorithim for a formula which will tell you the new
	// population
	// @Override
	/*
	 * protected final int getPopulationWithGrowth(long elapsedTime) { int
	 * carryingCapacityPopulation=getNormalPopulation(); int
	 * lastPopulation=getPopulation(); double
	 * A=(double)(carryingCapacityPopulation-lastPopulation)/(double)lastPopulation;
	 * double denominator=1+A*Math.exp(-relativeGrowthRate*elapsedTime); double
	 * rawPopulation=(double)carryingCapacityPopulation/denominator; //make it an
	 * int becuase we can't have (viable) fractions of seafood int
	 * updatedPopulation=(int)rawPopulation; //next line for debug
	 * //System.out.println(updatedPopulation); return updatedPopulation; }
	 * 
	 */

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

	public ArrayList<Fish> givePacketOfFish(FishSpecies fish, int currentPopulation, int maxPopulation) throws Exception
	{

		//System.out.println("in giving packet");

		double clientPlentifullness = (double) currentPopulation / (double) maxPopulation;
		ArrayList<Fish> fishPacket = new ArrayList<>();
		switch (fish)
		{
		case COD:
			double codPlentifullness = (double) codPopulation.size() / (double) Constants.COD_MAX_POPULATION;
			return getPacketOfFish(codPopulation, codPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
		// break;
		case SALMON:
			double salmonPlentifullness = (double) salmonPopulation.size() / (double) Constants.SALMON_MAX_POPULATION;
			return getPacketOfFish(salmonPopulation, salmonPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

		case TUNA:
			double tunaPlentifullness = (double) tunaPopulation.size() / (double) Constants.TUNA_MAX_POPULATION;
			return getPacketOfFish(tunaPopulation, tunaPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
		}
		return fishPacket;
	}

	public ArrayList<Shellfish> givePacketOfFish(ShellfishSpecies shellfish, int currentPopulation, int maxPopulation)
	{
		System.out.println("in giving packet");
		double clientPlentifullness = (double) currentPopulation / (double) maxPopulation;
		ArrayList<Shellfish> shellfishPacket = new ArrayList<>();
		switch (shellfish)
		{
		case OYSTER:
			double oysterPlentifullness = (double) oysterPopulation.size() / (double) Constants.OYSTER_MAX_POPULATION;
			return getPacketOfShellfish(oysterPopulation, oysterPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

		case LOBSTER:
			double lobsterPlentifullness = (double) lobsterPopulation.size() / (double) Constants.LOBSTER_MAX_POPULATION;
			return getPacketOfShellfish(lobsterPopulation, lobsterPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
		case CRAB:
			double crabPlentifullness = (double) crabPopulation.size() / (double) Constants.CRAB_MAX_POPULATION;
			return getPacketOfShellfish(crabPopulation, crabPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

		// default;
		}
		return shellfishPacket;
	}

	public ArrayList<Fish> getPacketOfFish(ArrayList<Fish> fishPopulation, double oceanPlentifullness, double clientPlentifullness, int maxPopulation, int currentPopulation) throws Exception
	{
		//System.out.println("Ocean p: " + oceanPlentifullness);
		//System.out.println("Client p:" + clientPlentifullness);
		if (oceanPlentifullness > clientPlentifullness)
		{
			int numFish = (int) (oceanPlentifullness * maxPopulation) - currentPopulation;
			//System.out.println("Number of fish for packet" + numFish);
			return extractABunchOfFish(fishPopulation, numFish);
		}
		else
		{
			ArrayList<Fish> fishPacket = new ArrayList<>();
			return fishPacket;
		}
	}

	public ArrayList<Shellfish> getPacketOfShellfish(ArrayList<Shellfish> shellfishPopulation, double oceanPlentifullness, double clientPlentifullness, int maxPopulation, int currentPopulation)
	{
		//System.out.println("Ocean p: " + oceanPlentifullness);
		//System.out.println("Client p:" + clientPlentifullness);
		if (oceanPlentifullness > clientPlentifullness)
		{
			int numShellfish = (int) (oceanPlentifullness * maxPopulation) - currentPopulation;
			//System.out.println("Number of shellfish for packet" + numShellfish);
			return extractABunchOfShellfish(shellfishPopulation, numShellfish);
		}
		else
		{
			ArrayList<Shellfish> shellfishPacket = new ArrayList<>();
			return shellfishPacket;
		}
	}

	public ArrayList<Fish> extractABunchOfFish(ArrayList<Fish> fishPopulation, int num) throws Exception
	{
		ArrayList<Fish> extractedFishes = new ArrayList<Fish>();
		for (int i = 0; i <= num - 1; i++)
		{
			extractedFishes.add(extractFish(fishPopulation));
		}

		return extractedFishes;
	}

	public ArrayList<Shellfish> extractABunchOfShellfish(ArrayList<Shellfish> shellfishPopulation, int num)
	{
		ArrayList<Shellfish> extractedShellfish = new ArrayList<Shellfish>();
		for (int i = 0; i < num - 1; i++)
		{
			extractedShellfish.add(extractShellfish(shellfishPopulation));
		}
		return extractedShellfish;
	}

	public Fish extractFish(ArrayList<Fish> fishPopulation) throws Exception
	{
		Fish fish = fishPopulation.get(fishPopulation.size() - 1);
		fishPopulation.remove(fishPopulation.size() - 1);

		return fish;
	}

	public Shellfish extractShellfish(ArrayList<Shellfish> shellfishPopulation)
	{
		Shellfish shellfish = shellfishPopulation.get(shellfishPopulation.size() - 1);
		shellfishPopulation.remove(shellfishPopulation.size() - 1);
		return shellfish;
	}

	public Fish extractCod()
	{
		return new Fish(FishSpecies.COD, 20);
	}

	public Fish extractSalmon()
	{
		return new Fish(FishSpecies.SALMON, 20);
	}

	public Fish extractTuna()
	{
		return new Fish(FishSpecies.TUNA, 20);
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

	public void fillOceanInitially()
	{
		addABunchOfFish(codPopulation, FishSpecies.COD, Constants.COD_INITIAL_POPULATION);
		addABunchOfFish(salmonPopulation, FishSpecies.SALMON, Constants.SALMON_INITIAL_POPULATION);
		addABunchOfFish(tunaPopulation, FishSpecies.TUNA, Constants.TUNA_INITIAL_POPULATION);
		addABunchOfShellfish(oysterPopulation, ShellfishSpecies.OYSTER, Constants.OYSTER_INITIAL_POPULATION);
		addABunchOfShellfish(lobsterPopulation, ShellfishSpecies.LOBSTER, Constants.LOBSTER_INITIAL_POPULATION);
		addABunchOfShellfish(crabPopulation, ShellfishSpecies.CRAB, Constants.CRAB_INITIAL_POPULATION);

	}

	public void addABunchOfFish(ArrayList<Fish> fishPopulation, FishSpecies species, int num)
	{
		for (int i = 0; i <= num - 1; i++)
		{
			addFish(fishPopulation, species);
		}
	}

	public void addABunchOfShellfish(ArrayList<Shellfish> shellfishPopulation, ShellfishSpecies species, int num)
	{
		for (int i = 0; i <= num - 1; i++)
		{
			addShellfish(shellfishPopulation, species);
		}
	}

	public void addFish(ArrayList<Fish> fishPopulation, FishSpecies species)
	{
		fishPopulation.add(new Fish(species, getFishSpeciesRandomWeight(species)));
	}

	public void addShellfish(ArrayList<Shellfish> shellfishPopulation, ShellfishSpecies species)
	{
		shellfishPopulation.add(new Shellfish(species, getShellfishSpeciesRandomWeight(species)));
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
	public double getCodWeight()
	{
		double random = getRandomDouble(Constants.COD_INITIAL_WEIGHT_MIN, Constants.COD_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getSalmonWeight()
	{

		double random = getRandomDouble(Constants.SALMON_INITIAL_WEIGHT_MIN, Constants.SALMON_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getTunaWeight()
	{
		double random = getRandomDouble(Constants.TUNA_INITIAL_WEIGHT_MIN, Constants.TUNA_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getCrabWeight()
	{
		double random = getRandomDouble(Constants.CRAB_INITIAL_WEIGHT_MIN, Constants.CRAB_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getLobsterWeight()
	{
		double random = getRandomDouble(Constants.LOBSTER_INITIAL_WEIGHT_MIN, Constants.LOBSTER_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getOysterWeight()
	{
		double random = getRandomDouble(Constants.OYSTER_INITIAL_WEIGHT_MIN, Constants.OYSTER_INITIAL_WEIGHT_MAX);
		return random;
	}

	public double getFishSpeciesRandomWeight(FishSpecies species)
	{
		double weight = 0;
		switch (species)
		{
		case COD:
			weight = getCodWeight();
			break;
		case SALMON:
			weight = getSalmonWeight();
			break;
		case TUNA:
			weight = getTunaWeight();
			break;

		}
		return weight;
	}

	public double getShellfishSpeciesRandomWeight(ShellfishSpecies species)
	{
		double weight = 0;
		switch (species)
		{
		case OYSTER:
			weight = getOysterWeight();
			break;
		case LOBSTER:
			weight = getLobsterWeight();
			break;
		case CRAB:
			weight = getCrabWeight();
			break;

		}
		return weight;
	}

	// helper function
	private int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max - min) + min;
		return randomInt;
	}

	private double getRandomDouble(double max, double min)
	{
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}
}
