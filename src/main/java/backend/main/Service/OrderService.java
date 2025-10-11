package backend.main.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.ResponseObject;
import backend.main.Model.Order.Order;
import backend.main.Repository.OrderRepository;

@Service
public class OrderService implements BaseService<Order, Integer> {
    @Autowired
    private OrderRepository repository;

    public ResponseEntity<ResponseObject> findAll() {
        List<Order> listitem = repository.findAll();
        if (listitem == null || listitem.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(204,
                    "Không tìm thấy", 0, null),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, repository.findAll()),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findObjectByID(Integer id) {
        Optional<Order> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(404,
                    "Không thành công", 0, "not Found source have ID: " + id),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, repository.findById(id)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Order entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Order entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
