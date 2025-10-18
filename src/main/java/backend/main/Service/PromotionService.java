package backend.main.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.DTO.PromotionDTO.PromotionDTO;
import backend.main.DTO.PromotionDTO.VourcherDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Discount;
import backend.main.Model.Promotion.Voucher;
import backend.main.Repository.DiscountRepository;
import backend.main.Repository.VoucherRepository;
import jakarta.transaction.Transactional;

@Service
public class PromotionService implements BaseService<Discount, Integer> {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    public ResponseEntity<ResponseObject> findAll() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.findAll()), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findDiscountForProduct() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.getDiscountInventoryNative()), HttpStatus.OK);
    }
    @Transactional()
    public boolean checkDiscountForProduct(Integer productId) {
        return discountRepository.getDiscountInventoryNative()
            .stream() // Chuyển sang Stream
            .anyMatch(discount -> 
                // So sánh các giá trị Integer bằng .equals() để tránh lỗi null
                discount.getProductId() != null && discount.getProductId().equals(productId)
            );
    }

    public PromotionDTO calculateDiscountForProduct(Integer productId) {
            List<PromotionDTO> discounts = discountRepository.getDiscountInventoryNative();
            PromotionDTO reuslt = null;
            boolean flag = false;
            for(PromotionDTO discount : discounts) {
                if(discount.getProductId() != null && discount.getProductId().equals(productId)) {
                    flag = true;
                    reuslt = discount;
                    break;
                }
            }
            return reuslt;
    }
    
    public ResponseEntity<ResponseObject> findAllPromotion() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Discount entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Discount entity) {
        // TODO Auto-generated method stub
        return null;
    }
    public ResponseEntity<ResponseObject> findDiscountForVoucher() {
        List<VourcherDTO> vouchers = voucherRepository.findAll().stream()
            .map(this::convertVourcherDTO)
            .collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, vouchers), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findVoucherById(Integer id) {
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            return new ResponseEntity<>(new ResponseObject(404, "Voucher not found", 1, null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(200, null, 0, convertVourcherDTO(voucher)), HttpStatus.OK);
    }
    public ResponseEntity<ResponseObject> checkVoucher(String code) {
      List<Voucher> vouchers = voucherRepository.findByCode(code);
      if (vouchers.isEmpty()) {
        return new ResponseEntity<>(new ResponseObject(404, "Voucher not found", 1, null), HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(new ResponseObject(200, null, 0, convertVourcherDTO(vouchers.get(0))), HttpStatus.OK);
    }
    private VourcherDTO convertVourcherDTO(Voucher voucher) {
        return new VourcherDTO(voucher.getId(), voucher.getCode(), voucher.getName(), voucher.getDescription(), voucher.getDiscountType(), voucher.getValue(), voucher.getMaxDiscount(), voucher.getMinOrderValue(), voucher.getTotalUses(), voucher.getMaxUses(), voucher.getMaxUsesPerUser(), voucher.getStartDate(), voucher.getEndDate(), voucher.getIsActive(), voucher.getPriority(), voucher.getCreatedAt(), voucher.getUpdatedAt());
    }
}
