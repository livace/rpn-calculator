import java.math.BigDecimal;
import java.util.*;

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
    PostfixToInfix {
        class InfixExpressionPart {
            public InfixExpressionPart() {
                this.tokens = new LinkedList<>();
                this.minimalPriority = Integer.MAX_VALUE;
            }

            public InfixExpressionPart(BigDecimal number) {
                this.tokens = new LinkedList<>();
                this.tokens.add(new Token(number));
                this.minimalPriority = Integer.MAX_VALUE;
            }

            public void AddBrackets() {
                List<Token> newPart = new LinkedList<>();
                newPart.add(new Token(SpecialToken.OpenBracket));
                newPart.addAll(this.tokens);
                newPart.add(new Token(SpecialToken.CloseBracket));
                this.tokens = newPart;
                this.minimalPriority = Integer.MAX_VALUE;
            }

            public void AddToken(Token token) {
                if (token.GetOperation().isPresent()) {
                    this.minimalPriority = Math.min(
                            this.minimalPriority,
                            token.GetOperation().get().GetPriority()
                    );
                }
                this.tokens.add(token);
            }

            public void Concat(InfixExpressionPart rhs) {
                this.tokens.addAll(rhs.tokens);
                this.minimalPriority = Math.min(this.minimalPriority, rhs.minimalPriority);
            }

            public int GetMinimalPriority() {
                return this.minimalPriority;
            }

            public List<Token> GetTokens() {
                return this.tokens;
            }

            private List<Token> tokens;
            private int minimalPriority;
        };

        @Override
        public Token[] Transform(Token[] sequence) {
            Stack<InfixExpressionPart> stack = new Stack<>();
            for (Token token : sequence) {
                var number = token.GetNumber();
                if (number.isPresent()) {
                    stack.add(new InfixExpressionPart(number.get()));
                    continue;
                }

                Operation operation = token.GetOperation().orElseThrow(() -> new IllegalArgumentException("Unexpected token type"));
                if (stack.size() < operation.GetArgumentCount()) {
                    throw new RuntimeException("Stack underflow");
                }

                InfixExpressionPart newPart = new InfixExpressionPart();
                switch (operation.GetArgumentCount()) {
                    case 1 -> {
                        newPart.AddToken(token);

                        var arg = stack.pop();
                        if (arg.GetMinimalPriority() < operation.GetPriority()) {
                            arg.AddBrackets();
                        }
                        newPart.Concat(arg);
                    }
                    case 2 -> {
                        var first = stack.pop();
                        if (first.GetMinimalPriority() < operation.GetPriority()) {
                            first.AddBrackets();
                        }
                        newPart.Concat(first);

                        newPart.AddToken(token);

                        var second = stack.pop();
                        if (second.GetMinimalPriority() < operation.GetPriority()) {
                            second.AddBrackets();
                        }
                        newPart.Concat(second);
                    }
                }
                stack.add(newPart);
            }
            if (stack.size() != 1) {
                throw new RuntimeException("Syntax error: stack length after evaluating is not 1");
            }
            return stack.pop().GetTokens().toArray(new Token[0]);
        }
    }
}
