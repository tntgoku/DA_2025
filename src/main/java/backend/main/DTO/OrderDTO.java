package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class OrderDTO {
    private Integer id;
    private String orderCode;
    private Integer customer;
    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String customerAddress;

    private String orderStatus = "pending"; // pending, confirmed, processing, shipped, delivered, cancelled, returned

    private String paymentStatus = "unpaid"; // unpaid, paid, partial, refunded

    private String paymentMethod;

    private BigDecimal subtotalAmount;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal shippingFee = BigDecimal.ZERO;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    private BigDecimal totalAmount;

    private BigDecimal amountPaid = BigDecimal.ZERO;

    private Integer voucherId;

    private BigDecimal voucherDiscount = BigDecimal.ZERO;

    private String shippingAddress;

    private String shippingMethod;

    private String trackingNumber;

    private String notes;

    private Integer createdBy;
    private LocalDateTime createdAt;

    List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderDTO(Integer id, String orderCode, Integer customer, String customerName, String customerPhone,
            String customerEmail, String customerAddress, String orderStatus, String paymentStatus,
            String paymentMethod, BigDecimal subtotalAmount, BigDecimal discountAmount, BigDecimal shippingFee,
            BigDecimal taxAmount, BigDecimal totalAmount, BigDecimal amountPaid, Integer voucherId,
            BigDecimal voucherDiscount, String shippingAddress, String shippingMethod, String trackingNumber,
            String notes, Integer createdBy, List<OrderItemDTO> listiem) {
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
        this.items = items;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return this.customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return this.customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getSubtotalAmount() {
        return this.subtotalAmount;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getShippingFee() {
        return this.shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return this.amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getVoucherId() {
        return this.voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public BigDecimal getVoucherDiscount() {
        return this.voucherDiscount;
    }

    public void setVoucherDiscount(BigDecimal voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public List<OrderItemDTO> getListiem() {
        return this.items;
    }

    public void setListiem(List<OrderItemDTO> items) {
        this.items = items;
    }

    public OrderDTO id(Integer id) {
        setId(id);
        return this;
    }

    public OrderDTO orderCode(String orderCode) {
        setOrderCode(orderCode);
        return this;
    }

    public OrderDTO customer(Integer customer) {
        setCustomer(customer);
        return this;
    }

    public OrderDTO customerName(String customerName) {
        setCustomerName(customerName);
        return this;
    }

    public OrderDTO customerPhone(String customerPhone) {
        setCustomerPhone(customerPhone);
        return this;
    }

    public OrderDTO customerEmail(String customerEmail) {
        setCustomerEmail(customerEmail);
        return this;
    }

    public OrderDTO customerAddress(String customerAddress) {
        setCustomerAddress(customerAddress);
        return this;
    }

    public OrderDTO orderStatus(String orderStatus) {
        setOrderStatus(orderStatus);
        return this;
    }

    public OrderDTO paymentStatus(String paymentStatus) {
        setPaymentStatus(paymentStatus);
        return this;
    }

    public OrderDTO paymentMethod(String paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }

    public OrderDTO subtotalAmount(BigDecimal subtotalAmount) {
        setSubtotalAmount(subtotalAmount);
        return this;
    }

    public OrderDTO discountAmount(BigDecimal discountAmount) {
        setDiscountAmount(discountAmount);
        return this;
    }

    public OrderDTO shippingFee(BigDecimal shippingFee) {
        setShippingFee(shippingFee);
        return this;
    }

    public OrderDTO taxAmount(BigDecimal taxAmount) {
        setTaxAmount(taxAmount);
        return this;
    }

    public OrderDTO totalAmount(BigDecimal totalAmount) {
        setTotalAmount(totalAmount);
        return this;
    }

    public OrderDTO amountPaid(BigDecimal amountPaid) {
        setAmountPaid(amountPaid);
        return this;
    }

    public OrderDTO voucherId(Integer voucherId) {
        setVoucherId(voucherId);
        return this;
    }

    public OrderDTO voucherDiscount(BigDecimal voucherDiscount) {
        setVoucherDiscount(voucherDiscount);
        return this;
    }

    public OrderDTO shippingAddress(String shippingAddress) {
        setShippingAddress(shippingAddress);
        return this;
    }

    public OrderDTO shippingMethod(String shippingMethod) {
        setShippingMethod(shippingMethod);
        return this;
    }

    public OrderDTO trackingNumber(String trackingNumber) {
        setTrackingNumber(trackingNumber);
        return this;
    }

    public OrderDTO notes(String notes) {
        setNotes(notes);
        return this;
    }

    public OrderDTO createdBy(Integer createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public OrderDTO listiem(List<OrderItemDTO> listiem) {
        setListiem(listiem);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderDTO)) {
            return false;
        }
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) && Objects.equals(orderCode, orderDTO.orderCode)
                && Objects.equals(customer, orderDTO.customer) && Objects.equals(customerName, orderDTO.customerName)
                && Objects.equals(customerPhone, orderDTO.customerPhone)
                && Objects.equals(customerEmail, orderDTO.customerEmail)
                && Objects.equals(customerAddress, orderDTO.customerAddress)
                && Objects.equals(orderStatus, orderDTO.orderStatus)
                && Objects.equals(paymentStatus, orderDTO.paymentStatus)
                && Objects.equals(paymentMethod, orderDTO.paymentMethod)
                && Objects.equals(subtotalAmount, orderDTO.subtotalAmount)
                && Objects.equals(discountAmount, orderDTO.discountAmount)
                && Objects.equals(shippingFee, orderDTO.shippingFee) && Objects.equals(taxAmount, orderDTO.taxAmount)
                && Objects.equals(totalAmount, orderDTO.totalAmount) && Objects.equals(amountPaid, orderDTO.amountPaid)
                && Objects.equals(voucherId, orderDTO.voucherId)
                && Objects.equals(voucherDiscount, orderDTO.voucherDiscount)
                && Objects.equals(shippingAddress, orderDTO.shippingAddress)
                && Objects.equals(shippingMethod, orderDTO.shippingMethod)
                && Objects.equals(trackingNumber, orderDTO.trackingNumber) && Objects.equals(notes, orderDTO.notes)
                && Objects.equals(createdBy, orderDTO.createdBy) && Objects.equals(items, orderDTO.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderCode, customer, customerName, customerPhone, customerEmail, customerAddress,
                orderStatus, paymentStatus, paymentMethod, subtotalAmount, discountAmount, shippingFee, taxAmount,
                totalAmount, amountPaid, voucherId, voucherDiscount, shippingAddress, shippingMethod, trackingNumber,
                notes, createdBy, items);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", orderCode='" + getOrderCode() + "'" +
                ", customer='" + getCustomer() + "'" +
                ", customerName='" + getCustomerName() + "'" +
                ", customerPhone='" + getCustomerPhone() + "'" +
                ", customerEmail='" + getCustomerEmail() + "'" +
                ", customerAddress='" + getCustomerAddress() + "'" +
                ", orderStatus='" + getOrderStatus() + "'" +
                ", paymentStatus='" + getPaymentStatus() + "'" +
                ", paymentMethod='" + getPaymentMethod() + "'" +
                ", subtotalAmount='" + getSubtotalAmount() + "'" +
                ", discountAmount='" + getDiscountAmount() + "'" +
                ", shippingFee='" + getShippingFee() + "'" +
                ", taxAmount='" + getTaxAmount() + "'" +
                ", totalAmount='" + getTotalAmount() + "'" +
                ", amountPaid='" + getAmountPaid() + "'" +
                ", voucherId='" + getVoucherId() + "'" +
                ", voucherDiscount='" + getVoucherDiscount() + "'" +
                ", shippingAddress='" + getShippingAddress() + "'" +
                ", shippingMethod='" + getShippingMethod() + "'" +
                ", trackingNumber='" + getTrackingNumber() + "'" +
                ", notes='" + getNotes() + "'" +
                ", createdBy='" + getCreatedBy() + "'" +
                ", listiem='" + getListiem() + "'" +
                "}";
    }

}
