package catchgame;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
<<<<<<< HEAD
=======
import javafx.scene.image.Image;
import resources.BoatTypes;
>>>>>>> nils_branch
import resources.Equipment;
import resources.FishSpecies;
import resources.SeaCreature;
import resources.ShellfishSpecies;
<<<<<<< HEAD
=======
import resources.SimpleFishingItem;
import resources.SimpleFishingItemType;
>>>>>>> nils_branch

/**
 * @author Nils
 */
public class Player extends authentication.User implements Serializable
{
	// general stats
	double cashOnHand;
	private int skillLevel;

	// arrays to hold resources, when serializing
	private SeaCreature[] iceChestArray = null;
	private Equipment[] toolChestArray = null;

	// resources as observable lists, for gameplay
	private transient ObservableList<Equipment> toolChest = null;
	private transient ObservableList<SeaCreature> iceChest = null;

	// flag to mark if the arrays need to be copied to the observable lists
	private boolean observableListsLoaded = false;
<<<<<<< HEAD

	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.skillLevel = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
=======

	public Player(String username)
	{
		super(username);
		this.cashOnHand = 0;
		this.skillLevel = 0;
	}

	public void addSeaCreatureToIceChest(SeaCreature item)
	{
>>>>>>> nils_branch
		if (!observableListsLoaded)
		{
			loadObservableLists();
		}

		iceChest.add(item);
	}

	public void removeSeaCreatureFromIceChest(int i)
	{
		iceChest.remove(i);
	}

	public void addItemToToolChest(Equipment item)
	{
		if (!observableListsLoaded)
		{
			loadObservableLists();
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

	public void addMoney(double amount)
	{
		cashOnHand += amount;
	}

<<<<<<< HEAD
	public void subtractMoney(int amount)
	{
		cashOnHand -= amount;
	}

	public SeaCreature getSeaCreatureAt(int index)
=======
	public void subtractMoney(double d)
	{
		cashOnHand -= d;
	}

	public SeaCreature<?> getSeaCreatureAt(int index)
>>>>>>> nils_branch
	{
		return this.iceChest.get(index);
	}

	public ObservableList<SeaCreature> getIceChest()
	{
		if (observableListsLoaded != true)
		{
			loadObservableLists();
		}

		return iceChest;
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
			}
		}
<<<<<<< HEAD
=======
		
		if (toolChest != null)
		{
			toolChestArray = new Equipment[toolChest.size()];

			for (int i = 0; i < toolChestArray.length; i++)
			{
				toolChestArray[i] = toolChest.get(i);
			}
		}
>>>>>>> nils_branch

		observableListsLoaded = false;
	}

<<<<<<< HEAD
	// TODO - make work with equipment
=======
	// TODO - make work with equipment, done, cleanup though!
>>>>>>> nils_branch
	private void loadObservableLists()
	{
		// if iceChest is null and the iceChestArray is not null, it means that this
		// object was recently deserialized and needs to have the array copied to the
		// observable list
		if (iceChest == null && iceChestArray != null)
		{
			iceChest = FXCollections.observableArrayList(iceChestArray);
		}
		// if both are null, it means no SeaCreatures have ever been caught. Set the
		// iceChest to be empty;
		else if (iceChest == null && iceChestArray == null)
		{
			iceChest = FXCollections.observableArrayList();
		}
<<<<<<< HEAD
=======
		
		if (toolChest == null && toolChestArray != null)
		{
			toolChest = FXCollections.observableArrayList(toolChestArray);
		}
		// if both are null, it means no SeaCreatures have ever been caught. Set the
		// iceChest to be empty;
		else if (toolChest == null && toolChestArray == null)
		{
			toolChest = FXCollections.observableArrayList();
		}
>>>>>>> nils_branch

		// set the object to not do this again, until it gets serialized
		observableListsLoaded = false;
	}

<<<<<<< HEAD
	public int getNumOf(Enum<?> species)
	{
=======
	// TODO holy shit nils, fix this up
	// see Constants -> public static final Image getImage(final Enum<?> desiredResourceType) for ideas
	public int getNumOf(Enum<?> species)
	{
		// GET RID OF THIS in a refactor
		loadObservableLists();
>>>>>>> nils_branch
		// counter
		int numOfSpecies = 0;

		if (species instanceof FishSpecies)
		{
			switch ((FishSpecies) species)
			{
			case COD:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == FishSpecies.COD)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case SALMON:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == FishSpecies.SALMON)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;
<<<<<<< HEAD

			case TUNA:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == FishSpecies.TUNA)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			// TODO - handle this a little better
			default:
				return 0;
			}
		}
		if (species instanceof ShellfishSpecies)
		{
			switch ((ShellfishSpecies) species)
			{
			case CRAB:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.CRAB)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case LOBSTER:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.LOBSTER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case OYSTER:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.OYSTER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			// TODO - handle this a little better
			default:
				return 0;
			}
		}
		return 0;
	}

	public SeaCreature<?> getSeaNextSeaCreature(Enum<?> species)
	{
		boolean creatureFound = false;
		int i = 0;
		SeaCreature<?> creature = null;
		
		while(!creatureFound && i < iceChest.size())
		{
			if(iceChest.get(i).getSpecies() == species)
			{
				creature = iceChest.get(i);
				creatureFound = true;
				iceChest.remove(i);
			}
			i++;
		}
		
		return creature;
	}
=======
>>>>>>> nils_branch

			case TUNA:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == FishSpecies.TUNA)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			// TODO - handle this a little better
			default:
				return 0;
			}
		}
		if (species instanceof ShellfishSpecies)
		{
			switch ((ShellfishSpecies) species)
			{
			case CRAB:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.CRAB)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case LOBSTER:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.LOBSTER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case OYSTER:
				for (int i = 0; i < iceChest.size(); i++)
				{
					if (iceChest.get(i).getSpecies() == ShellfishSpecies.OYSTER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			// TODO - handle this a little better
			default:
				return 0;
			}
		}
		
		// for Simple Fishing items
		if (species instanceof BoatTypes)
		{
			switch ((BoatTypes) species)
			{
			case FISHING_SKIFF:
				for (int i = 0; i < toolChest.size(); i++)
				{
					if (toolChest.get(i).getType() == BoatTypes.FISHING_SKIFF)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case TRAWLER:
				for (int i = 0; i < toolChest.size(); i++)
				{
					if (toolChest.get(i).getType() == BoatTypes.TRAWLER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;

			case COMMERCIAL_TRAWLER:
				for (int i = 0; i < toolChest.size(); i++)
				{
					if (toolChest.get(i).getType() == BoatTypes.COMMERCIAL_TRAWLER)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;
			// TODO - handle this a little better
			default:
				return 0;
			}
		}
		
		// For SimpleFishingTypes
		if (species instanceof SimpleFishingItemType)
		{
			switch ((SimpleFishingItemType) species)
			{
			case FISHING_POLE:
				for (int i = 0; i < toolChest.size(); i++)
				{
					if (toolChest.get(i).getType() == SimpleFishingItemType.FISHING_POLE)
					{
						numOfSpecies++;
					}
				}
				return numOfSpecies;


			// TODO - handle this a little better
			default:
				return 0;
			}
	
		}
		return 0;
	}

	public SeaCreature<?> getSeaNextSeaCreature(Enum<?> species)
	{
		boolean creatureFound = false;
		int i = 0;
		SeaCreature<?> creature = null;
		
		while(!creatureFound && i < iceChest.size())
		{
			if(iceChest.get(i).getSpecies() == species)
			{
				creature = iceChest.get(i);
				creatureFound = true;
				iceChest.remove(i);
			}
			i++;
		}
		
		return creature;
	}

	
	public ObservableList<?> getToolChest()
	{
		// move this or something, do this intuitively 
		loadObservableLists();
		return this.toolChest;
	}
}
