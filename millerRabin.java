/* Author: Lukas McIntosh
 * Class: M/CS 478 @ WWU
 * Professor: Dr. Hartenstine
 * Date: June 10th, 2020
 * Program Purpose: Recieve instructions from UIDriver.java and perform the following operations:
 *                      1.) Generate a prime number within bounds given by the user (range of [2^1, 2^128])
 *                      2.) Perform the Miller-Rabin test for composite numbers on a given user input and randomly generated a (range given by [2, n-2])
 *                      3.) Perform a variety of the operations, being able top interface with one another
 */ 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.text.NumberFormat;

public class millerRabin{

    public static BigInteger maxBigInt(long num){
        BigInteger newNum = new BigInteger(String.valueOf(num));
        return newNum;
    }

    public static String scientificNotationConverter(String num){
        char[] arr = num.toCharArray();
        int counter = 0;
        int i = 2;
        while(arr[i] == '0'){
            counter++;
        }
        String retVar = "10^-"+Integer.toString(counter);
        System.out.println(retVar);
        return retVar;
    }


    public static void generatePrimeNumberWithinBounds(int firstBound, int secondBound, int numTests){
        BigInteger[] lower_upper = powersOfTwo.buildArr(firstBound, secondBound);
        BigInteger lower = lower_upper[0];
        BigInteger upper = lower_upper[1];
        BigInteger prime = BigInteger.ZERO;
        NumberFormat numForm = NumberFormat.getInstance();
        numForm.setGroupingUsed(true);

        System.out.println("\nGenerating a prime number between the values " + numForm.format(lower).toString() + " and " + numForm.format(upper).toString() + ".");

        System.out.println("Prime number is --> " + prime.toString());
    }

    public static BigInteger randomBigInteger(BigInteger num){
        BigInteger lowerBound = new BigInteger("2");
        BigInteger upperBound = num.subtract(lowerBound);
        BigInteger range = upperBound.subtract(lowerBound);
        Random randomObj = new Random();
        int len = upperBound.bitLength();
        BigInteger newRandBigInt = new BigInteger(len, randomObj);
        if(newRandBigInt.compareTo(lowerBound) < 0){
            newRandBigInt = newRandBigInt.add(lowerBound);
        }
        if(newRandBigInt.compareTo(upperBound) >= 0){
            newRandBigInt = newRandBigInt.mod(range).add(lowerBound);
        }
        System.out.println("New Random BigInteger within range --> " + newRandBigInt.toString());
        return newRandBigInt;
    }

    // Returns TRUE if input number is compositite
    // Returns FALSE if there were no witnesses of compositeness for input number
    
    public static boolean millerRabinTest(BigInteger num){
        System.out.println("\nPerforming Miller-Rabin test on n = " + num);

        // Generating random number between 2 and num - 2 inclusive.
        BigInteger randA = randomBigInteger(num);
        BigInteger two = new BigInteger("2");

        // Trivial case where num is even and obviously not prime or gcd between the numbers is greater than 1 (meaning they contain a common factor)
        if((num.remainder(BigInteger.ONE.add(BigInteger.ONE)) == BigInteger.ZERO) || (BigInteger.ONE.compareTo(num.gcd(randA)) == -1)){
            //System.out.println("trivial case");
            return true;
        }
        int twoFactorInt = factorForMiller(num);
        BigInteger oddFactor = new BigInteger(num.divide(two.pow(twoFactorInt)).toString());
        //System.out.println("\nDONE WITH FACTORIZATION, n - 1 = " + (num.subtract(BigInteger.ONE)) + " = " + oddFactor.toString() + " * 2^(" + twoFactorInt + ")");
        // twoFactor = proper power of two, q = odd factor
        // n - 1 = 2^(k)*q ---> num - 1 = 2^(twoFactor)*oddFactor
        System.out.println("Before first test, a =  " + randA.toString());
        randA = randA.modPow(oddFactor, num);
        System.out.println("After first test, a = " + randA.toString());

        if(randA.compareTo(BigInteger.ONE) == 0){
            return false;
        }
        for(int i = 0; i <= twoFactorInt-1; i++){
            if(randA.compareTo(num.subtract(BigInteger.ONE)) == 0){
                return false;
            }
            randA = randA.multiply(randA);
            randA = randA.mod(num);
            System.out.println("Iteration " + (i+1) + ", a = " + randA.toString());
        }
        //System.out.println("end case");
        return true;
    }

    // function to factor n - 1 quantity into a power of two multiplied by an odd integer q. 
    // RETURNS k for n - 1 = 2^(k)q
    public static int factorForMiller(BigInteger num){
        BigInteger two = new BigInteger("2");
        BigInteger twoFactor = new BigInteger("2");
        BigInteger q = new BigInteger("2");
        BigInteger n = new BigInteger((num.subtract(BigInteger.ONE)).toString());
        int counter = 0;
        while(q.remainder(two) == BigInteger.ZERO){
            try {
                if(counter == 0){
                    q = n.divide(twoFactor);
                    counter += 1;
                    System.out.println("FIRST: q value --> " + q.toString());
                }
                else{
                    twoFactor = twoFactor.multiply(two);
                    q = n.divide(twoFactor);
                    counter += 1;
                    System.out.println("q value --> " + q.toString());
                }
            } catch (Exception e) {
                System.out.println("caught exception, q = " + q.toString());
            }
        }
        System.out.println(num.subtract(BigInteger.ONE) + " = " + q.toString() + " * 2^(" + counter + ")");
        return counter;
    }
}