package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Activo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ActivoRepository extends JpaRepository<Activo, Long> {

    @Query("select a from Activo a join fetch a.grupo join fetch a.oficina join fetch a.responsable")
    List<Activo> findAllConRelaciones();
}
