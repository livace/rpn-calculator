import java.math.BigDecimal;

public interface Operation {
    BigDecimal Evaluate(BigDecimal[] expression);
    int GetArgumentCount();
}
