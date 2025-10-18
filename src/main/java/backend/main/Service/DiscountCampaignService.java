package backend.main.Service;

import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.DiscountCampaign;
import backend.main.Repository.DiscountCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service quản lý DiscountCampaign
 */
@Service
public class DiscountCampaignService implements BaseService<DiscountCampaign, Integer> {

    @Autowired
    private DiscountCampaignRepository discountCampaignRepository;

    /**
     * Lấy tất cả discount campaigns
     */
    public List<DiscountCampaign> findAll() {
        return discountCampaignRepository.findAll();
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(DiscountCampaign entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(DiscountCampaign entity) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Lấy discount campaign theo ID
     */
    public Optional<DiscountCampaign> findById(Integer id) {
        return discountCampaignRepository.findById(id);
    }

    /**
     * Lấy discount campaigns theo loại
     */
    public List<DiscountCampaign> findByCampaignType(String campaignType) {
        return discountCampaignRepository.findByCampaignType(campaignType);
    }

    /**
     * Lưu discount campaign
     */
    public DiscountCampaign save(DiscountCampaign discountCampaign) {
        if (discountCampaign.getCreatedAt() == null) {
            discountCampaign.setCreatedAt(LocalDateTime.now());
        }
        discountCampaign.setUpdatedAt(LocalDateTime.now());
        return discountCampaignRepository.save(discountCampaign);
    }

    /**
     * Xóa discount campaign theo ID
     */
    public void deleteById(Integer id) {
        discountCampaignRepository.deleteById(id);
    }

    /**
     * Toggle trạng thái active
     */
    public DiscountCampaign toggleActive(Integer id) {
        Optional<DiscountCampaign> campaignOpt = discountCampaignRepository.findById(id);
        if (campaignOpt.isPresent()) {
            DiscountCampaign campaign = campaignOpt.get();
            campaign.setIsActive(!campaign.getIsActive());
            campaign.setUpdatedAt(LocalDateTime.now());
            return discountCampaignRepository.save(campaign);
        }
        throw new RuntimeException("Discount campaign not found with id: " + id);
    }

    /**
     * Lấy các discount campaigns đang hoạt động
     */
    public List<DiscountCampaign> findActiveCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return discountCampaignRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(now, now);
    }

    /**
     * Lấy các discount campaigns sắp bắt đầu
     */
    public List<DiscountCampaign> findUpcomingCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return discountCampaignRepository.findByStartDateAfter(now);
    }

    /**
     * Lấy các discount campaigns sắp kết thúc
     */
    public List<DiscountCampaign> findEndingSoonCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekFromNow = now.plusWeeks(1);
        return discountCampaignRepository.findByEndDateBetween(now, oneWeekFromNow);
    }

    /**
     * Lấy discount campaigns theo priority
     */
    public List<DiscountCampaign> findByPriority(Integer priority) {
        return discountCampaignRepository.findByPriority(priority);
    }

    /**
     * Lấy discount campaigns theo priority range
     */
    public List<DiscountCampaign> findByPriorityBetween(Integer minPriority, Integer maxPriority) {
        return discountCampaignRepository.findByPriorityBetween(minPriority, maxPriority);
    }


    /**
     * Kiểm tra campaign có đang hoạt động không
     */
    public boolean isActive(DiscountCampaign campaign) {
        LocalDateTime now = LocalDateTime.now();
        return campaign.getIsActive() && 
               campaign.getStartDate().isBefore(now) && 
               campaign.getEndDate().isAfter(now);
    }

    /**
     * Lấy discount campaigns theo tên (tìm kiếm)
     */
    public List<DiscountCampaign> findByNameContaining(String name) {
        return discountCampaignRepository.findByNameContainingIgnoreCase(name);
    }
}
