package backend.main.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.DTO.ProductSpecificationDTO;
import backend.main.Model.ProductSpecification;
import backend.main.Model.ResponseObject;
import backend.main.Model.Specification;
import backend.main.Model.Unit;
import backend.main.Repository.ProductSpecificationRepository;
import backend.main.Repository.SpecificationRepository;
import backend.main.Repository.UnitRepository;

@Service
public class SpecificationService implements BaseService<Specification, Integer> {
    private final ProductSpecificationRepository productSpecificationRepository;
    private final UnitRepository unitRepository;
    private final SpecificationRepository specificationRepository;
    private final Set<String> noUnitSpecs = Set.of("camera", "camerabehind", "chip", "operation", "refreshrate");

    public SpecificationService(ProductSpecificationRepository productSpecificationRepository,
            UnitRepository unitRepository, SpecificationRepository specificationService) {
        this.productSpecificationRepository = productSpecificationRepository;
        this.unitRepository = unitRepository;
        this.specificationRepository = specificationService;
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Specification entity) {
        // TODO Auto-generated method stub
        try {
            if (entity.getName() == null || entity.getName().isEmpty())
                return new ResponseEntity<>(new ResponseObject(500, "Có lỗi sảy ra!!!", 0, entity),
                        HttpStatus.OK);
            Specification result = specificationRepository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200, "Tạo thành công", 0, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(new ResponseObject(500, "Có lỗi sảy ra!!!", 500, e.getMessage()),
                    HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Specification entity) {
        // TODO Auto-generated method stub
        return null;
    }

    public ProductSpecificationDTO createorupdatespe(ProductSpecificationDTO dto) {
        Logger logger = LoggerFactory.getLogger(SpecificationService.class);
        logger.info("🔄 CREATE/UPDATE SPEC - Product: {}, Spec: {}, Label: {}, Unit: {}",
                dto.getProductId(), dto.getValue(), dto.getLabel(), dto.getUnitName());
        dto.setValue(dto.getValue().trim().toLowerCase());
        try {
            // 1. Tìm hoặc tạo Specification trước
            Specification spec = findOrCreateSpecification(dto);
            if (spec == null) {
                logger.error("❌ Cannot find or create specification");
                return null;
            }

            logger.info("✅ Specification resolved - ID: {}, Name: {}, UnitId: {}",
                    spec.getId(), spec.getName(), spec.getUnitId());

            // 2. Tìm hoặc tạo ProductSpecification
            ProductSpecification dbSpecification = findOrCreateProductSpecification(dto, spec);
            if (dbSpecification == null) {
                logger.error("❌ Cannot find or create product specification");
                return null;
            }

            // 3. Cập nhật giá trị
            dbSpecification.setValue(dto.getLabel());
            dbSpecification.setSpec_id(spec.getId());

            // 4. Lưu và trả về kết quả
            ProductSpecification resultsave = productSpecificationRepository.save(dbSpecification);
            if (resultsave != null) {
                return createResponseDTO(resultsave, spec);
            } else {
                logger.error("❌ Save failed");
                return null;
            }

        } catch (Exception e) {
            logger.error("❌ Error in createorupdatespe: {}", e.getMessage(), e);
            return null;
        }
    }

    private Specification findOrCreateSpecification(ProductSpecificationDTO dto) {
        Logger logger = LoggerFactory.getLogger(SpecificationService.class);

        // Danh sách các specification không cần unit
        Set<String> noUnitSpecs = Set.of("camera", "camerabehind", "chip", "operation",
                "color", "brand", "model", "material");

        String specName = dto.getValue().toLowerCase().trim();
        boolean isNoUnit = noUnitSpecs.contains(specName);

        // Nếu có specId, tìm specification hiện có
        if (dto.getSpecId() != null) {
            Optional<Specification> specOpt = specificationRepository.findById(dto.getSpecId());
            if (specOpt.isPresent()) {
                logger.info("✅ Found existing specification by ID: {}", dto.getSpecId());
                return specOpt.get();
            }
        }

        // Tìm specification theo name và unit
        Specification spec = findSpecificationByNameAndUnit(specName, dto.getUnitName(), isNoUnit);
        if (spec != null) {
            logger.info("✅ Found existing specification by name and unit: {}", specName);
            return spec;
        }

        // Tạo specification mới
        return createNewSpecification(specName, dto.getUnitName(), isNoUnit);
    }

    private Specification findSpecificationByNameAndUnit(String specName, String unitName, boolean isNoUnit) {
        List<Specification> specs = specificationRepository.findByName(specName);

        if (isNoUnit) {
            // Tìm specification không có unit
            return specs.stream()
                    .filter(s -> s.getUnitId() == null)
                    .findFirst()
                    .orElse(null);
        } else {
            // Tìm specification có unit phù hợp
            Integer targetUnitId = findOrCreateUnitId(unitName);
            return specs.stream()
                    .filter(s -> Objects.equals(s.getUnitId(), targetUnitId))
                    .findFirst()
                    .orElse(null);
        }
    }

    private Integer findOrCreateUnitId(String unitName) {
        if (unitName == null || unitName.trim().isEmpty()) {
            return null;
        }

        Optional<Unit> unitOpt = unitRepository.findByValue(unitName.trim());
        if (unitOpt.isPresent()) {
            return unitOpt.get().getKey();
        } else {
            // Tạo unit mới
            Unit newUnit = new Unit();
            newUnit.setValue(unitName.trim());
            Unit savedUnit = unitRepository.save(newUnit);
            return savedUnit.getKey();
        }
    }

    private Specification createNewSpecification(String specName, String unitName, boolean isNoUnit) {
        Logger logger = LoggerFactory.getLogger(SpecificationService.class);

        Specification newSpec = new Specification();
        newSpec.setName(specName);

        if (isNoUnit) {
            newSpec.setUnitId(null);
        } else {
            newSpec.setUnitId(findOrCreateUnitId(unitName));
        }
        Specification savedSpec = specificationRepository.save(newSpec);
        logger.info("➕ Created new specification: {} (UnitId: {})", specName, savedSpec.getUnitId());
        return savedSpec;
    }

    private ProductSpecification findOrCreateProductSpecification(ProductSpecificationDTO dto, Specification spec) {
        // Tìm ProductSpecification hiện có
        if (dto.getSpecId() != null) {
            Optional<ProductSpecification> optionalDbSpec = productSpecificationRepository
                    .findByProductIdAndSpecId(dto.getProductId(), dto.getSpecId());
            if (optionalDbSpec.isPresent()) {
                return optionalDbSpec.get();
            }
        }

        // Tìm bằng spec mới
        Optional<ProductSpecification> optionalDbSpec = productSpecificationRepository
                .findByProductIdAndSpecId(dto.getProductId(), spec.getId());
        if (optionalDbSpec.isPresent()) {
            return optionalDbSpec.get();
        }

        // Tạo mới
        ProductSpecification newSpec = new ProductSpecification();
        newSpec.setProduct_id(dto.getProductId());
        return newSpec;
    }

    private ProductSpecificationDTO createResponseDTO(ProductSpecification productSpec, Specification specification) {
        String unitValue = null;

        // Lấy unit name nếu có
        if (specification.getUnitId() != null) {
            Optional<Unit> unitOpt = unitRepository.findById(specification.getUnitId());
            if (unitOpt.isPresent()) {
                unitValue = unitOpt.get().getValue();
            }
        }

        return new ProductSpecificationDTO(
                productSpec.getProduct_id(),
                specification.getId(),
                unitValue,
                productSpec.getValue(),
                specification.getName());
    }

}
