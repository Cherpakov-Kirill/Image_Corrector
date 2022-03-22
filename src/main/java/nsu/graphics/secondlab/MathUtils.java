package nsu.graphics.secondlab;

public class MathUtils {
    public static boolean isNumeric(String string) {
        double value;
        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            value = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Numeric type.");
        }
        return false;
    }
}
