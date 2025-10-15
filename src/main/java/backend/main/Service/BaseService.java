package backend.main.Service;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;

import backend.main.Model.BaseEntity;
import backend.main.Model.ResponseObject;

public interface BaseService<E extends BaseEntity, ID extends Serializable> {
    ResponseEntity<ResponseObject> createNew(E entity);

    ResponseEntity<ResponseObject> update(E entity);

    ResponseEntity<ResponseObject> delete(ID id);
}
