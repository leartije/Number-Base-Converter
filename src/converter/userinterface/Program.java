package converter.userinterface;

import converter.services.Converter;
import converter.services.UniversalBaseConverter;

import java.util.Scanner;

import static converter.text.Msg.*;

public class Program {

    public static final Scanner SCANNER = new Scanner(System.in);
    public Converter converter;

    public void start() {
        while (true) {
            System.out.print(MAIN_MENU);
            String input = SCANNER.nextLine().toLowerCase();
            if (EXIT.equals(input)) {
                System.exit(0);
            }

            String[] split = input.strip().split("\\s+");

            if (split.length != 2) {
                System.out.println(NEED_TWO_NUMBERS);
                continue;
            }

            if (invalidNum(split[0]) || invalidNum(split[1])) {
                continue;
            }

            while (true) {
                System.out.printf(ENTER_DECIMAL, split[0], split[1]);
                String[] number = SCANNER.nextLine().split("\\.");

                if (BACK.equals(number[0])) {
                    break;
                }

                converter = new UniversalBaseConverter();
                if (number.length == 2) {
                    String decimal = converter.convertFractions(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
                            number[0], number[1]);
                    System.out.printf(CONVERSION_RESULT, decimal);
                    continue;
                }

                System.out.printf(CONVERSION_RESULT, converter.convertIntegers(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]), number[0]));
            }
        }
    }


    //util
    private boolean invalidNum(String potNum) {
        int num = toInt(potNum);
        if (num == -1) {
            return true;
        }

        int MIN = 2;
        int MAX = 36;

        if (num >= MIN && num <= MAX) {
            return false;
        }

        System.out.printf(IN_RANGE, num);
        return true;
    }

    private int toInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            System.out.printf(WRONG_INPUT, num);
            return -1;
        }
    }


}
