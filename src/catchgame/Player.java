package catchgame;

import java.util.ArrayList;
import resources.SeaCreature;
import resources.Fish;

public class Player
{
	double CashOnHand = 0;
	private ArrayList<SeaCreature> iceChest = new ArrayList<>();

	public void addItemToIceChest(SeaCreature item)
	{
		iceChest.add(item);
	}

	public void printIceChest()
	{
		for (int i = 0; i < iceChest.size(); i++)
		{
			System.out.println(iceChest.get(i).toString());
			if (iceChest.get(i) instanceof Fish)
			{
				System.out.println((((Fish) iceChest.get(i)).getWeight()) + "lbs");
			}
		}
	}
}
