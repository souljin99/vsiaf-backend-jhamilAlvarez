package com.vsiaf.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumenGeneral(String entidad, String unidad, LocalDate fechaCorte, long cantidad,
                             BigDecimal costoTotal, BigDecimal depreciacionAcumulada, BigDecimal valorNeto) {
}
