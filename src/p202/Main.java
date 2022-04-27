package p202;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            int numerator = Integer.parseInt(split[0]);
            int denominator = Integer.parseInt(split[1]);

            compute(numerator, denominator);
        }
    }

    private static void compute(int numerator, int denominator) {

        int remainder = (numerator % denominator) * 10;
        Map<Integer, Integer> seenRemainders = new HashMap<>();
        List<String> decimals = new ArrayList<>();
        int startRepeatingDecimals = -1;
        int length = 0;

        while (remainder != 0) {
            if (seenRemainders.containsKey(remainder)) {
                startRepeatingDecimals = seenRemainders.get(remainder);
                length = decimals.size() - startRepeatingDecimals;
                break;
            }

            seenRemainders.put(remainder, decimals.size());
            decimals.add((remainder / denominator) + "");
            remainder = (remainder % denominator) * 10;
        }

        System.out.print(numerator + "/" + denominator + " = " + numerator / denominator + ".");
        for (int i = 0; i < decimals.size(); i++) {
            if (i >= 50) {
                System.out.print("...");
                break;
            }
            if (i == startRepeatingDecimals) {
                System.out.print("(");
            }
            System.out.print(decimals.get(i));
        }

        if (startRepeatingDecimals != -1) {
            System.out.println(")");
        } else {
            System.out.println("(0)");
            length = 1;
        }

        System.out.println("   " + length + " = number of digits in repeating cycle\n");
    }

}
