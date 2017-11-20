package utilities;

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
		int randomInt = rand.nextInt(max) + min;
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
