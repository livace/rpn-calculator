import java.io.*;

public class StreamEvaluator {
    public StreamEvaluator(Calculator calc) {
        this.calculator = calc;
    }

    boolean EvaluateStream(BufferedReader input) {
        boolean success = true;
        try {
            int lineNumber = 0;
            while (input.ready()) {
                lineNumber++;
                String line = input.readLine();
                try {
                    System.out.println(calculator.Evaluate(line));
                } catch (Exception e) {
                    success = false;
                    System.out.println();
                    System.err.printf("Exception %s occurred in line %d, message: %s %n", e.getClass().getName(), lineNumber, e.getMessage());
                }
            }
        } catch (IOException e) {
            success = false;
            System.err.printf("Got IOException: %s %n", e.getMessage());
        }
        return success;
    }

    boolean EvaluateFile(String filename) {
        try {
            return EvaluateStream(new BufferedReader(new FileReader(filename)));
        } catch (FileNotFoundException e) {
            System.err.printf("File %s not found %n", filename);
            return false;
        }
    }

    Calculator calculator;
}
