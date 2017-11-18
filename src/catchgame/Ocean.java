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
 * This class generates, "gives away", and regenerates SeaCreatures. 
 * It has methods to create SeaCreatures, "give away" sea creatures,
 * and regenerate SeaCreature populations until they each reach their
 * max or "carrying capacity" populations.
 * 
 * @author Thanh Lam
 * @author Matt Roberts
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
	private double regenerationTimeInterval=10;

	/**
	 * If it is a new game, fills the Ocean to max population;
	 * otherwise it loads the saved ocean;
	 * regardless, it then starts regenerateOcean to populate Ocean
	 * with SeaCreatures (up to max population).
	 */
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
	
	public ArrayList<Fish> extractAndReturnABunchOfFish(FishSpecies fishSpecies, 
			int clientCurrentPopulation,int clientMaxPopulation) throws Exception
	{
		int oceanMaxPopulation=0;
		ArrayList<Fish> fishPopulation=null;
		switch (fishSpecies)
		{
		case COD:
			fishPopulation=codPopulation;
			oceanMaxPopulation=Constants.COD_MAX_POPULATION;
			break;
		case SALMON:
			fishPopulation=salmonPopulation;
			oceanMaxPopulation=Constants.SALMON_MAX_POPULATION;
			break;
		case TUNA:
			fishPopulation=tunaPopulation;
			oceanMaxPopulation=Constants.TUNA_MAX_POPULATION;
			break;
		default:
			break;
		}
		double oceanPlentifullness
			=(double)fishPopulation.size()/(double)oceanMaxPopulation;
		double clientPlentifullness 
			= (double) clientCurrentPopulation / (double) clientMaxPopulation;
		//System.out.println("Ocean p: " + oceanPlentifullness);
		//System.out.println("Client p:" + clientPlentifullness);
		if (oceanPlentifullness > clientPlentifullness)
		{
			int numFish = (int) (oceanPlentifullness 
					* clientMaxPopulation) - clientCurrentPopulation;
			//System.out.println("Number of fish for packet" + numFish);
			return extractABunchOfFish(fishPopulation, numFish);
		}
		else
		{
			ArrayList<Fish> fishPacket = new ArrayList<>();
			return fishPacket;
		}
	}

	public ArrayList<Shellfish> ecxtractAndReturnABunchOfShellfish(ShellfishSpecies shellfishSpecies,
			int clientCurrentPopulation,int clientMaxPopulation)
	{
		int oceanMaxPopulation=0;
		ArrayList<Shellfish> shellfishPopulation=null;
		switch (shellfishSpecies)
		{
		case OYSTER:
			shellfishPopulation=oysterPopulation;
			oceanMaxPopulation=Constants.OYSTER_MAX_POPULATION;
			break;
		case LOBSTER:
			shellfishPopulation=lobsterPopulation;
			oceanMaxPopulation=Constants.LOBSTER_MAX_POPULATION;
			break;
		case CRAB:
			shellfishPopulation=crabPopulation;
			oceanMaxPopulation=Constants.CRAB_MAX_POPULATION;
			break;
		default:
			break;
		}
		double oceanPlentifullness
			=(double)shellfishPopulation.size()/(double)oceanMaxPopulation;
		double clientPlentifullness 
			= (double) clientCurrentPopulation / (double) clientMaxPopulation;
		//System.out.println("Ocean p: " + oceanPlentifullness);
		//System.out.println("Client p:" + clientPlentifullness);
		if (oceanPlentifullness > clientPlentifullness)
		{
			int numShellfish = (int) (oceanPlentifullness * clientMaxPopulation) - clientCurrentPopulation;
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
	
	private void fillOceanInitially()
	{
		addABunchOfCod();
		addABunchOfSalmon();
		addABunchOfTuna();
		addABunchOfOyster();
		addABunchOfLobster();
		addABunchOfCrab();
	}

	/**
	 * Adds SeaCreatures to Ocean updateSeaCreaturePopulation
	 * growth formula every 5 seconds.
	 */
	private void regenerateOcean()
	{
		Timer timer = new Timer();
		TimerTask task;
		task = new TimerTask()
		{

			@Override
			public void run()
			{
				updateAllSeaCreaturePopulations();
			}

		};
		timer.schedule(task, 0, 5_000);
	}
	
	private void updateAllSeaCreaturePopulations(){
		updateCodPopulation();
		updateSalmonPopulation();
		updateTunaPopulation();
		updateOysterPopulation();
	}
	
	private void updateCodPopulation(){
		updateFishPopulation(regenerationTimeInterval, 
				codPopulation, Constants.COD_MAX_POPULATION, 
				FishSpecies.COD);
	}
	
	private void updateSalmonPopulation(){
		updateFishPopulation(regenerationTimeInterval, 
				salmonPopulation, Constants.SALMON_MAX_POPULATION, 
				FishSpecies.SALMON);
	}
	
	private void updateTunaPopulation(){
		updateFishPopulation(regenerationTimeInterval, 
				tunaPopulation, Constants.TUNA_MAX_POPULATION, 
				FishSpecies.TUNA);
	}
	
	private void updateOysterPopulation(){
		updateShellfishPopulation(regenerationTimeInterval, 
				oysterPopulation, Constants.OYSTER_MAX_POPULATION, 
				ShellfishSpecies.OYSTER);
	}
	private void updateLobsterPopulation(){
		updateShellfishPopulation(regenerationTimeInterval, 
				lobsterPopulation, Constants.LOBSTER_MAX_POPULATION, 
				ShellfishSpecies.LOBSTER);
	}
	private void updateCrabPopulation(){
		updateShellfishPopulation(regenerationTimeInterval, 
				crabPopulation, Constants.COD_MAX_POPULATION, 
				ShellfishSpecies.CRAB);
	}
	
	private void updateFishPopulation(double elapsedTime, 
			ArrayList<Fish> fishPopulationAL, int speciesMaxPopulation, 
			FishSpecies fishSpecies){
		int fishToAdd = determineNumberNewFishOfPopulationGrowth(elapsedTime, 
				fishPopulationAL, speciesMaxPopulation);
		// System.out.println("Cod to add "+codToAdd);
		addABunchOfFish(fishPopulationAL, fishSpecies, fishToAdd);
	}
	
	private void updateShellfishPopulation(double elapsedTime, 
			ArrayList<Shellfish> shellfishPopulationAL, int speciesMaxPopulation, 
			ShellfishSpecies shellfishSpecies){
		int fishToAdd = determineNumberNewShellfishOfPopulationGrowth(elapsedTime, 
				shellfishPopulationAL, speciesMaxPopulation);
		// System.out.println("Cod to add "+codToAdd);
		addABunchOfShellfish(shellfishPopulationAL, shellfishSpecies, fishToAdd);
	}
	
	/**
	 * Determines how many more SeaCreatures a given population
	 * should have added based on the elapsedTime, relativeGrowthRate, 
	 * and carryingCapacityPopulation according to the general 
	 * population growth formula found on Wikipedia.
	 * 
	 * @param elapsedTime a real or virtual time interval
	 * 
	 * @return the appropriate growth in the specified SeaCreature
	 * population, as a number of organisms.
	 */
	private int determineNumberNewFishOfPopulationGrowth(double elapsedTime, 
			ArrayList<Fish> oldPopulationAL, int speciesMaxPopulation){
		int carryingCapacityPopulation = speciesMaxPopulation;
		int lastPopulation = oldPopulationAL.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		// make it an int becuase we can't have (viable) fractions of SeaCreature
		int updatedPopulationNumber = (int) rawPopulation;
		// next line for debug
		//System.out.println(updatedPopulation);
		return updatedPopulationNumber - oldPopulationAL.size();
	}
	
	private int determineNumberNewShellfishOfPopulationGrowth(double elapsedTime, 
			ArrayList<Shellfish> oldPopulationAL, int speciesMaxPopulation){
		int carryingCapacityPopulation = speciesMaxPopulation;
		int lastPopulation = oldPopulationAL.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		// make it an int becuase we can't have (viable) fractions of SeaCreature
		int updatedPopulationNumber = (int) rawPopulation;
		// next line for debug
		//System.out.println(updatedPopulation);
		return updatedPopulationNumber - oldPopulationAL.size();
	}
	
	public void addABunchOfCod(){
		addABunchOfFish(codPopulation, FishSpecies.COD, 
				Constants.COD_INITIAL_POPULATION);
	}
	
	public void addABunchOfSalmon(){
		addABunchOfFish(salmonPopulation, FishSpecies.SALMON, 
				Constants.SALMON_INITIAL_POPULATION);
	}
	
	public void addABunchOfTuna(){
		addABunchOfFish(tunaPopulation, FishSpecies.TUNA, 
				Constants.TUNA_INITIAL_POPULATION);
	}

	public void addABunchOfOyster(){
		addABunchOfShellfish(oysterPopulation, ShellfishSpecies.OYSTER, 
				Constants.OYSTER_INITIAL_POPULATION);
	}
	public void addABunchOfLobster(){
		addABunchOfShellfish(lobsterPopulation, ShellfishSpecies.LOBSTER, 
				Constants.LOBSTER_INITIAL_POPULATION);
	}
	public void addABunchOfCrab(){
		addABunchOfShellfish(crabPopulation, ShellfishSpecies.CRAB, 
				Constants.CRAB_INITIAL_POPULATION);
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
		fishPopulation.add(new Fish(species, getRandomWeightForFish(species)));
	}

	public void addShellfish(ArrayList<Shellfish> shellfishPopulation, ShellfishSpecies species)
	{
		shellfishPopulation.add(new Shellfish(species, getRandomWeightForShellfish(species)));
	}
	
	 public double getRandomWeightForFish(FishSpecies species)
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

		public double getRandomWeightForShellfish(ShellfishSpecies species)
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

	// get random weight of each species
	public double getCodWeight()
	{
		return getRandomDouble(Constants.COD_INITIAL_WEIGHT_MIN, Constants.COD_INITIAL_WEIGHT_MAX);

	}

	public double getSalmonWeight()
	{
		return getRandomDouble(Constants.SALMON_INITIAL_WEIGHT_MIN, Constants.SALMON_INITIAL_WEIGHT_MAX);
	}

	public double getTunaWeight()
	{
		return  getRandomDouble(Constants.TUNA_INITIAL_WEIGHT_MIN, Constants.TUNA_INITIAL_WEIGHT_MAX);

	}

	public double getCrabWeight()
	{
		return getRandomDouble(Constants.CRAB_INITIAL_WEIGHT_MIN, Constants.CRAB_INITIAL_WEIGHT_MAX);

	}

	public double getLobsterWeight()
	{
		return getRandomDouble(Constants.LOBSTER_INITIAL_WEIGHT_MIN, Constants.LOBSTER_INITIAL_WEIGHT_MAX);
	}

	public double getOysterWeight()
	{
		return getRandomDouble(Constants.OYSTER_INITIAL_WEIGHT_MIN, Constants.OYSTER_INITIAL_WEIGHT_MAX);
		
	}
	
	public int getCurrentCodPopulation()
	{
		return codPopulation.size();
	}

	public int getCurrentSalmonPopulation()
	{
		return salmonPopulation.size();
	}

	public int getCurrentTunaPopulation()
	{
		return tunaPopulation.size();
	}

	public int getCurrentCrabPopulation()
	{
		return crabPopulation.size();
	}

	public int getCurrentLobsterPopulation()
	{
		return lobsterPopulation.size();
	}

	public int getCurrentOysterPopulation()
	{
		return oysterPopulation.size();
		//int random = getRandomInt(0, Constants.OYSTER_INITIAL_POPULATION);
		//return random;
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
/*
 **********************
 These are unused stubs:
 **********************
 
 
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
 */



/*
 ****************************************************
This code was reworked and is included in a new form:
****************************************************
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

	public ArrayList<Shellfish> extractABunchOfFish(ShellfishSpecies shellfish, int currentPopulation, int maxPopulation, int oceanMaxPopulation)
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

*/
