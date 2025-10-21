package backend.main.Service;

import backend.main.DTO.Comment.ProductCommentDTO;
import backend.main.Model.Comment.ProductComment;
import backend.main.Model.Product.Products;
import backend.main.Model.User.User;
import backend.main.Repository.CommentRepository;
import backend.main.Repository.ProductRepository;
import backend.main.Repository.UserRepository;
import backend.main.Model.ResponseObject;
import backend.main.Service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Create new comment
    public ResponseEntity<ResponseObject> createComment(ProductCommentDTO commentDTO, String ipAddress, String userAgent) {
        try {
            // Validate product exists
            Optional<Products> productOpt = productRepository.findById(commentDTO.getProductId());
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Sản phẩm không tồn tại", 1, null)
                );
            }

            // Validate user exists
            Optional<User> userOpt = userRepository.findById(commentDTO.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Người dùng không tồn tại", 1, null)
                );
            }

            // Check if user already commented on this product
            if (commentRepository.existsByProductIdAndUserId(commentDTO.getProductId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(400, "Bạn đã bình luận về sản phẩm này rồi", 1, null)
                );
            }

            // Create new comment
            ProductComment comment = new ProductComment();
            comment.setProduct(productOpt.get());
            comment.setUser(userOpt.get());
            comment.setContent(commentDTO.getContent());
            comment.setRating(commentDTO.getRating());
            comment.setIsAnonymous(commentDTO.getIsAnonymous() != null ? commentDTO.getIsAnonymous() : false);
            comment.setParentCommentId(commentDTO.getParentCommentId());
            comment.setIpAddress(ipAddress);
            comment.setUserAgent(userAgent);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());

            // Auto-approve if rating is provided (for now)
            comment.setIsApproved(commentDTO.getRating() != null);

            ProductComment savedComment = commentRepository.save(comment);
            logger.info("Created new comment: ID={}, Product={}, User={}", 
                       savedComment.getId(), commentDTO.getProductId(), commentDTO.getUserId());

            // Send notification email to admin
            try {
                String productUrl = "http://localhost:5173/product/" + commentDTO.getProductId();
                emailService.sendNewCommentNotificationEmail(
                    "admin@truonglcd.com", // Admin email - should be configurable
                    productOpt.get().getName(),
                    userOpt.get().getFullName() != null ? userOpt.get().getFullName() : userOpt.get().getEmail(),
                    commentDTO.getContent(),
                    productUrl
                );
                logger.info("Notification email sent to admin for new comment");
            } catch (Exception e) {
                logger.error("Failed to send notification email: {}", e.getMessage());
                // Don't fail the comment creation if email fails
            }

            return ResponseEntity.ok(
                new ResponseObject(201, "Bình luận đã được tạo thành công", 0, convertToDTO(savedComment))
            );

        } catch (Exception e) {
            logger.error("Error creating comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi tạo bình luận", 1, e.getMessage())
            );
        }
    }

    // Get comments by product ID
    public ResponseEntity<ResponseObject> getCommentsByProduct(Integer productId, int page, int size, boolean approvedOnly) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductComment> comments;

            if (approvedOnly) {
                comments = commentRepository.findApprovedByProductId(productId, pageable);
            } else {
                comments = commentRepository.findByProductIdAndParentCommentIdIsNull(productId, pageable);
            }

            List<ProductCommentDTO> commentDTOs = comments.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

            // Add replies for each comment
            for (ProductCommentDTO dto : commentDTOs) {
                List<ProductComment> replies = commentRepository.findRepliesByParentCommentId(dto.getId());
                dto.setReplies(replies.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
            }

            return ResponseEntity.ok(
                new ResponseObject(200, "Lấy danh sách bình luận thành công", 0, commentDTOs)
            );

        } catch (Exception e) {
            logger.error("Error getting comments by product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi lấy danh sách bình luận", 1, e.getMessage())
            );
        }
    }

    // Get pending comments (for admin)
    public ResponseEntity<ResponseObject> getPendingComments(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductComment> comments = commentRepository.findPendingComments(pageable);

            List<ProductCommentDTO> commentDTOs = comments.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(
                new ResponseObject(200, "Lấy danh sách bình luận chờ duyệt thành công", 0, commentDTOs)
            );

        } catch (Exception e) {
            logger.error("Error getting pending comments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi lấy danh sách bình luận chờ duyệt", 1, e.getMessage())
            );
        }
    }

    // Approve comment
    public ResponseEntity<ResponseObject> approveComment(Integer commentId) {
        try {
            Optional<ProductComment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Bình luận không tồn tại", 1, null)
                );
            }

            ProductComment comment = commentOpt.get();
            comment.setIsApproved(true);
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

            logger.info("Approved comment: ID={}", commentId);

            return ResponseEntity.ok(
                new ResponseObject(200, "Bình luận đã được duyệt", 0, convertToDTO(comment))
            );

        } catch (Exception e) {
            logger.error("Error approving comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi duyệt bình luận", 1, e.getMessage())
            );
        }
    }

    // Reject comment
    public ResponseEntity<ResponseObject> rejectComment(Integer commentId) {
        try {
            Optional<ProductComment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Bình luận không tồn tại", 1, null)
                );
            }

            ProductComment comment = commentOpt.get();
            comment.setIsApproved(false);
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

            logger.info("Rejected comment: ID={}", commentId);

            return ResponseEntity.ok(
                new ResponseObject(200, "Bình luận đã bị từ chối", 0, convertToDTO(comment))
            );

        } catch (Exception e) {
            logger.error("Error rejecting comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi từ chối bình luận", 1, e.getMessage())
            );
        }
    }

    // Add admin response
    public ResponseEntity<ResponseObject> addAdminResponse(Integer commentId, String response) {
        try {
            Optional<ProductComment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Bình luận không tồn tại", 1, null)
                );
            }

            ProductComment comment = commentOpt.get();
            comment.setAdminResponse(response);
            comment.setAdminResponseAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

            logger.info("Added admin response to comment: ID={}", commentId);

            return ResponseEntity.ok(
                new ResponseObject(200, "Phản hồi admin đã được thêm", 0, convertToDTO(comment))
            );

        } catch (Exception e) {
            logger.error("Error adding admin response: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi thêm phản hồi admin", 1, e.getMessage())
            );
        }
    }

    // Delete comment
    public ResponseEntity<ResponseObject> deleteComment(Integer commentId) {
        try {
            Optional<ProductComment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(404, "Bình luận không tồn tại", 1, null)
                );
            }

            commentRepository.deleteById(commentId);
            logger.info("Deleted comment: ID={}", commentId);

            return ResponseEntity.ok(
                new ResponseObject(200, "Bình luận đã được xóa", 0, null)
            );

        } catch (Exception e) {
            logger.error("Error deleting comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi xóa bình luận", 1, e.getMessage())
            );
        }
    }

    // Get comment statistics
    public ResponseEntity<ResponseObject> getCommentStatistics() {
        try {
            Long totalComments = commentRepository.count();
            Long approvedComments = commentRepository.countApprovedComments();
            Long pendingComments = commentRepository.countPendingComments();
            Long reportedComments = commentRepository.countReportedComments();

            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("totalComments", totalComments);
            stats.put("approvedComments", approvedComments);
            stats.put("pendingComments", pendingComments);
            stats.put("reportedComments", reportedComments);

            return ResponseEntity.ok(
                new ResponseObject(200, "Lấy thống kê bình luận thành công", 0, stats)
            );

        } catch (Exception e) {
            logger.error("Error getting comment statistics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject(500, "Lỗi khi lấy thống kê bình luận", 1, e.getMessage())
            );
        }
    }

    // Convert entity to DTO
    private ProductCommentDTO convertToDTO(ProductComment comment) {
        ProductCommentDTO dto = new ProductCommentDTO();
        dto.setId(comment.getId());
        if (comment.getProduct() != null) {
            dto.setProductId(comment.getProduct().getId());
            dto.setProductName(comment.getProduct().getName());
        }
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getId());
            dto.setUserName(comment.getUser().getFullName());
            dto.setUserEmail(comment.getUser().getEmail());
        }
        dto.setContent(comment.getContent());
        dto.setRating(comment.getRating());
        dto.setIsApproved(comment.getIsApproved());
        dto.setIsAnonymous(comment.getIsAnonymous());
        dto.setParentCommentId(comment.getParentCommentId());
        dto.setLikesCount(comment.getLikesCount());
        dto.setDislikesCount(comment.getDislikesCount());
        dto.setReportedCount(comment.getReportedCount());
        dto.setAdminResponse(comment.getAdminResponse());
        dto.setAdminResponseAt(comment.getAdminResponseAt());
        dto.setIpAddress(comment.getIpAddress());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
}
