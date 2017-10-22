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
	private double relativeGrowthRate=0.02;
	




	public Ocean()
	{
		System.out.println("In the Ocean Constructor");
		boolean newGame=true;
		if (newGame) {
			fillOceanInitially();
		}
		else {
			//load old ocean
		}
		regenerateOcean();
			
		
	}
	
	public void regenerateOcean() {
		Timer timer = new Timer();
		TimerTask task;
		task = new TimerTask() {

	        @Override
	        public void run() { 
	        int codToAdd=updateCodPopulation(10);
	        System.out.println("Cod to add "+codToAdd);
	        addABunchOfFish(codPopulation, FishSpecies.COD, codToAdd);
	        }
	        	
	    };
	    timer.schedule(task, 0, 5_000);
	}
	
	private int updateCodPopulation(long elapsedTime) {
        int carryingCapacityPopulation=Constants.COD_MAX_POPULATION;
        int lastPopulation=codPopulation.size();
        double A=(double)(carryingCapacityPopulation-lastPopulation)/(double)lastPopulation;
        double denominator=1+A*Math.exp(-relativeGrowthRate*elapsedTime);
        double rawPopulation=(double)carryingCapacityPopulation/denominator;
        //make it an int becuase we can't have (viable) fractions of seafood
        int updatedPopulation=(int)rawPopulation;
        //next line for debug
        System.out.println(updatedPopulation);
        return updatedPopulation-codPopulation.size();
    }
	/*add to population function
	populationAmount, arraytlist, while arraylist is less than population, adds a fish
		call add a fish function in add to population function
	
	//add fish
	//enum fish type, max weight, min weight
	//returns a fish of that kind between min and max weight using random number between min and max
	
	//random function for returning weight between max and min
	
	//add to population function using initial population size for all species in ocean
	
	return a packet of fish, takes as parameter a plentifulnes variable, also subtracts fish from ocean
	
	returnPacketOfFish(int plentifullness, typeOfFish){
		if plentifullness of fish in ocean<plentifullness of fish from parameter,
		it calculates a number of fish to give
		extracts them from the arraylist
		returns a new arraylist
		
	the extract fish functions should take last fish off end of arraylist, return it, and delete the last fish from the arraylist
	
	regneratePopulation
	it should be called by a timer
	it should use the formula
	it should take for time just some constant that you choose
	}
	
	//this is a general algorithim for a formula which will tell you the new population
    @Override
    protected final int getPopulationWithGrowth(long elapsedTime) {
        int carryingCapacityPopulation=getNormalPopulation();
        int lastPopulation=getPopulation();
        double A=(double)(carryingCapacityPopulation-lastPopulation)/(double)lastPopulation;
        double denominator=1+A*Math.exp(-relativeGrowthRate*elapsedTime);
        double rawPopulation=(double)carryingCapacityPopulation/denominator;
        //make it an int becuase we can't have (viable) fractions of seafood
        int updatedPopulation=(int)rawPopulation;
        //next line for debug
        //System.out.println(updatedPopulation);
        return updatedPopulation;
    }

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
	
	public ArrayList<Fish> givePacketOfFish(FishSpecies fish, int currentPopulation, int maxPopulation ) throws Exception{
		System.out.println("in giving packet");
		double clientPlentifullness=(double)currentPopulation/(double)maxPopulation;
		ArrayList<Fish> fishPacket=new ArrayList<>();
		switch (fish){
		case COD:
			double oceanPlentifullness=(double)codPopulation.size()/(double)Constants.COD_MAX_POPULATION;
			return getPacketOfFish(codPopulation, 
					oceanPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
			//break;
		}
		
		return fishPacket;
	}
	
	public ArrayList<Fish> getPacketOfFish(ArrayList<Fish> fishPopulation, 
			double oceanPlentifullness, double clientPlentifullness, int maxPopulation, int currentPopulation)throws Exception{
		System.out.println("Ocean p: "+oceanPlentifullness);
		System.out.println("Client p:"+clientPlentifullness);
		if (oceanPlentifullness>clientPlentifullness) {
			int numFish=(int)(oceanPlentifullness*maxPopulation)-currentPopulation;
			System.out.println("Num fish for packet"+numFish);
			return extractABunchOfFish(fishPopulation, numFish);
		}
		else {
			ArrayList<Fish> fishPacket=new ArrayList<>();
			return fishPacket;
		}
	}
	
	
	public ArrayList<Fish>extractABunchOfFish(ArrayList<Fish>fishPopulation, int num) throws Exception{
		ArrayList<Fish>extractedFishes=new ArrayList<Fish>();
		for (int i=0; i<=num-1; i++) {
			extractedFishes.add(extractFish(fishPopulation));
		}
		
		return extractedFishes;
	}
	
	public Fish extractFish(ArrayList<Fish>fishPopulation) throws Exception{
		Fish fish=fishPopulation.get(fishPopulation.size()-1);
		fishPopulation.remove(fishPopulation.size()-1);
		
		return fish;
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
	
	public void fillOceanInitially() {
		addABunchOfFish(codPopulation, FishSpecies.COD, Constants.COD_INITIAL_POPULATION);
		//add for all other populations
	}
	
	public void addABunchOfFish(ArrayList<Fish>fishPopulation, FishSpecies species, int num) {
		for (int i=0; i<=num-1; i++) {
			addFish(fishPopulation, species);
		}
	}
	
	public void addFish(ArrayList<Fish>fishPopulation, FishSpecies species) {
		fishPopulation.add(new Fish(species, getFishSpeciesRandomWeight(species)));
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
	
	public double getFishSpeciesRandomWeight(FishSpecies species) {
		double weight=0;
		switch (species) {
		case COD: weight = getCodWeight(); break;
		}
		return weight;
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
