package market;

import java.util.ArrayList;

import resources.Equipment;


/*
 fill in all these methods.
 Notice there is no method to buy an item. That is because Equipment resources are unlimited, and then can just be made
 in the event handler for when a player purchases something. This class just determines the values.  
 */
public class EquipmentMarket extends Market<Equipment>
{

	public EquipmentMarket(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	// price for purchasing new equipment
	@Override
	public double getCurrentPrice(Equipment item)
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
	public double sellItem(ArrayList<Equipment> items)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	// ammount of money you recieve when you sell old equip
	public double getItemValue(Equipment item)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
