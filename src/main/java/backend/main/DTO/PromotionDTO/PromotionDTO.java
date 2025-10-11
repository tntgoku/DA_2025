package backend.main.DTO.PromotionDTO;

public interface PromotionDTO {
    Integer getDiscountTargetId();

    String getTargetType();

    Double getValue();

    Boolean getIsActive();

    String getDiscountType();

    Integer getProductId();

    String getProductName();
}
