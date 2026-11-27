package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Activo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivoRepository extends JpaRepository<Activo, Long> {

    @Query("select a from Activo a join fetch a.grupo join fetch a.oficina join fetch a.responsable")
    List<Activo> findAllConRelaciones();

    @Query("select a from Activo a join fetch a.grupo join fetch a.oficina join fetch a.responsable where a.codActivo = :cod")
    Optional<Activo> findByCodActivoConRelaciones(@Param("cod") String cod);
}
