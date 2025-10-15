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
    private Integer voucher; // incoming may be number
    private BigDecimal totalPrice;
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
}


