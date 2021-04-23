import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CalculatorTest {
    @Test
    void PostfixBasicTest() {
        Calculator calc = new Calculator(Notation.Postfix);
        assertThat(calc.Evaluate("1 2 +")).isEqualTo("3");
        assertThat(calc.Evaluate("1 2 -")).isEqualTo("-1");
        assertThat(calc.Evaluate("100 0 *")).isEqualTo("0");
        assertThat(calc.Evaluate("100 0 max")).isEqualTo("100");
        assertThat(calc.Evaluate("100 0 min")).isEqualTo("0");
        assertThat(calc.Evaluate("100 sqrt")).isEqualTo("10");
        assertThat(calc.Evaluate("100 3 //")).isEqualTo("33");
        assertThat(calc.Evaluate("100 3 %")).isEqualTo("1");
        assertThat(calc.Evaluate("100 abs")).isEqualTo("100");
        assertThat(calc.Evaluate("-100 abs")).isEqualTo("100");

        assertThat(calc.Evaluate("1 2 +", Notation.Postfix)).isEqualTo("3");
        assertThat(calc.Evaluate("1 2 -", Notation.Postfix)).isEqualTo("-1");
        assertThat(calc.Evaluate("100 0 *", Notation.Postfix)).isEqualTo("0");
        assertThat(calc.Evaluate("100 0 max", Notation.Postfix)).isEqualTo("100");
        assertThat(calc.Evaluate("100 0 min", Notation.Postfix)).isEqualTo("0");
        assertThat(calc.Evaluate("100 sqrt", Notation.Postfix)).isEqualTo("10");
        assertThat(calc.Evaluate("100 3 //", Notation.Postfix)).isEqualTo("33");
        assertThat(calc.Evaluate("100 3 %", Notation.Postfix)).isEqualTo("1");
        assertThat(calc.Evaluate("100 abs", Notation.Postfix)).isEqualTo("100");
        assertThat(calc.Evaluate("-100 abs", Notation.Postfix)).isEqualTo("100");
    }

    @Test
    void PostfixErrorTest() {
        Calculator calc = new Calculator(Notation.Postfix);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 +", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 2 3 +", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 %", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 /", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 //", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("", Notation.Postfix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("-1 sqrt", Notation.Postfix));
    }

    @Test
    void InfixBasicTest() {
        Calculator calc = new Calculator(Notation.Infix);
        assertThat(calc.Evaluate("1 + 2")).isEqualTo("3");
        assertThat(calc.Evaluate("1 - 2")).isEqualTo("-1");
        assertThat(calc.Evaluate("100 * 0")).isEqualTo("0");
        assertThat(calc.Evaluate("max(100 0)")).isEqualTo("100");
        assertThat(calc.Evaluate("100 min 0")).isEqualTo("0");
        assertThat(calc.Evaluate("sqrt 100")).isEqualTo("10");
        assertThat(calc.Evaluate("100 // 3")).isEqualTo("33");
        assertThat(calc.Evaluate("100 % 3")).isEqualTo("1");
        assertThat(calc.Evaluate("abs 100")).isEqualTo("100");
        assertThat(calc.Evaluate("abs(-100)")).isEqualTo("100");
        assertThat(calc.Evaluate("99 + 1 % 3")).isEqualTo("100");
        assertThat(calc.Evaluate("(99 + 1) % 3")).isEqualTo("1");

        assertThat(calc.Evaluate("1 + 2", Notation.Infix)).isEqualTo("3");
        assertThat(calc.Evaluate("1 - 2", Notation.Infix)).isEqualTo("-1");
        assertThat(calc.Evaluate("100 * 0", Notation.Infix)).isEqualTo("0");
        assertThat(calc.Evaluate("100 max 0", Notation.Infix)).isEqualTo("100");
        assertThat(calc.Evaluate("min(100 0)", Notation.Infix)).isEqualTo("0");
        assertThat(calc.Evaluate("sqrt(100)", Notation.Infix)).isEqualTo("10");
        assertThat(calc.Evaluate("100 // 3", Notation.Infix)).isEqualTo("33");
        assertThat(calc.Evaluate("100 % 3", Notation.Infix)).isEqualTo("1");
        assertThat(calc.Evaluate("abs 100", Notation.Infix)).isEqualTo("100");
        assertThat(calc.Evaluate("abs -100", Notation.Infix)).isEqualTo("100");
    }

    @Test
    void InfixErrorTest() {
        Calculator calc = new Calculator(Notation.Infix);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 +", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 2 3 +", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 %", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 /", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("1 0 //", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("", Notation.Infix));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> calc.Evaluate("-1 sqrt", Notation.Infix));
    }
}
