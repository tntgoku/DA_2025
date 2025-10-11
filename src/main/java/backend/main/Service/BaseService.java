package backend.main.Service;

import org.springframework.http.ResponseEntity;

import backend.main.Model.ResponseObject;

public interface BaseService<E, ID> {
    ResponseEntity<ResponseObject> createNew(E entity);

    ResponseEntity<ResponseObject> update(E entity);

    ResponseEntity<ResponseObject> delete(ID id);
}
