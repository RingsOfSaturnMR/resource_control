package resources;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 */
public abstract class SeaCreature <T> implements Serializable
{
	protected static int totalPopulation = 0;
	
	private double weight;
	private T species;
	private Circle GUICircle = null;

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
	
	public Circle getBody()
	{
		return this.GUICircle;
	}
	
	public void SetBodyByWeight()
	{
		this.GUICircle = new Circle(weight);
	}
	
	public void setBodyColor(Color color){
		this.GUICircle.setFill(color);
	}
	
	// for serializing
	public void setBodyToNull()
	{
		this.GUICircle = null;
	}
}
