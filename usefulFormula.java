package limitedresourceprogram;
/*
Matt Roberts 9-27-2017
*/
/*
UML
abstract SeafoodPopulation extends TimeLimitedResource

-relativeGrowthRate
#SeafoodPopulation(initialPopulation:int, normalPopulation:int,
                        populationMinForMaxCatching:int, lastUpdateTime:long)
#getPopulationGrowth(elapsedTime: long): int
+final setRelativeGrowthRate(relativeGrowthRate: double): void
+final getRelativeGrowthRate(): double


 */
//cannot instantiate seafood population because it's still too abstract
public abstract class SeafoodPopulation extends TimeLimitedResource{

    //this should be set in all subclasses.  Otherwise there is no growth, unless set later
    private double relativeGrowthRate=0;


    //all fields are inherited, so just set-up super
    protected SeafoodPopulation(int initialPopulation, int normalPopulation, int populationMinForMaxCatching, long lastUpdateTime){
        super(initialPopulation, normalPopulation, populationMinForMaxCatching, lastUpdateTime);
    }

    //this is a general algorithim for a formula which will tell you the new population
    @Override
    protected final int getPopulationWithGrowth(long elapsedTime) {
        int carryingCapacityPopulation=getNormalPopulation();
        int lastPopulation=getPopulation();
        double A=(double)(carryingCapacityPopulation-lastPopulation)/(double)lastPopulation;
        double denominator=1+A*Math.exp(-relativeGrowthRate*elapsedTime);
        double rawPopulation=(double)carryingCapacityPopulation/denominator;
        //make it an int becuase we can't have (viable) fractions of seafood
        int updatedPopulation=(int)rawPopulation;
        //next line for debug
        //System.out.println(updatedPopulation);
        return updatedPopulation;
    }

    //each subclass should set it's own relative growth rate (varies by the species and maybe even population)
    public final void setRelativeGrowthRate(double relativeGrowthRate) {
        this.relativeGrowthRate = relativeGrowthRate;
    }

    //just gets it
    public final double getRelativeGrowthRate() {
        return relativeGrowthRate;
    }
}
