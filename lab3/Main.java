package lab3;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter base: ");
        long base = sc.nextLong();

        System.out.print("Enter exponent: ");
        long exponent = sc.nextLong();

        System.out.print("Enter modulus: ");
        long modulus = sc.nextLong();

        long result = modPow(base, exponent, modulus);

        System.out.println(base + "^" + exponent + " mod " + modulus + " = " + result);

        sc.close();
    }

    /**
     * Fast modular exponentiation using successive squaring.
     * Computes (base^exponent) mod modulus in O(log exponent) time.
     */
    private static long modPow(long base, long exponent, long modulus) {
        if (modulus == 1) return 0;

        base = base % modulus;
        long result = 1;

        while (exponent > 0) {
            // If current bit of exponent is 1, multiply result by base
            if ((exponent & 1L) == 1L) {
                result = (result * base) % modulus;
            }

            // Square the base each step (successive squaring)
            base = (base * base) % modulus;

            // Shift exponent to process the next bit
            exponent >>= 1;
        }

        return result;
    }
}
