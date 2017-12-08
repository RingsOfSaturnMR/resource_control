package catchgame;
/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/

/*
Purpose: to provide a class (acts more like a c++ struct) that contains
in one place all the constant values that can be 
played around tweak the game-play, the user interface etc. (or at least that's the goal)


Modification info:
added frequency histogram height and width
added application name
*/

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.BoatTypes;
import resources.FishSpecies;
import resources.ShellfishSpecies;
import resources.SimpleFishingItemType;

/**
 * Constants for initial values used throughout the program.
 */
public class Constants
{
	// market price changes
	public static final double MARKET_FLUCTUATION = .10;
	public static final int TIME_BEFORE_PRICE_EXPIRATION = 1_000_000;// milliseconds?

	// window sizes
	public final static int LOGIN_PANE_WIDTH = 325;
	public final static int LOGIN_PANE_HEIGHT = 500;
	public final static int NEW_USER_PANE_WIDTH = 500;
	public final static int NEW_USER_PANE_HEIGHT = 300;
	public final static int INITIAL_SERVER_PANE_WIDTH = 400;
	public final static int INITIAL_SERVER_PANE_HEIGHT = 400;
	public final static int INITIAL_GAME_PANE_WIDTH = 500;
	public final static int INITIAL_GAME_PANE_HEIGHT = 500;
	public final static int FREQUENCY_HISTOGRAM_PANE_WIDTH = 500;
	public final static int FREQUENCY_HISTOGRAM_PANE_HEIGHT = 500;
	public final static int INITIAL_SIMPLE_FISHING_PANE_WIDTH = 500;
	public final static int INITIAL_SIMPLE_FISHING_PANE_HEIGHT = 400;

	// initial Market values of resources
	public final static double TUNA_INITIAL_PRICE_PER_POUND = 6.40;
	public final static double SALMON_INITIAL_PRICE_PER_POUND = 12.0;
	public final static double COD_INITIAL_PRICE_PER_POUND = 17.0;
	public final static double OYSTER_INITIAL_PRICE_PER_POUND = 17.4;
	public final static double CRAB_INITIAL_PRICE_PER_POUND = 11.0;
	public final static double LOBSTER_INITIAL_PRICE_PER_POUND = 20.0;
	

	// initial population sizes for Ocean
	public final static int COD_INITIAL_POPULATION = 1000;
	public final static int SALMON_INITIAL_POPULATION = 700;
	public final static int TUNA_INITIAL_POPULATION = 700;

	public final static int CRAB_INITIAL_POPULATION = 700;
	public final static int LOBSTER_INITIAL_POPULATION = 300;
	public final static int OYSTER_INITIAL_POPULATION = 500;

	// maximum population sizes for Ocean
	// for now they are equal to initial, but that could be changed
	public final static int COD_MAX_POPULATION = 1000;
	public final static int SALMON_MAX_POPULATION = 700;
	public final static int TUNA_MAX_POPULATION = 700;

	public final static int CRAB_MAX_POPULATION = 700;
	public final static int LOBSTER_MAX_POPULATION = 300;
	public final static int OYSTER_MAX_POPULATION = 500;

	// initial SeaCreature min/max weight
	public final static double COD_INITIAL_WEIGHT_MIN = 5;
	public final static double COD_INITIAL_WEIGHT_MAX = 12;
	public final static double SALMON_INITIAL_WEIGHT_MIN = 5;
	public final static double SALMON_INITIAL_WEIGHT_MAX = 12;
	public final static double TUNA_INITIAL_WEIGHT_MIN = 5;
	public final static double TUNA_INITIAL_WEIGHT_MAX = 12;

	public final static double CRAB_INITIAL_WEIGHT_MIN = 2;
	public final static double CRAB_INITIAL_WEIGHT_MAX = 5;
	public final static double LOBSTER_INITIAL_WEIGHT_MIN = 3;
	public final static double LOBSTER_INITIAL_WEIGHT_MAX = 7;
	public final static double OYSTER_INITIAL_WEIGHT_MIN = 1;
	public final static double OYSTER_INITIAL_WEIGHT_MAX = 4;

	// Basics
	public final static String APPLICATION_NAME = "Catch!! - By Caileigh, Matt, Nls, Thanh";
	public final static short LEFT = -1;
	public final static short RIGHT = 1;
	/**
	 * An array of SeaCreature Species the game supports
	 */
	public final static Enum[] SUPPORTED_SPECIES =
	{ FishSpecies.COD, FishSpecies.SALMON, FishSpecies.TUNA, ShellfishSpecies.CRAB, ShellfishSpecies.LOBSTER, ShellfishSpecies.OYSTER };

	/**
	 * An array of Equipment types the game supports
	 */
	public static Enum[] SUPPORTED_EQUIPMENT =
	{ BoatTypes.COMMERCIAL_TRAWLER, BoatTypes.FISHING_SKIFF, BoatTypes.TRAWLER, SimpleFishingItemType.FISHING_POLE };

	/**
	 * 
	 * @param desiredResourceType species of SeaCreature
	 * @return Image of SeaCreature or null if it can't find the image.
	 */
	public static final Image getImage(final Enum<?> desiredResourceType)
	{
		if (desiredResourceType instanceof FishSpecies || desiredResourceType instanceof ShellfishSpecies)
		{
			for (int i = 0; i < SUPPORTED_SPECIES.length; i++)
			{
				if (desiredResourceType == SUPPORTED_SPECIES[i])
				{
					return (new Image("img/" + SUPPORTED_SPECIES[i].toString().toLowerCase() +
							".png"));
				}
			}
		}
		
		if (desiredResourceType instanceof BoatTypes || desiredResourceType instanceof SimpleFishingItemType)
		{	
			for(Enum<?> currentType: SUPPORTED_EQUIPMENT )
			{
				if( desiredResourceType == currentType)
				{
					return (new Image("img/" + currentType.toString().toLowerCase() +
							".png"));
				}
			}
		}
		return null;
	}
	
	//constants to multiple weight by to get pixel width (aspect is preserved)
		public final static short COD_WEIGHT_GRAPHIC_MULTIPLE = 10;
}
