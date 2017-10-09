package resources;

/**
 * 
 * @author Nils 
 *			Create the UML for your resource Class name Extends? Implements?
 *         Runnable*? instance variables - private / public setters & getters
 *         specialized methods - private / public Create the class (write the
 *         Java code!) Create a test program to exercise the class - you should
 *         be able to instantiate one and multiple instances of your class. Make
 *         sure your code has your name in the comments, and a space for change
 *         history.
 */
public abstract class SeaCreature <T>
{
	protected static int totalPopulation = 0;
	
	private double weight;
	private T species;

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
