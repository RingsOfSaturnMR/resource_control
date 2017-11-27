package catchgame;

/**
 * Constants for initial used throughout program.
 */
public class Constants
{
	// market price changes
	public static final double MARKET_FLUCTUATION = .10;
	public static final int TIME_BEFORE_PRICE_EXPIRATION = 1_000_000;// milliseconds? 
	
	// window sizes
	public final static int LOGIN_PANE_WIDTH = 355;
	public final static int LOGIN_PANE_HEIGHT = 275;
	public final static int NEW_USER_PANE_WIDTH = 500;
	public final static int NEW_USER_PANE_HEIGHT = 300;
	public final static int INITIAL_SERVER_PANE_WIDTH = 400;
	public final static int INITIAL_SERVER_PANE_HEIGHT = 400;
	public final static int INITIAL_GAME_PANE_WIDTH = 500;
	public final static int INITIAL_GAME_PANE_HEIGHT = 500;
	public final static int FREQUENCY_HISTOGRAM_PANE_WIDTH=500;
	public final static int FREQUENCY_HISTOGRAM_PANE_HEIGHT=500;
	
	// initial Market values of resources
	public final static double TUNA_INITIAL_PRICE_PER_POUND = 6.40;
	public final static double SALMON_INITIAL_PRICE_PER_POUND = 12.0;
	public final static double COD_INITIAL_PRICE_PER_POUND = 17.0;
	
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
	public final static String APPLICATION_NAME = "Catch! - By Caileigh, Matt, Nils, Thanh";

}
