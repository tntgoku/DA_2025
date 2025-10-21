package backend.main.Repository;

import backend.main.Model.Comment.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends BaseRepository<ProductComment, Integer> {

    // Find comments by product ID
    @Query("SELECT c FROM ProductComment c WHERE c.product.id = :productId AND c.parentCommentId IS NULL ORDER BY c.createdAt DESC")
    Page<ProductComment> findByProductIdAndParentCommentIdIsNull(@Param("productId") Integer productId, Pageable pageable);

    // Find all comments by product ID (including replies)
    @Query("SELECT c FROM ProductComment c WHERE c.product.id = :productId ORDER BY c.createdAt DESC")
    List<ProductComment> findAllByProductId(@Param("productId") Integer productId);

    // Find approved comments by product ID
    @Query("SELECT c FROM ProductComment c WHERE c.product.id = :productId AND c.isApproved = true AND c.parentCommentId IS NULL ORDER BY c.createdAt DESC")
    Page<ProductComment> findApprovedByProductId(@Param("productId") Integer productId, Pageable pageable);

    // Find comments by user ID
    @Query("SELECT c FROM ProductComment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    Page<ProductComment> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    // Find replies for a specific comment
    @Query("SELECT c FROM ProductComment c WHERE c.parentCommentId = :parentCommentId ORDER BY c.createdAt ASC")
    List<ProductComment> findRepliesByParentCommentId(@Param("parentCommentId") Integer parentCommentId);

    // Find pending comments (not approved)
    @Query("SELECT c FROM ProductComment c WHERE c.isApproved = false ORDER BY c.createdAt DESC")
    Page<ProductComment> findPendingComments(Pageable pageable);

    // Find reported comments
    @Query("SELECT c FROM ProductComment c WHERE c.reportedCount > 0 ORDER BY c.reportedCount DESC, c.createdAt DESC")
    Page<ProductComment> findReportedComments(Pageable pageable);

    // Find comments by rating
    @Query("SELECT c FROM ProductComment c WHERE c.product.id = :productId AND c.rating = :rating AND c.isApproved = true ORDER BY c.createdAt DESC")
    List<ProductComment> findByProductIdAndRating(@Param("productId") Integer productId, @Param("rating") Integer rating);

    // Count comments by product ID
    @Query("SELECT COUNT(c) FROM ProductComment c WHERE c.product.id = :productId AND c.isApproved = true")
    Long countApprovedByProductId(@Param("productId") Integer productId);

    // Count comments by user ID
    @Query("SELECT COUNT(c) FROM ProductComment c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Integer userId);

    // Get average rating for a product
    @Query("SELECT AVG(c.rating) FROM ProductComment c WHERE c.product.id = :productId AND c.isApproved = true AND c.rating IS NOT NULL")
    Double getAverageRatingByProductId(@Param("productId") Integer productId);

    // Find comments created between dates
    @Query("SELECT c FROM ProductComment c WHERE c.createdAt BETWEEN :startDate AND :endDate ORDER BY c.createdAt DESC")
    List<ProductComment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find comments by content (search)
    @Query("SELECT c FROM ProductComment c WHERE c.content LIKE %:keyword% AND c.isApproved = true ORDER BY c.createdAt DESC")
    Page<ProductComment> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);

    // Find comments with admin response
    @Query("SELECT c FROM ProductComment c WHERE c.adminResponse IS NOT NULL ORDER BY c.adminResponseAt DESC")
    Page<ProductComment> findCommentsWithAdminResponse(Pageable pageable);

    // Find comments without admin response
    @Query("SELECT c FROM ProductComment c WHERE c.adminResponse IS NULL AND c.isApproved = true ORDER BY c.createdAt DESC")
    Page<ProductComment> findCommentsWithoutAdminResponse(Pageable pageable);

    // Find recent comments (last 24 hours)
    @Query("SELECT c FROM ProductComment c WHERE c.createdAt >= :since ORDER BY c.createdAt DESC")
    List<ProductComment> findRecentComments(@Param("since") LocalDateTime since);

    // Find comments by IP address
    @Query("SELECT c FROM ProductComment c WHERE c.ipAddress = :ipAddress ORDER BY c.createdAt DESC")
    List<ProductComment> findByIpAddress(@Param("ipAddress") String ipAddress);

    // Find comments by product and user
    @Query("SELECT c FROM ProductComment c WHERE c.product.id = :productId AND c.user.id = :userId ORDER BY c.createdAt DESC")
    List<ProductComment> findByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    // Check if user has commented on product
    @Query("SELECT COUNT(c) > 0 FROM ProductComment c WHERE c.product.id = :productId AND c.user.id = :userId")
    boolean existsByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    // Get comment statistics
    @Query("SELECT COUNT(c) FROM ProductComment c WHERE c.isApproved = true")
    Long countApprovedComments();

    @Query("SELECT COUNT(c) FROM ProductComment c WHERE c.isApproved = false")
    Long countPendingComments();

    @Query("SELECT COUNT(c) FROM ProductComment c WHERE c.reportedCount > 0")
    Long countReportedComments();

    // Find top rated products by average rating
    @Query("SELECT c.product.id, AVG(c.rating) as avgRating, COUNT(c) as commentCount " +
           "FROM ProductComment c WHERE c.isApproved = true AND c.rating IS NOT NULL " +
           "GROUP BY c.product.id HAVING COUNT(c) >= :minComments " +
           "ORDER BY avgRating DESC")
    List<Object[]> findTopRatedProducts(@Param("minComments") Long minComments, Pageable pageable);
}
