package resources;

import java.util.ArrayList;
import java.util.Random;

public class Ocean
{
	private static final double COD_WEIGHT_MAX = 27;
	private static final double COD_WEIGHT_MIN = 10;
	
	private static final double SALMON_WEIGHT_MAX = 16;
	private static final double SALMON_WEIGHT_MIN = 7;
	
	private static final double CLAM_WEIGHT = .02;
	private static final double QUAHOG_WEIGHT = .03;
	private static final double OYSTER_WEIGHT = .015;
	
	private static int INITIAL_TOTAL_POPULATION = 8;
	
	private static int NUM_DECIMAL_PLACES = 2;
	
	private Random rand = new Random();
	
	private ArrayList<Fish>codPopulation = new ArrayList<>();

	private ArrayList<Fish>salmonPopulation = new ArrayList<>();
	
	public Ocean()
	{
		int randomInt;
		
		for (int i = 0; i < INITIAL_TOTAL_POPULATION; i++)
		{
			randomInt = getRandomInt(0, 2);
			
			if(randomInt == 0)
			{
				addRandomFish(FishSpecies.COD);
			}
			else if(randomInt == 1)
			{
				addRandomFish(FishSpecies.SALMON);
			}
			else
			{
				System.out.println("This shouldnt happen!");
			}
			
		}
	}
	
	
	private void addRandomFish(FishSpecies type)
	{
		double weight;
		if (type == FishSpecies.COD)
		{
			weight = round(getRandomDouble(COD_WEIGHT_MIN, COD_WEIGHT_MAX), NUM_DECIMAL_PLACES);
			codPopulation.add(new Fish(FishSpecies.COD, weight ));
		}
		if (type == FishSpecies.SALMON)
		{
			weight = round(getRandomDouble(SALMON_WEIGHT_MIN, SALMON_WEIGHT_MAX), NUM_DECIMAL_PLACES);
			salmonPopulation.add(new Fish(FishSpecies.SALMON, weight));
		}
	}
	
	private double getRandomDouble(double min, double max)
	{
		return  min + (rand.nextDouble() * (max - min));	
	}
	
	private int getRandomInt(int min, int max)
	{
		return  rand.nextInt(max) + min;
	}
	
	public ArrayList<Fish> getCodPopulation()
	{
		return codPopulation;
	}

	public void setCodPopulation(ArrayList<Fish> codPopulation)
	{
		this.codPopulation = codPopulation;
	}

	public ArrayList<Fish> getSalmonPopulation()
	{
		return salmonPopulation;
	}

	public void setSalmonPopulation(ArrayList<Fish> salmonPopulation)
	{
		this.salmonPopulation = salmonPopulation;
	}
	
	public static double round(double value, int places)
	{
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}
	
}


/*
 public class Ocean
{
	private static final double COD_WEIGHT_MAX = 27;
	private static final double COD_WEIGHT_MIN = 10;
	
	private static final double SALMON_WEIGHT_MAX = 16;
	private static final double SALMON_WEIGHT_MIN = 7;
	
	private static final double CLAM_WEIGHT = .02;
	private static final double QUAHOG_WEIGHT = .03;
	private static final double OYSTER_WEIGHT = .015;
	
	private static int INITIAL_TOTAL_POPULATION = 8;
	
	private static int NUM_DECIMAL_PLACES = 2;
	
	private Random rand = new Random();
	
	private ArrayList<Fish>codPopulation = new ArrayList<>();

	private ArrayList<Fish>salmonPopulation = new ArrayList<>();
	
	public Ocean()
	{
		int randomInt;
		
		for (int i = 0; i < INITIAL_TOTAL_POPULATION; i++)
		{
			randomInt = getRandomInt(0, 2);
			
			if(randomInt == 0)
			{
				addRandomFish(FishSpecies.COD);
			}
			else if(randomInt == 1)
			{
				addRandomFish(FishSpecies.SALMON);
			}
			else
			{
				System.out.println("This shouldnt happen!");
			}
			
		}
	}
	
	
	private void addRandomFish(FishSpecies type)
	{
		double weight;
		if (type == FishSpecies.COD)
		{
			weight = round(getRandomDouble(COD_WEIGHT_MIN, COD_WEIGHT_MAX), NUM_DECIMAL_PLACES);
			codPopulation.add(new Fish(FishSpecies.COD, weight ));
		}
		if (type == FishSpecies.SALMON)
		{
			weight = round(getRandomDouble(SALMON_WEIGHT_MIN, SALMON_WEIGHT_MAX), NUM_DECIMAL_PLACES);
			salmonPopulation.add(new Fish(FishSpecies.SALMON, weight));
		}
	}
	
	private double getRandomDouble(double min, double max)
	{
		return  min + (rand.nextDouble() * (max - min));	
	}
	
	private int getRandomInt(int min, int max)
	{
		return  rand.nextInt(max) + min;
	}
	
	public ArrayList<Fish> getCodPopulation()
	{
		return codPopulation;
	}

	public void setCodPopulation(ArrayList<Fish> codPopulation)
	{
		this.codPopulation = codPopulation;
	}

	public ArrayList<Fish> getSalmonPopulation()
	{
		return salmonPopulation;
	}

	public void setSalmonPopulation(ArrayList<Fish> salmonPopulation)
	{
		this.salmonPopulation = salmonPopulation;
	}
	
	public static double round(double value, int places)
	{
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}
	
}

*/