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
    private BigDecimal totalPrice;

    private List<ItemProductJson> items;

    public CheckoutRequest() {
    }

    public CheckoutRequest(Integer id, String email, String fullname, String phone, String province, String district,
            String address, String note, String paymentMethod, String voucher, BigDecimal totalPrice,
            List<ItemProductJson> items) {
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
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVoucher() {
        return this.voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ItemProductJson> getItems() {
        return this.items;
    }

    public void setItems(List<ItemProductJson> items) {
        this.items = items;
    }

    public CheckoutRequest id(Integer id) {
        setId(id);
        return this;
    }

    public CheckoutRequest email(String email) {
        setEmail(email);
        return this;
    }

    public CheckoutRequest fullname(String fullname) {
        setFullname(fullname);
        return this;
    }

    public CheckoutRequest phone(String phone) {
        setPhone(phone);
        return this;
    }

    public CheckoutRequest province(String province) {
        setProvince(province);
        return this;
    }

    public CheckoutRequest district(String district) {
        setDistrict(district);
        return this;
    }

    public CheckoutRequest address(String address) {
        setAddress(address);
        return this;
    }

    public CheckoutRequest note(String note) {
        setNote(note);
        return this;
    }

    public CheckoutRequest paymentMethod(String paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }

    public CheckoutRequest voucher(String voucher) {
        setVoucher(voucher);
        return this;
    }

    public CheckoutRequest totalPrice(BigDecimal totalPrice) {
        setTotalPrice(totalPrice);
        return this;
    }

    public CheckoutRequest items(List<ItemProductJson> items) {
        setItems(items);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CheckoutRequest)) {
            return false;
        }
        CheckoutRequest checkoutRequest = (CheckoutRequest) o;
        return Objects.equals(id, checkoutRequest.id) && Objects.equals(email, checkoutRequest.email)
                && Objects.equals(fullname, checkoutRequest.fullname) && Objects.equals(phone, checkoutRequest.phone)
                && Objects.equals(province, checkoutRequest.province)
                && Objects.equals(district, checkoutRequest.district)
                && Objects.equals(address, checkoutRequest.address) && Objects.equals(note, checkoutRequest.note)
                && Objects.equals(paymentMethod, checkoutRequest.paymentMethod)
                && Objects.equals(voucher, checkoutRequest.voucher)
                && Objects.equals(totalPrice, checkoutRequest.totalPrice)
                && Objects.equals(items, checkoutRequest.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, fullname, phone, province, district, address, note, paymentMethod, voucher,
                totalPrice, items);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", email='" + getEmail() + "'" +
                ", fullname='" + getFullname() + "'" +
                ", phone='" + getPhone() + "'" +
                ", province='" + getProvince() + "'" +
                ", district='" + getDistrict() + "'" +
                ", address='" + getAddress() + "'" +
                ", note='" + getNote() + "'" +
                ", paymentMethod='" + getPaymentMethod() + "'" +
                ", voucher='" + getVoucher() + "'" +
                ", totalPrice='" + getTotalPrice() + "'" +
                ", items='" + getItems() + "'" +
                "}";
    }

}
