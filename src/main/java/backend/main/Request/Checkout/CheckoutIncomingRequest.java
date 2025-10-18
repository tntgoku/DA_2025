package backend.main.Request.Checkout;

import java.math.BigDecimal;
import java.util.List;

import backend.main.Request.Json.ItemProductJson;

public class CheckoutIncomingRequest {
    private Integer id;
    private String email;
    private String fullname;
    private String phone;
    private String province;
    private String district;
    private String address;
    private String note;
    private String paymentMethod;
    private String shippingAddress;
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    private Integer voucher; 
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal voucherDiscount;
    private List<ItemProductJson> items;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Integer getVoucher() { return voucher; }
    public void setVoucher(Integer voucher) { this.voucher = voucher; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public List<ItemProductJson> getItems() { return items; }
    public void setItems(List<ItemProductJson> items) { this.items = items; }


    
    @Override
    public String toString() {
        return "CheckoutIncomingRequest [id=" + id + ", email=" + email + ", fullname=" + fullname + ", phone=" + phone
                + ", province=" + province + ", district=" + district + ", address=" + address + ", note=" + note
                + ", paymentMethod=" + paymentMethod + ", voucher=" + voucher + ", totalPrice=" + totalPrice
                + ", shippingFee=" + shippingFee + ", taxAmount=" + taxAmount + ", discountAmount=" + discountAmount
                + ", voucherDiscount=" + voucherDiscount + ", items=" + items + "]";
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
    
}


