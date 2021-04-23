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

    public Token(@NotNull SpecialToken special) {
        this.special = Optional.of(special);
    }

    // mainly for testing
    public Token(int number) {
        this.number = Optional.of(new BigDecimal(number));
    }

    @Override
    public String toString() {
        String stringValue = "Unknown";
        if (number.isPresent()) {
            stringValue = number.get().toString();
        }
        if (operation.isPresent()) {
            stringValue = "operation";
        }
        if (special.isPresent()) {
            stringValue = special.toString();
        }
        return String.format("Token[%s]", stringValue);
    }

    @NotNull
    public Optional<BigDecimal> GetNumber() {
        return number;
    }

    @NotNull
    public Optional<Operation> GetOperation() {
        return operation;
    }

    @NotNull
    public Optional<SpecialToken> GetSpecial() {
        return special;
    }

    // maybe just use nullable pointers instead of optional?
    @NotNull
    private Optional<BigDecimal> number = Optional.empty();
    @NotNull
    private Optional<Operation> operation = Optional.empty();
    @NotNull
    private Optional<SpecialToken> special = Optional.empty();
}
