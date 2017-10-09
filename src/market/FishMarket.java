package market;

import java.util.ArrayList;

import catchgame.Catch;
import resources.Fish;

public class FishMarket extends Market<Fish>
{
	private double tunaPrice = Catch.TUNA_PRICE_PER_POUND;
	private double codPrice = Catch.COD_PRICE_PER_POUND;
	private double salmonPrice = Catch.SALMON_PRICE_PER_POUND;

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
			currentPrice = tunaPrice;
			break;
		case COD:
			currentPrice = codPrice;
			break;
		case SALMON:
			currentPrice = salmonPrice;
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
