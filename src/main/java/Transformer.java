import java.util.Optional;
import java.util.Stack;

public enum Transformer implements TransformerInterface {
    InfixToPostfix {
        @Override
        public Token[] Transform(Token[] sequence) {
            Stack<Token> result = new Stack<>();
            Stack<Token> queue = new Stack<>();
            for (Token token : sequence) {
                token.GetNumber().ifPresent(number -> result.push(token));
                token.GetSpecial().ifPresent(special -> {
                    switch (special) {
                        case OpenBracket -> queue.push(token);
                        case CloseBracket -> {
                            boolean foundPair = false;
                            while (!queue.empty()) {
                                Token current = queue.pop();
                                if (current.GetSpecial().isPresent() && current.GetSpecial().get() == SpecialToken.OpenBracket) {
                                    foundPair = true;
                                    break;
                                }
                                result.push(current);
                            }
                            if (!foundPair) {
                                throw new IllegalArgumentException("Unmatched bracket: missing opening bracket");
                            }
                        }
                    }
                });
                token.GetOperation().ifPresent(operation -> {
                    while (!queue.empty()) {
                        Optional<Operation> topMaybe = queue.peek().GetOperation();
                        if (topMaybe.isEmpty()) {
                            break;
                        }
                        Operation top = topMaybe.get();
                        if (top.GetPriority() < operation.GetPriority()) {
                            break;
                        }
                        result.push(queue.pop());
                    }
                    queue.push(token);
                });
            }
            while (!queue.empty()) {
                if (queue.peek().GetSpecial().isPresent()) {
                    throw new IllegalArgumentException("Unmatched bracket: missing closing bracket");
                }
                result.push(queue.pop());
            }
            return result.toArray(new Token[0]);
        }
    },
}
