import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class TransformerTest {
    Token[] Tokenize(String expression) {
        return new Tokenizer().Tokenize(expression, operations);
    }

    // other types of tests are checked in CalculatorTest
    @Test
    void ErrorTest() {
        Transformer transformer = Transformer.InfixToPostfix;
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> transformer.Transform(Tokenize("( 1 + 2")));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> transformer.Transform(Tokenize("1 + 2 )")));
    }

    final Map<String, Operation> operations = new DefaultOperations().RegisterAll();
}
