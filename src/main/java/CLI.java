import org.apache.commons.cli.*;

public class CLI {
    public static void main(String[] args) {
        Options options = new Options();
        Option expression_arg = new Option("e", "evaluate", true, "Expression to evaluate");
        // now this is only possible scenario
        expression_arg.setRequired(true);
        options.addOption(expression_arg);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("utility-name", options);

            System.exit(1);
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            System.exit(1);
        }

        String expression = cmd.getOptionValue(expression_arg.getOpt());
        Calculator calc = new Calculator(Notation.Postfix);
        System.out.println(calc.Evaluate(expression));
    }
}
