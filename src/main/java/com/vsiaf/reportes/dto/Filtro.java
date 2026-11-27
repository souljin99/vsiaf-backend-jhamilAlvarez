package com.vsiaf.reportes.dto;

import java.time.LocalDate;

/** Filtros opcionales. 'corte' es la fecha a la que se calcula la depreciacion (por defecto, hoy). */
public record Filtro(Integer codOfic, Integer codGrupo, Integer codResp, String estado,
                     LocalDate desde, LocalDate hasta, LocalDate corte) {
}
