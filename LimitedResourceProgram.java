/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
Matt Roberts 9-27-2017
*/
package limitedresourceprogram;

import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author mattroberts
 */
public class LimitedResourceProgram {

    /**
     * @param args the command line arguments
     */
    //demos catches on two seafood populations
    public static void main(String[] args) {
        //get the starting date
        Date startingDate=new Date();
        //tell the starting dtae
        System.out.println("The starting date and time is "+startingDate.toString()+".");
        long startingTime=startingDate.getTime();
        //create a CodPopulation object codNearCapeCod
        CodPopulation codNearCapeCod=new CodPopulation(500, 500,
                500, startingTime, 0.2);
        //tell the user
        System.out.println("Created a codeNearCapeCod CodPopulation of 500.");
        System.out.println("Trying to catch 500 cod");
        //try to catch 500 cod
        System.out.println("Caught "+codNearCapeCod.tryToConsume(500)+" cod.");
        //Tell the user that the program will wait for user input
        System.out.println("Hit any key when you are ready to try to catch 500 cod again. " +
                "The longer you wait, the closer the cod population will return to it's carrying capacity.");
        Scanner commandLineIn=new Scanner(System.in);
        commandLineIn.nextLine();
        //try to catch 500 cod
        System.out.println("Caught "+codNearCapeCod.tryToConsume(500)+" cod.");


        //create a bigger cod population
        CodPopulation biggerCodPopulation=new CodPopulation(10000, 10000,
                8000, startingTime, 0.2);
        //tell the user
        System.out.println("Created a biggerCodPopulation CodPopulation of 10000.");
        System.out.println("Trying to catch 500 cod");
        //try to catch 500 cod
        System.out.println("Caught "+biggerCodPopulation.tryToConsume(500)+" cod.");
        System.out.println("The program will now loop indefinitely,\n" +
                "letting you experiment with population growth and catching cod.\n" +
                "You will have to catch a fair amount of cod pretty quick to see effects\n" +
                "on your catch size.  Just quit it when you feel done.");
        while(true) {
            //Tell the user that the program will wait for user input
            System.out.println("Hit any key when you are ready to try to catch 500 cod again.\n" +
                    "The longer you wait, the closer the cod population will return to it's\n"
                    + "carrying capacity.");
            commandLineIn = new Scanner(System.in);
            commandLineIn.nextLine();
            //try to catch 500 cod
            System.out.println("Caught " + biggerCodPopulation.tryToConsume(500) + " cod.");
        }

    }
    
}
