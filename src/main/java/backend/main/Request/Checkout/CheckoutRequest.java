package backend.main.Request.Checkout;

import java.math.BigDecimal;
import java.util.List;

import backend.main.Request.Json.ItemProductJson;
import java.util.Objects;

public class CheckoutRequest {
    private Integer id;
    private String email;
    private String fullname;
    private String phone;
    private String province;
    private String district;
    private String address;
    private String note;
    private String paymentMethod;
    private String voucher;
    private BigDecimal shippingFee;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal voucherDiscount;
    private BigDecimal subtotalAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalPrice;

    private List<ItemProductJson> items;
    public CheckoutRequest(){
        
    }
    @Override
    public String toString() {
        return "CheckoutRequest [id=" + id + ", email=" + email + ", fullname=" + fullname + ", phone=" + phone
                + ", province=" + province + ", district=" + district + ", address=" + address + ", note=" + note
                + ", paymentMethod=" + paymentMethod + ", voucher=" + voucher + ", shippingFee=" + shippingFee
                + ", taxAmount=" + taxAmount + ", discountAmount=" + discountAmount + ", voucherDiscount="
                + voucherDiscount + ", subtotalAmount=" + subtotalAmount + ", totalAmount=" + totalAmount
                + ", totalPrice=" + totalPrice + ", items=" + items + "]";
    }
    public CheckoutRequest(Integer id, String email, String fullname, String phone, String province, String district,
            String address, String note, String paymentMethod, String voucher, BigDecimal shippingFee,
            BigDecimal taxAmount, BigDecimal discountAmount, BigDecimal voucherDiscount, BigDecimal subtotalAmount,
            BigDecimal totalAmount, BigDecimal totalPrice, List<ItemProductJson> items) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.province = province;
        this.district = district;
        this.address = address;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.voucher = voucher;
        this.shippingFee = shippingFee;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.voucherDiscount = voucherDiscount;
        this.subtotalAmount = subtotalAmount;
        this.totalAmount = totalAmount;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(BigDecimal voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public BigDecimal getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ItemProductJson> getItems() {
        return items;
    }

    public void setItems(List<ItemProductJson> items) {
        this.items = items;
    }

}
