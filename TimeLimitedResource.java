package limitedresourceprogram;
/*
Matt Roberts 9-27-2017
*/

/*
so one of our resources is various kinds of seafood
we could try to model individual population growths at an individual level, but that sounds hard
so I thought we'd go with the overall population (individuals don't matter much here)

species should be subclasses of TimeLimitedResource
tryToConsume should be callable for any species
thus, you can try to catch any kind of seafood
*/

/*
UMl
 abstract TimeLimitedResource extends Object

 -lastUpdateTime: long
 -population: int
 -normalPopulation: int
 -populationMinForMaxCatching: int

 #TimeLimitedResource(initialPopulation: int, normalPopulation: int,
                        populationMinForMaxCatching: int lastUpdateTime: long)
 -updatePopulation(): void
 #abstract getPopulationWithGrowth(elapsedTime: long): int
 +final tryToConsume(units: int): int
 -setLastUpdateTime(lastUpdateTime: long): void
 -setPopulation(population: int): void
 -setNormalPopulation(normalPopulation: int): void
 -setPopulationMinForMaxCatching(populationMinForMaxCatching: int): void
 +final getLastUpdateTime(): long
 +final getPopulation(): int
 +final getNormalPopulation(): int
 +final getPopulationMinForMaxCatching(): int
*/

/*
 setters are private because right now we cannot effect the population or update time
 this is easy to change as necessary!
 */

import java.util.Date;

public abstract class TimeLimitedResource extends Object{

    //the fields (could be for everywhere in the game or a specific area)

    //for now, this can be the number of milliseconds since the start of 1970
    //we may want a game controller object or function that gives it in gameTime instead
    //the importance is that a standard is kept (a little math may need to be done
    // if the game is stopped and then resumed)
    private long lastUpdateTime;
    //the total population in the game or area
    private int population;
    //what it would return to if left alone
    private int normalPopulation;
    //the population necessary to catch all the fish you want to catch
    private int populationMinForMaxCatching;

    //the constructor

    //initial population is what the population starts out at
    protected TimeLimitedResource(int initialPopulation, int normalPopulation, int populationMinForMaxCatching, long lastUpdateTime){
        setLastUpdateTime(lastUpdateTime);
        setPopulation(initialPopulation);
        setNormalPopulation(normalPopulation);
        setPopulationMinForMaxCatching(populationMinForMaxCatching);
    }

    //always will need to update (called in tryToConsume
    private void updatePopulation(){
        Date currentDate=new Date();
        long currentUpdateTime=currentDate.getTime();
        long elapsedTime=currentUpdateTime-lastUpdateTime;
        lastUpdateTime=currentUpdateTime;
        if (population<normalPopulation){
            population=getPopulationWithGrowth(elapsedTime/1000);
        }
    }

    //this can vary by the resource, so it's abstract, but is only needed by subclasses
    protected abstract int getPopulationWithGrowth(long elaspedTime);

    //the method for seeing how much seafood you catch if you try to catch x units
    //this method should return a value between 0 and the parameter units
    public final int tryToConsume(int units){

        //first update the population
        updatePopulation();

        //then figure out how many are caught

        //can't catch more than the population
        if (units>population){
            units=population;
        }
        //this takes into account, say if there are 500 fish and you try to ctach 500 fish, you probably won't catch
        //all of them, no matter how "plentiful" they are. So we say the most you could catch is half
        double effectivePopulation=population-((double)units)/2.0;
        double plentifulness=effectivePopulation/(double)populationMinForMaxCatching;
        //plentifulness should be between 0 and 1
        if (plentifulness>1){
            plentifulness=1;
        }
        //plentifulness acts as a coefficient for units.  They just multiply to give you the units caught
        units*=plentifulness;
        population-=units;
        return units;
    }

    //kind of unnecessary as any long is (theoretically) valid, but in here for good measure
    private void setLastUpdateTime(long lastUpdateTime){
        this.lastUpdateTime=lastUpdateTime;
    }

    //if less than 0, set to 0
    private void setPopulation(int population){
        if (population<0)
            population=0;

        this.population=population;
    }

    //if less than 0, set to 0
    private void setNormalPopulation(int normalPopulation){
        if (normalPopulation<0)
            normalPopulation=0;

        this.normalPopulation=normalPopulation;
    }

    //if less than 0, set to 0
    private void setPopulationMinForMaxCatching(int populationMinForMaxCatching){
        if (populationMinForMaxCatching<0)
            populationMinForMaxCatching=0;

        this.populationMinForMaxCatching=populationMinForMaxCatching;
    }

    //if less than 0, set to 0
    public final long getLastUpdateTime() {
        return lastUpdateTime;
    }

    //used by subclasses, for anywhere
    public final int getPopulation() {
        return population;
    }

    //used by subclasses, for anywhere
    public final int getNormalPopulation() {
        return normalPopulation;
    }

    //for anywhere
    public final int getPopulationMinForMaxCatching() {
        return populationMinForMaxCatching;
    }
}
