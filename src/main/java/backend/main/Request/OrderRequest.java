package backend.main.Request;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    private Integer id;
    private String orderCode;
    private Integer customer;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private Integer voucherId;
    private BigDecimal voucherDiscount;
    private String shippingAddress;
    private String shippingMethod;
    private String trackingNumber;
    private String notes;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private List<OrderItemRequest> items;

    public OrderRequest() {
    }

    public OrderRequest(Integer id, String orderCode, Integer customer, String customerName, String customerPhone, String customerEmail, String customerAddress, String orderStatus, String paymentStatus, String paymentMethod, BigDecimal subtotalAmount, BigDecimal discountAmount, BigDecimal shippingFee, BigDecimal taxAmount, BigDecimal totalAmount, BigDecimal amountPaid, Integer voucherId, BigDecimal voucherDiscount, String shippingAddress, String shippingMethod, String trackingNumber, String notes, Integer createdBy, LocalDateTime createdAt, List<OrderItemRequest> items) {
        this.id = id;
        this.orderCode = orderCode;
        this.customer = customer;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.subtotalAmount = subtotalAmount;
        this.discountAmount = discountAmount;
        this.shippingFee = shippingFee;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.voucherId = voucherId;
        this.voucherDiscount = voucherDiscount;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.trackingNumber = trackingNumber;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderRequest [id=" + id + ", orderCode=" + orderCode + ", customer=" + customer + ", customerName="
                + customerName + ", customerPhone=" + customerPhone + ", customerEmail=" + customerEmail + ", customerAddress="
                + customerAddress + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", paymentMethod="
                + paymentMethod + ", subtotalAmount=" + subtotalAmount + ", discountAmount=" + discountAmount + ", shippingFee="
                + shippingFee + ", taxAmount=" + taxAmount + ", totalAmount=" + totalAmount + ", amountPaid=" + amountPaid + ", voucherId="
                + voucherId + ", voucherDiscount=" + voucherDiscount + ", shippingAddress=" + shippingAddress + ", shippingMethod="
                + shippingMethod + ", trackingNumber=" + trackingNumber + ", notes=" + notes + ", createdBy=" + createdBy + ", createdAt="
                + createdAt + ", items=" + items + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getCustomer() {      
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }       

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }   

    public BigDecimal getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(BigDecimal voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }   
}
