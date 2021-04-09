import java.math.BigDecimal;
import java.util.Stack;

public enum Notation implements NotationInterface {
    Postfix {
        @Override
        public BigDecimal Evaluate(Token[] expression) {
            // not actually stack because be need to copy last elements in same order
            Stack<BigDecimal> stack = new Stack<>();
            for (Token token : expression) {
                var number = token.GetNumber();
                if (number.isPresent()) {
                    stack.add(number.get());
                    continue;
                }
                Operation operation = token.GetOperation().orElseThrow(() -> new IllegalArgumentException("Unexpected token type"));
                if (stack.size() < operation.GetArgumentCount()) {
                    throw new RuntimeException("Stack underflow");
                }
                BigDecimal[] args = new BigDecimal[operation.GetArgumentCount()];
                for (int i = args.length - 1; i >= 0; i--) {
                    args[i] = stack.pop();
                }
                stack.push(operation.Evaluate(args));
            }
            if (stack.size() != 1) {
                throw new RuntimeException("Syntax error: stack length after evaluating is not 1");
            }
            return stack.pop();
        }
    }
}
