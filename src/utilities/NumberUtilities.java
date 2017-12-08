package utilities;
/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/
/*
Purpose: to contain any really generic number functions.
This currently includes getRandomInt for a range,
and getRandomDouble for a range

Modification info:
new
*/
import java.util.Random;

public class NumberUtilities {
	
	private static Random rand = new Random();
	/**
	 * Returns random int in the specified range.
	 * @param min the minimum value for the range
	 * @param max the maximum value for the range
	 * @return A random int in the specified range
	 */
	public static int getRandomInt(int min, int max)
	{
		int randomInt = rand.nextInt(max+1) + min;
		return randomInt;
	}

	/**
	 * Returns random double in the specified range.
	 * @param min min the minimum value for the range
	 * @param max the minimum value for the range
	 * @return A random double in the specified range
	 */
	public static double getRandomDouble(double max, double min)
	{
		double randomDouble = (max - min) * rand.nextDouble() + min;
		return randomDouble;
	}
}
