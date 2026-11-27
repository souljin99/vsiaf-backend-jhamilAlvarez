package com.vsiaf.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ActivoRow(String codActivo, String descripcion,
                        Integer codGrupo, String grupo,
                        Integer codOfic, String oficina,
                        Integer codResp, String responsable,
                        LocalDate fechaIncorporacion, String estado,
                        BigDecimal costo, Integer vidaUtil,
                        BigDecimal depreciacionAcumulada, BigDecimal valorNeto) {
}
