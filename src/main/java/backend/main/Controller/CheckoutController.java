package backend.main.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Voucher;
import backend.main.Request.Checkout.CheckoutRequest;
import backend.main.Request.Checkout.CheckoutIncomingRequest;
import backend.main.Request.Json.ItemProductJson;
import backend.main.Repository.VoucherRepository;
import backend.main.Service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private VoucherRepository voucherRepository;
    private static final org.slf4j.Logger logger = LoggerE.getLogger();

    @PostMapping
    public ResponseEntity<ResponseObject> checkout(@RequestBody CheckoutIncomingRequest incoming,
            HttpServletRequest httpReq) {
        logger.info("Checkout incoming {}", (incoming == null ? "null" : incoming.getEmail()));
        if (incoming == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Dữ liệu không hợp lệ", 0, null),
                    HttpStatus.BAD_REQUEST);
        }

        CheckoutRequest body = new CheckoutRequest();
        // OrderRequest body = new OrderRequest();
        // Chỉ set id nếu không null để tránh lỗi
        if (incoming.getId() != null) {
            body.setId(incoming.getId());
        }
        body.setEmail(incoming.getEmail());
        body.setFullname(incoming.getFullname());
        body.setPhone(incoming.getPhone());
        body.setProvince(incoming.getProvince());
        body.setDistrict(incoming.getDistrict());
        logger.debug("Here", body);
        String codeaddress = "Mã Tỉnh: " + incoming.getProvince() + ", Xã,Phường: " + incoming.getDistrict();
        body.setAddress(incoming.getAddress());
        body.setNote(incoming.getNote());
        body.setPaymentMethod(incoming.getPaymentMethod());
        body.setShippingFee(incoming.getShippingFee());
        body.setTaxAmount(incoming.getTaxAmount());
        body.setDiscountAmount(incoming.getDiscountAmount());
        body.setVoucherDiscount(incoming.getVoucherDiscount());
        if(incoming.getVoucher()==null){
            body.setVoucher(null);
        }else{
            body.setVoucher(String.valueOf(incoming.getVoucher()));

        }
        body.setTotalAmount(incoming.getTotalPrice());
        logger.info("Data {}", incoming.getTotalPrice());
        List<ItemProductJson> cart = new ArrayList<>();
        cart = incoming.getItems();
        if (incoming.getItems() != null) {
            for (ItemProductJson p : incoming.getItems()) {
                if (p.getObject() == null)
                    continue;
                Integer requestedQty = p.getQuantity();
                if (requestedQty == null || requestedQty <= 0) {
                    return new ResponseEntity<>(new ResponseObject(400, "Số lượng không hợp lệ", 0, null),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (body == null || cart == null || cart.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(400, "Giỏ hàng trống", 0, null), HttpStatus.BAD_REQUEST);
        }

        String method = body.getPaymentMethod() == null ? "cod" : body.getPaymentMethod().toLowerCase();
        BigDecimal total = BigDecimal.ZERO;
        try {
            total = orderService.calculateOrderTotal(cart);
            logger.info("Total:  in trycatch {}", total);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(400, e.getMessage(), 0, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        total = total.add(body.getShippingFee());
        logger.info("Voucher: {}", body.getVoucher());
        if (body.getVoucher() != null && !body.getVoucher().isEmpty()) {
            Voucher voucher = voucherRepository.findById(Integer.parseInt(body.getVoucher())).orElse(null);
            if (voucher != null) {
                BigDecimal discountAmount = voucher.getValue();
                BigDecimal newTotal = total.multiply(discountAmount).divide(BigDecimal.valueOf(100));
                logger.info("New Total after discount: {}", newTotal);
                total = total.subtract(newTotal);
                logger.info("Total after discount: {}", total);
                // body.setTotalPrice(total.longValue());
            }
        }
        logger.info("method:{}", method);
        // if ("vnpay".equals(method)) {
        // if (total.compareTo(BigDecimal.valueOf(5000)) < 0) {
        // // Nếu dưới 5.000 VND: không tạo link VNPAY, trả thông báo hoặc tự động
        // chuyển
        // // COD
        // return new ResponseEntity<>(
        // new ResponseObject(400, "Số tiền tối thiểu cho VNPAY là 5.000 VND", 0, null),
        // HttpStatus.BAD_REQUEST);
        // }
        // logger.info("Total: {}" , total);
        // String url = vnPayService.createOrder(httpReq, total.longValue(), "Thanh toan
        // don hang",
        // VNPayConfig.vnp_Returnurl);
        // Map<String, Object> resp = new HashMap<>();
        // resp.put("paymentUrl", url);
        // return ResponseEntity.ok(new ResponseObject(200, "Tạo link thanh toán thành
        // công", 0, resp));
        // }
        // normalize items for service
        if (body.getItems() == null || body.getItems().isEmpty()) {
            body.setItems(cart);
        }
        if (cart.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(400, "Giỏ hàng trống", 0, null), HttpStatus.BAD_REQUEST);
        }
        // COD
        body.setTotalAmount(total);
        body.setTotalPrice(total);
        logger.info("Body: {}", body.getTotalAmount());
        // return new ResponseEntity<>(new ResponseObject(200,
        // "Thanh toán thành công",
        // 0,
        // body),
        // HttpStatus.OK);

        return orderService.checkout(body, httpReq);
    }
}
