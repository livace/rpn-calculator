import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;


public class TokenizerTest {
    boolean IsOperation(Token token) {
        assertThat(token.GetNumber().isPresent() && token.GetOperation().isPresent()).isFalse();
        return token.GetOperation().isPresent();
    }

    Token[] Tokenize(String expression) {
        return new Tokenizer().Tokenize(expression, operations);
    }

    void assertNum(Token token, String num) {
        assertThat(IsOperation(token)).isFalse();
        assertThat(token.GetNumber().isPresent()).isTrue();
        assertThat(token.GetNumber().get()).isEqualTo(num);
    }

    void assertOperation(Token token, String name) {
        assertThat(token.GetNumber().isEmpty()).isTrue();
        assertThat(token.GetOperation().isPresent()).isTrue();
        assertThat(token.GetOperation().get()).isEqualTo(operations.get(name));
    }

    @Test
    void BasicTest() {
        Token[] tokens = Tokenize("1 -2 -3 4 1.234 + - 0 -100 // max 100500100500100500");
        assertThat(tokens.length).isEqualTo(12);
        assertNum(tokens[0], "1");
        assertNum(tokens[1], "-2");
        assertNum(tokens[2], "-3");
        assertNum(tokens[3], "4");
        assertNum(tokens[4], "1.234");
        assertThat(IsOperation(tokens[5])).isTrue();
        assertThat(IsOperation(tokens[6])).isTrue();
        assertNum(tokens[7], "0");
        assertNum(tokens[8], "-100");
        assertThat(IsOperation(tokens[9])).isTrue();
        assertThat(IsOperation(tokens[10])).isTrue();
        assertNum(tokens[11], "100500100500100500");
    }

    @Test
    void UnknownOperationTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> Tokenize("sin"));
    }

    @Test
    void EmptyStringTest() {
        assertThat(Tokenize("").length).isEqualTo(0);
    }

    final Map<String, Operation> operations = new DefaultOperations().RegisterAll();
}
