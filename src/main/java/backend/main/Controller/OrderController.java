package backend.main.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.ResponseObject;
import backend.main.Service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getMethodName() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMethodNameByID(@PathVariable String id) {
        Integer idorder = Integer.parseInt(id);
        return orderService.findObjectByID(idorder);
    }
}
