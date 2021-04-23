import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CLI {
    @Parameter(names = {"-e", "--expression"}, description = "expression to evaluate")
    private List<String> expressions = new ArrayList<>();

    @Parameter(names = {"-f", "--file"}, description = "name of file with expressions")
    private List<String> filenames = new ArrayList<>();

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help = false;

    public static void main(String[] args) {
        CLI main = new CLI();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();
        try {
            jc.parse(args);
        } catch (Exception e) {
            jc.usage();
            System.exit(1);
        }
        if (main.help) {
            jc.usage();
            return;
        }
        main.run();
    }

    void run() {
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
            System.exit(1);
        }
    }
}
