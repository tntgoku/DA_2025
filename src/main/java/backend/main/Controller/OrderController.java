package backend.main.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.DTO.OrderDTO;
import backend.main.DTO.OrderItemDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Voucher;
import backend.main.Repository.VoucherRepository;
import backend.main.Request.OrderItemRequest;
import backend.main.Request.OrderRequest;
import backend.main.Request.Checkout.CheckoutIncomingRequest;
import backend.main.Request.Checkout.CheckoutRequest;
import backend.main.Request.Json.ItemProductJson;
import backend.main.Service.OrderService;
import backend.main.Service.VNPAY.VNPayConfig;
import backend.main.Service.VNPAY.VNPayService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final org.slf4j.Logger logger =LoggerE.getLogger();
    @Autowired
    private OrderService orderService;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VNPayService vnPayService;
    @GetMapping
    public ResponseEntity<ResponseObject> getMethodName() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMethodNameByID(@PathVariable String id) {
        Integer idorder = Integer.parseInt(id);
        return orderService.findObjectByID(idorder);
    }

    // Tạo đơn mới
    @PostMapping
    public ResponseEntity<ResponseObject> createNewOrder(@RequestBody  CheckoutIncomingRequest orderDTO,
    HttpServletRequest httpReq) {
        if (orderDTO == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Dữ liệu không hợp lệ", 0, null),
                    HttpStatus.BAD_REQUEST);
        }

        CheckoutRequest body = new CheckoutRequest();
        // Chỉ set id nếu không null để tránh lỗi
        if (orderDTO.getId() != null) {
            body.setId(orderDTO.getId());
        }
        body.setEmail(orderDTO.getEmail());
        body.setFullname(orderDTO.getFullname());
        body.setPhone(orderDTO.getPhone());
        body.setProvince(orderDTO.getProvince());
        body.setDistrict(orderDTO.getDistrict());
        body.setAddress(orderDTO.getAddress());
        body.setNote(orderDTO.getNote());
        body.setPaymentMethod(orderDTO.getPaymentMethod());
        body.setShippingFee(orderDTO.getShippingFee());
        body.setTaxAmount(orderDTO.getTaxAmount());
        body.setDiscountAmount(orderDTO.getDiscountAmount());
        body.setVoucherDiscount(orderDTO.getVoucherDiscount());
        body.setVoucher(orderDTO.getVoucher() == null ? null : String.valueOf(orderDTO.getVoucher()));
        body.setTotalPrice(orderDTO.getTotalPrice());
        logger.info("Data {}",orderDTO.getTotalPrice());
        List<ItemProductJson> cart = new ArrayList<>();
        cart = orderDTO.getItems();
        if (orderDTO.getItems() != null) {
            for (ItemProductJson p : orderDTO.getItems()) {
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
            logger.info("Total:  in trycatch {}" , total);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(400, e.getMessage(), 0,e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        total = total.add(body.getShippingFee());
        if(body.getVoucher()!=null){
            Voucher voucher = voucherRepository.findById(Integer.parseInt(body.getVoucher())).orElse(null);
        if (voucher != null) {
            BigDecimal discountAmount = voucher.getValue();
            BigDecimal newTotal = total.multiply(discountAmount).divide(BigDecimal.valueOf(100));
                logger.info("New Total after discount: {}" , newTotal);
                total = total.subtract(newTotal);
                logger.info("Total after discount: {}" , total);
                // body.setTotalPrice(total.longValue());
            }
        }
        if ("vnpay".equals(method)) {
            if (total.compareTo(BigDecimal.valueOf(5000)) < 0) {
                // Nếu dưới 5.000 VND: không tạo link VNPAY, trả thông báo hoặc tự động chuyển
                // COD
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số tiền tối thiểu cho VNPAY là 5.000 VND", 0, null),
                        HttpStatus.BAD_REQUEST);
            }
            logger.info("Total: {}" , total);
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
        logger.info("Creating new order: {}", body.toString());

        return orderService.checkout(body);
        // return new ResponseEntity<>(new ResponseObject(200, "Order created successfully", 0, orderDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateOrder(@PathVariable String id, @RequestBody OrderRequest order) {
        logger.info("Updating order ID: {}, data: {}", id, order);
        try {
            order.setId(Integer.parseInt(id));
            return orderService.updateFromRequest(order);
            // return new ResponseEntity<>(new ResponseObject(200, "Order updated successfully", 0,order), HttpStatus.OK);
        } catch (NumberFormatException e) {
            logger.error("Invalid order ID: {}", id);
            return new ResponseEntity<>(new ResponseObject(400, "Không tồn tại ID đơn hàng", 1, null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error updating order: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500, "Lỗi máy chủ", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseObject> updateOrderStatus(@PathVariable String id,
            @RequestBody java.util.Map<String, String> request) {
        Integer orderId = Integer.parseInt(id);
        String status = request.get("status");
        logger.info("Cập nhật trạng thái đơn hàng orderId: {}, status: {}", orderId, status);
        return orderService.updateOrderStatus(orderId, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable String id) {
        Integer orderId = Integer.parseInt(id);
        return orderService.delete(orderId);
    }

    public OrderDTO convertOrderDTO(OrderRequest order) {
        OrderDTO newOrderDTO = new OrderDTO();
        newOrderDTO.setId(order.getId());
        newOrderDTO.setOrderCode(order.getOrderCode());
        newOrderDTO.setOrderStatus(order.getOrderStatus());
        newOrderDTO.setCreatedAt(java.time.LocalDateTime.now());
        newOrderDTO.setCreatedBy(order.getCreatedBy());
        newOrderDTO.setCustomer(order.getCustomer());
        newOrderDTO.setCustomerName(order.getCustomerName());
        newOrderDTO.setCustomerPhone(order.getCustomerPhone());
        newOrderDTO.setCustomerEmail(order.getCustomerEmail());
        newOrderDTO.setCustomerAddress(order.getCustomerAddress());
        newOrderDTO.setPaymentMethod(order.getPaymentMethod());
        newOrderDTO.setSubtotalAmount(order.getSubtotalAmount());
        newOrderDTO.setDiscountAmount(order.getDiscountAmount());
        newOrderDTO.setShippingFee(order.getShippingFee());
        newOrderDTO.setTaxAmount(order.getTaxAmount());
        newOrderDTO.setTotalAmount(order.getTotalAmount());
        newOrderDTO.setAmountPaid(order.getAmountPaid());
        newOrderDTO.setVoucherDiscount(order.getVoucherDiscount());
        newOrderDTO.setShippingAddress(order.getShippingAddress());
        newOrderDTO.setShippingMethod(order.getShippingMethod());
        newOrderDTO.setTrackingNumber(order.getTrackingNumber());
        newOrderDTO.setNotes(order.getNotes());
        newOrderDTO.setCreatedAt(java.time.LocalDateTime.now());
        newOrderDTO.setItems(convertOrderItemDTO(order.getItems()));
        newOrderDTO.setVoucherId(order.getVoucherId());
        newOrderDTO.setVoucherDiscount(order.getVoucherDiscount());
        newOrderDTO.setShippingAddress(order.getShippingAddress());
        newOrderDTO.setShippingMethod(order.getShippingMethod());
        newOrderDTO.setTrackingNumber(order.getTrackingNumber());
        newOrderDTO.setNotes(order.getNotes());
        newOrderDTO.setCreatedAt(java.time.LocalDateTime.now());
        return newOrderDTO;
    }

    public List<OrderItemDTO> convertOrderItemDTO(List<OrderItemRequest> items) {
        return items.stream()
                .map(this::convertOrderItemDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO convertOrderItemDTO(OrderItemRequest item) {
        OrderItemDTO newOrderItemDTO = new OrderItemDTO();
        newOrderItemDTO.setId(item.getId());
        return newOrderItemDTO;
    }
}
