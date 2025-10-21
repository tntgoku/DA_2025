package backend.main.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.DTO.OrderDTO;
import backend.main.DTO.OrderItemDTO;
import backend.main.DTO.VoucherDTO;
import backend.main.DTO.Product.VariantDTO;
import backend.main.DTO.PromotionDTO.PromotionDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Order.Order;
import backend.main.Model.Order.OrderItem;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Promotion.Voucher;
import backend.main.Model.InventoryItem;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.UserRepository;
import backend.main.Repository.VoucherRepository;
import backend.main.Request.OrderItemRequest;
import backend.main.Request.Checkout.CheckoutRequest;
import backend.main.Request.Json.ItemProductJson;
import backend.main.Repository.OrderRepository;
import backend.main.Repository.OrderITemRepository;
import backend.main.Model.User.User;

@Service
public class OrderService implements BaseService<Order, Integer> {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private InventoryReponsitory inventoryReponsitory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private OrderITemRepository orderItemRepository;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private VariantService variantService;
    @Autowired
    private EmailService emailService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> findAll() {
        List<Order> listitem = repository.findAll();
        if (listitem == null || listitem.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(204,
                    "Không tìm thấy", 0, null),
                    HttpStatus.NO_CONTENT);
        }
        List<OrderDTO> dto = listitem.stream().map(item -> {
            return convertOrderDTO(item);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, dto),
                HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> findObjectByID(Integer id) {
        Optional<Order> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(404,
                    "Không thành công", 0, "not Found source have ID: " + id),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, repository.findById(id)),
                HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> findAllOrderByIdUser(Integer id) {
        List<OrderDTO> listnew = new ArrayList<>();
        repository.findListOrdersByIdCustomer(id)
                .forEach(item -> {
                    OrderDTO dto = convertOrderDTO(item);
                    listnew.add(dto);
                });
        ;
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, listnew),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseObject> createNewDTO(OrderDTO orderDTO) {
        Order order = convertOrder(orderDTO);
        return createNew(order);
    }

    @Transactional // Chỉ đọc, không cần thay đổi data
    public BigDecimal calculateOrderTotal(List<ItemProductJson> cart) throws Exception {
        BigDecimal total = BigDecimal.ZERO;
        // ... Khởi tạo các biến khác ...
        for (ItemProductJson it : cart) {
            Integer id = it.getId();
            Optional<InventoryItem> invOpt = inventoryReponsitory.findByProductVariant_Id(id);
            InventoryItem inv = invOpt.orElseGet(() -> inventoryReponsitory.findById(id).orElse(null));
            if (inv == null) {
                throw new Exception("Không tìm thấy tồn kho cho ID: " + id);
            }
            logger.info("Inventory: {} {}", inv.getProductVariant().getId(), it.getQuantity());
            // Tải Lazy-loaded data TRONG TRANSACTION
            if (inv.getProductVariant().getSalePrice() != null
                    && inv.getProductVariant().getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
                total = total
                        .add(inv.getProductVariant().getSalePrice().multiply(BigDecimal.valueOf(it.getQuantity())));
            } else if (inv.getProductVariant().getListPrice() != null
                    && inv.getProductVariant().getListPrice().compareTo(BigDecimal.ZERO) > 0) {
                if (promotionService.checkDiscountForProduct(inv.getProductVariant().getProduct().getId())) {
                    PromotionDTO discount = promotionService
                            .calculateDiscountForProduct(inv.getProductVariant().getProduct().getId());
                    logger.info("Discount: {}", discount.getValue());
                    BigDecimal discountValue = BigDecimal.valueOf(discount.getValue());

                    BigDecimal discountPrice = inv.getProductVariant().getListPrice().multiply(discountValue)
                            .divide(BigDecimal.valueOf(100));
                    logger.info("DiscountPrice: {}", discountPrice);
                    BigDecimal ProductPrice = inv.getProductVariant().getListPrice();
                    logger.info("ProductPrice before discount: {}", ProductPrice);
                    ProductPrice = ProductPrice.subtract(discountPrice);

                    logger.info("ProductPrice after discount: {}", ProductPrice);
                    total = total.add(ProductPrice.multiply(BigDecimal.valueOf(it.getQuantity())));
                } else {
                    total = total
                            .add(inv.getProductVariant().getListPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
                }
                // total =
                // total.add(inv.getProductVariant().getListPrice().multiply(BigDecimal.valueOf(it.getQuantity())));

            } else if (inv.getProductVariant().getCostPrice() != null
                    && inv.getProductVariant().getCostPrice().compareTo(BigDecimal.ZERO) > 0) {
                total = total
                        .add(inv.getProductVariant().getCostPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
            }
            // BigDecimal unit = inv.getProductVariant().getSalePrice() != null ?
            // inv.getProductVariant().getSalePrice()
            // : (inv.getProductVariant().getListPrice() != null ?
            // inv.getProductVariant().getListPrice() :
            // inv.getProductVariant().getCostPrice());

            // total = total.add(unit.multiply(BigDecimal.valueOf(it.getQuantity())));
        }

        logger.info("Total: {}", total);
        return total;
    }

    // Create new order from OrderRequest
    @Transactional
    public ResponseEntity<ResponseObject> createNewFromRequest(backend.main.Request.OrderRequest orderRequest) {
        try {
            Order order = convertFromOrderRequest(orderRequest);
            order.setCreatedAt(java.time.LocalDateTime.now());
            order.setUpdatedAt(java.time.LocalDateTime.now());

            if (order.getOrderCode() == null || order.getOrderCode().isEmpty()) {
                order.setOrderCode("ORD" + System.currentTimeMillis());
            }

            Order saved = repository.save(order);
            logger.info("✅ Created new order: ID={}, Code={}", saved.getId(), saved.getOrderCode());

            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo đơn hàng thành công", 0, saved),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("❌ Error creating order: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tạo đơn hàng: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update order from OrderRequest
    @Transactional
    public ResponseEntity<ResponseObject> updateFromRequest(backend.main.Request.OrderRequest orderRequest) {
        try {
            Optional<Order> optional = repository.findById(orderRequest.getId());
            if (!optional.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy đơn hàng với ID: " + orderRequest.getId(), 1, null),
                        HttpStatus.NOT_FOUND);
            }

            Order existing = optional.get();
            String oldStatus = existing.getOrderStatus(); // Lưu trạng thái cũ
            
            updateOrderFromRequest(existing, orderRequest);
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            Order updated = repository.save(existing);
            
            // Gửi email thông báo nếu trạng thái thay đổi
            if (orderRequest.getOrderStatus() != null && !orderRequest.getOrderStatus().equals(oldStatus)) {
                try {
                    emailService.sendOrderStatusUpdateEmail(
                        updated.getCustomerEmail(),
                        updated.getCustomerName(),
                        updated.getOrderCode(),
                        oldStatus,
                        updated.getOrderStatus(),
                        updated.getNotes()
                    );
                    logger.info("Email cập nhật trạng thái đơn hàng đã được gửi cho: {}", updated.getCustomerEmail());
                } catch (Exception e) {
                    logger.error("Lỗi khi gửi email cập nhật trạng thái đơn hàng: {}", e.getMessage());
                    // Không throw exception để không ảnh hưởng đến việc cập nhật đơn hàng
                }
            }
            
            logger.info("✅ Updated order: ID={}, Code={}", updated.getId(), updated.getOrderCode());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật đơn hàng thành công", 0, updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error updating order: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi cập nhật đơn hàng: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Order convertFromOrderRequest(backend.main.Request.OrderRequest orderRequest) {
        Order order = new Order();
        if (orderRequest.getId() != null) {
            order.setId(orderRequest.getId());
        }

        order.setOrderCode(orderRequest.getOrderCode());
        order.setOrderStatus(orderRequest.getOrderStatus());
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerPhone(orderRequest.getCustomerPhone());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setCustomerAddress(orderRequest.getCustomerAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        order.setSubtotalAmount(orderRequest.getSubtotalAmount());
        order.setDiscountAmount(orderRequest.getDiscountAmount());
        order.setShippingFee(orderRequest.getShippingFee());
        order.setTaxAmount(orderRequest.getTaxAmount());
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setAmountPaid(orderRequest.getAmountPaid());
        order.setVoucherDiscount(orderRequest.getVoucherDiscount());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setShippingMethod(orderRequest.getShippingMethod());
        order.setTrackingNumber(orderRequest.getTrackingNumber());
        order.setNotes(orderRequest.getNotes());

        if (orderRequest.getVoucherId() != null) {
            try {
                order.setVoucher(voucherRepository.findById(orderRequest.getVoucherId()).orElse(null));
            } catch (Exception e) {
                order.setVoucher(null);
            }
        }

        // Set created/updated dates
        if (orderRequest.getCreatedAt() != null) {
            order.setCreatedAt(orderRequest.getCreatedAt());
        } else {
            order.setCreatedAt(java.time.LocalDateTime.now()); // Default value if not set by DB
        }
        order.setUpdatedAt(java.time.LocalDateTime.now());
        return order;
    }

    // Update existing order from OrderRequest
    private void updateOrderFromRequest(Order existing, backend.main.Request.OrderRequest orderRequest) {
        if (orderRequest.getOrderCode() != null) {
            existing.setOrderCode(orderRequest.getOrderCode());
        }
        if (orderRequest.getOrderStatus() != null) {
            existing.setOrderStatus(orderRequest.getOrderStatus());
        }
        if (orderRequest.getCustomerName() != null) {
            existing.setCustomerName(orderRequest.getCustomerName());
        }
        if (orderRequest.getCustomerPhone() != null) {
            existing.setCustomerPhone(orderRequest.getCustomerPhone());
        }
        if (orderRequest.getCustomerEmail() != null) {
            existing.setCustomerEmail(orderRequest.getCustomerEmail());
        }
        if (orderRequest.getCustomerAddress() != null) {
            existing.setCustomerAddress(orderRequest.getCustomerAddress());
        }
        if (orderRequest.getPaymentMethod() != null) {
            existing.setPaymentMethod(orderRequest.getPaymentMethod());
        }
        if (orderRequest.getSubtotalAmount() != null) {
            existing.setSubtotalAmount(orderRequest.getSubtotalAmount());
        }
        if (orderRequest.getDiscountAmount() != null) {
            existing.setDiscountAmount(orderRequest.getDiscountAmount());
        }
        if (orderRequest.getShippingFee() != null) {
            existing.setShippingFee(orderRequest.getShippingFee());
        }
        if (orderRequest.getTaxAmount() != null) {
            existing.setTaxAmount(orderRequest.getTaxAmount());
        }
        if (orderRequest.getTotalAmount() != null) {
            existing.setTotalAmount(orderRequest.getTotalAmount());
        }
        if (orderRequest.getAmountPaid() != null) {
            existing.setAmountPaid(orderRequest.getAmountPaid());
        }
        if (orderRequest.getVoucherDiscount() != null) {
            existing.setVoucherDiscount(orderRequest.getVoucherDiscount());
        }
        if (orderRequest.getShippingAddress() != null) {
            existing.setShippingAddress(orderRequest.getShippingAddress());
        }
        if (orderRequest.getShippingMethod() != null) {
            existing.setShippingMethod(orderRequest.getShippingMethod());
        }
        if (orderRequest.getTrackingNumber() != null) {
            existing.setTrackingNumber(orderRequest.getTrackingNumber());
        }
        if (orderRequest.getNotes() != null) {
            existing.setNotes(orderRequest.getNotes());
        }
        if (orderRequest.getPaymentStatus() != null) {
            existing.setPaymentStatus(orderRequest.getPaymentStatus());
        }
        if (orderRequest.getVoucherId() != null) {
            try {
                existing.setVoucher(voucherRepository.findById(orderRequest.getVoucherId()).orElse(null));
            } catch (Exception e) {
                logger.warn("Error updating voucher: {}", e.getMessage());
                existing.setVoucher(null);
            }
        }
        if (orderRequest.getItems() != null && !orderRequest.getItems().isEmpty()) {
            updateOrderItems(existing, orderRequest.getItems());
        }
    }

    // Update order items - handle add/update/delete operations
    private void updateOrderItems(Order order, List<backend.main.Request.OrderItemRequest> newItems) {
        // 1. Lấy tập hợp OrderItem hiện tại (managed by Hibernate)
        Set<OrderItem> existingItems = order.getOrderItems();

        // Tải và ánh xạ các Item đã có ID (để cập nhật)
        Map<Integer, OrderItem> existingItemsMap = existingItems.stream()
                .filter(item -> item.getId() != null) // Lọc các item đã có ID (đã tồn tại trong DB)
                .collect(Collectors.toMap(OrderItem::getId, item -> item));
        Set<OrderItem> updatedItems = new HashSet<>();
        // 2. Lặp qua Request để CẬP NHẬT hoặc TẠO MỚI
        for (backend.main.Request.OrderItemRequest newItem : newItems) {
            if (newItem.getId() != null && existingItemsMap.containsKey(newItem.getId())) {
                // CẬP NHẬT item đã tồn tại
                OrderItem existingItem = existingItemsMap.get(newItem.getId());
                updateOrderItemFromRequest(existingItem, newItem);
                updatedItems.add(existingItem);
            } else {
                OrderItem newOrderItem = convertOrderItemFromRequest(newItem, order.getId(), order);// Truyền Order
                                                                                                    // Entity
                if (newOrderItem != null) {
                    updatedItems.add(newOrderItem);
                } else {
                    logger.error("Error converting order item from request: {}", newItem.toString());
                }
            }
        }

        existingItems.clear();
        existingItems.addAll(updatedItems);

        order.setOrderItems(existingItems);

        logger.info("✅ Finished updating items. Total items to be persisted: {}", updatedItems.size());
    } // Update existing order item from request

    private void updateOrderItemFromRequest(OrderItem existing, backend.main.Request.OrderItemRequest request) {
        BigDecimal listPrice = existing.getVariant().getListPrice();
        BigDecimal salePrice = existing.getVariant().getSalePrice();
        BigDecimal costPrice = existing.getVariant().getCostPrice();
        PromotionDTO discount = promotionService
                .calculateDiscountForProduct(existing.getVariant().getProduct().getId());
        if (discount != null) {
            logger.info("Discount:\n\n\n\n\n {}", discount.getValue());
            BigDecimal discountValue = BigDecimal.valueOf(discount.getValue());
            BigDecimal discountPrice = listPrice.multiply(discountValue).divide(BigDecimal.valueOf(100));
            BigDecimal ProductPrice = listPrice;
            ProductPrice = ProductPrice.subtract(discountPrice);
            existing.setUnitSalePrice(ProductPrice);
            existing.setDiscountAmount(discountPrice);
            existing.setTotalPrice(ProductPrice.multiply(BigDecimal.valueOf(request.getQuantity())));
        } else {
            existing.setUnitSalePrice(salePrice);
            existing.setTotalPrice(listPrice.multiply(BigDecimal.valueOf(request.getQuantity())));
        }
        existing.setProductName(existing.getVariant().getProduct().getName() + " - " + existing.getVariant().getColor()
                + " - " + existing.getVariant().getStorage());
        if (request.getProductName() != null) {
            existing.setProductName(request.getProductName());
        }
        existing.setVariantAttributes(existing.getVariant().getColor() + " - " + existing.getVariant().getStorage()
                + " - " + existing.getVariant().getRam() + " - " + existing.getVariant().getRegionCode());
        if (request.getVariantAttributes() != null) {
            existing.setVariantAttributes(request.getVariantAttributes());
        }
        existing.setSku(existing.getVariant().getSku());
        if (request.getSku() != null) {
            existing.setSku(request.getSku());
        }
        if (request.getUnitSalePrice() != null) {
            existing.setUnitSalePrice(request.getUnitSalePrice());
        }
        if (request.getUnitCostPrice() != null && request.getUnitCostPrice().compareTo(costPrice) != 0) {
            existing.setUnitCostPrice(request.getUnitCostPrice());
        } else {
            existing.setUnitCostPrice(costPrice);
        }
        if (request.getQuantity() != null) {
            existing.setQuantity(request.getQuantity());
            Integer stockDelta = request.getQuantity();
            Integer newStock = existing.getVariant().getInventoryItem().getStock() - stockDelta;
            if (newStock < 0) {
                throw new RuntimeException("Không đủ tồn kho (" + existing.getVariant().getInventoryItem().getStock()
                        + ") để giảm " + stockDelta + " sản phẩm cho biến thể ID " + existing.getVariant().getId());
            } else {
                existing.getVariant().getInventoryItem().setStock(newStock);
                InventoryItem inventoryItem = inventoryReponsitory.save(existing.getVariant().getInventoryItem());
                if (inventoryItem == null) {
                    throw new RuntimeException(
                            "Không thể cập nhật tồn kho cho biến thể ID " + existing.getVariant().getId());
                } else {
                    logger.info("Updated stock for variant {}: Delta {}, new stock {}", existing.getVariant().getId(),
                            stockDelta, newStock);
                }
            }
        }
        if (request.getTotalPrice() != null) {
            existing.setTotalPrice(request.getTotalPrice());
        }
        if (request.getWarrantyMonths() != null) {
            existing.setWarrantyMonths(request.getWarrantyMonths());
        }
        existing.setConditionType("request.getConditionType()");
        logger.debug("Update: {},{},{},{},{}", existing.getId(), existing.getProductName(),
                existing.getVariantAttributes(), existing.getSku(), existing.getTotalPrice());
    }

    // Convert OrderItemRequest to OrderItem entity
    private OrderItem convertOrderItemFromRequest(OrderItemRequest request, Integer orderId, Order order) {
        OrderItem item = new OrderItem();
        try {
            if (order != null) {
                item.setOrder(order);
            } else {
                logger.error("Order is null");
                return null;
            }
            item.setProductName(request.getProductName());
            item.setVariant(
                    (ProductVariant) variantService.findVariantById(request.getVariantId()).getBody().getData());
            item.setVariantAttributes(request.getVariantAttributes());
            item.setSku(request.getSku());
            item.setUnitSalePrice(request.getUnitSalePrice());
            item.setUnitCostPrice(request.getUnitCostPrice());
            item.setQuantity(request.getQuantity());
            item.setTotalPrice(request.getTotalPrice());
            item.setConditionType(null);
            item.setWarrantyMonths(request.getWarrantyMonths());
            item.setCreatedAt(java.time.LocalDateTime.now());
            logger.debug("Convert: {},{},{},{},{}", item.getId(), item.getProductName(), item.getVariantAttributes(),
                    item.getSku(), item.getTotalPrice());

        } catch (Exception e) {
            logger.error("Error converting order item from request: {}", e.getMessage());
            throw new IllegalArgumentException(
                    "Variant ID cannot be null for a new order item." + request.getVariantId()); // Ném ngoại lệ
        }

        return item;
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Order entity) {
        try {
            Order saved = repository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Tạo đơn hàng thành công", 0, saved),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500, "Lỗi khi tạo đơn hàng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ResponseObject> checkout(CheckoutRequest request) {
        List<ItemProductJson> cart = request != null && request.getItems() != null && !request.getItems().isEmpty()
                ? request.getItems()
                : (request != null ? request.getItems() : null);
        if (request == null || cart == null || cart.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(400, "Giỏ hàng trống", 0, null),
                    HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();
        order.setOrderCode("OD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Integer customerId = request.getId();
        if(customerId != null){
        logger.info("Customer ID: {}", customerId);
        }
        order.setCustomer(customerId);
        order.setCustomerName(request.getFullname());
        order.setCustomerPhone(request.getPhone());
        order.setCustomerEmail(request.getEmail());
        order.setCustomerAddress(request.getAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        String shippingAddress = request.getProvince();
        if (request.getDistrict() != null && !request.getDistrict().isEmpty()) {
            shippingAddress += ", " + request.getDistrict();
        }
        order.setShippingAddress(shippingAddress);
        order.setNotes(request.getNote());
        java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (ItemProductJson it : cart) {
            Integer id = it.getId();
            logger.info("ID truy vấn product_variant: " + it.getId());
            Optional<InventoryItem> invOpt = inventoryReponsitory.findByProductVariant_Id(id);
            if (!invOpt.isPresent()) {
                invOpt = inventoryReponsitory.findById(id);
            }

            if (!invOpt.isPresent()) {
                logger.warn("Not found Variant: {}", it.toString());
                logger.warn("Not found by variantId or inventoryId: {}", it.getId());
            }
            InventoryItem inv = invOpt.get();
            if (inv.getStock() == null || inv.getStock() <= 0) {
                logger.warn("Stock is 0 or less: {}", inv.getProductVariant().getSku());
                return new ResponseEntity<>(
                        new ResponseObject(400, "Sản phẩm đã hết hàng: " + inv.getProductVariant().getSku(), 0, null),
                        HttpStatus.BAD_REQUEST);
            }
            int newStock = inv.getStock() - it.getQuantity();
            if (newStock < 0) {
                logger.warn("Stock is less than quantity: {}", inv.getProductVariant().getSku());
                return new ResponseEntity<>(
                        new ResponseObject(400,
                                "Kho không đủ hàng cho SKU: "
                                        + inv.getProductVariant().getSku() + ", ID:"
                                        + inv.getProductVariant().getId(),
                                0,
                                null),
                        HttpStatus.BAD_REQUEST);
            }
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setVariant(inv.getProductVariant());
            oi.setProductName(inv.getProductVariant().getProduct().getName());
            oi.setVariantAttributes(inv.getProductVariant().getColor() + " " + inv.getProductVariant().getStorage());
            oi.setSku(inv.getProductVariant().getSku());
            oi.setConditionType("new");
            PromotionDTO discount = promotionService
                    .calculateDiscountForProduct(inv.getProductVariant().getProduct().getId());
            if (discount != null) {
                BigDecimal discountValue = BigDecimal.valueOf(discount.getValue());
                BigDecimal discountPrice = inv.getProductVariant().getListPrice().multiply(discountValue)
                        .divide(BigDecimal.valueOf(100));
                BigDecimal ProductPrice = inv.getProductVariant().getListPrice();
                ProductPrice = ProductPrice.subtract(discountPrice);
                oi.setUnitSalePrice(ProductPrice);
            } else {
                oi.setUnitSalePrice(
                        inv.getProductVariant().getSalePrice() != null ? inv.getProductVariant().getSalePrice()
                                : inv.getProductVariant().getListPrice());
            }
            oi.setUnitCostPrice(inv.getProductVariant().getCostPrice());
            oi.setQuantity(it.getQuantity());

            BigDecimal unitPrice = BigDecimal.ZERO;
            if (it.getObject() != null && it.getObject().getPrice() != null) {
                unitPrice = it.getObject().getList_price();
            }
            BigDecimal totalPrice = BigDecimal.ZERO;
            if (discount != null) {
                BigDecimal discountValue = BigDecimal.valueOf(discount.getValue());
                BigDecimal discountPrice = inv.getProductVariant().getListPrice().multiply(discountValue)
                        .divide(BigDecimal.valueOf(100));
                BigDecimal ProductPrice = inv.getProductVariant().getListPrice();
                ProductPrice = ProductPrice.subtract(discountPrice);
                totalPrice = ProductPrice.multiply(BigDecimal.valueOf(it.getQuantity()));
            } else {
                totalPrice = unitPrice.multiply(BigDecimal.valueOf(it.getQuantity()));
            }
            oi.setTotalPrice(totalPrice);
            subtotal = subtotal.add(totalPrice);
            items.add(oi);
            // giảm tồn (đảm bảo không âm)
            inv.setStock(newStock);
            inventoryReponsitory.save(inv);
        }
        subtotal = subtotal.add(request.getShippingFee() != null ? request.getShippingFee() : BigDecimal.ZERO);
        logger.info("Subtotal: {}", subtotal);
        order.setShippingFee(request.getShippingFee() != null ? request.getShippingFee() : BigDecimal.ZERO);
        order.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        order.setVoucherDiscount(request.getVoucherDiscount() != null ? request.getVoucherDiscount() : BigDecimal.ZERO);
        order.setTotalAmount(request.getTotalPrice() != null ? request.getTotalPrice() : BigDecimal.ZERO);
        order.setSubtotalAmount(subtotal);
        if (request.getVoucher() != null && !request.getVoucher().isEmpty()) {
            Voucher findv = voucherRepository.findById(Integer.parseInt(request.getVoucher())).orElse(null);
            order.setVoucher(findv);
        }

        Order saved = repository.save(order);
        for (OrderItem item : items) {
            item.setOrder(saved);
            try {
                OrderItem savedItem = orderItemRepository.save(item);
                if (savedItem != null) {
                    logger.info("Đặt hàng thành công: ID: {}, Code: {}, subtotal: {}", saved.getId(),
                            saved.getOrderCode(), subtotal);
                }
            } catch (Exception e) {
                logger.error("Lỗi khi đặt hàng: {}", e.getMessage());
            }

        }
        // saved.getOrderCode(),subtotal);
        if (saved != null) {
            // Gửi email xác nhận đơn hàng
            try {
                String orderItemsHtml = buildOrderItemsHtml(items);
                emailService.sendOrderConfirmationEmail(
                    saved.getCustomerEmail(),
                    saved.getCustomerName(),
                    saved.getOrderCode(),
                    saved.getTotalAmount().toString(),
                    orderItemsHtml
                );
                logger.info("Email xác nhận đơn hàng đã được gửi cho: {}", saved.getCustomerEmail());
            } catch (Exception e) {
                logger.error("Lỗi khi gửi email xác nhận đơn hàng: {}", e.getMessage());
                // Không throw exception để không ảnh hưởng đến việc tạo đơn hàng
            }
            
            return new ResponseEntity<>(
                    new ResponseObject(200, "Đặt hàng thành công", 0, saved),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi đặt hàng", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Xóa đơn hàng thành công", 0, id.toString()),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting order: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500, "Lỗi khi xóa đơn hàng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method để build HTML cho order items
    private String buildOrderItemsHtml(List<OrderItem> items) {
        StringBuilder html = new StringBuilder();
        html.append("<ul style='list-style: none; padding: 0;'>");
        
        for (OrderItem item : items) {
            html.append("<li style='border-bottom: 1px solid #eee; padding: 10px 0;'>");
            html.append("<div style='display: flex; justify-content: space-between;'>");
            html.append("<div>");
            html.append("<strong>").append(item.getProductName()).append("</strong><br>");
            html.append("<small style='color: #666;'>").append(item.getVariantAttributes()).append("</small><br>");
            html.append("<small>SKU: ").append(item.getSku()).append("</small>");
            html.append("</div>");
            html.append("<div style='text-align: right;'>");
            html.append("<div>").append(item.getQuantity()).append(" x ").append(item.getUnitSalePrice()).append(" VNĐ</div>");
            html.append("<strong>").append(item.getTotalPrice()).append(" VNĐ</strong>");
            html.append("</div>");
            html.append("</div>");
            html.append("</li>");
        }
        
        html.append("</ul>");
        return html.toString();
    }

    public ResponseEntity<ResponseObject> updateDTO(OrderDTO orderDTO) {
        Order order = convertOrder(orderDTO);
        return update(order);
    }

    private Order convertOrder(OrderDTO orderDTO) {
        Order order = new Order();
        if (orderDTO.getId() != null) {
            order.setId(orderDTO.getId());
        } else {
            order.setId(null);
        }
        order.setOrderCode(orderDTO.getOrderCode());
        order.setCustomer(orderDTO.getCustomer());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerPhone(orderDTO.getCustomerPhone());
        order.setCustomerEmail(orderDTO.getCustomerEmail());
        order.setCustomerAddress(orderDTO.getCustomerAddress());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setSubtotalAmount(orderDTO.getSubtotalAmount());
        order.setDiscountAmount(orderDTO.getDiscountAmount());
        order.setShippingFee(orderDTO.getShippingFee());
        order.setTaxAmount(orderDTO.getTaxAmount());
        Voucher voucher = voucherRepository.findById(orderDTO.getVoucherId()).orElse(null);
        if (voucher != null) {
            order.setVoucher(voucher);
        } else {
            order.setVoucher(null);
        }
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setAmountPaid(orderDTO.getAmountPaid());
        order.setVoucherDiscount(orderDTO.getVoucherDiscount());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setTrackingNumber(orderDTO.getTrackingNumber());
        order.setNotes(orderDTO.getNotes());
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setCreatedBy(orderDTO.getCreatedBy());
        order.getOrderItems().forEach(item -> {
            item.setOrder(order);
            item.setVariant(item.getVariant());
            item.setProductName(item.getVariant().getProduct().getName());
            item.setVariantAttributes(item.getVariant().getColor() + " " + item.getVariant().getStorage());
            item.setSku(item.getVariant().getSku());
            item.setUnitSalePrice(item.getVariant().getSalePrice() != null ? item.getVariant().getSalePrice()
                    : item.getVariant().getListPrice());
            item.setUnitCostPrice(item.getVariant().getCostPrice());
            item.setQuantity(item.getQuantity());
            item.setTotalPrice(
                item.getUnitSalePrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
        });
        return order;
    }

    public ResponseEntity<ResponseObject> updateOrderDTO(OrderDTO orderDTO) {
        Order order = convertOrder(orderDTO);
        return update(order);
    }

    @Override
    public ResponseEntity<ResponseObject> update(Order entity) {
        try {
            Order saved = repository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Cập nhật đơn hàng thành công", 0, saved),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating order: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500, "Lỗi khi cập nhật đơn hàng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private OrderDTO convertOrderDTO(Order a) {
        OrderDTO dto = new OrderDTO();
        dto.setId(a.getId());
        dto.setOrderCode(a.getOrderCode());
        dto.setCustomer(a.getCustomer());
        dto.setCustomerName(a.getCustomerName());
        dto.setCustomerPhone(a.getCustomerPhone());
        dto.setCustomerEmail(a.getCustomerEmail());
        dto.setCustomerAddress(a.getCustomerAddress());
        dto.setOrderStatus(a.getOrderStatus());
        dto.setPaymentStatus(a.getPaymentStatus());
        dto.setPaymentMethod(a.getPaymentMethod());
        dto.setSubtotalAmount(a.getSubtotalAmount());
        dto.setDiscountAmount(a.getDiscountAmount());
        dto.setShippingFee(a.getShippingFee());
        dto.setTaxAmount(a.getTaxAmount());
        dto.setTotalAmount(a.getTotalAmount());
        dto.setAmountPaid(a.getAmountPaid());

        // Nếu bạn muốn tránh lazy loading cho voucher
        if (a.getVoucher() != null) {
            dto.setVoucherId(a.getVoucher().getId());
        } else {
            dto.setVoucherId(-1);
        }

        dto.setVoucherDiscount(a.getVoucherDiscount());
        dto.setShippingAddress(a.getShippingAddress());
        dto.setShippingMethod(a.getShippingMethod());
        dto.setTrackingNumber(a.getTrackingNumber());
        dto.setNotes(a.getNotes());
        dto.setCreatedBy(a.getCreatedBy());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setItems(null);
        if (a.getOrderItems() != null) {
            List<OrderItemDTO> items = a.getOrderItems().stream().map(item -> {
                return convertItems(item);
            }).collect(Collectors.toList());
            dto.setItems(items);
        } else {
            dto.setItems(Collections.emptyList());
        }
        return dto;
    }

    private OrderItemDTO convertItems(OrderItem item) {
        OrderItemDTO newItem = new OrderItemDTO();
        newItem.setId(item.getId());

        // Kiểm tra variant có tồn tại không để tránh LazyInitializationException
        if (item.getVariant() != null) {
            try {
                VariantDTO itemorder = variantService.convertObject(item.getVariant());
                newItem.setObject(itemorder);
            } catch (Exception e) {
                logger.warn("Lỗi khi convert variant cho OrderItem ID {}: {}", item.getId(), e.getMessage());
                // Tạo VariantDTO rỗng nếu có lỗi
                VariantDTO emptyVariant = new VariantDTO();
                newItem.setObject(emptyVariant);
            }
        } else {
            // Tạo VariantDTO rỗng nếu variant null
            VariantDTO emptyVariant = new VariantDTO();
            newItem.setObject(emptyVariant);
        }

        newItem.setQuantity(item.getQuantity());
        return newItem;
    }

    private VoucherDTO convertVoucherDTO(Voucher v) {
        if (v == null)
            return null;
        VoucherDTO dto = new VoucherDTO();
        dto.setId(v.getId());
        dto.setCode(v.getCode());
        dto.setName(v.getName());
        dto.setDescription(v.getDescription());
        dto.setDiscountType(v.getDiscountType());
        dto.setValue(v.getValue());
        dto.setMaxDiscount(v.getMaxDiscount());
        dto.setMinOrderValue(v.getMinOrderValue());
        dto.setTotalUses(v.getTotalUses());
        dto.setMaxUses(v.getMaxUses());
        dto.setMaxUsesPerUser(v.getMaxUsesPerUser());
        dto.setStartDate(v.getStartDate());
        dto.setEndDate(v.getEndDate());
        dto.setIsActive(v.getIsActive());
        dto.setCreatedAt(v.getCreatedAt());
        // dto.setCampaign(v.getCampaign());
        return dto;
    }

    // Update order status
    public ResponseEntity<ResponseObject> updateOrderStatus(Integer orderId, String status) {
        try {
            Optional<Order> orderOptional = repository.findById(orderId);
            if (!orderOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy đơn hàng", 1, null),
                        HttpStatus.NOT_FOUND);
            }

            Order order = orderOptional.get();
            order.setOrderStatus(status);
            order.setUpdatedAt(java.time.LocalDateTime.now());

            Order updatedOrder = repository.save(order);
            logger.info("Order updated: {}", updatedOrder.getOrderCode());
            logger.info("Order status updated: {}", updatedOrder.getOrderStatus());
            return new ResponseEntity<>(new ResponseObject(200, "Cập nhật trạng thái đơn hàng thành công",
                    0, updatedOrder.getOrderStatus()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating order status: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500, "Lỗi khi cập nhật trạng thái đơn hàng",
                    1, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
