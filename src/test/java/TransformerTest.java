import org.junit.jupiter.api.Test;

import java.util.Arrays;
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

    @Test
    void PostfixToInfixTest() {
        Transformer transformer = Transformer.PostfixToInfix;
        var result = transformer.Transform(Tokenize("1 2 +"));
        var expected = Tokenize("2 + 1");
        System.out.println(Arrays.toString(result));
        System.out.println(Arrays.toString(expected));
        assertThat(Arrays.equals(result, expected));
    }

    final Map<String, Operation> operations = new DefaultOperations().RegisterAll();
}
