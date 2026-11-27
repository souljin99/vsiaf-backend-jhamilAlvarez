package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findAllByOrderByCodGrupoAsc();
}
