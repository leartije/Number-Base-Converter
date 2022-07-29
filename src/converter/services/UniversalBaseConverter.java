package converter.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import static converter.text.Msg.WRONG_CHARACTERS;
import static converter.text.Msg.ZERO;

public class UniversalBaseConverter implements Converter {

    public final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

    @Override
    public String convertIntegers(int base, int targetBase, String number) {
        if (isNumInValidBase(base, number)) {
            return String.format(WRONG_CHARACTERS, CHARACTERS.substring(0, base));
        }
        BigInteger decimal = convertToDec(base, number);
        return convertDecToAnyBase(targetBase, decimal);
    }

    @Override
    public String convertFractions(int base, int targetBase, String number, String fractional) {
        if (isNumInValidBase(base, number) || isNumInValidBase(base, fractional)) {
            return String.format(WRONG_CHARACTERS, CHARACTERS.substring(0, base));
        }
        //convert to decimal
        BigInteger integerToDecimal = convertToDec(base, number);
        BigDecimal fractionToDecimal = convertFractionalPartToDec(base, fractional);

        //convert to target base
        String integerPart = convertDecToAnyBase(targetBase, integerToDecimal);
        String fractionalPart = convertFractionalToAnyBase(targetBase, fractionToDecimal);

        if (integerPart.isEmpty()) {
            integerPart = ZERO;
        }

        return String.format("%s.%s", integerPart, fractionalPart);
    }

    //util
    private BigInteger convertToDec(int numBase, String number) {
        BigInteger sum = BigInteger.ZERO;
        int n = number.length() - 1;

        for (int i = n; i >= 0; i--) {
            sum = sum.add(BigInteger.valueOf(Character.getNumericValue(number.charAt(i)))
                    .multiply(BigInteger.valueOf(numBase).pow(n - i)));
        }

        return sum;
    }

    private String convertDecToAnyBase(int targetBase, BigInteger number) {
        StringBuilder result = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger temp = number.remainder(BigInteger.valueOf(targetBase));
            result.append(CHARACTERS.charAt(Integer.parseInt(String.valueOf(temp))));

            number = number.divide(BigInteger.valueOf(targetBase));
        }

        return result.reverse().toString();
    }

    public BigDecimal convertFractionalPartToDec(int base, String number) {
        BigDecimal sum = BigDecimal.ZERO;
        int n = -1;

        for (int i = 0; i < number.length() ; i++) {
            int realValue = Character.getNumericValue(number.charAt(i));
            sum = sum.add(BigDecimal.valueOf(realValue)
                    .multiply(BigDecimal.valueOf(base).pow(n, MathContext.DECIMAL64)));
            n--;

        }

        return sum.setScale(5, RoundingMode.DOWN);
    }

    public String convertFractionalToAnyBase(int base, BigDecimal number) {
        int n = 5;

        StringBuilder stringBuilder = new StringBuilder();
        while (n > 0) {
            BigDecimal temp = number.multiply(BigDecimal.valueOf(base));
            String sim = String.valueOf(CHARACTERS.charAt(temp.intValue()));
            stringBuilder.append(sim);

            number = temp.remainder(BigDecimal.ONE);
            n--;
        }

        return stringBuilder.toString();
    }

    private boolean isNumInValidBase(int base, String number) {
        String validCharacters = CHARACTERS.substring(0, base);

        boolean flag = true;
        int n;

        for (int i = 0; i < number.length() && flag; i++) {
            n = 0;
            for (int j = 0; j < validCharacters.length(); j++) {
                if (number.charAt(i) == validCharacters.charAt(j)) {
                    n++;
                }
                if (j == validCharacters.length() - 1 && n == 0) {
                    flag = false;
                }
            }
        }
        return !flag;
    }
}


