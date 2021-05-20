import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class Calculator {
    public Calculator(Notation defaultNotation) {
        this.defaultNotation = defaultNotation;
    }

    public Token[] Tokenize(String expression) {
        return tokenizer.Tokenize(expression, operationMap);
    }

    public BigDecimal Evaluate(String expression) {
        return Evaluate(expression, defaultNotation);
    }

    public BigDecimal Evaluate(String expression, @NotNull Notation notation) {
        return notation.Evaluate(Tokenize(expression));
    }

    private final Notation defaultNotation;
    private final Tokenizer tokenizer = new Tokenizer();
    private final Map<String, Operation> operationMap = new DefaultOperations().RegisterAll();
}
