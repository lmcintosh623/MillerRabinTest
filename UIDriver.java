/* Author: Lukas McIntosh
 * Class: M/CS 478 @ WWU
 * Professor: Dr. Hartenstine
 * Date: June 6th  2020
 * Program Purpose: Generate a user interface that performs functions from millerRabin.java
 */ 

import java.math.BigInteger;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Scanner;

public class UIDriver{

    public static final BigInteger maxNum = millerRabin.maxBigInt(16384);
    public static void main(String[] args) {
        boolean switchFulfilled = false;
        String strVar = "";
        Scanner userInput = new Scanner(System.in);
        NumberFormat myForm = NumberFormat.getInstance();
        myForm.setGroupingUsed(true);

        while(!switchFulfilled){
            System.out.println();
            System.out.println("Provide the number of the operation that you wish to perform from the following list: ");
            System.out.println("1. Generate a probably prime number p between user specified bounds within the range 2^1 <= p <= 2^16384 ");
            System.out.println("2. Perform the Miller-Rabin test on a user input number ");
            System.out.println("Enter \"Quit\" or \"q\" to quit");
            String menuOption = userInput.nextLine();
            switch (menuOption) {
                //  CHECK THE EXCEPTION/ERROR STATEMENTS
                case "1":
                    try {
                        System.out.println();
                        System.out.println("Input two distinct integers between 1 and 2048 inclusive for the bounds \n(Ex. Input: \"40 45\" --> Output: probable prime p such that 2^40 <= p <= 2^45)");
                        strVar = userInput.nextLine();
                        Scanner additionalInfo = new Scanner(strVar.trim());

                        int firstBound = Integer.parseInt(additionalInfo.next());
                        int secondBound = Integer.parseInt(additionalInfo.next());
                        if(firstBound == secondBound){
                            System.out.println(new IllegalArgumentException("INVALID INPUT; provided arguments are not distinct."));
                            additionalInfo.close();
                            return;
                        }
                        else if(firstBound > secondBound){
                            int temp = firstBound;
                            firstBound = secondBound;
                            secondBound = temp;
                        }
                        if(additionalInfo.hasNext()){
                            System.out.println(new IllegalArgumentException("INVALID INPUT; too many arguments."));
                            additionalInfo.close();
                            return;
                        }
                        else if((firstBound <= 0 || firstBound >= 16385) || (secondBound <= 0 || secondBound >= 16385)){
                            System.out.println(new IllegalArgumentException("INVALID INPUT; user input out of acceptable range."));
                            additionalInfo.close();
                            return;
                        }
                        additionalInfo.close();
                        //TODO: Edit this statement to be more clear
                        System.out.println("Please select the number of iterations to test each potential prime from the list below:\n1.) 50\n2.) 100\n3.)150");
                        strVar = userInput.nextLine();
                        int numTests = 0;
                        switch(strVar){
                            case "1":
                            case "1.)":
                            case "50":
                                numTests = 50;
                            case "2":
                            case "2.)":
                            case "100":
                                numTests = 100;
                            case "3":
                            case "3.)":
                            case "150":
                                numTests = 150;
                        }

                        BigInteger res = millerRabin.generatePrimeNumberWithinBounds(firstBound, secondBound, numTests);
                        if(res.compareTo(BigInteger.ZERO) == 0){
                            System.out.println("TEST FAILED;");
                        }
                        else{
                            System.out.println("Prime number generated = " + myForm.format(res) + "\n");
                        }
                        System.out.println("Would you like to perform another operation? Y/N");
                        strVar = userInput.nextLine();
                        switch (strVar.trim()) {
                            case "Y":
                            case "y":
                                break;
                            case "N":
                            case "n":
                                switchFulfilled = true;
                                break;
                            default:
                                System.out.println("INVALID INPUT; returning to user prompt");
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace(); //*DEBUGGING*
                        throw new IllegalArgumentException("INVALID INPUT;");
                    }
                    break;
                case "2":
                    try{
                        System.out.println();
                        System.out.println("Input a number with less than or equal to [insert fixed number] digits (without special characters)\n(Ex 2^16384: " + maxNum +")\n");
                        strVar = userInput.nextLine();
                        BigInteger number = new BigInteger(strVar.trim());
                        System.out.println("\nInput the number of iterations for the Miller-Rabin test to be performed for a given number\nNote: In general, 50-100 iterations is considered acceptable in common practice, and 100-200 is almost certainly accepectable\n(Ex. Input: 50 --> Output: The number being tested and it's results after 50 iterations of the Miller-Rabin test)\n");
                        strVar = userInput.nextLine();
                        int numTests = Integer.parseInt(strVar.trim());
                        if((number.compareTo(BigInteger.ONE) == -1) || (number.compareTo(maxNum) == 1)){
                            throw new IllegalArgumentException("INVALID INPUT; Input value out of acceptable range");
                        }
                        boolean composite = false;
                        int compositeCount = 0;
                        int primeCount = 0;
                        for(int i = 0; i < numTests; i++){
                            composite = millerRabin.millerRabinTest(number);
                            if (composite == true){
                                compositeCount++;
                            }
                            else{
                                primeCount++;
                            }
                        }
                        // At this point, if composite is false, it is PROBABLY prime.
                        if(compositeCount == 0){
                            System.out.println("\n---------------------RESULTS---------------------");
                            System.out.println(myForm.format(number) + "\nThe number above has at most " + millerRabin.scientificNotationConverter((new BigDecimal("0.25").pow(numTests)).toPlainString()) + " percent chance of being composite");
                            System.out.println("Composite Witnesses: " + compositeCount + ", non-witness: " + primeCount + ", and total summed = " + (primeCount + compositeCount));
                        }
                        else{
                            System.out.println("\n---------------------RESULTS---------------------");
                            System.out.println(myForm.format(number) + "\nThe number above is composite");
                            System.out.println("Composite Witnesses: " + compositeCount + ", non-witness: " + primeCount + ", and total summed = " + (primeCount + compositeCount));
                        }
                        System.out.println("\nWould you like to perform another computation? Y/N");
                        strVar = userInput.nextLine();
                        switch (strVar.trim()) {
                            case "Y":
                            case "y":
                                break;
                            case "N":
                            case "n":
                                switchFulfilled = true;
                                break;
                            default:
                                System.out.println("INVALID INPUT; returning to user prompt");
                                break;
                        }
                    }
                    catch(NumberFormatException e){
                        e.printStackTrace(); //*DEBUGGING*
                        throw new NumberFormatException("INVALID INPUT; number contained special characters --> " + strVar);
                    }
                    break;
                case "Quit":
                case "quit":
                case "Q":
                case "q":
                    switchFulfilled = true;
                    break;
            }
        }
    userInput.close();
    }    
}