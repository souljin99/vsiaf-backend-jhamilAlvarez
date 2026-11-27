package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
    List<Responsable> findAllByOrderByCodRespAsc();
}
