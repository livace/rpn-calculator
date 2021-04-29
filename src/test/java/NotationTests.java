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
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Notation.Postfix.Evaluate(Tokenize("(-1)")));
    }

    @Test
    void InfixBasicTest() {
        assertThat(Notation.Infix.Evaluate(Tokenize("1 + 2"))).isEqualTo("3");
        assertThat(Notation.Infix.Evaluate(Tokenize("1 - 2"))).isEqualTo("-1");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 * 0"))).isEqualTo("0");
        assertThat(Notation.Infix.Evaluate(Tokenize("max(100 0)"))).isEqualTo("100");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 min 0"))).isEqualTo("0");
        assertThat(Notation.Infix.Evaluate(Tokenize("sqrt 100"))).isEqualTo("10");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 // 3"))).isEqualTo("33");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 // 3 - 1"))).isEqualTo("32");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 // (3 - 1)"))).isEqualTo("50");
        assertThat(Notation.Infix.Evaluate(Tokenize("100 % 3"))).isEqualTo("1");
        assertThat(Notation.Infix.Evaluate(Tokenize("abs 100"))).isEqualTo("100");
        assertThat(Notation.Infix.Evaluate(Tokenize("abs(-100)"))).isEqualTo("100");
    }

    @Test
    void InfixErrorTest() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("1 +")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("1 + 2 3")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("1 % 0")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("1 / 0")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("1 // 0")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("")));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> Notation.Infix.Evaluate(Tokenize("sqrt -1")));
    }

    final Map<String, Operation> operations = new DefaultOperations().RegisterAll();
}
