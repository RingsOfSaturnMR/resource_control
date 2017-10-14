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


}
