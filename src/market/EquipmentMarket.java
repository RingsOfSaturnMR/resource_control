package market;

import java.util.ArrayList;

import catchgame.GameControl.SetCurrentEquipPricesHandler;
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
	public SetCurrentEquipPricesHandler updatePriceHandler;
	
	public EquipmentMarket(String name, SetCurrentEquipPricesHandler updatePriceHandler)
	{
		super(name);
		// TODO Auto-generated constructor stub
		this.updatePriceHandler = updatePriceHandler;
	}

	public void forcUpdate()
	{
		updatePriceHandler.setPrices();
	}
	
	// TODO refactor
	public Object buyItem(Enum<?> desiredItem)
	{
		// boats
		if(desiredItem == BoatTypes.COMMERCIAL_TRAWLER)
		{
			return new Boat(BoatTypes.COMMERCIAL_TRAWLER);
		}
		if(desiredItem == BoatTypes.FISHING_SKIFF)
		{
			return new Boat(BoatTypes.FISHING_SKIFF);
		}
		if(desiredItem == BoatTypes.TRAWLER)
		{
			return new Boat(BoatTypes.TRAWLER);
		}
		
		// other stuff
		if(desiredItem == SimpleFishingItemType.FISHING_POLE)
		{
			return new SimpleFishingItem(SimpleFishingItemType.FISHING_POLE);
		}

		else
		{
			return null;
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
