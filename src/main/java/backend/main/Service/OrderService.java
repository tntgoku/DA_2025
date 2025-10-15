package backend.main.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.Config.LoggerE;
import backend.main.DTO.OrderDTO;
import backend.main.DTO.OrderItemDTO;
import backend.main.DTO.VoucherDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Order.Order;
import backend.main.Model.Order.OrderItem;
import backend.main.Model.Promotion.Voucher;
import backend.main.Model.InventoryItem;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.UserRepository;
import backend.main.Request.Checkout.CheckoutRequest;
import backend.main.Request.Json.ItemProductJson;
import backend.main.Repository.OrderRepository;
import backend.main.Model.User.User;

@Service
public class OrderService implements BaseService<Order, Integer> {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private InventoryReponsitory inventoryReponsitory;
    @Autowired
    private UserRepository userRepository;
    private final Logger logger = LoggerE.logger;

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

    @Override
    public ResponseEntity<ResponseObject> createNew(Order entity) {
        // TODO Auto-generated method stub
        return null;
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
        // Resolve or create customer to satisfy NOT NULL FK
        Integer customerId = request.getId();
        if (customerId == null) {
            Optional<User> byEmail = (request.getEmail() != null && !request.getEmail().isEmpty())
                    ? userRepository.findByEmail(request.getEmail())
                    : Optional.empty();
            Optional<User> byPhone = (byEmail.isPresent() || request.getPhone() == null || request.getPhone().isEmpty())
                    ? Optional.empty()
                    : userRepository.findByPhone(request.getPhone());
            User user = byEmail.orElseGet(() -> byPhone.orElseGet(() -> {
                User u = new User();
                u.setFullName(request.getFullname());
                u.setEmail(request.getEmail());
                u.setPhone(request.getPhone());
                u.setAddress(request.getAddress());
                return userRepository.save(u);
            }));
            customerId = user.getId();
        }
        order.setCustomer(customerId);
        order.setCustomerName(request.getFullname());
        order.setCustomerPhone(request.getPhone());
        order.setCustomerEmail(request.getEmail());
        order.setCustomerAddress(request.getAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(request.getProvince());
        order.setNotes(request.getNote());

        // Tính toán tiền và giảm tồn
        java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();
        for (ItemProductJson it : cart) {
            Integer id = it.getId();
            logger.info("ID truy vấn product_variant: " + it.getId());
            Optional<InventoryItem> invOpt = inventoryReponsitory.findByProductVariant_Id(id);
            if (!invOpt.isPresent()) {
                logger.warning("Not found Variant: " + it.toString());
                logger.warning("Not found by variantId: " + it.getId());
                // logger.warning("Cart: " + (invOpt.get().getId()==null? "-1" :
                // invOpt.get().getId()));
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho: " + it.getId(), 0, null),
                        HttpStatus.NOT_FOUND);
            }
            InventoryItem inv = invOpt.get();
            if (inv.getStock() == null || inv.getStock() <= 0) {
                logger.warning("Stock is 0 or less: " + inv.getProductVariant().getSku());
                return new ResponseEntity<>(
                        new ResponseObject(400, "Sản phẩm đã hết hàng: " + inv.getProductVariant().getSku(), 0, null),
                        HttpStatus.BAD_REQUEST);
            }
            int newStock = inv.getStock() - it.getQuantity();
            if (newStock < 0) {
                logger.warning("Stock is less than quantity: " + inv.getProductVariant().getSku());
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
            oi.setInventoryItem(inv);
            oi.setVariant(inv.getProductVariant());
            oi.setProductName(inv.getProductVariant().getProduct().getName());
            oi.setVariantAttributes(inv.getProductVariant().getColor() + " " + inv.getProductVariant().getStorage());
            oi.setSku(inv.getProductVariant().getSku());
            oi.setConditionType("new");
            oi.setUnitSalePrice(inv.getSalePrice() != null ? inv.getSalePrice() : inv.getListPrice());
            oi.setUnitCostPrice(inv.getCostPrice());
            oi.setQuantity(it.getQuantity());
            oi.setTotalPrice(oi.getUnitSalePrice().multiply(java.math.BigDecimal.valueOf(it.getQuantity())));
            items.add(oi);

            // giảm tồn (đảm bảo không âm)
            inv.setStock(newStock);
            inventoryReponsitory.save(inv);

            subtotal = subtotal.add(oi.getTotalPrice());
        }

        order.setSubtotalAmount(subtotal);
        order.setTotalAmount(subtotal);
        order.setOrderItems(new java.util.HashSet<>(items));

        Order saved = repository.save(order);
        logger.info("Order saved: " + saved.getOrderCode());
        return new ResponseEntity<>(
                new ResponseObject(201, "Đặt hàng thành công", 0, saved.getOrderCode()),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Order entity) {
        // TODO Auto-generated method stub
        return null;
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
        dto.setListiem(null);
        if (a.getOrderItems() != null) {
            List<OrderItemDTO> items = a.getOrderItems().stream().map(item -> {
                return convertItems(item);
            }).collect(Collectors.toList());
            dto.setListiem(items);
        } else {
            dto.setListiem(Collections.emptyList());
        }
        return dto;
    }

    private OrderItemDTO convertItems(OrderItem item) {
        OrderItemDTO newItem = new OrderItemDTO();
        newItem.setId(item.getId());
        newItem.setOrderId(item.getOrder() != null ? item.getOrder().getId() : null);
        newItem.setInventoryId(item.getInventoryItem() != null ? item.getInventoryItem().getId() : -1);
        newItem.setVariantId(item.getVariant() != null ? item.getVariant().getId() : -1);
        newItem.setProductName(item.getProductName());
        newItem.setVariantAttributes(item.getVariantAttributes());
        newItem.setSku(item.getSku());
        newItem.setUnitSalePrice(item.getUnitSalePrice());
        newItem.setUnitCostPrice(item.getUnitCostPrice());
        newItem.setQuantity(item.getQuantity());
        newItem.setTotalPrice(item.getTotalPrice());
        newItem.setWarrantyMonths(item.getWarrantyMonths());
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
        // Nếu bạn không cần campaign thì có thể bỏ dòng này để tránh lazy load
        // dto.setCampaign(v.getCampaign());
        return dto;
    }

}
