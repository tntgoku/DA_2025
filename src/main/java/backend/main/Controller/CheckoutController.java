package backend.main.Controller;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.Model.InventoryItem;
import backend.main.Model.ResponseObject;
import backend.main.Request.Checkout.CheckoutRequest;
import backend.main.Request.Checkout.CheckoutIncomingRequest;
import backend.main.Request.Json.ItemProductJson;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Service.OrderService;
import backend.main.Service.VNPAY.VNPayConfig;
import backend.main.Service.VNPAY.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private InventoryReponsitory inventoryReponsitory;
    private final Logger logger = LoggerE.logger;

    @PostMapping
    public ResponseEntity<ResponseObject> checkout(@RequestBody CheckoutIncomingRequest incoming,
            HttpServletRequest httpReq) {
        logger.info("Checkout incoming\n" + (incoming == null ? "null" : incoming.getEmail()));
        if (incoming == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Dữ liệu không hợp lệ", 0, null),
                    HttpStatus.BAD_REQUEST);
        }

        CheckoutRequest body = new CheckoutRequest();
        body.setId(incoming.getId());
        body.setEmail(incoming.getEmail());
        body.setFullname(incoming.getFullname());
        body.setPhone(incoming.getPhone());
        body.setProvince(incoming.getProvince());
        body.setDistrict(incoming.getDistrict());
        body.setAddress(incoming.getAddress());
        body.setNote(incoming.getNote());
        body.setPaymentMethod(incoming.getPaymentMethod());
        body.setVoucher(incoming.getVoucher() == null ? null : String.valueOf(incoming.getVoucher()));
        body.setTotalPrice(incoming.getTotalPrice());
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

        if ("vnpay".equals(method)) {
            BigDecimal total = BigDecimal.ZERO;
            for (ItemProductJson it : cart) {
                // Ưu tiên tìm theo variantId
                Integer id = it.getId();
                Optional<InventoryItem> invOpt = inventoryReponsitory.findByProductVariant_Id(id);
                InventoryItem inv = invOpt.orElseGet(() -> inventoryReponsitory.findById(id).orElse(null));
                if (inv == null) {
                    // log thêm để check dữ liệu
                    logger.warning("Not found by variantId nor inventoryItemId: " + id);
                    return new ResponseEntity<>(new ResponseObject(404, "Không tìm thấy tồn kho", 0, body),
                            HttpStatus.NOT_FOUND);
                }
                BigDecimal unit = inv.getSalePrice() != null ? inv.getSalePrice()
                        : (inv.getListPrice() != null ? inv.getListPrice() : inv.getCostPrice());
                total = total.add(unit.multiply(BigDecimal.valueOf(it.getQuantity())));
            }
            total = body.getTotalPrice();
            if (total.compareTo(BigDecimal.valueOf(5000)) < 0) {
                // Nếu dưới 5.000 VND: không tạo link VNPAY, trả thông báo hoặc tự động chuyển
                // COD
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số tiền tối thiểu cho VNPAY là 5.000 VND", 0, null),
                        HttpStatus.BAD_REQUEST);
            }

            String url = vnPayService.createOrder(httpReq, total.longValue(), "Thanh toan don hang",
                    VNPayConfig.vnp_Returnurl);
            Map<String, Object> resp = new HashMap<>();
            resp.put("paymentUrl", url);
            return ResponseEntity.ok(new ResponseObject(200, "Tạo link thanh toán thành công", 0, resp));
        }
        // normalize items for service
        if (body.getItems() == null || body.getItems().isEmpty()) {
            body.setItems(cart);
        }
        if (cart.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(400, "Giỏ hàng trống", 0, null), HttpStatus.BAD_REQUEST);
        }
        // COD
        return orderService.checkout(body);
    }
}
