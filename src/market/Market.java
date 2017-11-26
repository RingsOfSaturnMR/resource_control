package market;

import java.util.ArrayList;

public abstract class Market<T, Enum>
{
	private String name;
	
	public Market(String name)
	{
		this.setName(name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public abstract double getCurrentPricePerPound(Enum species);
	
	public abstract String getMarketType();
	
	public abstract double sellItem(ArrayList<T> items);
	
}
