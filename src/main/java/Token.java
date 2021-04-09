import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Optional;

public class Token {
    public Token(@NotNull Operation operation) {
        this.operation = Optional.of(operation);
    }

    public Token(@NotNull BigDecimal number) {
        this.number = Optional.of(number);
    }

    // mainly for testing
    public Token(int number) {
        this.number = Optional.of(new BigDecimal(number));
    }

    @NotNull
    public Optional<BigDecimal> GetNumber() {
        return number;
    }

    @NotNull
    public Optional<Operation> GetOperation() {
        return operation;
    }

    // maybe just use nullable pointers instead of optional?
    @NotNull
    private Optional<BigDecimal> number = Optional.empty();
    @NotNull
    private Optional<Operation> operation = Optional.empty();
}
