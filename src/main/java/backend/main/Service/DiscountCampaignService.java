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

@Service
public class DiscountCampaignService implements BaseService<DiscountCampaign, Integer> {

    @Autowired
    private DiscountCampaignRepository discountCampaignRepository;


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

    public Optional<DiscountCampaign> findById(Integer id) {
        return discountCampaignRepository.findById(id);
    }

    public List<DiscountCampaign> findByCampaignType(String campaignType) {
        return discountCampaignRepository.findByCampaignType(campaignType);
    }

    public DiscountCampaign save(DiscountCampaign discountCampaign) {
        if (discountCampaign.getCreatedAt() == null) {
            discountCampaign.setCreatedAt(LocalDateTime.now());
        }
        discountCampaign.setUpdatedAt(LocalDateTime.now());
        return discountCampaignRepository.save(discountCampaign);
    }

    public void deleteById(Integer id) {
        discountCampaignRepository.deleteById(id);
    }

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

    public List<DiscountCampaign> findActiveCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return discountCampaignRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(now, now);
    }

    public List<DiscountCampaign> findUpcomingCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return discountCampaignRepository.findByStartDateAfter(now);
    }


    public List<DiscountCampaign> findEndingSoonCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekFromNow = now.plusWeeks(1);
        return discountCampaignRepository.findByEndDateBetween(now, oneWeekFromNow);
    }

    public List<DiscountCampaign> findByPriority(Integer priority) {
        return discountCampaignRepository.findByPriority(priority);
    }

    public List<DiscountCampaign> findByPriorityBetween(Integer minPriority, Integer maxPriority) {
        return discountCampaignRepository.findByPriorityBetween(minPriority, maxPriority);
    }

    public boolean isActive(DiscountCampaign campaign) {
        LocalDateTime now = LocalDateTime.now();
        return campaign.getIsActive() && 
               campaign.getStartDate().isBefore(now) && 
               campaign.getEndDate().isAfter(now);
    }

    public List<DiscountCampaign> findByNameContaining(String name) {
        return discountCampaignRepository.findByNameContainingIgnoreCase(name);
    }
}
