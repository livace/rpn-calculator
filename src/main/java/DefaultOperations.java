import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class DefaultOperations {
    public Map<String, Operation> RegisterAll() {
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].add(args[1]), "+", 0);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].subtract(args[1]), "-", 0);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].multiply(args[1]), "*", 1);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].divide(args[1], MathContext.DECIMAL128), "/", 1);
        RegisterOperation(1, (OperationEvaluator) (args) -> args[0].abs(), "abs", 2);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].divideToIntegralValue(args[1]), "//", 1);
        RegisterOperation(1, (OperationEvaluator) (args) -> args[0].sqrt(MathContext.DECIMAL128), "sqrt", 2);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].remainder(args[1]), "%", 1);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].min(args[1]), "min", 2);
        RegisterOperation(2, (OperationEvaluator) (args) -> args[0].max(args[1]), "max", 2);
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

    private void RegisterOperation(int argumentCount, OperationEvaluator evaluator, String name, int priority) {
        RegisterOperation(Evaluator2Operation(argumentCount, evaluator, priority), name);
    }

    private Operation Evaluator2Operation(int argumentCount, OperationEvaluator evaluator, int priority) {
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

            @Override
            public int GetPriority() {
                return priority;
            }
        };
    }

    private final Map<String, Operation> operations = new HashMap<>();
}
