import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Optional;

public class Token {
    public Token(@NotNull Operation operation, @NotNull String name) {
        this.operation = Optional.of(operation);
        this.name = name;
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
        return String.format("Token[%s]", printableString());
    }

    public String printableString() {
        if (number.isPresent()) {
            return number.get().toString();
        }
        if (operation.isPresent()) {
            return name;
        }
        if (special.isPresent()) {
            if (special.get() == SpecialToken.OpenBracket) {
                return "(";
            }
            if (special.get() == SpecialToken.CloseBracket) {
                return ")";
            }
        }
        return "Unknown";
    }

    @NotNull
    public Optional<BigDecimal> GetNumber() {
        return number;
    }

    @NotNull
    public Optional<Operation> GetOperation() {
        return operation;
    }
    // can be called only on operation
    public String GetName() {
        return name;
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
    private String name = "";
}
