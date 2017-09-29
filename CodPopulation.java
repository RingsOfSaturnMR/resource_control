package limitedresourceprogram;
/*
Matt Roberts 9-27-2017
*/
/*
+SeafoodPopulation(initialPopulation: int, normalPopulation: int,
                        populationMinForMaxCatching: int, startingTime: long,
                        relativeGrowthRate: double)
 */

//an example of a concrete SeafoodPopulation subclass
public class CodPopulation extends SeafoodPopulation {

    //set-up super and then change growth rate to whatever it should be
    public CodPopulation(int initialPopulation, int normalPopulation, int populationMinForMaxCatching,
                         long startingTime, double relativeGrowthRate){
        super(initialPopulation, normalPopulation, populationMinForMaxCatching, startingTime);
        //set whatever we want it to be for this population
        setRelativeGrowthRate(relativeGrowthRate);
    }
}
