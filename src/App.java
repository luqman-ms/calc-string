public class App {
    public static void main(String[] args) throws Exception {
        String sum = "( 10 + 2 ) - ( 2 + 3 * ( 7 - 5 ) )";
        System.out.println("Test input: " + sum);
        System.out.println("Actual result: " + Double.valueOf(( 10 + 2 ) - ( 2 + 3 * ( 7 - 5 ))));
        System.out.println("My result: " + calculate(sum));
    }

    public static double calculate(String sum) {
        // split string into array separated by space
        String[] array = sum.split(" ");
        return process(array);
    }

    public static double process(String[] array) {

        int arrLength = array.length;

        // allow doCalculation() for array.length == 3
        if (arrLength == 3) { 
            return doCalculation(array); 

        // allow doCalculation() for array.length == 5 with enclosing bracket
        // } else if (arrLength == 5 && array[0].equals("(") && array[array.length - 1].equals(")")) {
        //     // trim bracket
        //     array = pullFromArray(array, 1, array.length - 2);
        //     return doCalculation(array);

        } else if (arrLength > 3) {

            // ::: 1st PRIORITY :::
            // perform process() inside bracket
            int indexStartBracket = -1;
            int indexEndBracket = -1;
            for (int i = 0; i < arrLength; i++) {
                if (array[i].equals("(")) // look for left bracket (and nested left bracket if found)
                    indexStartBracket = i;

                if (indexStartBracket != -1 && array[i].equals(")")) { // look for right bracket (first time found will always as nested right bracket)
                    indexEndBracket = i;
                    double result = process(pullFromArray(array, indexStartBracket, indexEndBracket));
                    array = insertIntoArray(array, String.valueOf(result), indexStartBracket, indexEndBracket);
                    return process(array);
                }
            }
            // ::: 2nd PRIORITY :::
            // perform process() for priority operator (multiply(*) or divide(/))
            for (int i = 1; i < arrLength; i++) {
                if ((array[i].equals("*") || array[i].equals("/")) && !array[i + 1].equals("(")) {
                    double result = process(pullFromArray(array, i - 1, i + 1));
                    array = insertIntoArray(array, String.valueOf(result), i - 1, i + 1);
                    return process(array);
                }
            }
            // ::: NO PRIORITY :::
            // perform process() for left hand side
            double result = process(pullFromArray(array, 0, 2));
            array = insertIntoArray(array, String.valueOf(result), 0, 2);
            return process(array);
        } else {
            return 0.0;
        }
    }

    private static String[] pullFromArray(String[] array, int indexStart, int indexEnd) {
        indexEnd += 1; // add 1 offset into indexEnd
        String[] newArray = new String[indexEnd - indexStart];
        int j = 0;
        for (int i = indexStart; i < indexEnd; i++) {
            newArray[j++] = array[i]; // store new item according to indexStart and indexEnd
        }
        // trim bracket if found
        if (newArray[0].equals("(") && newArray[newArray.length - 1].equals(")")) {
            newArray = pullFromArray(newArray, 1, newArray.length - 2);
        }
        return newArray;
    }

    private static String[] insertIntoArray(String[] array, String insertItem, int indexStart, int indexEnd) {
        String[] newArray = new String[array.length - (indexEnd - indexStart)];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (i == indexStart)
                newArray[j++] = insertItem; // store insertItem into array
            else if (i < indexStart || i > indexEnd)
                newArray[j++] = array[i]; // reconstruct array skip from indexStart to indexEnd
        }
        return newArray;
    }

    private static double doCalculation(String[] array) {
        double result = 0.0;
        if (array[1].equals("+")) {
            result = Double.valueOf(Double.valueOf(array[0]) + Double.valueOf(array[2]));

        } else if (array[1].equals("-")) {
            result = Double.valueOf(Double.valueOf(array[0]) - Double.valueOf(array[2]));

        } else if (array[1].equals("*")) {
            result = Double.valueOf(Double.valueOf(array[0]) * Double.valueOf(array[2]));

        } else if (array[1].equals("/")) {
            result = Double.valueOf(Double.valueOf(array[0]) / Double.valueOf(array[2]));
        }
        System.out.println("Current operation: " + array[0] + array[1] + array[2] + " = " + result);
        return result;
    }
}
