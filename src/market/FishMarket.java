package market;

import java.util.ArrayList;

import catchgame.Catch;
import resources.Fish;
import catchgame.Constants;

public class FishMarket extends Market<Fish>
{
	public FishMarket(String name)
	{
		super(name);
	}

	@Override
	public double getCurrentPrice(Fish fish)
	{
		double currentPrice = 0;

		switch (fish.getSpecies())
		{
		case TUNA:
			currentPrice = Constants.TUNA_INITIAL_PRICE_PER_POUND;
			break;
		case COD:
			currentPrice = Constants.COD_INITIAL_PRICE_PER_POUND;
			break;
		case SALMON:
			currentPrice = Constants.SALMON_INITIAL_PRICE_PER_POUND;
			break;
		default:
			// TODO
			currentPrice = -1;
		}

		return currentPrice;
	}

	@Override
	public String getMarketType()
	{
		return "Fish Market";
	}


	@Override
	public double sellItem(ArrayList<Fish> fishList)
	{
		double totalPayment = 0;
		
		for (int i = 0; i < fishList.size(); i++)
		{
			totalPayment += getCurrentPrice(fishList.get(i));
		}
		
		return totalPayment;
	}

	

	
	


}
