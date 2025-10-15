package backend.main.Request;
import java.util.List;
public class OrderRequest {
    private Integer id;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentUrl;
    private String paymentId;
    private String paymentAmount;
    private String paymentCurrency;
    private String paymentTime;
    private List<OrderItemRequest> items;
}
