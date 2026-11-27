package com.vsiaf.reportes.repository;

import com.vsiaf.reportes.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findAllByOrderByCodGrupoAsc();
    Optional<Grupo> findByCodGrupo(Integer codGrupo);
}
