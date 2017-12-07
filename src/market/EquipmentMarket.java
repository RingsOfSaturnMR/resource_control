package market;

import java.util.ArrayList;

import resources.Boat;
import resources.BoatTypes;
import resources.Equipment;
import resources.SimpleFishingItem;
import resources.SimpleFishingItemType;


/*
 fill in all these methods.
 Notice there is no method to buy an item. That is because Equipment resources are unlimited, and then can just be made
 in the event handler for when a player purchases something. This class just determines the values.  
 */
public class EquipmentMarket extends Market<Equipment, Enum>
{

	public interface TakePlayersMoney
	{
		void takeMoney();
	}
	
	public EquipmentMarket(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Object buyItem(Enum<?> desiredItem)
	{
		if(desiredItem instanceof BoatTypes)
		{
			return new Boat(BoatTypes.TRAWLER);
		}
		else
		{
			return new SimpleFishingItem(SimpleFishingItemType.NET);
		}	
	}
	
	// price for purchasing new equipment
	public double getCurrentPrice(Enum item)
	{
		// TODO Auto-generated method stub
		return 12.3;
	}

	@Override
	public String getMarketType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double sellItem(Equipment item)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	// amount of money you recieve when you sell old equip
	public double getItemValue(Equipment item)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
