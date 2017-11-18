package market;

import java.util.ArrayList;
import java.util.Date;

import resources.SeaCreature;


/*
 
 -Make prices change at a set interval of time. (Example: Every 10 minutes, make the price for any item go up or down percentage, depending on how stable the market is.)
 
 - Use the Constants class to get the market fluctuation value, and how long before prices expire.
 
 */
public class SeafoodMarket extends Market<SeaCreature>
{

	public SeafoodMarket(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCurrentPrice(SeaCreature item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMarketType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double sellItem(ArrayList<SeaCreature> items)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Date getNextPriceChange()
	{
		return new Date();
	}


}
