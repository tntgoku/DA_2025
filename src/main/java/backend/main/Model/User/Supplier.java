// package backend.main.Model.User;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;
// import java.util.*;

// import backend.main.Model.Product.ProductSource;

// @Entity
// @Table(name = "suppliers")
// public class Supplier {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     private String name;
//     private String phone;
//     private String email;
//     private String address;
//     private String supplierType;
//     private String taxCode;
//     private Integer reliabilityRating;
//     private String paymentTerms;
//     private Boolean isActive;
//     private LocalDateTime createdAt;
//     @OneToMany(mappedBy = "supplier")
//     private List<ProductSource> productSources;
// }
