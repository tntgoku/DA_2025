package backend.main.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import backend.main.Model.Promotion.Voucher;
import java.util.List;


@Repository
public interface VoucherRepository  extends BaseRepository<Voucher, Integer>{
    List<Voucher> findByCode(String code);
    List<Voucher> findByIsActive(Boolean isActive);
    
}
