import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CLI implements Callable<Integer> {
    @Parameters(description = "expression to evaluate")
    private List<String> expressions = new ArrayList<>();

    @Option(names = {"-f", "--file"}, description = "name of file with expressions")
    private List<String> filenames = new ArrayList<>();

    @Option(names = {"--help", "-h"}, usageHelp = true, description = "display a help message")
    private boolean help = false;

    @Override
    public Integer call() {
        boolean success = true;
        Calculator calc = new Calculator(Notation.Infix);
        for (String expression : expressions) {
            try {
                System.out.println(calc.Evaluate(expression));
            } catch (Exception e) {
                success = false;
                System.out.println();
                System.err.printf("Exception %s occurred, message: %s %n", e.getClass().getName(), e.getMessage());
            }
        }
        StreamEvaluator evaluator = new StreamEvaluator(calc);
        for (String filename : filenames) {
            success &= evaluator.EvaluateFile(filename);
        }
        if (!success) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new CLI()).execute(args));
    }
}
