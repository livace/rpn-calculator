import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class TokenTest {
    void TestOne(int n) {
        Token token = new Token(n);
        assertThat(token.GetOperation().isEmpty()).isTrue();
        assertThat(token.GetNumber().isPresent()).isTrue();
        assertThat(token.GetNumber().get()).isEqualTo(String.valueOf(n));
    }

    void TestOne(BigDecimal n) {
        Token token = new Token(n);
        assertThat(token.GetOperation().isEmpty()).isTrue();
        assertThat(token.GetNumber().isPresent()).isTrue();
        assertThat(token.GetNumber().get()).isEqualTo(n.toString());
    }

    @Test
    void Integers() {
        for (int i = -10; i <= 10; ++i) {
            TestOne(i);
        }
        TestOne(-1000000000);
        TestOne(1000000000);
        TestOne(Integer.MAX_VALUE);
        TestOne(Integer.MIN_VALUE);
    }

    @Test
    void BigDecimals() {
        for (BigDecimal i = new BigDecimal(-10); i.compareTo(new BigDecimal(10)) <= 0; i = i.add(new BigDecimal("0.5"))) {
            TestOne(i);
        }
        TestOne(new BigDecimal(-1000000000));
        TestOne(new BigDecimal(1000000000));
        TestOne(new BigDecimal(Integer.MAX_VALUE));
        TestOne(new BigDecimal(Integer.MIN_VALUE));
        TestOne(new BigDecimal(0.123));
        TestOne(new BigDecimal(0));
        TestOne(new BigDecimal(Math.PI));
        TestOne(new BigDecimal(-Math.PI));
    }

    @Test
    void Operation() {
        Operation op = new Operation() {
            @Override
            public BigDecimal Evaluate(BigDecimal[] expression) {
                return null;
            }

            @Override
            public int GetArgumentCount() {
                return 0;
            }

            @Override
            public int GetPriority() {
                return 0;
            }
        };
        Token token = new Token(op);
        assertThat(token.GetNumber().isEmpty()).isTrue();
        assertThat(token.GetOperation().isPresent()).isTrue();
        assertThat(token.GetOperation().get()).isEqualTo(op);
    }
}
