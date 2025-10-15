package backend.main.Service.ServiceImp.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import backend.main.DTO.ProductSpecificationDTO;
import backend.main.Model.ProductSpecification;
import backend.main.Model.Specification;
import backend.main.Model.Unit;
import backend.main.Repository.ProductSpecificationRepository;
import backend.main.Repository.SpecificationRepository;
import backend.main.Repository.UnitRepository;

@Service
public class ProductSpecificationService {
    private final ProductSpecificationRepository pRepository;
    private final SpecificationRepository sRepository;
    private final UnitRepository uRepository;

    public ProductSpecificationService(ProductSpecificationRepository pRepository, SpecificationRepository sRepository,
            UnitRepository uRepository) {
        this.pRepository = pRepository;
        this.sRepository = sRepository;
        this.uRepository = uRepository;
    }

    // Lấy đưa về List DTO
    public List<ProductSpecificationDTO> findAllByProductId(Integer id) {
        List<ProductSpecificationDTO> listnew = new ArrayList<>();
        List<ProductSpecification> dbmodel = pRepository.findByProductId(id);
        if (dbmodel != null) {
            dbmodel.forEach(item -> {
                Specification specification = findSpecificationData(item.getSpec_id());
                if (specification != null) {
                    Unit iUnit = findUnit(specification.getUnitId());
                    listnew.add(new ProductSpecificationDTO(item.getProduct_id(), item.getSpec_id(), iUnit.getValue(),
                            item.getValue(), specification.getName()));
                }
            });
        }
        return null;
    }

    Specification findSpecificationData(Integer id) {
        try {
            Specification specification = sRepository.findById(id).get();
            return specification;
        } catch (Exception e) {
            // TODO: handle exception
            Logger.getLogger(ProductSpecificationService.class.getName()).info("Error: " + e.getMessage());
            return null;
        }
    }

    Unit findUnit(Integer id) {
        try {
            Unit specification = uRepository.findById(id).get();
            return specification;
        } catch (Exception e) {
            // TODO: handle exception
            Logger.getLogger(ProductSpecificationService.class.getName()).info("Error: " + e.getMessage());
            return null;
        }
    }
}
