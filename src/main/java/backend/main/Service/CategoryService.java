package backend.main.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final Logger logger = LoggerE.logger;
    @Autowired
    private CategoryRepository repository;

    public ResponseEntity<ResponseObject> findAll() {
        List<CategoryDTO> listroots = new ArrayList<>();
        List<Categories> roots = repository.findAll();
        roots.forEach(item -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setDisplayOrder(item.getDisplayOrder());
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
                listroots.forEach(rootParent -> {
                    if (rootParent.getId() == dto.getParentId()) {
                        rootParent.getParents().add(dto);
                    }
                });
            }
        });
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, listroots),
                HttpStatus.OK);
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

            logger.info("Save Successfully : Id: " + saved.getId() + "Name: " + saved.getName());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Save Failed  : " + "Name: " + saved.getName());
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
            logger.info("Delete Successfully ID: " + id);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Delete Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
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
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + entity.getId(), 1, null),
                    HttpStatus.NOT_FOUND);
        }
        try {
            Categories updated = repository.save(entity);
            logger.info("Update Successfully: " + updated.toString());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành công", 0, updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Update Exception: " + e.getMessage());
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
                    logger.info("Delete Successfully ID: " + id);
                } else {
                    logger.warning("ID not found: " + id);
                }
            }
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Delete Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
