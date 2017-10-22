package resources;

/**
 */
public abstract class SeaCreature <T>
{
	protected static int totalPopulation = 0;
	
	private double weight;
	private T species;

	SeaCreature(){
		
	}
	
	protected SeaCreature(T species, double weight)
	{
		setSpecies(species);
		setWeight(weight);
		totalPopulation++;
	}
	
	private void setSpecies(T species)
	{
		this.species = species;	
	}

	public void setWeight(double weight)
	{
		if (weight > 0)
		{
			this.weight = weight;
		}
		else
		{
			weight = 1;
		}
	}

	public double getWeight()
	{
		return weight;
	}

	public T getSpecies()
	{
		return this.species;
	}
}
