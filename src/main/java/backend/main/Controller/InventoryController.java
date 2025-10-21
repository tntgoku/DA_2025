package backend.main.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import backend.main.Model.ResponseObject;
import backend.main.Service.InventoryService;
import backend.main.Request.InventoryRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // ========== BASIC CRUD OPERATIONS ==========

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllInventory() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getInventoryById(@PathVariable Integer id) {
        return service.findItembyId(id);
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createInventory(@RequestBody InventoryRequest request) {
        return service.createNewFromRequest(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateInventory(@PathVariable Integer id, @RequestBody InventoryRequest request) {
        return service.updateFromRequest(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteInventory(@PathVariable Integer id) {
        return service.delete(id);
    }

    // ========== STOCK MANAGEMENT ==========

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ResponseObject> updateStock(@PathVariable Integer id, @RequestBody Map<String, Object> stockData) {
        return service.updateStock(id, stockData);
    }

    @PostMapping("/{id}/import")
    public ResponseEntity<ResponseObject> importStock(@PathVariable Integer id, @RequestBody Map<String, Object> importData) {
        return service.importStock(id, importData);
    }

    @PostMapping("/{id}/export")
    public ResponseEntity<ResponseObject> exportStock(@PathVariable Integer id, @RequestBody Map<String, Object> exportData) {
        return service.exportStock(id, exportData);
    }

    // ========== REPORTS AND ANALYTICS ==========

    @GetMapping("/report")
    public ResponseEntity<ResponseObject> getInventoryReport(@RequestParam(required = false) Map<String, String> filters) {
        return service.getInventoryReport(filters);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ResponseObject> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        return service.getLowStockProducts(threshold);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<ResponseObject> getOutOfStockProducts() {
        return service.getOutOfStockProducts();
    }

    @GetMapping("/stats")
    public ResponseEntity<ResponseObject> getInventoryStats() {
        return service.getInventoryStats();
    }


    // ========== SEARCH AND FILTER ==========

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchInventory(@RequestParam(required = false) String query,
                                                          @RequestParam(required = false) String status,
                                                          @RequestParam(required = false) String stockStatus,
                                                          @RequestParam(required = false) Integer minStock,
                                                          @RequestParam(required = false) Integer maxStock) {
        return service.searchInventory(query, status, stockStatus, minStock, maxStock);
    }

    @GetMapping("/by-variant/{variantId}")
    public ResponseEntity<ResponseObject> getInventoryByVariant(@PathVariable Integer variantId) {
        return service.findByVariantId(variantId);
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ResponseObject> getInventoryByProduct(@PathVariable Integer productId) {
        return service.findByProductId(productId);
    }

}
