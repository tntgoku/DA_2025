package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class OrderProductSummaryDTO {
    LocalDate date;
    String productName;
    Integer totalQuantity;
    BigDecimal totalAmount;
}
