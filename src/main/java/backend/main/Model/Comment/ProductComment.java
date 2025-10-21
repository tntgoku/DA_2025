package backend.main.Model.Comment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

import backend.main.Model.BaseEntity;
import backend.main.Model.Product.Products;
import backend.main.Model.User.User;

@Entity
@Table(name = "product_comments", schema = "dbo")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;

    @Column(name = "rating")
    private Integer rating; // 1-5 stars

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Column(name = "parent_comment_id")
    private Integer parentCommentId; // For replies

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    @Column(name = "dislikes_count")
    private Integer dislikesCount = 0;

    @Column(name = "reported_count")
    private Integer reportedCount = 0;

    @Column(name = "admin_response", columnDefinition = "NVARCHAR(MAX)")
    private String adminResponse;

    @Column(name = "admin_response_at")
    private LocalDateTime adminResponseAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "NVARCHAR(500)")
    private String userAgent;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public ProductComment() {
        super();
    }

    public ProductComment(Products product, User user, String content) {
        super();
        this.product = product;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ProductComment(Products product, User user, String content, Integer rating) {
        super();
        this.product = product;
        this.user = user;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isReply() {
        return parentCommentId != null;
    }

    public void incrementLikes() {
        this.likesCount = (this.likesCount == null ? 0 : this.likesCount) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementDislikes() {
        this.dislikesCount = (this.dislikesCount == null ? 0 : this.dislikesCount) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementReports() {
        this.reportedCount = (this.reportedCount == null ? 0 : this.reportedCount) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void approve() {
        this.isApproved = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.isApproved = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void addAdminResponse(String response) {
        this.adminResponse = response;
        this.adminResponseAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
