package backend.main.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.InventoryItem;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryReponsitory extends BaseRepository<InventoryItem, Integer> {
    Optional<InventoryItem> findByProductVariant_Id(Integer id);


    @Transactional // Bắt buộc cho mọi thao tác Cập nhật/Xóa
    @Modifying // Báo hiệu đây là truy vấn sửa đổi
    @Query("DELETE FROM InventoryItem i WHERE i.productVariant.id = :variantId")
    int deleteByProductVariantId(@Param("variantId") Integer variantId);
}
