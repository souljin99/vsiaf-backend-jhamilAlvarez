package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OficinaRepository extends JpaRepository<Oficina, Long> {
    List<Oficina> findAllByOrderByCodOficAsc();
    Optional<Oficina> findByCodOfic(Integer codOfic);
}
