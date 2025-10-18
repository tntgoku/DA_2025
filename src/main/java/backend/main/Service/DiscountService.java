package backend.main.Service;

import backend.main.Model.Promotion.Discount;
import backend.main.Model.Promotion.Voucher;
import backend.main.Model.ResponseObject;
import backend.main.Repository.DiscountRepository;
// import backend.main.DTO.PromotionDTO.DiscountCampaignDTO;
import backend.main.DTO.PromotionDTO.DiscountCampaignProjection;
import backend.main.DTO.PromotionDTO.VourcherDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service quản lý Discount (Voucher)
 */
@Service
public class DiscountService implements BaseService<Discount, Integer> {

    @Autowired
    private DiscountRepository discountRepository;

    /**
     * Lấy tất cả discounts
     */
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    /**
     * Lấy discount theo ID
     */
    public java.util.Optional<Discount> findById(Integer id) {
        return discountRepository.findById(id);
    }

    /**
     * Lưu discount
     */
    public Discount save(Discount discount) {
        if (discount.getCreatedAt() == null) {
            discount.setCreatedAt(LocalDateTime.now());
        }
        discount.setUpdatedAt(LocalDateTime.now());
        return discountRepository.save(discount);
    }

    /**
     * Xóa discount theo ID
     */
    public void deleteById(Integer id) {
        discountRepository.deleteById(id);
    }

    /**
     * Lấy các discount đang hoạt động
     */
    public List<Discount> findActiveDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(now, now);
    }

    /**
     * Lấy các discount sắp bắt đầu
     */
    public List<Discount> findUpcomingDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByStartDateAfter(now);
    }

    /**
     * Lấy các discount sắp kết thúc
     */
    public List<Discount> findEndingSoonDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekFromNow = now.plusWeeks(1);
        return discountRepository.findByEndDateBetween(now, oneWeekFromNow);
    }

    /**
     * Kiểm tra discount có đang hoạt động không
     */
    public boolean isActive(Discount discount) {
        LocalDateTime now = LocalDateTime.now();
        return discount.getIsActive() && 
               discount.getStartDate().isBefore(now) && 
               discount.getEndDate().isAfter(now);
    }

    /**
     * Lấy tất cả discount campaigns với thông tin chi tiết
     */
    public List<DiscountCampaignProjection> findAllDiscountCampaigns() {
        return discountRepository.findAllDiscountCampaign();
    }

    // Implement BaseService methods
    @Override
    public ResponseEntity<ResponseObject> createNew(Discount entity) {
        try {
            Discount save= discountRepository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200, "Discount created successfully", 0, save), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(400, "Error creating discount: " + e.getMessage(), 0, null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(Discount entity) {
        try {
            Discount update= discountRepository.save(entity);   
            return new ResponseEntity<>(new ResponseObject(200, "Discount updated successfully", 0, update), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(400, "Error updating discount: " + e.getMessage(), 0, null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        try {
            discountRepository.deleteById(id);
            return new ResponseEntity<>(new ResponseObject(200, "Discount deleted successfully", 0, null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(400, "Error deleting discount: " + e.getMessage(), 0, null), HttpStatus.BAD_REQUEST);
        }
    }

}
