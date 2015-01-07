package tk.hintss.botss.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Henry on 1/2/2015.
 */
public class NumberUtil {
    public static Short[] numberToDigitArray(long number) {
        ArrayList<Short> digits = new ArrayList<>();

        while (number > 0) {
            digits.add((short) (number % 10));
            number /= 10;
        }

        Collections.reverse(digits);

        return digits.toArray(new Short[digits.size()]);
    }

    public static short luhnSum(long number) {
        Short[] digits = numberToDigitArray(number);

        int workingNum = 0;

        for (int i = digits.length - 1; i >= 0; i--) {
            workingNum += digits[i];

            if ((digits.length - i) % 2 == 1) {
                if (digits[i] > 4) {
                    workingNum -= 9;
                }

                workingNum += digits[i];
            }
        }

        workingNum %= 10;

        if (workingNum == 0) {
            workingNum = 10;
        }

        workingNum = 10 - workingNum;

        return (short) workingNum;
    }

    public static boolean luhnCheck(long number) {
        Short[] digits = numberToDigitArray(number);

        int workingNum = 0;

        for (int i = digits.length - 1; i >= 0; i--) {
            workingNum += digits[i];

            if ((digits.length - i) % 2 == 0) {
                if (digits[i] > 4) {
                    workingNum -= 9;
                }

                workingNum += digits[i];
            }
        }

        workingNum %= 10;

        return (workingNum == 0);
    }
}
