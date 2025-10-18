package backend.main.Controller;

import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Discount;
import backend.main.Service.DiscountService;
import backend.main.DTO.PromotionDTO.CampaignDTO;
// import backend.main.DTO.PromotionDTO.DiscountCampaignDTO;
import backend.main.DTO.PromotionDTO.DiscountCampaignProjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller quản lý Discount (Voucher)
 */
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    /**
     * Lấy tất cả discounts
     */
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        List<Discount> discounts = discountService.findAll();
        return ResponseEntity.ok(discounts);
    }

    /**
     * Lấy discount theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscountById(@PathVariable Integer id) {
        java.util.Optional<Discount> discount = discountService.findById(id);
        if (discount.isPresent()) {
            return ResponseEntity.ok(discount.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tạo discount mới
     */
    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody Discount discount) {
        try {
            ResponseEntity<ResponseObject> response = discountService.createNew(discount);
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                "error", "Error creating discount",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Cập nhật discount
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Integer id, @RequestBody Discount discount) {
        try {
            discount.setId(id);
            ResponseEntity<ResponseObject> response = discountService.update(discount);
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                "error", "Error updating discount",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Xóa discount
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Integer id) {
        try {
            ResponseEntity<ResponseObject> response = discountService.delete(id);
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                "error", "Error deleting discount",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Lấy discount theo mã
     */
 

    /**
     * Lấy các discount đang hoạt động
     */
    @GetMapping("/active")
    public ResponseEntity<List<Discount>> getActiveDiscounts() {
        List<Discount> activeDiscounts = discountService.findActiveDiscounts();
        return ResponseEntity.ok(activeDiscounts);
    }

    /**
     * Lấy tất cả discount campaigns với thông tin chi tiết
     */
    @GetMapping("/campaigns")
    public ResponseEntity<ResponseObject> getAllDiscountCampaigns() {
        List<DiscountCampaignProjection> campaigns = discountService.findAllDiscountCampaigns();
        List<CampaignDTO> campaignDTOs = campaigns.stream()
            .map(campaign -> {
                return new CampaignDTO(campaign.getCampaignId(), 
                campaign.getCampaignName(), campaign.getCampaignDescription(), 
                campaign.getCampaignType(), campaign.getStartDate(), 
                campaign.getEndDate(), campaign.getIsActive(), 
                campaign.getCreatedAt(), campaign.getPriority(), 
                campaign.getValue(), campaign.getMaxDiscount(), 
                campaign.getMinOrderValue(), campaign.getTargetType().toUpperCase(), 
                campaign.getIsIncluded());
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(new ResponseObject(200, 
        "Lấy tất cả discount campaigns thành công", 0, campaignDTOs));
    }
}
