import java.math.BigInteger;

public class powersOfTwo{

    public static BigInteger[] buildArr(int lowerBound, int upperBound){
        // BigInteger[] pot = new BigInteger[64];
        // BigInteger temp = new BigInteger("2");
        // for(int i = 1; i <= 64; i++){
        //     pot[i-1] = temp.pow(i);
        // }
        // return pot;
        BigInteger[] pot = new BigInteger[2];
        BigInteger two = new BigInteger("2");
        pot[0] = two.pow(lowerBound);
        pot[1] = two.pow(upperBound);
        return pot;
    }

    // Possibly add parameter for upper bound of array? 
    public static BigInteger[] buildFullArr(){
        BigInteger[] pot = new BigInteger[64];
        BigInteger temp = new BigInteger("2");
        for(int i = 1; i <= 64; i++){
            pot[i-1] = temp.pow(i);
        }
        return pot;
    }

}