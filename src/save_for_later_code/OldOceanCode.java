package save_for_later_code;

public class OldOceanCode {
	/*
	 **********************
	 These are unused stubs:
	 **********************
	 
	 
	 public SeaCreature extractRandomSeaCreature()
		{
			// STUBB
			int random = getRandomInt(0, 6);

			switch (random)
			{
			case 0:
				return new Fish(FishSpecies.COD, 1);
			case 1:
				return new Fish(FishSpecies.SALMON, 1);
			case 2:
				return new Fish(FishSpecies.TUNA, 1);
			case 3:
				return new Shellfish(ShellfishSpecies.CRAB, 1);
			case 4:
				return new Shellfish(ShellfishSpecies.LOBSTER, 1);
			case 5:
				return new Shellfish(ShellfishSpecies.OYSTER, 1);
			default:
				return null;
			}
		}
		
		public Fish extractCod()
		{
			return new Fish(FishSpecies.COD, 20);
		}

		public Fish extractSalmon()
		{
			return new Fish(FishSpecies.SALMON, 20);
		}

		public Fish extractTuna()
		{
			return new Fish(FishSpecies.TUNA, 20);
		}

		public Shellfish extractCrab()
		{
			return new Shellfish(ShellfishSpecies.CRAB, 1);
		}

		public Shellfish extractLobster()
		{
			return new Shellfish(ShellfishSpecies.LOBSTER, 1);
		}

		public Shellfish extractOyster()
		{
			return new Shellfish(ShellfishSpecies.OYSTER, 1);
		}
	 */



	/*
	 ****************************************************
	This code was reworked and is included in a new form:
	****************************************************
		public ArrayList<Fish> givePacketOfFish(FishSpecies fish, int currentPopulation, int maxPopulation) throws Exception
		{

			//System.out.println("in giving packet");

			double clientPlentifullness = (double) currentPopulation / (double) maxPopulation;
			ArrayList<Fish> fishPacket = new ArrayList<>();
			switch (fish)
			{
			case COD:
				double codPlentifullness = (double) codPopulation.size() / (double) Constants.COD_MAX_POPULATION;
				return getPacketOfFish(codPopulation, codPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
			// break;
			case SALMON:
				double salmonPlentifullness = (double) salmonPopulation.size() / (double) Constants.SALMON_MAX_POPULATION;
				return getPacketOfFish(salmonPopulation, salmonPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

			case TUNA:
				double tunaPlentifullness = (double) tunaPopulation.size() / (double) Constants.TUNA_MAX_POPULATION;
				return getPacketOfFish(tunaPopulation, tunaPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
			}
			return fishPacket;
		}

		public ArrayList<Shellfish> extractABunchOfFish(ShellfishSpecies shellfish, int currentPopulation, int maxPopulation, int oceanMaxPopulation)
		{
			System.out.println("in giving packet");
			double clientPlentifullness = (double) currentPopulation / (double) maxPopulation;
			ArrayList<Shellfish> shellfishPacket = new ArrayList<>();
			switch (shellfish)
			{
			case OYSTER:
				double oysterPlentifullness = (double) oysterPopulation.size() / (double) Constants.OYSTER_MAX_POPULATION;
				return getPacketOfShellfish(oysterPopulation, oysterPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

			case LOBSTER:
				double lobsterPlentifullness = (double) lobsterPopulation.size() / (double) Constants.LOBSTER_MAX_POPULATION;
				return getPacketOfShellfish(lobsterPopulation, lobsterPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);
			case CRAB:
				double crabPlentifullness = (double) crabPopulation.size() / (double) Constants.CRAB_MAX_POPULATION;
				return getPacketOfShellfish(crabPopulation, crabPlentifullness, clientPlentifullness, maxPopulation, currentPopulation);

			// default;
			}
			return shellfishPacket;
		}

	private int updateCodPopulation(long elapsedTime)
	{
		int carryingCapacityPopulation = Constants.COD_MAX_POPULATION;
		int lastPopulation = codPopulation.size();
		double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
		double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
		double rawPopulation = (double) carryingCapacityPopulation / denominator;
		// make it an int becuase we can't have (viable) fractions of seafood
		int updatedPopulation = (int) rawPopulation;
		// next line for debug
		//System.out.println(updatedPopulation);
		return updatedPopulation - codPopulation.size();
	}

	private int updateSalmonPopulation(long elapsedTime)
		{
			int carryingCapacityPopulation = Constants.SALMON_MAX_POPULATION;
			int lastPopulation = salmonPopulation.size();
			double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
			double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
			double rawPopulation = (double) carryingCapacityPopulation / denominator;
			int updatedPopulation = (int) rawPopulation;
			//System.out.println(updatedPopulation);
			return updatedPopulation - salmonPopulation.size();

		}

		private int updateTunaPopulation(long elapsedTime)
		{
			int carryingCapacityPopulation = Constants.TUNA_MAX_POPULATION;
			int lastPopulation = tunaPopulation.size();
			double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
			double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
			double rawPopulation = (double) carryingCapacityPopulation / denominator;
			int updatedPopulation = (int) rawPopulation;
			System.out.println(updatedPopulation);
			return updatedPopulation - tunaPopulation.size();

		}

		private int updateOysterPopulation(long elapsedTime)
		{
			int carryingCapacityPopulation = Constants.OYSTER_MAX_POPULATION;
			int lastPopulation = oysterPopulation.size();
			double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
			double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
			double rawPopulation = (double) carryingCapacityPopulation / denominator;
			int updatedPopulation = (int) rawPopulation;

			System.out.println(updatedPopulation);

			return updatedPopulation - oysterPopulation.size();
		}

		private int updateLobsterPopulation(long elapsedTime)
		{
			int carryingCapacityPopulation = Constants.LOBSTER_MAX_POPULATION;
			int lastPopulation = lobsterPopulation.size();
			double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
			double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
			double rawPopulation = (double) carryingCapacityPopulation / denominator;
			int updatedPopulation = (int) rawPopulation;

			System.out.println(updatedPopulation);
			return updatedPopulation - lobsterPopulation.size();
		}

		private int updateCrabPopulation(long elapsedTime)
		{
			int carryingCapacityPopulation = Constants.CRAB_MAX_POPULATION;
			int lastPopulation = crabPopulation.size();
			double A = (double) (carryingCapacityPopulation - lastPopulation) / (double) lastPopulation;
			double denominator = 1 + A * Math.exp(-relativeGrowthRate * elapsedTime);
			double rawPopulation = (double) carryingCapacityPopulation / denominator;
			int updatedPopulation = (int) rawPopulation;

			System.out.println(updatedPopulation);

			return updatedPopulation - crabPopulation.size();
		}

	*/
}
