import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class Tokenizer {
    public Token[] Tokenize(String expression, Map<String, Operation> operations) {
        String[] splitted = expression.replace("(", " ( ").replace(")", " ) ").replaceFirst("^\\s+", "").split("\\s+");
        Token[] result = new Token[splitted.length];
        if (splitted.length == 0 || (splitted[0].equals("") && splitted.length == 1)) {
            return new Token[0];
        }
        for (int i = 0; i < splitted.length; i++) {
            if (splitted[i].equals("(")) {
                result[i] = new Token(SpecialToken.OpenBracket);
                continue;
            }
            boolean isClosingBracket = false;
            if (splitted[i].equals(")")) {
                result[i] = new Token(SpecialToken.CloseBracket);
                continue;
            }
            try {
                result[i] = new Token(new BigDecimal(splitted[i]));
            } catch (java.lang.NumberFormatException e) {
                Operation op = operations.get(splitted[i]);
                // I hate null pointers here
                if (op == null) {
                    throw new IllegalArgumentException("Unknown operation name '" + splitted[i] + "'");
                }
                result[i] = new Token(op, splitted[i]);
            }
        }
        return result;
    }
}
