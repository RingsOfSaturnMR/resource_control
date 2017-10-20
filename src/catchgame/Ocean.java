package catchgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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
	
	public Ocean()
	{
		// populate each array list with appropriate SeaCreatures.
		// Randomly generate each SeaCreature's weight by randomly generating a number between
		// the INITIAL_WEIGHT_MIN and INITAL_WEIGHT_MAX
		// get values from catchgame.Constant
		//
		//maybe use a timeline to regenrate fish if isfh is less than max population

		// this includes max_population for each fish
		// if they are not there, please add them
		// also, ocean should somehow regnerate it's population over time
		// this would require a formula
		// and some time related feature
		// maybe try the java class Timeline?
		// for the formula could use the formula in matt_branch in seafoodPopulation.java
		// different from seaCreature.java
		// formula is called getPopulationWithGrowth
		// would have to get the number of seacreatures to add to each array list,
		// using a formula like getPopulationWithGrowth
		// and then add them to each array list
		System.out.println("In the Ocean Constructor");
	}
	/*
	add to population function
	populationAmount, arraytlist, while arraylist is less than population, adds a fish
		call add a fish function in add to population function
	
	add fish
	enum fish type, max weight, min weight
	returns a fish of that kind between min and max weight using random number between min and max
	
	random function for returning weight between max and min
	
	add to population function using initial population size for all species in ocean
	
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
	
	@Override
    protected final int getPopulationRegneration(long elapsedTime, rawPopulation, mxaPopulation) {
        int carryingCapacityPopulation=maxPopulation
        int lastPopulation=rawPopulation
        double A=(double)(carryingCapacityPopulation-lastPopulation)/(double)lastPopulation;
        double denominator=1+A*Math.exp(-relativeGrowthRate*elapsedTime);
        double rawPopulation=(double)carryingCapacityPopulation/denominator;
        return rawPopulation;
    }
    
    updateSeaCreaturePopulation(arraylist seacreature, species){
    add until (int)rawpopulation==arraylist.length
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
