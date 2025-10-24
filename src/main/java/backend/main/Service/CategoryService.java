package backend.main.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.Model.Categories;
import backend.main.Model.ResponseObject;
import backend.main.Repository.CategoryRepository;
import backend.main.DTO.CategoryDTO;

@Service
public class CategoryService implements BaseService<Categories, Integer> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepository repository;

    public ResponseEntity<ResponseObject> findAll() {
        List<CategoryDTO> listroots = new ArrayList<>();
        List<Categories> roots = repository.findAll();
        listroots = convCategoryDTOs(roots);
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, listroots),
                HttpStatus.OK);
    }

    public List<CategoryDTO> convCategoryDTOs(List<Categories> roots) {
        List<CategoryDTO> listroots = new ArrayList<>();
        roots.forEach(item -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setDisplayOrder(item.getDisplayOrder());
            dto.setSlug(item.getSlug());
            dto.setIsActive(item.getIsActive());
            if (item.getParent() == null) {
                listroots.add(dto);
            }
        });

        roots.forEach(item -> {
            if (item.getParent() != null) {
                CategoryDTO dto = new CategoryDTO();
                dto.setId(item.getId());
                dto.setName(item.getName());
                dto.setDisplayOrder(item.getDisplayOrder());
                dto.setParentId(item.getParent());
                dto.setSlug(item.getSlug());
                listroots.forEach(rootParent -> {
                    if (rootParent.getId() == dto.getParentId()) {
                        rootParent.getParents().add(dto);
                    }
                });
            }
        });

        return listroots;
    }

    public ResponseEntity<ResponseObject> getById(Integer id) {
        Optional<Categories> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(404,
                    "Không thành công", 0, "not Found source have ID: " + id),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, optional.get()),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> getByParentId(Integer parentId) {
        List<Categories> categories = repository.findByParent(parentId);
        if (categories == null && categories.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục con với Parent ID: " + parentId, 1, null),
                    HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(
                new ResponseObject(200, "Thành công", 0, categories),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Categories entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        if (entity.getSlug() == null || entity.getSlug().isEmpty()) {
            String baseSlug = Engine.makeSlug(entity.getName());
            String slug = baseSlug;
            int counter = 1;
            while (repository.existsBySlug(slug)) {
                slug = baseSlug + "-" + counter++;
            }
            entity.setSlug(slug);
        }
        Categories saved = repository.save(entity);
        if (saved != null && saved.getId() != null) {

            logger.info("Save Successfully : Id: {}, Name: {}", saved.getId(), saved.getName());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Save Failed : Name: {}", saved.getName());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Tạo mới thất bại", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        Optional<Categories> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            repository.deleteById(id);
            logger.info("Delete Successfully ID: {}", id);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa danh mục thành công!", 0, null),
                    HttpStatus.OK);

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // lỗi vi phạm ràng buộc khóa ngoại
            logger.warn("Delete constraint violation for category ID {}: {}", id, ex.getMessage());

            return new ResponseEntity<>(
                    new ResponseObject(
                            409,
                            "Không thể xóa danh mục này vì đang được sử dụng trong dịch vụ hoặc sản phẩm khác.",
                            1,
                            null),
                    HttpStatus.CONFLICT);

        } catch (Exception ex) {
            // lỗi khác không xác định
            logger.error("Delete Exception for category ID {}: {}", id, ex.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(
                            500,
                            "Đã xảy ra lỗi trong quá trình xóa danh mục: " + ex.getMessage(),
                            1,
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(Categories entity) {
        if (entity.getId() == null) {
            return new ResponseEntity<>(
                    new ResponseObject(400, "ID không được để trống", 1, null),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Categories> optional = repository.findById(entity.getId());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + entity.getId(),
                            1, null),
                    HttpStatus.NOT_FOUND);
        }
        try {
            Categories updated = repository.save(entity);
            logger.info("Update Successfully: {}", updated.toString());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành công", 0, updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Update Exception: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Cập nhật thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> deletes(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                if (repository.existsById(id)) { // kiểm tra tồn tại trước khi xóa
                    repository.deleteById(id);
                    logger.info("Delete Successfully ID: {}", id);
                } else {
                    logger.warn("ID not found: {}", id);
                }
            }
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

    public ResponseEntity<ResponseObject> getBySlug(String slug) {
        Optional<Categories> optional = repository.findBySlug(slug);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với slug: " + slug, 0, null),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new ResponseObject(200, "Thành công", 0, optional.get()),
                HttpStatus.OK);
    }

    // SEO-friendly methods
    public ResponseEntity<ResponseObject> getCategoryHierarchy() {
        try {
            List<Categories> allCategories = repository.findAll();
            List<CategoryDTO> hierarchy = buildCategoryHierarchy(allCategories);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, hierarchy),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting category hierarchy: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy cây danh mục: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getBreadcrumbBySlug(String slug) {
        try {
            Optional<Categories> categoryOpt = repository.findBySlug(slug);
            if (!categoryOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục với slug: " + slug, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            List<CategoryDTO> breadcrumb = buildBreadcrumb(categoryOpt.get());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, breadcrumb),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting breadcrumb: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy breadcrumb: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getCategoriesByParent(Integer parentId) {
        try {
            List<Categories> categories = repository.findByParent(parentId);
            List<CategoryDTO> dtos = categories.stream()
                    .map(this::convertToDTO)
                    .collect(java.util.stream.Collectors.toList());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, dtos),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting categories by parent: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy danh mục con: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getCategoryBySeoUrl(String parentSlug, String childSlug) {
        try {
            // Tìm parent category
            Optional<Categories> parentOpt = repository.findBySlug(parentSlug);
            if (!parentOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục cha với slug: " + parentSlug, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            // Tìm child category
            Optional<Categories> childOpt = repository.findBySlugAndParent(childSlug, parentOpt.get().getId());
            if (!childOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục con với slug: " + childSlug, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            CategoryDTO result = convertToDTO(childOpt.get());
            result.setParentCategory(convertToDTO(parentOpt.get()));

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting category by SEO URL: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy danh mục theo SEO URL: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getCategoryByParentSlug(String parentSlug) {
        try {
            Optional<Categories> parentOpt = repository.findBySlug(parentSlug);
            if (!parentOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục với slug: " + parentSlug, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            CategoryDTO result = convertToDTO(parentOpt.get());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting category by parent slug: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy danh mục theo parent slug: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> searchCategories(String keyword, Integer parentId, Boolean isActive, int page,
            int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Categories> categories = repository.searchCategories(keyword, parentId, isActive, pageable);
            List<CategoryDTO> dtos = categories.stream()
                    .map(this::convertToDTO)
                    .collect(java.util.stream.Collectors.toList());

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, dtos),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error searching categories: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi tìm kiếm danh mục: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Thống kê danh mục
    public ResponseEntity<ResponseObject> getCategoryStats(Integer categoryId) {
        try {
            Optional<Categories> categoryOpt = repository.findById(categoryId);
            if (!categoryOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục với ID: " + categoryId, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            // TODO: Implement category stats logic
            // - Count products in category
            // - Count child categories
            // - Count total products in tree
            // - Get last product added date

            return new ResponseEntity<>(
                    new ResponseObject(200, "Thành công", 0, "Stats data"),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting category stats: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi lấy thống kê danh mục: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật thứ tự hiển thị
    public ResponseEntity<ResponseObject> updateDisplayOrder(List<Integer> categoryIds) {
        try {
            for (int i = 0; i < categoryIds.size(); i++) {
                Optional<Categories> categoryOpt = repository.findById(categoryIds.get(i));
                if (categoryOpt.isPresent()) {
                    Categories category = categoryOpt.get();
                    category.setDisplayOrder(i + 1);
                    repository.save(category);
                }
            }

            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thứ tự thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating display order: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi cập nhật thứ tự: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Kích hoạt/vô hiệu hóa danh mục
    public ResponseEntity<ResponseObject> toggleCategoryStatus(Integer categoryId) {
        try {
            Optional<Categories> categoryOpt = repository.findById(categoryId);
            if (!categoryOpt.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseObject(404, "Không tìm thấy danh mục với ID: " + categoryId, 0, null),
                        HttpStatus.NOT_FOUND);
            }

            Categories category = categoryOpt.get();
            category.setIsActive(!category.getIsActive());
            repository.save(category);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật trạng thái thành công", 0, category),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error toggling category status: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Lỗi khi cập nhật trạng thái: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<CategoryDTO> buildCategoryHierarchy(List<Categories> allCategories) {
        Map<Integer, CategoryDTO> categoryMap = new HashMap<>();
        List<CategoryDTO> rootCategories = new ArrayList<>();

        for (Categories category : allCategories) {
            CategoryDTO dto = convertToDTO(category);
            categoryMap.put(category.getId(), dto);
        }

        for (Categories category : allCategories) {
            CategoryDTO dto = categoryMap.get(category.getId());
            if (category.getParent() == null) {
                rootCategories.add(dto);
            } else {
                CategoryDTO parent = categoryMap.get(category.getParent());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return rootCategories;
    }

    private List<CategoryDTO> buildBreadcrumb(Categories category) {
        List<CategoryDTO> breadcrumb = new ArrayList<>();
        Categories current = category;

        while (current != null) {
            breadcrumb.add(0, convertToDTO(current)); // Add to beginning
            if (current.getParent() != null) {
                Optional<Categories> parentOpt = repository.findById(current.getParent());
                current = parentOpt.orElse(null);
            } else {
                current = null;
            }
        }

        return breadcrumb;
    }

    private CategoryDTO convertToDTO(Categories category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setParentId(category.getParent());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}
