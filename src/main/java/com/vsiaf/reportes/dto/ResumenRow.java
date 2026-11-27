package com.vsiaf.reportes.dto;

import java.math.BigDecimal;

public record ResumenRow(Integer codigo, String nombre, long cantidad,
                         BigDecimal costoTotal, BigDecimal depreciacionAcumulada, BigDecimal valorNeto) {
}
