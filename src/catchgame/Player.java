package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Equipment;
import resources.SeaCreature;

/**
 * @author Nils
 */
public class Player extends authentication.User implements Serializable
{
	// stats
	double cashOnHand;
	private int skillLevel;

	// resources as array, to serialize
	private SeaCreature[] iceChestArray = null;

	// TODO make observable
	private ArrayList<Equipment> toolChest = new ArrayList<>();

	private transient ObservableList<SeaCreature> iceChest = null;
	
	private boolean freshlyDeserialized = true;

	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.skillLevel = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
		if (freshlyDeserialized == true)
		{
			deserializeCleanUp();
		}
		
		iceChest.add(item);
		
		for (int i = 0; i < iceChest.size(); i++)
		{
			System.out.println(iceChest.get(i).getSpecies());
			System.out.println(iceChest.get(i).getWeight());
			System.out.println("");
		}
	}

	public void addItemToToolChest(Equipment item)
	{
		if (freshlyDeserialized == true)
		{
			deserializeCleanUp();
		}
		
		toolChest.add(item);
	}

	public double getCashOnHand()
	{
		return this.cashOnHand;
	}

	public int getSkillLevel()
	{
		return skillLevel;
	}

	public void addMoney(int amount)
	{
		cashOnHand += amount;
	}

	public void subtractMoney(int amount)
	{
		cashOnHand -= amount;
	}

	public ObservableList<SeaCreature> getIceChest()
	{
		// if iceChest is null and the iceChestArray is not null, it means that this
		// object was freshly deserialized and needs to have the array copied to the
		// observable
		if (iceChest == null && iceChestArray != null)
		{
			copyArrayToObservable();
			return this.iceChest;
		}
		// if both are null, it means no SeaCreatures have ever been caught.
		else if (iceChest == null && iceChestArray == null)
		{
			return null;
		}
		// otherwise return the list
		else if (iceChest != null)
		{
			return iceChest;
		}

		// if it hasnt returnted at this point, shit, I dunno man, return null
		return null;

	}

	// copies primitive array to observable list
	private void copyArrayToObservable()
	{
		/*for (int i = 0; i < iceChestArray.length; i++)
		{
			iceChest.add(iceChestArray[i]);
		}*/
		
		iceChest = FXCollections.observableArrayList(iceChestArray);
	}

	/**
	 * Must be called prior to serialization. Converts Observable Lists to primitive
	 * arrays.
	 */
	public void prepareToSerialze()
	{
		if (iceChest != null)
		{
			iceChestArray = new SeaCreature[iceChest.size()];

			for (int i = 0; i < iceChestArray.length; i++)
			{
				iceChestArray[i] = iceChest.get(i);
				System.out.println("Copying SeaCreature " + i + " to primative array");
			}
		}
		
		freshlyDeserialized = true;
	}

	public void deserializeCleanUp()
	{
		// if iceChest is null and the iceChestArray is not null, it means that this
		// object was freshly deserialized and needs to have the array copied to the
		// observable
		if (iceChest == null && iceChestArray != null)
		{
			copyArrayToObservable();
		}
		// if both are null, it means no SeaCreatures have ever been caught. Set the iceChest to not be null;
		else if (iceChest == null && iceChestArray == null)
		{
			iceChest = FXCollections.observableArrayList();
		}
		
		freshlyDeserialized = false;
	}

}
