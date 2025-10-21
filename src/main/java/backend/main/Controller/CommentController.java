package backend.main.Controller;

import backend.main.Config.LoggerE;
import backend.main.DTO.Comment.ProductCommentDTO;
import backend.main.Model.ResponseObject;
import backend.main.Service.CommentService;
import backend.main.Request.CommentRequest;
import backend.main.Request.AdminResponseRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerE.getLogger();

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseObject> createComment(
            @RequestBody CommentRequest commentRequest,
            HttpServletRequest request) {
        
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        logger.info("Creating comment for product: {}, user: {}", 
                   commentRequest.getProductId(), commentRequest.getUserId());
        
        // Convert CommentRequest to ProductCommentDTO
        ProductCommentDTO commentDTO = new ProductCommentDTO();
        commentDTO.setProductId(commentRequest.getProductId());
        commentDTO.setUserId(commentRequest.getUserId());
        commentDTO.setContent(commentRequest.getContent());
        commentDTO.setRating(commentRequest.getRating());
        commentDTO.setIsAnonymous(commentRequest.getIsAnonymous());
        commentDTO.setParentCommentId(commentRequest.getParentCommentId());
        
        return commentService.createComment(commentDTO, ipAddress, userAgent);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseObject> getCommentsByProduct(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean approvedOnly) {
        
        logger.info("Getting comments for product: {}, page: {}, size: {}", productId, page, size);
        
        return commentService.getCommentsByProduct(productId, page, size, approvedOnly);
    }

    @GetMapping("/pending")
    public ResponseEntity<ResponseObject> getPendingComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.info("Getting pending comments, page: {}, size: {}", page, size);
        
        return commentService.getPendingComments(page, size);
    }

    @PutMapping("/{commentId}/approve")
    public ResponseEntity<ResponseObject> approveComment(@PathVariable Integer commentId) {
        logger.info("Approving comment: {}", commentId);
        
        return commentService.approveComment(commentId);
    }

    @PutMapping("/{commentId}/reject")
    public ResponseEntity<ResponseObject> rejectComment(@PathVariable Integer commentId) {
        logger.info("Rejecting comment: {}", commentId);
        
        return commentService.rejectComment(commentId);
    }

    @PutMapping("/{commentId}/admin-response")
    public ResponseEntity<ResponseObject> addAdminResponse(
            @PathVariable Integer commentId,
            @RequestBody AdminResponseRequest adminResponseRequest) {
        
        logger.info("Adding admin response to comment: {}", commentId);
        
        return commentService.addAdminResponse(commentId, adminResponseRequest.getResponse());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable Integer commentId) {
        logger.info("Deleting comment: {}", commentId);
        
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/statistics")
    public ResponseEntity<ResponseObject> getCommentStatistics() {
        logger.info("Getting comment statistics");
        
        return commentService.getCommentStatistics();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
