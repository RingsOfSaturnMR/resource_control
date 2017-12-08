package market;

import java.util.ArrayList;
import java.util.Date;

import resources.FishSpecies;
import resources.SeaCreature;
import resources.ShellfishSpecies;
import java.util.HashMap;
import java.util.Iterator;

import catchgame.Constants;
import catchgame.GameControl.SeafoodPriceSetEventHandler;

/*
 
 -Make prices change at a set interval of time. (Example: Every 10 minutes, make the price for any item go up or down percentage, depending on how stable the market is.)
 
 - Use the Constants class to get the market fluctuation value, and how long before prices expire.
 
 */
public class SeafoodMarket extends Market<SeaCreature, Enum>
{
	// handler to tell the rest of the program there are new prices
	private SeafoodPriceSetEventHandler priceSetHandler;
	
	// Dictionary to hold merch and prices
	private HashMap<Enum, Double> inventory; // type : price
	// Iterator of traversing hashmap
	private Iterator<Enum> keySetIterator;

	// for keeping track of time
	private long previousTime;
	private long currentTime;

	public SeafoodMarket(String name, SeafoodPriceSetEventHandler updatePricePerPoundHandler)
	{
		super(name);
		// set the handler
		priceSetHandler = updatePricePerPoundHandler;
<<<<<<< HEAD
		
=======
	
>>>>>>> nils_branch
		// populate hashmap with inventory
		inventory = new HashMap<Enum, Double>();
		// for fish species
		inventory.put(FishSpecies.COD, Constants.COD_INITIAL_PRICE_PER_POUND);
		inventory.put(FishSpecies.SALMON, Constants.SALMON_INITIAL_PRICE_PER_POUND);
		inventory.put(FishSpecies.TUNA, Constants.TUNA_INITIAL_PRICE_PER_POUND);
		// for shellfish
		inventory.put(ShellfishSpecies.OYSTER, Constants.OYSTER_INITIAL_PRICE_PER_POUND);
		inventory.put(ShellfishSpecies.LOBSTER, Constants.LOBSTER_INITIAL_PRICE_PER_POUND);
		inventory.put(ShellfishSpecies.CRAB, Constants.CRAB_INITIAL_PRICE_PER_POUND);

		// initialize iterator for traversal
		this.keySetIterator = inventory.keySet().iterator();

		// set up clock
		currentTime = System.nanoTime();
		previousTime = System.nanoTime();
<<<<<<< HEAD
	}
	
	//temp for testing
	public void forcePriceUpdate()
	{
		// do this after a price change to tell the program there are new prices
		priceSetHandler.setPrices();
=======
		
>>>>>>> nils_branch
	}
	
	//temp for testing

<<<<<<< HEAD
	public void getRandTimeCoefficient() {}
	public void getDeltaTime() {} // will check time
	public void marketFlux() {} // this one will contain the thread for checking the time and updated price

	@Override
	public double getCurrentPricePerPound(Enum species)
	{
=======
	public void forcePriceUpdate()
	{
		// do this after a price change to tell the program there are new prices
		priceSetHandler.setPrices();
	}



	public void getRandTimeCoefficient() {}
	public void getDeltaTime() {} // will check time
	public void marketFlux() {} // this one will contain the thread for checking the time and updated price


	public double getCurrentPricePerPound(Enum species)
	{
>>>>>>> nils_branch
		if (this.inventory.containsKey(species)) {
			return this.inventory.get(species); // returns the value which is the current price per pound
		}
		else {
			// throw because input is not in hashmap
			// requires return added '0' for now - Nils
			return 0;
		}
	}

	@Override
	public String getMarketType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double sellItem(SeaCreature item)
	{
		if(item.getSpecies() == FishSpecies.SALMON)
		{
			// its a salmon!
		}
		return 2;
	}
	
	public Date getNextPriceChange()
	{
		return new Date();
	}


}
