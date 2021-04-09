import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class DefaultOperations {
    public Map<String, Operation> RegisterAll() {
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].add(args[1]), "+");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].subtract(args[1]), "-");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].multiply(args[1]), "*");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].divide(args[1]), "/");
        RegisterOperation(1, (OperationEvaluator) (args) -> args[0].abs(), "abs");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].divideToIntegralValue(args[1]), "//");
        RegisterOperation(1, (OperationEvaluator) (args) -> args[0].sqrt(MathContext.DECIMAL128), "sqrt");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].remainder(args[1]), "%");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].min(args[1]), "min");
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].max(args[1]), "max");
        return operations;
    }

    private interface OperationEvaluator {
        BigDecimal operation(BigDecimal[] expression);
    }

    private void RegisterOperation(Operation operation, String name) {
        if (operations.containsKey(name)) {
            throw new IllegalArgumentException("Operation with this name has already been registered");
        }
        operations.put(name, operation);
    }

    private void RegisterOperation(int argumentCount, OperationEvaluator evaluator, String name) {
        RegisterOperation(Evaluator2Operation(argumentCount, evaluator), name);
    }

    private void RegisterOperation(int argumentCount, OperationEvaluator evaluator, String[] names) {
        Operation operation = Evaluator2Operation(argumentCount, evaluator);
        for (String name: names) {
            RegisterOperation(operation, name);
        }
    }

    private Operation Evaluator2Operation(int argumentCount, OperationEvaluator evaluator) {
        return new Operation() {
            @Override
            public BigDecimal Evaluate(BigDecimal[] expression) {
                if (expression.length != GetArgumentCount()) {
                    throw new IllegalArgumentException("Wrong argument count");
                }
                return evaluator.operation(expression);
            }

            @Override
            public int GetArgumentCount() {
                return argumentCount;
            }
        };
    }

    private final Map<String, Operation> operations = new HashMap<>();
}
