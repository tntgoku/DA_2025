package backend.main.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.Model.InventoryItem;
import backend.main.Model.InventoryHistory;
import backend.main.Model.ResponseObject;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.InventoryHistoryRepository;
import backend.main.Repository.VariantReponsitory;
import backend.main.Request.InventoryRequest;
import backend.main.DTO.InventoryDTO;

@Service
public class InventoryService implements BaseService<InventoryItem, Integer> {
    @Autowired
    private InventoryReponsitory reponsitory;

    @Autowired
    private InventoryHistoryRepository historyRepository;

    @Autowired
    private VariantReponsitory variantRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private InventoryDTO convertToDTO(InventoryItem item) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(item.getId());
        dto.setCurrentStock(item.getStock());
        dto.setStatus(item.getStatus());
        dto.setLocation(item.getPosition());
        dto.setLastUpdated(item.getUpdatedAt());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        if (item.getProductVariant() != null) {
            dto.setVariantId(item.getProductVariant().getId());
            dto.setVariantName(item.getProductVariant().getNameVariants());
            dto.setColor(item.getProductVariant().getColor());
            dto.setStorage(item.getProductVariant().getStorage());
            dto.setSku(item.getProductVariant().getSku());
            dto.setPrice(item.getProductVariant().getListPrice());
            dto.setSalePrice(item.getProductVariant().getSalePrice());
            if (item.getProductVariant().getProduct() != null) {
                dto.setProductId(item.getProductVariant().getProduct().getId());
                dto.setProductName(item.getProductVariant().getProduct().getName());
            }
        }

        return dto;
    }

    @Transactional
    public ResponseEntity<ResponseObject> findAll() {
        try {
            List<InventoryItem> listitem = reponsitory.findAll();
            if (listitem == null || listitem.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(204,
                        "Không tìm thấy", 0, null),
                        HttpStatus.NO_CONTENT);
            }

            List<InventoryDTO> dtoList = listitem.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(new ResponseObject(200,
                    "Thành công", 0, dtoList),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error fetching inventory items: {}", e.getMessage());
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi lấy danh sách tồn kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> findItembyId(Integer id) {
        Optional<InventoryItem> optional = reponsitory.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404,
                            "Không thành công",
                            0,
                            "Not found source with ID: " + id),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new ResponseObject(200,
                        "Thành công",
                        0,
                        optional.get()),
                HttpStatus.OK);
    }

    public Optional<InventoryItem> findItembyVariant(Integer id) {
        return reponsitory.findByProductVariant_Id(id);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(InventoryItem entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setImportDate(LocalDateTime.now());
        InventoryItem saved = reponsitory.save(entity);
        if (saved != null && saved.getId() != null) {
            logger.info("Save Successfully : Id: " + saved.getId() + "Name: " + saved.getProductVariant().getId());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Lưu vào kho thất bại : " + "Name: " + saved.getProductVariant().getId());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Tạo mới thất bại", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        Optional<InventoryItem> optional = reponsitory.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Item với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            reponsitory.deleteById(id);
            logger.info("Delete Successfully ID: " + id);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Delete Exception: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ResponseObject> deleteByProductVariantId(Integer id) {
        Optional<InventoryItem> optional = reponsitory.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Item với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            // reponsitory.deleteByProductVariantId(id);
            reponsitory.deleteById(optional.get().getId());
            logger.info("Delete Successfully ID: " + id);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Delete Exception: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(InventoryItem entity) {
        Optional<InventoryItem> optional = reponsitory.findById(entity.getId());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Item với ID: " + entity.getId(), 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            InventoryItem update = reponsitory.save(entity);
            logger.info("Cập nhật thành công ID: " + update.getId() + "\t"
                    + "\t" + update.getProductVariant().getCostPrice().toString()
                    + "\t" + update.getProductVariant().getSalePrice().toString()
                    + "\t" + update.getProductVariant().getListPrice().toString());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Cập nhật Exception: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Cập nhật thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public InventoryItem convertRequestInventory(InventoryRequest request) {
        InventoryItem item = new InventoryItem();
        item.setId(request.getId());
        // item.setProductVariant(request.getProductVariant().parseInt(null));
        item.setCondition(request.getCondition());
        item.setSource(request.getSource());
        item.setImei(request.getImei());
        item.setSerialNumber(request.getSerialNumber());
        item.setStatus(request.getStatus());
        item.setPosition(request.getPosition());
        item.setStock(request.getStock());
        item.setImportDate(request.getImportDate());
        item.setSoldDate(request.getSoldDate());
        item.setWarrantyMonths(request.getWarrantyMonths());
        item.setDeviceConditionNotes(request.getDeviceConditionNotes());
        item.setPreviousOwnerInfo(request.getPreviousOwnerInfo());
        item.setCreatedAt(request.getCreatedAt());
        item.setUpdatedAt(request.getUpdatedAt());
        return item;
    }

    // Create inventory from request
    @Transactional
    public ResponseEntity<ResponseObject> createNewFromRequest(InventoryRequest request) {
        try {
            InventoryItem item = convertRequestInventory(request);
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());

            InventoryItem saved = reponsitory.save(item);
            if (saved != null && saved.getId() != null) {
                logger.info("✅ Created inventory item: ID={}, Variant={}", saved.getId(),
                        saved.getProductVariant().getId());
                return new ResponseEntity<>(
                        new ResponseObject(201, "Tạo mới tồn kho thành công", 0, saved),
                        HttpStatus.CREATED);
            } else {
                logger.error("❌ Failed to create inventory item");
                return new ResponseEntity<>(
                        new ResponseObject(500, "Tạo mới tồn kho thất bại", 1, null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("❌ Error creating inventory: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tạo tồn kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update inventory from request
    @Transactional
    public ResponseEntity<ResponseObject> updateFromRequest(Integer id, InventoryRequest request) {
        try {
            Optional<InventoryItem> optional = reponsitory.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho với ID: " + id, 1, null),
                        HttpStatus.NOT_FOUND);
            }

            InventoryItem existing = optional.get();
            Integer oldStock = existing.getStock();

            // Update fields
            existing.setCondition(request.getCondition());
            existing.setSource(request.getSource());
            existing.setImei(request.getImei());
            existing.setSerialNumber(request.getSerialNumber());
            existing.setStatus(request.getStatus());
            existing.setPosition(request.getPosition());
            existing.setStock(request.getStock());
            existing.setImportDate(request.getImportDate());
            existing.setSoldDate(request.getSoldDate());
            existing.setWarrantyMonths(request.getWarrantyMonths());
            existing.setDeviceConditionNotes(request.getDeviceConditionNotes());
            existing.setPreviousOwnerInfo(request.getPreviousOwnerInfo());
            existing.setUpdatedAt(LocalDateTime.now());

            InventoryItem updated = reponsitory.save(existing);

            // Log stock change if different
            if (!Objects.equals(oldStock, request.getStock())) {
                createStockHistory(existing, InventoryHistory.ActionType.ADJUSTMENT,
                        request.getStock() - oldStock, oldStock, request.getStock(),
                        "Cập nhật thông tin", "Cập nhật từ form");
            }

            logger.info("✅ Updated inventory item: ID={}", updated.getId());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật tồn kho thành công", 0, updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error updating inventory: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi cập nhật tồn kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update stock
    @Transactional
    public ResponseEntity<ResponseObject> updateStock(Integer id, Map<String, Object> stockData) {
        try {
            Optional<InventoryItem> optional = reponsitory.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho với ID: " + id, 1, null),
                        HttpStatus.NOT_FOUND);
            }

            InventoryItem item = optional.get();
            Integer oldStock = item.getStock();
            Integer newStock = (Integer) stockData.get("stock");
            String reason = (String) stockData.getOrDefault("reason", "Điều chỉnh tồn kho");
            String notes = (String) stockData.getOrDefault("notes", "");

            if (newStock == null || newStock < 0) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số lượng tồn kho không hợp lệ", 1, null),
                        HttpStatus.BAD_REQUEST);
            }

            item.setStock(newStock);
            item.setUpdatedAt(LocalDateTime.now());
            reponsitory.save(item);

            // Create history record
            createStockHistory(item, InventoryHistory.ActionType.ADJUSTMENT,
                    newStock - oldStock, oldStock, newStock, reason, notes);

            logger.info("✅ Updated stock for inventory ID={}: {} -> {}", id, oldStock, newStock);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật tồn kho thành công", 0, item),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error updating stock: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi cập nhật tồn kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Import stock
    @Transactional
    public ResponseEntity<ResponseObject> importStock(Integer id, Map<String, Object> importData) {
        try {
            Optional<InventoryItem> optional = reponsitory.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho với ID: " + id, 1, null),
                        HttpStatus.NOT_FOUND);
            }

            InventoryItem item = optional.get();
            Integer oldStock = item.getStock();
            Integer importQuantity = (Integer) importData.get("quantity");
            String reason = (String) importData.getOrDefault("reason", "Nhập kho");
            String notes = (String) importData.getOrDefault("notes", "");

            if (importQuantity == null || importQuantity <= 0) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số lượng nhập kho không hợp lệ", 1, null),
                        HttpStatus.BAD_REQUEST);
            }

            Integer newStock = oldStock + importQuantity;
            item.setStock(newStock);
            item.setUpdatedAt(LocalDateTime.now());
            reponsitory.save(item);

            // Create history record
            createStockHistory(item, InventoryHistory.ActionType.IMPORT,
                    importQuantity, oldStock, newStock, reason, notes);

            logger.info("✅ Imported stock for inventory ID={}: +{} ({} -> {})", id, importQuantity, oldStock, newStock);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Nhập kho thành công", 0, item),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error importing stock: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi nhập kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Export stock
    @Transactional
    public ResponseEntity<ResponseObject> exportStock(Integer id, Map<String, Object> exportData) {
        try {
            Optional<InventoryItem> optional = reponsitory.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho với ID: " + id, 1, null),
                        HttpStatus.NOT_FOUND);
            }

            InventoryItem item = optional.get();
            Integer oldStock = item.getStock();
            Integer exportQuantity = (Integer) exportData.get("quantity");
            String reason = (String) exportData.getOrDefault("reason", "Xuất kho");
            String notes = (String) exportData.getOrDefault("notes", "");

            if (exportQuantity == null || exportQuantity <= 0) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số lượng xuất kho không hợp lệ", 1, null),
                        HttpStatus.BAD_REQUEST);
            }

            if (exportQuantity > oldStock) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "Số lượng xuất kho vượt quá tồn kho hiện có", 1, null),
                        HttpStatus.BAD_REQUEST);
            }

            Integer newStock = oldStock - exportQuantity;
            item.setStock(newStock);
            item.setUpdatedAt(LocalDateTime.now());
            reponsitory.save(item);

            // Create history record
            createStockHistory(item, InventoryHistory.ActionType.EXPORT,
                    -exportQuantity, oldStock, newStock, reason, notes);

            logger.info("✅ Exported stock for inventory ID={}: -{} ({} -> {})", id, exportQuantity, oldStock, newStock);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xuất kho thành công", 0, item),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error exporting stock: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi xuất kho: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get inventory report
    public ResponseEntity<ResponseObject> getInventoryReport(Map<String, String> filters) {
        try {
            List<InventoryItem> allItems = reponsitory.findAll();
            Map<String, Object> report = new HashMap<>();
            report.put("totalProducts", allItems.size());
            report.put("totalStock",
                    allItems.stream().mapToInt(item -> item.getStock() != null ? item.getStock() : 0).sum());
            report.put("lowStockCount", allItems.stream()
                    .mapToInt(item -> (item.getStock() != null && item.getStock() <= 10) ? 1 : 0).sum());
            report.put("outOfStockCount", allItems.stream()
                    .mapToInt(item -> (item.getStock() == null || item.getStock() == 0) ? 1 : 0).sum());
            // Calculate total value (simplified)
            double totalValue = allItems.stream()
                    .filter(item -> item.getProductVariant() != null && item.getProductVariant().getSalePrice() != null)
                    .mapToDouble(item -> item.getProductVariant().getSalePrice().doubleValue() *
                            (item.getStock() != null ? item.getStock() : 0))
                    .sum();
            report.put("totalValue", totalValue);
            logger.info("✅ Generated inventory report: {} products, {} total stock",
                    report.get("totalProducts"), report.get("totalStock"));
            return new ResponseEntity<>(
                    new ResponseObject(200, "Báo cáo tồn kho", 0, report),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error generating inventory report: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tạo báo cáo: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get low stock products
    public ResponseEntity<ResponseObject> getLowStockProducts(Integer threshold) {
        try {
            List<InventoryItem> lowStockItems = reponsitory.findAll().stream()
                    .filter(item -> item.getStock() != null && item.getStock() <= threshold && item.getStock() > 0)
                    .collect(Collectors.toList());

            logger.info("✅ Found {} low stock products (threshold: {})", lowStockItems.size(), threshold);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Sản phẩm sắp hết hàng", 0, lowStockItems),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error getting low stock products: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy sản phẩm sắp hết hàng: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get out of stock products
    public ResponseEntity<ResponseObject> getOutOfStockProducts() {
        try {
            List<InventoryItem> outOfStockItems = reponsitory.findAll().stream()
                    .filter(item -> item.getStock() == null || item.getStock() == 0)
                    .collect(Collectors.toList());

            logger.info("✅ Found {} out of stock products", outOfStockItems.size());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Sản phẩm hết hàng", 0, outOfStockItems),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error getting out of stock products: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy sản phẩm hết hàng: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get inventory stats
    public ResponseEntity<ResponseObject> getInventoryStats() {
        return getInventoryReport(new HashMap<>());
    }

    // Get inventory history
    public ResponseEntity<ResponseObject> getInventoryHistory(Integer id) {
        try {
            List<InventoryHistory> history = historyRepository.findByInventoryItemIdOrderByPerformedAtDesc(id);

            logger.info("✅ Retrieved {} history records for inventory ID={}", history.size(), id);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Lịch sử tồn kho", 0, history),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error getting inventory history: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy lịch sử: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search inventory
    public ResponseEntity<ResponseObject> searchInventory(String query, String status, String stockStatus,
            Integer minStock, Integer maxStock) {
        try {
            List<InventoryItem> allItems = reponsitory.findAll();

            List<InventoryItem> filteredItems = allItems.stream()
                    .filter(item -> {
                        // Query filter
                        if (query != null && !query.trim().isEmpty()) {
                            String searchQuery = query.toLowerCase();
                            boolean matchesQuery = false;

                            if (item.getProductVariant() != null) {
                                if (item.getProductVariant().getProduct() != null &&
                                        item.getProductVariant().getProduct().getName() != null &&
                                        item.getProductVariant().getProduct().getName().toLowerCase()
                                                .contains(searchQuery)) {
                                    matchesQuery = true;
                                }
                                if (item.getProductVariant().getSku() != null &&
                                        item.getProductVariant().getSku().toLowerCase().contains(searchQuery)) {
                                    matchesQuery = true;
                                }
                            }
                            if (!matchesQuery)
                                return false;
                        }

                        // Status filter
                        if (status != null && !status.equals("all")) {
                            if (!status.equals(item.getStatus()))
                                return false;
                        }

                        // Stock status filter
                        if (stockStatus != null && !stockStatus.equals("all")) {
                            Integer stock = item.getStock() != null ? item.getStock() : 0;
                            switch (stockStatus) {
                                case "in-stock":
                                    if (stock <= 0)
                                        return false;
                                    break;
                                case "low-stock":
                                    if (stock <= 0 || stock > 10)
                                        return false;
                                    break;
                                case "out-of-stock":
                                    if (stock > 0)
                                        return false;
                                    break;
                            }
                        }

                        // Stock range filter
                        Integer stock = item.getStock() != null ? item.getStock() : 0;
                        if (minStock != null && stock < minStock)
                            return false;
                        if (maxStock != null && stock > maxStock)
                            return false;

                        return true;
                    })
                    .collect(Collectors.toList());

            logger.info("✅ Search returned {} results for query: {}", filteredItems.size(), query);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Kết quả tìm kiếm", 0, filteredItems),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error searching inventory: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tìm kiếm: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Find by variant ID
    public ResponseEntity<ResponseObject> findByVariantId(Integer variantId) {
        try {
            Optional<InventoryItem> item = reponsitory.findByProductVariant_Id(variantId);
            if (item.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(200, "Tìm thấy tồn kho", 0, item.get()),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy tồn kho cho biến thể này", 1, null),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("❌ Error finding inventory by variant: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tìm kiếm: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Find by product ID
    public ResponseEntity<ResponseObject> findByProductId(Integer productId) {
        try {
            List<InventoryItem> items = reponsitory.findAll().stream()
                    .filter(item -> item.getProductVariant() != null &&
                            item.getProductVariant().getProduct() != null &&
                            item.getProductVariant().getProduct().getId().equals(productId))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Tồn kho theo sản phẩm", 0, items),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error finding inventory by product: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tìm kiếm: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to create stock history
    private void createStockHistory(InventoryItem item, InventoryHistory.ActionType actionType,
            Integer quantityChange, Integer quantityBefore, Integer quantityAfter,
            String reason, String notes) {
        try {
            InventoryHistory history = new InventoryHistory(item, actionType, quantityChange, quantityBefore,
                    quantityAfter);
            history.setReason(reason);
            history.setNotes(notes);
            history.setPerformedBy("system"); // TODO: Get from security context
            history.setPerformedAt(LocalDateTime.now());

            historyRepository.save(history);
            logger.info("✅ Created stock history: {} {} for inventory ID={}",
                    actionType.getDescription(), quantityChange, item.getId());
        } catch (Exception e) {
            logger.error("❌ Error creating stock history: {}", e.getMessage());
        }
    }
}
