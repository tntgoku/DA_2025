package backend.main.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.DTO.OrderDTO;
import backend.main.DTO.OrderItemDTO;
import backend.main.DTO.VoucherDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Order.Order;
import backend.main.Model.Order.OrderItem;
import backend.main.Model.Promotion.Voucher;
import backend.main.Repository.OrderRepository;

@Service
public class OrderService implements BaseService<Order, Integer> {
    @Autowired
    private OrderRepository repository;

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
