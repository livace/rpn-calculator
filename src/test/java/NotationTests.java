import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class NotationTests {
    Token[] Tokenize(String expression) {
        return new Tokenizer().Tokenize(expression, operations);
    }

    @Test
    void PostfixBasicTest() {
        assertThat(Notation.Postfix.Evaluate(Tokenize("1 2 +"))).isEqualTo("3");
        assertThat(Notation.Postfix.Evaluate(Tokenize("1 2 -"))).isEqualTo("-1");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 0 *"))).isEqualTo("0");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 0 max"))).isEqualTo("100");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 0 min"))).isEqualTo("0");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 sqrt"))).isEqualTo("10");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 3 //"))).isEqualTo("33");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 3 %"))).isEqualTo("1");
        assertThat(Notation.Postfix.Evaluate(Tokenize("100 abs"))).isEqualTo("100");
        assertThat(Notation.Postfix.Evaluate(Tokenize("-100 abs"))).isEqualTo("100");
    }

    @Test
    void PostfixErrorTest() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("1 +")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("1 2 3 +")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("1 0 %")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("1 0 /")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("1 0 //")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("-1 sqrt")));
    }

    final Map<String, Operation> operations = new DefaultOperations().RegisterAll();
}
