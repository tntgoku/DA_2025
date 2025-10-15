
package backend.main.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.DTO.ProductSpecificationDTO;
import backend.main.Model.ResponseObject;
import backend.main.Request.Json.ProductJson;
import backend.main.Service.SpecificationService;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/specification")
public class SpecificationController {

    private final SpecificationService specificationService;
    // private final Logger logger = LoggerE.logger;

    public SpecificationController(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    /**
     * Lấy danh sách các unit có thể có cho specification
     */
    // @GetMapping("/possible-units/{specName}")
    // public ResponseEntity<List<String>> getPossibleUnits(@PathVariable String
    // specName) {
    // Map<String, List<String>> possibleUnits = Map.of(
    // "ram", Arrays.asList("GB", "TB", "MB"),
    // "storage", Arrays.asList("GB", "TB", "MB"),
    // "weight", Arrays.asList("gram", "kg", "g", "lb"),
    // "battery", Arrays.asList("mAh", "Ah", "Wh"),
    // "screen", Arrays.asList("inch", "cm"),
    // "refreshrate", Arrays.asList("Hz", "MHz"));

    // List<String> units = possibleUnits.getOrDefault(specName.toLowerCase(), new
    // ArrayList<>());
    // return ResponseEntity.ok(units);
    // }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseObject> getPossibleUnits(@PathVariable Integer id,
            @RequestBody ProductJson data) {

        Logger logger = LoggerFactory.getLogger(SpecificationController.class);

        Map<String, Object> response = new HashMap<>();
        data.getSpecifications().forEach(item -> {
            logger.info(item.toString());
        });
        List<ProductSpecificationDTO> listnew = data.getSpecifications().stream().map(item -> {
            return specificationService.createorupdatespe(item);
        }).toList();
        response.put("New", listnew);
        return new ResponseEntity<>(new ResponseObject(200, "Oke", 1, response), HttpStatus.OK);
    }
}