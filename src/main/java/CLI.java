import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

public class CLI implements Callable<Integer> {
    @Parameters(description = "expression to evaluate")
    private List<String> expressions = new ArrayList<>();

    @Option(names = {"-f", "--file"}, description = "name of file with expressions")
    private List<String> filenames = new ArrayList<>();

    @Option(names = {"--help", "-h"}, usageHelp = true, description = "display a help message")
    private boolean help = false;

    @Option(names = {"-i", "--shell"}, description = "launch interactive shell")
    private boolean shell = false;

    @Option(names = {"--from"}, description = "input notation. One of 'infix'(default) and 'postfix'")
    private String input_notation = "infix";

    @Option(names = {"--mode"}, description = "Calculator mode. One of 'evaluate'(default) and 'transform'")
    private String mode = "evaluate";

    private Calculator calc = null;
    private TransformerInterface transformer = null;
    private final Tokenizer tokenizer = new Tokenizer();

    private void ValidateArgs() {
        if (!input_notation.equals("infix") && !input_notation.equals("postfix")) {
            throw new IllegalArgumentException("Input notation must be one of 'infix' and 'postfix'");
        }
        if (!mode.equals("evaluate") && !mode.equals("transform")) {
            throw new IllegalArgumentException("Mode must be one of 'evaluate' and 'transform'");
        }
    }

    private boolean calculateAndPrintAnswer(String expression) {
        if (mode.equals("evaluate")) {
            try {
                System.out.println(getCalculator().Evaluate(expression));
                return true;
            } catch (Exception e) {
                System.out.println();
                System.err.printf("Exception %s occurred, message: %s %n", e.getClass().getName(), e.getMessage());
                return false;
            }
        } else if (mode.equals("transform")) {
            try {
                StringJoiner builder = new StringJoiner(" ");
                for (var token : getTransformer().Transform(getCalculator().Tokenize(expression))) {
                    builder.add(token.printableString());
                }
                System.out.println(builder);
                return true;
            } catch (Exception e) {
                System.out.println();
                System.err.printf("Exception %s occurred, message: %s %n", e.getClass().getName(), e.getMessage());
                return false;
            }
        } else {
            throw new IllegalArgumentException("Unknown mode " + mode);
        }
    }

    private Calculator getCalculator() {
        if (calc != null) {
            return calc;
        }
        if (input_notation.equals("infix")) {
            calc = new Calculator(Notation.Infix);
            return calc;
        } else if (input_notation.equals("postfix")) {
            calc = new Calculator(Notation.Postfix);
            return calc;
        } else {
            throw new IllegalArgumentException("unknown notation "+ input_notation);
        }
    }

    private TransformerInterface getTransformer() {
        if (transformer != null) {
            return transformer;
        }
        if (input_notation.equals("infix")) {
            transformer = Transformer.InfixToPostfix;
            return transformer;
        } else if (input_notation.equals("postfix")) {
            transformer = Transformer.PostfixToInfix;
            return transformer;
        } else {
            throw new IllegalArgumentException("unknown notation "+ input_notation);
        }
    }

    private void launchShell() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        Calculator calc = getCalculator();
        while (true) {
            System.out.print(">");
            System.out.flush();
            String line = reader.readLine();
            if (line.equals("exit")) {
                break;
            }
            calculateAndPrintAnswer(line);
        }
    }

    @Override
    public Integer call() {
        ValidateArgs();

        boolean success = true;
        Calculator calc = getCalculator();
        for (String expression : expressions) {
            success &= calculateAndPrintAnswer(expression);
        }
        StreamEvaluator evaluator = new StreamEvaluator(getCalculator());
        for (String filename : filenames) {
            success &= evaluator.EvaluateFile(filename);
        }

        if (shell) {
            try {
                launchShell();
            } catch (IOException ex) {
                System.err.printf("Get exception %s", ex.getMessage());
                return 1;
            }
            return 0;
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
