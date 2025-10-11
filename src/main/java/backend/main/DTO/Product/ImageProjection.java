package backend.main.DTO.Product;

public interface ImageProjection {
    Integer getid();

    String getname();

    String getProductType();

    Boolean getIsPrimary();

    Integer getProductId();

    Integer getVariantId();

    String getImageUrl();

    String getAltImg();

    Integer getDisplayOrder();

}
