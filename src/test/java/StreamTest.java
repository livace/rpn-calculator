import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StreamTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        outContent.reset();
        errContent.reset();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterAll
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void BasicFromFile() {
        StreamEvaluator evaluator = new StreamEvaluator(new Calculator(Notation.Infix));
        assertThat(evaluator.EvaluateFile(getClass().getResource("file_evaluator_test_infix.txt").getFile())).isFalse();
        assertThat(outContent.toString()).isEqualTo(String.join(System.lineSeparator(), new String[]{"2", "200", "", "", "", "", "", "-20000", ""}));
        outContent.reset();
        errContent.reset();
        assertThat(evaluator.EvaluateFile("some_non_existing_file_name_123")).isFalse();
        assertThat(outContent.size()).isEqualTo(0);
        assertThat(errContent.size()).isNotEqualTo(0);
    }
}
