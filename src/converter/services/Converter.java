package converter.services;

public interface Converter {

    String convertIntegers(int from, int to, String number);

    String convertFractions(int from, int to, String number, String fractionalPart);

}
