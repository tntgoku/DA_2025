package backend.main.Controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.Model.Categories;
import backend.main.Model.ResponseObject;
import backend.main.Request.CateRequest;
import backend.main.Service.CategoryService;
import java.util.*;

@RestController
@RequestMapping("/api/cate")
public class CategoryController {
    @Autowired
    private CategoryService service;
    private final Logger logger = LoggerE.logger;

    @GetMapping
    public ResponseEntity<ResponseObject> getproductall() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/slug/{id}")
    public ResponseEntity<ResponseObject> getCategoryBySlug(@PathVariable String id) {
        return service.getBySlug(id);
    }
    
    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CateRequest data) {
        Categories a = new Categories();
        logger.info("Post Client: " + data);
        a.setName(data.getName());
        a.setIsActive(data.getActive());
        a.setParent(data.getParentId());
        a.setDisplayOrder(data.getDisplayOrder());
        logger.info("Convenrt Object Client Post: " + a);
        return service.createNew(a);
        // return new ResponseEntity<>(new ResponseObject(200, "data", 0, data),
        // HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable String id,
            @RequestBody CateRequest data) {

        Categories a = new Categories();
        logger.info("Post Client: " + data);
        a.setName(data.getName());
        a.setIsActive(data.getActive());
        a.setParent(data.getParentId());
        a.setDisplayOrder(data.getDisplayOrder());
        a.setId(Integer.parseInt(id));
        a.setSlug(data.getSlug());
        return service.update(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id) {
        return service.delete(id);
    }

    @DeleteMapping("/removemultiple")
    public ResponseEntity<ResponseObject> deleteCategories(@RequestBody HashMap<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");

        return service.deletes(ids);
    }
}
