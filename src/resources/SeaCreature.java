package resources;

import java.io.Serializable;

<<<<<<< HEAD
import catchgame.Constants;
import graphicclasses.AbstractSeaCreatureGraphic;
=======

>>>>>>> nils_branch
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
<<<<<<< HEAD
	private double speed=10;
	private short direction=Constants.RIGHT;
	private transient Circle GUICircle = null;
	//protected transient AbstractSeaCreatureGraphic seaCreatureBody=null;
=======
	private transient Circle GUICircle = null;
>>>>>>> nils_branch

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
	
	@Override
	public String toString()
	{
		return this.getSpecies().toString() + ", " + this.getWeight() + " pounds";
	}
<<<<<<< HEAD
	
	public double getSpeed(){
		return speed;
	}
	
	public void setSpeed(double speed){
		if (speed > 0)
		{
			this.speed = speed;
		}
		else
		{
			speed = 1;
		}
	}
	
	public double getDirecTion(){
		return direction;
	}
	
	public void setDirecTion(boolean right){
		if (right)
		{
			this.direction = Constants.RIGHT;
		}
		else
		{
			this.direction = Constants.LEFT;
		}
	}
=======
>>>>>>> nils_branch
}
