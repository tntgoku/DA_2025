package backend.main.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.Model.ResponseObject;
import backend.main.Model.Order.Order;
import backend.main.Repository.OrderRepository;
import backend.main.Service.OrderService;
import backend.main.Service.VNPAY.VNPayService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Autowired
    private VNPayService vnPayService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    private static final org.slf4j.Logger logger = LoggerE.getLogger();

    /**
     * Xử lý callback từ VNPay sau khi thanh toán
     */
    @Transactional
     @GetMapping("/vnpay/callback")
    public ResponseEntity<ResponseObject> vnpayCallback(HttpServletRequest request) {
        try {
            logger.info("VNPay callback received");
            
            // Lấy các tham số từ VNPay
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            String vnp_Amount = request.getParameter("vnp_Amount");
            String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            
            logger.info("VNPay callback params: responseCode={}, txnRef={}, transactionNo={}, amount={}, orderInfo={}, secureHash={}", 
                vnp_ResponseCode, vnp_TxnRef, vnp_TransactionNo, vnp_Amount, vnp_OrderInfo, vnp_SecureHash);
            
            if (vnp_OrderInfo == null || vnp_OrderInfo.isEmpty()) {
                logger.error("vnp_OrderInfo is null or empty");
                return new ResponseEntity<>(
                    new ResponseObject(400, "Thiếu thông tin đơn hàng", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            String[] idOrder = vnp_OrderInfo.split(":");
            if (idOrder.length < 2) {
                logger.error("Invalid order info format: {}", vnp_OrderInfo);
                return new ResponseEntity<>(
                    new ResponseObject(400, "Định dạng thông tin đơn hàng không hợp lệ", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Xác thực chữ ký từ VNPay
            logger.info("Calling vnPayService.orderReturn()");
            int result = vnPayService.orderReturn(request);
            logger.info("VNPay service result: {}", result);
            
            if (result == 1) {
                // Thanh toán thành công
                logger.info("VNPay payment successful for order: {}", vnp_TxnRef);
                
                // Tìm đơn hàng theo mã đơn hàng
                Optional<Order> orderOpt = orderRepository.findByOrderCode(idOrder[1]);
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    
                    // Cập nhật trạng thái thanh toán
                    order.setPaymentStatus("paid");
                    order.setOrderStatus("confirmed");
                    order.setAmountPaid(order.getTotalAmount());
                    order.setUpdatedAt(java.time.LocalDateTime.now());
                    
                    orderRepository.save(order);
                    
                    logger.info("Order {} payment status updated to paid", vnp_TxnRef);
                    
                    // Trả về JSON với thông tin đơn hàng để frontend xử lý
                    return new ResponseEntity<>(
                        new ResponseObject(200, "Thanh toán thành công", 0, orderService.convertOrderDTO(order)),
                        HttpStatus.OK
                    );
                } else {
                    logger.warn("Order not found: {}", vnp_TxnRef);
                    return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy đơn hàng", 1, null),
                        HttpStatus.NOT_FOUND
                    );
                }
            } else if (result == 0) {
                logger.warn("VNPay payment failed for order: {}", vnp_TxnRef);
                Optional<Order> orderOpt = orderRepository.findByOrderCode(idOrder[1]);
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    
                    order.setPaymentStatus("failed");
                    order.setOrderStatus("cancelled");
                    order.setUpdatedAt(java.time.LocalDateTime.now());
                    orderRepository.save(order);
                    logger.info("Order {} payment status updated to failed and cancelled", vnp_TxnRef);
                    orderService.restoreInventory(order);
                    return new ResponseEntity<>(
                        new ResponseObject(400, "Thanh toán thất bại", 1, orderService.convertOrderDTO(order)),
                        HttpStatus.BAD_REQUEST
                    );
                } else {
                    logger.warn("Order not found for failed payment: {}", vnp_TxnRef);
                    return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy đơn hàng", 1, null),
                        HttpStatus.NOT_FOUND
                    );
                }
            } else {
                // Chữ ký không hợp lệ
                logger.error("VNPay signature verification failed for order: {}", vnp_TxnRef);
                return new ResponseEntity<>(
                    new ResponseObject(400, "Chữ ký không hợp lệ", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
        } catch (Exception e) {
            logger.error("Error processing VNPay callback: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Lỗi xử lý thanh toán", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * API để lấy thông tin đơn hàng cho trang PaymentSuccess
     */
    @GetMapping("/order-details")
    public ResponseEntity<ResponseObject> getOrderDetails(@RequestParam String orderCode) {
        try {
            logger.info("Getting order details for: {}", orderCode);
            
            Optional<Order> orderOpt = orderRepository.findByOrderCode(orderCode);
            if (!orderOpt.isPresent()) {
                return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy đơn hàng", 1, null),
                    HttpStatus.NOT_FOUND
                );
            }
            
            Order order = orderOpt.get();
            return new ResponseEntity<>(
                new ResponseObject(200, "Lấy thông tin đơn hàng thành công", 0, orderService.convertOrderDTO(order)),
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            logger.error("Error getting order details for {}: {}", orderCode, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Lỗi khi lấy thông tin đơn hàng", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * API để hủy đơn hàng khi thanh toán VNPay thất bại
     */
    @PostMapping("/cancel-order")
    public ResponseEntity<ResponseObject> cancelOrder(@RequestParam String orderCode) {
        try {
            logger.info("Cancelling order: {}", orderCode);
            
            Optional<Order> orderOpt = orderRepository.findByOrderCode(orderCode);
            if (!orderOpt.isPresent()) {
                return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy đơn hàng", 1, null),
                    HttpStatus.NOT_FOUND
                );
            }
            
            Order order = orderOpt.get();
            
            // Kiểm tra trạng thái đơn hàng
            if ("paid".equals(order.getPaymentStatus())) {
                return new ResponseEntity<>(
                    new ResponseObject(400, "Không thể hủy đơn hàng đã thanh toán", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Cập nhật trạng thái đơn hàng
            order.setOrderStatus("cancelled");
            order.setPaymentStatus("cancelled");
            order.setUpdatedAt(java.time.LocalDateTime.now());
            
            orderRepository.save(order);
            
            // Hoàn trả tồn kho
            orderService.restoreInventory(order);
            
            logger.info("Order {} cancelled successfully", orderCode);
            
            return new ResponseEntity<>(
                new ResponseObject(200, "Hủy đơn hàng thành công", 0, order),
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            logger.error("Error cancelling order {}: {}", orderCode, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Lỗi khi hủy đơn hàng", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * API để cập nhật trạng thái thanh toán
     */
    @PostMapping("/update-payment-status")
    public ResponseEntity<ResponseObject> updatePaymentStatus(
            @RequestParam String orderCode,
            @RequestParam String paymentStatus) {
        try {
            logger.info("Updating payment status for order {} to {}", orderCode, paymentStatus);
            
            Optional<Order> orderOpt = orderRepository.findByOrderCode(orderCode);
            if (!orderOpt.isPresent()) {
                return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy đơn hàng", 1, null),
                    HttpStatus.NOT_FOUND
                );
            }
            
            Order order = orderOpt.get();
            order.setPaymentStatus(paymentStatus);
            order.setUpdatedAt(java.time.LocalDateTime.now());
            
            // Nếu thanh toán thành công, cập nhật trạng thái đơn hàng
            if ("paid".equals(paymentStatus)) {
                order.setOrderStatus("confirmed");
                order.setAmountPaid(order.getTotalAmount());
            }
            
            orderRepository.save(order);
            
            logger.info("Payment status updated for order {} to {}", orderCode, paymentStatus);
            
            return new ResponseEntity<>(
                new ResponseObject(200, "Cập nhật trạng thái thanh toán thành công", 0, order),
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            logger.error("Error updating payment status for order {}: {}", orderCode, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Lỗi khi cập nhật trạng thái thanh toán", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
